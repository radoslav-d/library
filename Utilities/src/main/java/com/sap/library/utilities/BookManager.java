package com.sap.library.utilities;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface BookManager {

	void addBook(Book book);

	void deleteBook(String bookID);

	void markBookAsTaken(String bookID, String person, Calendar startDate, Optional<Calendar> endDate);

	void markBookAsReturned(String bookID, Calendar dateReturned);

	List<Book> searchBook(List<String> criteria);

	Optional<String> findIfBookIsTaken(String bookID);

	List<Book> getNotReturnedBooks();
}
