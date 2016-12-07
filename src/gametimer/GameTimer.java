package gametimer;

import java.io.File;
import java.util.Arrays;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
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

		
		double t = (now - Driver.startnanotime) / 1000000000.0;
	
	
		
		
		
		
		Driver.wallE.update();;


	}
}
