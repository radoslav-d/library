package com.sap.library.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.util.List;

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
	public void deleteBook(int bookId) {
		MessageDeliverer.deliverMessage(writer, new Message(MessageType.DELETE_BOOK_REQUEST, bookId));
	}

	@Override
	public void markBookAsTaken(int bookId, String person, Date startDate, Date endDate) {
		MessageDeliverer.deliverMessage(writer,
				new Message(MessageType.MARK_BOOK_AS_TAKEN_REQUEST, bookId, person, startDate, endDate));
	}

	@Override
	public void markBookAsReturned(int bookId, Date dateReturned) {
		MessageDeliverer.deliverMessage(writer,
				new Message(MessageType.MARK_BOOK_AS_RETURNED_REQUEST, bookId, dateReturned));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Book> searchBook(String criteria) {
		MessageDeliverer.deliverMessage(writer, new Message(MessageType.SEARCH_REQUEST, criteria));
		Message message = MessageDeliverer.receiveMessage(reader);
		if (!message.getType().equals(MessageType.SEARCH_RESPONSE)) {
			throw new IllegalMessageTypeException(MessageType.SEARCH_RESPONSE, message.getType());
		}
		return (List<Book>) message.getArgument(0);
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
