package com.sap.library.utilities;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface BookManager {

	void addBook(Book book);

	void deleteBook(int bookId);

	void markBookAsTaken(int bookId, String person, Date startDate, Optional<Date> endDate);

	void markBookAsReturned(int bookId, Date dateReturned);

	List<Book> searchBook(List<String> criteria);

	Optional<String> findIfBookIsTaken(int bookId);

	List<Book> getNotReturnedBooks();
}
