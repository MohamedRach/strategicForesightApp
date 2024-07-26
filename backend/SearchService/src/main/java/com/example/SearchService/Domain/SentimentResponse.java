package com.example.SearchService.Domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SentimentResponse {
    private List<Sentiment> sentiments;

    @Getter
    @Setter
    public static class Sentiment {
        private String label;
        private double score;


    }
}
