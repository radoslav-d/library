package com.sap.library.utilities;

import java.sql.Date;
import java.util.List;

public interface BookManager {

	void addBook(Book book);

	void deleteBook(int bookId);

	void markBookAsTaken(int bookId, String person, Date startDate, Date endDate);

	void markBookAsReturned(int bookId, Date dateReturned);

	List<Book> searchBook(String criteria);

	List<Book> getNotReturnedBooks();
}
