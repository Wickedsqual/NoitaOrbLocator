package main;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import infoClasses.PlayerLocation;

public class PlayerFile {

	private String transformTagName = "_Transform";
	private String playerLocationXAttributeName = "position.x";
	private String playerLocationYAttributeName = "position.y";

	private PlayerLocation playerLocation = null;

	private static final Logger log = LoggerFactory.getLogger(PlayerFile.class);

	public boolean readFile() {
		try {
			File file = FileHelper.getInstance().getPlayerFile();

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);

			NodeList transformTagList = document.getElementsByTagName(transformTagName);

			if (transformTagList.item(0) != null) {
				Node playerLocationTag = transformTagList.item(0);

				Node locationXNode = playerLocationTag.getAttributes().getNamedItem(playerLocationXAttributeName);
				Node locationYNode = playerLocationTag.getAttributes().getNamedItem(playerLocationYAttributeName);

				if (locationXNode == null || locationYNode == null) {
					log.error("LocationX or Y not found");
					return false;
				} else {
					String locationX = playerLocationTag.getAttributes().getNamedItem(playerLocationXAttributeName).getNodeValue();
					String locationY = playerLocationTag.getAttributes().getNamedItem(playerLocationYAttributeName).getNodeValue();

					playerLocation = new PlayerLocation(Double.parseDouble(locationX), Double.parseDouble(locationY));
					return true;
				}
			} else {
				// throw new RuntimeException(transformTagName + " Tag not found");
				return false;
			}
		} catch (Exception e) {
			log.error("Error while reading File: " + e);
			return false;
		}
	}

	public PlayerLocation getPlayerLocation() {
		return this.playerLocation;

	}

}
