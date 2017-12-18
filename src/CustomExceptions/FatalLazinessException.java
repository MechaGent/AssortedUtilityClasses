package CustomExceptions;

public class FatalLazinessException extends IllegalArgumentException
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6132771545223456722L;

	public FatalLazinessException()
	{
		super("Fatal Lack Of Effort!");
	}
	
	public FatalLazinessException(String unknown)
	{
		super("Fatal Lack Of Effort, because: " + unknown);
	}
}