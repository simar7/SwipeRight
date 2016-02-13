package com.ekc.swiperight.ui.pref;

import com.ekc.swiperight.ui.base.BaseView;

public interface SettingsView extends BaseView {
  SettingsView EMPTY = new SettingsView() {
    @Override public void onAuthSuccess() {

    }

    @Override public void onAuthFailure() {

    }

    @Override public void hideLoading() {

    }

    @Override public void showLoading() {

    }
  };

  void onAuthSuccess();

  void onAuthFailure();

  void hideLoading();

  void showLoading();
}
