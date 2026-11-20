package com.example.SimpleChatApp.service;

import com.example.SimpleChatApp.exception.ChatRoomAlreadyExistException;
import com.example.SimpleChatApp.exception.ChatRoomNotFoundException;
import com.example.SimpleChatApp.exception.UnauthorizedParticipantsException;
import com.example.SimpleChatApp.model.ChatMessage;
import com.example.SimpleChatApp.repository.ChatMessageRepository;
import com.example.SimpleChatApp.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    public void sendMessage(String roomId,String participant,String message) {

        if(!chatRoomRepository.existsById(roomId)) {
            throw new ChatRoomNotFoundException("Chat Room does not exist");
        }
        if (!chatRoomRepository.isParticipant(roomId, participant)) {
            throw new UnauthorizedParticipantsException("Participant has not joined this room");
        }

        ChatMessage chatMessage = new ChatMessage(participant, message, Instant.now().toString());

        chatMessageRepository.saveMessages(roomId, chatMessage);

        chatMessageRepository.publishMessages(roomId, chatMessage);
    }

    public List<ChatMessage> getMessages(String roomId, int limit) {

        if(limit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than 0");
        }
        if(!chatRoomRepository.existsById(roomId)) {
            throw new ChatRoomNotFoundException("Chat Room does not exist");
        }

        return chatMessageRepository.getMessages(roomId, limit);
    }

}
