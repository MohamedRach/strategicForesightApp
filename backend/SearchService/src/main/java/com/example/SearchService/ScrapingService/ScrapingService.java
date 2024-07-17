package com.example.SearchService.ScrapingService;

import com.example.SearchService.Domain.Result;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface ScrapingService {
    ArrayList<Result> Scrape(ArrayList<String> keywords);
}
