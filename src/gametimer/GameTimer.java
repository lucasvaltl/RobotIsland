package gametimer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import loggers.CustomLevel;
import map.Map;
import robot.CollisionDetection;
import robot.Movement;
import tests.Driver;

/** Description: Class that implements a JavaFX animation timer 
 * 
 * @author Geraint and Lucas
 *
 */
public class GameTimer extends AnimationTimer {

	private int timecounter = 0;
	private static boolean collisionDetected;
	int i = 0;
	int j = 0;
	
	public static void setCollisionDetected(boolean b){
		collisionDetected = b;
	}
	
	/**
	 *  
	 * Event handler called every timer interval and updates the robot
	 * 
	 */
	public void handle(long now) {

		if (Driver.soundtrack.isPlaying() == false) {
			Driver.soundtrack.play(0.1);
		}
				
		// Log current key presses to file
		//Driver.LOGGER.log(CustomLevel.INSTRUCTION, Arrays.toString(Driver.wallE.getCurrentKeyPresses()));
		
		// Log wallE x and y positions to a file
		double[] wallEInfo = {Driver.wallE.getxCoordinate(), Driver.wallE.getyCoordinate(), 
				Driver.wallE.getRotate(), Driver.wallE.getSpeed(), Driver.wallE.getBatteryLeft()};
		Driver.LOGGER.log(CustomLevel.INSTRUCTION, Arrays.toString(wallEInfo));
		
		double t = (now - Driver.startnanotime) / 1000000000.0;	
		
		Driver.wallE.update();;

	}
}
