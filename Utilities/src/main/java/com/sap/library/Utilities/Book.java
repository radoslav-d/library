package com.sap.library.utilities;

import java.sql.Date;
import java.util.Optional;

/**
 * Data Structure for Book.
 * 
 * @author Radoslav Dimitrov
 */
public class Book {

	private int id;
	private String isbn;
	private String title;
	private String author;
	private int yearOfPublishing;
	private boolean isTaken;
	private Optional<String> takenBy;
	private Date takenOn;
	private Date returnedOn;

	public Book() {
		// default constructor
	}

	public Book(int id, String isbn, String title, String author, int yearOfPublishing, boolean isTaken,
			Optional<String> takenBy, Date takenOn, Date returnedOn) {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public Date getTakenOn() {
		return takenOn;
	}

	public void setTakenOn(Date takenOn) {
		this.takenOn = takenOn;
	}

	public Date getReturnedOn() {
		return returnedOn;
	}

	public void setReturnedOn(Date returnedOn) {
		this.returnedOn = returnedOn;
	}

}
