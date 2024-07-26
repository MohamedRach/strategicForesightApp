package com.example.SentimentAnalysis;

import com.example.SentimentAnalysis.Adapter.ChatAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class ApplicationConf {

    @Bean
    Chat chat(@Autowired ChatAdapter chatAdapter) {
        return new ChatApplication(chatAdapter);
    }

}