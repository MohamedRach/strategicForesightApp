package com.example.SearchService.ScrapingService;

import com.example.SearchService.Domain.Result;
import com.example.SearchService.Domain.Sentiment;
import com.example.SearchService.SentimentAnalysis.SentimentClient;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class FacebookScraper implements ScrapingService{

    //span
    private static final String userNameClass = "html-span xdj266r x11i5rnm xat24cr x1mh8g0r xexx8yu x4uap5 x18d9i69 xkhd6sd x1hl2dhg x16tdsg8 x1vvkbs";
    // div
    private static final String captionClass = "xdj266r x11i5rnm xat24cr x1mh8g0r x1vvkbs x126k92a";
    // img
    private static final String imgClass1 = "x1ey2m1c xds687c x5yr21d x10l6tqk x17qophe x13vifvy xh8yej3 xl1xv1r";
    // img
    private static final String imgClass2 = "xz74otr x1ey2m1c xds687c x5yr21d x10l6tqk x17qophe x13vifvy xh8yej3";
    // span
    private static final String LikesClass = "xt0b8zv x2bj2ny xrbpyxo xl423tq";
    //public static final String LikesClass = "x1e558r4";
    private static final String reelClass = "x1i10hfl x1qjc9v5 xjbqb8w xjqpnuy xa49m3k xqeqjp1 x2hbi6w x13fuv20 xu3j5b3 x1q0q8m5 x26u7qi x972fbf xcfux6l x1qhh985 xm0m39n x9f619 x1ypdohk xdl72j9 x2lah0s xe8uvvx xdj266r x11i5rnm xat24cr x1mh8g0r x2lwn1j xeuugli xexx8yu x4uap5 x18d9i69 xkhd6sd x16tdsg8 x1hl2dhg xggy1nq x1ja2u2z x1t137rt x1o1ewxj x3x9cwd x1e5q0jg x13rtm0m x1q0g3np x87ps6o x1lku1pv x1rg5ohu x1a2a7pz x1n2onr6 xh8yej3";
    // chrome options
    private final ChromeOptions options;

    private SentimentClient sentimentClient;

    public FacebookScraper(SentimentClient sentimentClient) {
        options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        this.sentimentClient = sentimentClient;
    }


    @Override
    public ArrayList<Result> Scrape(List<String> keywords) {
        ArrayList<Result> results = new ArrayList<>();
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        driver.get("https://facebook.com/");

        driver.findElement(By.id("email")).sendKeys("rachidisadek@gmail.com");
        driver.findElement(By.id("pass")).sendKeys("Papa62@2");
        WebDriverWait wait = new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS));
        WebElement elemt = wait.until(ExpectedConditions.elementToBeClickable(By.name("login")));
        elemt.click();
        try {
            // Sleep for 5 seconds (5000 milliseconds)
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            // Handle the exception
            e.printStackTrace();
        }
        for (String keyword : keywords) {
            driver.findElement(By.xpath("//input[@placeholder='Search Facebook']")).sendKeys(keyword + Keys.ENTER);
            try {
                // Sleep for 5 seconds (5000 milliseconds)
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // Handle the exception
                e.printStackTrace();
            }
            WebElement div = driver.findElement(By.xpath("//div[@role='feed']"));
            List<WebElement> divElements = new ArrayList<>();
            int i = 0;

            while (i < 15) {
                List<WebElement> divElement = div.findElements(By.xpath("./div"));
                for (WebElement element : divElement) {
                    if (!divElements.contains(element)) {
                        divElements.add(element);
                    }
                }
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("window.scrollBy(0,1000)", "");
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
            for (WebElement divElement : divElements) {
                Result result = getResult(divElement, keyword);
                results.add(result);

            }

        }
        driver.quit();
        return results;
    }

    private Result getResult(WebElement divElement, String keyword) {
        Result result = new Result();
        LocalDate today = LocalDate.now();
        result.setSource("facebook");
        result.setKeyword(keyword);
        result.setDate(today);
        try {
            WebElement userNameElement = divElement.findElement(By.cssSelector("span." + userNameClass.replace(" ", ".")));
            String userName = userNameElement.getText();
            result.setUsername(userName);
            System.out.println("User Name: " + userName);
        } catch (Exception e) {
            result.setUsername("undefined");
            System.out.println("couldn't find username");
        }
        try {
            WebElement captionElement = divElement.findElement(By.cssSelector("div." + captionClass.replace(" ", ".")));
            String caption = captionElement.getText();
            Sentiment input = Sentiment.builder().input(caption).build();
            Sentiment sentiment = sentimentClient.sentiment(input);
            result.setSentiment(sentiment.getLabel());
            result.setCaption(caption);
            System.out.println("caption: " + caption);
        } catch (Exception e) {
            //e.printStackTrace();
            result.setCaption("undefined");
            System.out.println("couldn't find caption");
        }
        try {
            WebElement imgOneElement = divElement.findElement(By.cssSelector("img." + imgClass1.replace(" ", ".")));
            String img = imgOneElement.getAttribute("src");
            result.setImg(img);
            System.out.println("image: " + img);
        } catch (Exception e) {
            //e.printStackTrace();
            result.setImg("undefined");
            System.out.println("couldn't find image One");
        }
        try {
            WebElement imgOneElement = divElement.findElement(By.cssSelector("img." + imgClass2.replace(" ", ".")));
            String img = imgOneElement.getAttribute("src");
            result.setImg(img);
            System.out.println("image: " + img);
        } catch (Exception e) {
            if (result.getImg().equals("undefined")) {
                result.setImg("undefined");
            }
            System.out.println("couldn't find image two");
        }
        try {
            WebElement likesElement = divElement.findElement(By.cssSelector("span." + LikesClass.replace(" ", ".")));
            String likes = likesElement.getText();
            result.setLikes(likes);
            System.out.println("likes: " + likes);
        } catch (Exception e) {
            //e.printStackTrace();
            result.setLikes("undefined");
            System.out.println("couldn't find number of likes");
        }
        try {
            WebElement reelElement = divElement.findElement(By.cssSelector("a." + reelClass.replace(" ", ".")));
            String reel = reelElement.getAttribute("src");
            result.setImg(reel);
            System.out.println("reel: " + reel);
        } catch (Exception e) {
            //e.printStackTrace();
            if (result.getImg().equals("undefined")) {
                result.setImg("undefined");
            }
            System.out.println("couldn't find reel");
        }
        return result;
    }
}
