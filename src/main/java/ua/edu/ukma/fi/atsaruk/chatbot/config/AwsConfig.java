package ua.edu.ukma.fi.atsaruk.chatbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class AwsConfig {
  @Bean
  public DynamoDbClient dynamoDbClient(@Value("${aws.user}") String user, @Value("${aws.region}") Region region) {
    AwsCredentialsProvider credentialsProvider = DefaultCredentialsProvider.builder().profileName(user).build();
    return DynamoDbClient.builder()
        .credentialsProvider(credentialsProvider)
        .region(region)
        .build();
  }
}
