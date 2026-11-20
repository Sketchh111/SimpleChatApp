package com.example.SimpleChatApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(
            IllegalArgumentException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "message", ex.getMessage(),
                        "status", "error"
                ));
    }

    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRoomNotFound(
            ChatRoomNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "message", ex.getMessage(),
                        "status", "error"
                ));
    }

    @ExceptionHandler(ChatRoomAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleRoomAlreadyExists(
            ChatRoomAlreadyExistException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "message", ex.getMessage(),
                        "status", "error"
                ));
    }

    @ExceptionHandler(ParticipantAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateParticipant(
            ParticipantAlreadyExistsException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "message", ex.getMessage(),
                        "status", "error"
                ));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "message", errors,
                        "status", "error"
                ));
    }
    @ExceptionHandler(UnauthorizedParticipantsException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedParticipant(
            UnauthorizedParticipantsException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "message", ex.getMessage(),
                        "status", "error"
                ));
    }
}