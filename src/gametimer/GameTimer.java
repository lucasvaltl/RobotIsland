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
	 * Event handler called every timer interval.
	 * It calls the static methods in the movement class to 
	 * translate and transform the robot, and calculate the relative velocities
	 * of the left and right wheels.
	 */
	public void handle(long now) {
		
		
		
		double t = (now - Driver.startnanotime) / 1000000000.0;

		final double wallEorientation = Driver.wallE.getOrientation();

		final double[] wallEcomponents = Driver.wallE.getOrientationComponents(wallEorientation);
		

	Driver.wallE.animate();
		
		// read commands from file
		if (Driver.wallE.getInputCommandsReadingInProgress() == true) {
			// Request a new move
			Driver.wallE.singleMoveViaFile("src/movements2.txt");
		}
		
		//switch robot back to normal after given amount of timer after a collision
		if (GameTimer.collisionDetected){
			if (this.timecounter==80){
				Image pattern = new Image(new File("src/eve.png").toURI().toString(), 32, 48, false, true);
				ImagePattern skin = new ImagePattern(pattern);
				Driver.wallE.setFill(skin);
				this.timecounter = 0;
			} this.timecounter++;
		}
		
		//consume robots battery
		if(Driver.wallE.getSpeed()>0){
			Driver.wallE.decreaseCharge(0.05);
		}
		
		
		Driver.wallE.update();;

		// Otherwise move appropriately
//		if (Driver.wallE.getCurrentKeyPresses()[0] == "UP" && Driver.wallE.getCurrentKeyPresses()[1] == "LEFT") {
//			
//			if (Driver.wallE.getDecelerate() == true) {
//				Movement.decelerate(wallEcomponents);
//			} else {
//				Movement.moveUpLeft(wallEcomponents);
//			}
//			Driver.wallE.setLastMovement("moveUpLeft");
//		
//			
//		} else if (Driver.wallE.getCurrentKeyPresses()[0] == "UP" && 
//				Driver.wallE.getCurrentKeyPresses()[1] == "RIGHT") {
//			
//			if (Driver.wallE.getDecelerate() == true) {
//				Movement.decelerate(wallEcomponents);
//			} else {
//				Movement.moveUpRight(wallEcomponents);
//			}
//			Driver.wallE.setLastMovement("moveUpRight");
//			
//			
//			
//		} else if (Driver.wallE.getCurrentKeyPresses()[0] == "DOWN" && 
//				Driver.wallE.getCurrentKeyPresses()[1] == "LEFT") {
//			if (Driver.wallE.getDecelerate() == true) {
//				Movement.decelerate(wallEcomponents);
//			} else {
//				Movement.moveDownLeft(wallEcomponents);
//			}
//			Driver.wallE.setLastMovement("moveDownLeft");
//			
//			
//		} else if (Driver.wallE.getCurrentKeyPresses()[0] == "DOWN" && 
//				Driver.wallE.getCurrentKeyPresses()[1] == "RIGHT") {
//			if (Driver.wallE.getDecelerate() == true) {
//				Movement.decelerate(wallEcomponents);
//			} else {
//				Movement.moveDownRight(wallEcomponents);
//			}
//			Driver.wallE.setLastMovement("moveDownRight");
//				
//		} else if (Driver.wallE.getCurrentKeyPresses()[0] == "UP") {
//			if (Driver.wallE.getDecelerate() == true) {
//				// Robot must decelerate after previous motion in the opposite direction
//				Movement.decelerate(wallEcomponents);
//			} else {
//				// accelerate
//				Movement.moveUp(wallEcomponents);
//				Driver.wallE.setLastMovement("moveUp");
//			}
//
//		} else if (Driver.wallE.getCurrentKeyPresses()[0] == "DOWN") {
//			
//			if (Driver.wallE.getDecelerate() == true) {
//				// Robot must decelerate after previous motion in the opposite direction
//				Movement.decelerate(wallEcomponents);
//			} else {
//				// accelerate
//				Movement.moveDown(wallEcomponents);
//				Driver.wallE.setLastMovement("moveDown");
//			}			
//
//		} else if (Driver.wallE.getCurrentKeyPresses()[1] == "LEFT") {
//			Movement.moveLeft();
//			Driver.wallE.setLastMovement("moveLeft");
//			// allows robot to turn left during deceleration
//			if (Driver.wallE.getDecelerate() == true) {
//				Movement.decelerate(wallEcomponents);
//			}
//			
//			
//
//		} else if (Driver.wallE.getCurrentKeyPresses()[1] == "RIGHT") {
//			Movement.moveRight();
//			Driver.wallE.setLastMovement("moveRight");
//			// allows robot to turn right during deceleration
//			if (Driver.wallE.getDecelerate() == true) {
//				Movement.decelerate(wallEcomponents);
//			}
//			
//			
//		} else if (Driver.wallE.getCurrentKeyPresses()[0] == null) {
//			Movement.decelerate(wallEcomponents);
//			
//			
//		} 
//		
//		// change decelerate flag to false if speed is 0
//		if (Driver.wallE.getSpeed() <= 0) {
//			Driver.wallE.setDecelerate(false);
//			//Driver.lastUporDown = "";
//		}
	}
}
