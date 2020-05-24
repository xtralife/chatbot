package ua.edu.ukma.fi.atsaruk.chatbot.service;

import com.google.cloud.dialogflow.v2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.edu.ukma.fi.atsaruk.chatbot.entity.dto.ChatDetails;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DialogFlowService implements MessageSender {
  private static final Logger LOGGER = LoggerFactory.getLogger(DialogFlowService.class);

  private final String projectId;
  private final String language;
  //TODO Replace with persistent service
  private final Map<Long, ChatDetails> chats = new ConcurrentHashMap<>();

  public DialogFlowService(@Value("${chatbot.dialogflow.project-id}") String projectId,
                           @Value("${chatbot.dialogflow.language}") String language) {
    this.projectId = projectId;
    this.language = language;
  }

  @Override
  public String sendMessage(long chatId, String message) {
    try (SessionsClient client = SessionsClient.create()) {
      TextInput.Builder textInput = TextInput.newBuilder().setText(message).setLanguageCode(language);
      QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
      String sessionId = "session-" + chatId;
      SessionName session = SessionName.of(projectId, sessionId);

      DetectIntentResponse response = client.detectIntent(session, queryInput);

      QueryResult result = response.getQueryResult();
      if (result.getParameters().getFieldsCount() > 0) {
        updateChats(chatId, result.getParameters().getFieldsMap());
      }
      LOGGER.debug("Chat: {}", chats.get(chatId));

      return result.getFulfillmentText();

    } catch (IOException e) {
      LOGGER.error("Error while sending message to DialogFlow", e);
    }
    return "Я шось той во. Зламався.";
  }

  private void updateChats(long chatId, Map<String, com.google.protobuf.Value> parameters) {
    ChatDetails chat = chats.getOrDefault(chatId, new ChatDetails(chatId));
    Map<String, String> details = chat.getDetails();
    parameters.forEach((key, value) -> details.put(key, value.getStringValue()));
    chats.put(chatId, chat);
  }
}
