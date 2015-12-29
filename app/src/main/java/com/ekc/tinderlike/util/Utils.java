package com.ekc.tinderlike.util;

/**
 * Utility holder class. Static field variables hold utility methods for relevant tasks.
 */
public final class Utils {
  public static final UiUtils UI = UiUtils.UTILS;
  public static final StringUtils STRING = StringUtils.UTILS;

  private Utils() {
    throw new InstantiationError("Cannot instantiate utility class");
  }
}
