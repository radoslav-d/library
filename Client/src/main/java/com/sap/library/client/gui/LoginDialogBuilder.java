package com.sap.library.client.gui;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

/**
 * Constructs a Login Dialog, in which the user enters credentials as well as
 * network configs. The result of the Dialog is a map, where username, password,
 * port, host and type of authentication are mapped to the values the user has
 * entered.
 * 
 * @author Radoslav Dimitrov
 */
public class LoginDialogBuilder {

	private Dialog<Map<String, String>> dialog;

	private TextField usernameTextField;
	private PasswordField passwordTextField;
	private PasswordField repeatPasswordTextField;
	private Label passwordsNotEqual;
	private Button loginButtonNode;

	private ToggleGroup configRadioButtonsGroup;
	private ToggleGroup authenticationRadioButtonsGroup;

	private RadioButton defaultConfigButton;
	private RadioButton advancedConfigButton;

	private RadioButton loginRadioButton;
	private RadioButton registerRadioButton;

	private TextField hostTextField;
	private TextField portTextField;

	private Node enLangButton;
	private Node bgLangButton;

	/**
	 * Constructs the LoginDialog using fluent interface.
	 */
	public LoginDialogBuilder() {
		setDialogAndButtons().instantiateNodes().setUsernameTextField().setPasswordTextField().setConfigRadioButtons()
				.setAuthenticationRadioButtons().setAuthenticationInput().setAdvancedSettingsInput()
				.setLanguageChangeButtons().setGridPane().setDialogResultConverter();
	}

	public Dialog<Map<String, String>> getDialog() {
		return dialog;
	}

