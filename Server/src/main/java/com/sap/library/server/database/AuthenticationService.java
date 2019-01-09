package com.sap.library.server.database;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sap.library.utilities.PasswordUtils;
import com.sap.library.utilities.exceptions.AuthenticationFailedException;
import com.sap.library.utilities.exceptions.RegistrationFailedException;

public class AuthenticationService implements DataBaseService {

	private static final String SELECT_USER = "SELECT * FROM users WHERE username=?";
	private static final String INSERT_NEW_USER = "INSERT INTO users (username, hash) VALUES (?, ?)";

	private Connection connection;

	public AuthenticationService(Connection connection) {
		this.connection = connection;
	}

	public void authenticateUser(String username, String password) {
		try (ResultSet result = fetchUser(username)) {
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
		try (ResultSet result = fetchUser(username)) {
			if (result.next()) {
				throw new RegistrationFailedException("There is already an user with the same username");
			}
		} catch (SQLException e) {
			throw new RegistrationFailedException(e);
		}
		try (PreparedStatement prepStatement = connection.prepareStatement(INSERT_NEW_USER)) {
			String hash = PasswordUtils.getSaltedHash(password);
			prepStatement.setString(1, username);
			prepStatement.setString(2, hash);
			prepStatement.executeUpdate();
		} catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RegistrationFailedException(e);
		}
	}

	private ResultSet fetchUser(String username) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER);
		preparedStatement.setString(1, username);
		return preparedStatement.executeQuery();
	}

}
