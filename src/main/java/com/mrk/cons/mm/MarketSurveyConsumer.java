package com.mrk.cons.mm;

import java.util.List;

import com.mrk.domain.RequestMessage;
import com.mrk.excp.ConnectionError;

/**
 * Consumer interface
 * @author Leo
 * @version 1.0
 */
public interface MarketSurveyConsumer {
	
	/**
	 * Get all available survey's subjects from Survey Provider
	 * @throws ConnectionError 
	 */
	public void pullAllSurveySubject() throws ConnectionError ;
	/**
	 * Get surveys from Survey Provider by parameters
	 * @param req - Request Message with filter criteria
	 * @throws ConnectionError 
	 */
	public void pullSurvey(RequestMessage req) throws ConnectionError ;
	

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
			Integer incomeFrom, Integer incomeTo, List<String> channel, String frequency, boolean isClearPrev);
	
	/**
	 * Self - test
	 */
	public void selfTest() throws ConnectionError;
}
