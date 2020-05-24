package ua.edu.ukma.fi.atsaruk.chatbot.entity.dto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatDetails {
  private Long id;
  private Map<String, String> details = new ConcurrentHashMap<>();

  public ChatDetails(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Map<String, String> getDetails() {
    return details;
  }

  public void setDetails(Map<String, String> details) {
    this.details = details;
  }

  @Override
  public String toString() {
    return "ChatDetails{" +
        "id=" + id +
        ", details=" + details +
        '}';
  }
}
