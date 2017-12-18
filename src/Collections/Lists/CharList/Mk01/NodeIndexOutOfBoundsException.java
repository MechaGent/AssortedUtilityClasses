package Collections.Lists.CharList.Mk01;

public class NodeIndexOutOfBoundsException extends IndexOutOfBoundsException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3108846924162464634L;
	
	public NodeIndexOutOfBoundsException(int attemptedIndex, int length)
	{
		super("attempted to access index " + attemptedIndex + " of " + length);
	}
}
