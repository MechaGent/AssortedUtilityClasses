package CustomExceptions;

public class BadParseException extends IllegalArgumentException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8240401628171850797L;

	public BadParseException()
	{
		super("Bad Parse!");
	}
	
	public BadParseException(String unknown)
	{
		super("Bad Parse!\r\n\tdetails: " + unknown);
	}
	
	public BadParseException(String badlyParsedFieldName, String expectedValue, String receivedValue)
	{
		super("Bad parse of field '" + badlyParsedFieldName + "'! Was expecting '" + expectedValue + "', but got '" + receivedValue + "'");
	}
}
