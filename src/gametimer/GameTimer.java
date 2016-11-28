package gametimer;

import javafx.animation.AnimationTimer;
import tests.Driver;

public class GameTimer extends AnimationTimer {
	/** Class that implements a JavaFX animation timer **/
	
	public void handle(long now) {
		/** Event handler called every timer interval **/
		double t = (now - Driver.startnanotime) / 1000000000.0;
		
		final double wallEorientation = Driver.wallE.getOrientation();
		
		final double[] wallEcomponents = Driver.wallE.getOrientationComponents(wallEorientation);
		
		if (Driver.currentKeyPresses[0] == "UP" && 
				Driver.currentKeyPresses[1] == "LEFT") {
			// 
			
		} else if (Driver.currentKeyPresses[0] == "UP" && 
				Driver.currentKeyPresses[1] == "RIGHT") {
			// 
			
		} else if (Driver.currentKeyPresses[0] == "DOWN" && 
				Driver.currentKeyPresses[1] == "LEFT") {
			//
			
		} else if (Driver.currentKeyPresses[0] == "DOWN" && 
				Driver.currentKeyPresses[1] == "RIGHT") {
			//
			
		} else if (Driver.currentKeyPresses[0] == "UP") {
			// TODO accelerate 
			Driver.wallE.setSpeed(Driver.wallE.getSpeed() + 
				Driver.wallE.getAcceleration());
			Driver.wallE.setxCoordinate(Driver.wallE.getxCoordinate() + 
					Driver.wallE.getSpeed() * wallEcomponents[0]);
			Driver.wallE.setyCoordinate(Driver.wallE.getyCoordinate() - 
					Driver.wallE.getSpeed() * wallEcomponents[1]);
			
		} else if (Driver.currentKeyPresses[0] == "DOWN") {
			Driver.wallE.setSpeed(Driver.wallE.getSpeed() + 
					Driver.wallE.getAcceleration());
			Driver.wallE.setxCoordinate(Driver.wallE.getxCoordinate() - 
					Driver.wallE.getSpeed() * wallEcomponents[0]);
			Driver.wallE.setyCoordinate(Driver.wallE.getyCoordinate() + 
					Driver.wallE.getSpeed() * wallEcomponents[1]);
			
		} else if (Driver.currentKeyPresses[1] == "LEFT") {
			//
			Driver.wallE.setRotate(Driver.wallE.getRotate() - 
					Math.abs(Driver.wallE.getAngularVelocity()));
			
		} else if (Driver.currentKeyPresses[1] == "RIGHT") {
			//
			Driver.wallE.setRotate(Driver.wallE.getRotate() + 
					Math.abs(Driver.wallE.getAngularVelocity()));
			
		} else if (Driver.currentKeyPresses[0] == null) {
			// TODO Decelerate
			System.out.println(Driver.wallE.getSpeed());
			System.out.println(Driver.wallE.getAcceleration());
			double speed = Driver.wallE.getSpeed() - Driver.wallE.getAcceleration();
			speed = (speed <  0) ? 0 : speed;
			Driver.wallE.setSpeed(speed);
//			System.out.println(speed);
			
			if (Driver.lastUporDown.equals("DOWN")) {
				Driver.wallE.setxCoordinate(Driver.wallE.getxCoordinate() - 
						Driver.wallE.getSpeed() * wallEcomponents[0]);
				Driver.wallE.setyCoordinate(Driver.wallE.getyCoordinate() +
						Driver.wallE.getSpeed() * wallEcomponents[1]);
			} else if (Driver.lastUporDown.equals("UP")) {
				Driver.wallE.setxCoordinate(Driver.wallE.getxCoordinate() + 
						Driver.wallE.getSpeed() * wallEcomponents[0]);
				Driver.wallE.setyCoordinate(Driver.wallE.getyCoordinate() -
						Driver.wallE.getSpeed() * wallEcomponents[1]);
			}	
		}
	}
}
