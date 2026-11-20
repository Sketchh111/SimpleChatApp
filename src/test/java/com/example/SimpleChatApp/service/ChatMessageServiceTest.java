
package com.example.SimpleChatApp.service;

import com.example.SimpleChatApp.exception.ChatRoomNotFoundException;
import com.example.SimpleChatApp.exception.UnauthorizedParticipantsException;
import com.example.SimpleChatApp.model.ChatMessage;
import com.example.SimpleChatApp.repository.ChatMessageRepository;
import com.example.SimpleChatApp.repository.ChatRoomRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @InjectMocks
    private ChatMessageService chatMessageService;

    @Test
    void shouldSendMessageSuccessfully() {


        when(chatRoomRepository.existsById("general"))
                .thenReturn(true);

        when(chatRoomRepository.isParticipant("general", "khushal"))
                .thenReturn(true);


        assertDoesNotThrow(() ->
                chatMessageService.sendMessage(
                        "general",
                        "khushal",
                        "Hello everyone!"
                )
        );


        verify(chatMessageRepository)
                .saveMessages(eq("general"), any(ChatMessage.class));

        verify(chatMessageRepository)
                .publishMessages(eq("general"), any(ChatMessage.class));
    }

    @Test
    void shouldThrowExceptionWhenParticipantHasNotJoinedRoom() {


        when(chatRoomRepository.existsById("general"))
                .thenReturn(true);

        when(chatRoomRepository.isParticipant("general", "khushal"))
                .thenReturn(false);


        assertThrows(
                UnauthorizedParticipantsException.class,
                () -> chatMessageService.sendMessage(
                        "general",
                        "khushal",
                        "Hello everyone!"
                )
        );

        verify(chatMessageRepository, never())
                .saveMessages(anyString(), any(ChatMessage.class));

        verify(chatMessageRepository, never())
                .publishMessages(anyString(), any(ChatMessage.class));
    }
    @Test
    void shouldThrowExceptionWhenRoomDoesNotExist() {


        when(chatRoomRepository.existsById("general"))
                .thenReturn(false);


        assertThrows(
                ChatRoomNotFoundException.class,
                () -> chatMessageService.sendMessage(
                        "general",
                        "khushal",
                        "Hello everyone!"
                )
        );

        // Verify participant check is not performed
        verify(chatRoomRepository, never())
                .isParticipant(anyString(), anyString());

        // Verify message is neither saved nor published
        verify(chatMessageRepository, never())
                .saveMessages(anyString(), any(ChatMessage.class));

        verify(chatMessageRepository, never())
                .publishMessages(anyString(), any(ChatMessage.class));
    }
    @Test
    void shouldRetrieveMessagesSuccessfully() {


        ChatMessage message1 = new ChatMessage(
                "khushal",
                "Hello",
                "2026-11-20T10:00:00Z"
        );

        ChatMessage message2 = new ChatMessage(
                "sunil",
                "Hi Khushal",
                "2026-11-20T10:01:00Z"
        );

        List<ChatMessage> expectedMessages =
                List.of(message1, message2);

        when(chatRoomRepository.existsById("general"))
                .thenReturn(true);

        when(chatMessageRepository.getMessages("general", 2))
                .thenReturn(expectedMessages);


        List<ChatMessage> actualMessages =
                chatMessageService.getMessages("general", 2);


        assertEquals(2, actualMessages.size());
        assertEquals("Hello", actualMessages.get(0).getMessage());
        assertEquals("Hi Khushal", actualMessages.get(1).getMessage());

        verify(chatMessageRepository).getMessages("general", 2);
    }
    @Test
    void shouldThrowExceptionWhenMessageLimitIsZero() {


        assertThrows(
                IllegalArgumentException.class,
                () -> chatMessageService.getMessages("general", 0)
        );


        verify(chatRoomRepository, never())
                .existsById(anyString());

        verify(chatMessageRepository, never())
                .getMessages(anyString(), anyInt());
    }
    @Test
    void shouldThrowExceptionWhenMessageLimitIsNegative() {


        assertThrows(
                IllegalArgumentException.class,
                () -> chatMessageService.getMessages("general", -5)
        );


        verify(chatRoomRepository, never())
                .existsById(anyString());

        verify(chatMessageRepository, never())
                .getMessages(anyString(), anyInt());
    }
}
