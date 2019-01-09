package com.sap.library.server.app;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import com.sap.library.server.Controller;

public class RunServer {

	private RunServer() {
		// utility class constructor
	}

	public static void main(String[] args) throws IOException, SQLException {

		if (args.length == 0) {
			throw new IllegalArgumentException("Need to set database URL");
		}

		Controller controller = new Controller(args[0]);
		controller.start();

		try (Scanner scanner = new Scanner(System.in)) {
			while (!scanner.next().equalsIgnoreCase("stop")) {
				// wait for user to enter stop
			}
			controller.stop();
		}
	}

}
