package dev.dead.rsocket.streaming.servers;

import dev.dead.rsocket.streaming.models.StockQuote;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@Controller
public class RSocketPatternsServer {

    /**
     * 1. REQUEST-RESPONSE
     * One request, one response. Captures a path variable {name}.
     */
    @MessageMapping("greeting/{name}")
    public Mono<String> handleGreeting(@DestinationVariable("name") String name,
                                       Mono<String> greetingMono) {
        return greetingMono
                .doOnNext(msg -> log.info("[Request-Response] Received from {}: {}", name, msg))
                .map(msg -> "Hello to you, too, " + name);
    }

    /**
     * 2. FIRE-AND-FORGET
     * Receives data and returns Mono<Void>. No response is sent back to the client.
     */
    @MessageMapping("alert")
    public Mono<Void> handleAlert(Mono<String> alertMono) {
        return alertMono
                .doOnNext(alert -> log.info("[Fire-and-Forget] Alert received: {}", alert))
                .then(); // Completion signal only
    }

    /**
     * 3. REQUEST-STREAM
     * Returns an infinite Flux of data.
     */
    @MessageMapping("stock/{symbol}")
    public Flux<StockQuote> getStockPrice(
            @DestinationVariable("symbol") String symbol) {
        return Flux
                .interval(Duration.ofSeconds(1))
                .map(i -> new StockQuote(symbol, BigDecimal.valueOf(Math.random() * 100), Instant.now()))
                .doOnCancel(() -> log.info("[Request-Stream] Client cancelled stream for {}", symbol));
    }

    /**
     * 4. CHANNEL
     * Bidirectional streaming: The server reacts to a continuous stream from the client.
     */
    @MessageMapping("settings-channel")
    public Flux<String> handleSettingsChannel(Flux<String> settingsFlux) {
        return settingsFlux
                .doOnNext(setting -> log.info("[Channel] Processing: {}", setting))
                .map(setting -> "ACK: Updated " + setting);
    }
}
