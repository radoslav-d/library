package com.sap.library.utilities;

import java.util.Calendar;
import java.util.Optional;

/**
 * Data Structure for Book.
 * 
 * @author Radoslav Dimitrov
 */
public class Book {

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

	public Book(String isbn, String title, String author, int yearOfPublishing, boolean isTaken,
			Optional<String> takenBy, Calendar takenOn, Calendar returnedOn) {
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.yearOfPublishing = yearOfPublishing;
		this.isTaken = isTaken;
		this.takenBy = takenBy;
		this.takenOn = takenOn;
		this.returnedOn = returnedOn;
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
