package com.sap.library.utilities.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.sap.library.utilities.exceptions.MessageNotSentException;

/**
 * Writes a message on ObjectOutputStream in a new Thread.
 * 
 * @author Radoslav Dimitrov
 */
public class MessageDeliverer implements Runnable {

	private ObjectOutputStream writer;
	private Message message;

	private MessageDeliverer(ObjectOutputStream writer, Message message) {
		this.writer = writer;
		this.message = message;
		new Thread(this).start();
	}

	public static void deliverMessage(ObjectOutputStream writer, Message message) {
		new MessageDeliverer(writer, message);
	}

	public static Message receiveMessage(ObjectInputStream reader) {
		try {
			return (Message) reader.readObject();
		} catch (ClassNotFoundException | IOException e) {
			throw new MessageNotSentException(e);
		}
	}

	public synchronized void run() {
		try {
			writer.writeObject(message);
		} catch (IOException e) {
			throw new MessageNotSentException(e);
		}
	}

}
