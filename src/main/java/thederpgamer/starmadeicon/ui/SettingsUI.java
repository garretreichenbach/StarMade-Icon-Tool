package thederpgamer.starmadeicon.ui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import thederpgamer.starmadeicon.StarMadeIconTool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * Handles tool settings though the config file.
 *
 * @author TheDerpGamer
 * @version 1.0
 */
public class SettingsUI extends Application {

	private Stage stage;
	private Scene scene;
	private AnchorPane anchorPane;

	public static String starmadeDir = "C:/Program Files (x86)/Steam/steamapps/common/StarMade/StarMade/";

	public static void main(String[] args) {
		//Check for config file. If present, load settings, if not, run setup.
		File configFile = new File("config.json");
		if(configFile.exists()) {
			System.out.println("Config file found, loading settings...");
			loadSettings();
		} else {
			System.out.println("Config file not found, running setup...");
			runSetup();
		}
		launch(args);
	}

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

	@Override
	public void start(Stage stage) throws Exception {
		//Check for config file. If present, load settings, if not, run setup.
		File configFile = new File("config.json");
		if(configFile.exists()) {
			System.out.println("Config file found, loading settings...");
			loadSettings();
		} else {
			System.out.println("Config file not found, running setup...");
			runSetup();
		}

		this.stage = stage;
		anchorPane = (new FXMLLoader(StarMadeIconTool.class.getResource("/ui/SettingsUI.fxml"))).load();
		scene = new Scene(anchorPane);
		stage.setScene(scene);
		stage.setTitle("StarMade Icon Tool - Settings");
		setupControls();
		stage.show();
	}

	public void setupControls() {
		final TextField inputField = (TextField) anchorPane.lookup("#InputField");
		inputField.setText(starmadeDir);

		final Button browseButton = (Button) scene.lookup("#BrowseButton");
		browseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getButton().equals(MouseButton.PRIMARY)) browseFiles();
			}
		});

		final Button okButton = (Button) scene.lookup("#OkButton");
		okButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getButton().equals(MouseButton.PRIMARY)) stage.close();
			}
		});
	}

	public void browseFiles() {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(stage);
		if(file != null && file.isDirectory()) ((TextField) scene.lookup("#InputField")).setText(file.getAbsolutePath());
	}
}
