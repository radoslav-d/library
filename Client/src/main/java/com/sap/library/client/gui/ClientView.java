package com.sap.library.client.gui;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sap.library.client.BookRequestManager;
import com.sap.library.client.Controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ClientView extends Application {

	public static final String BASE_NAME = "client";

	private List<Node> nodes;

	private Controller controller;

	private BookRequestManager bookManager;

	@Override
	public void start(Stage primaryStage) throws IOException {
		if (!authenticate()) {
			Platform.exit();
			return;
		}
		Node addBookForm = new AddBookFormBuilder(bookManager).getAddBookForm();
		GridPane grid = new GridPane();
		grid.add(addBookForm, 0, 0);
		primaryStage.setScene(new Scene(grid));
		primaryStage.titleProperty().bind(LocaleBinder.createStringBinding(BASE_NAME, "stage.title"));
		primaryStage.show();
	}

	private boolean authenticate() throws IOException {
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

		if (host.isEmpty() || port.isEmpty()) {
			// controller = new Controller();
		} else {
			// controller = new Controller(host, Integer.parseInt(port));
		}

		if (results.get("authentication").equals("register")) {
			// controller.register(username, password);
		} else {
			// controller.authenticate(username, password);
		}
		// bookManager = controller.getBookManager();
		// controller.start();
		return true;
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void hideNodes() {
		nodes.forEach(node -> node.setVisible(false));
	}

}
