package com.ekc.tinderlike.ui.pref;

import com.ekc.tinderlike.data.StringPreference;
import com.ekc.tinderlike.data.TinderApi;
import com.ekc.tinderlike.model.AuthRequest;
import com.ekc.tinderlike.ui.base.BasePresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SettingsPresenter extends BasePresenter<SettingsView> {

  public static final String EN = "en";
  TinderApi api;
  StringPreference fbToken;
  StringPreference fbId;
  StringPreference tinderToken;

  Subscription subscription;

  public SettingsPresenter(TinderApi api, StringPreference fbToken, StringPreference fbId,
      StringPreference tinderToken) {
    this.api = api;
    this.fbToken = fbToken;
    this.fbId = fbId;
    this.tinderToken = tinderToken;
  }

  @Override protected void initialize() {

  }

  @Override protected void destroy() {
    if (subscription != null) {
      subscription.unsubscribe();
    }
  }

  public void getToken() {
    AuthRequest request = new AuthRequest.Builder()
        .facebookToken(fbToken.get())
        .facebookId(fbId.get())
        .locale(EN)
        .build();

    view.showLoading();
    subscription =
        api.auth(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(response -> {
                  tinderToken.set(response.token());
                  view.onAuthSuccess();
                  view.hideLoading();
                },
                error -> {
                  view.onAuthSuccess();
                  view.hideLoading();
                  Timber.e(error, error.getMessage());
                });
  }
}
