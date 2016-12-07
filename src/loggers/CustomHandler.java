package loggers;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class CustomHandler extends FileHandler
{
    public CustomHandler(final Level level) throws IOException, SecurityException
    {
        super();
        super.setLevel(level);
    }

    public CustomHandler(final String s, final Level level) throws IOException, SecurityException
    {
        super(s);
        super.setLevel(level);
    }

    public CustomHandler(final String s, final boolean b, final Level level) throws IOException, SecurityException
    {
        super(s, b);
        super.setLevel(level);
    }

    public CustomHandler(final String s, final int i, final int i1, final Level level) throws IOException, SecurityException
    {
        super(s, i, i1);
        super.setLevel(level);
    }

    public CustomHandler(final String s, final int i, final int i1, final boolean b, final Level level) throws IOException, SecurityException
    {
        super(s, i, i1, b);
        super.setLevel(level);
    }

    public void setLevel() { 
    	throw new UnsupportedOperationException("Can't change after construction!");
    }

    public void publish(final LogRecord logRecord) {
        if (logRecord.getLevel().equals(super.getLevel())) {
            super.publish(logRecord);
        }
    }
}