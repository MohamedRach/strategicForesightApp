package com.example.SearchService.Controllers;

import com.example.SearchService.Domain.Result;
import com.example.SearchService.ScrapingService.FacebookScraper;
import com.example.SearchService.ScrapingService.InstgrameScraper;
import com.example.SearchService.ScrapingService.ScrapingService;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchController {




    @GetMapping(path = "/search")
    public String search() {

        ArrayList<String> keywords = new ArrayList<>();
        keywords.add("wimbeldon");

        ScrapingService facebookScraper = new FacebookScraper();
        ScrapingService instagramScraper = new InstgrameScraper();
        ArrayList<Result> results = instagramScraper.Scrape(keywords);




        return "<html>" +
                "<body>" +
                "</body>" +
                "</html>";
    }
}
