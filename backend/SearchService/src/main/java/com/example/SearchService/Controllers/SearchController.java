package com.example.SearchService.Controllers;

import com.example.SearchService.Domain.ImageUrl;
import com.example.SearchService.Domain.Query;
import com.example.SearchService.Domain.Result;
import com.example.SearchService.ScrapingService.FacebookScraper;
import com.example.SearchService.ScrapingService.InstgrameScraper;
import com.example.SearchService.ScrapingService.NewsScraper;
import com.example.SearchService.ScrapingService.ScrapingService;
import com.example.SearchService.Service.ResultService;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
public class SearchController {

    private final ResultService resultService;
    private final List<SseEmitter> emitters = new ArrayList<>();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private final ScrapingService facebookScraper;
    private final ScrapingService instagramScraper;
    private final ScrapingService newsScraper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public SearchController(ResultService resultService) {
        this.resultService = resultService;
        this.facebookScraper = new FacebookScraper();
        this.instagramScraper = new InstgrameScraper();
        this.newsScraper = new NewsScraper();
    }
    @CrossOrigin
    @PostMapping(path = "/search")
    public ArrayList<Result> search(@RequestBody Query query) {
        System.out.println(query);
        List<String> keywords = query.getKeywords();
        List<String> sources = query.getSources();
        ArrayList<Result> results = new ArrayList<>();
        for (String source : sources) {
            switch (source) {
                case "facebook":
                    ArrayList<Result> facebookResults = facebookScraper.Scrape(keywords);
                    results.addAll(facebookResults);
                    break;
                case "instagram":
                    ArrayList<Result> instagramResults = instagramScraper.Scrape(keywords);
                    results.addAll(instagramResults);
                    break;
                case "news":
                    ArrayList<Result> newsResults = newsScraper.Scrape(keywords);
                    results.addAll(newsResults);
                    break;
                default:
                    System.out.println("Unknown source: " + source);
            }
        }
        resultService.CreateResult(results);
        this.stopEventStreaming();
        return results;
    }
    @CrossOrigin
    @PostMapping("/download-image")
    public ResponseEntity<String> downloadImage(@RequestBody ImageUrl imageUrl) {
        try {
            
            URI uri = new URI(imageUrl.getUrl());

            // Fetch the image data
            byte[] imageData = restTemplate.getForObject(uri, byte[].class);

            // Encode the image data to Base64
            String base64Image = Base64.getEncoder().encodeToString(imageData);

            // Create a data URL
            String dataUrl = "data:image/jpeg;base64," + base64Image;

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(dataUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error downloading image: " + e.getMessage());
        }
    }

    @PostMapping(path = "/test")
    public Query test(@RequestBody Query query) {

        return query;
    }

    @GetMapping("/events")
    public SseEmitter streamEvents() {
        SseEmitter emitter = new SseEmitter();
        this.addEmitter(emitter);
        this.startEventStreaming();
        return emitter;
    }

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }
    public void startEventStreaming() {
        executorService.scheduleAtFixedRate(this::sendEvent, 0, 1, TimeUnit.SECONDS);
    }
    public void stopEventStreaming() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
    public void sendEvent() {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send("in progress");
            } catch (Exception e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }

    }
}
