package infoClasses;

import javax.swing.SwingConstants;

public class LocationDiff {

	private double xDiff;
	private double yDiff;

	// Should be one of the Cardinal Directions in SwingConstants
	private int diffDirection;

	public LocationDiff(double xDiff, double yDiff) {
		this.xDiff = xDiff;
		this.yDiff = yDiff;
	}

	public int getDiffDirection() {
		return diffDirection;
	}

	private String getToDoString() {
		double north = 0;
		double west = 0;
		double east = 0;
		double south = 0;

		if (xDiff > 0) {
			west = Math.abs(xDiff);
			diffDirection = SwingConstants.WEST;
		} else if (xDiff < 0) {
			east = Math.abs(xDiff);
			diffDirection = SwingConstants.EAST;
		}

		if (yDiff > 0) {
			south = Math.abs(yDiff);
			diffDirection = SwingConstants.SOUTH;
		} else if (yDiff < 0) {
			north = Math.abs(yDiff);
			diffDirection = SwingConstants.NORTH;
		}


		String diffMoveFormat = "Move %s: %.2f";
		String todoFormat = "<html>%s<br/>%s</html>";

		String todoHorizontalPart = "";
		String todoVerticalPart = "";

		if (west > 0) {
			todoHorizontalPart = String.format(diffMoveFormat, "Left", west);
		}

		if (east > 0) {
			todoHorizontalPart = String.format(diffMoveFormat, "Right", east);
		}

		if (north > 0) {
			todoVerticalPart = String.format(diffMoveFormat, "Up", north);
		}

		if (south > 0) {
			todoVerticalPart = String.format(diffMoveFormat, "Down", south);
		}

		return String.format(todoFormat, todoHorizontalPart, todoVerticalPart);
	}

	@Override
	public String toString() {
		return getToDoString();
	}
}
