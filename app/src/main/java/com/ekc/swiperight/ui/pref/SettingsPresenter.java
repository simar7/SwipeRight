package com.ekc.swiperight.ui.pref;

import com.ekc.swiperight.dagger.qualifier.PerActivity;
import com.ekc.swiperight.data.provider.DataManager;
import com.ekc.swiperight.data.provider.DataObserver.AuthObserver;
import com.ekc.swiperight.ui.base.BasePresenter;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import javax.inject.Inject;
import timber.log.Timber;

@PerActivity
public class SettingsPresenter extends BasePresenter<SettingsView> implements
    AuthObserver,
    FacebookCallback<LoginResult> {
  private final DataManager dataManager;

  @Inject SettingsPresenter(DataManager dataManager) {
    this.dataManager = dataManager;
  }

  @Override public void initialize() {
    dataManager.subscribe(this);
  }

  @Override public void destroy() {
    dataManager.unsubscribe(this);
  }

  public void getToken() {
    setEmptyViewIfNull();

    view.showLoading();
    dataManager.auth();
  }

  @Override public void onAuthSuccess() {
    setEmptyViewIfNull();

    view.hideLoading();
    view.onAuthSuccess();
  }

  @Override public void onAuthFailure(Throwable error) {
    Timber.e(error, error.getMessage());

    setEmptyViewIfNull();

    view.hideLoading();
    view.onAuthFailure();
  }

  private void setEmptyViewIfNull() {
    if (view == null) {
      view = SettingsView.EMPTY;
    }
  }

  @Override public void onSuccess(LoginResult loginResult) {
    // Facebook login successful, now retrieve a Tinder API token
    getToken();
  }

  @Override public void onCancel() {
    // no-op
  }

  @Override public void onError(FacebookException error) {
    // no-op
  }
}
