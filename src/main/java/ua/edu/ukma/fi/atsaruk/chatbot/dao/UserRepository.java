package ua.edu.ukma.fi.atsaruk.chatbot.dao;

import org.springframework.stereotype.Repository;
import ua.edu.ukma.fi.atsaruk.chatbot.entity.dto.User;
import ua.edu.ukma.fi.atsaruk.chatbot.exception.ChatbotException;

@Repository
public class UserRepository {
  public void saveOrUpdate(User user) {
    throw new ChatbotException("User Repository is not yet implemented");
  }
}
