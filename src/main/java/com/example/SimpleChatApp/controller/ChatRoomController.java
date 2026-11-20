package com.example.SimpleChatApp.controller;

import com.example.SimpleChatApp.dto.CreateRoomRequest;
import com.example.SimpleChatApp.dto.CreateRoomResponse;
import com.example.SimpleChatApp.dto.JoinRoomRequest;
import com.example.SimpleChatApp.model.ChatRoom;
import com.example.SimpleChatApp.service.ChatRoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;


//This is a rest controller used to control http request and give response
//This will help to transfer http request with the help of endpoint to connect with business logic layer method.

@Tag(
        name = "Chat Room Management",
        description = "APIs for creating, joining and deleting chat rooms"
)

@RestController
@RequestMapping("/api/chatapp/")
public class ChatRoomController {

    private final ChatRoomService chatService;

    public ChatRoomController(ChatRoomService chatService){
        this.chatService = chatService;
    }

    @Operation(
            summary = "Create a chat room",
            description = "Creates a new chat room with a unique room name."
    )
    @PostMapping("/chatrooms")
    public ResponseEntity<CreateRoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest request){

        ChatRoom chatRoom = chatService.createRoom(request.getRoomName());

        CreateRoomResponse createRoomResponse = new CreateRoomResponse(
                "Chat room '" + chatRoom.getRoomName() + "' created successfully.",
                chatRoom.getRoomId(),
                "success");
        return ResponseEntity.status(HttpStatus.CREATED).body(createRoomResponse);
    }

    @Operation(
            summary = "Join a chat room",
            description = "Allows a participant to join an existing chat room."
    )
    @PostMapping("/chatrooms/{roomId}/join")
    public ResponseEntity<Map<String, String>> joinRoom(@PathVariable("roomId") String roomId,
                                                        @Valid @RequestBody JoinRoomRequest request){

        chatService.joinRoom(roomId, request.getParticipant());
        Map<String,String> response = Map.of("message", "User '" + request.getParticipant()
                        + "' joined chat room '" + roomId + "'.",
                "status",
                "success");



        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Delete a chat room",
            description = "Deletes room metadata, participants and stored messages from Redis."
    )
    @DeleteMapping("/chatrooms/{roomId}/delete")
    public ResponseEntity<?> deleteRoom(@PathVariable("roomId")
                                            String roomId) {

        chatService.deleteRoom(roomId);

        return ResponseEntity.ok(Map.of(
                "message", "Chat room '" + roomId + "' deleted successfully.",
                "status", "success"
        ));
    }
}
