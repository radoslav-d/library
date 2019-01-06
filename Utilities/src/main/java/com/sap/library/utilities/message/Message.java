package com.sap.library.utilities.message;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = -7118707488670961531L;

	private MessageType type;

	private Object[] args;

	/**
	 * Constructs a Message. Depending on the MessageType, the args should be as
	 * follows:
	 * 
	 * - AUTHENTICATION_REQUEST: requires an username and a password as a String;
	 * 
	 * - AUTHENTICATION_FAILED: does not require anything;
	 * 
	 * - AUTHENTICATION_SUCCESS: does not require anything;
	 * 
	 * - REGISTER_REQUEST: requires an username and a password;
	 *
	 * - REGISTER_FAILED: does not require anything;
	 * 
	 * - REGISTER_SUCCESS: does not require anything;
	 * 
	 * - ADD_BOOK_REQUEST: requires a book instance;
	 * 
	 * - DELETE_BOOK_REQUEST: requires a book ID as an int;
	 * 
	 * - MARK_BOOK_AS_TAKEN_REQUEST: requires a book ID as an int, person name as a
	 * String, a Date from and an Optional<Date> to;
	 * 
	 * - MARK_BOOK_AS_RETURNED_REQUEST: requires a book ID as an int and a Date, on
	 * which is returned;
	 * 
	 * - SEARCH_REQUEST: requires a String;
	 * 
	 * - SEARCH_RESPONSE: requires a List<Book>;
	 * 
	 * - IS_BOOK_TAKEN_REQUEST: requires a book ID as an int;
	 * 
	 * - IS_BOOK_TAKEN_RESPONSE: requires an Optional<String> containing the person,
	 * who has taken the book;
	 * 
	 * - NOT_RETURNED_BOOKS_REQUEST: does not require anything;
	 * 
	 * - NOT_RETURNED_BOOKS_RESPONSE: requires a List<Book>;
	 * 
	 * @param type
	 *            - the MessageType of the Message
	 * @param args
	 *            - the arguments depending on the MessageType
	 */
	public Message(MessageType type, Object... args) {
		this.type = type;
		this.args = args;
	}

	public MessageType getType() {
		return type;
	}

	public Object getArgument(int index) {
		if (index >= args.length || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		return args[index];
	}

	public enum MessageType {
		AUTHENTICATION_REQUEST, AUTHENTICATION_FAILED, AUTHENTICATION_SUCCESS,

		REGISTER_REQUEST, REGISTER_FAILED, REGISTER_SUCCESS,

		ADD_BOOK_REQUEST, DELETE_BOOK_REQUEST,

		MARK_BOOK_AS_TAKEN_REQUEST, MARK_BOOK_AS_RETURNED_REQUEST,

		SEARCH_REQUEST, SEARCH_RESPONSE,

		NOT_RETURNED_BOOKS_REQUEST, NOT_RETURNED_BOOKS_RESPONSE,

		DISCONNECT_REQUEST
	}
}
