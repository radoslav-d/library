package com.sap.library.client.gui;

import java.io.File;

import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class LanguageButtons {

	private static final String EN_LANGUAGE_BUTTON_IMAGE_PATH = "src/main/resources/image-resources/en-lang.png";
	private static final String BG_LANGUAGE_BUTTON_IMAGE_PATH = "src/main/resources/image-resources/bg-lang.png";
	private static final String GE_LANGUAGE_BUTTON_IMAGE_PATH = "src/main/resources/image-resources/ge-lang.png";

	private LanguageButtons() {
		// Utility class constructor
	}

	public static Node getEnButton() {
		ImageView enLangButton = new ImageView(new File(EN_LANGUAGE_BUTTON_IMAGE_PATH).toURI().toString());
		enLangButton.setFitHeight(30);
		enLangButton.setFitWidth(30);
		// changes the text in the GUI depending on the clicked image view
		enLangButton.setOnMouseClicked(event -> LocaleBinder.setLocale(LocaleBinder.EN_LOCALE));
		return enLangButton;
	}

	public static Node getBgButton() {
		ImageView bgLangButton = new ImageView(new File(BG_LANGUAGE_BUTTON_IMAGE_PATH).toURI().toString());
		bgLangButton.setFitHeight(30);
		bgLangButton.setFitWidth(30);
		// changes the text in the GUI depending on the clicked image view
		bgLangButton.setOnMouseClicked(event -> LocaleBinder.setLocale(LocaleBinder.BG_LOCALE));
		return bgLangButton;
	}

	public static Node getGeButton() {
		ImageView geLangButton = new ImageView(new File(GE_LANGUAGE_BUTTON_IMAGE_PATH).toURI().toString());
		geLangButton.setFitHeight(30);
		geLangButton.setFitWidth(30);
		// changes the text in the GUI depending on the clicked image view
		geLangButton.setOnMouseClicked(event -> LocaleBinder.setLocale(LocaleBinder.GE_LOCALE));
		return geLangButton;
	}

}
