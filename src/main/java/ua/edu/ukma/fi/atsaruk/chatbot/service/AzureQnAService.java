package ua.edu.ukma.fi.atsaruk.chatbot.service;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.util.List;
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
import ua.edu.ukma.fi.atsaruk.chatbot.entity.azure.Question;

@Service
public class AzureQnAService implements MessageSender {

  private static final Logger LOGGER = LoggerFactory.getLogger(AzureQnAService.class);

  private static final String AZURE_URL = "https://%s.azurewebsites.net/qnamaker/knowledgebases/%s/generateAnswer";

  private final String qnaServiceName;
  private final String knowledgeBaseId;
  private final String endpointKey;

  public AzureQnAService(
      @Value("${chatbot.azure.qnaServiceName}") final String qnaServiceName,
      @Value("${chatbot.azure.knowledgeBaseId}") final String knowledgeBaseId,
      @Value("${chatbot.azure.endpointKey}") final String endpointKey) {

    this.qnaServiceName = qnaServiceName;
    this.knowledgeBaseId = knowledgeBaseId;
    this.endpointKey = endpointKey;
  }

  @Override
  public String sendMessage(long chatId, String message) {
    RestTemplate restTemplate = new RestTemplate();

    Question question = new Question();
    question.setQuestion(message);

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION, format("EndpointKey %s", endpointKey));

    ResponseEntity<AnswerResponse> response = restTemplate.postForEntity(
        format(AZURE_URL, qnaServiceName, knowledgeBaseId), new HttpEntity<>(question, headers),
        AnswerResponse.class);

    if (response.getStatusCode() != HttpStatus.OK) {
      LOGGER.error("Something wrong");
      return "Sorry, Azure QnA Service is temporarily unavailable";
    }

    AnswerResponse answerResponse = response.getBody();

    requireNonNull(answerResponse);

    List<Answer> answers = answerResponse.getAnswers();
    if (answers == null || answers.isEmpty()) {
      return "There are no answers to your question. Please try to rephrase it.";
    }

    return answers.get(0).getAnswer();
  }
}
