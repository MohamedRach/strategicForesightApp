package com.example.SearchService.Controllers;

import com.example.SearchService.Domain.*;
import com.example.SearchService.ScrapingService.FacebookScraper;
import com.example.SearchService.ScrapingService.InstgrameScraper;
import com.example.SearchService.ScrapingService.NewsScraper;
import com.example.SearchService.ScrapingService.ScrapingService;
import com.example.SearchService.Service.ResultService;
import com.example.SearchService.Service.SearchService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private static final String URL = "https://api-inference.huggingface.co/models/lxyuan/distilbert-base-multilingual-cased-sentiments-student";

    @Autowired
    private RestTemplate restTemplate;

    private final SearchService searchService;


    @Autowired
    public SearchController(ResultService resultService, SearchService searchService, ObjectMapper objectMapper) {
        this.resultService = resultService;
        this.searchService = searchService;
        this.facebookScraper = new FacebookScraper();
        this.instagramScraper = new InstgrameScraper();
        this.newsScraper = new NewsScraper();
        this.objectMapper = objectMapper;
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
    public ResponseEntity<String> getSentiment() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth("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");

        String requestBody = "{\"inputs\": \"" + "@esmeralda_rania وفاة زوج المؤثرة #رانيا_إزميرالدا … غاضتني\"" + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        int MAX_RETRIES = 3;
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);

                if (response.getStatusCode() == HttpStatus.OK) {
                    ArrayNode jsonNode = (ArrayNode) objectMapper.readTree(response.getBody());
                    String highestScoreLabel = getHighestScoreLabel(jsonNode);
                    return ResponseEntity.ok(highestScoreLabel);
                } else if (response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                    JsonNode errorJson = objectMapper.readTree(response.getBody());
                    if (errorJson.has("error") && errorJson.has("estimated_time")) {
                        String errorMessage = errorJson.get("error").asText();
                        double estimatedTime = errorJson.get("estimated_time").asDouble();

                        if (errorMessage.contains("Model") && errorMessage.contains("is currently loading")) {
                            System.out.println("Model is loading. Estimated time: " + estimatedTime + " seconds");
                            Thread.sleep((long) (estimatedTime * 1000) + 1000); // Wait for estimated time plus 1 second
                            continue;
                        }
                    }
                }

                // If we reach here, it's an unhandled status code
                return ResponseEntity.status(response.getStatusCode())
                        .body("Unexpected response: " + response.getBody());

            } catch (Exception e) {
                System.err.println("Error occurred: " + e.getMessage());

                if (attempt == MAX_RETRIES - 1) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error processing sentiment after " + MAX_RETRIES + " attempts");
                }

                try {
                    Thread.sleep(5000); // Wait 5 seconds before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Retry interrupted");
                }
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to process sentiment after " + MAX_RETRIES + " attempts");
    }

    private String getHighestScoreLabel(ArrayNode jsonNode) {
        if (jsonNode.isEmpty() || jsonNode.get(0).isEmpty()) {
            return "No sentiment data available";
        }

        JsonNode sentiments = jsonNode.get(0);
        String highestLabel = "";
        double highestScore = Double.NEGATIVE_INFINITY;

        for (JsonNode sentiment : sentiments) {
            double score = sentiment.get("score").asDouble();
            if (score > highestScore) {
                highestScore = score;
                highestLabel = sentiment.get("label").asText();
            }
        }

        return highestLabel;
    }

}
