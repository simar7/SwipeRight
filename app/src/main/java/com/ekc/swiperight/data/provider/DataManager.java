package com.ekc.swiperight.data.provider;

import android.app.Application;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import com.ekc.swiperight.R;
import com.ekc.swiperight.dagger.qualifier.PerApp;
import com.ekc.swiperight.model.Recommendation;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

@PerApp
public class DataManager {
  private final Observable<DataProvider> dataProviders;
  private final AccessTokenTracker tokenTracker;

  private boolean likeAllInProgress;

  @Inject DataManager(@NonNull List<DataProvider> providers, Application app) {
    dataProviders = Observable.from(providers);
    this.tokenTracker = new FbTokenTracker(app);
    this.tokenTracker.startTracking();
  }

  public void subscribe(DataObserver observer) {
    dataProviders.filter(observer::canObserve)
        .subscribe(provider -> provider.subscribe(observer));
  }

  public void unsubscribe(DataObserver observer) {
    dataProviders.filter(observer::canObserve)
        .subscribe(provider -> provider.unsubscribe(observer));
  }

  /**
   * Start a likeAll chain. Repeated calls will drop until the chain finishes.
   *
   * @param start the recommendation to start with
   */
  public void likeAll(Recommendation start) {
    if (!likeAllInProgress) {
      likeAllInProgress = true;
      like(start);
    }
  }

  public void like(Recommendation recommendation) {
    likeProvider().subscribe(provider -> provider.like(recommendation));
  }

  public void recommendations() {
    recommendationProvider().subscribe(provider -> provider.recommendations());
  }

  public void auth() {
    authProvider().subscribe(AuthProvider::auth);
  }

  public void invalidateCache() {
    invalidateRecommendationsCache();
  }

  public void invalidateRecommendationsCache() {
    recommendationProvider().subscribe(RecommendationProvider::invalidateCache);
  }

  public boolean isLikeAllInProgress() {
    return likeAllInProgress;
  }

  public void setLikeAllInProgress(boolean likeAllInProgress) {
    this.likeAllInProgress = likeAllInProgress;
  }

  @NonNull private Observable<LikeProvider> likeProvider() {
    return provider(LikeProvider.class);
  }

  @NonNull private Observable<AuthProvider> authProvider() {
    return provider(AuthProvider.class);
  }

  @NonNull private Observable<RecommendationProvider> recommendationProvider() {
    return provider(RecommendationProvider.class);
  }

  @NonNull private <T extends DataProvider> Observable<T> provider(Class<T> clazz) {
    return dataProviders.filter(clazz::isInstance).map(clazz::cast);
  }

  class FbTokenTracker extends AccessTokenTracker {
    private final Application app;

    FbTokenTracker(Application app) {
      this.app = app;
    }

    @Override protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
        AccessToken currentAccessToken) {
      if (currentAccessToken == null) {
        invalidateCache();
        PreferenceManager.getDefaultSharedPreferences(app).edit().clear().commit();
        PreferenceManager.setDefaultValues(app, R.xml.preferences, true);
      }
    }
  }
}