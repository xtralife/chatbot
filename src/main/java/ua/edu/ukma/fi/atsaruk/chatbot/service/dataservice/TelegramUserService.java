package ua.edu.ukma.fi.atsaruk.chatbot.service.dataservice;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ua.edu.ukma.fi.atsaruk.chatbot.dao.UserRepository;

import static ua.edu.ukma.fi.atsaruk.chatbot.entity.dto.User.fromTelegram;

@Service
public class TelegramUserService {
  private final UserRepository userRepository;

  public TelegramUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void saveOrUpdate(User user) {
    userRepository.saveOrUpdate(fromTelegram(user));
  }
}
