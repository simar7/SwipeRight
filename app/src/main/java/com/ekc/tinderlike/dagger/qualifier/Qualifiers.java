package com.ekc.tinderlike.dagger.qualifier;

import java.lang.annotation.Retention;
import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class Qualifiers {
  @Qualifier
  @Retention(RUNTIME)
  public @interface Token {
  }
}
