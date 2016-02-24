package com.ekc.swiperight.data;

import com.ekc.swiperight.model.AuthRequest;
import com.ekc.swiperight.model.AuthResponse;
import com.ekc.swiperight.model.LikeResponse;
import com.ekc.swiperight.model.Match;
import com.ekc.swiperight.model.Recommendation;
import com.ekc.swiperight.model.RecommendationResponse;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TinderApiClient {

  private final TinderApi api;

  public TinderApiClient(TinderApi api) {
    this.api = api;
  }

  public Observable<RecommendationResponse> recs(String token) {
    return api.recs(token)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  public Observable<Match> like(String token, String targetId) {
    return api.like(token, targetId)
        .subscribeOn(Schedulers.io())
        .map(LikeResponse::match);
  }

  public Observable<AuthResponse> auth(AuthRequest request) {
    return api.auth(request)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }
}
