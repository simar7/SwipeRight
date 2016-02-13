package com.ekc.swiperight.data.provider;

import android.support.annotation.NonNull;
import com.ekc.swiperight.dagger.qualifier.PerApp;
import com.ekc.swiperight.dagger.qualifier.Qualifiers.Token;
import com.ekc.swiperight.data.TinderApiClient;
import com.ekc.swiperight.data.pref.StringPreference;
import com.ekc.swiperight.data.provider.DataObserver.LikeObserver;
import com.ekc.swiperight.model.Match;
import com.ekc.swiperight.model.Recommendation;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;

@PerApp
public class LikeProvider extends DataProvider<LikeObserver> {
  private static final long BASE_DELAY = 500;
  private static final double VARIANCE = 0.2d;
  private static final LikeObserver EMPTY_OBSERVER = new EmptyLikeObserver();

  private final Random random = new Random();
  private final TinderApiClient api;
  private final StringPreference token;

  @NonNull private LikeObserver observer = EMPTY_OBSERVER;

  @Inject LikeProvider(TinderApiClient api, @Token StringPreference token) {
    super(LikeObserver.class);
    this.api = api;
    this.token = token;
  }

  @Override void subscribe(@NonNull LikeObserver observer) {
    this.observer = observer;
  }

  @Override void unsubscribe(@NonNull LikeObserver observer) {
    this.observer = EMPTY_OBSERVER;
  }

  void like(Recommendation recommendation) {
    api.like(token.get(), recommendation.id())
        .delay(getRandomDelay(), TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            match -> {
              match.setRecommendation(recommendation);
              observer.onLikeSuccess(match);
            },
            observer::onLikeFailure
        );
  }

  /**
   * Return a delay of 500 ms +/- 20% (100 ms) to add variance to API calls
   *
   * @return delay in milliseconds
   */
  private long getRandomDelay() {
    return BASE_DELAY + (long) (random.nextGaussian() * VARIANCE * BASE_DELAY);
  }

  static final class EmptyLikeObserver implements LikeObserver {
    @Override public void onLikeSuccess(Match match) {

    }

    @Override public void onLikeFailure(Throwable error) {

    }
  }
}
