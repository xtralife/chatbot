package ua.edu.ukma.fi.atsaruk.chatbot.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ibm.watson.assistant.v2.model.MessageContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WatsonConfiguration {

  @Bean
  public Map<Long, MessageContext> contextCache() {
    return new ConcurrentHashMap<>();
  }
}
