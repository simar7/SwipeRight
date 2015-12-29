package com.ekc.tinderlike.dagger.module;

import android.content.SharedPreferences;
import com.ekc.tinderlike.dagger.qualifier.PerApp;
import com.ekc.tinderlike.dagger.qualifier.Qualifiers.Token;
import com.ekc.tinderlike.data.StringPreference;
import com.ekc.tinderlike.data.TinderApi;
import com.ekc.tinderlike.data.converter.LocalDateConverter;
import com.ekc.tinderlike.data.converter.MatchAdapterFactory;
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
  private static final String DEFAULT_TOKEN = "c1099c81-300c-4d79-aa8a-e7d80dc0e09f";
  public static final String PREF_TOKEN = "api_key";

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

  @Provides
  @PerApp Gson provideGson() {
    return new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .registerTypeAdapterFactory(new MatchAdapterFactory())
        .registerTypeAdapter(LocalDate.class, new LocalDateConverter())
        .create();
  }

  @Provides
  @PerApp @Token StringPreference provideToken(SharedPreferences prefs) {
    return new StringPreference(prefs, PREF_TOKEN, DEFAULT_TOKEN);
  }
}
