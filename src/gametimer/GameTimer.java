package gametimer;

import java.util.Arrays;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import map.Map;
import robot.CollisionDetection;
import robot.Movement;
import tests.Driver;

public class GameTimer extends AnimationTimer {
	/** Class that implements a JavaFX animation timer **/

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

		//changes the robots color back to blue after a set amount of time
		if (Driver.wallE.getFill().equals(Color.RED)) {
			timecounter += 1;
			System.out.println(timecounter);
			if (timecounter == 10) {
				Driver.wallE.setFill(Color.BLUE);
				timecounter = 0;
			}
		}

		// set speed to zero if collision is found
		if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
			Driver.wallE.setSpeed(0);
		}

		// Otherwise move appropriately
		if (Driver.currentKeyPresses[0] == "UP" && Driver.currentKeyPresses[1] == "LEFT") {
			Movement.moveUpLeft(wallEcomponents);

		} else if (Driver.currentKeyPresses[0] == "UP" && Driver.currentKeyPresses[1] == "RIGHT") {
			Movement.moveUpRight(wallEcomponents);

		} else if (Driver.currentKeyPresses[0] == "DOWN" && Driver.currentKeyPresses[1] == "LEFT") {
			Movement.moveDownLeft(wallEcomponents);
			
		} else if (Driver.currentKeyPresses[0] == "DOWN" && Driver.currentKeyPresses[1] == "RIGHT") {
			Movement.moveDownRight(wallEcomponents);
		
		} else if (Driver.currentKeyPresses[0] == "UP") {
			
			if (Driver.decelerate == true) {
				// Robot must decelerate after previous motion in the opposite direction
				Movement.decelerate(wallEcomponents);
			} else {
				// accelerate
				Movement.moveUp(wallEcomponents);
				if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
					Movement.moveDown(wallEcomponents);
					Driver.wallE.setSpeed(0);
				}
			}

		} else if (Driver.currentKeyPresses[0] == "DOWN") {
			
			if (Driver.decelerate == true) {
				// Robot must decelerate after previous motion in the opposite direction
				Movement.decelerate(wallEcomponents);
			} else {
				// accelerate
				Movement.moveDown(wallEcomponents);
				if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
					Movement.moveUp(wallEcomponents);
					Driver.wallE.setSpeed(0);
				}
			}			

		} else if (Driver.currentKeyPresses[1] == "LEFT") {
			Movement.moveLeft();
			
			// allows robot to turn left during deceleration
			if (Driver.decelerate == true) {
				Movement.decelerate(wallEcomponents);
			}
			if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
				Movement.moveRight();
				Driver.wallE.setSpeed(0);
			}

		} else if (Driver.currentKeyPresses[1] == "RIGHT") {
			Movement.moveRight();
			
			// allows robot to turn right during deceleration
			if (Driver.decelerate == true) {
				Movement.decelerate(wallEcomponents);
			}
			
			if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
				Movement.moveLeft();
				Driver.wallE.setSpeed(0);
			}

		} else if (Driver.currentKeyPresses[0] == null) {
			Movement.decelerate(wallEcomponents);
			if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
				// Decelerate in the correct direction
				if (Driver.lastUporDown.equals("DOWN")) {
					Movement.moveUp(wallEcomponents);
					Driver.wallE.setSpeed(0);
				} else if (Driver.lastUporDown.equals("UP")) {
					Movement.moveDown(wallEcomponents);
					Driver.wallE.setSpeed(0);
				}
			}
		}
		
		// change decelerate flag to false if speed is 0
		if (Driver.wallE.getSpeed() <= 0) {
			Driver.decelerate = false;
			//Driver.lastUporDown = "";
		}
	}
}
