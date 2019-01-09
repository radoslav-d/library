package com.sap.library.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.invoke.MethodHandles;
import java.net.Socket;
import java.sql.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.library.server.database.DataBaseException;
import com.sap.library.utilities.Book;
import com.sap.library.utilities.exceptions.AuthenticationFailedException;
import com.sap.library.utilities.exceptions.ConnectionInterruptedException;
import com.sap.library.utilities.exceptions.IllegalMessageTypeException;
import com.sap.library.utilities.exceptions.RegistrationFailedException;
import com.sap.library.utilities.message.Message;
import com.sap.library.utilities.message.Message.MessageType;
import com.sap.library.utilities.message.MessageDeliverer;

public class SocketHandler implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private Controller controller;
	private BookResponsesManager bookManager;
	private ObjectOutputStream writer;
	private ObjectInputStream reader;
	private boolean isActive = false;

	public SocketHandler(Socket socket, Controller controller, BookResponsesManager bookManager) throws IOException {
		writer = new ObjectOutputStream(socket.getOutputStream());
		reader = new ObjectInputStream(socket.getInputStream());
		this.bookManager = bookManager;
		this.controller = controller;
		isActive = false;
		LOGGER.info("SocketHandler constructed successfully");
	}

	public void start() {
		if (!isActive) {
			isActive = true;
			new Thread(this).start();
			LOGGER.info("New socket handling thread started...");
		}
	}

	public synchronized void stop() {
		isActive = false;
	}

	private void stopAndRemoveReferenceFromController() {
		stop();
		controller.removeHandler(this);
	}

	public void run() {
		try {
			authenticate();
			while (isActive) {
				processRequests();
			}
		} catch (ConnectionInterruptedException e) {
			stopAndRemoveReferenceFromController();
		} catch (IllegalMessageTypeException | DataBaseException e) {
			MessageDeliverer.deliverMessage(writer, new Message(MessageType.DISCONNECT_REQUEST));
			LOGGER.error(e.getMessage(), e);
		}
	}

	private void authenticate() {
		Message message = MessageDeliverer.receiveMessage(reader);
		MessageType type = message.getType();
		if (!type.equals(MessageType.AUTHENTICATION_REQUEST) && !type.equals(MessageType.REGISTER_REQUEST)) {
			throw new IllegalMessageTypeException("Expected to receive " + MessageType.AUTHENTICATION_REQUEST + " or "
					+ MessageType.REGISTER_REQUEST + " but got " + message.getType());
		}
		String username = (String) message.getArgument(0);
		String password = (String) message.getArgument(1);
		if (type.equals(MessageType.AUTHENTICATION_REQUEST)) {
			LOGGER.info("Trying to authenticate user [" + username + "]...");
			sendResponseToAuthentication(username, password);
		} else {
			LOGGER.info("Trying to register user [" + username + "]...");
			sendResponseToRegistration(username, password);
		}
	}

	private void sendResponseToAuthentication(String username, String password) {
		try {
			controller.authenticateUser(username, password);
			MessageDeliverer.deliverMessage(writer, new Message(MessageType.AUTHENTICATION_SUCCESS));
		} catch (AuthenticationFailedException e) {
			LOGGER.error(e.getMessage(), e);
			MessageDeliverer.deliverMessage(writer, new Message(MessageType.AUTHENTICATION_FAILED));
		}
	}

	private void sendResponseToRegistration(String username, String password) {
		try {
			controller.registerUser(username, password);
			MessageDeliverer.deliverMessage(writer, new Message(MessageType.REGISTER_SUCCESS));
		} catch (RegistrationFailedException e) {
			LOGGER.error(e.getMessage(), e);
			MessageDeliverer.deliverMessage(writer, new Message(MessageType.REGISTER_FAILED));
		}
	}

	private void processRequests() {
		Message message = MessageDeliverer.receiveMessage(reader);
		LOGGER.info("New [" + message.getType() + "] received");
		switch (message.getType()) {
		case ADD_BOOK_REQUEST:
			Book book = (Book) message.getArgument(0);
			bookManager.addBook(book);
			break;
		case DELETE_BOOK_REQUEST:
			int bookToDelete = (int) message.getArgument(0);
			bookManager.deleteBook(bookToDelete);
			break;
		case MARK_BOOK_AS_RETURNED_REQUEST:
			int bookReturned = (int) message.getArgument(0);
			Date returnedOn = (Date) message.getArgument(1);
			bookManager.markBookAsReturned(bookReturned, returnedOn);
			break;
		case MARK_BOOK_AS_TAKEN_REQUEST:
			int bookTaken = (int) message.getArgument(0);
			String person = (String) message.getArgument(1);
			Date from = (Date) message.getArgument(2);
			Date to = (Date) message.getArgument(3);
			bookManager.markBookAsTaken(bookTaken, person, from, to);
			break;
		case NOT_RETURNED_BOOKS_REQUEST:
			List<Book> notReturnedBooks = bookManager.getNotReturnedBooks();
			MessageDeliverer.deliverMessage(writer,
					new Message(MessageType.NOT_RETURNED_BOOKS_RESPONSE, notReturnedBooks));
			break;
		case SEARCH_REQUEST:
			String criteria = (String) message.getArgument(0);
			List<Book> resultOfSearch = bookManager.searchBook(criteria);
			MessageDeliverer.deliverMessage(writer, new Message(MessageType.SEARCH_RESPONSE, resultOfSearch));
			break;
		case DISCONNECT_REQUEST:
			stopAndRemoveReferenceFromController();
			isActive = false;
			controller.removeHandler(this);
			break;
		default:
			throw new IllegalMessageTypeException("Expected Request MessageType but got " + message.getType());
		}
	}

}
