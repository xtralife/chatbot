package ua.edu.ukma.fi.atsaruk.chatbot.entity.azure;

import java.util.List;

public class Context {

  private boolean isContextOnly;
  private List<String> prompts;

  public boolean isContextOnly() {
    return isContextOnly;
  }

  public void setContextOnly(boolean contextOnly) {
    isContextOnly = contextOnly;
  }

  public List<String> getPrompts() {
    return prompts;
  }

  public void setPrompts(List<String> prompts) {
    this.prompts = prompts;
  }
}
