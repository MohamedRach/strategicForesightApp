package com.example.SearchService.ScrapingService;

import com.example.SearchService.Domain.Response;
import com.example.SearchService.Domain.Result;
import com.example.SearchService.Domain.Sentiment;
import com.example.SearchService.SentimentAnalysis.SentimentClient;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TwitterScraper implements ScrapingService{

    private final String nextButtonClass = "css-175oi2r r-sdzlij r-1phboty r-rs99b7 r-lrvibr r-ywje51 r-184id4b r-13qz1uu r-2yi16 r-1qi8awa r-3pj75a r-1loqt21 r-o7ynqc r-6416eg r-1ny4l3l";
    private final String LoginButtonClass = "css-175oi2r r-sdzlij r-1phboty r-rs99b7 r-lrvibr r-19yznuf r-64el8z r-1fkl15p r-1loqt21 r-o7ynqc r-6416eg r-1ny4l3l";
    private final String PhoneNumberInput = "r-30o5oe r-1dz5y72 r-13qz1uu r-1niwhzg r-17gur6a r-1yadl64 r-deolkf r-homxoj r-poiln3 r-7cikom r-1ny4l3l r-t60dpp r-fdjqy7";
    private final String SecondNextButtonClass = "css-175oi2r r-sdzlij r-1phboty r-rs99b7 r-lrvibr r-19yznuf r-64el8z r-1fkl15p r-1loqt21 r-o7ynqc r-6416eg r-1ny4l3l";
    private final String TweetClass = "css-175oi2r r-1igl3o0 r-qklmqi r-1adg3ll r-1ny4l3l";
    private final String UsernameClass = "css-146c3p1 r-dnmrzs r-1udh08x r-3s2u2q r-bcqeeo r-1ttztb7 r-qvutc0 r-37j5jr r-a023e6 r-rjixqe r-16dba41 r-18u37iz r-1wvb978";
    private final String tweetClass = "css-146c3p1 r-bcqeeo r-1ttztb7 r-qvutc0 r-37j5jr r-1inkyih r-16dba41 r-bnwqim r-135wba7";
    private final String ResponseClass = "css-146c3p1 r-8akbws r-krxsd3 r-dnmrzs r-1udh08x r-bcqeeo r-1ttztb7 r-qvutc0 r-37j5jr r-a023e6 r-rjixqe r-16dba41 r-bnwqim";
    private Set<Integer> processedTweets;
    private SentimentClient sentimentClient;
    private ChromeOptions options;

    public TwitterScraper(SentimentClient sentimentClient) {
        this.sentimentClient = sentimentClient;
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
        processedTweets = new HashSet<>();
        ArrayList<Result> results = new ArrayList<>();

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        for (String keyword: keywords) {
            driver.get("https://x.com/");
            driver.findElement(By.xpath("//a[@href='/login']")).click();
            try {
                // Sleep for 5 seconds (5000 milliseconds)
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                // Handle the exception
                e.printStackTrace();
            }
            driver.findElement(By.xpath("//input[@autocomplete='username']")).sendKeys("rachidisadek@gmail.com");
            driver.findElement(By.cssSelector("button." + nextButtonClass.replace(" ", "."))).click();
            try {
                // Sleep for 5 seconds (5000 milliseconds)
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Handle the exception
                e.printStackTrace();
            }
            WebElement phoneNumberInput = driver.findElement(By.cssSelector("input." + PhoneNumberInput.replace(" ", ".")));
            if (phoneNumberInput != null) {
                phoneNumberInput.sendKeys("0766597297");
                driver.findElement(By.cssSelector("button." + SecondNextButtonClass.replace(" ", "."))).click();
                try {
                    // Sleep for 5 seconds (5000 milliseconds)
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // Handle the exception
                    e.printStackTrace();
                }
                driver.findElement(By.xpath("//input[@type='password']")).sendKeys("Papa62@2");
                driver.findElement(By.cssSelector("button." + LoginButtonClass.replace(" ", "."))).click();
            } else {
                driver.findElement(By.xpath("//input[@type='password']")).sendKeys("Papa62@2");
                driver.findElement(By.cssSelector("button." + LoginButtonClass.replace(" ", "."))).click();
            }
            try {
                // Sleep for 5 seconds (5000 milliseconds)
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                // Handle the exception
                e.printStackTrace();
            }
            driver.findElement(By.xpath("//input[@placeholder='Chercher']")).sendKeys(keyword + Keys.ENTER);
            try {
                // Sleep for 5 seconds (5000 milliseconds)
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                // Handle the exception
                e.printStackTrace();
            }
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Store the original window handle
            String originalWindow = driver.getWindowHandle();

            Integer tweetIndex = 0;
            int scrollPosition = 4000;
            while (tweetIndex < 10) {
                System.out.println(".....................tweet index: " + tweetIndex);
                List<WebElement> tweets = new ArrayList<>(driver.findElements(By.cssSelector("div." + TweetClass.replace(" ", "."))));
                for (WebElement tweet : tweets) {
                    try {

                        if (!processedTweets.contains(tweetIndex)) {
                            ArrayList<Response> responses = new ArrayList<>();
                            // Open the tweet in a new tab using Actions or JavaScript
                            Actions actions = new Actions(driver);
                            actions.keyDown(Keys.CONTROL).click(tweet).keyUp(Keys.CONTROL).build().perform();

                            try {
                                Thread.sleep(2000); // Wait for new content to load
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            // Get all window handles and switch to the last one
                            Set<String> windowHandles = driver.getWindowHandles();
                            String lastWindowHandle = null;
                            for (String windowHandle : windowHandles) {
                                lastWindowHandle = windowHandle;
                            }
                            if (lastWindowHandle != null && !lastWindowHandle.equals(originalWindow)) {
                                driver.switchTo().window(lastWindowHandle);
                            }
                            try {
                                Thread.sleep(10000); // Wait for new content to load
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Result result = new Result();
                            List<String> usernames = driver.findElements(By.cssSelector("div." + UsernameClass.replace(" ", ".")))
                                    .stream()
                                    .map(e -> e.findElement(By.tagName("span")).getText())
                                    .filter(user -> !user.equals("@RachidiSadek"))
                                    .distinct()
                                    .toList();

                            WebElement tweetDiv = driver.findElement(By.cssSelector("div." + tweetClass.replace(" ", ".")));


                            List<WebElement> spanElements = tweetDiv.findElements(By.tagName("span"));


                            StringBuilder combinedText = new StringBuilder();


                            for (WebElement span : spanElements) {
                                combinedText.append(span.getText());
                            }


                            String mainTweetText = combinedText.toString();


                            List<String> response = driver.findElements(By.cssSelector("div." + ResponseClass.replace(" ", ".")))
                                    .stream()
                                    .map(e -> e.findElements(By.tagName("span"))
                                            .stream()
                                            .map(WebElement::getText)
                                            .collect(Collectors.joining()))
                                    .distinct()
                                    .toList();

                            for (int i = 1; i < usernames.size(); i++) {
                                Response response1 = new Response();
                                response1.setUsername(usernames.get(i));
                                responses.add(response1);
                            }

                            int minSize = Math.min(responses.size(), response.size());

                            for (int i = 0; i < minSize; i++) {
                                responses.get(i).setTweet(response.get(i));
                                Sentiment input = Sentiment.builder().input(response.get(i)).build();
                                Sentiment sentiment = sentimentClient.sentiment(input);
                                responses.get(i).setSentiment(sentiment.getLabel());
                            }
                            Sentiment input = Sentiment.builder().input(mainTweetText).build();
                            Sentiment sentiment = sentimentClient.sentiment(input);
                            result.setUsername(usernames.getFirst());
                            result.setResponses(responses);
                            result.setSource("Twitter");
                            result.setDate(LocalDate.now());
                            result.setCaption(mainTweetText);
                            result.setSentiment(sentiment.getLabel());
                            result.setKeyword(keyword);
                            // Process the tweet here
                            if (results.stream().noneMatch(r -> r.getCaption().equals(result.getCaption()))) {
                                results.add(result);
                            }
                            processedTweets.add(tweetIndex);
                            tweetIndex++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // If the element becomes stale, we'll catch it on the next scroll
                        break;
                    }



                }
                // Switch back to the original window
                driver.switchTo().window(originalWindow);
                js.executeScript("window.scrollTo(0, arguments[0]);", scrollPosition);
                scrollPosition += 2000;
                try {
                    Thread.sleep(2000); // Wait for new content to load
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        driver.quit();
        return results;
    }


}
