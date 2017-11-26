package com.mrk.cons.mm.impl;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.mrk.cons.mm.MarketSurveyConsumer;
import com.mrk.domain.MarketSurvey;
import com.mrk.domain.RequestMessage;
import com.mrk.domain.RequestMessage.Provider;
import com.mrk.domain.RequestMessage.Requester;
import com.mrk.domain.RequestMessage.Subscription;
import com.mrk.domain.RequestMessage.Survey;
import com.mrk.domain.RequestMessage.Survey.Target;
import com.mrk.domain.RequestMessage.Survey.Target.Income;
import com.mrk.domain.ResponceAvailSurvey;
import com.mrk.excp.ConnectionError;

import lombok.extern.slf4j.Slf4j;

/**
 * Service for communcations with Surveys Provider
 * @author Leo
 * @version 1.0
 */
@Slf4j
@Service
public class MarketSurveyConsumerImpl implements MarketSurveyConsumer {

	// From application.properties:
	// selftest - 1 - do a self-test 
	@Value("${selftest}")
	private String selftest;
	// current port
	@Value("${server.port}")
	int port;
	// consumer Id 
	@Value("${consumer.id}")
	int consumerId;
	/**
	 * Get all available survey's subjects from Survey Provider
	 * @throws ConnectionError 
	 */
	@Override
 	public void pullAllSurveySubject() throws ConnectionError {
 		RestTemplate restTemplate = new RestTemplate();
        ResponceAvailSurvey[] respArr;
		try {
			respArr = restTemplate.getForObject("http://localhost:8088/getSurvey", 
					ResponceAvailSurvey[].class);
		} catch (RestClientException e) {
			throw new ConnectionError("Can't reach Market Survey Provider (MSP), please, start it!");
		}
        List<ResponceAvailSurvey> resp = Arrays.asList(respArr);
        log.info("Available surveys from provider:");
        resp.forEach(t-> log.info(t.getSubject()));
 		
	}
	
	/**
	 * Get surveys from Survey Provider by parameters
	 * @param req - Request Message with filter criteria
	 * @throws ConnectionError 
	 */
 	@Override
 	public void pullSurvey(RequestMessage req) throws ConnectionError {
 		log.info("");
 		
 		RestTemplate restTemplate = new RestTemplate();
 		String url = "http://localhost:8088/getSurveyByReq";
 		log.info("Market Survey Consumer: SEND {}", url);
 		HttpEntity<RequestMessage> requestEntity = new HttpEntity<RequestMessage>(req);
 		ResponseEntity<List<MarketSurvey>> resp;
		try {
	 		// get surveys from endpoint
	 		resp = restTemplate.exchange(url, HttpMethod.POST, requestEntity, 
	 						new ParameterizedTypeReference<List<MarketSurvey>>() {});
		} catch (RestClientException e) {
			throw new ConnectionError("Can't reach Market Survey Provider (MSP), please, start it!");
		}
 		
 		log.info("");
 		log.info("Market Survey Consumer:");
        log.info("GOT Surveys from provider:");
 		resp.getBody().forEach(t-> {
 	 		log.info("----------------------------------");
 	 		log.info("Survey:");
 	 		log.info("Subject={}", t.getSubject());
 	 		log.info("Id={}", t.getId());
 	 		log.info("Age={}", t.getAge());
 	 		log.info("Gender={}", t.getGender());
 	 		log.info("Income={}", t.getIncome());
 	 		log.info("Currency={}", t.getCurrency());
 	 		log.info("County={}", t.getCountry());
 	 		log.info("Any other data={}", t.getAnyData());
 	 		log.info("----------------------------------");
 		});
 		
	}

 	/**
 	 * Request Message builder
 	 * @param subject - Survey's subject
 	 * @param gender - Gender
 	 * @param country - Country
 	 * @param currency - Currency of income
 	 * @param ageFrom - From age   
 	 * @param ageTo - To age
 	 * @param incomeFrom - From income
 	 * @param incomeTo - To income
 	 * @param channel - Way of delivery
 	 * @param frequency - Subscribing frequency
 	 * @param isClearPrev - Clear previuos subscriptions  
 	 * @return RequestMessage
 	 */
	public RequestMessage buildMessage(String subject, String gender, 
			String currency, String country, Integer ageFrom, Integer ageTo, 
			Integer incomeFrom, Integer incomeTo, List<String> channel, String frequency,
			boolean isClearPrev) {
		RequestMessage req = new RequestMessage();
 		
 		// Requester information
		Requester requester = new Requester();
		// Requseter (Consumer) Id
		requester.setId(String.valueOf(consumerId));
		// Name
		requester.setName("Requester-1");
		req.setRequester(requester);
		if (channel !=null ) {
			Subscription subscription = new Subscription();
			subscription.setChannel(channel);
			subscription.setFrequency(frequency);
			// set current port
			subscription.setEndPoint("http://localhost:"+String.valueOf(port));
			subscription.setIsClearPrev(isClearPrev);
			req.setSubscription(subscription );
		}
 		
 		// Provider information
		Provider provider = new Provider();
		provider.setId("1");
		provider.setName("Provider-2");
		req.setProvider(provider);

		// Subject
		Survey survey = new Survey();
 		survey.setSubject(subject);

 		// Country
 		survey.setCountry(country);
 		
 		// Target
 		Target target = new Target();
 		List<Integer> age = new ArrayList<Integer>();
 		age.add(ageFrom);
 		age.add(ageTo);
		target.setAge(age );
		target.setGender(gender);
		
		Income income = new Income();
		income.setCurrency(currency);
		
		List<Integer> range = new ArrayList<Integer>();
		range.add(incomeFrom);
		range.add(incomeTo);
		income.setRange(range );
		target.setIncome(income );
		
		survey.setTarget(target);
		req.setSurvey(survey );
		return req;
	}

	/**
	 * Self - test
	 */
	@Override
	public void selfTest() throws ConnectionError {
		// check application.properties selftest
		if (selftest!=null && selftest.equals("1")) {
			log.info("Start self-test={}", selftest);
			// Consumer:
			// 
			log.info("--- Pull a list of Survey's subjects from provider---");
			pullAllSurveySubject();
			
			// Build a Request message
			// Subject, gender, country, currency, ageFrom, ageTo, incomeFrom, incomeTo, channel, frequency, 
			RequestMessage req = buildMessage("81111601", "F", "RUB", "RU", 21, 29, 34000, 40000, null, null, false);
			// Pull Surveys according request message
			pullSurvey(req);
			
			log.info("----------------- Subscribing --------------------");
			List<String> channel = new ArrayList<String>();
			channel.add("mail");
			channel.add("rest");
			channel.add("ftp");
			
	 		req = buildMessage("81111601", "F", "USD", "US", 21, 29, 34000, 60000, channel, "minute", false);
			pullSurvey(req);

			req = buildMessage("81111608", "M", "RUB", "RU", 21, 55, 30000, 140000, channel, "minute", false);
			pullSurvey(req);

			log.info("-------- Waiting for Provider's activity ---------");
	 		try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			log.info("--- Request new subscription, clear all previous---");
	 		req = buildMessage("81111607", "M", "USD", "US", 21, 50, 36000, 60000, channel, "minute", true);
			pullSurvey(req);

			log.info("-------- Waiting for Provider's activity ---------");
	 		try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	 		// End of test
			log.info("End self-test");
		}
	}

}
