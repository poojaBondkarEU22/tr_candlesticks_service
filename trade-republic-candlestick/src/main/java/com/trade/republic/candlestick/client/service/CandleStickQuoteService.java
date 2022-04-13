package com.trade.republic.candlestick.client.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trade.republic.candlestick.client.constants.ApplicationConstants;
import com.trade.republic.candlestick.client.dataaccessobjects.InstrumentEvent;
import com.trade.republic.candlestick.client.dataaccessobjects.QuoteEvent;
import com.trade.republic.candlestick.client.dataaccessobjects.QuotesEventConsumedData;
import com.trade.republic.candlestick.client.datatransferobject.Candlestick;
import com.trade.republic.candlestick.client.exception.InvalidInstrumentsException;
import com.trade.republic.candlestick.client.exception.NoQuotesForInstrumentsException;
import com.trade.republic.candlestick.client.repository.InstrumentEventRepository;
import com.trade.republic.candlestick.client.repository.InstrumentQuotesDefaultRepository;

@Service
public class CandleStickQuoteService {
	
	private static final Logger logger = (Logger) LogManager.getLogger(CandleStickQuoteService.class);

	@Autowired
	private InstrumentEventRepository instrumentEventRepository;
	
	@Autowired
	private InstrumentQuotesDefaultRepository instrumentQuotesDefaultRepository;
	
	@Autowired
	private ObjectMapper mapper;
	
