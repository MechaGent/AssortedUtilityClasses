package Collections.PackedArrays.HalfByte;

import Collections.Lists.CharList.CharList;
import Collections.Lists.CharList.CharListable;
import HandyStuff.MiscToStrings;
import HandyStuff.ArrayStuff.ArrayComparators;

public abstract class HalfByteArray implements CharListable
{
	private static final boolean diagVar_assumeSelfIsString = true;
	private static final HalfByteArray[] emptyArr = new HalfByteArray[0];

	/**
	 * in half-bytes
	 */
	final int lengthInHalfBytes;

	HalfByteArray(int inLengthInHalfBytes)
	{
		this.lengthInHalfBytes = inLengthInHalfBytes;
	}

	public static HalfByteArray getInstance(int lengthInHalfBytes)
	{
		final HalfByteArray result;

		// System.out.println("creating new array of length: " + lengthInHalfBytes);

		if (lengthInHalfBytes != 0)
		{
			if (shouldBackWithIntArr(lengthInHalfBytes))
			{
				// result = IntArrBackedArray
				result = new HalfByteArray_IntBack(lengthInHalfBytes);
				// System.out.println("new IntBack of length: " + lengthInHalfBytes);
			}
			else
			{
				// result = LongArrBackedArray
				result = new HalfByteArray_LongBack(lengthInHalfBytes);
				// System.out.println("new LongBack of length: " + lengthInHalfBytes);
			}
		}
		else
		{
			return HalfByteArray_Empty.getEmptyInstance();
		}

		return result;
	}

	public static HalfByteArray getEmptyInstance()
	{
		return HalfByteArray_Empty.getEmptyArr();
	}
	
	public static final HalfByteArray[] getEmptyArray()
	{
		return HalfByteArray.emptyArr;
	}

	static HalfByteArray parseAsRawHalfByteArray(int raw)
	{
		return parseAsRawHalfByteArray(raw, true);
	}

	static HalfByteArray parseAsRawHalfByteArray(int raw, boolean shouldFlipBits)
	{
		final int[] core = new int[1];

		if (shouldFlipBits)
		{
			final int b0 = (raw >>> 28) & 0xf; // ((raw >>> 28) & 0xf) << 0;
			final int b1 = (raw >>> 20) & 0xf0; // ((raw >>> 24) & 0xf) << 4;
			final int b2 = (raw >>> 12) & 0xf00; // ((raw >>> 20) & 0xf) << 8;
			final int b3 = (raw >>> 4) & 0xf000; // ((raw >>> 16) & 0xf) << 12;
			final int b4 = (raw << 4) & 0xf0000; // ((raw >>> 12) & 0xf) << 16;
			final int b5 = (raw << 12) & 0xf00000; // ((raw >>> 8) & 0xf) << 20;
			final int b6 = (raw << 20) & 0xf000000; // ((raw >>> 4) & 0xf) << 24;
			final int b7 = (raw << 28) & 0xf0000000; // ((raw >>> 0) & 0xf) << 28;
			core[0] = b0 | b1 | b2 | b3 | b4 | b5 | b6 | b7;
		}
		else
		{
			core[0] = raw;
		}

		return new HalfByteArray_IntBack(8, core);
	}

	static HalfByteArray parseAsRawHalfByteArray(int[] rawArr)
	{
		final int[] core = new int[rawArr.length];

		for (int i = 0; i < rawArr.length; i++)
		{
			final int raw = rawArr[i];

			/*
			 * flipping the half-bytes to minimize memory consumption
			 */
			final int b0 = (raw >>> 28) & 0xf; // ((raw >>> 28) & 0xf) << 0;
			final int b1 = (raw >>> 20) & 0xf0; // ((raw >>> 24) & 0xf) << 4;
			final int b2 = (raw >>> 12) & 0xf00; // ((raw >>> 20) & 0xf) << 8;
			final int b3 = (raw >>> 4) & 0xf000; // ((raw >>> 16) & 0xf) << 12;
			final int b4 = (raw << 4) & 0xf0000; // ((raw >>> 12) & 0xf) << 16;
			final int b5 = (raw << 12) & 0xf00000; // ((raw >>> 8) & 0xf) << 20;
			final int b6 = (raw << 20) & 0xf000000; // ((raw >>> 4) & 0xf) << 24;
			final int b7 = (raw << 28) & 0xf0000000; // ((raw >>> 0) & 0xf) << 28;

			core[i] = b0 | b1 | b2 | b3 | b4 | b5 | b6 | b7;
		}

		return new HalfByteArray_IntBack(8 * rawArr.length, core);
	}

