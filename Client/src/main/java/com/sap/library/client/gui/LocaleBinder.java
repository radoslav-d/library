package com.sap.library.client.gui;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Utility class, which creates bindings for different nodes text properties.
 * 
 * @author Radoslav Dimitrov
 */
public class LocaleBinder {

	public static final Locale BG_LOCALE = new Locale("bg", "BG");

	public static final Locale EN_LOCALE = new Locale("en", "EN");

	/**
	 * The current locale.
	 */
	private static final ObjectProperty<Locale> LOCALE;
	static {
		LOCALE = new SimpleObjectProperty<>(EN_LOCALE);
		LOCALE.addListener((observable, oldValue, newValue) -> Locale.setDefault(newValue));
	}

	private LocaleBinder() {
		// Utility class constructor.
	}

	public static List<Locale> getSupportedLocales() {
		return Arrays.asList(EN_LOCALE, BG_LOCALE);
	}

	public static Locale getDefaultLocale() {
		Locale sysDefault = Locale.getDefault();
		return getSupportedLocales().contains(sysDefault) ? sysDefault : EN_LOCALE;
	}

	public static void setLocale(Locale locale) {
		LOCALE.set(locale);
		Locale.setDefault(locale);
	}

	/**
	 * Gets the string with the given key from the resource bundle for the current
	 * locale and uses it as first argument to format a string.
	 * 
	 * @param baseName
	 *            - the base name of the resource bundle.
	 * @param key
	 *            the key
	 * @param args
	 *            - optional arguments for the message.
	 * @return the formatted string.
	 */
	public static String get(String baseName, String key, Object... args) {
		return String.format(ResourceBundle.getBundle(baseName, LOCALE.get()).getString(key), args);

	}

	/**
	 * Creates a String binding to a localized String for the given bundle key.
	 * 
	 * @param baseName
	 *            - the base name of the resource bundle.
	 * @param key
	 *            - the resource key.
	 * @param args
	 *            - optional formatting args.
	 * @return - the string binding.
	 */
	public static StringBinding createStringBinding(String baseName, String key, Object... args) {
		return Bindings.createStringBinding(() -> get(baseName, key, args), LOCALE);
	}

}
