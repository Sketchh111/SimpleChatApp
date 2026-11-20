# Simple Chat Application Using Redis

A backend chat application built using **Java 21, Spring Boot, and Redis**. It supports chat room creation, participant management, message storage, message retrieval, and real-time message broadcasting using Redis Pub/Sub.

## 🚀 Features

- Create chat rooms with unique room names.
- Allow participants to join chat rooms.
- Prevent duplicate room names and duplicate participants.
- Send messages only from participants who have joined the room.
- Store messages in Redis Lists.
- Retrieve the latest N messages from a chat room.
- Broadcast messages using Redis Pub/Sub.
- Delete chat rooms and their associated stored data.
- Centralized exception handling and request validation.
- REST API documentation using Swagger UI.
- Automated unit and Redis integration tests.

## 🛠️ Technology Stack

| Technology        | Purpose                  |
| ----------------- | ------------------------ |
| Java 21           | Programming language     |
| Spring Boot 4.1.2 | Backend framework        |
| Spring Data Redis | Redis integration        |
| Redis 7           | Data storage and Pub/Sub |
| Maven             | Build management         |
| JUnit 5           | Testing                  |
| Mockito           | Unit testing and mocking |
| Swagger / OpenAPI | API documentation        |
| Docker            | Containerization         |
| Postman           | API testing              |

## 🏗️ Architecture

The application uses Redis data structures for different responsibilities.

### Redis Data Structures

| Data Structure  | Redis Key                    | Purpose                         |
| --------------- | ---------------------------- | ------------------------------- |
| Hash            | `room:{roomId}`              | Stores room metadata            |
| Set             | `room:{roomId}:participants` | Stores room participants        |
| List            | `room:{roomId}:messages`     | Stores messages as JSON strings |
| Pub/Sub Channel | `room:{roomId}:channel`      | Broadcasts new messages         |

**Important:** The Redis List and Pub/Sub channel use separate names to avoid confusion between persistent message storage and live message broadcasting.

### Message Flow

1. A participant joins a chat room.
2. Participant information is stored in a Redis Set.
3. The participant sends a message through the REST API.
4. The message is serialized into JSON.
5. The JSON message is stored in the Redis List.
6. The same message is published to the room's Pub/Sub channel.
7. Redis subscribers receive the published message.

### Message JSON Format

Messages are stored as JSON strings:

```json
{
  "participant": "guest_user",
  "message": "Hello, everyone!",
  "timestamp": "2024-01-01T10:00:00Z"
}
```

## 📁 Project Structure

```text
SimpleChatApp/
│
├── src/
│   ├── main/
│   │   ├── java/com/example/SimpleChatApp/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/com/example/SimpleChatApp/
│
├── Dockerfile
├── README.md
└── pom.xml
```

## ⚙️ Prerequisites

Install the following:

- Java 21 or later.
- Docker Desktop.
- Git.
- Postman (optional, for API testing).

## 🔧 Setup and Installation

### 1. Clone the Repository

```bash
git clone https://github.com/Sketchh111/SimpleChatApp.git
cd SimpleChatApp
```

### 2. Start Redis Using Docker

```bash
docker run --name chat-redis -p 6379:6379 -d redis:7-alpine
```

Verify Redis:

```bash
docker exec -it chat-redis redis-cli ping
```

Expected output:

```text
PONG
```

### 3. Configure Application Properties

The application uses environment variables with local defaults.

```properties
spring.application.name=SimpleChatApp

server.port=${PORT:8080}

spring.data.redis.host=${REDIS_HOST:localhost}
spring.data.redis.port=${REDIS_PORT:6379}
```

### 4. Run the Spring Boot Application

Run the application using IntelliJ IDEA or Maven:

```bash
mvn spring-boot:run
```

The application will start at:

`http://localhost:8080`

## 📡 REST API Endpoints

Base URL:

`http://localhost:8080/api/chatapp`

| Method | Endpoint                                | Description              |
| ------ | --------------------------------------- | ------------------------ |
| POST   | `/chatrooms`                            | Create a chat room       |
| POST   | `/chatrooms/{roomId}/join`              | Join a chat room         |
| POST   | `/chatrooms/{roomId}/messages`          | Send a message           |
| GET    | `/chatrooms/{roomId}/messages?limit=10` | Retrieve latest messages |
| DELETE | `/chatrooms/{roomId}`                   | Delete a chat room       |

### 1. Create Chat Room

**POST** `/api/chatapp/chatrooms`

Request:

```json
{
  "roomName": "general"
}
```

