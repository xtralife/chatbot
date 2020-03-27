package ua.edu.ukma.fi.atsaruk.chatbot.service.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.edu.ukma.fi.atsaruk.chatbot.exception.ChatbotException;
import ua.edu.ukma.fi.atsaruk.chatbot.service.processor.TelegramMessageProcessor;

import static java.lang.String.format;

/**
 * Handles {@link org.telegram.telegrambots.meta.api.objects.Update} objects
 */
@Service
public class TelegramUpdateHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(TelegramUpdateHandler.class);

  private final TelegramMessageProcessor messageProcessor;

  public TelegramUpdateHandler(TelegramMessageProcessor messageProcessor) {
    this.messageProcessor = messageProcessor;
  }

  public void handle(Update update, String token) {
    if (update.hasMessage()) {
      messageProcessor.process(update.getMessage(), token);
      return;
    }

    logUpdateContent(update);

    throw new ChatbotException(format("Update content is not supported yet. Payload: %s", update));
  }

  private void logUpdateContent(Update update) {
    if (update.hasEditedMessage()) {
      LOGGER.info("Update {} contains EditedMessage: {}", update.getUpdateId(), update.getEditedMessage());
    }
    if (update.hasChannelPost()) {
      LOGGER.info("Update {} contains ChannelPost: {}", update.getUpdateId(), update.getChannelPost());
    }
    if (update.hasEditedChannelPost()) {
      LOGGER.info("Update {} contains EditedChannelPost: {}", update.getUpdateId(), update.getEditedChannelPost());
    }
    if (update.hasInlineQuery()) {
      LOGGER.info("Update {} contains InlineQuery: {}", update.getUpdateId(), update.getInlineQuery());
    }
    if (update.hasChosenInlineQuery()) {
      LOGGER.info("Update {} contains ChosenInlineQuery: {}", update.getUpdateId(), update.getChosenInlineQuery());
    }
    if (update.hasCallbackQuery()) {
      LOGGER.info("Update {} contains CallbackQuery: {}", update.getUpdateId(), update.getCallbackQuery());
    }
    if (update.hasShippingQuery()) {
      LOGGER.info("Update {} contains ShippingQuery: {}", update.getUpdateId(), update.getShippingQuery());
    }
    if (update.hasPreCheckoutQuery()) {
      LOGGER.info("Update {} contains PreCheckoutQuery: {}", update.getUpdateId(), update.getPreCheckoutQuery());
    }
    if (update.hasPoll()) {
      LOGGER.info("Update {} contains Poll: {}", update.getUpdateId(), update.getPoll());
    }
    if (update.hasPollAnswer()) {
      LOGGER.info("Update {} contains PollAnswer: {}", update.getUpdateId(), update.getPollAnswer());
    }
  }
}
