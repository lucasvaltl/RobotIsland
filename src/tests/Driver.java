package tests;

import map.Map;
import readers.InvalidFormatException;
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
import javafx.scene.effect.PerspectiveTransform;
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
import javafx.scene.text.Font;
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

/**
 * Description: The main class of the robot simulator application, this class
 * optionally reads robot information from XML, before creating a robot and
 * map instance and rendering them, responding to various action events.
 * 
 * @author: Geraint and Lucas
 */
public class Driver extends Application {
	
	// Load audio
	public static final AudioClip soundtrack = new AudioClip(new File("rsc/wav/chibininja.wav").toURI().toString());
	public static final AudioClip collisionSound = new AudioClip(new File("rsc/wav/collision.wav").toURI().toString());
	public static final AudioClip rechargeSound = new AudioClip(new File("rsc/wav/recharge.wav").toURI().toString());
	public static final AudioClip batteryDeadSound = new AudioClip(new File("rsc/wav/batterydead.wav").toURI().toString());
	public static final AudioClip batteryLowSound = new AudioClip(new File("rsc/wav/batterylow.wav").toURI().toString());
	public static final AudioClip highscoreSound = new AudioClip(new File("rsc/wav/highscore.wav").toURI().toString());
	public static final AudioClip batteryFullSound = new AudioClip(new File("rsc/wav/fullrecharge.wav").toURI().toString());
	public static final AudioClip finishLine = new AudioClip(new File("rsc/wav/finishline.wav").toURI().toString());
	
	public static final int SCREENWIDTH = 800;
	public static final int SCREENHEIGHT = 800;
	
	// raycasting variables
	public static final int THREEDEEPLANEWIDTH = 320;
	public static final int THREEDEEPLANEHEIGHT = 200;
	public static final int[] THREEDEEPLANECENTRE = { THREEDEEPLANEWIDTH / 2, THREEDEEPLANEHEIGHT / 2 };
	public static final double fieldOfViewAngle = Math.PI / 3; // in radians
	public static final double DISTANCETOTHREEDEEPLANE = (THREEDEEPLANEWIDTH / 2) * Math.tan(fieldOfViewAngle / 2);
	public static final double angleBetweenRays = Driver.fieldOfViewAngle / (THREEDEEPLANEWIDTH * 1.0);
	
	// JavaFX variables
	public StackPane stack;
	public static long startnanotime;
	public static boolean decelerate = false;
	public static Map map;
	public static Robot wallE;
	public static Group root;
	public static TilePane lapTimes;
	public static TilePane devmode;
	public static StackPane splashscreen;
	public static StackPane gameOverScreen;
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
	public static Button getfile;
	public static Label time;
	public static Label timeLabel;
	public static Label lastLapTimeLabel;
	public static Label lastLapTime;
	public static Label highscoreLabel;
	public static Label highscore;
	public static boolean toggledevmode;
	public static File movementFile;
	public static Alert alert;
	public static boolean alerttriggered;
	public static boolean gameInProgress = false;
	public static String robotType;
	
	// custom logging stuff - outputs to different files based on level
	public static Logger LOGGER = Logger.getLogger(Driver.class.getName());
	private static CustomHandler fineHandler;
	private static CustomHandler finerHandler;
	private static CustomHandler finestHandler;
	private static CustomHandler infoHandler;
	private static CustomHandler warningHandler;
	private static CustomHandler severeHandler;
	private static CustomHandler instructionHandler;

