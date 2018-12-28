package com.sap.library.Server;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import com.sap.library.utilities.Book;
import com.sap.library.utilities.PasswordUtils;
import com.sap.library.utilities.exceptions.AuthenticationFailedException;
import com.sap.library.utilities.exceptions.RegistrationFailedException;

public class PostgreService {

	private static final String SELECT_BOOK_BY_ID_QUERY = "SELECT * FROM books WHERE id=";
	private static final String SELECT_USER = "SELECT * FROM users WHERE username=";
	private static final String INSERT_BOOK = "INSERT INTO books (isbn, title, author, year, returnedOn, takenOn, isTaken, takenBy) VALUES ('%s', '%s', '%s', %d, false)";
	private static final String INSERT_NEW_USER = "INSERT INTO users (username, hash) VALUES (%s, %s)";
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

	public void authenticate(String username, String password) {
		try (ResultSet result = statement.executeQuery(SELECT_USER + username)) {
			if (!result.next()) {
				throw new AuthenticationFailedException("There is no such user!");
			}
			String hash = result.getString("hash");
			if (!PasswordUtils.check(password, hash)) {
				throw new AuthenticationFailedException("The password does not match!");
			}
		} catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new AuthenticationFailedException(e);
		}
	}

	public void registerUser(String username, String password) {
		try (ResultSet result = statement.executeQuery(SELECT_USER + username)) {
			if (result.next()) {
				throw new RegistrationFailedException("There is already an user with the same username");
			}
			String hash = PasswordUtils.getSaltedHash(password);
			statement.executeUpdate(String.format(INSERT_NEW_USER, username, hash));
		} catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RegistrationFailedException(e);
		}
	}

	public Optional<Book> getBookById(String bookId) throws SQLException {
		Book book = new Book();
		try (ResultSet result = statement.executeQuery(SELECT_BOOK_BY_ID_QUERY + bookId)) {
			if (result.next()) {
				book.setIsbn(result.getString("isbn"));
				book.setTitle(result.getString("title"));
				book.setAuthor(result.getString("author"));
				book.setYearOfPublishing(result.getInt("year"));
				book.setReturnedOn(result.getDate("returnedOn"));
				book.setTaken(result.getBoolean("isTaken"));
				book.setTakenBy(Optional.ofNullable(result.getString("takenBy")));
				book.setTakenOn(result.getDate("takenOn"));
				return Optional.of(book);
			}
		}
		return Optional.empty();
	}

	public void addBook(Book book) throws SQLException {
		String query = String.format(INSERT_BOOK, book.getIsbn(), book.getTitle(), book.getAuthor(),
				book.getYearOfPublishing());
		statement.executeUpdate(query);
	}

	public void deleteBook(String bookId) throws SQLException {
		statement.executeUpdate("DELETE FROM books WHERE id=" + bookId);
	}

}
