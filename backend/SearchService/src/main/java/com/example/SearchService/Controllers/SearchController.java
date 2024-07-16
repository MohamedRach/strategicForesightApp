package com.example.SearchService.Controllers;

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
        WebDriver driver = new ChromeDriver();

        driver.get("https://facebook.com/");

        driver.findElement(By.id("email")).sendKeys("rachidisadek@gmail.com");
        driver.findElement(By.id("pass")).sendKeys("Papa62@2");
        driver.findElement(By.name("login")).click();
        try {
            // Sleep for 5 seconds (5000 milliseconds)
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // Handle the exception
            e.printStackTrace();
        }

        driver.findElement(By.xpath("//input[@placeholder='Search Facebook']")).sendKeys("euro2024" + Keys.ENTER);
        try {
            // Sleep for 5 seconds (5000 milliseconds)
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // Handle the exception
            e.printStackTrace();
        }
        WebElement div = driver.findElement(By.xpath("//div[@role='feed']"));
        List<WebElement> divElements = div.findElements(By.xpath("./div"));

        //span
        String userNameClass = "html-span xdj266r x11i5rnm xat24cr x1mh8g0r xexx8yu x4uap5 x18d9i69 xkhd6sd x1hl2dhg x16tdsg8 x1vvkbs";
        // div
        String captionClass = "xdj266r x11i5rnm xat24cr x1mh8g0r x1vvkbs x126k92a";
        // img
        String imgClass1 = "x1ey2m1c xds687c x5yr21d x10l6tqk x17qophe x13vifvy xh8yej3 xl1xv1r";
        // img
        String imgClass2 = "xz74otr x1ey2m1c xds687c x5yr21d x10l6tqk x17qophe x13vifvy xh8yej3";
        // span
        String LikesClass = "xt0b8zv x2bj2ny xrbpyxo xl423tq";

        for (WebElement divElement : divElements) {
            try {
                WebElement userNameElement = divElement.findElement(By.cssSelector("span." + userNameClass.replace(" ", ".")));
                String userName = userNameElement.getText();
                System.out.println("User Name: " + userName);
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("couldn't find username");
            }
            try {
                WebElement captionElement = divElement.findElement(By.cssSelector("div." + captionClass.replace(" ", ".")));
                String caption = captionElement.getText();
                System.out.println("caption: " + caption);
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("couldn't find caption");
            }
            try {
                WebElement imgOneElement = divElement.findElement(By.cssSelector("img." + imgClass1.replace(" ", ".")));
                String img = imgOneElement.getAttribute("src");
                System.out.println("image: " + img);
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("couldn't find image One");
            }
            try {
                WebElement imgOneElement = divElement.findElement(By.cssSelector("img." + imgClass2.replace(" ", ".")));
                String img = imgOneElement.getAttribute("src");
                System.out.println("image: " + img);
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("couldn't find image two");
            }
            try {
                WebElement likesElement = divElement.findElement(By.cssSelector("span." + LikesClass.replace(" ", ".")));
                String likes = likesElement.getText();
                System.out.println("likes: " + likes);
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("couldn't find number of likes");
            }

        }

        /*
        for (WebElement div : divElements) {
            System.out.println(div.getAttribute("innerHTML"));
        }
        driver.quit();
        /*
        WebDriver driver = new ChromeDriver();

        driver.get("https://instagram.com/tags/euro2024");
        String pageTitle = driver.getTitle();
        System.out.println("Page title is: " + pageTitle);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6000));
        wait.until((ExpectedCondition<Boolean>) d -> {
            WebElement divElement = d.findElement(By.xpath("//div[@id='splash-screen']"));
            return divElement != null && divElement.getAttribute("style").contains("display: none");
        });
        // Define the class names to search for
        String classNames = "x1i10hfl xjbqb8w x1ejq31n xd10rxx x1sy0etr x17r0tee x972fbf xcfux6l x1qhh985 xm0m39n x9f619 x1ypdohk xt0psk2 xe8uvvx xdj266r x11i5rnm xat24cr x1mh8g0r xexx8yu x4uap5 x18d9i69 xkhd6sd x16tdsg8 x1hl2dhg xggy1nq x1a2a7pz _a6hd";

        // Find all a tags with the specified class names
        List<WebElement> elements = driver.findElements(By.cssSelector("a." + classNames.replace(" ", ".")));

        for (WebElement element : elements) {
            System.out.println(element.getAttribute("href"));
        }

        String userNameclass = "x1i10hfl xjbqb8w x1ejq31n xd10rxx x1sy0etr x17r0tee x972fbf xcfux6l x1qhh985 xm0m39n x9f619 x1ypdohk xt0psk2 xe8uvvx xdj266r x11i5rnm xat24cr x1mh8g0r xexx8yu x4uap5 x18d9i69 xkhd6sd x16tdsg8 x1hl2dhg xggy1nq x1a2a7pz  _acan _acao _acat _acaw _aj1- _ap30 _a6hd";
        String captionClass = "_ap3a _aaco _aacu _aacx _aad7 _aade";
        String pictureClass = "x5yr21d xu96u03 x10l6tqk x13vifvy x87ps6o xh8yej3";
        String LikesClass = "html-span xdj266r x11i5rnm xat24cr x1mh8g0r xexx8yu x4uap5 x18d9i69 xkhd6sd x1hl2dhg x16tdsg8 x1vvkbs";
        // Extract the href attributes
        List<String> hrefs = elements.stream()
                .map(element -> element.getAttribute("href"))
                .collect(Collectors.toList());
        List<WebElement> usernameElements = new ArrayList<>();
        List<WebElement> captionElements = new ArrayList<>();
        List<WebElement> pictureElements = new ArrayList<>();
        List<WebElement> likesElements = new ArrayList<>();
        int i = 0;
        for (String href : hrefs) {
            System.out.println(i);
            if (href != null && !href.contains("/p/")) {
                System.out.println("Link without '/p/': " + href);
                continue;
            }
            WebDriver innerDriver = new ChromeDriver();
            WebDriverWait innerWait = new WebDriverWait(innerDriver, Duration.ofSeconds(30));
            innerDriver.get(href);
            innerWait.until((ExpectedCondition<Boolean>) d -> {
                WebElement divElement = d.findElement(By.xpath("//div[@id='splash-screen']"));
                return divElement != null && divElement.getAttribute("style").contains("display: none");
            });
            try {
                // Sleep for 5 seconds (5000 milliseconds)
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // Handle the exception
                e.printStackTrace();
            }
            WebElement usernameElement = innerDriver.findElement(By.cssSelector("a." + classNames.replace(" ", ".")));
            WebElement captionElement = innerDriver.findElement(By.cssSelector("h1." + captionClass.replace(" ", ".")));
            WebElement PictureElement = innerDriver.findElement(By.cssSelector("img." + pictureClass.replace(" ", ".")));
            WebElement LikesElement = innerDriver.findElement(By.cssSelector("span." + LikesClass.replace(" ", ".")));
            System.out.println(usernameElement.getText());
            System.out.println(captionElement.getText());
            System.out.println(PictureElement.getAttribute("src"));
            System.out.println(LikesElement.getText());
            usernameElements.add(usernameElement);
            captionElements.add(captionElement);
            pictureElements.add(PictureElement);
            likesElements.add(LikesElement);
            i++;
            innerDriver.quit();

        }
        System.out.println("Usernames:");
        for (WebElement element : usernameElements) {
            System.out.println(element.getText());
        }

        // Print contents of captionElements
        System.out.println("\nCaptions:");
        for (WebElement element : captionElements) {
            System.out.println(element.getText());
        }

        // Print contents of pictureElements
        System.out.println("\nPictures:");
        for (WebElement element : pictureElements) {
            System.out.println(element.getAttribute("src"));
        }

        // Print contents of likesElements
        System.out.println("\nLikes:");
        for (WebElement element : likesElements) {
            System.out.println(element.getText());
        }
        driver.quit();
        */
        return "<html>" +
                "<body>" +
                "</body>" +
                "</html>";
    }
}
