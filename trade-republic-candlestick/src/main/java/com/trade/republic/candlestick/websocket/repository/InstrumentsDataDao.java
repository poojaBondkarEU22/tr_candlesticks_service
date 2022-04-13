package com.trade.republic.candlestick.websocket.repository;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoException;

@Repository
public class InstrumentsDataDao {
	
	private static final Logger logger = (Logger) LogManager.getLogger(InstrumentsDataDao.class);
	
	@Autowired
	private MongoTemplate mongoTemplate;

	public void saveInstrument(List<String> instrumentList) throws MongoException,SQLException,Exception{
		mongoTemplate.insert(instrumentList, "tr_m_instruments_event");
	}
}
