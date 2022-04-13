package com.trade.republic.candlestick.client.dataaccessobjects;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Quote {
	
	private String isin;
	private BigDecimal price;
	
	public Quote() {
		
	}

	public Quote(String isin, BigDecimal price) {
		super();
		this.isin = isin;
		this.price = price;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Quote [isin=" + isin + ", price=" + price + "]";
	}
	
	

}
