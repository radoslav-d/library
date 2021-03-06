package com.sap.library.server;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import com.sap.library.server.database.DataBaseException;
import com.sap.library.server.database.PostgreService;
import com.sap.library.utilities.Book;
import com.sap.library.utilities.BookManager;

public class BookResponsesManager implements BookManager {

	private PostgreService postgreService;

	public BookResponsesManager(PostgreService postgreService) {
		this.postgreService = postgreService;
	}

	@Override
	public void addBook(Book book) {
		try {
			postgreService.addBook(book);
		} catch (SQLException e) {
			throw new DataBaseException(e);
		}
	}

	@Override
	public void deleteBook(int bookId) {
		try {
			postgreService.deleteBook(bookId);
		} catch (SQLException e) {
			throw new DataBaseException(e);
		}

	}

	@Override
	public void markBookAsTaken(int bookId, String person, Date startDate, Date endDate) {
		try {
			postgreService.markBookAsTaken(bookId, person, startDate, endDate);
		} catch (SQLException e) {
			throw new DataBaseException(e);
		}
	}

	@Override
	public void markBookAsReturned(int bookId, Date dateReturned) {
		try {
			postgreService.markBookAsReturned(bookId, dateReturned);
		} catch (SQLException e) {
			throw new DataBaseException(e);
		}
	}

	@Override
	public List<Book> searchBook(String criteria) {
		try {
			return postgreService.searchBook(criteria);
		} catch (SQLException e) {
			throw new DataBaseException(e);
		}
	}

	@Override
	public List<Book> getNotReturnedBooks() {
		try {
			return postgreService.getNotReturnedBooks();
		} catch (SQLException e) {
			throw new DataBaseException(e);
		}
	}

}
