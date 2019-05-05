package ua.edu.ukma.fi.atsaruk.chatbot.entity.azure;

import java.util.List;

public class Question {
  private String question;
  private int top = 1;
  private String userId;
  private int scoreThreshold = 0;
  private boolean isTest = false;
  private List<NameValue> strictFilters;

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public int getTop() {
    return top;
  }

  public void setTop(int top) {
    this.top = top;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public int getScoreThreshold() {
    return scoreThreshold;
  }

  public void setScoreThreshold(int scoreThreshold) {
    this.scoreThreshold = scoreThreshold;
  }

  public boolean isTest() {
    return isTest;
  }

  public void setTest(boolean test) {
    isTest = test;
  }

  public List<NameValue> getStrictFilters() {
    return strictFilters;
  }

  public void setStrictFilters(
      List<NameValue> strictFilters) {
    this.strictFilters = strictFilters;
  }
}
