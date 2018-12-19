package com.sap.library.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.sap.library.utilities.SocketFactory;
import com.sap.library.utilities.exceptions.AuthenticationFailedException;

public class Controller implements Runnable {

	private ServerSocket serverSocket;
	private List<SocketHandler> socketHandlers;
	private BookResponsesManager bookManager;
	private boolean isActive;

	public Controller() throws IOException {
		serverSocket = SocketFactory.getNewServerSocket();
		construct();
	}

	public Controller(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		construct();
	}

	public void start() {
		isActive = true;
		new Thread(this).start();
	}

	public void stop() {
		isActive = false;
		socketHandlers.stream().forEach(handler -> handler.stop());
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		while (isActive) {
			acceptClients();
		}

	}

	private void acceptClients() {
		try {
			Socket socket = serverSocket.accept();
			SocketHandler handler = new SocketHandler(socket, this, bookManager);
			handler.start();
			socketHandlers.add(handler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void authenticateUser(String username, String password) {
		// TODO fetch user from database
		throw new AuthenticationFailedException(
				"Authentication Failed! There is no matching username and password in the DB");
	}

	private void construct() {
		socketHandlers = new ArrayList<>();
		bookManager = new BookResponsesManager();
	}

}
