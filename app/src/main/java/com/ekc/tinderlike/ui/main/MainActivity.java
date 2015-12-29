package com.ekc.tinderlike.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import com.ekc.tinderlike.R;
import com.ekc.tinderlike.dagger.component.MainActivityComponent;
import com.ekc.tinderlike.model.Match;
import com.ekc.tinderlike.model.Recommendation;
import com.ekc.tinderlike.ui.base.BaseActivity;
import com.ekc.tinderlike.ui.pref.SettingsActivity;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

public class MainActivity extends BaseActivity<MainPresenter, MainActivityComponent>
    implements MainView {
  @Bind(android.R.id.list) RecyclerView list;
  @Bind(R.id.fab) FloatingActionButton fab;
  @Bind(R.id.progress_bar) ContentLoadingProgressBar progressBar;

  @Inject RecommendationAdapter adapter;

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
    component = MainActivityComponent.Initializer.init(this);
    component.inject(this);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    final int id = item.getItemId();

    if (id == R.id.action_settings) {
      startActivity(new Intent(this, SettingsActivity.class));
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @OnClick(R.id.fab) void onFabClick() {
    adapter.likeAll();
  }

  @Override public void loadResults(List<Recommendation> results) {
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

  @Override public void failure(String errorMessage) {
    Toast.makeText(this, String.format("Error: %s", errorMessage), Toast.LENGTH_SHORT).show();
  }

  public void refresh() {
    adapter.clear();
    presenter.getRecommendations();
  }
}
