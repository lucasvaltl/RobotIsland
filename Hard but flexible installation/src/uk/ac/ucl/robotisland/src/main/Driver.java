package uk.ac.ucl.robotisland.src.main;


import uk.ac.ucl.robotisland.src.gametimer.GameTimer;
import uk.ac.ucl.robotisland.src.loggers.CustomFormatter;
import uk.ac.ucl.robotisland.src.loggers.CustomHandler;
import uk.ac.ucl.robotisland.src.loggers.CustomLevel;
import uk.ac.ucl.robotisland.src.map.Map;
import uk.ac.ucl.robotisland.src.readers.InvalidFormatException;
import uk.ac.ucl.robotisland.src.readers.NewerFileReader;
import uk.ac.ucl.robotisland.src.robot.DummyRobot;
import uk.ac.ucl.robotisland.src.robot.Robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Description: The main class of the robot simulator application, this class
 * optionally reads robot information from XML, before creating a robot and
 * map instance and rendering them, responding to various action events.
 * 
 * @author Geraint and Lucas 
 */
public class Driver extends Application {
			
	public static AudioClip soundtrack;
	public static AudioClip collisionSound;
	public static AudioClip rechargeSound;
	public static AudioClip batteryDeadSound;
	public static AudioClip batteryLowSound;
	public static AudioClip highscoreSound;
	public static AudioClip batteryFullSound;
	public static AudioClip finishLine;
	
	public static final int SCREENWIDTH = 800;
	public static final int SCREENHEIGHT = 800;
	
	// raycasting variables
	public static final int THREEDEEPLANEWIDTH = 320;
	public static final int THREEDEEPLANEHEIGHT = 200;
	public static final int[] THREEDEEPLANECENTRE = { THREEDEEPLANEWIDTH / 2, THREEDEEPLANEHEIGHT / 2 };
	public static final double fieldOfViewAngle = Math.PI / 3; // in radians
	public static final double DISTANCETOTHREEDEEPLANE = (THREEDEEPLANEWIDTH / 2) * Math.tan(fieldOfViewAngle / 2);

	public static DummyRobot dummy;
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

	public static Button getmovementfile;
	public static Button gettimetrialfile;
	public static Label time;
	public static Label timeLabel;
	public static Label lastLapTimeLabel;
	public static Label lastLapTime;
	public static Label highscoreLabel;
	public static Label highscore;
	public static Label batteryLabel;
	public static Label batteryLeft;
	public static boolean toggledevmode;
	public static File movementFile;
	public static InputStream timeTrialInputStream;
	public static Alert alert;
	public static boolean alerttriggered;
	public static boolean gameInProgress = false;
	public static String robotType;
	
	public static boolean timeTrialMode = false;
	
	// custom logging handlers - outputs to different files based on level
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
			File uk = new File("uk");
			File ac = new File("uk/ac");
			File ucl = new File("uk/ac/ucl");
			File robotisland = new File("uk/ac/ucl/robotisland");
			File res = new File("uk/ac/ucl/robotisland/res");
			File logs = new File("uk/ac/ucl/robotisland/res/logs");
			if (!uk.exists()) uk.mkdirs();
			if (!ac.exists()) ac.mkdirs();
			if (!ucl.exists()) ucl.mkdirs();
			if (!robotisland.exists()) robotisland.mkdirs();
			if (!res.exists()) res.mkdirs();
			if (!logs.exists()) logs.mkdirs();
			
