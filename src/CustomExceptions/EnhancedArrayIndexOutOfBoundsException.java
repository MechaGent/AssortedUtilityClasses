package CustomExceptions;

public class EnhancedArrayIndexOutOfBoundsException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5104593963409243815L;

	public EnhancedArrayIndexOutOfBoundsException(Object array, int attemptedIndex)
	{
		this(getArrayLength(array), attemptedIndex);
	}
	
	public EnhancedArrayIndexOutOfBoundsException(long arrayLength, long attemptedIndex)
	{
		this(generateMessage(arrayLength, attemptedIndex));
	}
	
	public EnhancedArrayIndexOutOfBoundsException(long arrayLength, long attemptedOverallIndex, long subArrayLength, long attemptedSubIndex)
	{
		this("\r\n\t" + generateMessage(arrayLength, attemptedOverallIndex) + "\r\n\t" + generateMessage(subArrayLength, attemptedSubIndex));
	}
	
	private EnhancedArrayIndexOutOfBoundsException(String message)
	{
		super(message);
	}
	
	public static final void checkArrayContents(Object array, int attemptedIndex)
	{
		final int arrayLength = getArrayLength(array);
		
		if(attemptedIndex >= arrayLength)
		{
			throw new EnhancedArrayIndexOutOfBoundsException(arrayLength, attemptedIndex);
		}
	}
	
	public static final void checkArrayRange(Object array, int startIndex, int rangeLength)
	{
		final int arrayLength = getArrayLength(array);
		
		if(startIndex >= arrayLength)
		{
			throw new EnhancedArrayIndexOutOfBoundsException(arrayLength, startIndex);
		}
		
		final int lastIndex = (startIndex + rangeLength) - 1;
		
		if(lastIndex >= arrayLength)
		{
			throw new EnhancedArrayIndexOutOfBoundsException(generateMessage(arrayLength, lastIndex) + ", and startIndex was " + startIndex);
		}
	}
	
	public static final void checkArrayCopy(Object src, int srcPos, Object dest, int destPos, int length)
	{
		checkArrayRange(src, srcPos, length);
		checkArrayRange(dest, destPos, length);
	}
	
	private static final int getArrayLength(Object array)
	{
		final int arrayLength;

		if (array instanceof boolean[])
		{
			arrayLength = ((boolean[]) array).length;
		}
		else if (array instanceof byte[])
		{
			arrayLength = ((byte[]) array).length;
		}
		else if (array instanceof short[])
		{
			arrayLength = ((short[]) array).length;
		}
		else if (array instanceof char[])
		{
			arrayLength = ((char[]) array).length;
		}
		else if (array instanceof int[])
		{
			arrayLength = ((int[]) array).length;
		}
		else if (array instanceof float[])
		{
			arrayLength = ((float[]) array).length;
		}
		else if (array instanceof long[])
		{
			arrayLength = ((long[]) array).length;
		}
		else if (array instanceof double[])
		{
			arrayLength = ((double[]) array).length;
		}
		else if(array instanceof String)
		{
			arrayLength = ((String) array).length();
		}
		else if (array instanceof Object[])
		{
			arrayLength = ((Object[]) array).length;
		}
		else
		{
			throw new IllegalArgumentException("\"array\" isn't even an array, you fool! It's a " + array.getClass().toString());
		}
		
		return arrayLength;
	}
	
	private static final String generateMessage(long arrayLength, long attemptedIndex)
	{
		return "tried for index " + attemptedIndex + ", when length was " + arrayLength;
	}
}
