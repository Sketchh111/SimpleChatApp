package com.example.SimpleChatApp.controller;


import com.example.SimpleChatApp.dto.SendMessageRequest;
import com.example.SimpleChatApp.service.ChatMessageService;
import com.example.SimpleChatApp.service.ChatRoomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
@Tag(
        name = "Chat Messages",
        description = "APIs for sending and retrieving chat messages"
)

//This rest controller APIs used for sending and retrieving messages of participants of an existing room.
@RestController
@RequestMapping("/api/chatapp/")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatService;

    public ChatMessageController(ChatMessageService chatMessageService, ChatRoomService chatService) {
        this.chatMessageService = chatMessageService;
        this.chatService = chatService;
    }
    @Operation(
            summary = "Send a chat message",
            description = "Sends a message to an existing room. Only joined participants can send messages."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Participant has not joined the room"),
            @ApiResponse(responseCode = "404", description = "Chat room does not exist")
    })
    @PostMapping("chatrooms/{roomId}/messages")
    public ResponseEntity<Map<String, String>> sendMessage(@Valid @PathVariable ("roomId") String roomId,
                                                               @RequestBody SendMessageRequest sendMessageRequest){
        chatMessageService.sendMessage(roomId,
                sendMessageRequest.getParticipant(),
                sendMessageRequest.getMessage());

        Map<String, String > response = Map.of("message","Message sent successfully",
                "status" , "success");

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Retrieve chat messages",
            description = "Retrieves the latest N messages from a chat room in chronological order."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid message limit"),
            @ApiResponse(responseCode = "404", description = "Chat room does not exist")
    })
    @GetMapping("chatrooms/{roomId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable ("roomId") String roomId,
                                                           @RequestParam(defaultValue = "10") int limit){

        return ResponseEntity.ok(Map.of("messages",chatMessageService.getMessages(roomId, limit)));
    }
}
