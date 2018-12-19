package com.sap.library.Client;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import com.sap.library.Utilities.Book;
import com.sap.library.Utilities.Requester;
import com.sap.library.Utilities.Responser;

public class BookManager {
	
	private Requester requester;
	private Responser responser;
	
	public void addBook(Book book) {
		requester.sendRequest(); // TODO
	}
	
	public void deleteBook(String bookID) {
		requester.sendRequest(); // TODO
	}
	
	public void markBookAsTaken(String bookID, String person, Calendar startDate, Calendar endDate) {
		requester.sendRequest();
	}
	
	public void markBookAsReturned(String bookID, Calendar dateReturned) {
		requester.sendRequest(); // TODO
	}
	
	public Book searchByTitle(String title) {
		requester.sendRequest(); // TODO
		return (Book) responser.getResponse();
	}
	
	public Book searchByCreteria(String ...criteria) {
		requester.sendRequest(); // TODO
		return (Book) responser.getResponse();
	}
	
	public Optional<String> findIfBookIsTaken(String bookID) {
		requester.sendRequest(); // TODO
		return (Optional<String>) responser.getResponse();
	}
	
	public List<Book> getNotReturnedBooks() {
		return null;
	}
}
