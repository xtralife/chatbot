package ua.edu.ukma.fi.atsaruk.chatbot.entity.dto;

public class User {
  private Integer id;
  private String source;
  private String username;
  private String firstName;
  private String lastName;
  private Boolean isBot;
  private String languageCode;

  public static User fromTelegram(org.telegram.telegrambots.meta.api.objects.User telegramUser) {
    User user = new User();
    user.setSource(Source.TELEGRAM.getValue());
    user.setId(telegramUser.getId());
    user.setUsername(telegramUser.getUserName());
    user.setFirstName(telegramUser.getFirstName());
    user.setLastName(telegramUser.getLastName());
    user.setLanguageCode(telegramUser.getLanguageCode());
    user.setBot(telegramUser.getBot());
    return user;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Boolean getBot() {
    return isBot;
  }

  public void setBot(Boolean bot) {
    isBot = bot;
  }

  public String getLanguageCode() {
    return languageCode;
  }

  public void setLanguageCode(String languageCode) {
    this.languageCode = languageCode;
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", source='" + source + '\'' +
        ", username='" + username + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", isBot=" + isBot +
        ", languageCode='" + languageCode + '\'' +
        '}';
  }
}
