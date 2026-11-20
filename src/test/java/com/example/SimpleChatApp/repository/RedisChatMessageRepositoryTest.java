
package com.example.SimpleChatApp.repository;

import com.example.SimpleChatApp.model.ChatMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
@SpringBootTest
class RedisChatMessageRepositoryTest {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final String roomId = "integration-test";

    @BeforeEach
    void cleanRedis() {
        redisTemplate.delete("room:" + roomId + ":messages");
    }

    @Test
    void shouldStoreAndRetrieveMessagesInCorrectOrder() {

        // Arrange
        ChatMessage message1 = new ChatMessage(
                "khushal",
                "Hello",
                "2026-11-20T10:00:00Z"
        );

        ChatMessage message2 = new ChatMessage(
                "sunil",
                "Hi Khushal",
                "2026-11-20T10:01:00Z"
        );


        chatMessageRepository.saveMessages(roomId, message1);
        chatMessageRepository.saveMessages(roomId, message2);

        List<ChatMessage> messages =
                chatMessageRepository.getMessages(roomId, 2);

        assertEquals(2, messages.size());

        assertEquals("Hello", messages.get(0).getMessage());
        assertEquals("Hi Khushal", messages.get(1).getMessage());

        assertEquals("khushal", messages.get(0).getParticipant());
        assertEquals("sunil", messages.get(1).getParticipant());
    }

    @Test
    void shouldRetrieveOnlyLastNMessages() {

        chatMessageRepository.saveMessages(
                roomId,
                new ChatMessage("khushal", "Message 1",
                        "2026-11-20T10:00:00Z")
        );

        chatMessageRepository.saveMessages(
                roomId,
                new ChatMessage("khushal", "Message 2",
                        "2026-11-20T10:01:00Z")
        );

        chatMessageRepository.saveMessages(
                roomId,
                new ChatMessage("khushal", "Message 3",
                        "2026-11-20T10:02:00Z")
        );


        List<ChatMessage> messages =
                chatMessageRepository.getMessages(roomId, 1);


        assertEquals(1, messages.size());
        assertEquals("Message 3", messages.get(0).getMessage());
    }


    @Test
    void shouldPublishMessageToRedisPubSubChannel() throws Exception {

        // Arrange
        String channel = "room:" + roomId + ":channel";

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> receivedMessage = new AtomicReference<>();

        MessageListener testSubscriber = (Message message, byte[] pattern) -> {
            receivedMessage.set(
                    new String(message.getBody(), StandardCharsets.UTF_8)
            );
            latch.countDown();
        };

        var connectionFactory = redisTemplate.getConnectionFactory();

        var connection = connectionFactory.getConnection();

        try {
            connection.subscribe(
                    testSubscriber,
                    channel.getBytes(StandardCharsets.UTF_8)
            );

            // Act
            ChatMessage chatMessage = new ChatMessage(
                    "khushal",
                    "Hello everyone!",
                    "2026-11-20T10:00:00Z"
            );

            chatMessageRepository.publishMessages(roomId, chatMessage);

            // Assert
            assertTrue(
                    latch.await(5, TimeUnit.SECONDS),
                    "Subscriber did not receive the published message"
            );

            assertNotNull(receivedMessage.get());
            assertTrue(receivedMessage.get().contains("Hello everyone!"));

        } finally {
            connection.close();
        }
    }

}
