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
import ua.edu.ukma.fi.atsaruk.chatbot.service.handler.TelegramUpdateHandler;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "telegram")
public class TelegramController {

  private static final Logger LOGGER = LoggerFactory.getLogger(TelegramController.class);
  private final TelegramUpdateHandler handler;

  public TelegramController(final TelegramUpdateHandler handler) {
    this.handler = handler;
  }

  @PostMapping(value = "/{token}")
  public ResponseEntity<Boolean> chatbotWebHook(
      @PathVariable final String token,
      @RequestBody final Update data) {
    LOGGER.debug("POST telegram update received -> {}", data);

//    CompletableFuture.runAsync(() -> handler.handle(data, token));
    handler.handle(data, token);

    return ResponseEntity.ok(Boolean.TRUE);
  }
}
