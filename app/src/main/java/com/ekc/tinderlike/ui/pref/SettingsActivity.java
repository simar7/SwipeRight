package com.ekc.tinderlike.ui.pref;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.Menu;
import android.widget.Button;
import butterknife.Bind;
import butterknife.OnClick;
import com.ekc.tinderlike.R;
import com.ekc.tinderlike.dagger.component.ActivityComponent;
import com.ekc.tinderlike.ui.base.BaseActivity;

public class SettingsActivity extends BaseActivity<SettingsPresenter, ActivityComponent>
    implements SettingsView {
  @Bind(R.id.get_token) Button getToken;
  @Bind(R.id.reset) Button reset;
  @Bind(R.id.progress_bar) ContentLoadingProgressBar progressBar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    hideLoading();
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
  }

  @Override protected void setLayoutId() {
    layoutId = R.layout.activity_settings;
  }

  @Override protected void setupInjector() {
    component = ActivityComponent.Initializer.init(this);
    component.inject(this);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    return false;
  }

  @OnClick(R.id.get_token) public void onGetToken() {
    presenter.getToken();
    setResult(RESULT_OK);
  }

  @OnClick(R.id.reset) public void onReset() {
    PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();
    PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
    // Technically not okay, but force refresh so you can't make Like calls with stale API token
    setResult(RESULT_OK);

    // Update the preference summary text
    SettingsFragment fragment =
        (SettingsFragment) getFragmentManager().findFragmentById(R.id.settings_fragment);

    if (fragment != null) {
      fragment.initPreferenceSummary();
    }
  }

  @Override
  public void showLoading() {
    progressBar.show();
  }

  @Override public void onAuthSuccess() {

  }

  @Override public void onAuthFailure() {

  }

  @Override
  public void hideLoading() {
    progressBar.hide();
  }
}
