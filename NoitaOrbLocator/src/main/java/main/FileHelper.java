package main;

import java.io.File;

public class FileHelper {

	private static final FileHelper INSTANCE = new FileHelper();

	private final File userHome = new File(System.getProperty("user.home"));

	private final String nollaGamesFolderName = "Nolla_Games_Noita";
	private final String appDataFolderName = "AppData";
	private final String localLowFolderName = "LocalLow";
	private final String firstSaveFolderName = "save00";
	private final String statsFolderName = "stats";
	private final String sessionsFolderName = "sessions";

	private final String playerFileName = "player.xml";

	private final File appDataFolder = new File(userHome, appDataFolderName);
	private final File localLowFolder = new File(appDataFolder, localLowFolderName);
	private final File userNollaGamesFolder = new File(localLowFolder, nollaGamesFolderName);
	private final File firstSaveFolderFile = new File(userNollaGamesFolder, firstSaveFolderName);
	private final File statsFolder = new File(firstSaveFolderFile, statsFolderName);
	private final File sessionFolder = new File(statsFolder, sessionsFolderName);

	private File playerFile = new File(getFirstSaveFolderFile(), playerFileName);


	public static FileHelper getInstance() {
		return INSTANCE;
	}

	public File getFirstSaveFolderFile() {
		return firstSaveFolderFile;
	}

	public File getSessionsFolderFile() {
		return sessionFolder;
	}

	public File getPlayerFile() {
		return playerFile;
	}

	public File getUserHomeFile() {
		return userHome;
	}

	public void setPlayerFile(File newPlayerFile) {
		this.playerFile = newPlayerFile;
	}
}
