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
import ua.edu.ukma.fi.atsaruk.chatbot.entity.dto.Chat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

@Repository
public class ChatRepository {
  private final static Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

  private final DynamoDbClient client;
  private final String table;

  public ChatRepository(DynamoDbClient client,
                        @Value("${aws.dynamo-db.tables.chats}") String table) {
    this.client = client;
    this.table = table;
  }

  public void saveOrUpdate(Chat chat) {
    UpdateItemRequest request = UpdateItemRequest.builder()
        .tableName(table)
        .key(buildKey(chat.getId()))
        .attributeUpdates(buildAttributeUpdates(chat))
        .build();
    try {
      client.updateItem(request);
      LOGGER.debug("Chat of type {} with id {} was successfully saved to DynamoDb", chat.getType(), chat.getId());
    } catch (AwsServiceException ex) {
      LOGGER.error(format("Error during item update in Dynamo Db. Table: %s, Chat: %s", table, chat), ex);
    }
  }

  private Map<String, AttributeValue> buildKey(Long id) {
    return Collections.singletonMap("id", AttributeValue.builder().n(Long.toString(id)).build());
  }

  private Map<String, AttributeValueUpdate> buildAttributeUpdates(Chat chat) {
    Map<String, AttributeValueUpdate> updates = new HashMap<>();
    if (chat.getSource() != null) {
      AttributeValue av = AttributeValue.builder().s(chat.getSource()).build();
      updates.put("source", AttributeValueUpdate.builder().value(av).build());
    }
    if (chat.getUsername() != null) {
      AttributeValue av = AttributeValue.builder().s(chat.getUsername()).build();
      updates.put("username", AttributeValueUpdate.builder().value(av).build());
    }
    if (chat.getFirstName() != null) {
      AttributeValue av = AttributeValue.builder().s(chat.getFirstName()).build();
      updates.put("first-name", AttributeValueUpdate.builder().value(av).build());
    }
    if (chat.getLastName() != null) {
      AttributeValue av = AttributeValue.builder().s(chat.getLastName()).build();
      updates.put("last-name", AttributeValueUpdate.builder().value(av).build());
    }
    if (chat.getType() != null) {
      AttributeValue av = AttributeValue.builder().s(chat.getType()).build();
      updates.put("type", AttributeValueUpdate.builder().value(av).build());
    }
    if (chat.getTitle() != null) {
      AttributeValue av = AttributeValue.builder().s(chat.getTitle()).build();
      updates.put("title", AttributeValueUpdate.builder().value(av).build());
    }
    if (chat.getDescription() != null) {
      AttributeValue av = AttributeValue.builder().s(chat.getDescription()).build();
      updates.put("description", AttributeValueUpdate.builder().value(av).build());
    }
    if (chat.getInviteLink() != null) {
      AttributeValue av = AttributeValue.builder().s(chat.getInviteLink()).build();
      updates.put("invite-link", AttributeValueUpdate.builder().value(av).build());
    }

    return updates;
  }
}
