package com.trade.republic.candlestick.client.exception;

public class NoQuotesForInstrumentsException extends RuntimeException{
	
	
	private static final long serialVersionUID = -6277856507904288549L;

	public NoQuotesForInstrumentsException() {
		super();
	}
	
	public NoQuotesForInstrumentsException(String message) {
		super(message);
	}
	
	public NoQuotesForInstrumentsException(String message,Throwable e) {
		super(message,e);
	}

}
