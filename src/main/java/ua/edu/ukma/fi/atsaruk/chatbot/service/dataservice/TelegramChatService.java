package ua.edu.ukma.fi.atsaruk.chatbot.service.dataservice;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import ua.edu.ukma.fi.atsaruk.chatbot.dao.ChatRepository;

import static ua.edu.ukma.fi.atsaruk.chatbot.entity.dto.Chat.fromTelegram;

@Service
public class TelegramChatService {
  private final ChatRepository chatRepository;

  public TelegramChatService(ChatRepository chatRepository) {
    this.chatRepository = chatRepository;
  }

  public void saveOrUpdate(Chat chat) {
    chatRepository.saveOrUpdate(fromTelegram(chat));
  }
}
