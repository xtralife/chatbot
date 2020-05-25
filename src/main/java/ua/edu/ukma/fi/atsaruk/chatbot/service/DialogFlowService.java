package ua.edu.ukma.fi.atsaruk.chatbot.service;

import com.google.cloud.dialogflow.v2.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.edu.ukma.fi.atsaruk.chatbot.dao.ContextRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DialogFlowService implements MessageSender {
  private static final Logger LOGGER = LoggerFactory.getLogger(DialogFlowService.class);

  private final String projectId;
  private final String language;
  private final List<String> contextParameters;
  private final ContextRepository repository;

  public DialogFlowService(@Value("${chatbot.dialogflow.project-id}") String projectId,
                           @Value("${chatbot.dialogflow.language}") String language,
                           @Value("${chatbot.dialogflow.contexts.parameters}") List<String> contextParameters,
                           ContextRepository repository) {
    this.projectId = projectId;
    this.language = language;
    this.contextParameters = contextParameters;
    this.repository = repository;
  }

  @Override
  public String sendMessage(long chatId, String message) {
    try (SessionsClient client = SessionsClient.create()) {
      String sessionId = Long.toString(chatId);
      SessionName session = SessionName.of(projectId, sessionId);

      TextInput.Builder textInput = TextInput.newBuilder().setText(message).setLanguageCode(language);
      QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

      DetectIntentResponse response = client.detectIntent(session, queryInput);
      QueryResult result = response.getQueryResult();
      updateContexts(result.getOutputContextsList());

      if (StringUtils.isNotBlank(result.getFulfillmentText())) {
        return result.getFulfillmentText();
      }
      if (!result.getFulfillmentMessagesList().isEmpty()) {
        return result.getFulfillmentMessagesList().stream()
            .map(Intent.Message::getText)
            .map(Intent.Message.Text::getTextList)
            .map(Collection::stream)
            .flatMap(Function.identity())
            .collect(Collectors.joining(System.lineSeparator()));
      }

      return null;
    } catch (IOException e) {
      LOGGER.error("Error while sending message to DialogFlow", e);
    }
    return "Я шось той во. Зламався.";
  }

  private void updateContexts(Iterable<Context> contexts) {
    StreamSupport.stream(contexts.spliterator(), false)
        .forEach(context -> {
          Map<String, String> parameters = context.getParameters().getFieldsMap().entrySet().stream()
              .filter(entry -> contextParameters.contains(entry.getKey()))
              .filter(entry -> isNotBlank(entry.getValue()))
              .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getStringValue()));
          if (parameters.isEmpty()) {
            return;
          }
          ua.edu.ukma.fi.atsaruk.chatbot.entity.dto.Context dto = new ua.edu.ukma.fi.atsaruk.chatbot.entity.dto.Context();
          dto.setId(context.getName());
          dto.setParameters(parameters);
          repository.saveOrUpdate(dto);
          LOGGER.debug("Updated context: {}\nParameters: {}", context.getName(), parameters);
        });
    LOGGER.debug("Updating context");
  }

  private boolean isNotBlank(com.google.protobuf.Value value) {
    return value != null && StringUtils.isNotBlank(value.getStringValue());
  }
}
