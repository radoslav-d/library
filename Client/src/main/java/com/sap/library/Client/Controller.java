package com.sap.library.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.sap.library.utilities.SocketFactory;
import com.sap.library.utilities.exceptions.AuthenticationFailedException;
import com.sap.library.utilities.exceptions.MessageNotSentException;
import com.sap.library.utilities.exceptions.RegistrationFailedException;
import com.sap.library.utilities.message.Message;
import com.sap.library.utilities.message.Message.MessageType;
import com.sap.library.utilities.message.MessageDeliverer;

public class Controller implements Runnable {

	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private Socket socket;
	private AuthenticationHelper authenticationHelper;
	private BookRequestManager bookManager;
	private boolean isAuthenticated;
	private boolean isActive;

	public Controller() throws IOException {
		socket = SocketFactory.getNewSocket();
		construct();
	}

	public Controller(String host, int port) throws IOException {
		socket = new Socket(host, port);
		construct();
	}

	public void start() {
		if (!isAuthenticated) {
			throw new IllegalStateException("User is not authenticated!");
		}
		isActive = true;
		new Thread(this).start();
	}

	public void stop() {
		isActive = false;
	}

	public void run() {
		// TODO Auto-generated method stub
		while (isActive) {
			// TODO
		}
		endActivity();
	}

	/**
	 * Authenticate the user.
	 * 
	 * @throws MessageNotSentException
	 * @throws AuthenticationFailedException
	 */
	public void authenticate(String username, String password) {
		authenticationHelper.authenticate(username, password);
		isAuthenticated = true;
	}

	/**
	 * Register the user.
	 * 
	 * @throws MessageNotSentException
	 * @throws RegistrationFailedException
	 */
	public void register(String username, String password) {
		MessageDeliverer.deliverMessage(writer, new Message(MessageType.REGISTER_REQUEST, username, password));
		isAuthenticated = true;
	}

	public BookRequestManager getBookManager() {
		return bookManager;
	}

	private void construct() throws IOException {
		writer = new ObjectOutputStream(socket.getOutputStream());
		reader = new ObjectInputStream(socket.getInputStream());
		authenticationHelper = new AuthenticationHelper(reader, writer);
		bookManager = new BookRequestManager(reader, writer);
		isAuthenticated = false;
		isActive = false;
	}

	private void endActivity() {
		try {
			writer.close();
			reader.close();
			socket.close();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
