package ua.edu.ukma.fi.atsaruk.chatbot.service;

import com.google.cloud.dialogflow.v2.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DialogFlowService implements MessageSender {
  private final String projectId;
  private final String language;

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

      return result.getFulfillmentText();

    } catch (IOException e) {
      e.printStackTrace();
    }
    return "Я шось той во. Зламався.";
  }
}
