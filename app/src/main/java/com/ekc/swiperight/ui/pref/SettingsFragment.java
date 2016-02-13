package com.ekc.swiperight.ui.pref;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import com.ekc.swiperight.R;
import com.ekc.swiperight.dagger.qualifier.Qualifiers.Token;
import com.ekc.swiperight.data.pref.StringPreference;
import javax.inject.Inject;

public class SettingsFragment extends PreferenceFragment
    implements SharedPreferences.OnSharedPreferenceChangeListener {
  @Inject @Token StringPreference token;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.preferences);
    SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
    sharedPreferences.registerOnSharedPreferenceChangeListener(this);
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    ((SettingsActivity) getActivity()).component().inject(this);
    initTokenSummary();
  }

  public void initTokenSummary() {
    Preference preference = getPreferenceScreen().findPreference(token.getKey());
    preference.setSummary(TextUtils.isEmpty(token.get())
                          ? getString(R.string.summary_token_not_found)
                          : token.get());
  }

  @Override public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (key.equals(token.getKey())) {
      initTokenSummary();
    }
  }
}
