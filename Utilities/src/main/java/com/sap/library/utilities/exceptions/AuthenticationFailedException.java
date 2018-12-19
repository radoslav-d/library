package com.sap.library.utilities.exceptions;

public class AuthenticationFailedException extends RuntimeException {
	private static final long serialVersionUID = 6573251221366134919L;
	
	public AuthenticationFailedException() {
		super();
	}
	
	public AuthenticationFailedException(String message) {
		super(message);
	}

	public AuthenticationFailedException(Throwable cause) {
		super(cause);
	}

}
