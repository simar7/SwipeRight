package com.ekc.swiperight.util;

import rx.Subscription;

public final class RxUtil {
  private RxUtil() {
    // no-op
  }

  public static boolean inFlight(final Subscription subscription) {
    return subscription != null && !subscription.isUnsubscribed();
  }

  public static void unSubscribeIfNeeded(final Subscription subscription) {
    if (inFlight(subscription)) {
      subscription.unsubscribe();
    }
  }

  public static void unSubscribeIfNeeded(final Subscription... subscriptions) {
    for (final Subscription subscription : subscriptions) {
      unSubscribeIfNeeded(subscription);
    }
  }
}
