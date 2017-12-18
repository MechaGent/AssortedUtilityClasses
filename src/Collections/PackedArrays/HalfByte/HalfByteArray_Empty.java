package Collections.PackedArrays.HalfByte;

class HalfByteArray_Empty extends HalfByteArray
{
	private static final HalfByteArray_Empty EmptyArr = new HalfByteArray_Empty();
	
	HalfByteArray_Empty()
	{
		super(0);
	}
	
	static final HalfByteArray_Empty getEmptyArr()
	{
		return EmptyArr;
	}

	@Override
	public final void setHalfByteAt(int inHalfByteIndex, byte inNewValue)
	{
		throw new ArrayIndexOutOfBoundsException();
	}
	
	@Override
	public final void setHalfByteAt(int inHalfByteIndex, int inNewValue)
	{
		throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	public final int getHalfByteAt(int inHalfByteIndex)
	{
		throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	protected final int getRawIntAt(int inHalfByteIndex)
	{
		throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	protected final long getRawLongAt(int inHalfByteIndex)
	{
		throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	protected final int getLastPossibleIntIndex()
	{
		throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	protected final int getLastPossibleLongIndex()
	{
		throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	public final int matchAgainst(HalfByteArray inOther)
	{
		if(inOther.lengthInHalfBytes == 0)
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}

	@Override
	public final int matchAgainst(HalfByteArray inOther, int inStartIndex)
	{
		if(inStartIndex != 0)
		{
			throw new IndexOutOfBoundsException();
		}
		else if(inOther.lengthInHalfBytes == 0)
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}

	@Override
	public final int matchAgainst(HalfByteArray inOther, int inStartIndex, int inStartIndex_other)
	{
		if(inStartIndex != 0)
		{
			throw new IndexOutOfBoundsException();
		}
		else if(inOther.lengthInHalfBytes == 0)
		{
			if(inStartIndex_other > 0)
			{
				throw new IndexOutOfBoundsException();
			}
			else
			{
				return 0;
			}
		}
		else
		{
			return -1;
		}
	}

	@Override
	public final HalfByteArray copySelf()
	{
		return EmptyArr;
	}

	@Override
	public final void copyInArray(HalfByteArray inCopier)
	{
		throw new IndexOutOfBoundsException("you can't add things to an empty array!");
	}

	@Override
	public final void copyInArray(int inCopieeBegin, HalfByteArray inCopier)
	{
		throw new IndexOutOfBoundsException("you can't add things to an empty array!");
	}

	@Override
	public final void copyInArray(int inCopieeBegin, HalfByteArray inCopier, int inCopierBegin)
	{
		throw new IndexOutOfBoundsException("you can't add things to an empty array!");
	}

	@Override
	public final void copyInArray(int inCopyLength, int inCopieeBegin, HalfByteArray inCopier, int inCopierBegin)
	{
		throw new IndexOutOfBoundsException("you can't add things to an empty array!");
	}

	@Override
	public final HalfByteArray concatenateSelfWith(HalfByteArray_IntBack inSecond)
	{
		return inSecond.copySelf();
	}

	@Override
	public final HalfByteArray concatenateSelfWith(HalfByteArray_LongBack inSecond)
	{
		return inSecond.copySelf();
	}

	@Override
	public final HalfByteArray concatenateSelfWith(HalfByteArray_CharArrWrapper inSecond)
	{
		return inSecond.copySelf();
	}

	@Override
	public final HalfByteArray concatenateSelfWith(HalfByteArray_StringWrapper inSecond)
	{
		return inSecond.copySelf();
	}

	@Override
	public final HalfByteArray concatenateSelfWith(HalfByteArray_Empty inSecond)
	{
		return EmptyArr;
	}
}
