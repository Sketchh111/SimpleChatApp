package com.example.SimpleChatApp.repository;
import com.example.SimpleChatApp.model.ChatRoom;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// This is a redisRepository we are using this to implement chatroom repository methods
//In this we are using Redis datastructures to store data and interact with database.

@Repository
public class RedisChatRoomRepository implements ChatRoomRepository {

    private final StringRedisTemplate redisTemplate;

    public RedisChatRoomRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(ChatRoom ChatRoom) {
        String Key = "room:" + ChatRoom.getRoomId();

        Map<String, String> roomData = new HashMap<>();
        roomData.put("roomId", ChatRoom.getRoomId());
        roomData.put("roomName", ChatRoom.getRoomName());

        //This opsForHash is used to room metadata in hash datastructures
        redisTemplate.opsForHash().putAll(Key, roomData);
    }

    //This method used to check whether participant exist in database.
    @Override
    public boolean existsById(String roomId) {

        return Boolean.TRUE.equals(redisTemplate.hasKey("room:" + roomId));
    }

    @Override
    public Long addParticipant(String roomId, String participant) {
        String key = "room:" + roomId + ":participants";


        //This is used to stored participant in Redis database using the set datastructures
        //Set does not allowed duplicate so for storing participant in database I have used SET
        return redisTemplate.opsForSet().add(key, participant);
    }

    //This method is used to check whether this participant already inside the room or not.
    //This uses Redis (SISMEMBER) to check whether the participant exists in the Set.
    @Override
    public boolean isParticipant(String roomId, String participant) {

        String key = "room:" + roomId + ":participants";

        return Boolean.TRUE.equals(
                redisTemplate.opsForSet().isMember(key, participant)
        );
    }

    @Override
    public void deleteRoom(String roomId){

        List<String> keys = List.of( "room:" + roomId,
                            "room:" + roomId + ":participants",
                            "room:" + roomId + ":messages"
                );

        Long deletedCount = redisTemplate.delete(keys);

        System.out.println("Deleted Redis keys: " + deletedCount);
    }


}
