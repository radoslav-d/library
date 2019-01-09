package com.sap.library.client.gui;

import javafx.scene.control.Alert;

public class ErrorAlert implements Runnable {

	private String errorMessage;
	private boolean bindToLocale;

	public ErrorAlert(String errorMessage, boolean bindToLocale) {
		this.errorMessage = errorMessage;
		this.bindToLocale = bindToLocale;
	}

	@Override
	public void run() {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		if (bindToLocale) {
			alert.contentTextProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, errorMessage));
		} else {
			alert.setContentText(errorMessage);
		}
		alert.showAndWait();
	}

}
