package HandyStuff.ArrayStuff;

import CustomExceptions.EnhancedArrayIndexOutOfBoundsException;

public final class PrimArraysCopier
{
	public static final void fillRange(char[] dest, int destStart, char filler, int copyLength)
	{
		if ((destStart + copyLength) > dest.length)
		{
			throw new ArrayIndexOutOfBoundsException("tried for index " + (destStart + copyLength) + ", when length was " + dest.length);
		}

		switch (copyLength)
		{
			case 0:
			{
				return;
			}
			case 1:
			{
				dest[destStart] = filler;
				return;
			}
			case 2:
			{
				dest[destStart++] = filler;
				dest[destStart] = filler;
				return;
			}
			case 3:
			{
				dest[destStart++] = filler;
				dest[destStart++] = filler;
				dest[destStart] = filler;
				return;
			}
			default:
			{
				break;
			}
		}
		
		dest[destStart] = filler;
		dest[destStart+1] = filler;
		dest[destStart+2] = filler;
		dest[destStart+3] = filler;
		int offset = 4;
		int bulkLength = 4;
		
		while((offset + bulkLength) < copyLength)
		{
			System.arraycopy(dest, destStart, dest, destStart + offset, bulkLength);
			offset += bulkLength;
			bulkLength *= 2;
		}
		
		if(offset != copyLength)
		{
			System.arraycopy(dest, destStart, dest, destStart + offset, copyLength - offset);
		}
	}
	
	public static final void fillRange(char[] dest, int destStart, char[] filler, int numRepeats)
	{
		//System.out.println("destStart: " + destStart + "\r\nfillerLength: " + filler.length + "\r\nnumRepeats: " + numRepeats);
		
		switch(filler.length)
		{
			case 0:
			{
				return;
			}
			case 1:
			{
				switch(numRepeats)
				{
					case 0:
					{
						return;
					}
					case 1:
					{
						dest[destStart] = filler[0];
						return;
					}
					default:
					{
						PrimArraysCopier.fillRange(dest, destStart, filler[0], numRepeats);
						return;
					}
				}
			}
			default:
			{
				switch(numRepeats)
				{
					case 0:
					{
						return;
					}
					case 1:
					{
						System.arraycopy(filler, 0, dest, destStart, filler.length);
						return;
					}
					default:
					{
						break;
					}
				}
				break;
			}
		}
		
		System.arraycopy(filler, 0, dest, destStart, filler.length);
		System.arraycopy(filler, 0, dest, destStart+filler.length, filler.length);
		
		final int numTotalElements = filler.length * numRepeats;
		int offset = filler.length * 2;
		int bulkLength = offset;
		
		while((offset + bulkLength) < numTotalElements)
		{
			System.arraycopy(dest, destStart, dest, destStart + offset, bulkLength);
			offset += bulkLength;
			bulkLength *= 2;
		}
		
		if(offset != numTotalElements)
		{
			System.arraycopy(dest, destStart, dest, destStart + offset, numTotalElements - offset);
		}
	}
	
