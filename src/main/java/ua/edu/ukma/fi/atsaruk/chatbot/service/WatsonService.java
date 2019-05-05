package ua.edu.ukma.fi.atsaruk.chatbot.service;

import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.assistant.v1.Assistant;
import com.ibm.watson.assistant.v1.model.Context;
import com.ibm.watson.assistant.v1.model.MessageInput;
import com.ibm.watson.assistant.v1.model.MessageOptions;
import com.ibm.watson.assistant.v1.model.MessageResponse;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WatsonService implements MessageSender {

  private final Assistant assistant;
  private final String workspaceId;
  //TODO Replace with cache manager in the future
  private final Map<Long, Context> contextCache;

  public WatsonService(
      @Value("${chatbot.watson.apiKey}") final String apiKey,
      @Value("${chatbot.watson.version}") final String version,
      @Value("${chatbot.watson.endpoint}") final String endpoint,
      @Value("${chatbot.watson.workspaceId}") final String workspaceId,
      final Map<Long, Context> contextCache) {

    IamOptions options = new IamOptions.Builder()
        .apiKey(apiKey)
        .build();

    this.assistant = new Assistant(version, options);
    this.assistant.setEndPoint(endpoint);
    this.assistant.setApiKey(apiKey);

    this.workspaceId = workspaceId;
    this.contextCache = contextCache;
  }

  @Override
  public String sendMessage(long chatId, String message) {
    final Context context;
    if (contextCache.containsKey(chatId)) {
      context = contextCache.get(chatId);
    } else {
      context = new Context();
    }

    MessageInput input = new MessageInput();
    input.setText(message);

    MessageOptions messageOptions = new MessageOptions
        .Builder(workspaceId)
        .input(input)
        .context(context)
        .build();

    MessageResponse messageResponse = assistant.message(messageOptions).execute().getResult();
    contextCache.put(chatId, messageResponse.getContext());

    return messageResponse.getOutput().getText().stream()
        .reduce((a, b) -> a + System.lineSeparator() + b)
        .orElse("");
  }
}
