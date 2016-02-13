package com.ekc.swiperight.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import com.ekc.swiperight.R;
import com.ekc.swiperight.dagger.component.ActivityComponent;
import com.ekc.swiperight.dagger.component.BaseComponent;
import com.ekc.swiperight.model.Match;
import com.ekc.swiperight.model.Recommendation;
import com.ekc.swiperight.ui.base.BaseActivity;
import com.ekc.swiperight.ui.pref.SettingsActivity;
import com.facebook.CallbackManager;
import com.facebook.login.LoginBehavior;
import com.facebook.login.widget.LoginButton;
import java.util.List;
import javax.inject.Inject;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.ekc.swiperight.ui.RequestCode.REQUEST_NEW_API_TOKEN;
import static com.ekc.swiperight.ui.ResultCode.RESULT_REFRESH;

public class MainActivity extends BaseActivity implements MainView {
  @Bind(R.id.auth_error) View authErrorView;
  @Bind(R.id.limit_reached) View limitReachedView;
  @Bind(R.id.connection_error) View connectionErrorView;
  @Bind(android.R.id.list) RecyclerView list;
  @Bind(R.id.progress_bar) ContentLoadingProgressBar progressBar;
  @Bind(R.id.fb_login) LoginButton fbLoginButton;

  @Inject MainPresenter presenter;
  @Inject RecommendationAdapter adapter;
  @Inject CallbackManager callbackManager;

  private ActivityComponent component;

  @Override protected void setLayoutId() {
    layoutId = R.layout.activity_main;
  }

  @Override protected void setupInjector() {
    component = ActivityComponent.Initializer.init(this);
    component.inject(this);
  }

  @Override public BaseComponent component() {
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

    list.setLayoutManager(new LinearLayoutManager(this));
    list.setAdapter(adapter);
    // Cannot use item animators with notifyChanged calls with payloads. Booo-urns.
    list.setItemAnimator(null);
    fbLoginButton.registerCallback(callbackManager, presenter);
    fbLoginButton.setLoginBehavior(LoginBehavior.WEB_ONLY);
    presenter.auth();
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    final int id = item.getItemId();

    if (id == R.id.action_settings) {
      startActivityForResult(new Intent(this, SettingsActivity.class), REQUEST_NEW_API_TOKEN);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_NEW_API_TOKEN) {
      switch (resultCode) {
        case RESULT_OK:
        case RESULT_REFRESH:
          adapter.clear();
          presenter.refresh();
          return;
      }
    }
  }

  @OnClick(R.id.fab) void onFabClick() {
    adapter.likeAll();
  }

  @Override public void likeResponse(Match match) {
    adapter.likeResponse(match);
    if (match != Match.NO_MATCH) {
      Toast.makeText(this,
          getString(R.string.match_success, match.recommendation().name()),
          Toast.LENGTH_SHORT).show();
    }
  }

  @Override public void failure(String errorMessage) {
    Toast.makeText(this, getString(R.string.generic_error, errorMessage), Toast.LENGTH_SHORT)
        .show();
  }

  @Override public void loadRecommendations(List<Recommendation> results) {
    showList();
    adapter.updateList(results);
  }

  @Override public void showLoading() {
    progressBar.show();
  }

  @Override public void hideLoading() {
    progressBar.hide();
  }

  @Override public void showAuthError() {
    authErrorView.setVisibility(VISIBLE);
    hideList();
  }

  @Override public void hideAuthError() {
    authErrorView.setVisibility(INVISIBLE);
  }

  @Override public void showLimitReached() {
    limitReachedView.setVisibility(VISIBLE);
    hideList();
  }

  @Override public void hideLimitReached() {
    limitReachedView.setVisibility(INVISIBLE);
  }

  @Override public void showConnectionError() {
    connectionErrorView.setVisibility(VISIBLE);
    hideList();
  }

  @Override public void hideConnectionError() {
    connectionErrorView.setVisibility(INVISIBLE);
  }

  private void showList() {
    list.setVisibility(VISIBLE);
  }

  private void hideList() {
    list.setVisibility(INVISIBLE);
  }

  @Override public void hideErrorViews() {
    hideAuthError();
    hideLimitReached();
    hideConnectionError();
  }
}
