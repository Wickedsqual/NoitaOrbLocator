package main;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicArrowButton;

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

	private JPanel mainPanel = new JPanel();
	private JPanel infoPanel = new JPanel();
	private JPanel todoPanel = new JPanel();

	private JLabel playerlocationInfoLabel = new JLabel("PlayerLocation");
	private JLabel playerlocationLabel = new JLabel("No Location");
	private JLabel orblocationInfoLabel = new JLabel("OrbLocation");
	private JLabel orblocationLabel = new JLabel("Seek and you will find");

	private JLabel diffInfoLabel = new JLabel("Difference");
	private JLabel diffXLabel = new JLabel("No Difference for Now");
	private JLabel diffYLabel = new JLabel("No Difference for Now");

	private JLabel diffToDoLabel = new JLabel("Nothing to do for Now");
	private BasicArrowButton todoButton = new BasicArrowButton(0);

	private GreaterChestOrbLocationControl greaterChestOrbLocationControl;

	private PlayerLocation currentPlayerLocation;
	private OrbLocation currentOrbLocation;

	public MainWindow() throws IOException {

		this.setSize(450, 400);

		this.add(mainPanel);

		greaterChestOrbLocationControl = new GreaterChestOrbLocationControl(new OrbLocationChangedListener() {

			@Override
			public void onOrbLocationChanged(OrbLocation orbLocation) {
				currentOrbLocation = orbLocation;
				setOrbLocation();
				calculateLocationDifference();
			}

		});

		TitledBorder infoTitleBorder = BorderFactory.createTitledBorder("Infos");
		infoTitleBorder.setTitleColor(Color.blue);
		infoPanel.setBorder(infoTitleBorder);

		GridBagLayoutHelper infoPanelLayoutHelper = new GridBagLayoutHelper(infoPanel);

		System.out.println("InfoPanel");
		System.out.println("\r\nPlayerLocation");
		infoPanelLayoutHelper.addComponent(playerlocationInfoLabel, 0, 0, 0.0, 0.0);
		infoPanelLayoutHelper.addComponent(playerlocationLabel, 0, 1);

		System.out.println("\r\nOrbLocation");
		infoPanelLayoutHelper.addComponent(orblocationInfoLabel, 1, 0, 0.0, 0.0);
		infoPanelLayoutHelper.addComponent(orblocationLabel, 1, 1);


		System.out.println("\r\nDiffInfo");
		infoPanelLayoutHelper.addComponent(diffInfoLabel, 2, 0);

		System.out.println("\r\nDiffX and Y");
		infoPanelLayoutHelper.addComponent(diffXLabel, 3, 0);
		infoPanelLayoutHelper.addComponent(diffYLabel, 3, 1);


		GridBagLayoutHelper mainPanelLayoutHelper = new GridBagLayoutHelper(mainPanel);
		System.out.println("\r\nMainLayout");

		TitledBorder todoTitleBorder = BorderFactory.createTitledBorder("TODO:");
		todoTitleBorder.setTitleColor(Color.blue);
		todoPanel.setBorder(todoTitleBorder);
		todoPanel.add(diffToDoLabel);


		todoButton.setSize(50, 50);
		todoButton.setBackground(Color.white);
		todoPanel.add(todoButton);
		mainPanelLayoutHelper.addComponent(todoPanel, 0, 0);

		mainPanelLayoutHelper.addComponent(infoPanel, 1, 0);

		mainPanelLayoutHelper.addComponent(greaterChestOrbLocationControl, 2, 0, GridBagConstraints.HORIZONTAL, 0.5, 0.2, 2);

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


			DecimalFormat df = new DecimalFormat("####0.00");
			df.setRoundingMode(RoundingMode.HALF_DOWN);

			ThreadHelper.updateLabelText(diffXLabel, df.format(xDiff));
			ThreadHelper.updateLabelText(diffYLabel, df.format(yDiff));

			String todo = "<html>";
			if (xDiff > 0) {
				todo += "Move Left: " + df.format(Math.abs(xDiff)) + " Pixel";
				todoButton.setDirection(BasicArrowButton.WEST);

			} else if (xDiff < 0) {
				todo += "Move Right: " + df.format(Math.abs(xDiff)) + " Pixel";
				todoButton.setDirection(BasicArrowButton.EAST);
			}

			if (todo.length() > 0) {
				todo += "<br>";
			}
			if (yDiff > 0) {
				todo += "Move Down: " + df.format(Math.abs(yDiff)) + " Pixel";
				todoButton.setDirection(BasicArrowButton.SOUTH);

			} else if (yDiff < 0) {
				todo += "Move Up: " + df.format(Math.abs(yDiff)) + " Pixel";
				todoButton.setDirection(BasicArrowButton.NORTH);
			}

			if (todo.length() > 0) {
				todo += "</html>";
			} else {
				todo += "Hold still and keep sweating!";
			}

			ThreadHelper.updateLabelText(diffToDoLabel, todo);
		}
	}

}
