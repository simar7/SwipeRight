package com.ekc.swiperight.model;

public class AuthRequest {
  String facebookToken;
  String facebookId;
  String locale;

  private AuthRequest(Builder builder) {
    facebookToken = builder.facebookToken;
    facebookId = builder.facebookId;
    locale = builder.locale;
  }

  public static final class Builder {
    private String facebookToken;
    private String facebookId;
    private String locale;

    public Builder() {}

    public Builder facebookToken(String val) {
      facebookToken = val;
      return this;
    }

    public Builder facebookId(String val) {
      facebookId = val;
      return this;
    }

    public Builder locale(String val) {
      locale = val;
      return this;
    }

    public AuthRequest build() {return new AuthRequest(this);}
  }
}
