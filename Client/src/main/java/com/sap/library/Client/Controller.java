package com.sap.library.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.sap.library.Utilities.SocketFactory;

public class Controller implements Runnable {

	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private Socket socket;
	private AuthenticationHelper authenticationHelper;
	private boolean isAuthenticated;

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
		new Thread(this).start();
	}

	public void run() {
		// TODO Auto-generated method stub

	}

	public void authenticate(String username, String password) {
		authenticationHelper.authenticate(username, password);
		isAuthenticated = true;
	}

	private void construct() throws IOException {
		writer = new ObjectOutputStream(socket.getOutputStream());
		reader = new ObjectInputStream(socket.getInputStream());
		authenticationHelper = new AuthenticationHelper(reader, writer);
		isAuthenticated = false;
	}

}
