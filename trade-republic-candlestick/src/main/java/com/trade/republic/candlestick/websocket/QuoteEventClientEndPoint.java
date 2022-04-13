package com.trade.republic.candlestick.websocket;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.trade.republic.candlestick.client.exception.ApplicationException;

@ClientEndpoint
public class QuoteEventClientEndPoint {
	
	private static final Logger logger = (Logger) LogManager.getLogger(QuoteEventClientEndPoint.class);
	
	Session userSession = null;
    
	BlockingQueue<Map<Date,String>> quotesDataQueue = null;
	
	
	HashMap<Date,String> map = null;
	
	public QuoteEventClientEndPoint(BlockingQueue<Map<Date,String>> quotesDataQueue) {
		this.quotesDataQueue = quotesDataQueue;
	}
    
	@OnOpen
	public void onOpen(Session session, EndpointConfig endpointConfig) {
		logger.info("Web socket opened");
	}
	
	@OnMessage
    public void onMessage(String message) {
		try {
			map = new HashMap<Date, String>();
			map.put(Calendar.getInstance().getTime(), message);
			quotesDataQueue.put(map);
		} catch (Exception e) {
			throw new ApplicationException("Exception in onMessage of quotes : "+ e.getMessage());
		}
    }
	
	@OnClose
    public void onClose(Session userSession, CloseReason reason) {
		logger.info("closing websocket connection");
        this.userSession = null;
    }

	
}
