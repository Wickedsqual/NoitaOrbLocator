package infoClasses;


public class OrbLocation extends LocationBase {

	private final String xEquals = "x = ";
	private final String yEquals = "y = ";

	public OrbLocation(String locationString) {
		super(null, null);

		if (isValidLocation(locationString)) {
			setLocation(locationString);
		}
	}

	private void setLocation(String locationString) {
		int xindex = locationString.indexOf(xEquals);
		int yindex = locationString.indexOf(yEquals);

		if (isValidLocation(locationString)) {

			String xLocation = removeWhiteSpaceAndTrailingComma(locationString.substring(xindex + xEquals.length(), yindex));
			String yLocation = removeWhiteSpaceAndTrailingComma(locationString.substring(yindex + yEquals.length()));

			this.locationX = Double.parseDouble(xLocation);
			this.locationY = Double.parseDouble(yLocation);
		} else {
			this.locationX = null;
			this.locationY = null;
		}
	}

	private String removeWhiteSpaceAndTrailingComma(String str) {
		str = str.trim();
		if (str.endsWith(",")) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}


	private boolean isValidLocation(String line) {
		return line != null && line.startsWith(xEquals) && line.contains(xEquals) && line.contains(yEquals);
	}

	public String getLocationXString() {
		return Double.toString(locationX);
	}

	public String getLocationYString() {
		return Double.toString(locationY);
	}
}
