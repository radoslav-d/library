package com.sap.library.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.sap.library.utilities.exceptions.AuthenticationFailedException;
import com.sap.library.utilities.exceptions.RegistrationFailedException;
import com.sap.library.utilities.message.Message;
import com.sap.library.utilities.message.Message.MessageType;
import com.sap.library.utilities.message.MessageDeliverer;

/**
 * Helper class used in client side for sending and receiving authentication
 * request and response.
 * 
 * @author Radoslav Dimitrov
 */
public class AuthenticationHelper {

	private ObjectInputStream reader;
	private ObjectOutputStream writer;

	public AuthenticationHelper(ObjectInputStream reader, ObjectOutputStream writer) {
		this.reader = reader;
		this.writer = writer;
	}

	public void authenticate(String username, String password) {
		sendRequestForAuthentication(username, password);
		receiveAuthenticationResponse();
	}

	private void sendRequestForAuthentication(String username, String password) {
		MessageDeliverer.deliverMessage(writer, new Message(MessageType.AUTHENTICATION_REQUEST, username, password));
	}

	private void receiveAuthenticationResponse() {
		Message response = MessageDeliverer.receiveMessage(reader);
		if (!response.getType().equals(MessageType.AUTHENTICATION_SUCCESS)) {
			throw new AuthenticationFailedException(
					"Authentication Failed! Your username or password might be incorrect");
		}
	}

	public void register(String username, String password) {
		sendRequestForRegistration(username, password);
		receiveRegistrationResponse();
	}

	private void sendRequestForRegistration(String username, String password) {
		MessageDeliverer.deliverMessage(writer, new Message(MessageType.REGISTER_REQUEST, username, password));
	}

	private void receiveRegistrationResponse() {
		Message response = MessageDeliverer.receiveMessage(reader);
		if (!response.getType().equals(MessageType.REGISTER_SUCCESS)) {
			throw new RegistrationFailedException("Registration Failed! Your username is already taken.");
		}
	}
}
