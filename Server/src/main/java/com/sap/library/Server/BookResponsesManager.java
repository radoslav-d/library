package com.sap.library.Server;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import com.sap.library.utilities.Book;
import com.sap.library.utilities.BookManager;

public class BookResponsesManager implements BookManager {

	public BookResponsesManager() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addBook(Book book) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteBook(String bookID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void markBookAsTaken(String bookID, String person, Calendar startDate, Optional<Calendar> endDate) {
		// TODO Auto-generated method stub

	}

	@Override
	public void markBookAsReturned(String bookID, Calendar dateReturned) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Book> searchBook(List<String> criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> findIfBookIsTaken(String bookID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> getNotReturnedBooks() {
		// TODO Auto-generated method stub
		return null;
	}

}
