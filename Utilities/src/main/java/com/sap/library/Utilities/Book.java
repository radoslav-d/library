package com.sap.library.utilities;

import java.util.Calendar;
import java.util.Optional;

/**
 * Data Structure for Book.
 * 
 * @author Radoslav Dimitrov
 */
public class Book {

	private String id;
	private String isbn;
	private String title;
	private String author;
	private int yearOfPublishing;
	private boolean isTaken;
	private Optional<String> takenBy;
	private Calendar takenOn;
	private Calendar returnedOn;

	public Book() {
		// default constructor
	}

	public Book(String id, String isbn, String title, String author, int yearOfPublishing, boolean isTaken,
			Optional<String> takenBy, Calendar takenOn, Calendar returnedOn) {
		setId(id);
		setIsbn(isbn);
		setTitle(title);
		setAuthor(author);
		setYearOfPublishing(yearOfPublishing);
		setTaken(isTaken);
		setTakenBy(takenBy);
		setTakenOn(takenOn);
		setReturnedOn(returnedOn);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getYearOfPublishing() {
		return yearOfPublishing;
	}

	public void setYearOfPublishing(int yearOfPublishing) {
		this.yearOfPublishing = yearOfPublishing;
	}

	public boolean isTaken() {
		return isTaken;
	}

	public void setTaken(boolean isTaken) {
		this.isTaken = isTaken;
	}

	public Optional<String> getTakenBy() {
		return takenBy;
	}

	public void setTakenBy(Optional<String> takenBy) {
		this.takenBy = takenBy;
	}

	public Calendar getTakenOn() {
		return takenOn;
	}

	public void setTakenOn(Calendar takenOn) {
		this.takenOn = takenOn;
	}

	public Calendar getReturnedOn() {
		return returnedOn;
	}

	public void setReturnedOn(Calendar returnedOn) {
		this.returnedOn = returnedOn;
	}

}
