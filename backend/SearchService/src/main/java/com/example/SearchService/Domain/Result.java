package com.example.SearchService.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {
    private String Id;
    private String source;
    private String caption;
    private String img;
    private String likes;
    private String username;
}
