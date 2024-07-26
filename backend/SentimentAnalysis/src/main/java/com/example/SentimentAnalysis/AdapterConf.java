package com.example.SentimentAnalysis;

import java.time.Duration;

import com.example.SentimentAnalysis.Adapter.ChatAdapter;
import com.example.SentimentAnalysis.Adapter.ChatLangChainAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.huggingface.HuggingFaceChatModel;

import static dev.langchain4j.model.huggingface.HuggingFaceModelName.TII_UAE_FALCON_7B_INSTRUCT;

public class AdapterConf {

    @Value("${application.ai.huggingface.chat.api-key}")
    String huggingFaceAccessToken;

    @Bean
    ChatLanguageModel chatLanguageModel() {
        return HuggingFaceChatModel.builder()
                .accessToken(huggingFaceAccessToken)
                .modelId(TII_UAE_FALCON_7B_INSTRUCT)
                .timeout(Duration.ofSeconds(15))
                .temperature(0.7)
                .maxNewTokens(20)
                .waitForModel(true)
                .build();
    }

    @Bean
    ChatAdapter chatLangChainAdapter(@Autowired ChatLanguageModel chatLanguageModel) {
        return new ChatLangChainAdapter(chatLanguageModel);
    }
}