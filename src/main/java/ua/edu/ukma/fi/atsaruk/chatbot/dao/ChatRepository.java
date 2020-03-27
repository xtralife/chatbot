package ua.edu.ukma.fi.atsaruk.chatbot.dao;

import org.springframework.stereotype.Repository;
import ua.edu.ukma.fi.atsaruk.chatbot.entity.dto.Chat;
import ua.edu.ukma.fi.atsaruk.chatbot.exception.ChatbotException;

@Repository
public class ChatRepository {
  public void saveOrUpdate(Chat chat) {
    throw new ChatbotException("Chat Repository is not yet implemented");
  }
}
