package ua.edu.ukma.fi.atsaruk.chatbot.service;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.edu.ukma.fi.atsaruk.chatbot.entity.azure.Answer;
import ua.edu.ukma.fi.atsaruk.chatbot.entity.azure.AnswerResponse;
import ua.edu.ukma.fi.atsaruk.chatbot.entity.azure.FeedbackRecord;
import ua.edu.ukma.fi.atsaruk.chatbot.entity.azure.FeedbackResponse;
import ua.edu.ukma.fi.atsaruk.chatbot.entity.azure.Question;

@Service
public class AzureQnAService implements MessageSender {

  private static final Logger LOGGER = LoggerFactory.getLogger(AzureQnAService.class);

  private static final String AZURE_BASE_URL = "https://%s.azurewebsites.net/qnamaker/knowledgebases/%s";
  private static final String AZURE_GET_ANSWER = AZURE_BASE_URL + "/generateAnswer";
  private static final String AZURE_SEND_FEEDBACK = AZURE_BASE_URL + "/train";

  private final String qnaServiceName;
  private final String knowledgeBaseId;
  private final String endpointKey;

  private Map<Long, String> questionCache;
  private Map<Long, List<Answer>> answerCache;

  public AzureQnAService(
      @Value("${chatbot.azure.qnaServiceName}") final String qnaServiceName,
      @Value("${chatbot.azure.knowledgeBaseId}") final String knowledgeBaseId,
      @Value("${chatbot.azure.endpointKey}") final String endpointKey,
      final Map<Long, String> questionCache,
      final Map<Long, List<Answer>> answerCache) {

    this.qnaServiceName = qnaServiceName;
    this.knowledgeBaseId = knowledgeBaseId;
    this.endpointKey = endpointKey;
    this.questionCache = questionCache;
    this.answerCache = answerCache;
  }

  @Override
  public String sendMessage(long chatId, String message) {
    if (questionCache.containsKey(chatId)) {
      return sendExplicitFeedback(chatId, message);
    }

    RestTemplate restTemplate = new RestTemplate();

    Question question = new Question();
    question.setQuestion(message);
    question.setTop(5);

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION, format("EndpointKey %s", endpointKey));

    ResponseEntity<AnswerResponse> response = restTemplate.postForEntity(
        format(AZURE_GET_ANSWER, qnaServiceName, knowledgeBaseId),
        new HttpEntity<>(question, headers),
        AnswerResponse.class);

    if (response.getStatusCode() != HttpStatus.OK) {
      LOGGER.error("Error while sending question to QnA Maker. Status code: {}",
          response.getStatusCodeValue());
      return "Sorry, Azure QnA Service is temporarily unavailable";
    }

    AnswerResponse answerResponse = response.getBody();

    requireNonNull(answerResponse);

    List<Answer> answers = answerResponse.getAnswers();
    if (answers == null || answers.isEmpty()) {
      return "There are no answers to your question. Please try to rephrase it.";
    }

    if (answers.size() == 1) {
      return answers.get(0).getAnswer();
    }

    final StringBuilder sb = new StringBuilder(
        "Multiple answers received from service. Please reply with number of answer that suite you most:\n");

    final AtomicInteger index = new AtomicInteger(1);
    answers.forEach(
        answer -> sb.append(index.getAndIncrement()).append(". ").append(answer.getAnswer())
            .append("\n"));
    questionCache.put(chatId, message);
    answerCache.put(chatId, answers);

    return sb.toString();
  }

  private String sendExplicitFeedback(long chatId, String text) {
    final int answerNumber;
    try {
      answerNumber = Integer.parseInt(text);
    } catch (NumberFormatException ex) {
      return "Wrong reply. Use number of answer, e.g. 1";
    }
    final List<Answer> answers = answerCache.get(chatId);
    if (answerNumber > answers.size()) {
      return "Number is too big. Try again.";
    }

    final Answer answer = answers.get(answerNumber - 1);

    FeedbackRecord feedback = new FeedbackRecord();
    feedback.setUserId("" + chatId);
    feedback.setUserQuestion(questionCache.get(chatId));
    feedback.setQnaId(answer.getId());
    FeedbackResponse feedbackResponse = new FeedbackResponse();
    feedbackResponse.setFeedbackRecords(Collections.singletonList(feedback));

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION, format("EndpointKey %s", endpointKey));

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<Void> response = restTemplate.postForEntity(
        format(AZURE_SEND_FEEDBACK, qnaServiceName, knowledgeBaseId),
        new HttpEntity<>(feedbackResponse, headers),
        Void.class);

    questionCache.remove(chatId);
    answerCache.remove(chatId);

    if (response.getStatusCode() != HttpStatus.NO_CONTENT) {
      LOGGER.error("Error while sending feedback to QnA Maker. Status code: {}",
          response.getStatusCodeValue());
      return "Sorry, we could not send your feedback to Azure QnA Maker Service";
    }

    return "Your feedback was successfully sent to Azure QnA Maker Service";
  }
}
