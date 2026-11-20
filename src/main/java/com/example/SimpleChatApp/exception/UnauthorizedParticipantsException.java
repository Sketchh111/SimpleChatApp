package com.example.SimpleChatApp.exception;

public class UnauthorizedParticipantsException extends  RuntimeException{
    public UnauthorizedParticipantsException(String message){
        super(message);

    }
}
