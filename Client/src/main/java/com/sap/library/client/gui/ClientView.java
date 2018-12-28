package com.sap.library.client.gui;

import java.util.Map;
import java.util.Optional;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ClientView extends Application {

	public static final String BASE_NAME = "client";

	@Override
	public void start(Stage primaryStage) throws Exception {
		LoginDialog dialog = new LoginDialog();
		Optional<Map<String, String>> result = dialog.getDialog().showAndWait();
		if (result.isPresent()) {
			for (String key : result.get().keySet()) {
				System.out.println(key + ": " + result.get().get(key));
			}
		}
		GridPane grid = new GridPane();
		primaryStage.setScene(new Scene(grid));
		primaryStage.titleProperty().bind(LocaleBinder.createStringBinding(BASE_NAME, "stage.title"));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
