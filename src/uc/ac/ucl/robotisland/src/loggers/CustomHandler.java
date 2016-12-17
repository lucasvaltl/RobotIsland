package uc.ac.ucl.robotisland.src.loggers;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Description: A custom logger handler used to enable logging to multiple files.
 * @author Geraint and Lucas
 */
public class CustomHandler extends FileHandler
{
	/**
	 * Description: A custom handler for a given logger level.
	 * @param level: A logger level.
	 * @throws IOException: Signals that an I/O exception of some sort has occurred.
	 * @throws SecurityException: Thrown by the security manager to indicate a security violation.
	 */
    public CustomHandler(final Level level) throws IOException, SecurityException
    {
        super();
        super.setLevel(level);
    }

    /**
     * Description: A custom handler for a given logger level and output file.
     * @param s: The name of the output file
     * @param level: A logger level.
     * @throws IOException: Signals that an I/O exception of some sort has occurred
     * @throws SecurityException: Thrown by the security manager to indicate a security violation.
     */
    public CustomHandler(final String s, final Level level) throws IOException, SecurityException
    {
        super(s);
        super.setLevel(level);
    }

    /**
     * Description: A custom handler for a given logger level and output file, with optional append
     * @param s: The name of the output file.
     * @param b: Optionally append to the output file.
     * @param level: A logger level.
     * @throws IOException: Signals that an I/O exception of some sort has occurred
     * @throws SecurityException: Thrown by the security manager to indicate a security violation.
     */
    public CustomHandler(final String s, final boolean b, final Level level) throws IOException, SecurityException
    {
        super(s, b);
        super.setLevel(level);
    }

    /**
     * Description: A custom handler for a given logger level and output file.
     * When (approximately) the given limit has been written to one file, 
     * another file will be opened. 
     * The output will cycle through a set of count files.
     * @param s: The name of the output file.
     * @param i: The maximum number of bytes to write to any one file
     * @param i1: The number of files to use.
     * @param level: A logger level.
     * @throws IOException: Signals that an I/O exception of some sort has occurred.
     * @throws SecurityException: Thrown by the security manager to indicate a security violation.
     */
    public CustomHandler(final String s, final int i, final int i1, final Level level) throws IOException, SecurityException
    {
        super(s, i, i1);
        super.setLevel(level);
    }

    /**
     * Description: A custom handler to write to a set of files 
     * with optional append. 
     * When (approximately) the given limit has been written to one file, 
     * another file will be opened. 
     * The output will cycle through a set of count files.
     * @param s: The name of the output file
     * @param i: The maximum number of bytes to write to any one file.
     * @param i1: THe number of files to use
     * @param b: Optional append.
     * @param level: A logger level.
     * @throws IOException: Signals that an I/O exception of some sort has occurred.
     * @throws SecurityException: Thrown by the security manager to indicate a security violation.
     */
    public CustomHandler(final String s, final int i, final int i1, final boolean b, final Level level) throws IOException, SecurityException
    {
        super(s, i, i1, b);
        super.setLevel(level);
    }
    
    /**
     * Description: Format and publish a LogRecord.
     * @param logRecord: description of the log event. 
     * A null record is silently ignored and is not published
     */
    public void publish(final LogRecord logRecord) {
        if (logRecord.getLevel().equals(super.getLevel())) {
            super.publish(logRecord);
        }
    }

    /**
     * Description: Override the FileHandler setLevel function.
     */
    public void setLevel() { 
    	throw new UnsupportedOperationException("Can't change after construction!");
    }
}