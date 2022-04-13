package com.trade.republic.candlestick.websocket.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoException;
import com.trade.republic.candlestick.client.dataaccessobjects.QuotesEventConsumedData;

@Repository
public class QuotesDataDao {
	
	private static final Logger logger = (Logger) LogManager.getLogger(QuotesDataDao.class);
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	public void saveQuotesData(ArrayList<QuotesEventConsumedData> quotesList) throws MongoException,SQLException,Exception{
		
		//logger.info("Saving quotes data into db - start");
		mongoTemplate.insert(quotesList, "tr_t_instruemts_quote_event");
		//logger.info("Saving quotes data into db - End");
	}
	
	
	

}
