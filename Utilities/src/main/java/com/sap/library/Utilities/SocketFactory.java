package com.sap.library.Utilities;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Utility Factory class for Sockets and ServerSockets.
 * 
 * @author Radoslav Dimitrov
 */
public class SocketFactory {

	/**
	 * First port from the range of ports.
	 */
	public static final int FIRST_PORT = 7000;

	/**
	 * Last port from the range of ports.
	 */
	public static final int LAST_PORT = 7020;

	/**
	 * Utility class constructor.
	 */
	private SocketFactory() {
	}

	/**
	 * Seeks the ServerSocket on the localhost from port 7000 to 7020.
	 * 
	 * @return - a Socket instance.
	 * @throws IOException if unable to find the ServerSocket.
	 */
	public static Socket getNewSocket() throws IOException {
		for (int port = FIRST_PORT; port <= LAST_PORT; port++) {
			try {
				return new Socket("localhost", port);
			} catch (IOException e) {
				// Try the next port
			}
		}
		throw new IOException("No ServerSocket running on ports range 7000 - 7020.");
	}

	/**
	 * Finds an empty port (from 7000 to 7020) and returns an instance of SocketServer.
	 * 
	 * @return - an instance of SocketServer.
	 * @throws IOException - if there is no empty port.
	 */
	public static ServerSocket getNewServerSocket() throws IOException {
		for (int port = FIRST_PORT; port <= LAST_PORT; port++) {
			try {
				return new ServerSocket(port);
			} catch (IOException e) {
				// Try the next port
			}
		}
		throw new IOException("ServerSocket is unable to find port in range 7000 - 7020!");
	}

}
