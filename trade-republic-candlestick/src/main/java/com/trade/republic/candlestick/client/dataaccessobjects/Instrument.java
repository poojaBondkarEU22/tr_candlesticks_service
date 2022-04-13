package com.trade.republic.candlestick.client.dataaccessobjects;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Instrument {
	
	private String isin;
	private String description;
	
	public Instrument() {
		
	}

	public Instrument(String isin, String description) {
		super();
		this.isin = isin;
		this.description = description;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Instrument [isin=" + isin + ", description=" + description + "]";
	}
	
	

}
