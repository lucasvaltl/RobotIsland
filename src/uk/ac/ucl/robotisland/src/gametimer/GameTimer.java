/** A custom implementation of a JavaFX animation timer **/
package uk.ac.ucl.robotisland.src.gametimer;


import java.util.Arrays;

import javafx.animation.AnimationTimer;
import uk.ac.ucl.robotisland.src.loggers.CustomLevel;
import uk.ac.ucl.robotisland.src.main.Driver;

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
		if (Driver.wallE.getLapInProgress() == true && wallEInfo != null)
				Driver.wallE.setLapTimeTrialMoves(Arrays.toString(wallEInfo));
		
		/*
		 * catch index out of bounds exception that can occur when 
		 * the collision detection malfunctions 
		 * (usually never happens, but this is just a safety net)
		*/
		try {
		Driver.wallE.update();
			if (Driver.dummy != null) {
				Driver.dummy.update();
			}
		} catch (IndexOutOfBoundsException e){
			Driver.LOGGER.severe("Collision detection malfunctioned, "
					+ "Robot made it out of the racetrack");
			Driver.wallE.reset(Driver.robotType);
		}
		
	}
	
	
}
