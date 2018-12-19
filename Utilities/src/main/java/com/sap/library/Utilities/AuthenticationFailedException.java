package com.sap.library.Utilities;

public class AuthenticationFailedException extends RuntimeException {
	private static final long serialVersionUID = 6573251221366134919L;
	
	public AuthenticationFailedException() {
		super();
	}
	
	public AuthenticationFailedException(String message) {
		super(message);
	}

}
