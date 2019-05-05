package ua.edu.ukma.fi.atsaruk.chatbot.entity.azure;

import java.util.List;

public class AnswerResponse {

  private List<Answer> answers;
  private String debugInfo;

  public List<Answer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<Answer> answers) {
    this.answers = answers;
  }

  public String getDebugInfo() {
    return debugInfo;
  }

  public void setDebugInfo(String debugInfo) {
    this.debugInfo = debugInfo;
  }
}
