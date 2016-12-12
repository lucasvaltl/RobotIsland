package readers;

/**
 * Description: A custom exception used in the FileReaders.
 * @author Geraint and Lucas
 *
 */
public class InvalidFormatException extends Exception{
	
	/**
	 * Description: Calls the Exception class constructor with a given message.
	 * @param message: A message to display.
	 */
	public InvalidFormatException(String message) {
        super(message);
    }
}
