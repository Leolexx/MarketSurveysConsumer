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
	 * Get survey from Provider 
	 * @param lstSurvey - List of Surveys
	 * @return - Survey data
	 */
	@RequestMapping(value = "/postSurvey", method = RequestMethod.POST, consumes = "application/json")
 	public String postSurvey(@RequestBody List<MarketSurvey> lstSurvey) {
		log.info("");
 		log.info("Market Survey Consumer: GOT /postSurvey:");
 		log.info("SUBSCRIPTION:");
 		log.info("size={}", lstSurvey.size());
 		
 		lstSurvey.stream().forEach(t-> {
 	 		log.info("Survey.Id={}", t.getId());
 		});
 		
 		return "OK";
 	}

	
	
}
