package com.example.SearchService;


import com.example.SearchService.Domain.Result;
import com.example.SearchService.Domain.SearchEntity;
import com.example.SearchService.ScrapingService.FacebookScraper;
import com.example.SearchService.ScrapingService.InstgrameScraper;
import com.example.SearchService.ScrapingService.NewsScraper;
import com.example.SearchService.ScrapingService.ScrapingService;
import com.example.SearchService.SentimentAnalysis.SentimentClient;
import com.example.SearchService.Service.ResultService;
import com.example.SearchService.Service.SearchService;
import com.example.notificationService.Domain.NotificationEntity;
import com.example.notificationService.Domain.NotificationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;


@Component
public class KafkaConsumer {

    private final ResultService resultService;
    private final KafkaTemplate<String, NotificationResponse> kafkaTemplate;
    private final ScrapingService facebookScraper;
    private final ScrapingService instagramScraper;
    private final ScrapingService newsScraper;
    private final SearchService searchService;
    private final String topic;
    private SentimentClient sentimentClient;
    @Autowired
    public KafkaConsumer(ResultService resultService,SentimentClient sentimentClient, SearchService searchService, ObjectMapper objectMapper, KafkaTemplate<String, NotificationResponse> kafkaTemplate, @Value("${sadek.kafka.topicc}") String topic) {
        this.resultService = resultService;
        this.searchService = searchService;
        this.facebookScraper = new FacebookScraper(sentimentClient);
        this.instagramScraper = new InstgrameScraper(sentimentClient);
        this.newsScraper = new NewsScraper(sentimentClient);
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @KafkaListener(topics = "${sadek.kafka.topic}", groupId = "my-group")
    public void listen(NotificationEntity message) {
        // Process the message
        search(message);

    }

    private void search(NotificationEntity notification) {
        SearchEntity searchEntity  = new SearchEntity();

        List<String> keywords = notification.getKeywords();
        List<String> sources = notification.getSources();
        ArrayList<Result> results = new ArrayList<>();
        for (String source : sources) {
            switch (source) {
                case "facebook":
                    ArrayList<Result> facebookResults = facebookScraper.Scrape(keywords);
                    for (Result facebookResult : facebookResults) {
                        if (resultService.getResultByCaption(facebookResult.getCaption()) == null){
                            results.add(facebookResult);
                        }
                    }
                    break;
                case "instagram":
                    ArrayList<Result> instagramResults = instagramScraper.Scrape(keywords);
                    for (Result instagramResult : instagramResults) {
                        if (resultService.getResultByCaption(instagramResult.getCaption()) == null){
                            results.add(instagramResult);
                        }
                    }
                    break;
                case "news":
                    ArrayList<Result> newsResults = newsScraper.Scrape(keywords);
                    for (Result newsResult : newsResults) {
                        if (resultService.getResultByCaption(newsResult.getCaption()) == null) {
                            results.add(newsResult);
                        }
                    }
                    break;
                default:
                    System.out.println("Unknown source: " + source);
            }
        }
        searchEntity.setKeywords(keywords);
        searchEntity.setSources(sources);
        NotificationResponse notificationResponse = NotificationResponse.builder().sources(sources).keywords(keywords).count(results.size()).build();
        resultService.CreateResult(results);
        searchService.save(searchEntity);
        try {
            sendNotification(notificationResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sendNotification(NotificationResponse notification) throws Exception {
        kafkaTemplate.send(topic, notification);
    }
}
