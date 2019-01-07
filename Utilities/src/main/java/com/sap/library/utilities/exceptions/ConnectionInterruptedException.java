package com.sap.library.utilities.exceptions;

public class ConnectionInterruptedException extends RuntimeException {

	private static final long serialVersionUID = 7185234417978670324L;

	public ConnectionInterruptedException() {
	}

	public ConnectionInterruptedException(String message) {
		super(message);
	}

	public ConnectionInterruptedException(Throwable cause) {
		super(cause);
	}

}
