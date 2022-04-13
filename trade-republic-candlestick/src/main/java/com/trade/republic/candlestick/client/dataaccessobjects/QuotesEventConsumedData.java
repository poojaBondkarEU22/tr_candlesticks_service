package com.trade.republic.candlestick.client.dataaccessobjects;



import java.util.Calendar;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("tr_t_instruemts_quote_event")
public class QuotesEventConsumedData {
	
	
	private Calendar fetchedTimeStamp;
	private String data;
	
	public QuotesEventConsumedData() {
		
	}

	public QuotesEventConsumedData(Calendar fetchedTimeStamp, String data) {
		super();
		this.fetchedTimeStamp = fetchedTimeStamp;
		this.data = data;
	}

	
	public Calendar getFetchedTimeStamp() {
		return fetchedTimeStamp;
	}

	public void setFetchedTimeStamp(Calendar fetchedTimeStamp) {
		this.fetchedTimeStamp = fetchedTimeStamp;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "QuotesEventConsumedData [fetchedTimeStamp=" + fetchedTimeStamp.getTime() + ", data=" + data + "]";
	}

	
}
