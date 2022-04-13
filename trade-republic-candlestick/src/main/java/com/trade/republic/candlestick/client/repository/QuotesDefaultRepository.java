package com.trade.republic.candlestick.client.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trade.republic.candlestick.client.dataaccessobjects.Quote;

public interface QuotesDefaultRepository extends MongoRepository<Quote, Long>{

}
