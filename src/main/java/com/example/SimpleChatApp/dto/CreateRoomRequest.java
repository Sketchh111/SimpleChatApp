package com.example.SimpleChatApp.dto;


//Using this dto for taking input only of roomname from client.

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateRoomRequest {

    @NotBlank(message = "Room name cannot Empty")
    @Size(min=3 , max = 30, message = "Room name must be between 3 and 30 characters")
    private String roomName;

    public CreateRoomRequest(){
    }

    public String getRoomName(){
        return this.roomName;
    }

    public void setRoomName(String roomName){
        this.roomName = roomName;
    }


}
