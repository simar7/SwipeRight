package com.ekc.swiperight.data.provider;

import com.ekc.swiperight.model.Match;
import com.ekc.swiperight.model.Recommendation;
import java.util.List;

public interface DataObserver {
  interface RecommendationObserver extends DataObserver {
    void onGetRecommendationsSuccess(List<Recommendation> results);

    void onGetRecommendationsFailure(Throwable error);
  }

  interface LikeObserver extends DataObserver {
    void onLikeSuccess(Match match);

    void onLikeFailure(Throwable error);
  }

  interface AuthObserver extends DataObserver {
    void onAuthSuccess();

    void onAuthFailure(Throwable error);
  }
}
