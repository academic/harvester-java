package com.article.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories (basePackages = {"com.article.repository"})
@ComponentScan(basePackages = {"com.article"})
public class CrawlerApplication {

	public static void main(String[] args) throws Exception{
		SpringApplication.run(CrawlerApplication.class, args);
	}



}
