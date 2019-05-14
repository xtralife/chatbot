package ua.edu.ukma.fi.atsaruk.chatbot.entity.azure;

import java.util.List;

public class Answer {

  private List<String> questions;
  private String answer;
  private int score;
  private int id;
  private String source;
  private List<NameValue> metadata;
  private Context context;

  public List<String> getQuestions() {
    return questions;
  }

  public void setQuestions(List<String> questions) {
    this.questions = questions;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public List<NameValue> getMetadata() {
    return metadata;
  }

  public void setMetadata(List<NameValue> metadata) {
    this.metadata = metadata;
  }

  public Context getContext() {
    return context;
  }

  public void setContext(Context context) {
    this.context = context;
  }
}
