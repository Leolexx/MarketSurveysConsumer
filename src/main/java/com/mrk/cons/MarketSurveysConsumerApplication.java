package com.mrk.cons;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.mrk.cons.mm.MarketSurveyConsumer;
import com.mrk.excp.ConnectionError;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring Boot Application - Market Survey Consumer
 * @author Leo
 * @version 1.2
 *
 */
@Slf4j
@SpringBootApplication
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class MarketSurveysConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketSurveysConsumerApplication.class, args);
	}
	
	
	
	/**
	 * Command Line runner for initialization purposes
	 * @param ctx - app.context
	 * @return
	 */
	@Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
        	log.info("Starting Market Survey Consumer...");
        	MarketSurveyConsumer msc = ctx.getBean(MarketSurveyConsumer.class);
        	// Consumer self-test
        	try {
				msc.selfTest();
			} catch (ConnectionError e) {
				log.error("");
				log.error("");
				log.error("ERROR: Can't connect to Market Survey Provider!");
				log.error("");
				log.error("");
				SpringApplication.exit(ctx, () -> 1);
			}
        };
    }
}
