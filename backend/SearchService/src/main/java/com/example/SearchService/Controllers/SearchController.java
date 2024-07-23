package com.example.SearchService.Controllers;

import com.example.SearchService.Domain.ImageUrl;
import com.example.SearchService.Domain.Query;
import com.example.SearchService.Domain.Result;
import com.example.SearchService.Domain.SearchEntity;
import com.example.SearchService.ScrapingService.FacebookScraper;
import com.example.SearchService.ScrapingService.InstgrameScraper;
import com.example.SearchService.ScrapingService.NewsScraper;
import com.example.SearchService.ScrapingService.ScrapingService;
import com.example.SearchService.Service.ResultService;
import com.example.SearchService.Service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.net.URI;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
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

    @Autowired
    private RestTemplate restTemplate;

    private final SearchService searchService;


    @Autowired
    public SearchController(ResultService resultService, SearchService searchService) {
        this.resultService = resultService;
        this.searchService = searchService;
        this.facebookScraper = new FacebookScraper();
        this.instagramScraper = new InstgrameScraper();
        this.newsScraper = new NewsScraper();
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
        System.out.println(searchEntity.getKeywords());
        System.out.println(searchEntity.getSources());
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
    @DeleteMapping(path = "/search/{id}")
    public String deleteResult(@PathVariable String id) {
        resultService.DeleteResult(id);
        return "deleted success";
    }


}
