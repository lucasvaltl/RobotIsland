package loggers;

import java.util.logging.Level;

public class CustomLevel extends Level {
	public static final Level INSTRUCTION = new CustomLevel("INSTRUCTION", Level.SEVERE.intValue() + 1);
	
	public CustomLevel(String name, int value) {
		super(name, value);
	}
	
}
