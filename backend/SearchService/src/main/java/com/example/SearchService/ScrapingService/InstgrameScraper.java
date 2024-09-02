package com.example.SearchService.ScrapingService;

import com.example.SearchService.Domain.Response;
import com.example.SearchService.Domain.Result;
import com.example.SearchService.Domain.Sentiment;
import com.example.SearchService.SentimentAnalysis.SentimentClient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.min;

public class InstgrameScraper implements ScrapingService{

    public static final String classNames = "x1i10hfl xjbqb8w x1ejq31n xd10rxx x1sy0etr x17r0tee x972fbf xcfux6l x1qhh985 xm0m39n x9f619 x1ypdohk xt0psk2 xe8uvvx xdj266r x11i5rnm xat24cr x1mh8g0r xexx8yu x4uap5 x18d9i69 xkhd6sd x16tdsg8 x1hl2dhg xggy1nq x1a2a7pz _a6hd";
    public static final String userNameclass = "x1i10hfl xjqpnuy xa49m3k xqeqjp1 x2hbi6w xdl72j9 x2lah0s xe8uvvx xdj266r x11i5rnm xat24cr x1mh8g0r x2lwn1j xeuugli x1hl2dhg xggy1nq x1ja2u2z x1t137rt x1q0g3np x1lku1pv x1a2a7pz x6s0dn4 xjyslct x1ejq31n xd10rxx x1sy0etr x17r0tee x9f619 x1ypdohk x1f6kntn xwhw2v2 xl56j7k x17ydfre x2b8uid xlyipyv x87ps6o x14atkfc xcdnw81 x1i0vuye xjbqb8w xm3z3ea x1x8b98j x131883w x16mih1h x972fbf xcfux6l x1qhh985 xm0m39n xt0psk2 xt7dq6l xexx8yu x4uap5 x18d9i69 xkhd6sd x1n2onr6 x1n5bzlp xqnirrm xj34u2y x568u83";
    public static final String captionClass = "_ap3a _aaco _aacu _aacx _aad7 _aade";
    public static final String pictureClass = "x5yr21d xu96u03 x10l6tqk x13vifvy x87ps6o xh8yej3";
    public static final String LikesClass = "html-span xdj266r x11i5rnm xat24cr x1mh8g0r xexx8yu x4uap5 x18d9i69 xkhd6sd x1hl2dhg x16tdsg8 x1vvkbs";
    public static final String userCommentClass = "x1i10hfl xjqpnuy xa49m3k xqeqjp1 x2hbi6w xdl72j9 x2lah0s xe8uvvx xdj266r x11i5rnm xat24cr x1mh8g0r x2lwn1j xeuugli x1hl2dhg xggy1nq x1ja2u2z x1t137rt x1q0g3np x1lku1pv x1a2a7pz x6s0dn4 xjyslct x1ejq31n xd10rxx x1sy0etr x17r0tee x9f619 x1ypdohk x1f6kntn xwhw2v2 xl56j7k x17ydfre x2b8uid xlyipyv x87ps6o x14atkfc xcdnw81 x1i0vuye xjbqb8w xm3z3ea x1x8b98j x131883w x16mih1h x972fbf xcfux6l x1qhh985 xm0m39n xt0psk2 xt7dq6l xexx8yu x4uap5 x18d9i69 xkhd6sd x1n2onr6 x1n5bzlp xqnirrm xj34u2y x568u83";

    private ChromeOptions options;

    private SentimentClient sentimentClient;


    public InstgrameScraper(SentimentClient sentimentClient) {
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
        for (String keyword : keywords) {
            driver.get("https://instagram.com/tags/"+ keyword);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6000));
            wait.until((ExpectedCondition<Boolean>) d -> {
                WebElement divElement = d.findElement(By.xpath("//div[@id='splash-screen']"));
                return divElement != null && divElement.getAttribute("style").contains("display: none");
            });

            // Find all a tags with the specified class names
            List<WebElement> elements = driver.findElements(By.cssSelector("a." + classNames.replace(" ", ".")));

