package uk.ac.ucl.robotisland.src.loggers;

import java.util.logging.Level;

/**
 * Description: Initialise a custom logger Level to send robot instructions to a file.
 * @author Geraint and Lucas
 *
 */
public class CustomLevel extends Level {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Level INSTRUCTION = new CustomLevel("INSTRUCTION", Level.SEVERE.intValue() + 1);
	
	/**
	 * Description: Create a new custom level with a given name and value
	 * @param name: The name of the logger level.
	 * @param value: The value of the logger level.
	 */
	public CustomLevel(String name, int value) {
		super(name, value);
	}
	
}
