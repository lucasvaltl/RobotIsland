package gametimer;

import java.io.File;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import map.Map;
import robot.CollisionDetection;
import robot.Movement;
import robot.Robot;
import tests.Driver;

public class GameTimer extends AnimationTimer {
	/** Class that implements a JavaFX animation timer **/

	private int timecounter = 0;

	public void handle(long now) {
		/** Event handler called every timer interval **/
		double t = (now - Driver.startnanotime) / 1000000000.0;

		final double wallEorientation = Driver.wallE.getOrientation();

		final double[] wallEcomponents = Driver.wallE.getOrientationComponents(wallEorientation);

		
		Driver.wallE.render(Driver.gc);
		System.out.println(Driver.wallE.getAxleLength());
		System.out.println(Driver.wallE.getWheelRadius());
		
		//changes the robots color back to blue after a set amount of time
//		if (Driver.wallE.getFill().equals(Color.RED)) {
//			timecounter += 1;
//			System.out.println(timecounter);
//			if (timecounter == 10) {
//				Driver.wallE.setFill(Color.BLUE);
//				timecounter = 0;
//			}
//		}

		// set speed to zero if collision is found
		if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
			Driver.wallE.setSpeed(0);
		}

		if (Driver.currentKeyPresses[0] == "UP" && Driver.currentKeyPresses[1] == "LEFT") {
			//
			Movement.moveUpLeft(wallEcomponents);
			if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
				Movement.moveUpRight(wallEcomponents);
				Driver.wallE.setSpeed(0);
			}

		} else if (Driver.currentKeyPresses[0] == "UP" && Driver.currentKeyPresses[1] == "RIGHT") {
			//
			Movement.moveUpRight(wallEcomponents);
			if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
				Movement.moveUpLeft(wallEcomponents);
				Driver.wallE.setSpeed(0);

			}

		} else if (Driver.currentKeyPresses[0] == "DOWN" && Driver.currentKeyPresses[1] == "LEFT") {
			//
			Movement.moveDownLeft(wallEcomponents);
			if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
				Movement.moveDownRight(wallEcomponents);
				Driver.wallE.setSpeed(0);

			}
		} else if (Driver.currentKeyPresses[0] == "DOWN" && Driver.currentKeyPresses[1] == "RIGHT") {
			//
			Movement.moveDownRight(wallEcomponents);
			if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
				Movement.moveDownLeft(wallEcomponents);
				Driver.wallE.setSpeed(0);

			}
			
		} else if (Driver.currentKeyPresses[0] == "UP") {
			// accelerate

			Movement.moveUp(wallEcomponents);

			if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
				Movement.moveDown(wallEcomponents);
				Driver.wallE.setSpeed(0);

			}

		} else if (Driver.currentKeyPresses[0] == "DOWN") {

			Movement.moveDown(wallEcomponents);
			if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
				Movement.moveUp(wallEcomponents);
				Driver.wallE.setSpeed(0);

			}

		} else if (Driver.currentKeyPresses[1] == "LEFT") {
			//
			
			double[] wheelspeeds= Movement.moveLeft();
			if(wheelspeeds[0]<wheelspeeds[1]){
				Image looks = new Image(new File("src/eveleft.png").toURI().toString(), Driver.wallE.getAxleLength(), Driver.wallE.getWheelRadius(), false, true);
				Driver.wallE.setImage(looks);
			}
			
			
			if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
				Movement.moveRight();
				Driver.wallE.setSpeed(0);

			}

		} else if (Driver.currentKeyPresses[1] == "RIGHT") {
			//
			double[] wheelspeeds= Movement.moveRight();
			
			if(wheelspeeds[1]<wheelspeeds[0]){
				Image looks = new Image(new File("src/eve.png").toURI().toString(), Driver.wallE.getAxleLength(), Driver.wallE.getWheelRadius(), false, true);
				Driver.wallE.setImage(looks);
			}
			if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
				Movement.moveLeft();
				Driver.wallE.setSpeed(0);
			}

		} else if (Driver.currentKeyPresses[0] == null) {
			Movement.decelerate(wallEcomponents);

			if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
				if (Driver.lastUporDown.equals("DOWN")) {
					Movement.moveUp(wallEcomponents);
					Driver.wallE.setSpeed(0);
				} else if (Driver.lastUporDown.equals("UP")) {
					Movement.moveDown(wallEcomponents);
					Driver.wallE.setSpeed(0);

				}
			}

		}
	}
}
