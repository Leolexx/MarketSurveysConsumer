package com.mrk.cons.mm.impl;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mrk.domain.MarketSurvey;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ConsumerRestController {

	/**
	 * Post survey to Consumer 
	 * @param lstSurvey - List of Surveys
	 */
	@RequestMapping(value = "/postSurvey", method = RequestMethod.POST, consumes = "application/json")
 	public void postSurvey(@RequestBody List<MarketSurvey> lstSurvey) {
		log.info("");
 		log.info("Market Survey Consumer: GOT /postSurvey:");
 		log.info("SUBSCRIPTION:");
 		log.info("size={}", lstSurvey.size());
 		
 		lstSurvey.stream().forEach(t-> {
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

	
	
}
