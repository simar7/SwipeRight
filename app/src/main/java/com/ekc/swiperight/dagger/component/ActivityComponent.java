package com.ekc.swiperight.dagger.component;

import android.app.Activity;
import com.ekc.swiperight.SwipeRightApp;
import com.ekc.swiperight.dagger.module.ActivityModule;
import com.ekc.swiperight.dagger.qualifier.PerActivity;
import com.ekc.swiperight.ui.main.MainActivity;
import com.ekc.swiperight.ui.pref.SettingsActivity;
import com.ekc.swiperight.ui.pref.SettingsFragment;
import dagger.Component;

@PerActivity
@Component(
    dependencies = AppComponent.class,
    modules = ActivityModule.class
)
public interface ActivityComponent extends BaseComponent {
  void inject(MainActivity activity);

  void inject(SettingsActivity activity);

  void inject(SettingsFragment fragment);

  final class Initializer {
    private Initializer() {
      // no-op
    }

    public static ActivityComponent init(Activity activity) {
      return DaggerActivityComponent.builder()
          .activityModule(new ActivityModule(activity))
          .appComponent(SwipeRightApp.getComponent(activity))
          .build();
    }
  }
}
