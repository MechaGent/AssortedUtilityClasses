package Collections.PackedArrays.HalfByte;

import HandyStuff.BitTwiddler;

class HalfByteArray_CharArrWrapper extends HalfByteArray
{
	private final char[] spine;

	HalfByteArray_CharArrWrapper(char[] inSpine)
	{
		super(inSpine.length * 2);
		this.spine = inSpine;
	}
	
	final char[] getSpine()
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
		
		return BitTwiddler.getNibble(this.spine[majIndex], minIndex, true);
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
	public final boolean equals(HalfByteArray_CharArrWrapper inOther)
	{
		if(this.spine.length == inOther.spine.length)
		{
			for(int i = 0; i < this.spine.length; i++)
			{
				if(this.spine[i] != inOther.spine[i])
				{
					return false;
				}
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public final boolean equals(HalfByteArray_StringWrapper inOther)
	{
		final String other = inOther.getSpine();
		
		if(this.spine.length == other.length())
		{
			for(int i = 0; i < this.spine.length; i++)
			{
				if(this.spine[i] != other.charAt(i))
				{
					return false;
				}
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public final char[] interpretAsCharArr()
	{
		return this.spine;
	}
	
	@Override
	public final char[] interpretAsCharArr(int startOffset)
	{
		if(startOffset == 0)
		{
			return this.spine;
		}
		else
		{
			return super.interpretAsCharArr(startOffset);
		}
	}

	@Override
	public final HalfByteArray copySelf()
	{
		final char[] nextSpine = new char[this.spine.length];
		System.arraycopy(this.spine, 0, nextSpine, 0, this.spine.length);
		return new HalfByteArray_CharArrWrapper(nextSpine);
	}

	@Override
	public final void copyInArray(HalfByteArray inCopier)
	{
		throw new UnsupportedOperationException("This object is wrapping a char[]. No touchy.");
	}

	@Override
	public final void copyInArray(int inCopieeBegin, HalfByteArray inCopier)
	{
		throw new UnsupportedOperationException("This object is wrapping a char[]. No touchy.");
	}

	@Override
	public final void copyInArray(int inCopieeBegin, HalfByteArray inCopier, int inCopierBegin)
	{
		throw new UnsupportedOperationException("This object is wrapping a char[]. No touchy.");
	}
	
	@Override
	public final void copyInArray(int inCopyLength, int inCopieeBegin, HalfByteArray inCopier, int inCopierBegin)
	{
		throw new UnsupportedOperationException("This object is wrapping a char[]. No touchy.");
	}

	@Override
	public final HalfByteArray concatenateSelfWith(HalfByteArray_CharArrWrapper inSecond)
	{
		final char[] other = inSecond.spine;
		final char[] result = new char[this.spine.length + other.length];
		System.arraycopy(this, 0, result, 0, this.spine.length);
		System.arraycopy(other, 0, result, this.spine.length, other.length);
		return new HalfByteArray_CharArrWrapper(result);
	}

	@Override
	public final HalfByteArray concatenateSelfWith(HalfByteArray_StringWrapper inSecond)
	{
		final String other = inSecond.getSpine();
		final char[] result = new char[this.spine.length + other.length()];
		System.arraycopy(this, 0, result, 0, this.spine.length);
		other.getChars(0, other.length(), result, this.spine.length);
		return new HalfByteArray_CharArrWrapper(result);
	}
}
