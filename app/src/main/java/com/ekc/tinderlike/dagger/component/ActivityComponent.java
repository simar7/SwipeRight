package com.ekc.tinderlike.dagger.component;

import android.app.Activity;
import com.ekc.tinderlike.TinderLikeApplication;
import com.ekc.tinderlike.dagger.module.ActivityModule;
import com.ekc.tinderlike.dagger.qualifier.PerActivity;
import com.ekc.tinderlike.ui.main.MainActivity;
import com.ekc.tinderlike.ui.pref.SettingsActivity;
import dagger.Component;

@PerActivity
@Component(
    dependencies = AppComponent.class,
    modules = ActivityModule.class
)
public interface ActivityComponent extends BaseComponent {
  void inject(MainActivity activity);

  void inject(SettingsActivity activity);

  final class Initializer {
    private Initializer() {
      // no-op
    }

    public static ActivityComponent init(Activity activity) {
      return DaggerActivityComponent.builder()
          .activityModule(new ActivityModule(activity))
          .appComponent(TinderLikeApplication.getComponent(activity))
          .build();
    }
  }
}
