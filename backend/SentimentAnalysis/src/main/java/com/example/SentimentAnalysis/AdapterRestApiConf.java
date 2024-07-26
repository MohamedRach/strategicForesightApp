package com.example.SentimentAnalysis;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import com.example.SentimentAnalysis.DTO.messageDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log4j2
public class AdapterRestApiConf {

    @NonNull
    Chat chat;

    @Bean
    RouterFunction<ServerResponse> composedNotifyRoutes() {
        return route().POST("/chat", accept(MediaType.APPLICATION_JSON), this::createClaim)
                .build();
    }



    Mono<ServerResponse> createClaim(ServerRequest request) {
        log.info("Request received");
        System.out.println("Request received");
        return request.bodyToMono(messageDTO.class)
                .doOnNext(n -> log.info("Received request payload [messageDto={}]", n))
                .flatMap(chat::handleChat)
                .doOnNext(it -> log.info("Chat responded [chat={}]", it))
                .flatMap(c -> ServerResponse.ok()
                        .bodyValue(c));
    }

}
