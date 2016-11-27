package tests;

import map.Map;
import robot.Robot;

import java.io.File;
import javafx.application.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Driver extends Application {
	/** TODO The main class of the robot simulator application, 
	 *  this class optionally reads robot information from XML, before 
	 *  creating a robot and map instance and rendering them, responding to 
	 *  various action events.
	 */

	public static final int SCREENWIDTH = 500;
	public static final int SCREENHEIGHT = 500;
	public static Map map;
	public static Robot wallE;
	public static Group root;
	
	public static void main(String[] args) {

		int[][] grid = {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
				{1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
				{1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
				{1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
				{1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
				{1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
				{1, 0, 1, 0, 1, 1, 0, 1, 0, 1},
				{1, 0, 1, 0, 1, 1, 0, 0, 0, 1},
				{1, 0, 0, 0, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};
		
		map = new Map(grid); // create map
		
		launch(args); // launch javaFX
	}

	public void start(Stage primaryStage) throws Exception {
		/** Start method for JavaFX.  Draws the robot and maps.  **/
		primaryStage.setTitle("Robot test");
		root = new Group();
		Canvas canvas = new Canvas(SCREENWIDTH, SCREENHEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		// Draw map and add to canvas
		
		gc.setFill(Color.BLACK);
		gc.setStroke(Color.GREY);
		gc.setLineWidth(5);
		map.render2DMap(gc, true);
		root.getChildren().add(canvas);
	
		/* Draw robot and add to canvas */
		wallE = new Robot("fast");
		wallE.setFill(Color.BLUE); 
		wallE.setFocusTraversable(true);
		// Robot(xCoordinate, yCoordinate, speed, xVel, yVel, xAcc, yAcc, 
		// angularVelocity, odometer, 
		// battery left, axle length, wheel radius.
		wallE.setOnKeyPressed(wallE); // adds Event handler
		wallE.setOnKeyReleased(wallE); // adds Event handler

		root.getChildren().add(wallE);
				
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
		wallE.moveViaFile("src/movements.txt");
		
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