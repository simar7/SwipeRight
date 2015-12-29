package com.ekc.tinderlike.ui.base;

public abstract class BasePresenter<V extends BaseView> {
  protected V view;

  protected abstract void initialize();

  protected abstract void destroy();

  protected <T extends V> void bindView(T view) {
    this.view = view;
  }

  protected void unbindView() {
    this.view = null;
  }
}
