package com.ekc.tinderlike.ui.pref;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.text.TextUtils;
import com.ekc.tinderlike.R;

public class SettingsFragment extends PreferenceFragment
    implements SharedPreferences.OnSharedPreferenceChangeListener {
  private Resources res;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    res = getResources();
    addPreferencesFromResource(R.xml.preferences);
    SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
    sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    initPreferenceSummary();
  }

  public void initPreferenceSummary() {
    // TODO not scalable, depends on knowing that there is only 1 group in pref tree. because lazy
    PreferenceGroup group = (PreferenceGroup) getPreferenceScreen().getPreference(0);
    SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
    for (int i = 0, size = group.getPreferenceCount(); i < size; i++) {
      Preference preference = group.getPreference(i);
      String key = preference.getKey();
      String summary = sharedPreferences.getString(key, "");
      if (TextUtils.isEmpty(summary)) {
        summary = getEmptySummary(key);
      }
      preference.setSummary(summary);
    }
  }

  private String getEmptySummary(String key) {
    int summaryRes = R.string.summary_pref_not_found;

    if (key.equals(res.getString(R.string.key_tinder_api))) {
      summaryRes = R.string.summary_token_not_found;
    } else if (key.equals(res.getString(R.string.key_fb_token))) {
      summaryRes = R.string.summary_fb_token_not_found;
    } else if (key.equals(res.getString(R.string.key_fb_id))) {
      summaryRes = R.string.summary_id_not_found;
    }

    return res.getString(summaryRes);
  }

  @Override public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Preference preference = getPreferenceScreen().findPreference(key);
    preference.setSummary(sharedPreferences.getString(key, getEmptySummary(key)));
  }
}
