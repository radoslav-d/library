package com.sap.library.Server;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.library.utilities.SocketFactory;

public class Controller implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
		LOGGER.info("Controller started...");
	}

	public void stop() {
		isActive = false;
		socketHandlers.stream().forEach(SocketHandler::stop);
		try {
			serverSocket.close();
		} catch (IOException e) {
			LOGGER.warn(e.getMessage());
		}
		LOGGER.info("Controller stopped");
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
			LOGGER.error(e.getMessage(), e);
		}

	}

	public void authenticateUser(String username, String password) {
		postgreService.authenticate(username, password);
		LOGGER.info("User [" + username + "] authenticated");
	}

	public void registerUser(String username, String password) {
		postgreService.registerUser(username, password);
		LOGGER.info("User [" + username + "] registered");
	}

	public void removeHandler(SocketHandler handler) {
		socketHandlers.remove(handler);
	}

	private void construct(String databaseUrl) throws SQLException {
		LOGGER.info("Server started on port: " + serverSocket.getLocalPort());
		socketHandlers = new ArrayList<>();
		postgreService = new PostgreService(databaseUrl);
		bookManager = new BookResponsesManager(postgreService);
		LOGGER.info("Controller constructed successfully");
	}

}
