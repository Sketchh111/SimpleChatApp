package com.example.SimpleChatApp.dto;

// This is used to give response back to user after storing the data in redis
// this contains message,room id and status

public class CreateRoomResponse {

    private String message;
    private String roomId;
    private String status;

    public CreateRoomResponse(String message, String roomId, String status) {
        this.message = message;
        this.roomId = roomId;
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
