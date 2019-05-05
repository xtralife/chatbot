package ua.edu.ukma.fi.atsaruk.chatbot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageSenderProvider implements Provider<MessageSender> {

  private static final Logger LOGGER = LoggerFactory.getLogger(MessageSenderProvider.class);

  private final Map<String, MessageSender> messageSenders = new ConcurrentHashMap<>();
  private final String defaultMessageSender;
  private String currentMessageSender;

  public MessageSenderProvider(
      @Value("${chatbot.defaultMessageSender}") String defaultMessageSender) {
    this.defaultMessageSender = defaultMessageSender;
  }

  public synchronized boolean setCurrentMessageSender(String messageSender) {
    LOGGER.debug("Trying to change current message sender from {} to {}", currentMessageSender,
        messageSender);

    if (messageSenders.containsKey(messageSender)) {
      currentMessageSender = messageSender;
      LOGGER.info("Current message sender changed to {}", messageSender);
      return true;
    }
    LOGGER.debug("Current message sender {} was not changed", currentMessageSender);
    return false;
  }

  public List<String> getAvailableMessageSenders() {
    return new ArrayList<>(messageSenders.keySet());
  }

  public void registerMessageSender(String name, MessageSender sender) {
    LOGGER.debug("Registering message sender {}", name);
    messageSenders.put(name, sender);
  }

  @Override
  public MessageSender get() {
    if (currentMessageSender != null && messageSenders.containsKey(currentMessageSender)) {
      return messageSenders.get(currentMessageSender);
    }

    if (defaultMessageSender != null && messageSenders.containsKey(defaultMessageSender)) {
      return messageSenders.get(defaultMessageSender);
    }

    LOGGER.warn("No message sender was found. Returning echo message sender");
    return (chatId, message) -> "You've said: " + message;
  }
}
