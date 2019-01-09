package com.sap.library.utilities.exceptions;

public class RegistrationFailedException extends RuntimeException {

	private static final long serialVersionUID = -733350220620164754L;

	public RegistrationFailedException() {
		super();
	}

	public RegistrationFailedException(String message) {
		super(message);
	}

	public RegistrationFailedException(Throwable cause) {
		super(cause);
	}
}
