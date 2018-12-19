package com.sap.library.utilities.exceptions;

public class MessageNotSentException extends RuntimeException {

	private static final long serialVersionUID = -127541731797170972L;

	public MessageNotSentException() {
		super();
	}

	public MessageNotSentException(String message) {
		super(message);
	}

	public MessageNotSentException(Throwable cause) {
		super(cause);
	}

}
