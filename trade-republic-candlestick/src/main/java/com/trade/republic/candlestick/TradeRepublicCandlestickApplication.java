package com.trade.republic.candlestick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.trade.republic.candlestick.websocket.ConsumeStreamData;
import com.trade.republic.candlestick.websocket.service.ProcessConsumedData;

@SpringBootApplication
@ComponentScan("com.trade.republic.candlestick")
public class TradeRepublicCandlestickApplication {
	
	public static void main(String[] args) {
		
		ApplicationContext context = SpringApplication.run(TradeRepublicCandlestickApplication.class, args);
		ConsumeStreamData consumeStreamData = context.getBean(ConsumeStreamData.class);
		ProcessConsumedData processConsumedData = context.getBean(ProcessConsumedData.class);
		consumeStreamData.consumeInstrumentData(processConsumedData);
		consumeStreamData.consumeQuoteData(processConsumedData);
	}

}
