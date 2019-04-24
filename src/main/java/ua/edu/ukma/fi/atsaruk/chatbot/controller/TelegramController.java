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
import ua.edu.ukma.fi.atsaruk.chatbot.service.TelegramService;

@RestController
@RequestMapping(value = "telegram")
public class TelegramController {

  private static final Logger LOGGER = LoggerFactory.getLogger(TelegramController.class);

  private final TelegramService telegramService;

  public TelegramController(TelegramService telegramService) {
    this.telegramService = telegramService;
  }

  @PostMapping(value = "/{chatbotToken}")
  public ResponseEntity<Boolean> chatbotWebHook(
      @PathVariable final String chatbotToken,
      @RequestBody final Update data) {
    LOGGER.debug("POST telegram update received -> {}", data);
    telegramService
        .sendMessage(chatbotToken, data.getMessage().getChatId(), data.getMessage().getMessageId(),
            data.getMessage().getText());

    return ResponseEntity.ok(Boolean.TRUE);
  }
}
