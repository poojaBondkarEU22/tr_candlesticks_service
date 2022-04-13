package com.trade.republic.candlestick.client.datatransferobject;

import java.math.BigDecimal;

public class Candlestick {
	
	private String openTimeStamp;
	private String closeTimeStamp;
	private BigDecimal openPrice;
	private BigDecimal highPrice;
	private BigDecimal lowPrice;
	private BigDecimal closingPrice;
	
	public Candlestick() {
		
	}

	public Candlestick(String openTimeStamp, String closeTimeStamp, BigDecimal openPrice, BigDecimal highPrice,
			BigDecimal lowPrice, BigDecimal closingPrice) {
		super();
		this.openTimeStamp = openTimeStamp;
		this.closeTimeStamp = closeTimeStamp;
		this.openPrice = openPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.closingPrice = closingPrice;
	}


	public String getOpenTimeStamp() {
		return openTimeStamp;
	}


	public void setOpenTimeStamp(String openTimeStamp) {
		this.openTimeStamp = openTimeStamp;
	}


	public String getCloseTimeStamp() {
		return closeTimeStamp;
	}


	public void setCloseTimeStamp(String closeTimeStamp) {
		this.closeTimeStamp = closeTimeStamp;
	}


	public BigDecimal getOpenPrice() {
		return openPrice;
	}


	public void setOpenPrice(BigDecimal openPrice) {
		this.openPrice = openPrice;
	}


	public BigDecimal getHighPrice() {
		return highPrice;
	}


	public void setHighPrice(BigDecimal highPrice) {
		this.highPrice = highPrice;
	}


	public BigDecimal getLowPrice() {
		return lowPrice;
	}


	public void setLowPrice(BigDecimal lowPrice) {
		this.lowPrice = lowPrice;
	}


	public BigDecimal getClosingPrice() {
		return closingPrice;
	}


	public void setClosingPrice(BigDecimal closingPrice) {
		this.closingPrice = closingPrice;
	}


	@Override
	public String toString() {
		return "Candlestick [openTimeStamp=" + openTimeStamp + ", closeTimeStamp=" + closeTimeStamp + ", openPrice="
				+ openPrice + ", highPrice=" + highPrice + ", lowPrice=" + lowPrice + ", closingPrice=" + closingPrice
				+ "]";
	}
	
	

}
