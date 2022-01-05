package main;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;

import controls.GreaterChestOrbLocationControl;
import events.OrbLocationChangedListener;
import events.PlayerFileChangedListener;
import infoClasses.OrbLocation;
import infoClasses.PlayerLocation;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9210653035202850478L;

	private PlayerFileWatcher playerFileWatcher = new PlayerFileWatcher();

	private JLabel playerlocationInfoLabel = new JLabel("PlayerLocation");
	private JLabel playerlocationLabel = new JLabel("No Location");
	private JLabel orblocationInfoLabel = new JLabel("OrbLocation");
	private JLabel orblocationLabel = new JLabel("Seek and you will find");

	private JLabel diffInfoLabel = new JLabel("Difference");
	private JLabel diffXLabel = new JLabel("No Difference for Now");
	private JLabel diffYLabel = new JLabel("No Difference for Now");

	private JLabel diffToDoLabel = new JLabel("Nothing to do for Now");

	private GreaterChestOrbLocationControl greaterChestOrbLocationControl;

	private PlayerLocation currentPlayerLocation;
	private OrbLocation currentOrbLocation;

	public MainWindow() throws IOException {

		this.setSize(450, 400);

		this.setLayout(new GridBagLayout());

		greaterChestOrbLocationControl = new GreaterChestOrbLocationControl(new OrbLocationChangedListener() {

			@Override
			public void onOrbLocationChanged(OrbLocation orbLocation) {
				currentOrbLocation = orbLocation;
				setOrbLocation();
				calculateLocationDifference();
			}

		});

		addComponent(diffToDoLabel, 0, 0);

		addComponent(diffInfoLabel, 2, 0);
		addComponent(diffXLabel, 3, 0);
		addComponent(diffYLabel, 3, 1);

		addComponent(playerlocationInfoLabel, 4, 0, 0.0, 0.0);
		addComponent(playerlocationLabel, 4, 1);

		addComponent(orblocationInfoLabel, 5, 0, 0.0, 0.0);
		addComponent(orblocationLabel, 5, 1);

		addComponent(greaterChestOrbLocationControl, 6, 0, GridBagConstraints.HORIZONTAL, 0.5, 0.2, 2);

		updatePlayerLocation();
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (playerFileWatcher != null) {
					try {
						playerFileWatcher.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				super.windowClosing(e);
			}
		});

		playerFileWatcher.init(this, new PlayerFileChangedListener() {

			@Override
			public void onPlayerFileChanged(File playerFilePath) {
				updatePlayerLocation();
			}
		});

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void setOrbLocation() {
		if (currentOrbLocation.isLocationValid()) {
			ThreadHelper.updateLabelText(orblocationLabel, currentOrbLocation.toString());
		} else {
			ThreadHelper.updateLabelText(orblocationLabel, "No Valid Location");
		}
	}

	private void updatePlayerLocation() {
		PlayerFile playerFile = new PlayerFile();
		if (playerFile.readFile()) {
			currentPlayerLocation = playerFile.getPlayerLocation();
			calculateLocationDifference();
			ThreadHelper.updateLabelText(playerlocationLabel, playerFile.getPlayerLocation().toString());
		} else {
			ThreadHelper.updateLabelText(playerlocationLabel, "Location can't be read");
		}
	}

	private void calculateLocationDifference() {
		if (currentPlayerLocation != null && currentPlayerLocation.isLocationValid() && currentOrbLocation != null
			&& currentOrbLocation.isLocationValid()) {
			double xDiff = currentPlayerLocation.getLocationX() - currentOrbLocation.getLocationX();
			double yDiff = currentPlayerLocation.getLocationY() - currentOrbLocation.getLocationY();

			ThreadHelper.updateLabelText(diffXLabel, Double.toString(xDiff));
			ThreadHelper.updateLabelText(diffYLabel, Double.toString(yDiff));

			String todo = "<html>";
			if (xDiff > 0) {
				todo += "Move Left: " + Math.abs(xDiff) + " Pixel";

			} else if (xDiff < 0) {
				todo += "Move Right: " + Math.abs(xDiff) + " Pixel";
			}

			if (todo.length() > 0) {
				todo += "<br>";
			}
			if (yDiff > 0) {
				todo += "Move Down: " + Math.abs(yDiff) + " Pixel";

			} else if (yDiff < 0) {
				todo += "Move Up: " + Math.abs(yDiff) + " Pixel";
			}

			if (todo.length() > 0) {
				todo += "</html>";
			} else {
				todo += "Hold still and keep sweating!";
			}

			ThreadHelper.updateLabelText(diffToDoLabel, todo);
		}
	}

	// TODO move to common base (abstract class?)
	private void addComponent(Component component, int row, int column, int fill, double weightX, double weightY) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = fill;
		c.weightx = weightX;
		c.weighty = weightY;
		c.gridx = column;
		c.gridy = row;
		this.add(component, c);
	}

	private void addComponent(Component component, int row, int column, int fill, double weightX, double weightY, int gridwith) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = gridwith;
		c.fill = fill;
		c.weightx = weightX;
		c.weighty = weightY;
		c.gridx = column;
		c.gridy = row;
		this.add(component, c);
	}

	private void addComponent(Component component, int row, int column) {
		addComponent(component, row, column, GridBagConstraints.HORIZONTAL);
	}

	private void addComponent(Component component, int row, int column, int fill) {
		addComponent(component, row, column, fill, 0.5, 0.2);
	}

	private void addComponent(Component component, int row, int column, double weightX, double weightY) {
		addComponent(component, row, column, GridBagConstraints.HORIZONTAL, weightX, weightY);
	}
}