	public Map<String,List<Candlestick>> getCandleSticksForInstruments(List<String> isinList,Calendar quoteRequestTimeStamp) 
										throws Exception {
		
		logger.info("Enter getCandleSticksForInstruments for instruments : "+ isinList.size());
		
		Map<String,List<Candlestick>> isinCandleStickMap = null;
		Map<String ,List<List<Map<String,BigDecimal>>>>  perMinQuotesList = null;
		Map<String,List<QuotesEventConsumedData>> isinQuotesMapData = null;
		
		Date captureEndTime =quoteRequestTimeStamp.getTime();
		quoteRequestTimeStamp.add(Calendar.MINUTE, -ApplicationConstants.QUOTES_BUFFER_TIME);
		Date  captureStartTime  = quoteRequestTimeStamp.getTime();
		
		logger.info("captureStartTime : "+ captureStartTime + " , captureEndTime : "+ captureEndTime);
		
		List<String> validIsinList = new ArrayList<String>(isinList);
		
	
		if(validInputIsinList(isinList)) {
			
			isinQuotesMapData =findQuotesByIsinAndTimestamp(validIsinList,captureStartTime,captureEndTime);
			
			if(null != isinQuotesMapData && !isinQuotesMapData.isEmpty()) {
				
				for(Map.Entry<String, List<QuotesEventConsumedData>> map : isinQuotesMapData.entrySet()) {
					logger.info("quotesList : " + map.getValue().size() + " for isin "+ map.getKey());
				//	logger.info("quotesList Elements : "+ map.getValue().toString());
				}
				
				perMinQuotesList = collectPerMinQuotesForIsin(isinQuotesMapData);
				
				if(null != perMinQuotesList && !perMinQuotesList.isEmpty()) {
					isinCandleStickMap = getCandleStick(perMinQuotesList);
				}
			}else {
				logger.info("No quotes found for instruments.Please re-try after sometime");
				throw new NoQuotesForInstrumentsException("No quotes found for instruments.Please re-try after sometime.");
			}
			
		}
		return isinCandleStickMap;
	}
	
	
	public List<InstrumentEvent> getAllInstruments() {
		return instrumentEventRepository.findAll();
		
	}
	public boolean validInputIsinList(List<String> isinList) throws Exception {
		
		Set<String> existingInstruments = getAllInstruments().stream()
															 .map(instrumentEvent -> instrumentEvent.getData().getIsin())
															 .collect(Collectors.toSet());
		
		if(existingInstruments.containsAll(isinList)== false) {
			isinList.removeAll(existingInstruments);
			
			String invalidIsin = String.join(", ", isinList);
			throw new InvalidInstrumentsException("Invalid instruments "+ invalidIsin);
		}
		
		return true;
	}
	
	
	public Map<String,List<QuotesEventConsumedData>> findQuotesByIsinAndTimestamp(List<String> isinList,Date captureStartTime , Date captureEndTime) 
																						throws JsonMappingException, JsonProcessingException, ParseException {
		
		logger.info("Enter findQuotesByIsinAndTimestamp for isin "+ isinList.toString());
		
		List<QuotesEventConsumedData> quoteEventList = instrumentQuotesDefaultRepository.findAll();
		
		logger.info("All captured quotes : "+ quoteEventList.size());
		
		Map<String,List<QuotesEventConsumedData>> isinQuotesMapData = new LinkedHashMap<String, List<QuotesEventConsumedData>>();
		
		List<QuotesEventConsumedData> quotesList =null;
		
		for(String isin : isinList) {
			logger.info("collecting quotes for isin : "+ isin);
			quotesList = quoteEventList.stream()
			  .filter(e -> e.getData().contains(isin)
					  	&& (e.getFetchedTimeStamp().getTime().equals(captureStartTime)  || e.getFetchedTimeStamp().getTime().after(captureStartTime) )
					  	&& e.getFetchedTimeStamp().getTime().before(captureEndTime))
			  .collect(Collectors.toList());
			
			if(null != quotesList && !quotesList.isEmpty()) {
				isinQuotesMapData.put(isin, quotesList);
			}else {
				logger.info("Capturing older quotes ");
				Calendar newCaptureEndTime = Calendar.getInstance();
				newCaptureEndTime.setTime(captureStartTime);  ;
				Calendar newCaptureStartTime = Calendar.getInstance();
				newCaptureStartTime.setTime(captureStartTime);
				newCaptureStartTime.add(Calendar.MINUTE, -ApplicationConstants.QUOTES_BUFFER_TIME);
				
				logger.info("newCaptureStartTime : "+ newCaptureStartTime.getTime().toString()
							+ ":: newCaptureEndTime : "+ newCaptureEndTime.getTime().toString());
				
				quotesList = quoteEventList.stream()
						  .filter(e -> e.getData().contains(isin)
								  	&& (e.getFetchedTimeStamp().equals(newCaptureStartTime)  || e.getFetchedTimeStamp().after(newCaptureStartTime) )
								  	&& e.getFetchedTimeStamp().before(newCaptureEndTime))
						  .collect(Collectors.toList());
				
				if(null != quotesList && !quotesList.isEmpty()) 
						isinQuotesMapData.put(isin, quotesList);
				
			}
			
		}

		return isinQuotesMapData;
	}
	
	
	public Map<String,List<Candlestick>> getCandleStick(Map<String ,List<List<Map<String,BigDecimal>>>> perMinQuotesList)  {
		logger.info("Enter candlestick calculation");
		
		List<Candlestick> candleStickList = null;
		Map<String,List<Candlestick>> isinCandleStickMap = new LinkedHashMap<String, List<Candlestick>>();
		
		BigDecimal highPricePerMinuteForIsin= new BigDecimal(0);
		BigDecimal lowPricePerMinuteForIsin= new BigDecimal(0);
		Candlestick candlestick = null;
		
		if(null != perMinQuotesList && !perMinQuotesList.isEmpty()) {
			logger.info("available data : "+ perMinQuotesList.size());
			
			for(Map.Entry<String, List<List<Map<String, BigDecimal>>>> eachIsinQuotesData : perMinQuotesList.entrySet()) {
				logger.info("fetching quotes for isin : "+ eachIsinQuotesData.getKey() + " available data : "+ eachIsinQuotesData.getValue().size());
				candleStickList = new ArrayList<Candlestick>();
				for( List<Map<String, BigDecimal>> list : eachIsinQuotesData.getValue()) {
					
					if(null != list && !list.isEmpty()) {
						
						//logger.info("list : "+list.size());		
						
						if(list.size() == 1) {
							Map.Entry<String,BigDecimal> mapData = (Entry<String, BigDecimal>) list.get(0).entrySet().iterator().next(); 
							
							candlestick = new Candlestick(mapData.getKey(),mapData.getKey(),
													mapData.getValue(),mapData.getValue(),mapData.getValue(),mapData.getValue());
							
							candleStickList.add(candlestick);
						}else {
							highPricePerMinuteForIsin = list.stream()                               
									    	  				.flatMap(c -> c.entrySet().stream()) 
									    	  				.max(Comparator.comparing(Map.Entry::getValue)).get().getValue();
							
							lowPricePerMinuteForIsin = list.stream()                               
							    	  						  .flatMap(c -> c.entrySet().stream()) 
							    	  						  .min(Comparator.comparing(Map.Entry::getValue)).get().getValue();
							
							logger.info("highPricePerMinuteForIsin : "+ highPricePerMinuteForIsin + " :: lowPricePerMinuteForIsin : "+ lowPricePerMinuteForIsin);
							logger.info("Open timestamp data  : "+list.get(0));
							logger.info("Close timestamp data : "+list.get(list.size()-1));
							
							Map.Entry<String,BigDecimal> openTimeStampData =  (Entry<String, BigDecimal>) list.get(0).entrySet().iterator().next();
							Map.Entry<String,BigDecimal> closeTimeStampData =  (Entry<String, BigDecimal>) list.get(list.size()-1).entrySet().iterator().next();
							
							
							candlestick = new Candlestick(openTimeStampData.getKey() ,closeTimeStampData.getKey(),
														openTimeStampData.getValue(),highPricePerMinuteForIsin,
														lowPricePerMinuteForIsin,closeTimeStampData.getValue());
							candleStickList.add(candlestick);
							
						}	
					}
				}
				isinCandleStickMap.put(eachIsinQuotesData.getKey(), candleStickList);
			}
		}
		/*for(Map.Entry<String, List<Candlestick>> isinCandleSticks : isinCandleStickMap.entrySet()) {
			logger.info ( isinCandleSticks.getKey() + " :: " + isinCandleSticks.getValue().toString());
		}*/
		
		return isinCandleStickMap;
	}
	
