package com.ekc.swiperight.data.provider;

import android.support.annotation.NonNull;
import com.ekc.swiperight.dagger.qualifier.PerApp;
import com.ekc.swiperight.dagger.qualifier.Qualifiers.Token;
import com.ekc.swiperight.data.TinderApiClient;
import com.ekc.swiperight.data.pref.StringPreference;
import com.ekc.swiperight.data.provider.DataObserver.RecommendationObserver;
import com.ekc.swiperight.model.Recommendation;
import com.ekc.swiperight.util.RxUtil;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;

@PerApp
public class RecommendationProvider extends DataProvider<RecommendationObserver> {
  private static final RecommendationObserver EMPTY_OBSERVER = new EmptyRecommendationObserver();

  private final TinderApiClient api;
  private final StringPreference token;
  private final List<Recommendation> cache = new ArrayList<>();

  private Subscription subscription;
  @NonNull private RecommendationObserver observer = EMPTY_OBSERVER;

  @Inject RecommendationProvider(TinderApiClient api, @Token StringPreference token) {
    super(RecommendationObserver.class);
    this.api = api;
    this.token = token;
  }

  @Override void subscribe(@NonNull RecommendationObserver observer) {
    this.observer = observer;
  }

  @Override void unsubscribe(@NonNull RecommendationObserver observer) {
    this.observer = EMPTY_OBSERVER;
  }

  void recommendations() {
    if (!getRecommendationCache()) {
      if (!RxUtil.inFlight(subscription)) {
        subscription = api.recs(token.get())
            .subscribe(
                response -> {
                  if (response.recsExhausted()) {
                    observer.onGetRecommendationsFailure(new RecExhaustedException());
                  } else {
                    cache.clear();
                    cache.addAll(response.getResults());
                    observer.onGetRecommendationsSuccess(response.getResults());
                  }
                },
                observer::onGetRecommendationsFailure
            );
      }
    }
  }

  private boolean getRecommendationCache() {
    List<Recommendation> copy = new ArrayList<>();
    if (!cache.isEmpty()) {
      copy.addAll(cache);
      observer.onGetRecommendationsSuccess(copy);
      return true;
    }
    return false;
  }

  void invalidateCache() {
    cache.clear();
  }

  static final class EmptyRecommendationObserver implements RecommendationObserver {
    @Override public void onGetRecommendationsSuccess(List<Recommendation> results) {

    }

    @Override public void onGetRecommendationsFailure(Throwable error) {

    }
  }

  public static final class RecExhaustedException extends Exception {
    @Override public String getMessage() {
      return "There's no one new around you.";
    }
  }
}