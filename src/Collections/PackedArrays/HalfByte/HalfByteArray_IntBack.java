package Collections.PackedArrays.HalfByte;

import CustomExceptions.FatalLazinessException;
import HandyStuff.BitTwiddler;

class HalfByteArray_IntBack extends HalfByteArray_NumBack
{
	final int[] spine;

	HalfByteArray_IntBack(int inLengthInHalfBytes)
	{
		this(inLengthInHalfBytes, new int[calcSpineLength(inLengthInHalfBytes)]);
	}

	HalfByteArray_IntBack(int inLengthInHalfBytes, int[] inSpine)
	{
		super(inLengthInHalfBytes);
		this.spine = inSpine;
	}

	private static final int calcSpineLength(int lengthInHalfBytes)
	{
		final int result = lengthInHalfBytes >>> 3; // lengthInHalfBytes / 8
		final int dif = lengthInHalfBytes & 7; // lengthInHalfBytes % 8

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
		final int intIndex = inHalfByteIndex >>> 3; // lengthInHalfBytes / 8
		final int subIndex = inHalfByteIndex & 7; // lengthInHalfBytes % 8

		// final int beforeRaw = this.spine[intIndex];
		this.spine[intIndex] = BitTwiddler.setNibble(this.spine[intIndex], subIndex, inNewValue);
		// System.out.println("before: " + Integer.toHexString(beforeRaw) + "\r\nafter: " + Integer.toHexString(this.spine[intIndex]));
	}

	@Override
	public final void setHalfByteAt(int inHalfByteIndex, int inNewValue)
	{
		final int intIndex = inHalfByteIndex >>> 3; // lengthInHalfBytes / 8
		final int subIndex = inHalfByteIndex & 7; // lengthInHalfBytes % 8

		// final int beforeRaw = this.spine[intIndex];

		/*
		if(intIndex > this.spine.length)
		{
			throw new ArrayIndexOutOfBoundsException("tried for " + intIndex + " of " + this.spine.length);
		}
		else
		{
			System.out.println("tried for " + intIndex + " of " + this.spine.length);
		}
		*/

		this.spine[intIndex] = BitTwiddler.setNibble(this.spine[intIndex], subIndex, inNewValue);
		// System.out.println("before: " + Integer.toHexString(beforeRaw) + "\r\nafter: " + Integer.toHexString(this.spine[intIndex]));
	}

	@Override
	public final int getHalfByteAt(int inHalfByteIndex)
	{
		final int intIndex = inHalfByteIndex >>> 3; // lengthInHalfBytes / 8
		final int subIndex = inHalfByteIndex & 7; // lengthInHalfBytes % 8

		return BitTwiddler.getNibbleAsByte(this.spine[intIndex], subIndex);
	}
	
	protected final int getRawShortAt(int halfByteIndex)
	{
		final int result;
		final int intIndex = halfByteIndex >>> 3; // lengthInHalfBytes / 8
		final int subIndex = halfByteIndex & 7; // lengthInHalfBytes % 8
		
		/*
		 * 	this.spine[intIndex]	=	[7]		[6]		[5]		[4]		[3]		[2]		[1]		[0]
		 * this.spine[intIndex + 1]	=	[15]	[14]	[13]	[12]	[11]	[10]	[9]		[8]
		 */
		switch(subIndex)
		{
			case 0:	//nibbles 3 to 0
			{
				result = this.spine[intIndex] & 0xffff;
				break;
			}
			case 1:	//nibbles 4 to 1
			{
				result = (this.spine[intIndex] >> 4) & 0xffff;
				break;
			}
			case 2:	//nibbles 5 to 2
			{
				result = (this.spine[intIndex] >> 8) & 0xffff;
				break;
			}
			case 3:	//nibbles 6 to 3
			{
				result = (this.spine[intIndex] >> 12) & 0xffff;
				break;
			}
			case 4:	//nibbles 7 to 4
			{
				result = (this.spine[intIndex] >> 16) & 0xffff;
				break;
			}
			case 5:	//nibbles 8 to 5
			{
				result = ((this.spine[intIndex] >> 20) & 0xfff) | ((this.spine[intIndex + 1] & 0xf) << 12);
				break;
			}
			case 6:	//nibbles 9 to 6
			{
				result = ((this.spine[intIndex] >> 24) & 0xff) | ((this.spine[intIndex + 1] & 0xff) << 8);
				break;
			}
			case 7:	//nibbles 10 to 7
			{
				result = ((this.spine[intIndex] >> 28) & 0xf) | ((this.spine[intIndex + 1] & 0xfff) << 4);
				break;
			}
			default:
			{
				throw new IllegalArgumentException("unexpected subIndex: " + subIndex);
			}
		}
		
		return result;
	}

