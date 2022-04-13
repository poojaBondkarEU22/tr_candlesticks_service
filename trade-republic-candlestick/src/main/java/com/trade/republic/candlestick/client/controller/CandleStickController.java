package com.trade.republic.candlestick.client.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trade.republic.candlestick.client.datatransferobject.CandleSticksRepsonseDTO;
import com.trade.republic.candlestick.client.datatransferobject.Candlestick;
import com.trade.republic.candlestick.client.service.CandleStickQuoteService;

@RestController
@RequestMapping("/candlestick")
public class CandleStickController {
	
	private static final Logger logger = (Logger) LogManager.getLogger(CandleStickController.class);
	
	@Autowired
	private CandleStickQuoteService candleStickQuoteService;
	
	@GetMapping
	public ResponseEntity<CandleSticksRepsonseDTO>  getInstrumentsCandleStick(@RequestParam("isin") List<String> isinList) throws Exception 
					 {
		
		logger.info("Enter getInstrumentsCandleStick : "+ isinList.toString());
		CandleSticksRepsonseDTO candleSticksRepsonseDTO = null;
		
		Calendar cal = Calendar.getInstance();
		Map<String,List<Candlestick>> candleSticks = candleStickQuoteService.getCandleSticksForInstruments(isinList,cal);
		candleSticksRepsonseDTO = new CandleSticksRepsonseDTO(candleSticks);
		
		
		return new ResponseEntity<CandleSticksRepsonseDTO>(candleSticksRepsonseDTO,HttpStatus.OK);
	}

}
