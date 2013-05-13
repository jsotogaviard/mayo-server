package com.mayo;

public class MayoException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public MayoException() {}
	
	public MayoException(String message, Throwable exception) {
		super(message, exception);
	}

	public MayoException(Throwable exception) {
		super(exception);
	}
	
	public MayoException(String message) {
		super(message);
	}
}
