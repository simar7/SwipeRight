package com.ekc.tinderlike.model;

import com.google.gson.JsonArray;
import org.threeten.bp.LocalDate;

public class Match {
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

  boolean isMatch;

  public boolean isMatch() {
    return isMatch;
  }
}
