
package com.example.SimpleChatApp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Simple Chat Application API",
                version = "1.0.0",
                description = """
            REST API for a simple chat application using Spring Boot and Redis.

            Features:
            - Create and join chat rooms
            - Send and retrieve chat messages
            - Redis List for message storage
            - Redis Pub/Sub for real-time message broadcasting
            - Redis Hash and Set for room management
            - Delete chat rooms
            """
        )
)
public class OpenApiConfig {
}
