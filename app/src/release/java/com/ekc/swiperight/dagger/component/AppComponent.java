package com.ekc.swiperight.dagger.component;

import android.app.Application;
import android.content.res.Resources;
import com.ekc.swiperight.dagger.module.AppModule;
import com.ekc.swiperight.dagger.module.DataModule;
import com.ekc.swiperight.dagger.module.ReleaseDataModule;
import com.ekc.swiperight.dagger.qualifier.PerApp;
import com.ekc.swiperight.dagger.qualifier.Qualifiers.Token;
import com.ekc.swiperight.data.pref.StringPreference;
import com.ekc.swiperight.data.provider.DataManager;
import dagger.Component;

@PerApp
@Component(modules = { AppModule.class, ReleaseDataModule.class })
public interface AppComponent extends BaseComponent {
  Application application();

  Resources resources();

  DataManager dataManager();

  @Token StringPreference token();

  final class Initializer {
    private Initializer() {
      // no-op
    }

    public static AppComponent init(Application app) {
      return DaggerAppComponent.builder()
          .appModule(new AppModule(app))
          .releaseDataModule(new ReleaseDataModule())
          .build();
    }
  }
}
