package com.mrk.cons.mm;

import java.util.List;

import com.mrk.domain.RequestMessage;
import com.mrk.excp.ConnectionError;

public interface MarketSurveyConsumer {
	
	public void pullSurvey(RequestMessage req) throws ConnectionError ;
	public void pullAllSurveySubject() throws ConnectionError ;
	public RequestMessage buildMessage(String subject, String gender, 
			String currency, String country, Integer ageFrom, Integer ageTo, 
			Integer incomeFrom, Integer incomeTo, List<String> channel, String frequency, boolean isClearPrev);
	public void selfTest() throws ConnectionError;
}
