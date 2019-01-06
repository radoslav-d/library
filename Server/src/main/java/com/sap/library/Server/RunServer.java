package com.sap.library.Server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class RunServer {

	private RunServer() {
		// utility class constructor
	}

	public static void main(String[] args) throws IOException, SQLException {
		Controller controller = new Controller(args[0]);
		controller.start();

		try (Scanner scanner = new Scanner(System.in)) {
			while (!scanner.next().equalsIgnoreCase("stop")) {
				controller.stop();
			}
		}

	}

}
