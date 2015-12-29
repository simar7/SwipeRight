package com.ekc.tinderlike.ui.pref;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ekc.tinderlike.R;

public class SettingsActivity extends AppCompatActivity {
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.reset) Button reset;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
  }

  @OnClick(R.id.reset) public void onReset() {
    PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();
    PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
  }
}