Success response:

```json
{
  "message": "Chat room 'general' created successfully.",
  "roomId": "general",
  "status": "success"
}
```

### 2. Join Chat Room

**POST** `/api/chatapp/chatrooms/general/join`

Request:

```json
{
  "participant": "guest_user"
}
```

Success response:

```json
{
  "message": "User 'guest_user' joined chat room 'general'.",
  "status": "success"
}
```

### 3. Send Message

**POST** `/api/chatapp/chatrooms/general/messages`

Request:

```json
{
  "participant": "guest_user",
  "message": "Hello, everyone!"
}
```

Success response:

```json
{
  "message": "Message sent successfully.",
  "status": "success"
}
```

### 4. Retrieve Messages

**GET** `/api/chatapp/chatrooms/general/messages?limit=10`

Success response:

```json
{
  "messages": [
    {
      "participant": "guest_user",
      "message": "Hello, everyone!",
      "timestamp": "2024-01-01T10:00:00Z"
    }
  ]
}
```

The API retrieves the latest N messages while maintaining chronological order.

### 5. Delete Chat Room

**DELETE** `/api/chatapp/chatrooms/general`

Success response:

```json
{
  "message": "Chat room 'general' deleted successfully.",
  "status": "success"
}
```

Deleting a room removes its Redis Hash, participant Set, and message List.

## 📢 Redis Pub/Sub Implementation

Redis Pub/Sub is used to broadcast newly sent messages.

**Channel naming convention:**

```text
room:{roomId}:channel
```

Example:

```text
room:general:channel
```

The application uses a Redis message listener container with a pattern subscription:

```java
new PatternTopic("room:*:channel")
```

When a message is sent:

- It is saved in the Redis List `room:general:messages`.
- It is published to the Redis channel `room:general:channel`.
- The configured Redis subscriber listens for published messages.

Pub/Sub provides live message delivery to active subscribers. Unlike Redis Lists, Pub/Sub messages are not persisted by Redis.

## 🐳 Docker Setup

The application includes a Dockerfile to build and run the Spring Boot application.

### 1. Build Docker Image

```bash
docker build -t simple-chat-app .
```

### 2. Create Docker Network

```bash
docker network create chat-network
```

### 3. Connect Redis to Docker Network

```bash
docker network connect chat-network chat-redis
```

### 4. Run Application Container

```bash
docker run --name simple-chat-app \
  --network chat-network \
  -p 8080:8080 \
  -e REDIS_HOST=chat-redis \
  -e REDIS_PORT=6379 \
  simple-chat-app
```

For PowerShell, the same command can be written on one line:

```powershell
docker run --name simple-chat-app --network chat-network -p 8080:8080 -e REDIS_HOST=chat-redis -e REDIS_PORT=6379 simple-chat-app
```

Swagger UI:

`http://localhost:8080/swagger-ui.html`

## 🧪 Testing

The project includes automated unit tests and Redis integration tests.

### Unit Testing

JUnit 5 and Mockito are used to test service-layer business logic.

Test scenarios include:

- Successful room creation.
- Duplicate room creation.
- Successful participant joining.
- Duplicate participant joining.
- Joining a nonexistent room.
- Successful message sending.
- Unauthorized message sending.
- Sending messages to nonexistent rooms.
- Retrieving messages.
- Invalid message retrieval limits.

### Redis Integration Testing

Integration tests verify actual Redis operations:

- Room metadata storage in Redis Hash.
- Participant storage in Redis Set.
- Message storage and retrieval using Redis List.
- Retrieval of the latest N messages.
- Message publishing and subscriber delivery through Redis Pub/Sub.
- Room deletion and associated Redis key cleanup.

**Test Result:** All 18 automated tests passed successfully.

Run tests using Maven:

```bash
mvn test
```

## ⚠️ Error Handling

The application handles common error scenarios using centralized exception handling.

| HTTP Status     | Scenario                              |
| --------------- | ------------------------------------- |
| 400 Bad Request | Invalid request or validation failure |
| 403 Forbidden   | Participant has not joined the room   |
| 404 Not Found   | Chat room does not exist              |
| 409 Conflict    | Duplicate room or participant         |

## 👨‍💻 Author

**Khushal Khairnar**

Java Backend Developer

Technologies: Java, Spring Boot, Redis, REST APIs, Hibernate.

---

**Note:** This project was developed as a backend development demonstrate REST API development, Redis data structures, Pub/Sub messaging, exception handling, automated testing, and Docker containerization.
