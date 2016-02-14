SwipeRight
=======

A lightweight Tinder liking tool for Android. 

![](/swiperight_feature.png)

This has been an exercise in reverse engineering and software architecture exploration.

Download
--------
<a href="https://play.google.com/store/apps/details?id=com.ekc.swiperight&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge-border.png" height="60" width="185"/></a> 

SwipeRight is available [on Google Play][1] or clone the repo:
```
git clone https://github.com/ekchang/SwipeRight.git
```

You will need Java JDK 8 installed and Gradle Plugin 1.5+, as I use retrolambda and vector drawables gratuitously.


Use
--------

Login with Facebook (using the account you've connected with Tinder), then press the floating action button to start a like chain for everyone in the list. There is a 500 ms (+/- 20%) delay between calls.



Dependencies
--------

* Android Design Support Library
* ButterKnife
* Dagger 2
* Timber
* Retrofit
* RxJava
* Retrolambda
* LeakCanary
* AnnotatedAdapter
* Facebook SDK



Architecture
--------

I started exploring this architecture to solve two problems I had with some of my own MVP implementations:

* No worker/retain Fragments as the gimmicky workaround to retaining data through configuration change/rotate. I hate Fragments. I use them, but I hate them. 
* Presenters are given too much responsibility retrieving data (either network or storage), formatting, saving to state (which stemmed from my poor understanding of 'Model' in MVP)

The app follows MVP architecture with a DataManager abstraction layer that delegates network calls to subclasses of DataProvider. DataManager/Provider technically falls under 'Model' in the MVP scheme of things because it's business logic. 

Unlike other MVP implementations on Android I've seen, the focus was not to try and retain Presenter instances, but rather have them be resubscribed to data sources as needed.

This allows the app to execute a chain of API calls (with a small delay inbetween), rotate the screen/change configuration during the middle of the chain, and allow the UI and Presenter to continue receiving events. All without any retained Fragments and done solely through Java objects.

The relationship between DataManager, DataProvider, and DataObserver is worth discussing. 

* All presenters that want data (through storage or through the network) request it through the DataManager. Presenters should never access a DataProvider directly, and it is clear although all of the DataProvider implementations have public constructors (for Dagger purposes), they all have package local methods accessed by the DataManager. Let the data manager be the gateway.
* Each presenter implements a particular DataObserver it is interested in receiving. You shouldn't associate a single DataObserver to a single API call; realize that you can write DataProviders to make multiple API calls and format them to give to the presenter.
* DataProviders do the actual API call and will most likely need to be constructed with a Retrofit API client. 

The LikeProvider is the most straight forward data provider - you make one call, get one response, and no caching or validation either way. 

```java
public class LikeProvider extends DataProvider<LikeObserver> {
  @NonNull private LikeObserver observer = EMPTY_OBSERVER;

  LikeProvider(TinderApiClient api, ...) {
    this.api = api;
  }

  void subscribe(@NonNull LikeObserver observer) {
  // Can be replaced with a data structure if you want more than one observer
    this.observer = observer;
  }

  void unsubscribe(@NonNull LikeObserver observer) {
  // Can be replaced with a data structure if you want more than one observer
    this.observer = EMPTY_OBSERVER;
  }

  void like(Recommendation recommendation) {
    api.like(recommendation.id())
        .subscribe(
            observer::onLikeSuccess,
            observer::onLikeFailure
        );
  }
}
```

There's the flexibility of abstracting away whether or not your DataProvider manages multiple observers or just one. The presenter/DataObserver doesn't care if your DataProvider is also pushing data to other observers, it just wants to make sure it is receiving them too. The DataProvider can also include caching logic, as seen in RecommendationProvider:

```java
public class RecommendationProvider extends DataProvider<RecommendationObserver> {
  private final List<Recommendation> cache = new ArrayList<>();
  
  void recommendations() {
    if (!getRecommendationCache()) {
      api.recs(token.get())
        .subscribe(
            recs -> {
              cache.clear();
              cache.addAll(recs);
              observer.onGetRecommendationsSuccess(recs);
            },
            observer::onGetRecommendationsFailure
        );
    }
  }
  
  private boolean getRecommendationCache() {
    List<Recommendation> copy = new ArrayList<>();
      if (!cache.isEmpty()) {
        copy.addAll(cache);
        observer.onGetRecommendationsSuccess(copy); // Push a copy of the cache to the observer
        return true;
      }
      return false;
  }
}
```

When the presenter subscribes to the list of recommendations, the presenter doesn't care whether the data is cached or fresh. It just responds to `onGetRecommendationsSuccess` events to update data. In the above implementation, cached data is returned whenever possible unless forcibly cleared (through an `invalidateCache` method). If I decided I no longer wanted to cache or if I wanted to return a cached copy while still making the API call for fresh data, the implementation would be trivial and neither the UI nor Presenters have to be altered.

So if this boilerplate wrapper around your API client seems too bulky, perhaps the Presenter implementation will change your mind:

```java
public class MainPresenter extends BasePresenter<MainView> implements RecommendationObserver {
  public void initialize() {
    dataManager.subscribe(this);
  }

  public void destroy() {
    dataManager.unsubscribe(this);
  }

  public void onGetRecommendationsSuccess(List<Recommendation> results) {
    view.hideLoading();
    view.hideErrorViews();
    view.loadRecommendations(results);
  }

  public void onGetRecommendationsFailure(Throwable error) {
    view.hideLoading();
    view.failure(error.getMessage());
    if (error instanceof HttpException) {
      if ((HttpException) error).code() == 401) {
        view.showAuthError();
      } 
      ... // handle as many HTTP error codes as you wish here and update the view accordingly
    } else {
      view.showConnectionError();
    }
  }
}

``` 

The presenter's job is solely to a) subscribe to data events (determined by what kind of DataObserver it has implemented), and b) update the view when they get it. Done. #cleanpresenters

#### Drawbacks
* I'm not sure if debugging Observable streams is any better than FragmentManager stack traces.
* I worry that more diverse API calls and response types will lead to bloat for each DataProvider class you need to implement.
* The concept of managing multiple observers for a single provider (and how to juggle their subscriptions) was not explored yet.



License
--------

    Copyright 2016 Erick Chang.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

Legal
---------
Android, Google Play and the Google Play logo are trademarks of Google Inc.

 [1]: https://play.google.com/store/apps/details?id=com.ekc.swiperight
