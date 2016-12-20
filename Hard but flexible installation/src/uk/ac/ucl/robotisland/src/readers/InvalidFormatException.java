package uk.ac.ucl.robotisland.src.readers;

/**
 * Description: A custom exception used in the FileReaders.
 * @author Geraint and Lucas
 *
 */
public class InvalidFormatException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Description: Calls the Exception class constructor with a given message.
	 * @param message: A message to display.
	 */
	public InvalidFormatException(String message) {
        super(message);
    }
}
