package com.trade.republic.candlestick.client.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trade.republic.candlestick.client.dataaccessobjects.QuotesEventConsumedData;

public interface InstrumentQuotesDefaultRepository extends MongoRepository<QuotesEventConsumedData, Long>{
	
	

}
