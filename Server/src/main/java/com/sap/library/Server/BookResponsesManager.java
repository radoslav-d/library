package com.sap.library.Server;

import java.sql.Date;
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
	public void deleteBook(int bookId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void markBookAsTaken(int bookId, String person, Date startDate, Optional<Date> endDate) {
		// TODO Auto-generated method stub

	}

	@Override
	public void markBookAsReturned(int bookId, Date dateReturned) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Book> searchBook(List<String> criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> findIfBookIsTaken(int bookId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> getNotReturnedBooks() {
		// TODO Auto-generated method stub
		return null;
	}

}
