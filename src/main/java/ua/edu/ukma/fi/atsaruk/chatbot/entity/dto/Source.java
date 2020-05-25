package ua.edu.ukma.fi.atsaruk.chatbot.entity.dto;

public enum Source {
  TELEGRAM("telegram");

  private String value;

  Source(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
