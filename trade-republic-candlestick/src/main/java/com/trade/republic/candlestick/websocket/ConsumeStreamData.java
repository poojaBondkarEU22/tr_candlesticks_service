package com.trade.republic.candlestick.websocket;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.stereotype.Component;

import com.trade.republic.candlestick.websocket.service.ProcessConsumedData;

@Component
public class ConsumeStreamData {
	
	private static final Logger logger = (Logger) LogManager.getLogger(ConsumeStreamData.class);
	
	BlockingQueue<Map<Date,String>> quotesDataQueue = new ArrayBlockingQueue<Map<Date,String>>(5000);
	
	//BlockingQueue<String> quotesDataQueue = new ArrayBlockingQueue<String>(5000);
	BlockingQueue<String> instrumentsDataQueue = new ArrayBlockingQueue<String>(5000);
	

	public void consumeQuoteData(ProcessConsumedData processConsumedData) {
		logger.info("Consuming quote data - Start");
		QuotesDataProducer quoteProducer = new QuotesDataProducer(quotesDataQueue);
		QuotesDataConsumer quoteConsumer = new QuotesDataConsumer(quotesDataQueue,processConsumedData);
		
		quoteProducer.start();
		quoteConsumer.start();
	}
	
	
	public void consumeInstrumentData(ProcessConsumedData processConsumedData) {
		
		logger.info("Consuming instrument data - Start");
		
		InstrumentEventDataProducer instrumentProducer = new InstrumentEventDataProducer(instrumentsDataQueue);
		InstrumentEventDataConsumer instrumentConsumer = new InstrumentEventDataConsumer(instrumentsDataQueue,processConsumedData);
		
		instrumentProducer.start();
		instrumentConsumer.start();
	}
	
	

}
