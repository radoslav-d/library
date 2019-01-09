package com.sap.library.server.database;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sap.library.utilities.Book;

public class PostgreService implements DataBaseService {

	private static final String INSERT_BOOK = "INSERT INTO books (isbn, title, author, year, isTaken) VALUES (?, ?, ?, ?, false)";
	private static final String DELETE_BOOK_BY_ID = "DELETE FROM books WHERE id=?";
	private static final String MARK_BOOK_AS_TAKEN = "UPDATE books SET isTaken=true, takenBy=?, takenOn=?, returnedOn=? WHERE id=?";
	private static final String MARK_BOOK_AS_RETURNED = "UPDATE books SET isTaken=false, takenBy=NULL, returnedOn=? WHERE id=?";
	private static final String SEARCH_BOOK = "SELECT * FROM books WHERE isbn LIKE ? OR title LIKE ? OR author LIKE ? OR year=? OR id=?";
	private static final String SEARCH_BOOK_WITH_ALPHANUMERICS = "SELECT * FROM books WHERE isbn LIKE ? OR title LIKE ? OR author LIKE ?";
	private static final String FIND_TAKEN_BOOKS = "SELECT * FROM books WHERE isTaken=true";

	private Connection connection;

	public PostgreService(String databaseUrl) throws SQLException {
		try {
			Class.forName("org.postgresql.Driver");
			createConnection(databaseUrl);
		} catch (ClassNotFoundException | URISyntaxException e) {
			throw new SQLException(e);
		}
	}

	private void createConnection(String databaseUrl) throws URISyntaxException, SQLException {
		URI uri = new URI(databaseUrl);
		String dbUrl = "jdbc:postgresql://" + uri.getHost() + ':' + uri.getPort() + uri.getPath();
		String[] credetentials = databaseUrl.substring(11).split("@")[0].split(":");
		connection = DriverManager.getConnection(dbUrl, credetentials[0], credetentials[1]);
	}

	public Connection getConnection() {
		return this.connection;
	}

	public void close() throws SQLException {
		connection.close();
	}

	public synchronized void addBook(Book book) throws SQLException {
		try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BOOK)) {
			preparedStatement.setString(1, book.getIsbn());
			preparedStatement.setString(2, book.getTitle());
			preparedStatement.setString(3, book.getAuthor());
			preparedStatement.setString(4, String.valueOf(book.getYearOfPublishing()));
			preparedStatement.executeUpdate();
		}
	}

	public synchronized void deleteBook(int bookId) throws SQLException {
		try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BOOK_BY_ID)) {
			preparedStatement.setInt(1, bookId);
			preparedStatement.executeUpdate();
		}
	}

	public synchronized void markBookAsTaken(int bookId, String person, Date startDate, Date endDate) throws SQLException {
		try (PreparedStatement preparedStatement = connection.prepareStatement(MARK_BOOK_AS_TAKEN)) {
			preparedStatement.setString(1, person);
			preparedStatement.setDate(2, startDate);
			preparedStatement.setDate(3, endDate);
			preparedStatement.setInt(4, bookId);
			preparedStatement.executeUpdate();
		}
	}

	public synchronized void markBookAsReturned(int bookId, Date returnDate) throws SQLException {
		try (PreparedStatement preparedStatement = connection.prepareStatement(MARK_BOOK_AS_RETURNED)) {
			preparedStatement.setDate(1, returnDate);
			preparedStatement.setInt(2, bookId);
			preparedStatement.executeUpdate();
		}
	}

	public synchronized List<Book> searchBook(String criteria) throws SQLException {
		if (criteria.matches("^[0-9]*$") && criteria.length() < 7) {
			return searchWithOutAlphaNumericalCriteria(criteria);
		}
		return searchWithAlphaNumericalCriteria(criteria);
	}

	private List<Book> searchWithAlphaNumericalCriteria(String criteria) throws SQLException {
		try (PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_BOOK_WITH_ALPHANUMERICS)) {
			String wildcardCriteria = "%" + criteria + "%";
			preparedStatement.setString(1, wildcardCriteria);
			preparedStatement.setString(2, wildcardCriteria);
			preparedStatement.setString(3, wildcardCriteria);
			try (ResultSet results = preparedStatement.executeQuery()) {
				return toListOfBooks(results);
			}

		}
	}

	private List<Book> searchWithOutAlphaNumericalCriteria(String criteria) throws SQLException {
		try (PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_BOOK)) {
			String wildcardCriteria = "%" + criteria + "%";
			preparedStatement.setString(1, wildcardCriteria);
			preparedStatement.setString(2, wildcardCriteria);
			preparedStatement.setString(3, wildcardCriteria);
			preparedStatement.setString(4, criteria);
			preparedStatement.setInt(5, Integer.parseInt(criteria));
			try (ResultSet results = preparedStatement.executeQuery()) {
				return toListOfBooks(results);
			}

		}
	}

	public synchronized List<Book> getNotReturnedBooks() throws SQLException {
		try (Statement statement = connection.createStatement();
				ResultSet results = statement.executeQuery(FIND_TAKEN_BOOKS)) {
			return toListOfBooks(results);
		}
	}

	private List<Book> toListOfBooks(ResultSet result) throws SQLException {
		List<Book> books = new ArrayList<>();
		while (result.next()) {
			Book book = new Book();
			book.setId(result.getInt("id"));
			book.setIsbn(result.getString("isbn"));
			book.setTitle(result.getString("title"));
			book.setAuthor(result.getString("author"));
			book.setYearOfPublishing(Integer.parseInt(result.getString("year")));
			book.setTaken(result.getBoolean("isTaken"));
			book.setTakenBy(result.getString("takenBy"));
			book.setTakenOn(result.getDate("takenOn"));
			book.setReturnedOn(result.getDate("returnedOn"));
			books.add(book);
		}
		return books;
	}

}
