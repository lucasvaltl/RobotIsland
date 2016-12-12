package loggers;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * Description: Custom Formatter used to log robot instructions to a file.
 * 
 * @author Geraint and Lucas
 *
 */
public class CustomFormatter extends SimpleFormatter {

	// Level instruction = Level.parse("INSTRUCTION");
	
	/** Description:  Removes the logger time stamp
	 * @param record: A log record.
	 */
	public String format(LogRecord record){
		  if(record.getLevel() == CustomLevel.INSTRUCTION){
		    return record.getMessage() + "\r\n";
		  }else{
		    return super.format(record);
		  }
		}
}
