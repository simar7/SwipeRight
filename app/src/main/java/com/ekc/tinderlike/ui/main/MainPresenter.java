package com.ekc.tinderlike.ui.main;

import com.ekc.tinderlike.dagger.qualifier.Qualifiers.Token;
import com.ekc.tinderlike.data.StringPreference;
import com.ekc.tinderlike.data.TinderApi;
import com.ekc.tinderlike.model.LikeResponse;
import com.ekc.tinderlike.model.Recommendation;
import com.ekc.tinderlike.model.RecommendationResponse;
import com.ekc.tinderlike.ui.base.BasePresenter;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;

public class MainPresenter extends BasePresenter<MainView> {
  // TODO pull token from fb login
  public static final long BASE_DELAY = 500;
  public static final double VARIANCE = 0.2d;

  final TinderApi api;
  Subscription subscription;
  Subscription likeSubscription;
  final StringPreference token;
  final BehaviorSubject<Recommendation> recommendationSubject;
  final Random random = new Random();

  public MainPresenter(TinderApi api, BehaviorSubject<Recommendation> recommendationSubject,
      @Token StringPreference token) {

    this.api = api;
    this.token = token;
    this.recommendationSubject = recommendationSubject;
    this.recommendationSubject.delay(getDelay(), TimeUnit.MILLISECONDS).subscribe(this::like);
  }

  private void like(Recommendation recommendation) {
    view.showLoading();
    likeSubscription = api.like(token.get(), recommendation.id())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map(LikeResponse::match)
        .subscribe(match -> {
          view.hideLoading();
          view.likeResponse(recommendation, match);
        }, error -> {
          view.hideLoading();
          view.failure(error.getMessage());
          Timber.e(error, error.getMessage());
        });
  }

  private long getDelay() {
    return BASE_DELAY + (long) (random.nextGaussian() * VARIANCE);
  }

  @Override protected void initialize() {

  }

  @Override protected void destroy() {
    if (subscription != null) {
      subscription.unsubscribe();
    }
    if (likeSubscription != null) {
      likeSubscription.unsubscribe();
    }
  }

  public void getRecommendations() {
    view.showLoading();
    subscription =
        api.recs(token.get()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .map(RecommendationResponse::getResults)
            .subscribe(results -> {
                  view.hideLoading();
                  view.loadResults(results);
                },
                error -> {
                  view.hideLoading();
                  view.failure(error.getMessage());
                  Timber.e(error, error.getMessage());
                });
  }


}
