package com.trade.republic.candlestick.client.exception;

public class InvalidInstrumentsException extends RuntimeException{
	

	private static final long serialVersionUID = -7705382229430747220L;

	public InvalidInstrumentsException() {
		super();
	}
	
	public InvalidInstrumentsException(String message) {
		super(message);
	}
	
	public InvalidInstrumentsException(String message,Throwable e) {
		super(message,e);
	}

}
