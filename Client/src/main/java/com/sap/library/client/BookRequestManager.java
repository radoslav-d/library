package com.sap.library.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import com.sap.library.utilities.Book;
import com.sap.library.utilities.BookManager;
import com.sap.library.utilities.exceptions.IllegalMessageTypeException;
import com.sap.library.utilities.message.Message;
import com.sap.library.utilities.message.Message.MessageType;
import com.sap.library.utilities.message.MessageDeliverer;

public class BookRequestManager implements BookManager {

	private ObjectInputStream reader;
	private ObjectOutputStream writer;

	public BookRequestManager(ObjectInputStream reader, ObjectOutputStream writer) {
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	public void addBook(Book book) {
		MessageDeliverer.deliverMessage(writer, new Message(MessageType.ADD_BOOK_REQUEST, book));
	}

	@Override
	public void deleteBook(String bookID) {
		MessageDeliverer.deliverMessage(writer, new Message(MessageType.DELETE_BOOK_REQUEST, bookID));
	}

	@Override
	public void markBookAsTaken(String bookID, String person, Calendar startDate, Optional<Calendar> endDate) {
		MessageDeliverer.deliverMessage(writer,
				new Message(MessageType.MARK_BOOK_AS_TAKEN_REQUEST, bookID, person, startDate, endDate));
	}

	@Override
	public void markBookAsReturned(String bookID, Calendar dateReturned) {
		MessageDeliverer.deliverMessage(writer,
				new Message(MessageType.MARK_BOOK_AS_RETURNED_REQUEST, bookID, dateReturned));
	}

	@SuppressWarnings("unchecked")
	public List<Book> searchBook(List<String> criteria) {
		MessageDeliverer.deliverMessage(writer, new Message(MessageType.SEARCH_REQUEST, criteria));
		Message message = MessageDeliverer.receiveMessage(reader);
		if (!message.getType().equals(MessageType.SEARCH_RESPONSE)) {
			throw new IllegalMessageTypeException(MessageType.SEARCH_RESPONSE, message.getType());
		}
		return (List<Book>) message.getArgument(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<String> findIfBookIsTaken(String bookID) {
		MessageDeliverer.deliverMessage(writer, new Message(MessageType.IS_BOOK_TAKEN_REQUEST, bookID));
		Message message = MessageDeliverer.receiveMessage(reader);
		if (!message.getType().equals(MessageType.IS_BOOK_TAKEN_RESPONSE)) {
			throw new IllegalMessageTypeException(MessageType.IS_BOOK_TAKEN_RESPONSE, message.getType());
		}
		return (Optional<String>) message.getArgument(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Book> getNotReturnedBooks() {
		MessageDeliverer.deliverMessage(writer, new Message(MessageType.NOT_RETURNED_BOOKS_REQUEST));
		Message message = MessageDeliverer.receiveMessage(reader);
		if (!message.getType().equals(MessageType.NOT_RETURNED_BOOKS_RESPONSE)) {
			throw new IllegalMessageTypeException(MessageType.NOT_RETURNED_BOOKS_RESPONSE, message.getType());
		}
		return (List<Book>) message.getArgument(0);
	}
}
