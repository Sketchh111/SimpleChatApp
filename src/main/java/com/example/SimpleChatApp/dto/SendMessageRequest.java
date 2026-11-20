package com.example.SimpleChatApp.dto;

//I am using this to handle request of the messages send by participants.

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SendMessageRequest {

    @NotBlank(message = "Participant cannot be empty")
    String participant;

    @NotBlank(message = "Message cannot be empty")
    @Size(max = 1000,
            message = "Message cannot exceed 1000 characters")
    String message;

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

