package com.sap.library.client.app;

import com.sap.library.client.gui.ClientView;

import javafx.application.Application;

/**
 * Starts the client application
 * 
 * @author Radoslav Dimitrov
 */
public class Run {

	public static void main(String[] args) {
		Application.launch(ClientView.class, args);
	}

}
