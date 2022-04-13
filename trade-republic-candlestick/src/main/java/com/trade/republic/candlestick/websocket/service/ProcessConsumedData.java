package com.trade.republic.candlestick.websocket.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mongodb.MongoException;
import com.trade.republic.candlestick.client.dataaccessobjects.QuotesEventConsumedData;
import com.trade.republic.candlestick.websocket.repository.InstrumentsDataDao;
import com.trade.republic.candlestick.websocket.repository.QuotesDataDao;

@Service
public class ProcessConsumedData {
	
	private static final Logger logger = (Logger) LogManager.getLogger(ProcessConsumedData.class);
	
	@Autowired
	private InstrumentsDataDao instrumentDao;
	
	@Autowired
	private QuotesDataDao quotesDataDao;
	
	
	public ArrayList<HashMap<Date,String>> proccessQuoteEventData(ArrayList<HashMap<Date,String>> list) throws MongoException,SQLException,Exception{
		
		ArrayList<QuotesEventConsumedData> quotesList = processQuotesJson(list);
		if(null != quotesList && !quotesList.isEmpty()) {
			quotesDataDao.saveQuotesData(quotesList);
		}
		list.clear();
			
		return list;
	}
	
	
	public ArrayList<QuotesEventConsumedData> processQuotesJson(ArrayList<HashMap<Date,String>> list) throws JsonParseException, JsonMappingException, JsonProcessingException{
		ArrayList<QuotesEventConsumedData> quotesEventList = new ArrayList<QuotesEventConsumedData>();
		
		for(HashMap<Date, String> map : list) { 
			for (Map.Entry<Date, String> entry : map.entrySet()) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(entry.getKey());
				
				quotesEventList.add(new QuotesEventConsumedData(cal,entry.getValue()));
			}
		}
		return quotesEventList;
	}
	
	public ArrayList<String> proccessInstrumentData(ArrayList<String> list) throws MongoException,SQLException,Exception{
	
		if(null != instrumentDao) {
			instrumentDao.saveInstrument(list);
		}
		list.clear();
			
		return list;
	}

}
