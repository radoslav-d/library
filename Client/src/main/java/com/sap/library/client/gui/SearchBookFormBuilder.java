package com.sap.library.client.gui;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import com.sap.library.client.BookRequestManager;
import com.sap.library.utilities.Book;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SearchBookFormBuilder {

	private VBox vBox;

	private TextField searchField;

	private ListView<Book> searchResults;

	private HBox resultsHeader;

	private Button showTakenBooksButton;

	public SearchBookFormBuilder(BookRequestManager bookManager) {
		setSearchResults(bookManager).setSearchField(bookManager).setResultsHeader()
				.setShowTakenBooksButton(bookManager).setVBox();
	}

	public Node getSearchBookForm() {
		return vBox;
	}

	private SearchBookFormBuilder setSearchResults(BookRequestManager bookManager) {
		searchResults = new ListView<>();
		searchResults.setCellFactory(book -> new ListCell<Book>() {
			@Override
			protected void updateItem(Book item, boolean empty) {
				super.updateItem(item, empty);
				setGraphic(null);
				if (item != null && !empty) {
					HBox hBox = createRowAndAddChildNodes(item);
					hBox.getChildren().addAll(createDeleteButton(bookManager, item.getId(), getIndex()),
							createMarkBookButton(bookManager, item, getIndex()));
					hBox.setSpacing(30);
					this.setGraphic(hBox);
				}
			}
		});
		return this;
	}

	private HBox createRowAndAddChildNodes(Book book) {
		HBox hBox = new HBox();
		hBox.getChildren().addAll(formatAsTextNode(String.valueOf(book.getId()), 4),
				formatAsTextNode(book.getIsbn(), 10), formatAsTextNode(book.getTitle(), 30),
				formatAsTextNode(book.getAuthor(), 20), new Text(String.valueOf(book.getYearOfPublishing())));
		if (book.isTaken()) {
			hBox.getChildren().addAll(formatAsTextNode(book.getTakenBy(), 20), new Text(book.getTakenOn().toString()),
					new Text(book.getReturnedOn().toString()));
		} else {
			hBox.getChildren().addAll(getNotTakenText(), formatAsTextNode("", 10), formatAsTextNode("", 10));
		}
		return hBox;
	}

	private Button createDeleteButton(BookRequestManager bookManager, int bookId, int cellIndex) {
		Button deleteBookButton = new Button();
		deleteBookButton.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "button.delete"));
		deleteBookButton.setOnMouseClicked(event -> {
			bookManager.deleteBook(bookId);
			searchResults.getItems().remove(cellIndex);
		});
		return deleteBookButton;
	}

	private Button createMarkBookButton(BookRequestManager bookManager, Book book, int cellIndex) {
		Button markBookButton = new Button();
		String labelId = book.isTaken() ? "label.markreturned" : "label.marktaken";
		markBookButton.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, labelId));
		markBookButton.setOnMouseClicked(event -> {
			if (book.isTaken()) {
				bookManager.markBookAsReturned(book.getId(), new Date(System.currentTimeMillis()));
				book.setTaken(false);
				book.setReturnedOn(new Date(System.currentTimeMillis()));
				book.setTakenBy("");
				searchResults.getItems().remove(cellIndex);
				searchResults.getItems().add(book);
			} else {
				Optional<Book> takenBook = new MarkBookAsTakenDialog(book).getDialog().showAndWait();
				if (takenBook.isPresent()) {
					Book taken = takenBook.get();
					bookManager.markBookAsTaken(taken.getId(), taken.getTakenBy(), taken.getTakenOn(),
							taken.getReturnedOn());
					searchResults.getItems().remove(cellIndex);
					searchResults.getItems().add(taken);
				}
			}
		});
		return markBookButton;
	}

	private SearchBookFormBuilder setSearchField(BookRequestManager bookManager) {
		searchField = new TextField();
		searchField.promptTextProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "label.search"));
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			searchResults.getItems().clear();
			if (newValue.trim().isEmpty()) {
				return;
			}
			List<Book> results = bookManager.searchBook(newValue);
			results.forEach(book -> searchResults.getItems().add(book));
		});
		return this;
	}

	private SearchBookFormBuilder setResultsHeader() {
		resultsHeader = new HBox();
		Text id = new Text("ID");
		Text isbn = new Text("ISBN");
		Text title = new Text();
		title.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "label.title"));
		Text author = new Text();
		author.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "label.author"));
		Text year = new Text();
		year.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "label.year"));
		Text takenBy = new Text();
		takenBy.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "label.takenby"));
		Text takenOn = new Text();
		takenOn.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "label.takenon"));
		Text returnedOn = new Text();
		returnedOn.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "label.returnedon"));
		resultsHeader.getChildren().addAll(id, isbn, title, author, year, takenBy, takenOn, returnedOn);
		resultsHeader.setSpacing(70);
		resultsHeader.setPadding(new Insets(10));
		return this;
	}

	private SearchBookFormBuilder setShowTakenBooksButton(BookRequestManager bookManager) {
		showTakenBooksButton = new Button();
		showTakenBooksButton.textProperty()
				.bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "button.showTaken"));
		showTakenBooksButton.setOnMouseClicked(event -> {
			searchResults.getItems().clear();
			List<Book> results = bookManager.getNotReturnedBooks();
			results.forEach(book -> searchResults.getItems().add(book));
		});
		return this;
	}

	private SearchBookFormBuilder setVBox() {
		vBox = new VBox();
		vBox.getChildren().addAll(searchField, resultsHeader, searchResults, showTakenBooksButton);
		vBox.setPadding(new Insets(20));
		vBox.setSpacing(20);
		vBox.setMinWidth(1200);
		return this;
	}

	private Text formatAsTextNode(String text, int symbols) {
		int spacesToAdd = symbols - text.length();
		if (spacesToAdd > 0) {
			String formatted = String.format("%s%" + spacesToAdd + "s.", text, "");
			return new Text(formatted);
		}
		return new Text(text);
	}

	private Text getNotTakenText() {
		Text notTakenText = new Text();
		notTakenText.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "label.nottaken"));
		return notTakenText;
	}

}
