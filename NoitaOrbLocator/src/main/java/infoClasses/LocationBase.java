package infoClasses;


public abstract class LocationBase {

	protected Double locationX = null;
	protected Double locationY = null;

	public LocationBase(Double locationX, Double locationY) {
		this.locationX = locationX;
		this.locationY = locationY;
	}

	public Double getLocationX() {
		return locationX;
	}

	public Double getLocationY() {
		return locationY;
	}

	public boolean isLocationValid() {
		return locationX != null && locationY != null;
	}

	@Override
	public String toString() {
		if (locationX != null && locationY != null) {
			return String.format("X: %s Y: %s", locationX, locationY);
		} else {
			return "No Location set";
		}

	}
}
