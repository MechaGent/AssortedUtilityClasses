package Collections.PackedArrays.HalfByte;

import HandyStuff.BitTwiddler;

class HalfByteArray_LongBack extends HalfByteArray_NumBack
{
	final long[] spine;

	HalfByteArray_LongBack(int lengthInHalfBytes)
	{
		this(lengthInHalfBytes, new long[calcSpineLength(lengthInHalfBytes)]);
	}

	HalfByteArray_LongBack(int inLengthInHalfBytes, long[] inSpine)
	{
		super(inLengthInHalfBytes);
		this.spine = inSpine;
	}

	private static final int calcSpineLength(int lengthInHalfBytes)
	{
		final int result = lengthInHalfBytes >>> 4; // lengthInHalfBytes / 16
		final int dif = lengthInHalfBytes & 15; // lengthInHalfBytes % 16

		if (dif != 0)
		{
			return result + 1;
		}
		else
		{
			return result;
		}
	}

	@Override
	public final void setHalfByteAt(int inHalfByteIndex, byte inNewValue)
	{
		final int longIndex = inHalfByteIndex >>> 4;					// lengthInHalfBytes / 16
		final int subIndex = adjustMinIndex(inHalfByteIndex & 15);		// lengthInHalfBytes % 16

		this.spine[longIndex] = BitTwiddler.setNibble(this.spine[longIndex], subIndex, inNewValue);
	}
	
	@Override
	public final void setHalfByteAt(int inHalfByteIndex, int inNewValue)
	{
		final int longIndex = inHalfByteIndex >>> 4;					// lengthInHalfBytes / 16
		final int subIndex = adjustMinIndex(inHalfByteIndex & 15);		// lengthInHalfBytes % 16

		this.spine[longIndex] = BitTwiddler.setNibble(this.spine[longIndex], subIndex, inNewValue);
	}

	@Override
	public final int getHalfByteAt(int inHalfByteIndex)
	{
		final int longIndex = inHalfByteIndex >>> 4;					// lengthInHalfBytes / 16
		final int subIndex = adjustMinIndex(inHalfByteIndex & 15);		// lengthInHalfBytes % 16

		return BitTwiddler.getNibble(this.spine[longIndex], subIndex);
	}

	@Override
	protected final int getRawIntAt(int inHalfByteIndex)
	{
		final int longIndex = inHalfByteIndex >>> 4;					// lengthInHalfBytes / 16
		final int subIndex = adjustMinIndex(inHalfByteIndex & 15);		// lengthInHalfBytes % 16
		final int result;

		switch (subIndex)
		{
			case 0:
			{
				result = (int) this.spine[longIndex];
				break;
			}
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			{
				result = (int) (this.spine[longIndex] >>> subIndex);
				break;
			}
			default:
			{
				final int firstHalf = (int) (this.spine[longIndex] >>> subIndex);
				final int secondHalf = (int) (this.spine[longIndex] << subIndex);
				result = firstHalf | secondHalf;
				break;
			}
		}

		return result;
	}

	@Override
	protected final long getRawLongAt(int inHalfByteIndex)
	{
		final int longIndex = inHalfByteIndex >>> 4;					// lengthInHalfBytes / 16
		final int subIndex = adjustMinIndex(inHalfByteIndex & 15);		// lengthInHalfBytes % 16
		final long result;

		switch (subIndex)
		{
			case 0:
			{
				result = this.spine[longIndex];
				break;
			}
			default:
			{
				final long firstHalf = this.spine[longIndex] >>> subIndex;
				final long secondHalf = this.spine[longIndex + 1] << subIndex;
				result = firstHalf | secondHalf;
				break;
			}
		}

		return result;
	}

	@Override
	protected final int getLastPossibleIntIndex()
	{
		return (this.spine.length - 1) * 16;
	}

	@Override
	protected final int getLastPossibleLongIndex()
	{
		return (this.spine.length - 2) * 16;
	}
	
	@Override
	public final boolean equals(HalfByteArray_IntBack inOther)
	{
		return inOther.equals(this);
	}

	@Override
	public final boolean equals(HalfByteArray_LongBack inOther)
	{
		if(this.lengthInHalfBytes == inOther.lengthInHalfBytes)
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
	
	/**
	 * 
	 * @param oldMin
	 * @return
	 */
	private static final int adjustMinIndex(int oldMin)
	{
		/*
		final int result;
		
		if(oldMin < 8)
		{
			result = oldMin + 8;
		}
		else
		{
			result = oldMin - 8;
		}
		
		return result;
		*/
		
		return oldMin;
	}

	@Override
	public final HalfByteArray copySelf()
	{
		final long[] result = new long[this.spine.length];
		System.arraycopy(this.spine, 0, result, 0, this.spine.length);
		return new HalfByteArray_LongBack(this.lengthInHalfBytes, result);
	}
}
