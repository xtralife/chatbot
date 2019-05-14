package ua.edu.ukma.fi.atsaruk.chatbot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.edu.ukma.fi.atsaruk.chatbot.service.TelegramMessageProcessor;

@RestController
@RequestMapping(value = "telegram")
public class TelegramController {

  private static final Logger LOGGER = LoggerFactory.getLogger(TelegramController.class);

  private final TelegramMessageProcessor messageProcessor;

  public TelegramController(
      final TelegramMessageProcessor telegramMessageProcessor) {
    this.messageProcessor = telegramMessageProcessor;
  }

  @PostMapping(value = "/{chatbotToken}")
  public ResponseEntity<Boolean> chatbotWebHook(
      @PathVariable final String chatbotToken,
      @RequestBody final Update data) {
    LOGGER.debug("POST telegram update received -> {}", data);

    final long chatId = data.getMessage().getChatId();
    final String text = data.getMessage().getText();
    messageProcessor.processMessage(chatbotToken, chatId, text);

    return ResponseEntity.ok(Boolean.TRUE);
  }
}
