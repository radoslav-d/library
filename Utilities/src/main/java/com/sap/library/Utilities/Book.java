package com.sap.library.Utilities;

import java.util.Calendar;
import java.util.Optional;

public class Book {
	private String isbn;
	private String title;
	private String author;
	private int yearOfPublishing;
	private boolean isTaken;
	private Optional<String> takenBy;
	private Calendar takenOn;
	private Calendar returnedOn;
	
}
