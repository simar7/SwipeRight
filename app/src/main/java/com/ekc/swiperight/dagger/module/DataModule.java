package com.ekc.swiperight.dagger.module;

import android.content.SharedPreferences;
import android.content.res.Resources;
import com.ekc.swiperight.R;
import com.ekc.swiperight.dagger.qualifier.PerApp;
import com.ekc.swiperight.dagger.qualifier.Qualifiers.Token;
import com.ekc.swiperight.data.TinderApi;
import com.ekc.swiperight.data.TinderApiClient;
import com.ekc.swiperight.data.converter.LocalDateConverter;
import com.ekc.swiperight.data.converter.MatchAdapterFactory;
import com.ekc.swiperight.data.pref.StringPreference;
import com.ekc.swiperight.data.provider.AuthProvider;
import com.ekc.swiperight.data.provider.DataProvider;
import com.ekc.swiperight.data.provider.LikeProvider;
import com.ekc.swiperight.data.provider.RecommendationProvider;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import java.util.Arrays;
import java.util.List;
import org.threeten.bp.LocalDate;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class DataModule {
  public static final int PREF_TOKEN = R.string.key_tinder_api;

  public static final int DEFAULT_TOKEN = R.string.default_token;

  @Provides @PerApp List<DataProvider> provideDataProviders(
      RecommendationProvider recommendationProvider,
      LikeProvider likeProvider,
      AuthProvider authProvider) {
    return Arrays.asList(recommendationProvider, likeProvider, authProvider);
  }

  @Provides @PerApp @Token StringPreference provideToken(SharedPreferences prefs, Resources res) {
    return new StringPreference(prefs, res, PREF_TOKEN, DEFAULT_TOKEN);
  }

  @Provides @PerApp Gson provideGson() {
    return new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .registerTypeAdapterFactory(new MatchAdapterFactory())
        .registerTypeAdapter(LocalDate.class, new LocalDateConverter())
        .create();
  }

  @Provides @PerApp TinderApi provideTinderApi(OkHttpClient client, Gson gson) {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(TinderApi.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    return retrofit.create(TinderApi.class);
  }

  // Change param to @Mock TinderApi for mock responses in debug builds
  @Provides @PerApp TinderApiClient provideTinderApiClient(TinderApi api) {
    return new TinderApiClient(api);
  }
}
