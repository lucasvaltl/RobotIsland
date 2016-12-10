package tests;

import map.Map;
import robot.Robot;

import java.io.File;
import java.io.IOException;

import gametimer.GameTimer;
import javafx.application.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
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
	public static boolean toggledevmode;
	
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
			fineHandler = new CustomHandler ("src/logs/fine", Level.FINE);
			finerHandler = new CustomHandler ("src/logs/finer", Level.FINER);
			finestHandler = new CustomHandler ("src/logs/finest", Level.FINEST);
			infoHandler = new CustomHandler("src/logs/info", Level.INFO);
			warningHandler = new CustomHandler("src/logs/warning", Level.WARNING);
			severeHandler = new CustomHandler("src/logs/severe", Level.SEVERE);
			instructionHandler = new CustomHandler("src/robotinstructions", CustomLevel.INSTRUCTION);
			
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
		devmode.getChildren().addAll(hb1, hb2, hb3, hb4, hb5, hb6);
		devmode.setMaxSize(150,200);
		devmode.setStyle("-fx-background-color: BBBBBB;");
		
		root.getChildren().add(devmode);
		
		devmode.setLayoutX(400);
		devmode.setLayoutY(600);
		}
		primaryStage.setScene(new Scene(stack));
		primaryStage.show();
		
		
		// start animation loop
		startnanotime = System.nanoTime();
		GameTimer timer = new GameTimer();
		timer.start();

		// wallE.singleMoveViaFile("src/movements2.txt");

	}

}