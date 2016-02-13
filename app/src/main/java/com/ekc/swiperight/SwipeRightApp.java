package com.ekc.swiperight;

import android.app.Application;
import android.content.Context;
import com.ekc.swiperight.dagger.component.AppComponent;
import com.facebook.FacebookSdk;
import com.squareup.leakcanary.LeakCanary;
import timber.log.Timber;

public class SwipeRightApp extends Application {
  private AppComponent component;

  @Override
  public void onCreate() {
    super.onCreate();

    FacebookSdk.sdkInitialize(this);

    if (BuildConfig.DEBUG) {
      //LeakCanary setup
      LeakCanary.install(this);
      //Timber setup
      Timber.plant(new Timber.DebugTree());
    }

    //Dagger component setup
    component = AppComponent.Initializer.init(this);
  }

  public static AppComponent getComponent(Context context) {
    return ((SwipeRightApp) context.getApplicationContext()).component;
  }
}