            // Extract the href attributes
            List<String> hrefs = elements.stream()
                    .map(element -> element.getAttribute("href"))
                    .collect(Collectors.toList());

            for (String href : hrefs) {
                if (href != null && !href.contains("/p/")) {
                    System.out.println("Link without '/p/': " + href);
                    continue;
                }
                Result result = getResult(href, keyword);
                results.add(result);


            }



        }
        driver.quit();
        return results;
    }
    // retrieving data from each post
    private Result getResult(String post, String keyword) {
        Result result = new Result();
        LocalDate today = LocalDate.now();
        result.setSource("instagram");
        result.setKeyword(keyword);
        result.setDate(today);
        WebDriver innerDriver = new ChromeDriver(options);
        WebDriverWait innerWait = new WebDriverWait(innerDriver, Duration.ofSeconds(30));
        innerDriver.get(post);
        innerWait.until((ExpectedCondition<Boolean>) d -> {
            WebElement divElement = d.findElement(By.xpath("//div[@id='splash-screen']"));
            return divElement != null && divElement.getAttribute("style").contains("display: none");
        });
        try {
            // Sleep for 5 seconds (5000 milliseconds)
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            // Handle the exception
            e.printStackTrace();
        }

        try {
            WebElement usernameElement = innerDriver.findElement(By.cssSelector("a." + userNameclass.replace(" ", ".")));
            result.setUsername(usernameElement.getText());
            System.out.println("username: " + usernameElement.getText());
        } catch (Exception e) {
            System.out.println("couldn't find username");
            result.setUsername("Undefined");
            e.printStackTrace();
        }

        try {
            WebElement captionElement = innerDriver.findElement(By.cssSelector("h1." + captionClass.replace(" ", ".")));
            Sentiment input = Sentiment.builder().input(captionElement.getText()).build();
            Sentiment sentiment = sentimentClient.sentiment(input);
            result.setSentiment(sentiment.getLabel());
            result.setCaption(captionElement.getText());
            System.out.println("caption: " + captionElement.getText());
        } catch (Exception e) {
            System.out.println("couldn't find caption");
            e.printStackTrace();
            result.setCaption("Undefined");
        }

        try {
            WebElement PictureElement = innerDriver.findElement(By.cssSelector("img." + pictureClass.replace(" ", ".")));
            result.setImg(PictureElement.getAttribute("src"));
            System.out.println("Picture: " + PictureElement.getAttribute("src"));
        } catch (Exception e) {
            System.out.println("couldn't find picture");
            result.setImg("Undefined");
        }

        try {
            WebElement LikesElement = innerDriver.findElement(By.cssSelector("span." + LikesClass.replace(" ", ".")));
            result.setLikes(LikesElement.getText());
            System.out.println("Likes: " + LikesElement.getText());
        } catch (Exception e) {
            System.out.println("couldn't find likes");
            result.setLikes("Undefined");
        }

        try {
            // Comments
            List<Response> responses = new ArrayList<>();
            List<WebElement> userCommentsElement = innerDriver.findElements(By.cssSelector("a." + userCommentClass.replace(" ", ".")))
                    .stream().filter(user -> !user.getText().equals(result.getUsername())).toList();
            List<WebElement> CommentsElement = innerDriver.findElements(By.cssSelector("span." + captionClass.replace(" ", ".")));
            CommentsElement.removeFirst();
            int minSize = min(CommentsElement.size(), userCommentsElement.size());

            for (int i = 0; i < minSize; i++) {
                Response response = new Response();
                Sentiment input = Sentiment.builder().input(CommentsElement.get(i).getText()).build();
                Sentiment sentiment = sentimentClient.sentiment(input);
                response.setTweet(CommentsElement.get(i).getText());
                response.setUsername(userCommentsElement.get(i).getText());
                response.setSentiment(sentiment.getLabel());
                responses.add(response);

            }
            result.setResponses(responses);
        } catch (Exception e) {
            System.out.println("couldn't find comments");
        }

        innerDriver.quit();
        return result;
    }
}
