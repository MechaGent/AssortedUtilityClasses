package Collections.PackedArrays.HalfByte;

import Collections.Lists.CharList.CharList;
import HandyStuff.BitTwiddler;

class HalfByteArray_StringWrapper extends HalfByteArray
{
	private final String spine;

	HalfByteArray_StringWrapper(String inSpine)
	{
		super(inSpine.length() * 2);
		this.spine = inSpine;
	}
	
	final String getSpine()
	{
		return this.spine;
	}

	@Override
	public final void setHalfByteAt(int inHalfByteIndex, byte inNewValue)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public final void setHalfByteAt(int inHalfByteIndex, int inNewValue)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public final int getHalfByteAt(int inHalfByteIndex)
	{
		final int majIndex = inHalfByteIndex >>> 1;
		final int minIndex = inHalfByteIndex & 1;

		return BitTwiddler.getNibble(this.spine.charAt(majIndex), minIndex, true);
	}

	@Override
	protected final int getRawIntAt(int inHalfByteIndex)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	protected final long getRawLongAt(int inHalfByteIndex)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	protected final int getLastPossibleIntIndex()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	protected final int getLastPossibleLongIndex()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public final String interpretAsCharString()
	{
		return this.spine;
	}

	@Override
	public final char[] interpretAsCharArr(int startOffset)
	{
		if (startOffset == 0)
		{
			return this.spine.toCharArray();
		}
		else if (startOffset % 2 == 0)
		{
			startOffset = startOffset / 2;
			final int length = this.spine.length();
			final char[] result = new char[length - startOffset];
			this.spine.getChars(startOffset, length, result, 0);
			return result;
		}
		else
		{
			return super.interpretAsCharArr(startOffset);
		}
	}

	@Override
	public final int interpretAsCharsIntoCharArr(char[] result, int resultStart)
	{
		final int length = this.spine.length();
		this.spine.getChars(0, length, result, resultStart);
		return resultStart + length;
	}

	@Override
	public final int interpretAsCharsIntoCharArr(int srcStart, int srcEnd, char[] result, int resultStart)
	{
		if(srcStart % 2 == 0)
		{
			srcStart = srcStart / 2;	//converting from index_inHalfBytes to index_inChars
			srcEnd = srcEnd / 2;
			this.spine.getChars(srcStart, srcEnd, result, resultStart);
			return resultStart + (srcEnd - srcStart);
		}
		else if(srcEnd % 2 == 1)
		{
			return super.interpretAsCharsIntoCharArr_internal_odd_noChecks(srcStart, srcEnd, result, resultStart);
		}
		else
		{
			throw new IllegalArgumentException("starting at index " + srcStart + " and ending at index " + (srcEnd - 1) + " would result in an uneven parse!");
		}
	}

	@Override
	public final CharList interpretAsChars(int startOffset)
	{
		return new CharList(this.spine);
	}

	@Override
	public final HalfByteArray copySelf()
	{
		final char[] result = new char[this.spine.length()];
		this.spine.getChars(0, result.length, result, 0);
		return new HalfByteArray_CharArrWrapper(result);
	}
	
	@Override
	public final void copyInArray(HalfByteArray inCopier)
	{
		throw new UnsupportedOperationException("This object is wrapping a String. No touchy.");
	}

	@Override
	public final void copyInArray(int inCopieeBegin, HalfByteArray inCopier)
	{
		throw new UnsupportedOperationException("This object is wrapping a String. No touchy.");
	}

	@Override
	public final void copyInArray(int inCopieeBegin, HalfByteArray inCopier, int inCopierBegin)
	{
		throw new UnsupportedOperationException("This object is wrapping a String. No touchy.");
	}
	
	@Override
	public final void copyInArray(int inCopyLength, int inCopieeBegin, HalfByteArray inCopier, int inCopierBegin)
	{
		throw new UnsupportedOperationException("This object is wrapping a String. No touchy.");
	}

	@Override
	public final HalfByteArray concatenateSelfWith(HalfByteArray_CharArrWrapper inSecond)
	{
		final char[] other = inSecond.getSpine();
		final int thisLength = this.spine.length();
		final char[] result = new char[thisLength + other.length];
		this.spine.getChars(0, thisLength, result, 0);
		System.arraycopy(other, 0, result, thisLength, other.length);
		return new HalfByteArray_CharArrWrapper(result);
	}

	@Override
	public final HalfByteArray concatenateSelfWith(HalfByteArray_StringWrapper inSecond)
	{
		final String other = inSecond.spine;
		final int thisLength = this.spine.length();
		final int otherLength = other.length();
		final char[] result = new char[thisLength + otherLength];
		this.spine.getChars(0, thisLength, result, 0);
		other.getChars(0, otherLength, result, thisLength);
		return new HalfByteArray_CharArrWrapper(result);
	}
}
