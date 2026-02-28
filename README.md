# Spring Boot RSocket Patterns Demo

A high-performance, reactive demonstration of **RSocket** communication patterns using **Spring Boot 4**, **Java 25**, and **Project Reactor**. This project showcases how to move beyond traditional REST by leveraging multiplexed, bidirectional binary streaming.

---

## 📡 RSocket Interaction Models

RSocket is a binary protocol that treats a connection as a stream of frames. Below are the four patterns implemented in this project:

### 1. Request-Response (`Mono<T>`)
* **The Pattern**: One request yields exactly one response.
* **How it works**: Similar to a standard HTTP call but asynchronous and multiplexed over a single connection.
* **Use Case**: Fetching a user profile or a single record.

### 2. Fire-and-Forget (`Mono<Void>`)
* **The Pattern**: The client sends data and expects no response.
* **How it works**: The client moves on immediately after sending. The server processes the data silently.
* **Use Case**: Non-critical telemetry, metrics, or background logging.

### 3. Request-Stream (`Flux<T>`)
* **The Pattern**: One request yields a continuous stream of responses.
* **How it works**: The server pushes data to the client as it becomes available. Supports **Backpressure**, meaning the client can control the flow.
* **Use Case**: Live stock price feeds or log tailing.

### 4. Channel (`Flux<T>` ↔ `Flux<T>`)
* **The Pattern**: A bidirectional, conversational stream.
* **How it works**: Both client and server send multiple messages simultaneously over the same stream.
* **Use Case**: Real-time collaborative tools or interactive gaming.

---

## 🛠 Tech Stack & Requirements

* **Java 25**: Leveraging modern language features like unnamed variables (`_`).
* **Spring Boot 4**: Utilizing the latest Spring framework improvements.
* **Project Reactor**: Core library for non-blocking reactive programming.
* **Lombok**: For boilerplate-free model and log management.

---

## ⚙️ Configuration

The application is configured to use **TCP** transport on port **8787**.

**application.yml**
```yaml
spring:
  rsocket:
    server:
      port: 8787
logging:
  level:
    dev.dead: DEBUG