	static HalfByteArray parseAsRawHalfByteArray(long raw)
	{
		final long result = ((raw >>> 60) & 0xfL) | // ((raw >>> 60) & 0xf) << 0;
				((raw >>> 52) & 0xf0L) | // ((raw >>> 56) & 0xf) << 4;
				((raw >>> 44) & 0xf00L) | // ((raw >>> 52) & 0xf) << 8;
				((raw >>> 36) & 0xf000L) | // ((raw >>> 48) & 0xf) << 12;
				((raw >>> 28) & 0xf0000L) | // ((raw >>> 44) & 0xf) << 16;
				((raw >>> 20) & 0xf00000L) | // ((raw >>> 40) & 0xf) << 20;
				((raw >>> 12) & 0xf000000L) | // ((raw >>> 36) & 0xf) << 24;
				((raw >>> 4) & 0xf0000000L) | // ((raw >>> 32) & 0xf) << 28;
				((raw << 4) & 0xf00000000L) | // ((raw >>> 28) & 0xf) << 32;
				((raw << 12) & 0xf000000000L) | // ((raw >>> 24) & 0xf) << 36;
				((raw << 20) & 0xf0000000000L) | // ((raw >>> 20) & 0xf) << 40;
				((raw << 28) & 0xf00000000000L) | // ((raw >>> 16) & 0xf) << 44;
				((raw << 36) & 0xf000000000000L) | // ((raw >>> 12) & 0xf) << 48;
				((raw << 44) & 0xf0000000000000L) | // ((raw >>> 8) & 0xf) << 52;
				((raw << 52) & 0xf00000000000000L) | // ((raw >>> 4) & 0xf) << 56;
				((raw << 60) & 0xf000000000000000L); // ((raw >>> 0) & 0xf) << 60;

		return new HalfByteArray_LongBack(16, new long[] {
															result });
	}

	static HalfByteArray wrapInHalfByteArray(String in)
	{
		return new HalfByteArray_StringWrapper(in);
	}

	protected static final boolean shouldBackWithIntArr(int length)
	{
		/*
		 * if( 0 < length % 16 < 9), use int[]
		 * length % 16 == length & 15, because x modulo y = (x & (y − 1)) when y is a power of 2
		 */

		final int modulo = length & 15;

		return (modulo > 0) && (modulo < 9);
	}

	/**
	 * 
	 * @return length in halfBytes
	 */
	public int length()
	{
		return this.lengthInHalfBytes;
	}

	public abstract void setHalfByteAt(int halfByteIndex, byte newValue);

	/**
	 * convenience method, only reads first 4 bits
	 * 
	 * @param halfByteIndex
	 * @param newValue
	 */
	public abstract void setHalfByteAt(int halfByteIndex, int newValue);

	public byte getHalfByteAsByteAt(int halfByteIndex)
	{
		return (byte) this.getHalfByteAt(halfByteIndex);
	}

	public abstract int getHalfByteAt(int halfByteIndex);
	
	public int getByteAt(int halfByteIndex)
	{
		int result = this.getHalfByteAt(halfByteIndex++);
		result |= this.getHalfByteAt(halfByteIndex) << 4;
		return result;
	}

	public final int getFirstHalfByte()
	{
		return this.getHalfByteAt(0);
	}

	public final int getLastHalfByte()
	{
		return this.getHalfByteAt(this.lengthInHalfBytes - 1);
	}

	protected abstract int getRawIntAt(int halfByteIndex);

	protected abstract long getRawLongAt(int halfByteIndex);

	protected boolean hasHalfByteAt(int halfByteIndex)
	{
		return halfByteIndex < this.lengthInHalfBytes;
	}

