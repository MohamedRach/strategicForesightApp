package com.example.SentimentAnalysis;

import com.example.SentimentAnalysis.Adapter.ChatAdapter;
import com.example.SentimentAnalysis.DTO.messageDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log4j2
public class ChatApplication implements Chat {

    @NonNull
    ChatAdapter adapter;

    @Override
    public Mono<Object> handleChat(messageDTO message) {
        return Mono.just(message)
                .doOnNext(it -> log.info("Processing in chat application [messageDto={}]", it))
                .flatMap(adapter::chatPrompt);
    }
}
