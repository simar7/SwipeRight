package com.ekc.tinderlike.ui.pref;

import com.ekc.tinderlike.ui.base.BaseView;

public interface SettingsView extends BaseView {
  void onAuthSuccess();

  void onAuthFailure();

  void hideLoading();

  void showLoading();
}
