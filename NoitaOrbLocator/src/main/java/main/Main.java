package main;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws IOException {
		try {
			log.info("Starting");
			MainWindow mainWindow = new MainWindow();
			mainWindow.setVisible(true);

		} catch (Exception e) {
			log.error("General Error: ", e);
		}
	}
}