	@Override
	protected final int getRawIntAt(int inHalfByteIndex)
	{
		final int result;
		final int intIndex = inHalfByteIndex >>> 3; // lengthInHalfBytes / 8
		final int subIndex = inHalfByteIndex & 7; // lengthInHalfBytes % 8

		switch (subIndex)
		{
			case 0:
			{
				result = this.spine[intIndex];
				break;
			}
			default:
			{
				final int firstHalf = this.spine[intIndex] >>> subIndex;
				final int secondHalf = this.spine[intIndex + 1] << subIndex;
				result = firstHalf | secondHalf;
				break;
			}
		}

		return result;
	}

	@Override
	protected final long getRawLongAt(int inHalfByteIndex)
	{
		final long result;
		final int intIndex = inHalfByteIndex >>> 3; // lengthInHalfBytes / 8
		final int subIndex = inHalfByteIndex & 7; // lengthInHalfBytes % 8

		switch (subIndex)
		{
			case 0:
			{
				result = (((long) this.spine[intIndex]) & 0xffffl) | ((((long) this.spine[intIndex + 1]) & 0xffffl) << 8);
				break;
			}
			default:
			{
				final long firstHalf = (((long) this.spine[intIndex]) & 0xffffl) >>> subIndex;
				final long secondHalf = (((long) this.spine[intIndex + 1]) & 0xffffl) << subIndex;
				final long thirdHalf = (((long) this.spine[intIndex + 2]) & 0xffffl) << (subIndex + 8);
				result = firstHalf | secondHalf | thirdHalf;
				break;
			}
		}

		return result;
	}

	@Override
	protected final int getLastPossibleIntIndex()
	{
		return (this.spine.length - 1) * 8;
	}

	@Override
	protected final int getLastPossibleLongIndex()
	{
		return (this.spine.length - 2) * 8;
	}

	@Override
	public final boolean equals(HalfByteArray_IntBack inOther)
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

	@Override
	public final boolean equals(HalfByteArray_LongBack inOther)
	{
		if(this.lengthInHalfBytes == inOther.lengthInHalfBytes)
		{
			int indexThis = 0;
			int indexOther = 0;
			
			if(this.spine.length % 2 == 0)
			{
				while(indexThis < this.spine.length)
				{
					final long composite = (((long) this.spine[indexThis]) & 0xffff) | (((long) this.spine[indexThis + 1]) << 32);
					indexThis += 2;
					
					if(composite != inOther.spine[indexOther++])
					{
						return false;
					}
				}
				
				return true;
			}
			else
			{
				while(indexThis < this.spine.length - 1)
				{
					final long composite = (((long) this.spine[indexThis]) & 0xffff) | (((long) this.spine[indexThis + 1]) << 32);
					indexThis += 2;
					
					if(composite != inOther.spine[indexOther++])
					{
						return false;
					}
				}
				
				return this.spine[indexThis] == ((int) inOther.spine[indexOther]);
			}
		}
		else
		{
			return false;
		}
	}

