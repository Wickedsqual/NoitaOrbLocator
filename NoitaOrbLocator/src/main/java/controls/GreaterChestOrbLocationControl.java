package controls;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.OrbLocationChangedListener;
import greaterOrbFinder.GreaterOrbFinderWrapper;
import infoClasses.OrbLocation;
import main.GridBagLayoutHelper;
import main.StatsFile;
import main.ThreadHelper;

public class GreaterChestOrbLocationControl extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 400794653835139424L;

	private JLabel seedLabel = new JLabel("Seed");
	private JLabel newGameLabel = new JLabel("NG+");
	private JLabel xStartLabel = new JLabel("x");
	private JLabel yStartLabel = new JLabel("y");

	private ImprovedFormattedTextField seedTextArea = new ImprovedFormattedTextField(NumberFormat.getIntegerInstance(), 0);
	private ImprovedFormattedTextField newGameTextArea = new ImprovedFormattedTextField(NumberFormat.getIntegerInstance(), 0);
	private ImprovedFormattedTextField xStartTextArea = new ImprovedFormattedTextField(NumberFormat.getNumberInstance(), 0);
	private ImprovedFormattedTextField yStartTextArea = new ImprovedFormattedTextField(NumberFormat.getNumberInstance(), 0);

	private JButton findOrbButton = new JButton("Seek True Knowledge");

	private static final Logger log = LoggerFactory.getLogger(GreaterChestOrbLocationControl.class);

	public GreaterChestOrbLocationControl(OrbLocationChangedListener orbLocationChangedListener) {

		GridBagLayoutHelper layoutHelper = new GridBagLayoutHelper(this);

		TitledBorder infoTitleBorder = BorderFactory.createTitledBorder("GreaterOrbLocation");
		infoTitleBorder.setTitleColor(Color.blue);
		this.setBorder(infoTitleBorder);

		this.setLayout(new GridBagLayout());

		layoutHelper.addComponent(seedLabel, 0, 0);
		layoutHelper.addComponent(seedTextArea, 0, 1);

		layoutHelper.addComponent(newGameLabel, 1, 0);
		layoutHelper.addComponent(newGameTextArea, 1, 1);

		layoutHelper.addComponent(xStartLabel, 2, 0);
		layoutHelper.addComponent(xStartTextArea, 2, 1);

		layoutHelper.addComponent(yStartLabel, 3, 0);
		layoutHelper.addComponent(yStartTextArea, 3, 1);

		layoutHelper.addComponent(findOrbButton, 4, 0);

		addOrbLocationChangedListener(orbLocationChangedListener);

		addButtonListener();

		updateWorldSeed();
	}

	private void updateWorldSeed() {
		StatsFile statsFile = new StatsFile();
		if (statsFile.readFile()) {
			int seed = statsFile.getWorldSeed();
			ThreadHelper.updateJTextFieldText(seedTextArea, Integer.toString(seed));
			findOrbLocation(seed, 0, 0, 0);
		}
	}

	private void addButtonListener() {
		findOrbButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {

					int seed = NumberFormat.getIntegerInstance().parse(seedTextArea.getText()).intValue();
					int newGame = NumberFormat.getIntegerInstance().parse(newGameTextArea.getText()).intValue();

					double x = NumberFormat.getNumberInstance().parse(xStartTextArea.getText()).doubleValue();
					double y = NumberFormat.getNumberInstance().parse(yStartTextArea.getText()).doubleValue();

					findOrbLocation(seed, newGame, x, y);
				} catch (Exception e1) {
					log.error("Error while Parsing OrbFinder input: ", e1);
				}
			}
		});
	}

	public void addOrbLocationChangedListener(OrbLocationChangedListener orbLocationChangedListener) {
		listenerList.add(OrbLocationChangedListener.class, orbLocationChangedListener);
	}

	public void removeOrbLocationChangedListener(OrbLocationChangedListener orbLocationChangedListener) {
		listenerList.remove(OrbLocationChangedListener.class, orbLocationChangedListener);
	}

	private void findOrbLocation(int seed, int newGame, double xStartPosition, double yStartPosition) {
		GreaterOrbFinderWrapper orbFinder = new GreaterOrbFinderWrapper();

		OrbLocation orbLocation = orbFinder.find(seed, newGame, xStartPosition, yStartPosition);

		fireOrbLocationChangedEvent(orbLocation);
	}

	private void fireOrbLocationChangedEvent(OrbLocation orbLocation) {
		Object[] listeners = listenerList.getListenerList();

		for (Object listener : listeners) {
			if (listener instanceof OrbLocationChangedListener) {
				((OrbLocationChangedListener) listener).onOrbLocationChanged(orbLocation);
			}
		}
	}

	private void addComponent(Component component, int row, int column, int fill) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = fill;
		c.weightx = 0.5;
		c.weighty = 0.2;
		c.gridx = column;
		c.gridy = row;
		this.add(component, c);
	}
}