	protected boolean hasIntAt(int halfByteIndex)
	{
		return (halfByteIndex + 7) < this.lengthInHalfBytes;
	}

	protected boolean hasLongAt(int halfByteIndex)
	{
		return (halfByteIndex + 15) < this.lengthInHalfBytes;
	}

	// protected abstract HalfByteIterator getHalfByteIterator();

	protected abstract int getLastPossibleIntIndex();

	protected abstract int getLastPossibleLongIndex();
	
	public final boolean equals(HalfByteArray other)
	{
		return this.compareTo(other) == 0;
	}
	
	public final boolean equals(char[] other)
	{
		return this.equals(HalfByteArrayFactory.wrapIntoArray(other));
	}
	
	public boolean equals(HalfByteArray_CharArrWrapper other)	//non-final
	{
		return this.compareTo(other) == 0;
	}
	
	public boolean equals(HalfByteArray_Empty other)	//non-final
	{
		return this == other;	//all instances of HalfByteArray_Empty should be the same one
	}
	
	public boolean equals(HalfByteArray_IntBack other)	//non-final
	{
		return this.compareTo(other) == 0;
	}
	
	public boolean equals(HalfByteArray_LongBack other)	//non-final
	{
		return this.compareTo(other) == 0;
	}
	
	public final boolean equals(String other)
	{
		return this.equals(HalfByteArrayFactory.wrapIntoArray(other));
	}
	
	public boolean equals(HalfByteArray_StringWrapper other)	//non-final
	{
		return this.compareTo(other) == 0;
	}

	public static int findOffsetOfFirstDifference(int lengthOfCheck, HalfByteArray arr1, int arr1StartOffset, HalfByteArray arr2, int arr2StartOffset)
	{
		final int limit_LastIntIndex1 = arr1.getLastPossibleIntIndex();
		final int limit_LastIntIndex2 = arr2.getLastPossibleIntIndex();
		boolean isDone = false;
		int result = 0;

		if (arr1StartOffset == arr2StartOffset)
		{
			while (!isDone && result < lengthOfCheck)
			{
				int currentIndex1 = arr1StartOffset + result;
				int currentIndex2 = arr2StartOffset + result;

				if (currentIndex1 <= limit_LastIntIndex1 && currentIndex2 <= limit_LastIntIndex2)
				{
					// compare by int
					if (arr1.getRawIntAt(currentIndex1) == arr2.getRawIntAt(currentIndex2))
					{
						result++;
					}
					else
					{
						while (arr1.getHalfByteAsByteAt(arr1StartOffset + result) == arr2.getHalfByteAsByteAt(arr2StartOffset + result))
						{
							result++;
						}

						isDone = true;
					}
				}
				else
				{
					while ((result < lengthOfCheck) && (arr1.getHalfByteAsByteAt(arr1StartOffset + result) == arr2.getHalfByteAsByteAt(arr2StartOffset + result)))
					{
						result++;
					}

					isDone = true;
				}
			}
		}
		else
		{
			while (!isDone && result < lengthOfCheck)
			{
				if (arr1.getHalfByteAsByteAt(arr1StartOffset + result) == arr2.getHalfByteAsByteAt(arr2StartOffset + result))
				{
					result++;
				}
				else
				{
					isDone = true;
				}
			}
		}

		return result;
	}

	// @Override
	public HalfByteArray[] splitAt(int index)
	{
		return this.splitAt(index, false);
	}

	// @Override
	public HalfByteArray[] splitAt(int index, boolean indexIsLastOfFirst)
	{
		return new HalfByteArray[] {
										this.getSubArray(0, index, indexIsLastOfFirst),
										this.getSubArray(indexIsLastOfFirst ? index + 1 : index, this.lengthInHalfBytes, false) };
	}

