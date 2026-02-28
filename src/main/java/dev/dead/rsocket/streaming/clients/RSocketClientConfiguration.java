package dev.dead.rsocket.streaming.clients;

import dev.dead.rsocket.streaming.models.StockQuote;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Configuration
@Slf4j
public class RSocketClientConfiguration {

    @Bean
    public ApplicationRunner sender(RSocketRequester.Builder requesterBuilder,
                                    @Value("${spring.rsocket.server.port:8787}") int port) {
        return _ -> {
            RSocketRequester tcp = requesterBuilder.tcp("localhost", port);

            // 1. Request-Response
            requestResponse(tcp, "Ahmed");

            // 2. Fire-and-Forget
//            fireAndForget(tcp);

            // 3. Request-Stream
//            requestStream(tcp, "XYZ");

            // 4. Channel (Bidirectional Streaming)
//            requestChannel(tcp);
        };
    }

    /**
     * REQUEST-RESPONSE: One request, one response.
     */
    private void requestResponse(RSocketRequester requester, String name) {
        requester.route("greeting/{name}", name)
                .data("Hello RSocket!")
                .retrieveMono(String.class)
                .subscribe(res -> log.info("[Request-Response] Response: {}", res));
    }

    /**
     * FIRE-AND-FORGET: Send data, expect no response.
     */
    private void fireAndForget(RSocketRequester requester) {
        requester.route("alert")
                .data("Critical System Warning!")
                .send()
                .subscribe(); // Subscription triggers the send
        log.info("[Fire-and-Forget] Alert sent.");
    }

    /**
     * REQUEST-STREAM: One request, a stream of responses.
     */
    private void requestStream(RSocketRequester requester, String symbol) {
        requester.route("stock/{symbol}", symbol)
                .retrieveFlux(StockQuote.class)
                .subscribe(quote -> log.info("[Request-Stream] {} Price: {} at {}",
                        quote.getSymbol(), quote.getPrice(), quote.getTimestamp()));
    }

    /**
     * CHANNEL: Bidirectional stream of data.
     */
    private void requestChannel(RSocketRequester requester) {
        Flux<String> settingsStream = Flux.interval(Duration.ofSeconds(5))
                .map(i -> "Setting Update #" + i);

        requester.route("settings-channel")
                .data(settingsStream)
                .retrieveFlux(String.class)
                .subscribe(ack -> log.info("[Channel] Server ACK: {}", ack));
    }
}