	private LoginDialogBuilder setDialogAndButtons() {
		dialog = new Dialog<>();
		dialog.titleProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "button.login"));
		dialog.getDialogPane().setPrefSize(800, 200);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		loginButtonNode = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
		loginButtonNode.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "button.login"));
		Button cancelBttnNode = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
		cancelBttnNode.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "button.cancel"));
		loginButtonNode.setDisable(true);
		return this;
	}

	private LoginDialogBuilder setUsernameTextField() {
		usernameTextField.promptTextProperty()
				.bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "username.prompt"));
		usernameTextField.textProperty().addListener((observable, oldValue, newValue) -> refreshLoginButtonStatus());
		return this;
	}

	private LoginDialogBuilder setPasswordTextField() {
		passwordTextField = new PasswordField();
		passwordTextField.promptTextProperty()
				.bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "password.prompt"));
		passwordTextField.textProperty().addListener((observable, oldValue, newValue) -> refreshLoginButtonStatus());
		return this;
	}

	private LoginDialogBuilder setConfigRadioButtons() {
		defaultConfigButton.textProperty()
				.bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "radiobutton.default"));
		defaultConfigButton.setToggleGroup(configRadioButtonsGroup);
		defaultConfigButton.setSelected(true);
		advancedConfigButton.textProperty()
				.bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "radiobutton.advanced"));
		advancedConfigButton.setToggleGroup(configRadioButtonsGroup);
		return this;
	}

	private LoginDialogBuilder setAuthenticationRadioButtons() {
		loginRadioButton.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "button.login"));
		loginRadioButton.setToggleGroup(authenticationRadioButtonsGroup);
		loginRadioButton.setSelected(true);
		registerRadioButton.textProperty()
				.bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "button.register"));
		registerRadioButton.setToggleGroup(authenticationRadioButtonsGroup);
		return this;
	}

	private LoginDialogBuilder setAuthenticationInput() {
		repeatPasswordTextField.setVisible(false);
		repeatPasswordTextField.promptTextProperty()
				.bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "repeatpassword.prompt"));
		passwordsNotEqual.setVisible(false);
		passwordsNotEqual.textProperty()
				.bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "passwordsnotequals.label"));
		repeatPasswordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			refreshLoginButtonStatus();
			passwordsNotEqual.setVisible(!passwordTextField.getText().equals(repeatPasswordTextField.getText()));
		});
		authenticationRadioButtonsGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
			boolean isRegister = newToggle.equals(registerRadioButton);
			repeatPasswordTextField.setVisible(isRegister);
			refreshLoginButtonStatus();
			refreshTextBindingsOfLoginButtonAndTitle(isRegister);
		});
		return this;
	}

	private LoginDialogBuilder setAdvancedSettingsInput() {
		setHostTextField();
		setPortTextField();
		// sets text fields visible depending on the chosen radio button
		configRadioButtonsGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
			boolean isVisible = newToggle.equals(advancedConfigButton);
			hostTextField.setVisible(isVisible);
			portTextField.setVisible(isVisible);
			refreshLoginButtonStatus();
		});
		return this;
	}

	private LoginDialogBuilder setHostTextField() {
		hostTextField.promptTextProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "host.prompt"));
		hostTextField.setVisible(false);
		hostTextField.textProperty().addListener((observable, oldValue, newValue) -> refreshLoginButtonStatus());
		return this;
	}

	private LoginDialogBuilder setPortTextField() {
		portTextField.promptTextProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, "port.prompt"));
		portTextField.setVisible(false);
		// permits non-digit input in the port text field
		portTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				portTextField.setText(newValue.replaceAll("[^\\d]", ""));
			}
			refreshLoginButtonStatus();
		});
		return this;
	}

	private LoginDialogBuilder setDialogResultConverter() {
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton.equals(ButtonType.OK)) {
				Map<String, String> results = new HashMap<>();
				results.put("username", usernameTextField.getText().trim());
				results.put("password", passwordTextField.getText().trim());
				if (advancedConfigButton.isSelected()) {
					results.put("host", hostTextField.getText().trim());
					results.put("port", portTextField.getText());
				} else {
					results.put("host", "");
					results.put("port", "");
				}
				if (loginRadioButton.isSelected()) {
					results.put("authentication", "login");
				} else {
					results.put("authentication", "register");
				}
				return results;
			}
			return null;
		});
		return this;
	}

	private LoginDialogBuilder setGridPane() {
		GridPane grid = new GridPane();
		dialog.getDialogPane().setContent(grid);
		grid.setVgap(20);
		grid.setHgap(20);
		grid.setPadding(new Insets(20, 10, 20, 40));
		grid.add(loginRadioButton, 0, 0);
		grid.add(registerRadioButton, 0, 0);
		GridPane.setHalignment(registerRadioButton, HPos.RIGHT);
		grid.add(usernameTextField, 0, 1);
		grid.add(passwordTextField, 0, 2);
		grid.add(repeatPasswordTextField, 0, 3);
		grid.add(passwordsNotEqual, 0, 4);
		grid.add(enLangButton, 6, 0);
		grid.add(bgLangButton, 6, 1);
		grid.add(defaultConfigButton, 3, 0);
		grid.add(advancedConfigButton, 3, 1);
		grid.add(hostTextField, 3, 2);
		grid.add(portTextField, 3, 3);
		return this;
	}

	private LoginDialogBuilder setLanguageChangeButtons() {
		enLangButton = LanguageButtons.getEnButton();
		bgLangButton = LanguageButtons.getBgButton();
		return this;
	}

	private LoginDialogBuilder instantiateNodes() {
		usernameTextField = new TextField();
		passwordTextField = new PasswordField();
		repeatPasswordTextField = new PasswordField();
		passwordsNotEqual = new Label();
		configRadioButtonsGroup = new ToggleGroup();
		authenticationRadioButtonsGroup = new ToggleGroup();
		defaultConfigButton = new RadioButton();
		advancedConfigButton = new RadioButton();
		loginRadioButton = new RadioButton();
		registerRadioButton = new RadioButton();
		hostTextField = new TextField();
		portTextField = new TextField();
		return this;
	}

	private void refreshLoginButtonStatus() {
		loginButtonNode.setDisable((advancedConfigButton.isSelected()
				&& (hostTextField.getText().trim().isEmpty() || portTextField.getText().trim().isEmpty()))
				|| usernameTextField.getText().trim().isEmpty() || passwordTextField.getText().trim().isEmpty()
				|| (registerRadioButton.isSelected()
						&& !passwordTextField.getText().equals(repeatPasswordTextField.getText())));
		passwordsNotEqual.setVisible(registerRadioButton.isSelected()
				&& !passwordTextField.getText().equals(repeatPasswordTextField.getText()));
	}

	private void refreshTextBindingsOfLoginButtonAndTitle(boolean register) {
		String resourceKey = register ? "button.register" : "button.login";
		loginButtonNode.textProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, resourceKey));
		dialog.titleProperty().bind(LocaleBinder.createStringBinding(ClientView.BASE_NAME, resourceKey));

	}

}
