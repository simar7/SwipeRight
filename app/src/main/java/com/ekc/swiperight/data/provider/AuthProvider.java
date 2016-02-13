package com.ekc.swiperight.data.provider;

import android.support.annotation.NonNull;
import com.ekc.swiperight.dagger.qualifier.PerApp;
import com.ekc.swiperight.dagger.qualifier.Qualifiers.Token;
import com.ekc.swiperight.data.TinderApiClient;
import com.ekc.swiperight.data.pref.StringPreference;
import com.ekc.swiperight.data.provider.DataObserver.AuthObserver;
import com.ekc.swiperight.model.AuthRequest;
import com.ekc.swiperight.util.RxUtil;
import com.facebook.AccessToken;
import javax.inject.Inject;
import rx.Subscription;

@PerApp
public class AuthProvider extends DataProvider<AuthObserver> {
  public static final String EN = "en";

  private static final AuthObserver EMPTY_OBSERVER = new EmptyAuthObserver();

  private final TinderApiClient api;
  private final StringPreference tinderToken;

  @NonNull private AuthObserver observer = EMPTY_OBSERVER;
  private Subscription subscription;

  @Inject AuthProvider(
      TinderApiClient api,
      @Token StringPreference tinderToken) {
    super(AuthObserver.class);
    this.api = api;
    this.tinderToken = tinderToken;
  }

  @Override void subscribe(@NonNull AuthObserver observer) {
    this.observer = observer;
  }

  @Override void unsubscribe(@NonNull AuthObserver observer) {
    this.observer = EMPTY_OBSERVER;
  }

  void auth() {
    if (RxUtil.inFlight(subscription)) {
      return;
    }

    final AccessToken currentAccessToken = AccessToken.getCurrentAccessToken();

    if (currentAccessToken == null) {
      observer.onAuthFailure(new NullPointerException(
          "User not logged in to Facebook, cannot connect to Tinder"));
      return;
    }

    AuthRequest request = new AuthRequest.Builder()
        .facebookToken(currentAccessToken.getToken())
        .facebookId(currentAccessToken.getUserId())
        .locale(EN)
        .build();

    subscription = api.auth(request)
        .subscribe(
            response -> {
              tinderToken.set(response.token());
              observer.onAuthSuccess();
            },
            observer::onAuthFailure
        );
  }

  static final class EmptyAuthObserver implements AuthObserver {
    @Override public void onAuthSuccess() {

    }

    @Override public void onAuthFailure(Throwable error) {

    }
  }
}