	public HalfByteArray getSubArray(int start)
	{
		if (start >= this.lengthInHalfBytes)
		{
			final CharList errorDesc;
			final double castStart;
			final double castLength;

			if (diagVar_assumeSelfIsString)
			{
				errorDesc = this.interpretAsChars();
				castStart = ((double) start) / 2;
				castLength = ((double) this.lengthInHalfBytes) / 2;
			}
			else
			{
				errorDesc = this.toCharList();
				castStart = start;
				castLength = this.lengthInHalfBytes;
			}

			errorDesc.push("Tried to split '");

			errorDesc.add("' at index ");
			errorDesc.add_asDecString(castStart);
			errorDesc.add(", of length ");
			errorDesc.add_asDecString(castLength);

			throw new IndexOutOfBoundsException(errorDesc.toString());
		}

		final int halfByteLength = this.lengthInHalfBytes - start;

		final HalfByteArray result = getInstance(halfByteLength);

		for (int i = 0; i < halfByteLength; i++)
		{
			result.setHalfByteAt(i, this.getHalfByteAsByteAt(start++));
		}

		return result;
	}

	public HalfByteArray getSubArray(int start, int frontPadCount, int endPadCount)
	{
		final int length = this.lengthInHalfBytes - start;

		final HalfByteArray result = getInstance(length + frontPadCount + endPadCount);

		for (int i = 0; i < length; i++)
		{
			result.setHalfByteAt(i + frontPadCount, this.getHalfByteAt(i + start));
		}

		return result;
	}

	public HalfByteArray getSubArray(int start, int end, boolean endIsInclusive)
	{
		if (end > this.lengthInHalfBytes)
		{
			end = this.lengthInHalfBytes;
		}

		final int halfByteLength;

		if (start < 0)
		{
			start = 0;
			halfByteLength = endIsInclusive ? end : end - 1;
		}
		else if (start != 0)
		{
			halfByteLength = endIsInclusive ? (end - start) : (end - (start + 1));
		}
		else
		{
			halfByteLength = endIsInclusive ? end + 1 : end;
		}

		if ((halfByteLength == this.lengthInHalfBytes && start != 0))
		{
			throw new IndexOutOfBoundsException("new length: " + halfByteLength + ", total length: " + this.lengthInHalfBytes + "\r\nfrom start: " + start + " and end: " + end);
		}
		else if (halfByteLength > this.lengthInHalfBytes)
		{
			throw new IndexOutOfBoundsException("new length: " + halfByteLength + ", total length: " + this.lengthInHalfBytes);
		}

		final HalfByteArray result = getInstance(halfByteLength);

		for (int i = 0; i < halfByteLength; i++)
		{
			result.setHalfByteAt(i, this.getHalfByteAt(start++));
		}

		return result;
	}

	public HalfByteArray getSubArray(int start, int end, boolean endIsInclusive, int frontPadCount, int endPadCount)
	{
		if (end > this.lengthInHalfBytes)
		{
			end = this.lengthInHalfBytes;
		}

		final int halfByteLength;

		if (start < 0)
		{
			start = 0;
			halfByteLength = endIsInclusive ? end : end - 1;
		}
		else
		{
			halfByteLength = endIsInclusive ? (end - start) : (end - (start + 1));
		}

		if ((halfByteLength == this.lengthInHalfBytes && start != 0))
		{
			throw new IndexOutOfBoundsException("new length: " + halfByteLength + ", total length: " + this.lengthInHalfBytes + "\r\nfrom start: " + start + " and end: " + end);
		}
		else if (halfByteLength > this.lengthInHalfBytes)
		{
			throw new IndexOutOfBoundsException("new length: " + halfByteLength + ", total length: " + this.lengthInHalfBytes);
		}

		final HalfByteArray result = getInstance(halfByteLength + frontPadCount + endPadCount);

		for (int i = frontPadCount; i < halfByteLength + frontPadCount; i++)
		{
			result.setHalfByteAt(i, this.getHalfByteAt(start++));
		}

		return result;
	}

	@Override
	public String toString()
	{
		return this.toCharList(0).toString();
	}

	public CharList interpretAsChars()
	{
		return this.interpretAsChars(0);
	}

	public String interpretAsCharString()
	{
		return this.interpretAsChars().toString();
	}

