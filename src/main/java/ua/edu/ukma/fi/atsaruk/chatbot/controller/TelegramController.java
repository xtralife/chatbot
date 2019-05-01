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
import ua.edu.ukma.fi.atsaruk.chatbot.service.WatsonService;

@RestController
@RequestMapping(value = "telegram")
public class TelegramController {

  private static final Logger LOGGER = LoggerFactory.getLogger(TelegramController.class);

  private final TelegramService telegramService;
  private final WatsonService watsonService;

  public TelegramController(
      final TelegramService telegramService,
      final WatsonService watsonService) {
    this.telegramService = telegramService;
    this.watsonService = watsonService;
  }

  @PostMapping(value = "/{chatbotToken}")
  public ResponseEntity<Boolean> chatbotWebHook(
      @PathVariable final String chatbotToken,
      @RequestBody final Update data) {
    LOGGER.debug("POST telegram update received -> {}", data);

    final long chatId = data.getMessage().getChatId();
    final String text = data.getMessage().getText();
    final String reply = watsonService.sendMessage(chatId, text);
    telegramService.sendMessage(chatbotToken, chatId, reply);

    return ResponseEntity.ok(Boolean.TRUE);
  }
}
