package main;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicArrowButton;

public class ThreadHelper {


	public static void updateLabelText(JLabel label, String text) {
		invokeLater(() -> label.setText(text));
	}

	public static void updateJTextFieldText(JTextField textField, String text) {
		invokeLater(() -> textField.setText(text));
	}

	public static void setBasicButtonDirection(BasicArrowButton arrowButton, int direction) {
		invokeLater(() -> arrowButton.setDirection(direction));
	}

	private static void invokeLater(Runnable r) {
		SwingUtilities.invokeLater(r);
	}
}
