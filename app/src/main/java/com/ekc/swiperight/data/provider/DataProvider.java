package com.ekc.swiperight.data.provider;

import android.support.annotation.NonNull;

public abstract class DataProvider<T extends DataObserver> {
  final Class<T> observerType;

  DataProvider(Class<T> observerType) {
    this.observerType = observerType;
  }

  abstract void subscribe(@NonNull T observer);

  abstract void unsubscribe(@NonNull T observer);
}
