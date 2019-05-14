package ua.edu.ukma.fi.atsaruk.chatbot.entity.azure;

import java.util.List;

public class FeedbackResponse {

  private List<FeedbackRecord> feedbackRecords;

  public List<FeedbackRecord> getFeedbackRecords() {
    return feedbackRecords;
  }

  public void setFeedbackRecords(
      List<FeedbackRecord> feedbackRecords) {
    this.feedbackRecords = feedbackRecords;
  }
}
