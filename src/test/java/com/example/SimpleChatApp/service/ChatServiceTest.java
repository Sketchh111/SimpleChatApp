package com.example.SimpleChatApp.service;

import com.example.SimpleChatApp.exception.ChatRoomAlreadyExistException;
import com.example.SimpleChatApp.exception.ChatRoomNotFoundException;
import com.example.SimpleChatApp.exception.ParticipantAlreadyExistsException;
import com.example.SimpleChatApp.model.ChatRoom;
import com.example.SimpleChatApp.repository.ChatRoomRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private ChatRoomService chatService;

    @Test
    void shouldCreateRoomSuccessfully() {

        when(chatRoomRepository.existsById("general"))
                .thenReturn(false);

        ChatRoom room = chatService.createRoom("general");

        assertNotNull(room);
        assertEquals("general", room.getRoomId());
        assertEquals("general", room.getRoomName());

        verify(chatRoomRepository).save(any(ChatRoom.class));
    }

    @Test
    void shouldThrowExceptionWhenRoomAlreadyExists() {

        when(chatRoomRepository.existsById("general"))
                .thenReturn(true);

        assertThrows(
                ChatRoomAlreadyExistException.class,
                () -> chatService.createRoom("general")
        );

        verify(chatRoomRepository, never())
                .save(any(ChatRoom.class));
    }

    @Test
    void shouldJoinRoomSuccessfully() {

        when(chatRoomRepository.existsById("general"))
                .thenReturn(true);

        when(chatRoomRepository.addParticipant("general", "khushal"))
                .thenReturn(1L);

        assertDoesNotThrow(
                () -> chatService.joinRoom("general", "khushal")
        );

        verify(chatRoomRepository)
                .addParticipant("general", "khushal");
    }

    @Test
    void shouldThrowExceptionWhenParticipantAlreadyExists() {

        when(chatRoomRepository.existsById("general"))
                .thenReturn(true);

        when(chatRoomRepository.addParticipant("general", "khushal"))
                .thenReturn(0L);

        assertThrows(
                ParticipantAlreadyExistsException.class,
                () -> chatService.joinRoom("general", "khushal")
        );
    }

    @Test
    void shouldThrowExceptionWhenRoomDoesNotExist() {

        when(chatRoomRepository.existsById("general"))
                .thenReturn(false);

        assertThrows(
                ChatRoomNotFoundException.class,
                () -> chatService.joinRoom("general", "khushal")
        );

        verify(chatRoomRepository, never())
                .addParticipant(anyString(), anyString());
    }
}