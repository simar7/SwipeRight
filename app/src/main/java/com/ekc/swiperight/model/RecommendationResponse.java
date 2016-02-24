package com.ekc.swiperight.model;

import java.util.List;

import static com.ekc.swiperight.model.RecommendationResponse.Message.RECS_EXHAUSTED;

public class RecommendationResponse {
  String message;
  List<Recommendation> results;

  public List<Recommendation> getResults() {
    return results;
  }

  public boolean recsExhausted() {
    return RECS_EXHAUSTED.equals(message);
  }

  static class Message {
    public static final String RECS_EXHAUSTED = "recs exhausted";
  }
}
