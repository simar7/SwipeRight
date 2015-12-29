package com.ekc.tinderlike.dagger.module;

import android.app.Activity;
import com.ekc.tinderlike.dagger.qualifier.PerActivity;
import com.ekc.tinderlike.dagger.qualifier.Qualifiers.FbId;
import com.ekc.tinderlike.dagger.qualifier.Qualifiers.FbToken;
import com.ekc.tinderlike.dagger.qualifier.Qualifiers.Token;
import com.ekc.tinderlike.data.StringPreference;
import com.ekc.tinderlike.data.TinderApi;
import com.ekc.tinderlike.model.Recommendation;
import com.ekc.tinderlike.ui.main.MainPresenter;
import com.ekc.tinderlike.ui.main.RecommendationAdapter;
import com.ekc.tinderlike.ui.pref.SettingsPresenter;
import dagger.Module;
import dagger.Provides;
import rx.subjects.BehaviorSubject;

@Module
public class ActivityModule {
  Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  // Use @Mock TinderApi for mock mode
  @Provides
  @PerActivity MainPresenter providePresenter(TinderApi api,
      BehaviorSubject<Recommendation> recommendationSubject,
      @Token StringPreference token) {
    return new MainPresenter(api, recommendationSubject, token);
  }

  @Provides
  @PerActivity RecommendationAdapter provideRecommendationAdapter(
      BehaviorSubject<Recommendation> recommendationSubject) {
    return new RecommendationAdapter(activity, recommendationSubject);
  }

  @Provides
  @PerActivity BehaviorSubject<Recommendation> provideRecommendationSubject() {
    return BehaviorSubject.create();
  }

  @Provides
  @PerActivity SettingsPresenter provideSettingsPresenter(TinderApi api,
      @FbToken StringPreference fbToken, @FbId StringPreference fbId,
      @Token StringPreference tinderToken) {
    return new SettingsPresenter(api, fbToken, fbId, tinderToken);
  }
}
