package com.example.SearchService.Domain;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class Sentiment {
    private String input;
    private String label;
}