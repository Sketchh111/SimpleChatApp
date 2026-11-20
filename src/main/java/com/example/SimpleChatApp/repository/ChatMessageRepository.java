package com.example.SimpleChatApp.repository;

import com.example.SimpleChatApp.model.ChatMessage;

import java.util.List;

//These are the unimplemented method used to perform database operations for storing and managing chatMessage.
public interface ChatMessageRepository {

    void saveMessages(String roomId, ChatMessage message);

    void publishMessages(String roomId, ChatMessage message);

    List<ChatMessage> getMessages(String roomId, int limit);
}

