package com.trade.republic.candlestick.websocket;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.MongoException;
import com.trade.republic.candlestick.client.exception.ApplicationException;
import com.trade.republic.candlestick.websocket.service.ProcessConsumedData;

public class QuotesDataConsumer extends Thread{
	
	private static final Logger logger = (Logger) LogManager.getLogger(QuotesDataConsumer.class);

	@Autowired
	private ProcessConsumedData processConsumedData;
	

	BlockingQueue<Map<Date,String>> quotesDataQueue = null;
	ArrayList<HashMap<Date,String>> quotesData = new ArrayList<HashMap<Date,String>>();
	
	public QuotesDataConsumer(BlockingQueue<Map<Date,String>> quotesDataQueue, ProcessConsumedData processConsumedData) {
		super();
		this.quotesDataQueue = quotesDataQueue;
		this.processConsumedData = processConsumedData;
		
	}

	@Override
	public void run() {
		logger.info("Consuming quotes.......");
		try {
			while(true) {
				Map<Date,String> map = new HashMap<Date, String>();
				map = quotesDataQueue.take();
				quotesData.add((HashMap<Date, String>) map);
				if(quotesData.size() == 500) {
					Thread.sleep(1000);
					quotesData = processConsumedData.proccessQuoteEventData(quotesData);
				}
			}
			
		}catch (InterruptedException e) {
           throw new ApplicationException("Exception in conusing quotes : "+ e.getMessage());
        } catch (MongoException e) {
        	 throw new ApplicationException("Exception in conusing quotes : "+ e.getMessage());
		} catch (SQLException e) {
			 throw new ApplicationException("Exception in conusing quotes : "+ e.getMessage());
		} catch (Exception e) {
			 throw new ApplicationException("Exception in conusing quotes : "+ e.getMessage());
		}
		
	}

}
