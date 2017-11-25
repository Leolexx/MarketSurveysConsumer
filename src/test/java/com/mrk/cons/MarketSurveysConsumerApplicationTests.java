package com.mrk.cons;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.mrk.cons.mm.MarketSurveyConsumer;
import com.mrk.cons.mm.impl.ConsumerRestController;
import com.mrk.domain.RequestMessage;
import com.mrk.excp.ConnectionError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8089"})
public class MarketSurveysConsumerApplicationTests {

	@Autowired 
	MarketSurveyConsumer consumer;
	
	@Test
	public void contextLoads() throws ConnectionError {
		
		// Consumer:
		// Pull a list of Survey's subjects from provider
		consumer.pullAllSurveySubject();
		
		// Build a Request message
		// Subject, gender, country, currency, ageFrom, ageTo, incomeFrom, incomeTo, channel, frequency, 
		RequestMessage req = consumer.buildMessage("81111601", "M", "RUB", "RU", 21, 29, 34000, 40000, null, null, false);
		// Pull Surveys according request message
		consumer.pullSurvey(req);
		
		// Subscribing
		List<String> channel = new ArrayList<String>();
		channel.add("mail");
		channel.add("rest");
		channel.add("ftp");
		
 		req = consumer.buildMessage("81111601", "F", "USD", "US", 21, 29, 34000, 60000, channel, "minute", false); //TODO (minute)
		consumer.pullSurvey(req);

		req = consumer.buildMessage("81111608", "M", "RUB", "RU", 21, 55, 30000, 140000, channel, "minute", false);
		consumer.pullSurvey(req);

		// Wait for Provider show it's activity
 		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

 		// Request new subscription, clear all previous
 		req = consumer.buildMessage("81111607", "M", "USD", "US", 21, 50, 36000, 60000, channel, "minute", true);
		consumer.pullSurvey(req);

		// Wait for Provider show it's activity
 		try {
			Thread.sleep(160000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
 		// End of test
	}

}
