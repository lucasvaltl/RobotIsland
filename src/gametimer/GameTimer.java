package gametimer;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import map.Map;
import robot.CollisionDetection;
import robot.Movement;
import tests.Driver;

public class GameTimer extends AnimationTimer {
	/** Class that implements a JavaFX animation timer **/

	private int timecounter = 0;

	public void handle(long now) {
		/** Event handler called every timer interval **/
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

		if (Driver.currentKeyPresses[0] == "UP" && Driver.currentKeyPresses[1] == "LEFT") {
			//

		} else if (Driver.currentKeyPresses[0] == "UP" && Driver.currentKeyPresses[1] == "RIGHT") {
			//

		} else if (Driver.currentKeyPresses[0] == "DOWN" && Driver.currentKeyPresses[1] == "LEFT") {
			//

		} else if (Driver.currentKeyPresses[0] == "DOWN" && Driver.currentKeyPresses[1] == "RIGHT") {
			//

		} else if (Driver.currentKeyPresses[0] == "UP") {
			// TODO accelerate

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
			Movement.moveLeft();
			if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
				Movement.moveRight();
				Driver.wallE.setSpeed(0);

			}

		} else if (Driver.currentKeyPresses[1] == "RIGHT") {
			//
			Movement.moveRight();
			if (CollisionDetection.collisionDetection(Driver.wallE, Map.blocks)) {
				Movement.moveLeft();
				Driver.wallE.setSpeed(0);
			}

		} else if (Driver.currentKeyPresses[0] == null) {
			Movement.deccelerate(wallEcomponents);

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