	public static final void fillRange(char[] dest, int destStart, char[] filler, int fillerStartIndex, int numRepeats)
	{
		switch(filler.length)
		{
			case 0:
			{
				return;
			}
			case 1:
			{
				switch(numRepeats)
				{
					case 0:
					{
						return;
					}
					case 1:
					{
						dest[destStart] = filler[0];
						return;
					}
					default:
					{
						PrimArraysCopier.fillRange(dest, destStart, filler[0], numRepeats);
						return;
					}
				}
			}
			default:
			{
				switch(numRepeats)
				{
					case 0:
					{
						return;
					}
					case 1:
					{
						final int firstPartialLength = filler.length - fillerStartIndex;
						System.arraycopy(filler, fillerStartIndex, dest, destStart, firstPartialLength);
						System.arraycopy(filler, 0, dest, destStart + firstPartialLength, filler.length - firstPartialLength);
						return;
					}
					default:
					{
						break;
					}
				}
				break;
			}
		}
		
		final int firstPartialLength = filler.length - fillerStartIndex;
		final int otherPartialLength = filler.length - firstPartialLength;
		int offset = destStart;
		System.arraycopy(filler, fillerStartIndex, dest, destStart, firstPartialLength);
		offset += firstPartialLength;
		System.arraycopy(filler, 0, dest, offset, filler.length);
		offset += filler.length;
		System.arraycopy(filler, 0, dest, destStart + firstPartialLength, otherPartialLength);
		offset += otherPartialLength;
		
		final int numTotalElements = filler.length * numRepeats;
		int bulkLength = offset;
		
		while((offset + bulkLength) < numTotalElements)
		{
			System.arraycopy(dest, destStart, dest, destStart + offset, bulkLength);
			offset += bulkLength;
			bulkLength *= 2;
		}
		
		if(offset != numTotalElements)
		{
			System.arraycopy(dest, destStart, dest, destStart + offset, numTotalElements - offset);
		}
	}
	
	public static final void fillRange(char[] dest, int destStart, String filler, int numRepeats)
	{
		final int fillerLength = filler.length();
		
		switch(fillerLength)
		{
			case 0:
			{
				return;
			}
			case 1:
			{
				switch(numRepeats)
				{
					case 0:
					{
						return;
					}
					case 1:
					{
						dest[destStart] = filler.charAt(0);
						return;
					}
					default:
					{
						PrimArraysCopier.fillRange(dest, destStart, filler.charAt(0), numRepeats);
						return;
					}
				}
			}
			default:
			{
				switch(numRepeats)
				{
					case 0:
					{
						return;
					}
					case 1:
					{
						filler.getChars(0, fillerLength, dest, destStart);
						//System.arraycopy(filler, 0, dest, destStart, fillerLength);
						return;
					}
					default:
					{
						break;
					}
				}
				break;
			}
		}
		
		filler.getChars(0, fillerLength, dest, destStart);
		filler.getChars(0, fillerLength, dest, destStart+fillerLength);
		
		final int numTotalElements = fillerLength * numRepeats;
		int offset = fillerLength * 2;
		int bulkLength = offset;
		
		while((offset + bulkLength) < numTotalElements)
		{
			System.arraycopy(dest, destStart, dest, destStart + offset, bulkLength);
			offset += bulkLength;
			bulkLength *= 2;
		}
		
		if(offset != numTotalElements)
		{
			System.arraycopy(dest, destStart, dest, destStart + offset, numTotalElements - offset);
		}
	}
	
	public static final void arrayCopy_boundsChecked(Object src, int srcPos, Object dest, int destPos, int length)
	{
		EnhancedArrayIndexOutOfBoundsException.checkArrayContents(src, srcPos + length - 1);
		EnhancedArrayIndexOutOfBoundsException.checkArrayContents(dest, destPos + length - 1);
		
		System.arraycopy(src, srcPos, dest, destPos, length);
	}
	
	public static final void fillRange(byte[] dest, int destStart, byte filler, int copyLength)
	{
		if ((destStart + copyLength) > dest.length)
		{
			throw new ArrayIndexOutOfBoundsException("tried for index " + (destStart + copyLength) + ", when length was " + dest.length);
		}

		switch (copyLength)
		{
			case 0:
			{
				return;
			}
			case 1:
			{
				dest[destStart] = filler;
				return;
			}
			case 2:
			{
				dest[destStart++] = filler;
				dest[destStart] = filler;
				return;
			}
			case 3:
			{
				dest[destStart++] = filler;
				dest[destStart++] = filler;
				dest[destStart] = filler;
				return;
			}
			default:
			{
				break;
			}
		}
		
		dest[destStart] = filler;
		dest[destStart+1] = filler;
		dest[destStart+2] = filler;
		dest[destStart+3] = filler;
		int offset = 4;
		int bulkLength = 4;
		
		while((offset + bulkLength) < copyLength)
		{
			System.arraycopy(dest, destStart, dest, destStart + offset, bulkLength);
			offset += bulkLength;
			bulkLength *= 2;
		}
		
		if(offset != copyLength)
		{
			System.arraycopy(dest, destStart, dest, destStart + offset, copyLength - offset);
		}
	}
	
