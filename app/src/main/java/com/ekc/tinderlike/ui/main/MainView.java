package com.ekc.tinderlike.ui.main;

import com.ekc.tinderlike.model.Match;
import com.ekc.tinderlike.model.Recommendation;
import com.ekc.tinderlike.ui.base.BaseView;
import java.util.List;

public interface MainView extends BaseView {
  void loadResults(List<Recommendation> results);

  void failure(String errorMessage);

  void showLoading();

  void hideLoading();

  void likeResponse(Recommendation recommendation, Match match);

  void showAuthError();

  void hideAuthError();
}
