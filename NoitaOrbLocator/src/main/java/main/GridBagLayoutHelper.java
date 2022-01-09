package main;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GridBagLayoutHelper {

	private Container parent;
	private GridBagLayout gridBagLayout = new GridBagLayout();

	private static final Logger log = LoggerFactory.getLogger(GridBagLayoutHelper.class);

	public GridBagLayoutHelper(Container parent) {
		this.parent = parent;
		parent.setLayout(gridBagLayout);
	}

	public void addComponent(Component child, int row, int column, int fill, double weightX, double weightY, int gridwith) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = gridwith;
		c.fill = fill;
		c.weightx = weightX;
		c.weighty = weightY;
		c.gridx = column;
		c.gridy = row;

		log.debug(String.format("Add to row: %s, col: %s", row, column));

		parent.add(child, c);
	}

	public void addComponent(Component child, int row, int column, int fill, double weightX, double weightY) {
		addComponent(child, row, column, fill, weightX, weightY, 1);
	}

	public void addComponent(Component child, int row, int column) {
		addComponent(child, row, column, GridBagConstraints.HORIZONTAL);
	}

	public void addComponent(Component child, int row, int column, int fill) {
		addComponent(child, row, column, fill, 0.5, 0.2);
	}

	public void addComponent(Component child, int row, int column, double weightX, double weightY) {
		addComponent(child, row, column, GridBagConstraints.HORIZONTAL, weightX, weightY);
	}


	// Not working correct, because the gridBagLayout.getLayoutDimensions() doesnt see to give the correct values every time

	// private int getLayoutRows() {
	// int[][] dim = gridBagLayout.getLayoutDimensions();
	// return dim[1].length;
	// }
	//
	// private int getLayoutColumns() {
	// int[][] dim = gridBagLayout.getLayoutDimensions();
	// return dim[0].length;
	// }

	// public void addComponentToNewRow(Component child) {
	// addComponentToNewRow(child, 0, 0.5, 0.5);
	// }

	// public void addComponentToNewRow(Component child, int column, double weightX, double weightY) {
	// addComponentToNewRow(child, column, GridBagConstraints.HORIZONTAL, weightX, weightY);
	// }
	//
	// public void addComponentToNewRow(Component child, int column, int fill, double weightX, double weightY) {
	// int row = parent.getComponentCount() == 0 ? 0 : getLayoutRows() + 1;
	// log.debug("newRow: " + row);
	// addComponent(child, row, column, fill, weightX, weightY);
	// }
	//
	// public void addComponentToLastRow(Component child, int column) {
	// log.debug("lastRow: " + getLayoutRows());
	// addComponent(child, getLayoutRows(), column, GridBagConstraints.HORIZONTAL, 0.5, 0.5);
	// }

}
