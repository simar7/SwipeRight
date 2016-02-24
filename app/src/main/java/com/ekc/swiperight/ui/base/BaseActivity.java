package com.ekc.swiperight.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ekc.swiperight.R;
import com.ekc.swiperight.SwipeRightApp;
import com.ekc.swiperight.dagger.component.BaseComponent;
import com.ekc.swiperight.ui.pref.SettingsFragment;

import butterknife.ButterKnife;

public abstract class BaseActivity
    extends AppCompatActivity
    implements BaseView {

  @LayoutRes protected int layoutId;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setLayoutId();
    setContentView(layoutId);
    bindView();
    setupToolbar();
    setTheme(SwipeRightApp.getThemeID());

    setupInjector();
    setupPresenter();
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (SettingsFragment.scheduledRestart) {
      SettingsFragment.scheduledRestart = false;
      Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(i);
    }
  }


  protected abstract void setLayoutId();

  protected abstract void setupInjector();

  protected abstract void setupPresenter();

  public abstract BaseComponent component();

  protected abstract void destroyPresenter();

  public void bindView() {
    ButterKnife.bind(this);
  }

  public void setupToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_base, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    destroyPresenter();
  }
}
