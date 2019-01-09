package com.sap.library.server;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.library.server.database.AuthenticationService;
import com.sap.library.server.database.PostgreService;
import com.sap.library.utilities.SocketFactory;

public class Controller implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private ServerSocket serverSocket;
	private List<SocketHandler> socketHandlers;
	private BookResponsesManager bookManager;
	private PostgreService postgreService;
	private AuthenticationService authenticationService;
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
		if (isActive) {
			throw new IllegalThreadStateException();
		}
		isActive = true;
		new Thread(this).start();
		LOGGER.info("Controller started...");
	}

	public void stop() {
		isActive = false;
		socketHandlers.forEach(SocketHandler::stop);
		try {
			postgreService.close();
			serverSocket.close();
		} catch (IOException | SQLException e) {
			LOGGER.warn(e.getMessage());
		}
		LOGGER.info("Controller stopped");
	}

	@Override
	public synchronized void run() {
		while (isActive) {
			acceptClients();
		}
	}

	private void acceptClients() {
		try {
			LOGGER.info("Listening for connections...");
			Socket socket = serverSocket.accept();
			SocketHandler handler = new SocketHandler(socket, this, bookManager);
			handler.start();
			addHandlerToList(handler);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	public void authenticateUser(String username, String password) {
		authenticationService.authenticateUser(username, password);
		LOGGER.info("User [{}] authenticated", username);
	}

	public void registerUser(String username, String password) {
		authenticationService.registerUser(username, password);
		LOGGER.info("User [{}] registered", username);
	}

	public synchronized void removeHandler(SocketHandler handler) {
		socketHandlers.remove(handler);
	}

	private void construct(String databaseUrl) throws SQLException {
		LOGGER.info("Server started on port: {}", serverSocket.getLocalPort());
		socketHandlers = new ArrayList<>();
		postgreService = new PostgreService(databaseUrl);
		authenticationService = new AuthenticationService(postgreService.getConnection());
		bookManager = new BookResponsesManager(postgreService);
		isActive = false;
		LOGGER.info("Controller constructed successfully");
	}

	private void addHandlerToList(SocketHandler handler) {
		// add is done in a new thread because synchronized block may block the server
		new Thread(() -> {
			synchronized (socketHandlers) {
				socketHandlers.add(handler);
			}
		}).start();
	}

}
