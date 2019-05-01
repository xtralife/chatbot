package ua.edu.ukma.fi.atsaruk.chatbot.config;

import com.ibm.watson.assistant.v1.model.Context;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WatsonConfiguration {

  @Bean
  public Map<Long, Context> contextCache() {
    return new ConcurrentHashMap<>();
  }
}
