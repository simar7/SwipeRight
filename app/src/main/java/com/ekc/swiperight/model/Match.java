package com.ekc.swiperight.model;

import com.google.gson.JsonArray;
import org.threeten.bp.LocalDate;

public class Match {
  public static final Match NO_MATCH = new Match();

  String _id;
  boolean closed;
  int commonFriendCount;
  int commonLikeCount;
  LocalDate createdDate;
  boolean dead;
  LocalDate lastActivityDate;
  int messageCount;
  JsonArray messages;
  JsonArray participants;
  boolean pending;
  boolean isSuperLike;
  boolean following;
  boolean followingMoments;

  Recommendation recommendation = Recommendation.EMPTY;

  public void setRecommendation(Recommendation recommendation) {
    this.recommendation = recommendation;
  }

  public Recommendation recommendation() {
    return recommendation;
  }
}
