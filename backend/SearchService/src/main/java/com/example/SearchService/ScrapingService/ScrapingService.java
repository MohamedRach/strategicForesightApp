package com.example.SearchService.ScrapingService;

import com.example.SearchService.Domain.Result;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public interface ScrapingService {
    ArrayList<Result> Scrape(List<String> keywords);
}
