package com.ekc.tinderlike.dagger.component;

import android.app.Activity;
import com.ekc.tinderlike.TinderLikeApplication;
import com.ekc.tinderlike.dagger.module.MainActivityModule;
import com.ekc.tinderlike.dagger.qualifier.PerActivity;
import com.ekc.tinderlike.ui.main.MainActivity;
import dagger.Component;

@PerActivity
@Component(
    dependencies = AppComponent.class,
    modules = MainActivityModule.class
)
public interface MainActivityComponent extends BaseComponent {
  void inject(MainActivity activity);

  final class Initializer {
    private Initializer() {
      // no-op
    }

    public static MainActivityComponent init(Activity activity) {
      return DaggerMainActivityComponent.builder()
          .mainActivityModule(new MainActivityModule(activity))
          .appComponent(TinderLikeApplication.getComponent(activity))
          .build();
    }
  }
}
