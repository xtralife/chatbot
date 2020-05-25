package ua.edu.ukma.fi.atsaruk.chatbot.config;

import org.springframework.context.annotation.Configuration;
import ua.edu.ukma.fi.atsaruk.chatbot.service.MessageSender;
import ua.edu.ukma.fi.atsaruk.chatbot.service.MessageSenderProvider;

@Configuration
public class ChatbotConfiguration {

  public ChatbotConfiguration(MessageSenderProvider messageSenderProvider,
                              MessageSender watsonService,
                              MessageSender azureQnAService,
                              MessageSender dialogFlowService) {
    messageSenderProvider.registerMessageSender("watson", watsonService);
    messageSenderProvider.registerMessageSender("azure", azureQnAService);
    messageSenderProvider.registerMessageSender("dialogflow", dialogFlowService);
  }
}
