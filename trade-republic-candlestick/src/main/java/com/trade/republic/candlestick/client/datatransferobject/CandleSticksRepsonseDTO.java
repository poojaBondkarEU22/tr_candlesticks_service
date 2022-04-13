package com.trade.republic.candlestick.client.datatransferobject;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class CandleSticksRepsonseDTO {
	
	Map<String,List<Candlestick>> candleSticks;
	
	public CandleSticksRepsonseDTO() {
		
	}

	public CandleSticksRepsonseDTO(Map<String, List<Candlestick>> candleSticks) {
		super();
		this.candleSticks = candleSticks;
	}

	public Map<String, List<Candlestick>> getIsinCandleStickMap() {
		return candleSticks;
	}

	public void setIsinCandleStickMap(Map<String, List<Candlestick>> candleSticks) {
		this.candleSticks = candleSticks;
	}
	
	

}
