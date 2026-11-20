package com.example.SimpleChatApp.model;

//This model is created for storing chats in database and publishing live chats to participants

public class ChatMessage {

    private String participant;
    private String message;
    private String timestamp;

    public ChatMessage() {
    }

    public ChatMessage(String participant, String message, String timestamp) {
        this.participant = participant;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getParticipant(){
        return participant;
    }
    public void setParticipant(String participant){
        this.participant = participant;
    }
    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }
}
