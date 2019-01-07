package com.sap.library.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.invoke.MethodHandles;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.library.utilities.SocketFactory;
import com.sap.library.utilities.exceptions.AuthenticationFailedException;
import com.sap.library.utilities.exceptions.MessageNotSentException;
import com.sap.library.utilities.exceptions.RegistrationFailedException;
import com.sap.library.utilities.message.Message;
import com.sap.library.utilities.message.Message.MessageType;
import com.sap.library.utilities.message.MessageDeliverer;

public class Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private Socket socket;
	private AuthenticationHelper authenticationHelper;
	private BookRequestManager bookManager;
	private boolean isAuthenticated;

	public Controller() throws IOException {
		socket = SocketFactory.getNewSocket();
		construct();
	}

	public Controller(String host, int port) throws IOException {
		socket = new Socket(host, port);
		construct();
	}

	/**
	 * Authenticate the user.
	 * 
	 * @throws MessageNotSentException
	 * @throws AuthenticationFailedException
	 */
	public void authenticate(String username, String password) {
		LOGGER.info("Trying to authenticate...");
		authenticationHelper.authenticate(username, password);
		LOGGER.info("Authenticated successfully");
		isAuthenticated = true;
	}

	/**
	 * Register the user.
	 * 
	 * @throws MessageNotSentException
	 * @throws RegistrationFailedException
	 */
	public void register(String username, String password) {
		LOGGER.info("Trying to register...");
		authenticationHelper.register(username, password);
		LOGGER.info("Registered successfully");
		isAuthenticated = true;
	}

	public BookRequestManager getBookManager() {
		if (!isAuthenticated) {
			throw new IllegalStateException("User is not authenticated!");
		}
		return bookManager;
	}

	private void construct() throws IOException {
		writer = new ObjectOutputStream(socket.getOutputStream());
		reader = new ObjectInputStream(socket.getInputStream());
		authenticationHelper = new AuthenticationHelper(reader, writer);
		bookManager = new BookRequestManager(reader, writer);
		isAuthenticated = false;
		LOGGER.info("Client controller constructed successfully");
	}

	public void close() throws IOException {
		MessageDeliverer.deliverMessage(writer, new Message(MessageType.DISCONNECT_REQUEST));
		writer.close();
		reader.close();
		socket.close();
	}

}
