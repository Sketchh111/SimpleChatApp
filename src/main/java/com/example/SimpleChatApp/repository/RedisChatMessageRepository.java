package com.example.SimpleChatApp.repository;
import com.example.SimpleChatApp.model.ChatMessage;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.ArrayList;

//This is redisChatMessage repository
//It is used to publish and store messages in Redis database
//I am using list to stored message and RPUSH Redis command for new element storing
//New element(message) store at right in last end of the list.

@Repository
public class RedisChatMessageRepository implements ChatMessageRepository {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisChatMessageRepository(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void saveMessages(String roomId, ChatMessage message) {

        try {

            String key = "room:" + roomId + ":messages";

            String messagesJson = objectMapper.writeValueAsString(message);

            redisTemplate.opsForList().rightPush(key, messagesJson);
        }
        catch (JacksonException ex){
            throw new IllegalStateException("Unable to serialize message", ex);
        }
    }
    @Override
    public void publishMessages(String roomId, ChatMessage message) {

        try {

            String channel = "room:" + roomId + ":channel";

            String messagesJson = objectMapper.writeValueAsString(message);

            redisTemplate.convertAndSend(channel, messagesJson);
        } catch (JacksonException ex) {
            throw new IllegalStateException("Unable to publish message.", ex);
        }
    }
        @Override
        public List<ChatMessage> getMessages(String roomId, int limit){

            String key = "room:" + roomId + ":messages";

            List<String> messagesJson = redisTemplate.opsForList()
                    .range(key, -(long) limit, -1);

            List<ChatMessage> messages = new ArrayList<>();

            if (messagesJson == null) {
                return messages;
            }

            try {
                for (String json : messagesJson) {
                    ChatMessage message = objectMapper.readValue(
                            json, ChatMessage.class
                    );

                    messages.add(message);
                }
            } catch (JacksonException ex) {
                throw new IllegalStateException(
                        "Unable to deserialize messages", ex
                );
            }

            return messages;
        }

    }