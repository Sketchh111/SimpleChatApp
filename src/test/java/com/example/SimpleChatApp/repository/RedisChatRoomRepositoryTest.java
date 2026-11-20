
package com.example.SimpleChatApp.repository;

import com.example.SimpleChatApp.model.ChatRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisChatRoomRepositoryTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @BeforeEach
    void cleanRedis() {
        redisTemplate.delete("room:integration-test");
        redisTemplate.delete("room:integration-test:participants");
    }

    @Test
    void shouldStoreRoomMetadataInRedisHash() {


        ChatRoom room = new ChatRoom(
                "integration-test",
                "integration-test"
        );

        chatRoomRepository.save(room);


        assertTrue(
                chatRoomRepository.existsById("integration-test")
        );

        assertEquals(
                "integration-test",
                redisTemplate.opsForHash()
                        .get("room:integration-test", "roomId")
        );

        assertEquals(
                "integration-test",
                redisTemplate.opsForHash()
                        .get("room:integration-test", "roomName")
        );
    }

    @Test
    void shouldAddParticipantToRedisSet() {


        ChatRoom room = new ChatRoom(
                "integration-test",
                "integration-test"
        );

        chatRoomRepository.save(room);

        Long result = chatRoomRepository.addParticipant(
                "integration-test",
                "guest_user"
        );


        assertEquals(1L, result);

        assertTrue(
                chatRoomRepository.isParticipant(
                        "integration-test",
                        "guest_user"
                )
        );

        assertTrue(
                redisTemplate.opsForSet()
                        .isMember(
                                "room:integration-test:participants",
                                "guest_user"
                        )
        );
    }
    @Test
    void shouldDeleteRoomAndAllRelatedRedisKeys() {

        ChatRoom room = new ChatRoom(
                "integration-test",
                "integration-test"
        );

        chatRoomRepository.save(room);

        chatRoomRepository.addParticipant(
                "integration-test",
                "guest_user"
        );

        redisTemplate.opsForList().rightPush(
                "room:integration-test:messages",
                "test-message"
        );

        System.out.println("Room exists: " +
                redisTemplate.hasKey("room:integration-test"));

        System.out.println("Participants exist: " +
                redisTemplate.hasKey("room:integration-test:participants"));

        System.out.println("Messages exist: " +
                redisTemplate.hasKey("room:integration-test:messages"));

        chatRoomRepository.deleteRoom("integration-test");


        assertFalse(
                redisTemplate.hasKey("room:integration-test")
        );

        assertFalse(
                redisTemplate.hasKey("room:integration-test:participants")
        );

        assertFalse(
                redisTemplate.hasKey("room:integration-test:messages")
        );
    }
}
