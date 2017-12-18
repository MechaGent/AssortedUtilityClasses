package CustomExceptions;

public class UnhandledEnumException extends IllegalArgumentException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7302051146685075253L;
	
	/*
	public UnhandledEnumException()
	{
		super();
	}
	*/
	
	public UnhandledEnumException(Enum<?> unknown)
	{
		super("Unhandled enum value: " + unknown.toString());
	}
	
	public UnhandledEnumException(String unknown)
	{
		super("Unhandled enum value: " + unknown);
	}
	
	public UnhandledEnumException(char[] unknown)
	{
		this(new String(unknown));
	}
	
	public UnhandledEnumException(char pseudoEnum)
	{
		super("Unhandled enum value: " + pseudoEnum);
	}
	
	public UnhandledEnumException(int pseudoEnum)
	{
		super("Unhandled enum value: " + pseudoEnum);
	}
	
	public UnhandledEnumException(Incubator.JSONish.Mk04.LotusPojo in)
	{
		super(generateMessage(in));
	}
	
	private static final String generateMessage(Incubator.JSONish.Mk04.LotusPojo in)
	{
		if(in == null)
		{
			return "class: null!";
		}
		else
		{
			return "class: " + in.getClass().toString() + "\r\n\r\n" + in.toCharList().toString();
		}
	}
}
