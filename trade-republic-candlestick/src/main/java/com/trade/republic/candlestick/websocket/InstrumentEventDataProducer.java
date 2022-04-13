package com.trade.republic.candlestick.websocket;

import java.net.URI;
import java.util.concurrent.BlockingQueue;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.trade.republic.candlestick.client.constants.ApplicationConstants;
import com.trade.republic.candlestick.client.exception.ApplicationException;

public class InstrumentEventDataProducer extends Thread{
	
	private static final Logger logger = (Logger) LogManager.getLogger(InstrumentEventDataProducer.class);
	
	BlockingQueue<String> instrumentsDataQueue = null;

	public InstrumentEventDataProducer(BlockingQueue<String> instrumentsDataQueue) {
		super();
		this.instrumentsDataQueue = instrumentsDataQueue;
	}
	
	@Override
	public void run() {
		try {
	        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
	        logger.info("Connecting to:: " + ApplicationConstants.INSTRUMENT_EVENT_URL);
	            
	        InstrumentEventClientEndPoint instrumentEndPoints = new InstrumentEventClientEndPoint(instrumentsDataQueue);
	        container.connectToServer(instrumentEndPoints, URI.create(ApplicationConstants.INSTRUMENT_EVENT_URL));       
	                
	        } catch (Exception ex) {
	            throw new ApplicationException("Exception in connection with service : "+ ex.getMessage());
	        }
		
	}

}