			fineHandler = new CustomHandler ("uk/ac/ucl/robotisland/res/logs/fine.txt", Level.FINE);
			finerHandler = new CustomHandler ("uk/ac/ucl/robotisland/res/logs/finer.txt", Level.FINER);
			finestHandler = new CustomHandler ("uk/ac/ucl/robotisland/res/logs/finest.txt", Level.FINEST);
			infoHandler = new CustomHandler("uk/ac/ucl/robotisland/res/logs/info.txt", Level.INFO);
			warningHandler = new CustomHandler("uk/ac/ucl/robotisland/res/logs/warning.txt", Level.WARNING);
			severeHandler = new CustomHandler("uk/ac/ucl/robotisland/res/logs/severe.txt", Level.SEVERE);
			instructionHandler = new CustomHandler("uk/ac/ucl/robotisland/res/logs/robotinstructions.txt", CustomLevel.INSTRUCTION);
			
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
		
		 
		int[][] grid = { 
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
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

		// Load audio
		URL soundtrackURL = ClassLoader.getSystemResource("uk/ac/ucl/robotisland/src/wav/chibininja.wav");
		URL collisionURL = ClassLoader.getSystemResource("uk/ac/ucl/robotisland/src/wav/collision.wav");
		URL rechargeURL= ClassLoader.getSystemResource("uk/ac/ucl/robotisland/src/wav/recharge.wav");
		URL batteryDeadURL = ClassLoader.getSystemResource("uk/ac/ucl/robotisland/src/wav/batterydead.wav");
		URL batteryLowURL = ClassLoader.getSystemResource("uk/ac/ucl/robotisland/src/wav/batterylow.wav");
		URL highscoreURL = ClassLoader.getSystemResource("uk/ac/ucl/robotisland/src/wav/highscore.wav");
		URL batteryFullURL = ClassLoader.getSystemResource("uk/ac/ucl/robotisland/src/wav/fullrecharge.wav");
		URL finishLineURL = ClassLoader.getSystemResource("uk/ac/ucl/robotisland/src/wav/finishline.wav");
		soundtrack = new AudioClip(soundtrackURL.toString());
		collisionSound = new AudioClip(collisionURL.toString());
		rechargeSound = new AudioClip(rechargeURL.toString());
		batteryDeadSound = new AudioClip(batteryDeadURL.toString());
		batteryLowSound = new AudioClip(batteryLowURL.toString());
		highscoreSound = new AudioClip(highscoreURL.toString());
		batteryFullSound = new AudioClip(batteryFullURL.toString());
		finishLine = new AudioClip(finishLineURL.toString());
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
		
		Image looks = new Image(Driver.class.getResource("/uk/ac/ucl/robotisland/src/img/background.png").toString(), SCREENWIDTH, SCREENHEIGHT, false,
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
		lastLapTimeLabel.setStyle("-fx-font-size: 12;");
		lastLapTime = new Label("0 s");	
		lastLapTime.setStyle("-fx-font-size: 12;");
		HBox hb8 = new HBox(lastLapTimeLabel, lastLapTime);
		highscoreLabel = new Label("Highscore: ");
		highscoreLabel.setStyle("-fx-font-size: 12;");
		highscore = new Label("0 s");
		highscore.setStyle("-fx-font-size: 12;");
		HBox hb9 = new HBox(highscoreLabel, highscore);
		batteryLabel = new Label("Battery Left: ");
		batteryLabel.setStyle("-fx-font-size: 12;");
		batteryLeft = new Label("100 %");
		batteryLeft.setStyle("-fx-font-size: 13;");
		HBox hb10 = new HBox(batteryLabel, batteryLeft);
		lapTimes.getChildren().addAll(hb7, hb8, hb9, hb10);
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
			
		// creates a "fast" robot from the xml file
		robotType = "fast";
		wallE = new Robot(robotType, true);
		wallE.createAnimatedImages();
		wallE.setFill(wallE.getAnimatedImage(1, 1));
		wallE.setFocusTraversable(true);
		wallE.setOnKeyPressed(wallE); // adds Event handler
		wallE.setOnKeyReleased(wallE); // adds Event handler
		root.getChildren().add(wallE);
		stack.getChildren().add(root);
		
		//add new pane to accomodate buttons and labels
		devmode = new TilePane();
		gettimetrialfile = new Button("Time trial OFF");
//		gettimetrialfile.setStyle("fx-row-valignment: top; -fx-background-radius: 8;-fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a,radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);  -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75), 2,0,0,0 ); -fx-font-weight: bold; -fx-background-insets: 0 ,-3 -3 5 3, -3 -3 6 3, -3 -3 7 3;");
//		devmode.setStyle("-fx-font-family: \"Monaco\";");
		devmode.getStylesheets().add(Driver.class.getResource("/uk/ac/ucl/robotisland/src/css/style.css").toString());
		devmode.setMaxSize(50,40);
		devmode.setLayoutX(420);
		devmode.setLayoutY(580);
		
		//toggle to enable dev mode
		toggledevmode = false;
		//pane used for devmode
		if(toggledevmode){
		labelx = new Label("X Position: ");
		textx = new Label("0");
		HBox hb1 = new HBox(labelx, textx);
		hb1.setMaxHeight(5);
		labely = new Label("Y Position: ");
		texty = new Label("0");
		HBox hb2 = new HBox(labely, texty);
		hb2.setMaxHeight(5);
		labelcharge = new Label("Charge Level: ");
		labelcharge.setMaxHeight(5);
		textcharge = new Label("100%");
		textcharge.setMaxHeight(5);
		HBox hb3 = new HBox(labelcharge, textcharge);
		hb3.setMaxHeight(5);
		labeldistance = new Label("Distance: ");
		textdistance = new Label("0");
		HBox hb4 = new HBox(labeldistance, textdistance);
		hb4.setMaxHeight(5);
		labelangle = new Label("Angle: ");
		textangle = new Label("0");
		HBox hb5 = new HBox(labelangle, textangle);
		hb5.setMaxHeight(5);
		labelinfo = new Label("Info: ");
		textinfo = new Label();
		HBox hb6 = new HBox(labelinfo, textinfo);
		hb6.setMaxHeight(5);
		getmovementfile = new Button("Execute Movements from File");
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
							
						wallE.requestFocus(); // sets the focus back to main robot
							
					} catch (InvalidFormatException ex) {
						Driver.LOGGER.severe("WARNING: Invalid command in text file "+ e.toString());
						alert = new Alert(AlertType.WARNING);
						alert.setTitle("Invalid Format Error");
						alert.setHeaderText("Invalid format found in movement file!");
						alert.setContentText("This app only accepts the following movements, each on a single line: moveUp, moveDown, moveLeft, moveRight as well as "
											+ "moveUpLeft, moveUpRight, moveDownLeft, and moveDownRight.");
						alert.showAndWait();
					} catch (FileNotFoundException ex) {
						Driver.LOGGER.severe("WARNING: File not found "+ e.toString());
						alert.setTitle("File Not Found");
						alert.setHeaderText("Unfortunately the file could not be found");
						alert.setContentText("Please mke sure the file is stil where you it was when you selected it");
						alert.showAndWait();
					}
				}
			}
		});
		devmode.getChildren().addAll(hb1, hb2, hb3, hb4, hb5, hb6, getmovementfile);
		//change layout to accommodate additional buttons and labels when devmode is toggled
		devmode.setStyle("-fx-background-color: BBBBBB; ");
		devmode.setVgap(18);
		devmode.setPrefTileHeight(12);
		devmode.setMaxSize(150,160);
		devmode.setLayoutX(400);
		devmode.setLayoutY(552);
		}
		//add time trial mode irrespective if devmode is toggled
		
		gettimetrialfile.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(final ActionEvent e) {
				File highScoreLapFile= new File("uk/ac/ucl/robotisland/res/highscorelap.txt");
				if (timeTrialMode && highScoreLapFile.exists()) {
					gettimetrialfile.setText("Time Trial OFF");
					timeTrialMode = false;
				} else if (highScoreLapFile.exists() && !timeTrialMode) {
					gettimetrialfile.setText("Time Trial ON");
					timeTrialMode = true;
				} else if (!highScoreLapFile.exists()) {
					Driver.textinfo.setText("ERROR - No highscore yet");
				}
			}
		});

		devmode.getChildren().add(gettimetrialfile);
		root.getChildren().add(devmode);
		
		//create splash screen
		if (Driver.gameInProgress == false) {
			splashscreen = new StackPane();
			Image splashImage = new Image(Driver.class.getResource("/uk/ac/ucl/robotisland/src/img/splash.png").toString());
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
		Image gameOverImage = new Image(Driver.class.getResource("/uk/ac/ucl/robotisland/src/img/gameover.png").toString());
		ImageView gameOverView = new ImageView(gameOverImage);
		gameOverView.setFitHeight(SCREENHEIGHT);
		gameOverView.setFitWidth(SCREENWIDTH);
		gameOverScreen.getChildren().add(gameOverView);
		root.getChildren().add(gameOverScreen);
		gameOverScreen.setLayoutX(0);
		gameOverScreen.setLayoutY(0);
		gameOverScreen.setVisible(false);
		
		primaryStage.setScene(new Scene(stack));
		primaryStage.setResizable(false);
		primaryStage.show();
		
		// start JavaFX animation loop
		startnanotime = System.nanoTime();
		GameTimer timer = new GameTimer();
		timer.start();		

	}
}