	public static final void fillRange(byte[] dest, int destStart, byte[] filler, int numRepeats)
	{
		//System.out.println("destStart: " + destStart + "\r\nfillerLength: " + filler.length + "\r\nnumRepeats: " + numRepeats);
		
		switch(filler.length)
		{
			case 0:
			{
				return;
			}
			case 1:
			{
				switch(numRepeats)
				{
					case 0:
					{
						return;
					}
					case 1:
					{
						dest[destStart] = filler[0];
						return;
					}
					default:
					{
						PrimArraysCopier.fillRange(dest, destStart, filler[0], numRepeats);
						return;
					}
				}
			}
			default:
			{
				switch(numRepeats)
				{
					case 0:
					{
						return;
					}
					case 1:
					{
						System.arraycopy(filler, 0, dest, destStart, filler.length);
						return;
					}
					default:
					{
						break;
					}
				}
				break;
			}
		}
		
		System.arraycopy(filler, 0, dest, destStart, filler.length);
		System.arraycopy(filler, 0, dest, destStart+filler.length, filler.length);
		
		final int numTotalElements = filler.length * numRepeats;
		int offset = filler.length * 2;
		int bulkLength = offset;
		
		while((offset + bulkLength) < numTotalElements)
		{
			System.arraycopy(dest, destStart, dest, destStart + offset, bulkLength);
			offset += bulkLength;
			bulkLength *= 2;
		}
		
		if(offset != numTotalElements)
		{
			System.arraycopy(dest, destStart, dest, destStart + offset, numTotalElements - offset);
		}
	}
	
	public static final void fillRange(byte[] dest, int destStart, byte[] filler, int fillerStartIndex, int numRepeats)
	{
		switch(filler.length)
		{
			case 0:
			{
				return;
			}
			case 1:
			{
				switch(numRepeats)
				{
					case 0:
					{
						return;
					}
					case 1:
					{
						dest[destStart] = filler[0];
						return;
					}
					default:
					{
						PrimArraysCopier.fillRange(dest, destStart, filler[0], numRepeats);
						return;
					}
				}
			}
			default:
			{
				switch(numRepeats)
				{
					case 0:
					{
						return;
					}
					case 1:
					{
						final int firstPartialLength = filler.length - fillerStartIndex;
						System.arraycopy(filler, fillerStartIndex, dest, destStart, firstPartialLength);
						System.arraycopy(filler, 0, dest, destStart + firstPartialLength, filler.length - firstPartialLength);
						return;
					}
					default:
					{
						break;
					}
				}
				break;
			}
		}
		
		final int firstPartialLength = filler.length - fillerStartIndex;
		final int otherPartialLength = filler.length - firstPartialLength;
		int offset = destStart;
		System.arraycopy(filler, fillerStartIndex, dest, destStart, firstPartialLength);
		offset += firstPartialLength;
		System.arraycopy(filler, 0, dest, offset, filler.length);
		offset += filler.length;
		System.arraycopy(filler, 0, dest, destStart + firstPartialLength, otherPartialLength);
		offset += otherPartialLength;
		
		final int numTotalElements = filler.length * numRepeats;
		int bulkLength = offset;
		
		while((offset + bulkLength) < numTotalElements)
		{
			System.arraycopy(dest, destStart, dest, destStart + offset, bulkLength);
			offset += bulkLength;
			bulkLength *= 2;
		}
		
		if(offset != numTotalElements)
		{
			System.arraycopy(dest, destStart, dest, destStart + offset, numTotalElements - offset);
		}
	}
}
