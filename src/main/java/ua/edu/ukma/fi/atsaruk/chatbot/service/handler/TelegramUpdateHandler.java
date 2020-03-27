package ua.edu.ukma.fi.atsaruk.chatbot.service.handler;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.edu.ukma.fi.atsaruk.chatbot.service.TelegramMessageProcessor;

/**
 * Handles {@link org.telegram.telegrambots.meta.api.objects.Update} objects
 */
@Service
public class TelegramUpdateHandler {
  private final TelegramMessageProcessor messageProcessor;

  public TelegramUpdateHandler(TelegramMessageProcessor messageProcessor) {
    this.messageProcessor = messageProcessor;
  }

  public void handle(Update update, String token) {
    final long chatId = update.getMessage().getChatId();
    final String text = update.getMessage().getText();
    messageProcessor.processMessage(token, chatId, text);
  }
}
