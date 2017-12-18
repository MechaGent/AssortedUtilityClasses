package Collections.PackedArrays.HalfByte;

import Collections.Lists.CharList.CharList;
import Collections.Lists.CharList.linearCharsIterator;
import HandyStuff.BitTwiddler;

public class HalfByteArrayFactory
{
	private HalfByteArrayFactory()
	{
		//to prevent instantiation
	}
	
	public static HalfByteArray convertIntoArray(int in)
	{
		/*
		final HalfByteArray result = HalfByteArray.getInstance(8);
		
		result.setAt(7, (in));
		result.setAt(6, (in >>> 4));
		result.setAt(5, (in >>> 8));
		result.setAt(4, (in >>> 12));
		result.setAt(3, (in >>> 16));
		result.setAt(2, (in >>> 20));
		result.setAt(1, (in >>> 24));
		result.setAt(0, (in >>> 28));
		
		return result;
		*/
		return HalfByteArray.parseAsRawHalfByteArray(in);
	}
	
	public static HalfByteArray convertIntoArray(int ... in)
	{
		return HalfByteArray.parseAsRawHalfByteArray(in);
	}
	
	public static HalfByteArray convertIntoArray(long in)
	{
		return HalfByteArray.parseAsRawHalfByteArray(in);
	}
	
	public static HalfByteArray convertIntoArray(CharList in)
	{
		final HalfByteArray result = HalfByteArray.getInstance(in.size() * 2);
		final linearCharsIterator itsy = in.getLinearCharsIterator();
		boolean getNextChar = true;
		byte[] currChar = null;
		int arrPlace = 0;

		while ((itsy.hasNext()) || !getNextChar)
		{
			if (getNextChar)
			{
				currChar = BitTwiddler.splitCharIntoNibbles(itsy.next(), false);
				getNextChar = false;
				result.setHalfByteAt(arrPlace++, currChar[0]);
			}
			else
			{
				getNextChar = true;
				result.setHalfByteAt(arrPlace++, currChar[1]);
			}
		}
		
		return result;
	}
	
	/**
	 * deep conversion
	 * @param in
	 * @return
	 */
	public static HalfByteArray convertIntoArray(String in)
	{
		final int stringLength = in.length();
		final HalfByteArray result = HalfByteArray.getInstance(stringLength * 2);
		int inPlace = 0;
		boolean getNextChar = true;
		byte[] currChar = null;
		int arrPlace = 0;

		while ((inPlace < stringLength) || !getNextChar)
		{
			if (getNextChar)
			{
				currChar = BitTwiddler.splitCharIntoNibbles(in.charAt(inPlace++), false);
				getNextChar = false;
				result.setHalfByteAt(arrPlace++, currChar[0]);
			}
			else
			{
				getNextChar = true;
				result.setHalfByteAt(arrPlace++, currChar[1]);
			}
		}
		
		return result;
	}
	
	/**
	 * uses the string as core, doesn't do deep conversion
	 * @param in
	 * @return
	 */
	public static HalfByteArray wrapIntoArray(String in)
	{
		return new HalfByteArray_StringWrapper(in);
	}
	
	/**
	 * uses the char[] as core, doesn't do deep conversion
	 * @param in
	 * @return
	 */
	public static final HalfByteArray wrapIntoArray(char[] in)
	{
		return new HalfByteArray_CharArrWrapper(in);
	}
}
