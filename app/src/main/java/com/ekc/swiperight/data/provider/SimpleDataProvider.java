package com.ekc.swiperight.data.provider;

import android.support.annotation.NonNull;

public class SimpleDataProvider<T extends DataObserver> extends DataProvider<T>{
  private T observer;

  SimpleDataProvider(Class<T> observerType) {
    super(observerType);
  }

  @Override void subscribe(@NonNull T observer) {
    this.observer = observer;
  }

  @Override void unsubscribe(@NonNull T observer) {
    if (this.observer == observer) {
      this.observer = null;
    }
  }
}
