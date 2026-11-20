package com.example.SimpleChatApp.repository;

import com.example.SimpleChatApp.model.ChatRoom;

//These are the unimplemented method used to perform database operations for Room.
public interface ChatRoomRepository {

    void save(ChatRoom ChatRoom);

    boolean existsById(String roomId);

    Long addParticipant(String roomId, String participant);

    boolean isParticipant(String roomId, String participant);

    void deleteRoom(String roomId);

}
