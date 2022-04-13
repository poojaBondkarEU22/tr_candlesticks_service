package com.trade.republic.candlestick.client.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trade.republic.candlestick.client.dataaccessobjects.InstrumentEvent;

public interface InstrumentEventRepository  extends MongoRepository<InstrumentEvent, Long> {

}