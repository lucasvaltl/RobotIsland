package tests;

import map.Map;
import robot.Robot;
//import robot.Wheel;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import gametimer.GameTimer;
import javafx.application.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Driver extends Application {
	/** TODO The main class of the robot simulator application, 
	 *  this class optionally reads robot information from XML, before 
	 *  creating a robot and map instance and rendering them, responding to 
	 *  various action events.
	 */

	public static final int SCREENWIDTH = 1024;
	public static final int SCREENHEIGHT = 1024;
		// raycasting variables
	public static final int THREEDEEPLANEWIDTH = 320;
	public static final int THREEDEEPLANEHEIGHT = 200;
	public static final int[] THREEDEEPLANECENTRE = {THREEDEEPLANEWIDTH / 2, 
			THREEDEEPLANEHEIGHT / 2};
	public static final double fieldOfViewAngle = Math.PI / 3; // in radians
	public static final double DISTANCETOTHREEDEEPLANE = (THREEDEEPLANEWIDTH / 2) * Math.tan(fieldOfViewAngle / 2); // 160 / tan(30degrees)
	public static final double angleBetweenRays = Driver.fieldOfViewAngle / (THREEDEEPLANEWIDTH * 1.0);
	
	public static long startnanotime;
	public static Map map;
	public static Robot wallE;
	//public static Wheel leftWheel;
	//public static Wheel rightWheel;
	public static Rectangle[] wheelE;
	public static Group root;
	public static Group robot;
	public static String[] currentKeyPresses = new String[2];
	public static String lastUporDown = ""; // used to keep track of robot direction.
	public static GraphicsContext gc;
	
	public static void main(String[] args) {

		int[][] grid = {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
				{1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
				{1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
				{1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
				{1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
				{1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1},
				{1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1},
				{1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1},
				{1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1},
				{1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
				{1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
				{1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
				{1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
				{1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
				{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
				{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};
		
		map = new Map(grid); // create map
		
		launch(args); // launch javaFX
	}

	public void start(Stage primaryStage) throws Exception {
		/** Start method for JavaFX.  Draws the robot and maps.  **/
		primaryStage.setTitle("Robot test");
		root = new Group();
		robot = new Group();
		Canvas canvas = new Canvas(SCREENWIDTH, SCREENHEIGHT);
		Canvas canvas2 = new Canvas(200, 200); // TODO experimental
		GraphicsContext gctwo = canvas2.getGraphicsContext2D();
		
		gctwo.setFill(Color.RED); // experimental
		gctwo.fillOval(100, 100, 50, 50); // experimental
		
		gc = canvas.getGraphicsContext2D();
		
		// Draw map and add to canvas
		
		gc.setFill(Color.BLACK);
		gc.setStroke(Color.GREY);
		gc.setLineWidth(5);
		map.render2DMap(gc, true);
		
		root.getChildren().add(canvas);
		root.getChildren().add(canvas2); // experimental
		root.getChildren().add(robot);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
		
		
		//creates a "fast" robot from an xml file
		wallE = new Robot("fast");
		// wallE = new Robot("test", 40, 40, 3, 1, 1, 0, 0, 3, 0, 100, 16, 8);
		
		wallE.setFocusTraversable(true);

		// Robot(xCoordinate, yCoordinate, speed, xVel, yVel, xAcc, yAcc, 
		// angularVelocity, odometer, 
		// battery left, axle length, wheel radius.
		wallE.setOnKeyPressed(wallE); // adds Event handler
		wallE.setOnKeyReleased(wallE);
//		wallE.setFill(Color.TRANSPARENT);// adds Event handler
		robot.getChildren().add(wallE);
		
		
//		root.getChildren().add(wheelLeft);
//		root.getChildren().add(wheelRight);
	
		
		
		// start animation loop
		startnanotime = System.nanoTime();
		GameTimer timer = new GameTimer();
		timer.start();
		
		
//		wallE.moveViaFile("src/movements.txt");
		
	}
	
	public void writeCommandToFile(String command) {
		/** TODO Appends a string to a file, creating the file if it does not exist **/
	}
	
	public void readCommandsFromFile(File file) {
		/** TODO Reads a set of commands from a file and executes them one by one **/
	}

	public void readRobotInfoFromXML(File file) {
		/** TODO Reads data corresponding to the robot's geometry and returns it **/
	}
	
}