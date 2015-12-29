package com.ekc.tinderlike.data.mock;

import android.content.res.AssetManager;
import android.support.annotation.Nullable;
import com.ekc.tinderlike.data.TinderApi;
import com.ekc.tinderlike.model.AuthRequest;
import com.ekc.tinderlike.model.AuthResponse;
import com.ekc.tinderlike.model.LikeResponse;
import com.ekc.tinderlike.model.RecommendationResponse;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.Path;
import rx.Observable;

public class MockTinderApi implements TinderApi {

  private final Gson gson;
  private final AssetManager assetManager;

  public MockTinderApi(Gson gson, AssetManager assetManager) {
    this.gson = gson;
    this.assetManager = assetManager;
  }

  @Override public Observable<LikeResponse> like(@Header("X-Auth-Token") String token,
      @Path("targetId") String targetId) {
    final Reader jsonFromFile = getJsonFromFile("match.json");
    if (jsonFromFile != null) {
      LikeResponse response = gson.fromJson(jsonFromFile, LikeResponse.class);
      return Observable.just(response);
    } else {
      return Observable.empty();
    }
  }

  @Override public Observable<RecommendationResponse> recs(@Header("X-Auth-Token") String token) {
    final Reader jsonFromFile = getJsonFromFile("recs.json");
    if (jsonFromFile != null) {
      RecommendationResponse response = gson.fromJson(jsonFromFile, RecommendationResponse.class);
      return Observable.just(response);
    } else {
      return Observable.empty();
    }
  }

  @Override public Observable<AuthResponse> auth(@Body AuthRequest request) {
    return Observable.empty();
  }

  @Nullable private Reader getJsonFromFile(String filename) {
    try {
      InputStream inputStream = assetManager.open(filename);
      return new BufferedReader(new InputStreamReader(inputStream));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
