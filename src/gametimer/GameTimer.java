package gametimer;

import javafx.animation.AnimationTimer;
import map.Map;
import robot.CollisionDetection;
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
			// accelerate 
			
			Driver.wallE.setxCoordinate(Driver.wallE.getxCoordinate() + 
					Driver.wallE.getSpeed() * wallEcomponents[0]);
			Driver.wallE.setyCoordinate(Driver.wallE.getyCoordinate() - 
					Driver.wallE.getSpeed() * wallEcomponents[1]);
			CollisionDetection.collisionDetection(Driver.wallE, Map.blocks);
			
		} else if (Driver.currentKeyPresses[0] == "DOWN") {
			// 
			Driver.wallE.setxCoordinate(Driver.wallE.getxCoordinate() - 
					Driver.wallE.getSpeed() * wallEcomponents[0]);
			Driver.wallE.setyCoordinate(Driver.wallE.getyCoordinate() + 
					Driver.wallE.getSpeed() * wallEcomponents[1]);
			CollisionDetection.collisionDetection(Driver.wallE, Map.blocks);
			
		} else if (Driver.currentKeyPresses[1] == "LEFT") {
			// 
//			wallE.setRotate(this.getRotate() - Math.abs(this.angularVelocity));
//			System.out.println(this.getRotate());
//			
		} else if (Driver.currentKeyPresses[1] == "RIGHT") {
			//
//			wallE.setRotate(this.getRotate() + Math.abs(this.angularVelocity));
		}
	
	}
}
