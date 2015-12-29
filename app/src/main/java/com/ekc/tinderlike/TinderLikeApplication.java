package com.ekc.tinderlike;

import android.app.Application;
import android.content.Context;
import com.ekc.tinderlike.dagger.component.AppComponent;
import com.squareup.leakcanary.LeakCanary;
import timber.log.Timber;

public class TinderLikeApplication extends Application {
  private AppComponent component;

  @Override
  public void onCreate() {
    super.onCreate();

    //LeakCanary setup
    LeakCanary.install(this);

    //Timber setup
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }

    //Dagger component setup
    component = AppComponent.Initializer.init(this);
  }

  public static AppComponent getComponent(Context context) {
    return ((TinderLikeApplication) context.getApplicationContext()).component;
  }
}
