package com.sap.library.client.gui;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.library.client.BookRequestManager;
import com.sap.library.client.Controller;
import com.sap.library.utilities.exceptions.AuthenticationFailedException;
import com.sap.library.utilities.exceptions.ConnectionInterruptedException;
import com.sap.library.utilities.exceptions.MessageNotSentException;
import com.sap.library.utilities.exceptions.RegistrationFailedException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ClientView extends Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static final String BASE_NAME = "client";

	private Controller controller;

	private BookRequestManager bookManager;

	@Override
	public void start(Stage primaryStage) {
		try {
			if (!authenticate()) {
				Platform.exit();
			}
			Node bgLangButton = LanguageButtons.getBgButton();
			Node enLangButton = LanguageButtons.getEnButton();
			Node addBookForm = new AddBookFormBuilder(bookManager).getAddBookForm();
			Node searchBookForm = new SearchBookFormBuilder(bookManager).getSearchBookForm();
			GridPane grid = new GridPane();
			grid.add(searchBookForm, 0, 1);
			grid.add(addBookForm, 1, 1);
			grid.add(enLangButton, 2, 0);
			grid.add(bgLangButton, 3, 0);
			primaryStage.setScene(new Scene(grid));
			primaryStage.titleProperty().bind(LocaleBinder.createStringBinding(BASE_NAME, "stage.title"));
			primaryStage.setOnCloseRequest(event -> stop());
			primaryStage.show();
		} catch (ConnectionInterruptedException e) {
			errorAlert("", true);
		} catch (MessageNotSentException e) {
			LOGGER.error(e.getMessage(), e);
			errorAlert(e.getMessage(), false);
			Platform.exit();
		}
	}

	@Override
	public void stop() {
		try {
			if (controller != null) {
				controller.close();
			}
		} catch (IOException | MessageNotSentException e) {
			LOGGER.warn(e.getMessage());
		}
	}

	private boolean authenticate() {
		LoginDialog dialog = new LoginDialog();
		Optional<Map<String, String>> result = dialog.getDialog().showAndWait();
		if (!result.isPresent()) {
			return false;
		}
		Map<String, String> results = result.get();
		String username = results.get("username");
		String password = results.get("password");
		String host = results.get("password");
		String port = results.get("port");

		try {
			if (host.isEmpty() || port.isEmpty()) {
				controller = new Controller();
			} else {
				controller = new Controller(host, Integer.parseInt(port));
			}

			if (results.get("authentication").equals("register")) {
				controller.register(username, password);
			} else {
				controller.authenticate(username, password);
			}
			bookManager = controller.getBookManager();
			return true;
		} catch (AuthenticationFailedException | RegistrationFailedException | MessageNotSentException
				| IOException e) {
			LOGGER.error(e.getMessage(), e);
			errorAlert(e.getMessage(), false);
			return false;
		}
	}

	private void errorAlert(String errorMessage, boolean bindToLocale) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		if (bindToLocale) {
			alert.contentTextProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, errorMessage));
		} else {
			alert.setContentText(errorMessage);
		}
		alert.showAndWait();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
