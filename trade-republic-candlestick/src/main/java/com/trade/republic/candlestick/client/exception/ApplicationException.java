package com.trade.republic.candlestick.client.exception;

public class ApplicationException extends RuntimeException{
	
	
	private static final long serialVersionUID = 1555273809301865528L;

	public ApplicationException() {
		super();
	}
	
	public ApplicationException(String message) {
		super(message);
	}
	
	public ApplicationException(String message,Throwable e) {
		super(message,e);
	}

}
