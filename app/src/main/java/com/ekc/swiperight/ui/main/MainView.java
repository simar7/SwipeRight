package com.ekc.swiperight.ui.main;

import com.ekc.swiperight.model.Match;
import com.ekc.swiperight.model.Recommendation;
import com.ekc.swiperight.ui.base.BaseView;
import java.util.List;

public interface MainView extends BaseView {
  MainView EMPTY = new MainView() {
    @Override public void loadRecommendations(List<Recommendation> results) {

    }

    @Override public void failure(String errorMessage) {

    }

    @Override public void showLoading() {

    }

    @Override public void hideLoading() {

    }

    @Override public void likeResponse(Match match) {

    }

    @Override public void showAuthError() {

    }

    @Override public void hideAuthError() {

    }

    @Override public void hideErrorViews() {

    }

    @Override public void showLimitReached() {

    }

    @Override public void hideLimitReached() {

    }

    @Override public void showConnectionError() {

    }

    @Override public void hideConnectionError() {

    }
  };

  void loadRecommendations(List<Recommendation> results);

  void failure(String errorMessage);

  void showLoading();

  void hideLoading();

  void likeResponse(Match match);

  void showAuthError();

  void hideAuthError();

  void hideErrorViews();

  void showLimitReached();

  void hideLimitReached();

  void showConnectionError();

  void hideConnectionError();
}
