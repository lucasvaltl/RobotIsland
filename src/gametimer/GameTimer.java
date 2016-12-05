package gametimer;

import java.util.Arrays;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
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

		// read commands from file
		if (Driver.wallE.getInputCommandsReadingInProgress() == true) {
			// Request a new move
			Driver.wallE.singleMoveViaFile("src/movements2.txt");
		}
		
		
		

		// set speed to zero if collision is found and reverse movements to escape being blocked in walls
		if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
			Driver.wallE.setSpeed(0);
			if (Driver.wallE.getLastMovement().equals("moveDown")) {
				while (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
					Movement.moveUp(wallEcomponents);
				}
				Driver.wallE.setSpeed(0);
			} else if (Driver.wallE.getLastMovement().equals("moveUp")) {
				while (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
					Movement.moveDown(wallEcomponents);
				}
				Driver.wallE.setSpeed(0);
			} else if (Driver.wallE.getLastMovement().equals("moveLeft")) {
				while (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
					Movement.moveRight();
				}
				Driver.wallE.setSpeed(0);
			} else if (Driver.wallE.getLastMovement().equals("moveRight")) {
				while (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
					Movement.moveLeft();
				}
				Driver.wallE.setSpeed(0);
			} else if (Driver.wallE.getLastMovement().equals("moveUpLeft")) {
				while (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
					Movement.moveDownRight(wallEcomponents);
				}
				Driver.wallE.setSpeed(0);
			} else if (Driver.wallE.getLastMovement().equals("moveUpRight")) {
				while (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
					Movement.moveDownLeft(wallEcomponents);
				}
				Driver.wallE.setSpeed(0);
			} else if (Driver.wallE.getLastMovement().equals("moveDownLeft")) {
				while (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
					Movement.moveUpRight(wallEcomponents);
				}
				Driver.wallE.setSpeed(0);
			} else if (Driver.wallE.getLastMovement().equals("moveDownRight")) {
				while (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
					Movement.moveUpLeft(wallEcomponents);
				}
				Driver.wallE.setSpeed(0);
			}

		}

		// Otherwise move appropriately
		if (Driver.wallE.getCurrentKeyPresses()[0] == "UP" && Driver.wallE.getCurrentKeyPresses()[1] == "LEFT") {
			
			if (Driver.wallE.getDecelerate() == true) {
				Movement.decelerate(wallEcomponents);
			} else {
				Movement.moveUpLeft(wallEcomponents);
			}
			Driver.wallE.setLastMovement("moveUpLeft");
		
			
		} else if (Driver.wallE.getCurrentKeyPresses()[0] == "UP" && 
				Driver.wallE.getCurrentKeyPresses()[1] == "RIGHT") {
			
			if (Driver.wallE.getDecelerate() == true) {
				Movement.decelerate(wallEcomponents);
			} else {
				Movement.moveUpRight(wallEcomponents);
			}
			Driver.wallE.setLastMovement("moveUpRight");
			
			
			
		} else if (Driver.wallE.getCurrentKeyPresses()[0] == "DOWN" && 
				Driver.wallE.getCurrentKeyPresses()[1] == "LEFT") {
			if (Driver.wallE.getDecelerate() == true) {
				Movement.decelerate(wallEcomponents);
			} else {
				Movement.moveDownLeft(wallEcomponents);
			}
			Driver.wallE.setLastMovement("moveDownLeft");
			
			
		} else if (Driver.wallE.getCurrentKeyPresses()[0] == "DOWN" && 
				Driver.wallE.getCurrentKeyPresses()[1] == "RIGHT") {
			if (Driver.wallE.getDecelerate() == true) {
				Movement.decelerate(wallEcomponents);
			} else {
				Movement.moveDownRight(wallEcomponents);
			}
			Driver.wallE.setLastMovement("moveDownRight");
				
		} else if (Driver.wallE.getCurrentKeyPresses()[0] == "UP") {
			if (Driver.wallE.getDecelerate() == true) {
				// Robot must decelerate after previous motion in the opposite direction
				Movement.decelerate(wallEcomponents);
			} else {
				// accelerate
				Movement.moveUp(wallEcomponents);
				Driver.wallE.setLastMovement("moveUp");
			}

		} else if (Driver.wallE.getCurrentKeyPresses()[0] == "DOWN") {
			
			if (Driver.wallE.getDecelerate() == true) {
				// Robot must decelerate after previous motion in the opposite direction
				Movement.decelerate(wallEcomponents);
			} else {
				// accelerate
				Movement.moveDown(wallEcomponents);
				Driver.wallE.setLastMovement("moveDown");
			}			

		} else if (Driver.wallE.getCurrentKeyPresses()[1] == "LEFT") {
			Movement.moveLeft();
			Driver.wallE.setLastMovement("moveLeft");
			// allows robot to turn left during deceleration
			if (Driver.wallE.getDecelerate() == true) {
				Movement.decelerate(wallEcomponents);
			}
			
			

		} else if (Driver.wallE.getCurrentKeyPresses()[1] == "RIGHT") {
			Movement.moveRight();
			Driver.wallE.setLastMovement("moveRight");
			// allows robot to turn right during deceleration
			if (Driver.wallE.getDecelerate() == true) {
				Movement.decelerate(wallEcomponents);
			}
			
			
		} else if (Driver.wallE.getCurrentKeyPresses()[0] == null) {
			Movement.decelerate(wallEcomponents);
			
			
		} 
		
		// change decelerate flag to false if speed is 0
		if (Driver.wallE.getSpeed() <= 0) {
			Driver.wallE.setDecelerate(false);
			//Driver.lastUporDown = "";
		}
	}
}