	@Override
	public final HalfByteArray copySelf()
	{
		final int[] result = new int[this.spine.length];
		System.arraycopy(this.spine, 0, result, 0, this.spine.length);
		return new HalfByteArray_IntBack(this.lengthInHalfBytes, result);
	}

	public final void copyInArray(HalfByteArray_IntBack inCopier)
	{
		System.arraycopy(inCopier.spine, 0, this.spine, 0, inCopier.spine.length);
	}

	public final void copyInArray(int inCopieeBegin, HalfByteArray_IntBack inCopier)
	{
		if (inCopieeBegin % 2 == 0)
		{
			System.arraycopy(inCopier.spine, 0, this.spine, inCopieeBegin / 2, inCopier.spine.length);
			
			if (inCopier.lengthInHalfBytes % 2 != 0)
			{
				this.setHalfByteAt(inCopieeBegin + inCopier.lengthInHalfBytes - 1, inCopier.getLastHalfByte());
			}
		}
		else
		{
			final int limiter_this = inCopier.lengthInHalfBytes + inCopieeBegin;
			int realPos_this = inCopieeBegin / 2;
			int realPos_copier = 0;
			
			while(inCopieeBegin < limiter_this)
			{
				//TODO
			}
			
			throw new FatalLazinessException();
		}
	}

	public final void copyInArray(int inCopieeBegin, HalfByteArray_LongBack inCopier)
	{
		for (int i = 0; i < inCopier.spine.length; i++)
		{
			this.copyInRawLong(inCopieeBegin, inCopier.spine[i]);
			inCopieeBegin += 2;
		}
	}

	private final void copyInRawLong(int firstIndex, long raw)
	{
		this.spine[firstIndex++] = (int) (raw & 0xffffffff);
		this.spine[firstIndex] = (int) ((raw >>> 32) & 0xffffffff);
	}

	public final void copyInArray(int inCopieeBegin, HalfByteArray_IntBack inCopier, int inCopierBegin)
	{
		System.arraycopy(inCopier.spine, inCopierBegin, this.spine, inCopieeBegin, inCopier.spine.length - inCopierBegin + 1);
	}

	public final void copyInArray(int inCopieeBegin, HalfByteArray_LongBack inCopier, int inCopierBegin)
	{
		for (int i = inCopierBegin; i < inCopier.spine.length; i++)
		{
			this.copyInRawLong(inCopieeBegin, inCopier.spine[i]);
			inCopieeBegin += 2;
		}
	}

	public final void copyInArray(int inCopyLength, int inCopieeBegin, HalfByteArray_IntBack inCopier, int inCopierBegin)
	{
		System.arraycopy(inCopier.spine, inCopierBegin, this.spine, inCopieeBegin, inCopyLength);
	}

	public final void copyInArray(int inCopyLength, int inCopieeBegin, HalfByteArray_LongBack inCopier, int inCopierBegin)
	{
		//TODO
		throw new FatalLazinessException();
	}

	@Override
	public final HalfByteArray concatenateSelfWith(HalfByteArray_IntBack inSecond)
	{
		// TODO Auto-generated method stub
		//return super.concatenateSelfWith(inSecond);
		throw new FatalLazinessException();
	}

	@Override
	public final HalfByteArray concatenateSelfWith(HalfByteArray_LongBack inSecond)
	{
		// TODO Auto-generated method stub
		//return super.concatenateSelfWith(inSecond);
		throw new FatalLazinessException();
	}

	@Override
	public final HalfByteArray concatenateSelfWith(HalfByteArray_CharArrWrapper inSecond)
	{
		// TODO Auto-generated method stub
		//return super.concatenateSelfWith(inSecond);
		throw new FatalLazinessException();
	}

	@Override
	public final HalfByteArray concatenateSelfWith(HalfByteArray_StringWrapper inSecond)
	{
		// TODO Auto-generated method stub
		//return super.concatenateSelfWith(inSecond);
		throw new FatalLazinessException();
	}
}
