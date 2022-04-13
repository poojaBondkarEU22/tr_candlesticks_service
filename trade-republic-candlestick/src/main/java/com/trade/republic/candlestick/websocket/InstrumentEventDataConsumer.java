package com.trade.republic.candlestick.websocket;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.MongoException;
import com.trade.republic.candlestick.client.exception.ApplicationException;
import com.trade.republic.candlestick.websocket.service.ProcessConsumedData;

public class InstrumentEventDataConsumer extends Thread{
	
	private static final Logger logger = (Logger) LogManager.getLogger(InstrumentEventDataConsumer.class);
	
	BlockingQueue<String> instrumentsDataQueue = null;
	ArrayList<String> instrumentData = new ArrayList<String>();
	
	@Autowired
	private ProcessConsumedData processConsumedData;

	public InstrumentEventDataConsumer(BlockingQueue<String> instrumentsDataQueue,ProcessConsumedData processConsumedData) {
		super();
		this.instrumentsDataQueue = instrumentsDataQueue;
		this.processConsumedData = processConsumedData;
	}
	
	@Override
	public void run() {
		logger.info("Consuming instruments....");
		try {
			while(true) {
				instrumentData.add(instrumentsDataQueue.take());
				if(instrumentData.size() == 50) {
					Thread.sleep(1000);
					instrumentData = processConsumedData.proccessInstrumentData(instrumentData);
				}
			}
		}catch (InterruptedException e) {
            throw new ApplicationException("Exception in consuming instruments : "+ e.getMessage());
        } catch (MongoException e) {
        	throw new ApplicationException("Exception in consuming instruments : "+ e.getMessage());
		} catch (SQLException e) {
			throw new ApplicationException("Exception in consuming instruments : "+ e.getMessage());
		} catch (Exception e) {
			throw new ApplicationException("Exception in consuming instruments : "+ e.getMessage());
		}
		
	}


}
