package com.ekc.swiperight.dagger.module;

import android.app.Application;
import android.content.res.AssetManager;
import com.ekc.swiperight.dagger.qualifier.PerApp;
import com.ekc.swiperight.dagger.qualifier.Qualifiers.Mock;
import com.ekc.swiperight.data.TinderApi;
import com.ekc.swiperight.data.mock.MockTinderApi;
import com.google.gson.Gson;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import dagger.Module;
import dagger.Provides;
import java.io.File;

import static java.util.concurrent.TimeUnit.SECONDS;

@Module(includes = DataModule.class)
public class DebugDataModule {
  public static final int DISK_CACHE_SIZE = (int) (5 * 1024 * 1024); // 5 mb

  @Provides @PerApp OkHttpClient provideOkHttpClient(Application app) {
    OkHttpClient client = new OkHttpClient();
    client.setConnectTimeout(10, SECONDS);
    client.setReadTimeout(10, SECONDS);
    client.setWriteTimeout(10, SECONDS);

    final File cacheDir = new File(app.getCacheDir(), "http");
    final Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
    client.setCache(cache);

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    client.interceptors().add(interceptor);
    return client;
  }

  @Provides @PerApp @Mock TinderApi provideMockTinderApi(Gson gson, AssetManager assetManager) {
    return new MockTinderApi(gson, assetManager);
  }
}
