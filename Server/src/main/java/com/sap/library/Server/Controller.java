package com.sap.library.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sap.library.utilities.SocketFactory;

public class Controller implements Runnable {

	private ServerSocket serverSocket;
	private List<SocketHandler> socketHandlers;
	private BookResponsesManager bookManager;
	private PostgreService postgreService;
	private boolean isActive;

	public Controller(String databaseUrl) throws IOException, SQLException {
		serverSocket = SocketFactory.getNewServerSocket();
		construct(databaseUrl);
	}

	public Controller(int port, String databaseUrl) throws IOException, SQLException {
		serverSocket = new ServerSocket(port);
		construct(databaseUrl);
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
		postgreService.authenticate(username, password);
	}

	public void registerUser(String username, String password) {
		postgreService.registerUser(username, password);
	}

	private void construct(String databaseUrl) throws SQLException {
		socketHandlers = new ArrayList<>();
		bookManager = new BookResponsesManager();
		postgreService = new PostgreService(databaseUrl);
	}

}
