package com.ekc.tinderlike.ui.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;
import com.ekc.tinderlike.R;
import com.ekc.tinderlike.dagger.component.BaseComponent;
import javax.inject.Inject;

public abstract class BaseActivity<T extends BasePresenter, C extends BaseComponent>
    extends AppCompatActivity
    implements BaseView {

  @Inject protected T presenter;
  @LayoutRes protected int layoutId;
  protected C component;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setLayoutId();
    setContentView(layoutId);
    bindView();
    setupInjector();
    bindPresenter();
    setupToolbar();
  }

  protected abstract void setLayoutId();

  protected abstract void setupInjector();

  public void bindView() {
    ButterKnife.bind(this);
  }

  public void bindPresenter() {
    presenter.initialize();
    presenter.bindView(this);
  }

  public void setupToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_base, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    presenter.unbindView();
    presenter.destroy();
  }

  public C component() {
    return component;
  }
}
