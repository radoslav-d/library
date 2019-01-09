package com.sap.library.client.gui;

import java.sql.Date;
import java.time.LocalDate;

import com.sap.library.utilities.Book;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class MarkBookAsTakenDialog {
	private Dialog<Book> dialog;
	private Button confirmButton;
	private DatePicker dateTaken;
	private DatePicker dateReturned;
	private TextField userField;
	private Label takenLabel;
	private Label returnedLabel;

	public MarkBookAsTakenDialog(Book book) {
		setDialogAndButtons().setDatePickers().setUserField().setLabels().setGridPane().setResultConverter(book);
	}

	public Dialog<Book> getDialog() {
		return dialog;
	}

	private MarkBookAsTakenDialog setDialogAndButtons() {
		dialog = new Dialog<>();
		dialog.titleProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "label.marktaken"));
		dialog.getDialogPane().setPrefSize(800, 200);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		confirmButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
		confirmButton.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "button.confirm"));
		Button cancelBttnNode = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
		cancelBttnNode.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "button.cancel"));
		confirmButton.setDisable(true);
		return this;
	}

	private MarkBookAsTakenDialog setDatePickers() {
		dateTaken = new DatePicker();
		dateReturned = new DatePicker();
		dateTaken.setValue(LocalDate.now());
		dateReturned.setValue(dateTaken.getValue().plusMonths(1));
		dateTaken.valueProperty().addListener((observable, oldValue, newValue) -> {
			dateReturned.setValue(newValue.plusMonths(1));
		});
		return this;
	}

	private MarkBookAsTakenDialog setUserField() {
		userField = new TextField();
		userField.promptTextProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "label.user"));
		userField.textProperty()
				.addListener((observable, oldValue, newValue) -> confirmButton.setDisable(newValue.trim().isEmpty()));
		return this;
	}

	private MarkBookAsTakenDialog setLabels() {
		takenLabel = new Label();
		takenLabel.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "label.takenon"));
		returnedLabel = new Label();
		returnedLabel.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "label.returnedon"));
		return this;
	}

	private MarkBookAsTakenDialog setGridPane() {
		GridPane gridPane = new GridPane();
		dialog.getDialogPane().setContent(gridPane);
		gridPane.setVgap(20);
		gridPane.setHgap(20);
		gridPane.setPadding(new Insets(20, 10, 20, 40));
		gridPane.add(userField, 0, 1);
		gridPane.add(dateTaken, 1, 1);
		gridPane.add(dateReturned, 2, 1);
		gridPane.add(takenLabel, 1, 0);
		gridPane.add(returnedLabel, 2, 0);
		return this;
	}

	private MarkBookAsTakenDialog setResultConverter(Book book) {
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton.equals(ButtonType.OK)) {
				book.markBookAsTaken(userField.getText().trim(), new Date(dateTaken.getValue().toEpochDay() * 86400000),
						new Date(dateReturned.getValue().toEpochDay() * 86400000));
				return book;
			}
			return null;
		});
		return this;
	}

}
