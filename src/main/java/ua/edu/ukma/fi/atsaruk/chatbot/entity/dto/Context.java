package ua.edu.ukma.fi.atsaruk.chatbot.entity.dto;

import java.util.Map;

public class Context {
  private String id;
  private Map<String, String> parameters;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, String> parameters) {
    this.parameters = parameters;
  }
}
