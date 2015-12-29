package com.ekc.tinderlike.dagger.module;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import com.ekc.tinderlike.R;
import com.ekc.tinderlike.dagger.qualifier.PerApp;
import com.ekc.tinderlike.dagger.qualifier.Qualifiers;
import com.ekc.tinderlike.dagger.qualifier.Qualifiers.FbId;
import com.ekc.tinderlike.dagger.qualifier.Qualifiers.FbToken;
import com.ekc.tinderlike.dagger.qualifier.Qualifiers.Mock;
import com.ekc.tinderlike.dagger.qualifier.Qualifiers.Token;
import com.ekc.tinderlike.data.StringPreference;
import com.ekc.tinderlike.data.TinderApi;
import com.ekc.tinderlike.data.converter.LocalDateConverter;
import com.ekc.tinderlike.data.converter.MatchAdapterFactory;
import com.ekc.tinderlike.data.mock.MockTinderApi;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import dagger.Module;
import dagger.Provides;
import org.threeten.bp.LocalDate;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class DataModule {
  public static final int PREF_TOKEN = R.string.key_tinder_api;
  public static final int PREF_FB_TOKEN = R.string.key_fb_token;
  public static final int PREF_FB_ID = R.string.key_fb_id;

  public static final int DEFAULT_TOKEN = R.string.default_tinder_token;
  public static final int DEFAULT_FB_TOKEN = R.string.default_fb_token;
  public static final int DEFAULT_FB_ID = R.string.default_fb_id;

  @Provides
  @PerApp OkHttpClient provideOkHttpClient() {
    OkHttpClient client = new OkHttpClient();
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    client.interceptors().add(interceptor);
    return client;
  }

  @Provides
  @PerApp TinderApi provideTinderApiClient(OkHttpClient client, Gson gson) {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(TinderApi.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    return retrofit.create(TinderApi.class);
  }

  @Provides @PerApp @Mock TinderApi provideMockTinderApi(Gson gson, AssetManager assetManager) {
    return new MockTinderApi(gson, assetManager);
  }

  @Provides
  @PerApp Gson provideGson() {
    return new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .registerTypeAdapterFactory(new MatchAdapterFactory())
        .registerTypeAdapter(LocalDate.class, new LocalDateConverter())
        .create();
  }

  @Provides
  @PerApp @Token StringPreference provideToken(SharedPreferences prefs, Resources res) {
    return new StringPreference(prefs, res, PREF_TOKEN, DEFAULT_TOKEN);
  }

  @Provides
  @PerApp @FbToken StringPreference provideFbToken(SharedPreferences prefs, Resources res) {
    return new StringPreference(prefs, res, PREF_FB_TOKEN, DEFAULT_FB_TOKEN);
  }

  @Provides
  @PerApp @FbId StringPreference provideFbId(SharedPreferences prefs, Resources res) {
    return new StringPreference(prefs, res, PREF_FB_ID, DEFAULT_FB_ID);
  }
}
