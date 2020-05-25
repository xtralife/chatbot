package ua.edu.ukma.fi.atsaruk.chatbot.entity.dto;

public class Chat {
  private static final String USER_CHAT_TYPE = "private";
  private static final String GROUP_CHAT_TYPE = "group";
  private static final String CHANNEL_CHAT_TYPE = "channel";

  private Long id;
  private String source;
  private String type;
  private String title;
  private String username;
  private String firstName;
  private String lastName;
  private String description;
  private String inviteLink;

  public static Chat fromTelegram(org.telegram.telegrambots.meta.api.objects.Chat telegramChat) {
    Chat chat = new Chat();
    chat.setSource(Source.TELEGRAM.getValue());
    chat.setId(telegramChat.getId());
    if (telegramChat.isUserChat()) {
      chat.setType(USER_CHAT_TYPE);
    }
    if (telegramChat.isGroupChat()) {
      chat.setType(GROUP_CHAT_TYPE);
    }
    if (telegramChat.isChannelChat()) {
      chat.setType(CHANNEL_CHAT_TYPE);
    }
    chat.setTitle(telegramChat.getTitle());
    chat.setUsername(telegramChat.getUserName());
    chat.setFirstName(telegramChat.getFirstName());
    chat.setLastName(telegramChat.getLastName());
    chat.setDescription(telegramChat.getDescription());
    chat.setInviteLink(telegramChat.getInviteLink());
    return chat;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getInviteLink() {
    return inviteLink;
  }

  public void setInviteLink(String inviteLink) {
    this.inviteLink = inviteLink;
  }

  @Override
  public String toString() {
    return "Chat{" +
        "id=" + id +
        ", source='" + source + '\'' +
        ", type='" + type + '\'' +
        ", title='" + title + '\'' +
        ", username='" + username + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", description='" + description + '\'' +
        ", inviteLink='" + inviteLink + '\'' +
        '}';
  }
}
