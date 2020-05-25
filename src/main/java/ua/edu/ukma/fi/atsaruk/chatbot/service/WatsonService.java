package ua.edu.ukma.fi.atsaruk.chatbot.service;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WatsonService implements MessageSender {

  private final Assistant assistant;
  private final String assistantId;
  //TODO Replace with cache manager in the future
  private final Map<Long, MessageContext> contextCache;
  private final Map<Long, String> sessions = new ConcurrentHashMap<>();

  public WatsonService(
      @Value("${chatbot.watson.api-key}") final String apiKey,
      @Value("${chatbot.watson.version}") final String version,
      @Value("${chatbot.watson.endpoint}") final String endpoint,
      @Value("${chatbot.watson.assistant-id}") final String assistantId,
      final Map<Long, MessageContext> contextCache) {

    IamAuthenticator authenticator = new IamAuthenticator(apiKey);
    this.assistant = new Assistant(version, authenticator);
    this.assistant.setServiceUrl(endpoint);

    this.assistantId = assistantId;
    this.contextCache = contextCache;
  }

  @Override
  public String sendMessage(long chatId, String message) {
//    final MessageContext context;
//    if (contextCache.containsKey(chatId)) {
//      context = contextCache.get(chatId);
//    } else {
//      context = new MessageContext.Builder().build();
//    }

    String sessionId;
    if (sessions.containsKey(chatId)) {
      sessionId = sessions.get(chatId);
    } else {
      sessionId = createNewSession();
      sessions.put(chatId, sessionId);
    }
    MessageInputOptions inputOptions = new MessageInputOptions.Builder().returnContext(true).debug(true).build();
    MessageInput input = new MessageInput.Builder()
        .options(inputOptions)
        .messageType(MessageInput.MessageType.TEXT)
        .text(message)
        .build();
    MessageOptions options = new MessageOptions.Builder()
        .assistantId(assistantId)
        .sessionId(sessionId)
        .input(input)
        .build();

    MessageResponse messageResponse = assistant.message(options).execute().getResult();
    contextCache.put(chatId, messageResponse.getContext());

    return messageResponse.getOutput().getGeneric().stream()
        .map(RuntimeResponseGeneric::text)
        .reduce((a, b) -> a + System.lineSeparator() + b)
        .orElse("");
  }

  private String createNewSession() {
    CreateSessionOptions options = new CreateSessionOptions.Builder(assistantId).build();
    SessionResponse response = assistant.createSession(options).execute().getResult();
    return response.getSessionId();
  }
}
