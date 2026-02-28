package dev.dead.rsocket.streaming.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;

@Configuration
@Slf4j
public class RSocketClientConfiguration {
    private static void logResponse(String response) {
        log.info("Got a response: {}", response);
    }

    @Bean
    public ApplicationRunner sender(RSocketRequester.Builder requesterBuilder) {
        return _ -> {
            RSocketRequester tcp = requesterBuilder.tcp("localhost", 8787);
            tcp
                    .route("greeting")
                    .data("Hello RSocket!")
                    .retrieveMono(String.class)
                    .subscribe(RSocketClientConfiguration::logResponse);
            String who = "Craig";
            tcp
                    .route("greeting/{name}", who)
                    .data("Hello RSocket!")
                    .retrieveMono(String.class)
                    .subscribe(RSocketClientConfiguration::logResponse);
        };
    }
}

