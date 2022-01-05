package greaterOrbFinder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import infoClasses.OrbLocation;

public class GreaterOrbFinderWrapper {

	private static final Logger log = LoggerFactory.getLogger(GreaterOrbFinderWrapper.class);
	// TODO: CODE UGLY!

	public OrbLocation find(int seed, int newGameNumber, double xStartingPosition, double yStartingPosition) {
		File tmpOrbFinderExeFile = null;

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();

		try (InputStream is = classloader.getResourceAsStream("greaterOrbFinder/findorb11.exe")) {
			try {
				tmpOrbFinderExeFile = File.createTempFile("OrbFinder_", ".exe");
				tmpOrbFinderExeFile.deleteOnExit();

				try {
					Files.copy(
						is,
						tmpOrbFinderExeFile.toPath(),
						StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e1) {
					log.error("Error while copy findOrb.exe: ", e1);
				}
			} catch (Exception e) {

			}
		} catch (IOException e2) {
			log.error("Error while closing Stream: ", e2);
		}

		if (tmpOrbFinderExeFile.exists()) {

			Runtime rt = Runtime.getRuntime();

			String[] commands = { tmpOrbFinderExeFile.getAbsolutePath(), Integer.toString(seed), "ng=" + Integer.toString(newGameNumber),
				"x=" + Double.toString(xStartingPosition), "y=" + Double.toString(yStartingPosition) };

			log.debug("Commands: " + Arrays.toString(commands));

			Process proc;
			try {
				proc = rt.exec(commands);

				BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

				BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

				// Read the output from the command
				log.debug("Here is the standard output of the command:\n");

				String s = null;
				while ((s = stdInput.readLine()) != null) {
					log.debug(s);
					OrbLocation orbLocation = new OrbLocation(s);
					if (orbLocation.isLocationValid()) {
						return orbLocation;
					}

				}

				// Read any errors from the attempted command
				log.debug("Here is the standard error of the command (if any):\n");
				while ((s = stdError.readLine()) != null) {
					log.debug(s);
				}

			} catch (IOException e) {
				e.printStackTrace();
				log.error("Error while getting OrbLocation: " + e);
			}
		}

		return new OrbLocation(null);

	}

}
