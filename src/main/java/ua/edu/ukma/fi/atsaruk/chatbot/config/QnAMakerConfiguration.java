package ua.edu.ukma.fi.atsaruk.chatbot.config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.edu.ukma.fi.atsaruk.chatbot.entity.azure.Answer;

@Configuration
public class QnAMakerConfiguration {

  @Bean
  public Map<Long, String> questionCache() {
    return new ConcurrentHashMap<>();
  }

  @Bean
  public Map<Long, List<Answer>> answerCache() {
    return new ConcurrentHashMap<>();
  }
}
