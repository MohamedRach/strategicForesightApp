package com.example.SentimentAnalysis;

import com.example.SentimentAnalysis.DTO.messageDTO;
import reactor.core.publisher.Mono;

public interface Chat {
    Mono<Object> handleChat(messageDTO message);
}
