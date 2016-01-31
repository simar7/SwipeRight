package com.ekc.tinderlike.dagger.qualifier;

import java.lang.annotation.Retention;
import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class Qualifiers {
  @Qualifier
  @Retention(RUNTIME)
  public @interface Token {
  }

  @Qualifier
  @Retention(RUNTIME)
  public @interface FbToken {
  }

  @Qualifier
  @Retention(RUNTIME)
  public @interface FbId {
  }

  @Qualifier
  @Retention(RUNTIME)
  public @interface Mock {
  }
}
