package com.sap.library.utilities.exceptions;

import com.sap.library.utilities.message.Message.MessageType;

public class IllegalMessageTypeException extends RuntimeException {

	private static final long serialVersionUID = 1543297525097051088L;

	public IllegalMessageTypeException() {
		super();
	}

	public IllegalMessageTypeException(String message) {
		super(message);
	}

	public IllegalMessageTypeException(MessageType expected, MessageType actual) {
		this("Expected " + expected + " but got " + actual);
	}

	public IllegalMessageTypeException(Throwable cause) {
		super(cause);
	}

}
