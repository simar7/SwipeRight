package com.ekc.swiperight.dagger.module;

import android.app.Application;
import com.ekc.swiperight.dagger.qualifier.PerApp;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import java.io.File;

import static java.util.concurrent.TimeUnit.SECONDS;

@Module(includes = DataModule.class)
public class ReleaseDataModule {
  public static final int DISK_CACHE_SIZE = (int) (5 * 1024 * 1024); // 5 mb

  @Provides @PerApp OkHttpClient provideOkHttpClient(Application app) {
    OkHttpClient client = new OkHttpClient();
    client.setConnectTimeout(10, SECONDS);
    client.setReadTimeout(10, SECONDS);
    client.setWriteTimeout(10, SECONDS);

    final File cacheDir = new File(app.getCacheDir(), "http");
    final Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
    client.setCache(cache);

    return client;
  }
}
