package com.example.nettyChat;

import java.net.URI;
import java.time.Duration;
import java.util.Scanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SocketClientApplication {

  public static void main(String[] args) {
    WebSocketClient client = new ReactorNettyWebSocketClient();
    URI uri = URI.create("ws://localhost:8888/chat");

    client.execute(uri, session ->
            session.send(Mono.just(session.textMessage("Client connected")))
                .thenMany(session.receive()
                    .map(msg -> "Received: " + msg.getPayloadAsText())
                    .doOnNext(System.out::println))
                .then())
        .subscribe();

    Scanner scanner = new Scanner(System.in);
    while (true) {
      String message = scanner.nextLine();
      client.execute(uri, session ->
              session.send(Mono.just(session.textMessage(message)))
                  .then())
          .block(Duration.ofSeconds(5));
    }
  }
}
