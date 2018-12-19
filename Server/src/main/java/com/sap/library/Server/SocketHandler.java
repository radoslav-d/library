package com.sap.library.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import com.sap.library.utilities.Book;
import com.sap.library.utilities.exceptions.AuthenticationFailedException;
import com.sap.library.utilities.exceptions.IllegalMessageTypeException;
import com.sap.library.utilities.message.Message;
import com.sap.library.utilities.message.Message.MessageType;
import com.sap.library.utilities.message.MessageDeliverer;

public class SocketHandler implements Runnable {

	private Controller controller;
	private BookResponsesManager bookManager;
	private ObjectOutputStream writer;
	private ObjectInputStream reader;
	private boolean isActive = false;

	public SocketHandler(Socket socket, Controller controller, BookResponsesManager bookManager) throws IOException {
		this.bookManager = bookManager;
		this.controller = controller;
		writer = new ObjectOutputStream(socket.getOutputStream());
		reader = new ObjectInputStream(socket.getInputStream());
		isActive = false;
	}

	public void start() {
		isActive = true;
		new Thread(this).start();
	}

	public void stop() {
		isActive = false;
	}

	public void run() {
		try {
			authenticate();
			while (isActive) {
				processRequests();
			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void authenticate() throws ClassNotFoundException, IOException {
		Message message = MessageDeliverer.receiveMessage(reader);
		if (!message.getClass().equals(MessageType.AUTHENTICATION_REQUEST)) {
			throw new AuthenticationFailedException(
					"Expected to receive " + MessageType.AUTHENTICATION_REQUEST + " but got " + message.getType());
		}
		String username = (String) message.getArgument(0);
		String password = (String) message.getArgument(1);
		controller.authenticateUser(username, password);
	}

	@SuppressWarnings("unchecked")
	private void processRequests() throws ClassNotFoundException, IOException {
		Message message = MessageDeliverer.receiveMessage(reader);
		switch (message.getType()) {
		case ADD_BOOK_REQUEST:
			Book book = (Book) message.getArgument(0);
			bookManager.addBook(book);
			break;
		case DELETE_BOOK_REQUEST:
			String bookToDelete = (String) message.getArgument(0);
			bookManager.deleteBook(bookToDelete);
			break;
		case IS_BOOK_TAKEN_REQUEST:
			String bookToCheck = (String) message.getArgument(0);
			Optional<String> personName = bookManager.findIfBookIsTaken(bookToCheck);
			MessageDeliverer.deliverMessage(writer, new Message(MessageType.IS_BOOK_TAKEN_RESPONSE, personName));
			break;
		case MARK_BOOK_AS_RETURNED_REQUEST:
			String bookReturned = (String) message.getArgument(0);
			Calendar returnedOn = (Calendar) message.getArgument(1);
			bookManager.markBookAsReturned(bookReturned, returnedOn);
			break;
		case MARK_BOOK_AS_TAKEN_REQUEST:
			String bookTaken = (String) message.getArgument(0);
			String person = (String) message.getArgument(1);
			Calendar from = (Calendar) message.getArgument(2);
			Optional<Calendar> to = (Optional<Calendar>) message.getArgument(3);
			bookManager.markBookAsTaken(bookTaken, person, from, to);
			break;
		case NOT_RETURNED_BOOKS_REQUEST:
			List<Book> notReturnedBooks = bookManager.getNotReturnedBooks();
			MessageDeliverer.deliverMessage(writer,
					new Message(MessageType.NOT_RETURNED_BOOKS_RESPONSE, notReturnedBooks));
			break;
		case SEARCH_REQUEST:
			List<String> criteria = (List<String>) message.getArgument(0);
			List<Book> resultOfSearch = bookManager.searchBook(criteria);
			MessageDeliverer.deliverMessage(writer, new Message(MessageType.SEARCH_RESPONSE, resultOfSearch));
			break;
		default:
			throw new IllegalMessageTypeException("Expected Request MessageType but got " + message.getType());
		}
	}

}
