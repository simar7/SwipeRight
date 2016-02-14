package com.ekc.swiperight.ui.main;

import android.content.res.Resources;
import com.ekc.swiperight.R;
import com.ekc.swiperight.dagger.qualifier.PerActivity;
import com.ekc.swiperight.data.provider.DataManager;
import com.ekc.swiperight.data.provider.DataObserver.AuthObserver;
import com.ekc.swiperight.data.provider.DataObserver.LikeObserver;
import com.ekc.swiperight.data.provider.DataObserver.RecommendationObserver;
import com.ekc.swiperight.model.Match;
import com.ekc.swiperight.model.Recommendation;
import com.ekc.swiperight.ui.base.BasePresenter;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import java.util.List;
import javax.inject.Inject;
import retrofit.HttpException;
import timber.log.Timber;

@PerActivity
public class MainPresenter extends BasePresenter<MainView> implements
    RecommendationObserver,
    LikeObserver,
    AuthObserver,
    FacebookCallback<LoginResult> {
  private final DataManager dataManager;
  private final Resources res;

  @Inject MainPresenter(DataManager dataManager, Resources res) {
    this.dataManager = dataManager;
    this.res = res;
  }

  @Override public void initialize() {
    dataManager.subscribe(this);
  }

  @Override public void destroy() {
    dataManager.unsubscribe(this);
  }

  public void auth() {
    setEmptyViewIfNull();
    view.showLoading();
    view.hideErrorViews();
    // After successful authentication, request recommendations
    dataManager.auth();
  }

  public void getRecommendations() {
    setEmptyViewIfNull();
    view.showLoading();
    view.hideErrorViews();
    dataManager.recommendations();
  }

  public void like(Recommendation recommendation) {
    setEmptyViewIfNull();
    view.showLoading();
    view.hideErrorViews();
    dataManager.like(recommendation);
  }

  public void likeAll(Recommendation start) {
    setEmptyViewIfNull();
    view.showLoading();
    view.hideErrorViews();
    dataManager.likeAll(start);
  }

  @Override public void onAuthSuccess() {
    getRecommendations();
  }

  @Override public void onAuthFailure(Throwable error) {
    setEmptyViewIfNull();
    view.hideLoading();
    view.showAuthError();
  }

  @Override public void onGetRecommendationsSuccess(List<Recommendation> results) {
    setEmptyViewIfNull();
    view.hideLoading();
    view.hideErrorViews();
    // Whacky error handling but... there are no error codes when you hit your rate limit :|
    if (!results.isEmpty() && results.get(0).id().contains("tinder_rate_limited")) {
      view.showLimitReached();
      view.failure(res.getString(R.string.like_limit));
    } else {
      view.loadRecommendations(results);
    }
  }

  @Override public void onGetRecommendationsFailure(Throwable error) {
    setEmptyViewIfNull();
    view.hideLoading();
    view.failure(error.getMessage());
    if (error instanceof HttpException && ((HttpException) error).code() == 401) {
      view.showAuthError();
    } else {
      view.showConnectionError();
    }
  }

  @Override public void onLikeSuccess(Match match) {
    setEmptyViewIfNull();

    Timber.d(
        "Liked %s, user Id %s, match? %s",
        match.recommendation().name(),
        match.recommendation().id(),
        match != Match.NO_MATCH);

    view.hideLoading();
    view.likeResponse(match, dataManager.isLikeAllInProgress());
  }

  @Override public void onLikeFailure(Throwable error) {
    setEmptyViewIfNull();
    view.hideLoading();
    dataManager.setLikeAllInProgress(false);
    Timber.e(error, error.getMessage());
    if (error instanceof NullPointerException) {
      view.showLimitReached();
      view.failure(res.getString(R.string.like_limit));
    } else {
      view.failure(error.getMessage());
    }
  }

  @Override public void onSuccess(LoginResult loginResult) {
    setEmptyViewIfNull();
    view.showLoading();
    view.hideErrorViews();
    auth();
  }

  @Override public void onCancel() {
    setEmptyViewIfNull();
    view.hideLoading();
    view.showAuthError();
  }

  @Override public void onError(FacebookException error) {
    setEmptyViewIfNull();
    view.hideLoading();
    view.showAuthError();
  }

  private void setEmptyViewIfNull() {
    if (view == null) {
      view = MainView.EMPTY;
    }
  }

  public void invalidateCache() {
    dataManager.invalidateCache();
  }

  public void refresh() {
    dataManager.setLikeAllInProgress(false);
    invalidateCache();
    getRecommendations();
  }
}
