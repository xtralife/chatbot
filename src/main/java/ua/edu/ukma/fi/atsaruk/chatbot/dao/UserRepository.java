package ua.edu.ukma.fi.atsaruk.chatbot.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import ua.edu.ukma.fi.atsaruk.chatbot.entity.dto.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

@Repository
public class UserRepository {
  private final static Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

  private final DynamoDbClient client;
  private final String table;

  public UserRepository(DynamoDbClient client,
                        @Value("${aws.dynamo-db.tables.users}") String table) {
    this.client = client;
    this.table = table;
  }

  public void saveOrUpdate(User user) {
    UpdateItemRequest request = UpdateItemRequest.builder()
        .tableName(table)
        .key(buildKey(user.getId()))
        .attributeUpdates(buildAttributeUpdates(user))
        .build();
    try {
      client.updateItem(request);
      LOGGER.debug("User {} {} with id {} was successfully saved to DynamoDb",
          user.getFirstName(), user.getLastName(), user.getId());
    } catch (AwsServiceException ex) {
      LOGGER.error(format("Error during item update in Dynamo Db. Table: %s, User: %s", table, user), ex);
    }
  }

  private Map<String, AttributeValue> buildKey(Integer id) {
    return Collections.singletonMap("id", AttributeValue.builder().n(Integer.toString(id)).build());
  }

  private Map<String, AttributeValueUpdate> buildAttributeUpdates(User user) {
    Map<String, AttributeValueUpdate> updates = new HashMap<>();
    if (user.getSource() != null) {
      AttributeValue av = AttributeValue.builder().s(user.getSource()).build();
      updates.put("source", AttributeValueUpdate.builder().value(av).build());
    }
    if (user.getUsername() != null) {
      AttributeValue av = AttributeValue.builder().s(user.getUsername()).build();
      updates.put("username", AttributeValueUpdate.builder().value(av).build());
    }
    if (user.getFirstName() != null) {
      AttributeValue av = AttributeValue.builder().s(user.getFirstName()).build();
      updates.put("first-name", AttributeValueUpdate.builder().value(av).build());
    }
    if (user.getLastName() != null) {
      AttributeValue av = AttributeValue.builder().s(user.getLastName()).build();
      updates.put("last-name", AttributeValueUpdate.builder().value(av).build());
    }
    if (user.getLanguageCode() != null) {
      AttributeValue av = AttributeValue.builder().s(user.getLanguageCode()).build();
      updates.put("language", AttributeValueUpdate.builder().value(av).build());
    }
    if (user.getBot() != null) {
      AttributeValue av = AttributeValue.builder().bool(user.getBot()).build();
      updates.put("bot", AttributeValueUpdate.builder().value(av).build());
    }

    return updates;
  }
}