	/**
	 * Description: Sets up the logger, creates a map instance, and launches
	 * JavaFX.
	 *  
	 * @param args: No command line arguments are expected.
	 */
	public static void main(String[] args) {

		// setup logger with custom handler, custom level, and custom formatter
		try {
			fineHandler = new CustomHandler ("fine.txt", Level.FINE);
			finerHandler = new CustomHandler ("finer.txt", Level.FINER);
			finestHandler = new CustomHandler ("finest.txt", Level.FINEST);
			infoHandler = new CustomHandler("info.txt", Level.INFO);
			warningHandler = new CustomHandler("warning.txt", Level.WARNING);
			severeHandler = new CustomHandler("severe.txt", Level.SEVERE);
			instructionHandler = new CustomHandler("robotinstructions.txt", CustomLevel.INSTRUCTION);
			
//			fineHandler = new CustomHandler ("src/logs/fine.txt", Level.FINE);
//			finerHandler = new CustomHandler ("src/logs/finer.txt", Level.FINER);
//			finestHandler = new CustomHandler ("src/logs/finest.txt", Level.FINEST);
//			infoHandler = new CustomHandler("src/logs/info.txt", Level.INFO);
//			warningHandler = new CustomHandler("src/logs/warning.txt", Level.WARNING);
//			severeHandler = new CustomHandler("src/logs/severe.txt", Level.SEVERE);
//			instructionHandler = new CustomHandler("src/robotinstructions.txt", CustomLevel.INSTRUCTION);
			
			
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

	/**
	 * Description: Start method called when JavaFX is launched. 
	 * Draws the robot, map, devmode pane, and splashscreen.
	 */
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Robot Island");

		Canvas canvas = new Canvas(SCREENWIDTH, SCREENHEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		stack = new StackPane();

		// pane used for background
		Group background = new Group();
		Image looks = new Image(new File("rsc/img/background.png").toURI().toString(), SCREENWIDTH, SCREENHEIGHT, false,
				true);
		ImageView pattern = new ImageView(looks);

		background.getChildren().addAll(pattern);
		stack.getChildren().add(background);

		// pane used for game itself
		root = new Group();

		// Render map
		gc.setFill(Color.BLACK);
		gc.setStroke(Color.BLUE);
		gc.setLineWidth(5);
		map.render2DMap(gc, true);
		root.getChildren().add(canvas);
		
		// Pane for displaying lap times
		lapTimes = new TilePane();
		timeLabel = new Label("Current Lap: ");
		timeLabel.setStyle("-fx-font-size: 20;");
		time = new Label("0 s");
		time.setStyle("-fx-font-size: 20;");
		HBox hb7 = new HBox(timeLabel, time);
		lastLapTimeLabel = new Label("Last Lap: ");
		lastLapTimeLabel.setStyle("-fx-font-size: 15;");
		lastLapTime = new Label("0 s");	
		lastLapTime.setStyle("-fx-font-size: 15;");
		HBox hb8 = new HBox(lastLapTimeLabel, lastLapTime);
		highscoreLabel = new Label("Highscore: ");
		highscoreLabel.setStyle("-fx-font-size: 15;");
		highscore = new Label("0 s");
		highscore.setStyle("-fx-font-size: 15;");
		HBox hb9 = new HBox(highscoreLabel, highscore);
		lapTimes.getChildren().addAll(hb7, hb8, hb9);
		lapTimes.setMaxHeight(100);
		lapTimes.setMaxWidth(70);
		lapTimes.setStyle("-fx-font-family: \"Monaco\";");
		lapTimes.setLayoutX(500);
		lapTimes.setLayoutY(38);
		
		// Create 3d effect
		PerspectiveTransform pt = new PerspectiveTransform();
		pt.setUlx(35.0);
		 pt.setUly(0.0);
		 pt.setUrx(215.0);
		 pt.setUry(0.0);
		 pt.setLrx(180.0);
		 pt.setLry(70.0);
		 pt.setLlx(0.0);
		 pt.setLly(70.0);
		lapTimes.setEffect(pt);
		lapTimes.setCache(true);
		
		root.getChildren().add(lapTimes);
			
		// creates a "fast" robot from an xml file
		robotType = "fast";
		wallE = new Robot(robotType);
		wallE.createAnimatedImages();
		wallE.setFill(wallE.getAnimatedImage(1, 1));
		wallE.setFocusTraversable(true);
		wallE.setOnKeyPressed(wallE); // adds Event handler
		wallE.setOnKeyReleased(wallE); // adds Event handler
		root.getChildren().add(wallE);
		stack.getChildren().add(root);
		
		//toggle to enable dev mode
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
		getfile = new Button("Execute Movements from File");
		
			getfile.setOnAction(new EventHandler<ActionEvent>() {
				@Override
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
							alert = new Alert(AlertType.WARNING);
							alert.setTitle("Invalid Format Error");
							alert.setHeaderText("Invalid format found in movement file!");
							alert.setContentText(
									"This app only accepts the following movements, each on a single line: moveUp, moveDown, moveLeft, moveRight as well as "
											+ "moveUpLeft, moveUpRight, moveDownLeft, and moveDownRight.");
							alert.showAndWait();
						} catch (FileNotFoundException ex) {
							Driver.LOGGER.severe("WARNING: File not found "+ e.toString());
							alert.setTitle("File Not Found");
							alert.setHeaderText("Unfortunately the file could not be found");
							alert.setContentText(
									"Please mke sure the file is stil where you it was when you selected it");
							alert.showAndWait();
						}
					}

				}
			});
		devmode.getChildren().addAll(hb1, hb2, hb3, hb4, hb5, hb6, getfile);
		devmode.setMaxSize(150,200);
		devmode.setStyle("-fx-background-color: BBBBBB; -fx-font-family: \"Monaco\";");
		
		root.getChildren().add(devmode);
		
		devmode.setLayoutX(400);
		devmode.setLayoutY(580);
		}
		
		//create splash screen
		if (Driver.gameInProgress == false) {
			splashscreen = new StackPane();
			Image splashImage = new Image(new File("rsc/img/splash.png").toURI().toString());
			ImageView splashView = new ImageView(splashImage);
			splashView.setFitHeight(SCREENHEIGHT);
			splashView.setFitWidth(SCREENWIDTH);
			splashscreen.getChildren().add(splashView);
			root.getChildren().add(splashscreen);
			splashscreen.setLayoutX(0);
			splashscreen.setLayoutY(0);
		}
		
		//create gameover screen
		gameOverScreen = new StackPane();
		Image gameOverImage = new Image(new File("rsc/img/gameover.png").toURI().toString());
		ImageView gameOverView = new ImageView(gameOverImage);
		gameOverView.setFitHeight(SCREENHEIGHT);
		gameOverView.setFitWidth(SCREENWIDTH);
		gameOverScreen.getChildren().add(gameOverView);
		root.getChildren().add(gameOverScreen);
		gameOverScreen.setLayoutX(0);
		gameOverScreen.setLayoutY(0);
		gameOverScreen.setVisible(false);
		
		
		primaryStage.setScene(new Scene(stack));
		primaryStage.show();
		
		
		// start JavaFX animation loop
		startnanotime = System.nanoTime();
		GameTimer timer = new GameTimer();
		timer.start();

		

	}

}