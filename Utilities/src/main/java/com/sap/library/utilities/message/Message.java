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
	 * String, a Date from and an Date to;
	 * 
	 * - MARK_BOOK_AS_RETURNED_REQUEST: requires a book ID as an int and a Date, on
	 * which is returned;
	 * 
	 * - SEARCH_REQUEST: requires a String;
	 * 
	 * - SEARCH_RESPONSE: requires a List<Book>;
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

		/**
		 * Sent by client is requesting authentication
		 */
		AUTHENTICATION_REQUEST,

		/**
		 * Sent by server when the authentication failed
		 */
		AUTHENTICATION_FAILED,

		/**
		 * Sent by server when the authentication is successful
		 */
		AUTHENTICATION_SUCCESS,

		/**
		 * Sent by client is requesting registration
		 */
		REGISTER_REQUEST,

		/**
		 * Sent by server when the registration failed
		 */
		REGISTER_FAILED,

		/**
		 * Sent by server when the registration is successful
		 */
		REGISTER_SUCCESS,

		/**
		 * Sent by client when wants to add new book
		 */
		ADD_BOOK_REQUEST,

		/**
		 * Sent by client when wants to delete book
		 */
		DELETE_BOOK_REQUEST,

		/**
		 * Sent by client when wants to mark a book as taken
		 */
		MARK_BOOK_AS_TAKEN_REQUEST,

		/**
		 * Sent by client when wants to mark a book as returned
		 */
		MARK_BOOK_AS_RETURNED_REQUEST,

		/**
		 * Sent by client when wants to fetch books matching criteria
		 */
		SEARCH_REQUEST,

		/**
		 * Sent by server as a result of search request
		 */
		SEARCH_RESPONSE,

		/**
		 * Sent by client when wants to fetch all books that are not returned
		 */
		NOT_RETURNED_BOOKS_REQUEST,

		/**
		 * Sent by server as a result of request for not returned books
		 */
		NOT_RETURNED_BOOKS_RESPONSE,

		/**
		 * Sent by server when there is a problem. Sent by client when wants to
		 * disconnect
		 */
		DISCONNECT_REQUEST
	}
}
