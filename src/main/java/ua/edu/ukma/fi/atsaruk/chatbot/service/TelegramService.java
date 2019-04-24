package ua.edu.ukma.fi.atsaruk.chatbot.service;

import static java.lang.String.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class TelegramService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TelegramService.class);

  public void sendMessage(String token, long chatId, int messageId, String text) {
    RestTemplate restTemplate = new RestTemplate();

    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setReplyToMessageId(messageId);
    message.setText(text);

    ResponseEntity<String> response = restTemplate.postForEntity(
        format("https://api.telegram.org/bot%s/sendMessage", token), message, String.class);

    if (response.getStatusCode() == HttpStatus.OK) {
      String resp = response.getBody();
      LOGGER.debug("Response -> {}", resp);
    } else {
      LOGGER.error("Something wrong");
    }
  }
}