	public char[] interpretAsCharArr()
	{
		final char[] result = new char[this.lengthInHalfBytes / 2];

		int i = 0;
		int resultIndex = 0;

		while (i + 1 < this.lengthInHalfBytes)
		{
			final byte first = this.getHalfByteAsByteAt(i++);
			final byte second = this.getHalfByteAsByteAt(i++);
			final char combo = (char) ((second << 4) | first);
			result[resultIndex++] = combo;
		}

		return result;
	}

	/**
	 * 
	 * @param startOffset
	 *            is in halfBytes, not characters
	 * @return
	 */
	public char[] interpretAsCharArr(int startOffset)
	{
		final char[] result = new char[(this.lengthInHalfBytes - startOffset) / 2];

		int i = startOffset;
		int resultIndex = 0;

		while (i + 1 < this.lengthInHalfBytes)
		{
			final byte first = this.getHalfByteAsByteAt(i++);
			final byte second = this.getHalfByteAsByteAt(i++);
			final char combo = (char) ((second << 4) | first);
			result[resultIndex++] = combo;
		}

		return result;
	}

	/**
	 * 
	 * @param result
	 * @param resultStart
	 * @return the new resultStart value
	 */
	public int interpretAsCharsIntoCharArr(char[] result, int resultStart)
	{
		int i = 0;

		while (i + 1 < this.lengthInHalfBytes)
		{
			final byte first = this.getHalfByteAsByteAt(i++);
			final byte second = this.getHalfByteAsByteAt(i++);
			final char combo = (char) ((second << 4) | first);
			result[resultStart++] = combo;
		}

		return resultStart;
	}

	public int interpretAsCharsIntoCharArr(int srcStart, int srcEnd, char[] result, int resultStart)
	{
		/*
		 * second condition is the optimized way of checking if both the first and last index are odd
		 */
		if ((srcStart % 2 == 0) || (srcEnd % 2 == 1))
		{
			return this.interpretAsCharsIntoCharArr_internal_odd_noChecks(srcStart, srcEnd, result, resultStart);
		}
		else
		{
			throw new IllegalArgumentException("starting at index " + srcStart + " and ending at index " + (srcEnd - 1) + " would result in an uneven parse!");
		}
	}

	protected final int interpretAsCharsIntoCharArr_internal_odd_noChecks(int srcStart, int srcEnd, char[] result, int resultStart)
	{
		while (srcStart + 1 < srcEnd)
		{
			final byte first = this.getHalfByteAsByteAt(srcStart++);
			final byte second = this.getHalfByteAsByteAt(srcStart++);
			final char combo = (char) ((second << 4) | first);
			result[resultStart++] = combo;
		}

		return resultStart;
	}

	/**
	 * interprets every pair of halfBytes as an ASCII char
	 * 
	 * @param startOffset
	 * @return
	 */
	public CharList interpretAsChars(int startOffset)
	{
		final CharList result = new CharList();

		int i = startOffset;

		while (i + 1 < this.lengthInHalfBytes)
		{
			final byte first = this.getHalfByteAsByteAt(i++);
			final byte second = this.getHalfByteAsByteAt(i++);
			final char combo = (char) ((second << 4) | first);
			result.add(combo);
		}

		return result;
	}

	/**
	 * 
	 * @param startOffset
	 *            is in halfBytes
	 * @return
	 */
	public final char interpretAsChar(int startOffset)
	{
		final int first = this.getHalfByteAt(startOffset++);
		final int second = this.getHalfByteAt(startOffset);
		return (char) ((second << 4) | first);
	}

	public int interpretAsInt()
	{
		return this.interpretAsInt(true);
	}

	public int interpretAsInt(boolean shouldFlipBits)
	{
		if (shouldFlipBits)
		{
			final int raw = this.getRawIntAt(0);

			final int b7 = (raw >>> 28) & 0xf; // ((raw >>> 28) & 0xf) << 0;
			final int b6 = (raw >>> 20) & 0xf0; // ((raw >>> 24) & 0xf) << 4;
			final int b5 = (raw >>> 12) & 0xf00; // ((raw >>> 20) & 0xf) << 8;
			final int b4 = (raw >>> 4) & 0xf000; // ((raw >>> 16) & 0xf) << 12;
			final int b3 = (raw << 4) & 0xf0000; // ((raw >>> 12) & 0xf) << 16;
			final int b2 = (raw << 12) & 0xf00000; // ((raw >>> 8) & 0xf) << 20;
			final int b1 = (raw << 20) & 0xf000000; // ((raw >>> 4) & 0xf) << 24;
			final int b0 = (raw << 28) & 0xf0000000; // ((raw >>> 0) & 0xf) << 28;

			return b0 | b1 | b2 | b3 | b4 | b5 | b6 | b7;
		}
		else
		{
			return this.getRawIntAt(0);
		}
	}

