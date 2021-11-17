package com.trybyl.emailproject.exceptions;

public class CustomException extends RuntimeException {
	
    public CustomException() {
        super("");
    }
    public CustomException(String message) {
        super(message);
    }
    
    public CustomException(Exception exception) {
        super(exception);
    }


}
