package com.example.SimpleChatApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//This dto is used to take user request
public class JoinRoomRequest {

    @NotBlank(message = "Participant name cannot be empty")
    @Size(min = 3, max = 30,
            message = "Participant name must be between 3 and 30 characters")
    private String participant;


    public JoinRoomRequest() {
    }

    public String getParticipant() {

        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

}
