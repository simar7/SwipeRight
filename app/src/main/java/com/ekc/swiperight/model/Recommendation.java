package com.ekc.swiperight.model;

import com.google.gson.JsonArray;
import org.threeten.bp.LocalDate;

public class Recommendation {
  public static final Recommendation EMPTY = new Recommendation();

  int distanceMi;
  int connectionCount;
  int commonLikeCount;
  JsonArray commonLikes;
  JsonArray commonFriends;
  String _id;
  String bio;
  LocalDate birthDate;
  int gender;
  String name;
  LocalDate pingTime;
  JsonArray photos;
  JsonArray jobs;
  JsonArray schools;

  boolean liked;
  boolean matched;

  public String name() {
    return name;
  }

  public String id() {
    return _id;
  }

  public String bio() {
    return bio;
  }

  public boolean liked() {
    return liked;
  }

  public boolean matched() {
    return matched;
  }

  public void setLiked() {
    liked = true;
  }

  public void setMatched() {
    matched = true;
  }
}
