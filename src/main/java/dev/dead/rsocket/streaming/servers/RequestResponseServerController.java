package dev.dead.rsocket.streaming.servers;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class RequestResponseServerController {

    // This method handles incoming messages sent to the "greeting" route.
    // It takes a Mono<String> as input, which represents the asynchronous receipt of a greeting message.
    @MessageMapping("greeting")
    public Mono<String> handleGreeting(@NonNull Mono<String> greetingMono) {
        return greetingMono
                .doOnNext(greeting ->
                        log.info("Received a greeting: {}", greeting))
                .map(_ -> "Hello back to you!");
    }

    // This method handles incoming messages sent to the "greeting/{name}" route, where {name} is a path variable.
    // It takes a Mono<String> as input, which represents the asynchronous receipt of a greeting message, and also captures the name from the route.
    @MessageMapping("greeting/{name}")
    public Mono<String> handleGreeting(
            @DestinationVariable("name") String name,
            @NonNull Mono<String> greetingMono) {
        return greetingMono
                .doOnNext(greeting ->
                        log.info("Received a greeting from {} : {}", name, greeting))
                .map(_ -> "Hello to you, too, " + name);
    }
}
