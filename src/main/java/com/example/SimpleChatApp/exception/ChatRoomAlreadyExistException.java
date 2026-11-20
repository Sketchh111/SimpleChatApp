package com.example.SimpleChatApp.exception;

public class ChatRoomAlreadyExistException extends RuntimeException{

    public ChatRoomAlreadyExistException(String message) {
        super(message);
    }
}
