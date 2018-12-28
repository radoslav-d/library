package com.sap.library.utilities.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.sap.library.utilities.exceptions.MessageNotSentException;

public class MessageDeliverer {

	private MessageDeliverer() {
		// Utility class constructor
	}

	public static void deliverMessage(ObjectOutputStream writer, Message message) {
		try {
			writer.writeObject(message);
		} catch (IOException e) {
			throw new MessageNotSentException(e);
		}
	}

	public static Message receiveMessage(ObjectInputStream reader) {
		try {
			return (Message) reader.readObject();
		} catch (ClassNotFoundException | IOException e) {
			throw new MessageNotSentException(e);
		}
	}
}
