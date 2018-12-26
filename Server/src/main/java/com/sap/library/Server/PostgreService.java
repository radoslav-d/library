package com.sap.library.Server;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sap.library.utilities.Book;

public class PostgreService {

	private static final String SELECT_BOOK_BY_ID_QUERY = "SELECT * FROM books WHERE id=";
	private static final String INSERT_BOOK = "INSERT INTO books (isbn, title, author, year, returnedOn, takenOn, isTaken, takenBy) VALUES ('%s', '%s', '%s', %d, false)";

	private Statement statement;
	private Connection connection;

	public PostgreService(String databaseUrl) throws SQLException {
		try {
			Class.forName("org.postgresql.Driver");
			createConnection(databaseUrl);
			statement = connection.createStatement();
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

	public void close() throws SQLException {
		statement.close();
		connection.close();
	}

	public Book getBookById(String bookId) throws SQLException {
		Book book = new Book();
		try (ResultSet result = statement.executeQuery(SELECT_BOOK_BY_ID_QUERY + bookId)) {
			if (result.next()) {
				book.setIsbn(result.getString("isbn"));
				book.setTitle(result.getString("title"));
				book.setAuthor(result.getString("author"));
				book.setYearOfPublishing(result.getInt("year"));
				// book.setReturnedOn(returnedOn);
				// book.setTaken(isTaken);
				// book.setTakenBy(takenBy);
				// book.setTakenOn(takenOn);
			}
		}
		return book;
	}

	public void addBook(Book book) throws SQLException {
		String query = String.format(INSERT_BOOK, book.getIsbn(), book.getTitle(), book.getAuthor(),
				book.getYearOfPublishing());
		statement.executeUpdate(query);
	}

}
