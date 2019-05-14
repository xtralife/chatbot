package ua.edu.ukma.fi.atsaruk.chatbot.service;

import static java.lang.String.format;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TelegramMessageProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(TelegramMessageProcessor.class);

  private static final String LIST_SERVICES = "/list_services";
  private static final String SET_SERVICE = "/set_service";

  private final TelegramService telegramService;

  private final MessageSenderProvider provider;


  public TelegramMessageProcessor(
      TelegramService telegramService,
      MessageSenderProvider provider) {
    this.telegramService = telegramService;
    this.provider = provider;
  }

  public void processMessage(final String token, final long chatId, final String text) {
    if (isCommand(text)) {
      doCommand(token, chatId, text);
      return;
    }

    final String reply = provider.get().sendMessage(chatId, text);
    telegramService.sendMessage(token, chatId, reply);
  }

  private boolean isCommand(String text) {
    if (text == null) {
      return false;
    }
    if (text.startsWith(LIST_SERVICES)) {
      return true;
    }
    if (text.startsWith(SET_SERVICE)) {
      return true;
    }
    return false;
  }

  private void doCommand(String token, long chatId, String text) {
    if (text == null) {
      return;
    }
    if (text.startsWith(LIST_SERVICES)) {
      doListServices(token, chatId);
    }
    if (text.startsWith(SET_SERVICE)) {
      doSetService(token, chatId, text.substring(SET_SERVICE.length()));
    }
  }

  private void doListServices(String token, long chatId) {
    LOGGER.debug("Trying to list available services");

    List<String> services = provider.getAvailableMessageSenders();
    final StringBuilder sb = new StringBuilder("Available services:\n");
    services.forEach(provider -> sb.append(provider).append("\n"));

    telegramService.sendMessage(token, chatId, sb.toString());
  }

  private void doSetService(String token, long chatId, String text) {
    final String serviceName = text.trim();
    LOGGER.debug("Trying to set service to value '{}'", serviceName);

    if (!provider.getAvailableMessageSenders().contains(serviceName)) {
      telegramService.sendMessage(token, chatId,
          format("No such service available. Use %s command to see available services",
              LIST_SERVICES));
      return;
    }

    provider.setCurrentMessageSender(serviceName);
    telegramService
        .sendMessage(token, chatId, format("Current service value is set to '%s'", serviceName));
  }
}
