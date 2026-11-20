package com.example.SimpleChatApp.model;

//This is an ChatRoom entity how chatroom will be stored in database(redis)
//We will save The Chatroom in hash by using Redis hash datastructures.
//Why we used hash because it will store multiple values in a key.

public class ChatRoom {

    private String roomId;

    private String roomName;

    public ChatRoom(String roomId, String roomName){
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomId(){
        return this.roomId;
    }
    public void setRoomId(String roomId){
         this.roomId = roomId;
    }
}
