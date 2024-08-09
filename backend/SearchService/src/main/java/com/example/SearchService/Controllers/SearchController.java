package com.example.SearchService.Controllers;

import com.example.SearchService.Domain.*;
import com.example.SearchService.ScrapingService.FacebookScraper;
import com.example.SearchService.ScrapingService.InstgrameScraper;
import com.example.SearchService.ScrapingService.NewsScraper;
import com.example.SearchService.ScrapingService.ScrapingService;
import com.example.SearchService.SentimentAnalysis.SentimentClient;
import com.example.SearchService.Service.ResultService;
import com.example.SearchService.Service.SearchService;
import com.example.notificationService.Domain.NotificationResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.net.URI;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;



@RestController
public class SearchController {

    private final ResultService resultService;
    private final List<SseEmitter> emitters = new ArrayList<>();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private final ScrapingService facebookScraper;
    private final ScrapingService instagramScraper;
    private final ScrapingService newsScraper;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, NotificationResponse> kafkaTemplate;
    private String topic;

    private static final String URL = "https://api-inference.huggingface.co/models/lxyuan/distilbert-base-multilingual-cased-sentiments-student";

    @Autowired
    private RestTemplate restTemplate;

    private final SearchService searchService;

    private SentimentClient sentimentClient;


    @Autowired
    public SearchController(ResultService resultService, SearchService searchService, ObjectMapper objectMapper, KafkaTemplate<String, NotificationResponse> kafkaTemplate, @Value("${sadek.kafka.topicc}") String topic, SentimentClient sentimentClient) {
        this.resultService = resultService;
        this.searchService = searchService;
        this.facebookScraper = new FacebookScraper(sentimentClient);
        this.instagramScraper = new InstgrameScraper(sentimentClient);
        this.newsScraper = new NewsScraper(sentimentClient);
        this.objectMapper = objectMapper;
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
        this.sentimentClient = sentimentClient;
    }
    @CrossOrigin
    @PostMapping(path = "/search")
    public ArrayList<Result> search(@RequestBody Query query) {
        SearchEntity searchEntity  = new SearchEntity();
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
        searchEntity.setKeywords(keywords);
        searchEntity.setSources(sources);
        resultService.CreateResult(results);
        searchService.save(searchEntity);
        return results;
    }
    @CrossOrigin
    @PostMapping("/download-image")
    public ResponseEntity<String> downloadImage(@RequestBody ImageUrl imageUrl) {
        try {
            
            URI uri = new URI(imageUrl.getUrl());
            //restTemplate.get

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
    @CrossOrigin
    @GetMapping(path = "/search")
    public List<SearchEntity> getAllSearches(){
        return searchService.findAll();
    }
    @CrossOrigin
    @GetMapping(path = "/search/{id}")
    public List<Result> getSearchById(@PathVariable Long id) {
        SearchEntity searchEntity = searchService.findById(id);
        List<Result> results = resultService.getResultsByKeywordsAndSources(searchEntity.getKeywords(), searchEntity.getSources());
        return results;
    }
    @CrossOrigin
    @PostMapping(path= "/addSearch")
    public SearchEntity addSearch(@RequestBody Query query) {
        SearchEntity searchEntity = new SearchEntity();
        searchEntity.setKeywords(query.getKeywords());
        searchEntity.setSources(query.getSources());
        return searchService.save(searchEntity);
    }

    @CrossOrigin
    @DeleteMapping(path = "/result/{id}")
    public String deleteResult(@PathVariable String id) {
        resultService.DeleteResult(id);
        return "deleted success";
    }

    @CrossOrigin
    @DeleteMapping(path = "/search/{id}")
    public String deleteSearch(@PathVariable Long id) {
        searchService.deleteById(id);
        return "deleted success";
    }

    @GetMapping("/sentiment")
    public void getSentiment() {
        Sentiment input = Sentiment.builder().input("i hate you").build();
        Sentiment output = sentimentClient.sentiment(input);
        System.out.println(output.getLabel());
    }

}
