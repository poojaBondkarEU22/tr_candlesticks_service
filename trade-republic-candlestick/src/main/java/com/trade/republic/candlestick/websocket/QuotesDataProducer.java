package com.trade.republic.candlestick.websocket;

import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.trade.republic.candlestick.client.constants.ApplicationConstants;
import com.trade.republic.candlestick.client.exception.ApplicationException;

public class QuotesDataProducer extends Thread{

	private static final Logger logger = (Logger) LogManager.getLogger(QuotesDataProducer.class);
	
	BlockingQueue<Map<Date,String>> quotesDataQueue = null;
	
	public QuotesDataProducer(BlockingQueue<Map<Date,String>> quotesDataQueue) {
		super();
		this.quotesDataQueue = quotesDataQueue;
	}


	@Override
	public void run() {
		try {
	        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
	        logger.info("Connecting to:: " + ApplicationConstants.QUOTE_EVENT_URL);
	            
	        QuoteEventClientEndPoint quoteEventClientEndPoint = new QuoteEventClientEndPoint(quotesDataQueue);
	        container.connectToServer(quoteEventClientEndPoint, URI.create(ApplicationConstants.QUOTE_EVENT_URL));       
	                
	        } catch (Exception ex) {
	            throw new ApplicationException("Exception in connection with quote data provider service : "+ ex.getMessage());
	        }
		
	}

}
