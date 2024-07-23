package com.example.SearchService.ScrapingService;

import com.example.SearchService.Domain.Result;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NewsScraper implements ScrapingService{

    public static final String linkClass = "JtKRv";
    public static final String articleClass = "IFHyqb DeXSAc";
    public static final String sourceClass = "vr1PYe";
    public static final String imgClass = "Quavad vwBmvb";

    private ChromeOptions options;

    public NewsScraper() {
        options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
    }
    @Override
    public ArrayList<Result> Scrape(List<String> keywords) {
        ArrayList<Result> results = new ArrayList<>();
        for (String keyword : keywords) {
            WebDriver driver = new ChromeDriver(options);

            driver.get("https://news.google.com");

            driver.findElement(By.xpath("//input[@aria-label='Search for topics, locations & sources']")).sendKeys(keyword + Keys.ENTER);
            try {
             Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            List<WebElement> articles = driver.findElements(By.cssSelector("article."+ articleClass.replace(" ", ".")));

            for (WebElement article : articles) {
                Result result = new Result();
                LocalDate today = LocalDate.now();
                result.setKeyword(keyword);
                result.setSource("news");
                result.setDate(today);
                try {
                    WebElement link = article.findElement(By.cssSelector("a." + linkClass.replace(" ", ".")));
                    result.setCaption(link.getText());
                    System.out.println("title: " + link.getText());
                    System.out.println("link: " + link.getAttribute("href"));
                } catch (Exception e) {
                    System.out.println("couldn't find link");
                    result.setCaption("undefined");
                    result.setUsername("undefined");

                }

                try {
                    WebElement source = article.findElement(By.cssSelector("div." + sourceClass.replace(" ", ".")));
                    System.out.println("source: " + source.getText());
                    result.setUsername(source.getText());
                } catch (Exception e) {
                    System.out.println("couldn't find source");
                    result.setSource("undefined");

                }
                try {
                    WebElement img = article.findElement(By.cssSelector("img." + imgClass.replace(" ", ".")));
                    System.out.println("img: " + img.getAttribute("src"));
                    result.setImg(img.getAttribute("src"));
                } catch (Exception e) {
                    System.out.println("couldn't find img");
                    result.setImg("undefined");

                }
                results.add(result);
            }
            driver.quit();
        }
        return results;
    }
}
