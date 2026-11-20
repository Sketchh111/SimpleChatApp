package com.example.SimpleChatApp.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class RedisMessageSubscriber implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {

        String channel = new String(
                message.getChannel(), StandardCharsets.UTF_8);

        String receivedMessage = new String(
                message.getBody(), StandardCharsets.UTF_8);

        System.out.println("Channel: " + channel);
        System.out.println("Received Message: " + receivedMessage);
    }
}