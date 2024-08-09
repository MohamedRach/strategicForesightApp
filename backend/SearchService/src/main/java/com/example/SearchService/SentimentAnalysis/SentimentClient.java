package com.example.SearchService.SentimentAnalysis;

import com.example.SearchService.Domain.Sentiment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "sentiment-service", url = "${application.config.url}")
public interface SentimentClient {

    @PostMapping("/sentiment")
    Sentiment sentiment(@RequestBody Sentiment input);
}