	public final long interpretAsLong()
	{
		final long raw = this.getRawLongAt(0);

		return ((raw >>> 60) & 0xfL) | // ((raw >>> 60) & 0xf) << 0;
				((raw >>> 52) & 0xf0L) | // ((raw >>> 56) & 0xf) << 4;
				((raw >>> 44) & 0xf00L) | // ((raw >>> 52) & 0xf) << 8;
				((raw >>> 36) & 0xf000L) | // ((raw >>> 48) & 0xf) << 12;
				((raw >>> 28) & 0xf0000L) | // ((raw >>> 44) & 0xf) << 16;
				((raw >>> 20) & 0xf00000L) | // ((raw >>> 40) & 0xf) << 20;
				((raw >>> 12) & 0xf000000L) | // ((raw >>> 36) & 0xf) << 24;
				((raw >>> 4) & 0xf0000000L) | // ((raw >>> 32) & 0xf) << 28;
				((raw << 4) & 0xf00000000L) | // ((raw >>> 28) & 0xf) << 32;
				((raw << 12) & 0xf000000000L) | // ((raw >>> 24) & 0xf) << 36;
				((raw << 20) & 0xf0000000000L) | // ((raw >>> 20) & 0xf) << 40;
				((raw << 28) & 0xf00000000000L) | // ((raw >>> 16) & 0xf) << 44;
				((raw << 36) & 0xf000000000000L) | // ((raw >>> 12) & 0xf) << 48;
				((raw << 44) & 0xf0000000000000L) | // ((raw >>> 8) & 0xf) << 52;
				((raw << 52) & 0xf00000000000000L) | // ((raw >>> 4) & 0xf) << 56;
				((raw << 60) & 0xf000000000000000L); // ((raw >>> 0) & 0xf) << 60;
	}
	
	public abstract HalfByteArray copySelf();

	/**
	 * functionally equivalent to calling {@code this.copyInArray(copier.length(), 0, copier, 0)}
	 * 
	 * @param copieeBegin
	 * @param copier
	 */
	public void copyInArray(HalfByteArray copier)
	{
		for (int i = 0; i < copier.lengthInHalfBytes; i++)
		{
			this.setHalfByteAt(i, copier.getHalfByteAt(i));
		}
	}
	
	/**
	 * functionally equivalent to calling {@code this.copyInArray(copier.length(), copieeBegin, copier, 0)}
	 * 
	 * @param copieeBegin
	 * @param copier
	 */
	public void copyInArray(int copieeBegin, HalfByteArray copier)
	{
		for (int i = 0; i < copier.lengthInHalfBytes; i++)
		{
			this.setHalfByteAt(i + copieeBegin, copier.getHalfByteAt(i));
		}
	}

	/**
	 * functionally equivalent to calling {@code this.copyInArray(copier.length(), copieeBegin, copier, copierBegin)}
	 * 
	 * @param copieeBegin
	 * @param copier
	 * @param copierBegin
	 */
	public void copyInArray(int copieeBegin, HalfByteArray copier, int copierBegin)
	{
		while(copierBegin < copier.lengthInHalfBytes)
		{
			this.setHalfByteAt(copieeBegin++, copier.getHalfByteAt(copierBegin++));
		}
	}

	/**
	 * copies the contents of {@code copier} into {@code this}, with the iteration through {@code copier} starting at index {@code copierBegin},
	 * and the iteration through {@code this} starting at index {@code copieeBegin}.
	 * 
	 * @param copyLength
	 * @param copieeBegin
	 * @param copierBegin
	 */
	public void copyInArray(int copyLength, int copieeBegin, HalfByteArray copier, int copierBegin)
	{
		for (int i = 0; i < copyLength; i++)
		{
			this.setHalfByteAt(i + copieeBegin, copier.getHalfByteAt(i + copierBegin));
		}
	}

