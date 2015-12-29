package com.ekc.tinderlike.data;

import com.ekc.tinderlike.model.AuthRequest;
import com.ekc.tinderlike.model.AuthResponse;
import com.ekc.tinderlike.model.LikeResponse;
import com.ekc.tinderlike.model.RecommendationResponse;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

public interface TinderApi {
  String BASE_URL = "https://api.gotinder.com";

  @GET("/like/{targetId}")
  Observable<LikeResponse> like(@Header("X-Auth-Token") String token, @Path("targetId") String targetId);

  @GET("/user/recs?locale=en")
  Observable<RecommendationResponse> recs(@Header("X-Auth-Token") String token);

  @Headers("Content-Type: application/json")
  @POST("/auth")
  Observable<AuthResponse> auth(@Body AuthRequest request);
}