	public Map<String ,List<List<Map<String,BigDecimal>>>> collectPerMinQuotesForIsin(Map<String,List<QuotesEventConsumedData>> isinQuotesMapData) 
																		throws JsonMappingException, JsonProcessingException {
		
	
		Map<String ,List<List<Map<String,BigDecimal>>>> isinQuotesDataMap = new HashMap<String ,List<List<Map<String,BigDecimal>>>>();
		Map<String,BigDecimal> testMap = null;
		ArrayList<Map<String,BigDecimal>> list = null;
		List<List<Map<String,BigDecimal>>> finalList = null;
	
		Calendar startTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();	
		SimpleDateFormat format = new SimpleDateFormat(ApplicationConstants.DATE_FORMAT);
		
		
		for(Map.Entry<String, List<QuotesEventConsumedData>> map : isinQuotesMapData.entrySet()) {
			logger.info("Collecting quotes for isin : "+ map.getKey() +" : "+ map.getValue().size());
			finalList = new ArrayList<List<Map<String,BigDecimal>>>();
			List<QuotesEventConsumedData> lst = map.getValue();
			if(null != lst && !lst.isEmpty()) {
				
				startTime.setTime(lst.get(0).getFetchedTimeStamp().getTime());
				endTime.setTime(lst.get(0).getFetchedTimeStamp().getTime());
				endTime.set(Calendar.SECOND, 0);	
				
				Calendar capEndTime = Calendar.getInstance();
				capEndTime.setTime(	lst.get(lst.size()-1).getFetchedTimeStamp().getTime());
			//	logger.info("capEndTime : "+ capEndTime.getTime().toString());
				capEndTime.add(Calendar.MINUTE, 1 );
				capEndTime.set(Calendar.SECOND, 0);
				//logger.info("capEndTime : "+ capEndTime.getTime().toString());
				
				Calendar capStartTime = Calendar.getInstance();
				capStartTime.setTime(lst.get(0).getFetchedTimeStamp().getTime());
				
				
				while(capStartTime.before(capEndTime )) {
					
					
					list = new ArrayList<Map<String,BigDecimal>>();  
					logger.info("quotes collection for : " +capStartTime.getTime());
					endTime.add(Calendar.MINUTE, ApplicationConstants.QUOTES_MINUTES_DATA );
					
					logger.info("startTime : "+ startTime.getTime().toString() + " :: endTime : "+ endTime.getTime().toString());
					Iterator<QuotesEventConsumedData> itr = lst.iterator();
					while(itr.hasNext()) {
						QuotesEventConsumedData quotesEventConsumedData = (QuotesEventConsumedData) itr.next();
						
						if(( quotesEventConsumedData.getFetchedTimeStamp().getTime().equals(startTime.getTime())
								|| quotesEventConsumedData.getFetchedTimeStamp().getTime().after(startTime.getTime()))
								&& ( quotesEventConsumedData.getFetchedTimeStamp().getTime().before(endTime.getTime()))
								) {
							testMap =new LinkedHashMap<String,BigDecimal>();
							testMap.put(format.format(quotesEventConsumedData.getFetchedTimeStamp().getTime()).toString(), 
										mapper.readValue(quotesEventConsumedData.getData(), QuoteEvent.class).getData().getPrice());
							itr.remove();
						}else {
							break;
						}
						list.add(testMap);
					}
					logger.info("No. of quotes for this min  : "+ list.size());
					startTime.setTime(endTime.getTime());
					capStartTime.add(Calendar.MINUTE, ApplicationConstants.QUOTES_MINUTES_DATA );
					finalList.add(list);
					logger.info("Total quotes  : "+ finalList.size());
				}
				
			}
			isinQuotesDataMap.put(map.getKey(), finalList);
		}
		return isinQuotesDataMap;
	}

}
