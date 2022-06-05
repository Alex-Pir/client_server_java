package exceptions;

@SuppressWarnings("serial")
public class ReadConfigException extends Exception {
	
	public ReadConfigException()
	{
		super();
	}
	
	public ReadConfigException(String message)
	{
		super(message);
	}
	
	public ReadConfigException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
