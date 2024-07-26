package com.example.SentimentAnalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Import({ AdapterConf.class, AdapterRestApiConf.class, ApplicationConf.class })
public class SentimentAnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SentimentAnalysisApplication.class, args);
	}

}
