package com.sap.library.Utilities;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = -7118707488670961531L;

	private String username;

	private MessageType type;

	private String additionalInformation;

	public Message(String username, MessageType type, String additionalInformation) {
		this.username = username;
		this.type = type;
		this.additionalInformation = additionalInformation;
	}

	public enum MessageType {
		AUTHENTICATION_REQUEST, AUTHENTICATION_FAILED, AUTHENTICATION_SUCCESS,

	}

	public String getUsername() {
		return username;
	}

	public MessageType getType() {
		return type;
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}
}