	@Override
	public CharList toCharList()
	{
		return this.toCharList(0);
	}

	public CharList toCharList(int offset)
	{
		final CharList result = new CharList();

		result.add('\t', offset);

		for (int i = 0; i < this.lengthInHalfBytes; i++)
		{
			if (i != 0)
			{
				result.add(' ');
			}

			result.add(MiscToStrings.fromNibbleToHexString(this.getHalfByteAt(i), true));
		}

		return result;
	}

	/**
	 * 
	 * @param other
	 * @return either {@code other}'s last matching index (if partial match), the length of {@code other} (if full match), or -1 (if no match)
	 */
	public int matchAgainst(HalfByteArray other)
	{
		if (this == other)
		{
			// literally the same objects
			return this.lengthInHalfBytes;
		}
		else
		{
			// figure out which of the 2 is longer
			if (this.lengthInHalfBytes <= other.lengthInHalfBytes)
			{
				for (int i = 0; i < this.lengthInHalfBytes; i++)
				{
					if (this.getHalfByteAsByteAt(i) != other.getHalfByteAsByteAt(i))
					{
						// last index was the last good match
						return i - 1;
					}
				}

				return this.lengthInHalfBytes;
			}
			else
			{
				for (int i = 0; i < other.lengthInHalfBytes; i++)
				{
					if (other.getHalfByteAsByteAt(i) != this.getHalfByteAsByteAt(i))
					{
						// last index was the last good match
						return i - 1;
					}
				}

				return other.lengthInHalfBytes;
			}
		}
	}

	/**
	 * 
	 * @param other
	 * @param startIndex
	 *            the index at which {@code this} begins matching against {@code other}.
	 * @return either {@code other}'s last matching index (if partial match), the length of {@code other} (if full match), or -1 (if no match)
	 */
	public int matchAgainst(HalfByteArray other, int startIndex)
	{
		if (other.lengthInHalfBytes == 0)
		{
			return 0;
		}
		else
		{
			final int otherLength_offset = other.lengthInHalfBytes + startIndex;

			// figure out which of the 2 is longer
			if (this.lengthInHalfBytes <= otherLength_offset)
			{
				for (int i = 0; i < other.lengthInHalfBytes; i++)
				{
					if (this.getHalfByteAsByteAt(i + startIndex) != other.getHalfByteAsByteAt(i))
					{
						// last index was the last good match
						return i - 1;
					}
				}

				return other.lengthInHalfBytes;
			}
			else
			{
				final int sharedLength = this.lengthInHalfBytes - startIndex;
				for (int i = 0; i < sharedLength; i++)
				{
					if (this.getHalfByteAsByteAt(i + startIndex) != other.getHalfByteAsByteAt(i))
					{
						// last index was the last good match
						return i - 1;
					}
				}

				return sharedLength;
			}
		}
	}

