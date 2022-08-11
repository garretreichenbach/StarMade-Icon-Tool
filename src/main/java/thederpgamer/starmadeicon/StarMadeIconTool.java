package thederpgamer.starmadeicon;

import java.io.File;

/**
 * Main tool class.
 *
 * @author TheDerpGamer
 * @version 1.0
 */
public class StarMadeIconTool {

	public static void main(String[] args) {
		//Check for config file. If present, load settings, if not, run setup.
		File configFile = new File("config.json");
		if(configFile.exists()) {
			System.out.println("Config file found, loading settings...");
			Settings.loadSettings();
		} else {
			System.out.println("Config file not found, running setup...");
			Settings.runSetup();
		}
	}
}