package ua.edu.ukma.fi.atsaruk.chatbot.service.processor;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.edu.ukma.fi.atsaruk.chatbot.service.dataservice.TelegramChatService;
import ua.edu.ukma.fi.atsaruk.chatbot.service.dataservice.TelegramUserService;

@Service
public class TelegramMessageProcessor {
  private final TelegramUserService userService;
  private final TelegramChatService chatService;
  private final TelegramMessageTextProcessor textProcessor;


  public TelegramMessageProcessor(TelegramUserService userService,
                                  TelegramChatService chatService,
                                  TelegramMessageTextProcessor textProcessor) {
    this.userService = userService;
    this.chatService = chatService;
    this.textProcessor = textProcessor;

  }

  public void process(Message message, String token) {
    if (message.getFrom() != null) {
      userService.saveOrUpdate(message.getFrom());
    }
    if (message.getForwardFrom() != null) {
      userService.saveOrUpdate(message.getForwardFrom());
    }
    chatService.saveOrUpdate(message.getChat());
    if (message.hasText()) {
      textProcessor.process(message, token);
    }
  }
}
