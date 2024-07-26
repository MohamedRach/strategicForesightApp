package com.example.SentimentAnalysis.Adapter;

import com.example.SentimentAnalysis.DTO.messageDTO;
import reactor.core.publisher.Mono;

public interface ChatAdapter {

    Mono<Object> chatPrompt(messageDTO message);
}