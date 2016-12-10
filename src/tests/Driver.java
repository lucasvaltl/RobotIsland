package tests;

import map.Map;
import readers.InvalidFormatException;
import readers.NewFileReader;
import readers.NewerFileReader;
import robot.Robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import gametimer.GameTimer;
import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import loggers.CustomFormatter;
import loggers.CustomHandler;
import loggers.CustomLevel;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Driver extends Application {
	/**
	 * TODO The main class of the robot simulator application, this class
	 * optionally reads robot information from XML, before creating a robot and
	 * map instance and rendering them, responding to various action events.
	 */

	public static final AudioClip soundtrack = new AudioClip(new File("src/wav/chibininja.wav").toURI().toString());
	public static final AudioClip collisionSound = new AudioClip(new File("src/wav/collision.wav").toURI().toString());
	public static final AudioClip rechargeSound = new AudioClip(new File("src/wav/recharge.wav").toURI().toString());
	public static final AudioClip batteryDeadSound = new AudioClip(new File("src/wav/batterydead.wav").toURI().toString());
	public static final AudioClip batteryLowSound = new AudioClip(new File("src/wav/batterylow.wav").toURI().toString());
	
	public static final int SCREENWIDTH = 800;
	public static final int SCREENHEIGHT = 800;
	// raycasting variables
	public static final int THREEDEEPLANEWIDTH = 320;
	public static final int THREEDEEPLANEHEIGHT = 200;
	public static final int[] THREEDEEPLANECENTRE = { THREEDEEPLANEWIDTH / 2, THREEDEEPLANEHEIGHT / 2 };
	public static final double fieldOfViewAngle = Math.PI / 3; // in radians
	public static final double DISTANCETOTHREEDEEPLANE = (THREEDEEPLANEWIDTH / 2) * Math.tan(fieldOfViewAngle / 2); // 160
																													// /
																													// tan(30degrees)
	public static final double angleBetweenRays = Driver.fieldOfViewAngle / (THREEDEEPLANEWIDTH * 1.0);
	public StackPane stack;
	public static long startnanotime;
	public static boolean decelerate = false;
	public static Map map;
	public static Robot wallE;
	public static Group root;
	public static TilePane devmode;
	public static Label labelx;
	public static Label textx;
	public static Label labely;
	public static Label texty;
	public static Label labelcharge;
	public static Label textcharge;
	public static Label labeldistance;
	public static Label textdistance;
	public static Label labelangle;
	public static Label textangle;
	public static Label labelinfo;
	public static Label textinfo;
	public static Button getmovementfile;
	public static Button gettimetrialfile;
	public static boolean toggledevmode;
	public static File movementFile;
	public static File timeTrialFile;
	public static Alert alert = new Alert(AlertType.WARNING);
	public static boolean alerttriggered;
	
	// custom logging stuff - outputs to different files based on level
	public static Logger LOGGER = Logger.getLogger(Driver.class.getName());
	private static CustomHandler fineHandler;
	private static CustomHandler finerHandler;
	private static CustomHandler finestHandler;
	private static CustomHandler infoHandler;
	private static CustomHandler warningHandler;
	private static CustomHandler severeHandler;
	private static CustomHandler instructionHandler;

	public static void main(String[] args) {

		// setup logger
		try {
			fineHandler = new CustomHandler ("src/logs/fine.txt", Level.FINE);
			finerHandler = new CustomHandler ("src/logs/finer.txt", Level.FINER);
			finestHandler = new CustomHandler ("src/logs/finest.txt", Level.FINEST);
			infoHandler = new CustomHandler("src/logs/info.txt", Level.INFO);
			warningHandler = new CustomHandler("src/logs/warning.txt", Level.WARNING);
			severeHandler = new CustomHandler("src/logs/severe.txt", Level.SEVERE);
			instructionHandler = new CustomHandler("src/robotinstructions.txt", CustomLevel.INSTRUCTION);
			
			LOGGER.setUseParentHandlers(false);
			LOGGER.addHandler(instructionHandler);
			LOGGER.addHandler(fineHandler);
			LOGGER.addHandler(finerHandler);
			LOGGER.addHandler(finestHandler);
			LOGGER.addHandler(infoHandler);
			LOGGER.addHandler(warningHandler);
			LOGGER.addHandler(severeHandler);
			
			CustomFormatter formatter = new CustomFormatter();
			fineHandler.setFormatter(formatter);
			finerHandler.setFormatter(formatter);
			finestHandler.setFormatter(formatter);
			infoHandler.setFormatter(formatter);
			warningHandler.setFormatter(formatter);
			severeHandler.setFormatter(formatter);
			instructionHandler.setFormatter(formatter);
			
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		 
		int[][] grid = { { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1 }, 
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
				{ 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1 }, 
				{ 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1 },
				{ 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1 }, 
				{ 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1 },
				{ 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
				{ 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, 
				{ 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, 
				{ 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

		map = new Map(grid); // create map
		
		launch(args); // launch javaFX
	}

	public void start(Stage primaryStage) throws Exception {
		
		
		/** Start method for JavaFX. Draws the robot and maps. **/
		primaryStage.setTitle("Robot test");

		Canvas canvas = new Canvas(SCREENWIDTH, SCREENHEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		stack = new StackPane();

		// pane used for background
//		Group background = new Group();
//		Image looks = new Image(new File("src/background.png").toURI().toString(), SCREENWIDTH, SCREENHEIGHT, false,
//				true);
//		ImageView pattern = new ImageView(looks);
//
//		background.getChildren().addAll(pattern);
//		stack.getChildren().add(background);
//		

		// pane used for game itself
		root = new Group();

		gc.setFill(Color.BLACK);
		gc.setStroke(Color.BLUE);
		gc.setLineWidth(5);
		map.render2DMap(gc, true);
		root.getChildren().add(canvas);
		
	

		// creates a "fast" robot from an xml file
		wallE = new Robot("fast");
		wallE.createAnimatedImages();
		wallE.setFill(wallE.getAnimatedImage(1, 1));
		wallE.setFocusTraversable(true);

		wallE.setOnKeyPressed(wallE); // adds Event handler
		wallE.setOnKeyReleased(wallE); // adds Event handler
		root.getChildren().add(wallE);
		stack.getChildren().add(root);
		
		toggledevmode = true;
		//pane used for devmode
		if(toggledevmode){
		devmode = new TilePane();
		labelx = new Label("X Position: ");
		textx = new Label("0");
		HBox hb1 = new HBox(labelx, textx);
		labely = new Label("Y Position: ");
		texty = new Label("0");
		HBox hb2 = new HBox(labely, texty);
		labelcharge = new Label("Charge Level: ");
		textcharge = new Label("100%");
		HBox hb3 = new HBox(labelcharge, textcharge);
		labeldistance = new Label("Distance: ");
		textdistance = new Label("0");
		HBox hb4 = new HBox(labeldistance, textdistance);
		labelangle = new Label("Angle: ");
		textangle = new Label("0");
		HBox hb5 = new HBox(labelangle, textangle);
		labelinfo = new Label("Info: ");
		textinfo = new Label();
		HBox hb6 = new HBox(labelinfo, textinfo);
		getmovementfile = new Button("Execute Movements from File");
		gettimetrialfile = new Button("Time trial");
		
		getmovementfile.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(final ActionEvent e) {
				// create filechooser
				final FileChooser fileChooser = new FileChooser();
				// only allow text files
				fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
				movementFile = fileChooser.showOpenDialog(primaryStage);
	
				if (movementFile != null) {
					NewerFileReader nfr = null;
					try {
						// validate file, alert if invalid format or not
						// found
						nfr = new NewerFileReader();
						nfr.scanFile(movementFile);
						wallE.setInputCommandsReadingInProgress(true);
					} catch (InvalidFormatException ex) {
						Driver.LOGGER.severe("WARNING: Invalid command in text file "+ e.toString());
						alert.setTitle("Invalid Format Error");
						alert.setHeaderText("Invalid format found in movement file!");
						alert.setContentText("This app only accepts the following movements, each on a single line: moveUp, moveDown, moveLeft, moveRight as well as "
												+ "moveUpLeft, moveUpRight, moveDownLeft, and moveDownRight.");
						alert.showAndWait();
					} catch (FileNotFoundException ex) {
						Driver.LOGGER.severe("WARNING: File not found "+ e.toString());
						alert.setTitle("File Not Found");
						alert.setHeaderText("Unfortunately the file could not be found");
						alert.setContentText("Please mke sure the file is still where it was when you selected it");
						alert.showAndWait();
					}
				}
			}
		});
		gettimetrialfile.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(final ActionEvent e) {
				// create filechooser
				final FileChooser fileChooser = new FileChooser();
				// only allow text files
				fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
				timeTrialFile = fileChooser.showOpenDialog(primaryStage);
	
				if (timeTrialFile != null) {
					NewFileReader nfr = null;
					try {
						// validate file, alert if invalid format or not
						// found
						nfr = new NewFileReader();
						nfr.scanFile(timeTrialFile);
						wallE.setTimeTrialInputInProgress(true);
					} catch (InvalidFormatException ex) {
						Driver.LOGGER.severe("WARNING: Invalid command in text file "+ e.toString());
						alert.setTitle("Invalid Format Error");
						alert.setHeaderText("Invalid format found in movement file!");
						alert.setContentText("Each line of the time trial file should"
								+ " be of the form [x position, y position, robot orientation, "
								+ "robot speed, robot battery left]");
						alert.showAndWait();
					} catch (FileNotFoundException ex) {
						Driver.LOGGER.severe("WARNING: File not found "+ e.toString());
						alert.setTitle("File Not Found");
						alert.setHeaderText("Unfortunately the file could not be found");
						alert.setContentText("Please mke sure the file is still where it was when you selected it");
						alert.showAndWait();
					}
				}
			}
		});
		
		devmode.getChildren().addAll(hb1, hb2, hb3, hb4, hb5, hb6, getmovementfile, gettimetrialfile);
		devmode.setMaxSize(150,200);
		devmode.setStyle("-fx-background-color: BBBBBB;");
		
		root.getChildren().add(devmode);
		
		devmode.setLayoutX(400);
		devmode.setLayoutY(580);
		}
		primaryStage.setScene(new Scene(stack));
		primaryStage.show();
		
		
		// start animation loop
		startnanotime = System.nanoTime();
		GameTimer timer = new GameTimer();
		timer.start();

		

	}

}