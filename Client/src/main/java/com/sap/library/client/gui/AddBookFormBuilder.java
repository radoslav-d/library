package com.sap.library.client.gui;

import com.sap.library.client.BookRequestManager;
import com.sap.library.utilities.Book;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AddBookFormBuilder {

	private Label addBookLabel;
	private TextField isbnField;
	private TextField titleField;
	private TextField authorField;
	private TextField yearField;
	private Button addBookButton;
	private VBox vBox;

	public AddBookFormBuilder(BookRequestManager bookManager) {
		instantiateNodes().bindTextProperties().setAddBookButton(bookManager).setYearFieldSanitation().addNodesToVBox();
	}

	public Node getAddBookForm() {
		return vBox;
	}

	private AddBookFormBuilder instantiateNodes() {
		addBookLabel = new Label();
		isbnField = new TextField();
		titleField = new TextField();
		authorField = new TextField();
		yearField = new TextField();
		addBookButton = new Button();
		vBox = new VBox();
		return this;
	}

	private AddBookFormBuilder bindTextProperties() {
		addBookLabel.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "stage.title"));
		isbnField.promptTextProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "stage.title"));
		titleField.promptTextProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "stage.title"));
		authorField.promptTextProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "stage.title"));
		yearField.promptTextProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "stage.title"));
		addBookButton.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "stage.title"));
		return this;
	}

	private AddBookFormBuilder setAddBookButton(BookRequestManager bookManager) {
		addBookButton.setOnMouseClicked(event -> {
			Book book = new Book();
			book.setIsbn(isbnField.getText().trim());
			book.setTitle(titleField.getText().trim());
			book.setAuthor(authorField.getText().trim());
			book.setYearOfPublishing(Integer.parseInt(yearField.getText()));
			book.setTaken(false);
			bookManager.addBook(book);
			isbnField.clear();
			titleField.clear();
			authorField.clear();
			yearField.clear();
		});

		addBookButton.disableProperty().bind(Bindings.createBooleanBinding(() -> {
			return isbnField.getText().trim().isEmpty() || titleField.getText().trim().isEmpty()
					|| authorField.getText().trim().isEmpty() || yearField.getText().trim().isEmpty();
		}, isbnField.textProperty(), titleField.textProperty(), authorField.textProperty(), yearField.textProperty()));

		return this;
	}

	private AddBookFormBuilder setYearFieldSanitation() {
		yearField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				yearField.setText(newValue.replaceAll("[^\\d]", ""));
			}
			if (newValue.length() > 4) {
				yearField.setText(newValue.substring(0, 4));
			}
		});
		return this;
	}

	private AddBookFormBuilder addNodesToVBox() {
		vBox.getChildren().addAll(addBookLabel, isbnField, titleField, authorField, yearField, addBookButton);
		return this;
	}

}
