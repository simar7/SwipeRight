package com.ekc.tinderlike.dagger.module;

import android.app.Activity;
import android.content.res.Resources;
import com.ekc.tinderlike.dagger.qualifier.PerActivity;
import com.ekc.tinderlike.dagger.qualifier.Qualifiers;
import com.ekc.tinderlike.dagger.qualifier.Qualifiers.Token;
import com.ekc.tinderlike.data.StringPreference;
import com.ekc.tinderlike.data.TinderApi;
import com.ekc.tinderlike.model.Recommendation;
import com.ekc.tinderlike.ui.main.MainPresenter;
import com.ekc.tinderlike.ui.main.RecommendationAdapter;
import dagger.Module;
import dagger.Provides;
import rx.subjects.BehaviorSubject;

@Module
public class MainActivityModule {
  Activity activity;

  public MainActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides
  @PerActivity MainPresenter providePresenter(TinderApi api,
      BehaviorSubject<Recommendation> recommendationSubject,
      @Token StringPreference token) {
    return new MainPresenter(api, recommendationSubject, token);
  }

  @Provides
  @PerActivity RecommendationAdapter provideRecommendationAdapter(Resources res,
      BehaviorSubject<Recommendation> recommendationSubject) {

    return new RecommendationAdapter(activity, res, recommendationSubject);
  }

  @Provides
  @PerActivity BehaviorSubject<Recommendation> provideRecommendationSubject() {
    return BehaviorSubject.create();
  }
}
