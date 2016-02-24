package com.ekc.swiperight;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import com.ekc.swiperight.dagger.component.AppComponent;
import com.facebook.FacebookSdk;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

public class SwipeRightApp extends Application {
  private static Context context;
  private static int themeID;
  private static String themeSetting;
  private AppComponent component;

  public static AppComponent getComponent(Context context) {
    return ((SwipeRightApp) context.getApplicationContext()).component;
  }

  public static int getThemeID() {
    return SwipeRightApp.themeID;
  }

  public static void reloadTheme() {
    SwipeRightApp.themeSetting = PreferenceManager.getDefaultSharedPreferences(SwipeRightApp.context).getString("default_theme_choice", "0");
    if (themeSetting.equals("Light"))
      SwipeRightApp.themeID = R.style.AppTheme;
    else
      SwipeRightApp.themeID = R.style.AppThemeDark;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SwipeRightApp.context = getApplicationContext();

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
}
