package com.ekc.swiperight.model;

import com.google.gson.JsonObject;

public class AuthResponse {
  String token;
  JsonObject user;

  public String token() {
    return token;
  }

  public String user() {
    return user.toString();
  }
}
