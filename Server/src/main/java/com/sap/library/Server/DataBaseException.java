package com.sap.library.Server;

public class DataBaseException extends RuntimeException {

	private static final long serialVersionUID = -3445334152357204312L;

	public DataBaseException() {
	}

	public DataBaseException(String message) {
		super(message);
	}

	public DataBaseException(Throwable cause) {
		super(cause);
	}

}
