package com.ekc.tinderlike.ui.pref;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import com.ekc.tinderlike.R;
import com.ekc.tinderlike.dagger.module.DataModule;

public class SettingsFragment extends PreferenceFragment
    implements SharedPreferences.OnSharedPreferenceChangeListener {
  public static final String EMPTY_KEY = "Enter an API Key";

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.preferences);
    SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
    sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    Preference preference = getPreferenceScreen().findPreference(DataModule.PREF_TOKEN);
    preference.setSummary(sharedPreferences.getString(DataModule.PREF_TOKEN, EMPTY_KEY));
  }

  @Override public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Preference preference = getPreferenceScreen().findPreference(key);
    preference.setSummary(sharedPreferences.getString(key, EMPTY_KEY));
  }
}
