package com.example.SimpleChatApp.service;

import com.example.SimpleChatApp.exception.ChatRoomAlreadyExistException;
import com.example.SimpleChatApp.exception.ChatRoomNotFoundException;
import com.example.SimpleChatApp.exception.ParticipantAlreadyExistsException;
import com.example.SimpleChatApp.model.ChatRoom;
import com.example.SimpleChatApp.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

//This is a service layer it is used to implement business logic
//This layer decide how the data is manage by business logic and rules.

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository){
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoom createRoom(String roomName){

        if(chatRoomRepository.existsById(roomName)){
            throw new ChatRoomAlreadyExistException("Room already exists");
        }

        ChatRoom chatRoom = new ChatRoom(roomName ,roomName);

        chatRoomRepository.save(chatRoom);

        return chatRoom;
    }

    public void joinRoom(String roomId, String participant) {

        if (!chatRoomRepository.existsById(roomId)) {
            throw new ChatRoomNotFoundException("Chat Room '" + roomId + "' does not exist");
        }

        Long added = chatRoomRepository.addParticipant(roomId, participant);

        if (added == null || added == 0) {
            throw new ParticipantAlreadyExistsException(
                    "Participant '" + participant + "' already exists in room '" + roomId + "'.");
        }
        ;
    }
        public void deleteRoom(String roomId){

            if(!chatRoomRepository.existsById(roomId)){
                throw new ChatRoomAlreadyExistException("ChatRoom does not exists");
            }

            chatRoomRepository.deleteRoom(roomId);
        }

    }

