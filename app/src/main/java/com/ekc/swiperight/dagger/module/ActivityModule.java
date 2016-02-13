package com.ekc.swiperight.dagger.module;

import android.app.Activity;
import com.ekc.swiperight.dagger.qualifier.PerActivity;
import com.ekc.swiperight.ui.main.MainPresenter;
import com.ekc.swiperight.ui.main.RecommendationAdapter;
import com.facebook.CallbackManager;
import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
  Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides @PerActivity RecommendationAdapter provideRecommendationAdapter(
      MainPresenter presenter) {
    return new RecommendationAdapter(activity, presenter);
  }

  @Provides @PerActivity CallbackManager provideCallbackManager() {
    return CallbackManager.Factory.create();
  }
}
