package com.ekc.tinderlike.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import com.ekc.tinderlike.R;
import com.ekc.tinderlike.dagger.component.ActivityComponent;
import com.ekc.tinderlike.dagger.qualifier.Qualifiers;
import com.ekc.tinderlike.dagger.qualifier.Qualifiers.Mock;
import com.ekc.tinderlike.model.Match;
import com.ekc.tinderlike.model.Recommendation;
import com.ekc.tinderlike.ui.base.BaseActivity;
import com.ekc.tinderlike.ui.pref.SettingsActivity;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MainActivity extends BaseActivity<MainPresenter, ActivityComponent>
    implements MainView {
  private static final int REQUEST_NEW_API_TOKEN = 1000;
  @Bind(R.id.auth_error) View authErrorView;
  @Bind(R.id.limit_reached) View limitReachedView;
  @Bind(android.R.id.list) RecyclerView list;
  @Bind(R.id.fab) FloatingActionButton fab;
  @Bind(R.id.progress_bar) ContentLoadingProgressBar progressBar;

  @Inject RecommendationAdapter adapter;
  private boolean likesAllInProgress;

  @Override protected void setLayoutId() {
    layoutId = R.layout.activity_main;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    list.setLayoutManager(new LinearLayoutManager(this));
    list.setAdapter(adapter);
    presenter.getRecommendations();
  }

  @Override protected void setupInjector() {
    component = ActivityComponent.Initializer.init(this);
    component.inject(this);
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
    if (requestCode == REQUEST_NEW_API_TOKEN) {
      if (resultCode == RESULT_OK) {
        refresh();
      }
    }
  }

  @OnClick(R.id.fab) void onFabClick() {
    if (!likesAllInProgress) {
      likesAllInProgress = true;
      adapter.likeAll();
    }
  }

  @Override public void loadResults(List<Recommendation> results) {
    showList();
    adapter.updateList(results);
  }

  @Override public void showLoading() {
    progressBar.show();
  }

  @Override public void hideLoading() {
    progressBar.hide();
  }

  @Override public void likeResponse(Recommendation recommendation, Match match) {
    Timber.d("Liked %s, user Id %s, match? %s", recommendation.name(), recommendation.id(),
        match.isMatch());
    adapter.likeResponse(recommendation, match, this);
    if (match.isMatch()) {
      Toast.makeText(this, String.format("Matched with %s!", recommendation.name()),
          Toast.LENGTH_SHORT).show();
    }
  }

  @Override public void showAuthError() {
    authErrorView.setVisibility(VISIBLE);
  }

  @Override public void hideAuthError() {
    authErrorView.setVisibility(INVISIBLE);
  }

  @Override public void hideErrorViews() {
    hideAuthError();
    hideLimitReached();
  }

  @Override public void showLimitReached() {
    limitReachedView.setVisibility(VISIBLE);
    hideList();
  }

  @Override public void hideLimitReached() {
    limitReachedView.setVisibility(INVISIBLE);
  }

  private void showList() {
    list.setVisibility(VISIBLE);
  }

  private void hideList() {
    list.setVisibility(INVISIBLE);
  }

  @Override public void failure(String errorMessage) {
    Toast.makeText(this, String.format("Error: %s", errorMessage), Toast.LENGTH_SHORT).show();
  }

  public void refresh() {
    likesAllInProgress = false;
    adapter.clear();
    presenter.getRecommendations();
  }
}
