package com.sap.library.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.sap.library.utilities.exceptions.AuthenticationFailedException;
import com.sap.library.utilities.message.Message;
import com.sap.library.utilities.message.Message.MessageType;

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
		try {
			sendRequestForAuthentication(username, password);
			receiveAuthenticationResponse();
		} catch (ClassNotFoundException | IOException e) {
			throw new AuthenticationFailedException(e);
		}
	}
	
	private void sendRequestForAuthentication(String username, String password) throws IOException {
		writer.writeObject(new Message(MessageType.AUTHENTICATION_REQUEST, username, password));
	}
	
	private void receiveAuthenticationResponse() throws ClassNotFoundException, IOException {
		Message response = (Message) reader.readObject();
		if (!response.getType().equals(MessageType.AUTHENTICATION_SUCCESS)) {
			throw new AuthenticationFailedException("Authentication Failed! Your username or password might be incorrect");
		}
	}
}
