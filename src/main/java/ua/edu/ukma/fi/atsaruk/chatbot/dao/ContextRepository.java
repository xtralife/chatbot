package ua.edu.ukma.fi.atsaruk.chatbot.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import ua.edu.ukma.fi.atsaruk.chatbot.entity.dto.Context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

@Repository
public class ContextRepository {
  private final static Logger LOGGER = LoggerFactory.getLogger(ContextRepository.class);

  private final static String PARAMETERS = "parameters";
  private final static String ID = "id";
  private final static String EMPTY_MAP_JSON = "{}";

  private final DynamoDbClient client;
  private final ObjectMapper objectMapper;
  private final String table;

  public ContextRepository(DynamoDbClient client,
                           ObjectMapper objectMapper,
                           @Value("${aws.dynamo-db.tables.contexts}") String table) {
    this.client = client;
    this.objectMapper = objectMapper;
    this.table = table;
  }

  public void saveOrUpdate(Context context) {
    GetItemRequest getItemRequest = GetItemRequest.builder()
        .tableName(table)
        .key(buildKey(context.getId()))
        .build();
    GetItemResponse getItemResponse = client.getItem(getItemRequest);

    Map<String, AttributeValueUpdate> updates;
    if (getItemResponse.hasItem()) {
      updates = buildAttributeUpdates(context, parseParameters(getItemResponse.item()));
    } else {
      updates = buildAttributeUpdates(context);
    }

    UpdateItemRequest request = UpdateItemRequest.builder()
        .tableName(table)
        .key(buildKey(context.getId()))
        .attributeUpdates(updates)
        .build();
    try {
      client.updateItem(request);
      LOGGER.debug("Context with id {} was successfully saved to DynamoDb. Parameters: {}",
          context.getId(), context.getParameters());
    } catch (AwsServiceException ex) {
      LOGGER.error(format("Error during item update in Dynamo Db. Table: %s, Context: %s", table, context), ex);
    }
  }

  private Map<String, String> parseParameters(Map<String, AttributeValue> item) {
    TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
    };
    try {
      return objectMapper.readValue(item.getOrDefault(PARAMETERS, AttributeValue.builder().s(EMPTY_MAP_JSON).build()).s(), typeRef);
    } catch (JsonProcessingException e) {
      LOGGER.error("Error while parsing parameters to Map for context with id {}", item.get(ID).s());
      return new HashMap<>();
    }
  }

  private String asString(Map<String, String> map) {
    try {
      return objectMapper.writeValueAsString(map);
    } catch (JsonProcessingException e) {
      LOGGER.error("Error while serializing map to json");
      return EMPTY_MAP_JSON;
    }
  }

  private Map<String, AttributeValue> buildKey(String id) {
    return Collections.singletonMap(ID, AttributeValue.builder().s(id).build());
  }

  private Map<String, AttributeValueUpdate> buildAttributeUpdates(Context context,
                                                                  Map<String, String> parameters) {
    Map<String, String> toSave = new HashMap<>(parameters);
    toSave.putAll(context.getParameters());
    AttributeValueUpdate av = AttributeValueUpdate.builder()
        .value(AttributeValue.builder()
            .s(asString(toSave))
            .build())
        .build();
    return Collections.singletonMap(PARAMETERS, av);
  }

  private Map<String, AttributeValueUpdate> buildAttributeUpdates(Context context) {
    return buildAttributeUpdates(context, new HashMap<>());
  }
}
