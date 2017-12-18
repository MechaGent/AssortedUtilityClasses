package CustomExceptions;

public class PartyGoneOutOfBoundsException extends IllegalArgumentException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5461020402858559237L;

	public PartyGoneOutOfBoundsException()
	{
		super("Contrived B-52s reference!");
	}
	
	public PartyGoneOutOfBoundsException(String unknown)
	{
		super("Contrived B-52s reference, because: " + unknown);
	}
}
