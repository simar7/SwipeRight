package com.ekc.swiperight.data.pref;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.StringRes;

public class StringPreference {
  private final SharedPreferences preferences;
  private final String key;
  private final String defaultValue;

  public StringPreference(SharedPreferences preferences, String key) {
    this(preferences, key, null);
  }

  public StringPreference(SharedPreferences preferences, String key, String defaultValue) {
    this.preferences = preferences;
    this.key = key;
    this.defaultValue = defaultValue;
  }

  public StringPreference(SharedPreferences preferences, Resources res, @StringRes int key,
      @StringRes int defaultValue) {
    this.preferences = preferences;
    this.key = res.getString(key);
    this.defaultValue = res.getString(defaultValue);
  }

  public String get() { return preferences.getString(key, defaultValue); }

  public String getKey() {
    return key;
  }

  public boolean isSet() {
    return preferences.contains(key);
  }

  public void set(String value) {
    preferences.edit().putString(key, value).apply();
  }

  public void delete() {
    preferences.edit().remove(key).apply();
  }
}