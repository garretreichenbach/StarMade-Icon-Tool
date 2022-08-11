package thederpgamer.starmadeicon;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * Handles tool settings.
 *
 * @author TheDerpGamer
 * @version 1.0
 */
public class Settings {
	private static String starmadeDir = "C:/Program Files (x86)/Steam/steamapps/common/StarMade/StarMade/";

	/**
	 * Loads settings from config.json.
	 */
	public static void loadSettings() {
		File configFile = new File("config.json");
		if(!configFile.exists()) runSetup();
		else parseConfig(configFile);
	}

	/**
	 * Attempts to parse the config file. If the file is invalid, it will be deleted and the setup will be run.
	 *
	 * @param configFile The config file to parse.
	 */
	private static void parseConfig(File configFile) {
		try {
			String config = FileUtils.readFileToString(configFile);
			JSONObject json = new JSONObject(config);

			{ //Check starmade dir
				String dir = json.getString("starmadeDir");
				File file = new File(dir);
				if(!file.exists() || !file.isDirectory() || !containsSMJar(file)) {
					System.out.println("Starmade directory not found, running setup...");
					runSetup();
				} else starmadeDir = dir;
			}
		} catch(Exception exception) { //Catch any random exceptions.
			exception.printStackTrace();
			runSetup();
		}
	}

	/**
	 * Checks if the given directory contains the StarMade jar file.
	 *
	 * @param file The directory to check.
	 * @return True if the directory contains the StarMade jar file, false otherwise.
	 */
	public static boolean containsSMJar(File file) {
		for(File f : Objects.requireNonNull(file.listFiles())) {
			if(f.getName().equals("StarMade.jar")) return true;
		}
		return false;
	}

	/**
	 * Runs the setup.
	 */
	public static void runSetup() {
		//Create config file
		File configFile = new File("config.json");
		try {
			if(configFile.exists()) configFile.delete();
			configFile.createNewFile();
		} catch(IOException exception) {
			throw new RuntimeException(exception);
		}

		//Set defaults
		try {
			FileWriter writer = new FileWriter(configFile);
			String defaultConfig = "{\"starmadeDir\":\"" + starmadeDir + "\"}";
			JSONObject jsonObject = new JSONObject(defaultConfig);
			writer.write(jsonObject.toString());
			writer.flush();
			writer.close();
		} catch(Exception exception) {
			throw new RuntimeException(exception);
		}

	}
}
