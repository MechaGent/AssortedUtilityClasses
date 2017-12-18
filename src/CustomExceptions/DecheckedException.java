package CustomExceptions;

public class DecheckedException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7518531507485219528L;
	
	public DecheckedException(Exception e)
	{
		this.initCause(e);
	}
}
