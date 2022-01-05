package main;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ThreadHelper {


	public static void updateLabelText(JLabel label, String text) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				label.setText(text);

			}
		});
	}

	public static void updateJTextFieldText(JTextField textField, String text) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				textField.setText(text);
			}
		});
	}
}
