package ua.edu.ukma.fi.atsaruk.chatbot.entity.azure;

public class FeedbackRecord {

  private String userId;
  private String userQuestion;
  private int qnaId;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserQuestion() {
    return userQuestion;
  }

  public void setUserQuestion(String userQuestion) {
    this.userQuestion = userQuestion;
  }

  public int getQnaId() {
    return qnaId;
  }

  public void setQnaId(int qnaId) {
    this.qnaId = qnaId;
  }
}
