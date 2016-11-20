package mimcore.misc;

/**
 * Log formater for MimicrEE
 * @author robertkofler
 */
public class MimicreeLogFormatter extends java.util.logging.Formatter
{
    public MimicreeLogFormatter()
    {}
    
    @Override
    public String format(java.util.logging.LogRecord record)
    {
        String msg=String.format("%tD %<tT: %s\n",new java.util.Date(record.getMillis()),record.getMessage());
        return msg;
    }
}