	/**
	 * 
	 * @param other
	 * @param startIndex
	 *            the index at which {@code this} begins matching against {@code other}.
	 * @param startIndex_other
	 *            the index at which {@code other} begins matching against {@code this}.
	 * @return either {@code other}'s last matching index (if partial match), the length of {@code other} (if full match), or {@code startIndex_other} - 1 (if no match)
	 */
	public int matchAgainst(HalfByteArray other, int startIndex, int startIndex_other)
	{
		final int result;

		MasterBreaker:
		{
			if (startIndex == startIndex_other)
			{
				// arrays are starting at same offset

				if (this == other)
				{
					// arrays are the same object
					result = this.lengthInHalfBytes;
					break MasterBreaker;
				}
				else if (this.lengthInHalfBytes == other.lengthInHalfBytes)
				{
					// arrays are of same length

					int i = startIndex;

					for (; i < this.lengthInHalfBytes; i++)
					{
						if (this.getHalfByteAsByteAt(i) != other.getHalfByteAsByteAt(i))
						{
							// last index was the last good match
							result = i - 1;
							break MasterBreaker;
						}
					}

					result = this.lengthInHalfBytes;
					break MasterBreaker;
				}
			}

			// figure out which of the 2 is longer
			final int effectiveLength_This = this.lengthInHalfBytes - startIndex;
			final int effectiveLength_Other = other.lengthInHalfBytes - startIndex_other;

			if (effectiveLength_This <= effectiveLength_Other)
			{
				for (int i = 0; i < effectiveLength_This; i++)
				{
					if (this.getHalfByteAt(i + startIndex) != other.getHalfByteAt(i + startIndex_other))
					{
						// last index was the last good match
						result = (i - 1) + startIndex_other;
						break MasterBreaker;
					}
				}

				result = effectiveLength_This + startIndex_other;
				break MasterBreaker;
			}
			else
			{
				for (int i = 0; i < effectiveLength_Other; i++)
				{
					if (this.getHalfByteAt(i + startIndex) != other.getHalfByteAt(i + startIndex_other))
					{
						// last index was the last good match
						result = (i - 1) + startIndex_other;
						break MasterBreaker;
					}
				}

				result = other.lengthInHalfBytes;
				break MasterBreaker;
			}
		}

		return result;
	}

	public final int compareTo(HalfByteArray in)
	{
		if (this != in)
		{
			// figure out which of the 2 is longer
			if (this.lengthInHalfBytes == in.lengthInHalfBytes)
			{
				for (int i = 0; i < this.lengthInHalfBytes; i++)
				{
					final int delta = this.getHalfByteAt(i) - in.getHalfByteAt(i);

					if (delta != 0)
					{
						return delta;
					}
				}
				
				return 0;
			}
			else if(this.lengthInHalfBytes < in.lengthInHalfBytes)
			{
				for (int i = 0; i < this.lengthInHalfBytes; i++)
				{
					final int delta = this.getHalfByteAt(i) - in.getHalfByteAt(i);

					if (delta != 0)
					{
						return delta;
					}
				}
				
				return -1;
			}
			else
			{
				for (int i = 0; i < in.lengthInHalfBytes; i++)
				{
					final int delta = this.getHalfByteAt(i) - in.getHalfByteAt(i);

					if (delta != 0)
					{
						return delta;
					}
				}
				
				return 1;
			}
		}

		return 0;
	}

	public final int compareTo(char[] in)
	{
		if(this instanceof HalfByteArray_CharArrWrapper)
		{
			return ArrayComparators.compare(((HalfByteArray_CharArrWrapper) this).getSpine(), in);
		}
		else
		{
			return this.compareTo(HalfByteArrayFactory.wrapIntoArray(in));
		}
	}

	public final int compareTo(String in)
	{
		if(this instanceof HalfByteArray_StringWrapper)
		{
			final String core = this.interpretAsCharString();
			return core.compareTo(in);
		}
		else
		{
			return this.compareTo(HalfByteArrayFactory.wrapIntoArray(in));
		}
	}
	
	public final HalfByteArray concatenateSelfWith(HalfByteArray second)
	{
		final HalfByteArray result = getInstance(this.lengthInHalfBytes + second.lengthInHalfBytes);
		
		result.copyInArray(this);
		result.copyInArray(this.lengthInHalfBytes, second);
		
		return result;
	}
	
	public HalfByteArray concatenateSelfWith(HalfByteArray_IntBack second)
	{
		return this.concatenateSelfWith((HalfByteArray) second);
	}
	
	public HalfByteArray concatenateSelfWith(HalfByteArray_LongBack second)
	{
		return this.concatenateSelfWith((HalfByteArray) second);
	}
	
	public HalfByteArray concatenateSelfWith(HalfByteArray_CharArrWrapper second)
	{
		return this.concatenateSelfWith((HalfByteArray) second);
	}
	
	public HalfByteArray concatenateSelfWith(HalfByteArray_StringWrapper second)
	{
		return this.concatenateSelfWith((HalfByteArray) second);
	}
	
	public HalfByteArray concatenateSelfWith(HalfByteArray_Empty second)
	{
		return this.copySelf();
	}
}
