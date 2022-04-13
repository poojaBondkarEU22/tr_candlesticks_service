package com.trade.republic.candlestick.websocket;

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
public class InstrumentEventClientEndPoint {
	
	private static final Logger logger = (Logger) LogManager.getLogger(InstrumentEventClientEndPoint.class);
	Session userSession = null;
    
	BlockingQueue<String> instrumentsDataQueue = null;
	
	
	public InstrumentEventClientEndPoint(BlockingQueue<String> instrumentsDataQueue) {
		this.instrumentsDataQueue = instrumentsDataQueue;
	}
    
	@OnOpen
	public void onOpen(Session session, EndpointConfig endpointConfig) {
		logger.info("Web socket opened - Instrument Data");
	}
	
	@OnMessage
    public void onMessage(String message) {
		try {
			instrumentsDataQueue.put(message);
		} catch (Exception e) {
			throw new ApplicationException("Exception in instrument even client "+ e.getMessage());
		}
    }
	
	@OnClose
    public void onClose(Session userSession, CloseReason reason) {
		logger.info("closing websocket connection");
        this.userSession = null;
    }

}
