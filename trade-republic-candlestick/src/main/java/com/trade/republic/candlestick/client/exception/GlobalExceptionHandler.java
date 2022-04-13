package com.trade.republic.candlestick.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	
	@ExceptionHandler(value = InvalidInstrumentsException.class)
    public ResponseEntity invalidInstrumentException(InvalidInstrumentsException invalidInstrumentsException) {
        return new ResponseEntity(invalidInstrumentsException.getMessage(), HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity applicationException(ApplicationException applicationException) {
        return new ResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	@ExceptionHandler(value = NoQuotesForInstrumentsException.class)
    public ResponseEntity noQuotesForInstrumentsException(NoQuotesForInstrumentsException noQuotesForInstrumentsException) {
        return new ResponseEntity(noQuotesForInstrumentsException.getMessage(), HttpStatus.NOT_FOUND);
    }
 

}
