package com.ekc.swiperight.ui.base;

public abstract class BasePresenter<V extends BaseView> {
  protected V view;

  public abstract void initialize();

  public abstract void destroy();

  public <T extends V> void bindView(T view) {
    this.view = view;
  }

  public void unbindView() {
    this.view = null;
  }
}
