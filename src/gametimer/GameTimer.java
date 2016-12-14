/** A custom implementation of a JavaFX animation timer **/
package gametimer;


import java.util.Arrays;
import loggers.CustomLevel;

import javafx.animation.AnimationTimer;
import tests.Driver;

/** Description: A custom implementation of a JavaFX animation timer 
 * 
 * @author Geraint and Lucas
 *
 */
public class GameTimer extends AnimationTimer {

	int i = 0;
	int j = 0;
	
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
		
		/*
		 * catch index out of bounds exeption that can occur when 
		 * the collision detection malfunctions 
		 * (usually never happens, but this is just a safety net)
		*/
		try{
		Driver.wallE.update();
		if (Driver.dummy != null) {
			Driver.dummy.update();
		}
		}catch (IndexOutOfBoundsException e){
			Driver.LOGGER.severe("Collision detection malfunctioned, "
					+ "Robot made it out of the racetrack");
			Driver.wallE.reset(Driver.robotType);
		}
		
	}
	
	
}
