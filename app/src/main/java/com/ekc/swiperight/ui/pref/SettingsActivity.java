package com.ekc.swiperight.ui.pref;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import com.ekc.swiperight.R;
import com.ekc.swiperight.dagger.component.ActivityComponent;
import com.ekc.swiperight.ui.base.BaseActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginBehavior;
import com.facebook.login.widget.LoginButton;
import javax.inject.Inject;

import static com.ekc.swiperight.ui.ResultCode.RESULT_REFRESH;

public class SettingsActivity extends BaseActivity implements SettingsView {
  @Bind(R.id.get_token) Button getToken;
  @Bind(R.id.progress_bar) ContentLoadingProgressBar progressBar;
  @Bind(R.id.fb_login) LoginButton fbLoginButton;

  @Inject SettingsPresenter presenter;
  @Inject CallbackManager callbackManager;

  private ActivityComponent component;

  @Override protected void setLayoutId() {
    layoutId = R.layout.activity_settings;
  }

  @Override protected void setupInjector() {
    component = ActivityComponent.Initializer.init(this);
    component.inject(this);
  }

  @Override public ActivityComponent component() {
    return component;
  }

  @Override protected void setupPresenter() {
    presenter.initialize();
    presenter.bindView(this);
  }

  @Override protected void destroyPresenter() {
    presenter.unbindView();
    presenter.destroy();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

    fbLoginButton.registerCallback(callbackManager, presenter);
    fbLoginButton.setLoginBehavior(LoginBehavior.WEB_ONLY);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    return false;
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }

  @OnClick(R.id.get_token) public void onClickGetToken() {
    presenter.getToken();
  }

  @Override public void onAuthSuccess() {
    setResult(RESULT_OK);
  }

  @Override public void onAuthFailure() {
    Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT).show();
  }

  @Override public void showLoading() {
    progressBar.show();
  }

  @Override public void hideLoading() {
    progressBar.hide();
  }

  @Override public void finish() {
    if (AccessToken.getCurrentAccessToken() == null) {
      setResult(RESULT_REFRESH);
    }
    super.finish();
  }
}
