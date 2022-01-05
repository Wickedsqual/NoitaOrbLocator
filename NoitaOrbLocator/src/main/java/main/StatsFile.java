package main;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StatsFile {

	private final String statsTagName = "stats";
	private final String worldSeedAttributeName = "world_seed";

	private int worldSeed;

	private static final Logger log = LoggerFactory.getLogger(StatsFile.class);

	public boolean readFile() {
		try {
			File latestStatsFile = findLatestStatsFile();

			if (latestStatsFile != null) {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.parse(latestStatsFile);

				NodeList statsTagList = document.getElementsByTagName(statsTagName);

				if (statsTagList.item(0) != null) {
					Node statsTag = statsTagList.item(0);

					Node worldSeedAttributes = statsTag.getAttributes().getNamedItem(worldSeedAttributeName);

					if (worldSeedAttributes == null) {
						log.error("WorldSeed not found");
						return false;
					} else {
						worldSeed = Integer.parseInt(worldSeedAttributes.getNodeValue());
						return true;
					}
				} else {
					// throw new RuntimeException(transformTagName + " Tag not found");
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error("Error while reading File: " + e);
			return false;
		}
	}

	private File findLatestStatsFile() {
		File sessionFolder = FileHelper.getInstance().getSessionsFolderFile();

		return getLastModifiedStatsFile(sessionFolder);
	}

	public static File getLastModifiedStatsFile(File directory) {
		File[] files = directory.listFiles(File::isFile);
		long lastModifiedTime = Long.MIN_VALUE;
		File chosenFile = null;

		if (files != null) {
			for (File file : files) {
				if (isStatsFile(file)) {
					if (file.lastModified() > lastModifiedTime) {
						chosenFile = file;
						lastModifiedTime = file.lastModified();
					}
				}
			}
		}

		return chosenFile;
	}

	private static boolean isStatsFile(File file) {
		return file.getName().contains("_stats.xml");
	}

	public int getWorldSeed() {
		return this.worldSeed;
	}
}
