package com.trade.republic.candlestick.client.dataaccessobjects;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
@Document
public class QuoteEvent {
	
	private Quote data;
	private String type;
	
	public QuoteEvent() {
		
	}

	public QuoteEvent(Quote data, String type) {
		super();
		this.data = data;
		this.type = type;
	}

	public Quote getData() {
		return data;
	}

	public void setData(Quote data) {
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "QuoteEvent [data=" + data + ", type=" + type + "]";
	}


}
