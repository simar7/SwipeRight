package com.ekc.tinderlike.dagger.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import com.ekc.tinderlike.dagger.qualifier.PerApp;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
  private final Application app;

  public AppModule(Application app) {
    this.app = app;
  }

  @Provides
  @PerApp Resources provideResources() {
    return app.getResources();
  }

  @Provides
  @PerApp SharedPreferences provideSharedPreferences() {
    return PreferenceManager.getDefaultSharedPreferences(app);
  }
}
