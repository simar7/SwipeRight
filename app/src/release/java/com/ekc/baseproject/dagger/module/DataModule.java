package com.ekc.tinderlike.dagger.module;

import com.ekc.tinderlike.dagger.qualifier.PerApp;
import com.ekc.tinderlike.data.GitHubApi;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class DataModule {
  @Provides
  @PerApp
  OkHttpClient provideOkHttpClient() {
    OkHttpClient client = new OkHttpClient();
    return client;
  }

  @Provides
  @PerApp
  GitHubApi provideGitHubApi() {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(GitHubApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    return retrofit.create(GitHubApi.class);
  }
}
