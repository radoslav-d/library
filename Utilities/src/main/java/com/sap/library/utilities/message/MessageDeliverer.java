package com.sap.library.utilities.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.sap.library.utilities.exceptions.ConnectionInterruptedException;
import com.sap.library.utilities.exceptions.MessageNotSentException;
import com.sap.library.utilities.message.Message.MessageType;

/**
 * Utility class for sending and receiving messages
 * 
 * @author Radoslav Dimitrov
 *
 */
public class MessageDeliverer {

	private MessageDeliverer() {
		// Utility class constructor
	}

	/**
	 * Writes a message on the ObjectOutputStream
	 * 
	 * @throws MessageNotSentException
	 *             if an IOException occurs
	 */
	public static void deliverMessage(ObjectOutputStream writer, Message message) {
		try {
			writer.writeObject(message);
		} catch (IOException e) {
			throw new MessageNotSentException(e);
		}
	}

	/**
	 * Reads a message from the ObjectInputStream
	 * 
	 * @throws MessageNotSentException
	 *             if an IOException occurs
	 */
	public static Message receiveMessage(ObjectInputStream reader) {
		try {
			Message message = (Message) reader.readObject();
			if (message.getType().equals(MessageType.DISCONNECT_REQUEST)) {
				throw new ConnectionInterruptedException();
			}
			return message;
		} catch (ClassNotFoundException | IOException e) {
			throw new MessageNotSentException(e);
		}
	}
}
