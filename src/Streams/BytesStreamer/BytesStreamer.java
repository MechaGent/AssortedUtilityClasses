package Streams.BytesStreamer;

import Collections.Lists.CharList.CharList;
import HandyStuff.BitTwiddler;
import HandyStuff.CharConverters.CharConverter.Conversions;
import HandyStuff.HandyEnums.EndianSettings;
import HandyStuff.HandyEnums.Primitives;

public abstract class BytesStreamer
{
	private static final int stringLengthSafetyCutoff = 1000;

	protected static final int numBits_Byte = 8;
	protected static final int numBits_Char = 8;
	protected static final int numBits_Short = 16;
	protected static final int numBits_Int = 32;
	protected static final int numBits_Float = 32;
	protected static final int numBits_Long = 64;
	protected static final int numBits_Double = 64;

	/**
	 * isolates the [index] bit, when used as a mask
	 */
	// private static final int[] bitMasks_LeftBit = new int[] { 0x0, 0x1, 0x3, 0x7, 0xf, 0x1f, 0x3f, 0x7f };

	/**
	 * this is used as the default, but can be re-specified locally via method argument
	 */
	protected final EndianSettings inputSigEnd;

	/**
	 * this is always going to be the output format.
	 */
	protected final EndianSettings outputSigEnd;
	
	protected final Conversions DefaultCharConversion;

	// protected int bitbuffer;
	// protected int numBitsInBuffer;

	public BytesStreamer()
	{
		this(EndianSettings.LeftBitLeftByte);
	}

	protected BytesStreamer(EndianSettings inInputSigEnd)
	{
		this(inInputSigEnd, EndianSettings.LeftBitLeftByte);
	}

	protected BytesStreamer(EndianSettings inInputSigEnd, EndianSettings inOutputSigEnd)
	{
		this.inputSigEnd = inInputSigEnd;
		this.outputSigEnd = inOutputSigEnd;
		this.DefaultCharConversion = Conversions.Default;

		// this.bitbuffer = 0;
		// this.numBitsInBuffer = 0;
	}

	public final boolean canSafelyConsume(int quantityOfBytes)
	{
		return this.canSafelyConsume_internal(quantityOfBytes, Primitives.Byte);
	}

	public final boolean canSafelyConsume(int quantity, Primitives type)
	{
		return this.canSafelyConsume_internal(quantity, type);
	}

	public final boolean hasNextByte()
	{
		return this.canSafelyConsume_internal(1, Primitives.Byte);
	}

	/**
	 * will never need to check bits
	 * 
	 * @param quantity
	 * @param type
	 * @return
	 */
	protected abstract boolean canSafelyConsume_internal(int quantity, Primitives type);

	public abstract long getPosition();

	public abstract void setToPosition(long position);

	public abstract void advancePosition(long delta);

	/**
	 * does not use any formatting, bits- or bytes- wise
	 * 
	 * @return
	 */
	protected abstract byte getNextByte_internal();

	public final int getNextByte()
	{
		return this.getNextByte(this.inputSigEnd);
	}

	public final int getNextByte(EndianSettings sigEnd)
	{
		return BitTwiddler.normalizeByte(this.getNextByte_internal(), sigEnd);
	}

	public final int[] getNextByteArray(int length)
	{
		return this.getNextByteArray(this.inputSigEnd, length);
	}

	public final int[] getNextByteArray(EndianSettings sigEnd, int length)
	{
		final int[] result = new int[length];

		for (int i = 0; i < length; i++)
		{
			result[i] = this.getNextByte(sigEnd);
		}

		return result;
	}
	
	public final byte[] getNextByteArray_Literally(int length)
	{
		return this.getNextByteArray_Literally(this.inputSigEnd, length);
	}
	
	public final byte[] getNextByteArray_Literally(EndianSettings sigEnd, int length)
	{
		final byte[] result = new byte[length];
		
		for (int i = 0; i < length; i++)
		{
			result[i] = (byte) this.getNextByte(sigEnd);
		}

		return result;
	}

	public final char getNextChar()
	{
		return this.getNextChar(this.inputSigEnd);
	}
	
	/**
	 * used for parsing bytes into codepoints
	 */
	public static final int CharMask_OneByte = 		0b10000000;
	
	/**
	 * used for parsing bytes into codepoints
	 */
	public static final int CharMask_TwoByte = 		0b11000000;
	
	/**
	 * used for parsing bytes into codepoints
	 */
	public static final int CharMask_ThreeByte = 	0b11100000;

	/**
	 * partial compliance with UTF-8 - can only handle up to 2 bytes
	 * @param sigEnd
	 * @return
	 */
	public char getNextChar(EndianSettings sigEnd) // non-final
	{
		//return (char) this.getNextByte(sigEnd);
		
		//final int firstByte = this.getNextByte(sigEnd);
		//return (char) firstByte;
		
		final int firstByte = this.getNextByte(sigEnd);
		final char result;
		
		if((firstByte & CharMask_OneByte) == 0)
		{
			// one byte
			result = (char) firstByte;
		}
		else if((firstByte & CharMask_TwoByte) == CharMask_OneByte)
		{
			// two byte
			//System.out.println("firstByte: " + ((char) firstByte));
			result = (char) BitTwiddler.parseShort(sigEnd, firstByte, this.getNextByte(sigEnd));
		}
		else
		{
			result = (char) BitTwiddler.parseShort(sigEnd, firstByte, this.getNextByte(sigEnd));
			//result = (char) firstByte;
			//throw new UnhandledEnumException(NumNodeConversion.toBinString(firstByte));
		}
		
		return result;

		/*
		if ((firstByte & CharMask_TwoByte) == 0)
		{
			// one byte
			return (char) firstByte;
		}
		else if ((firstByte & CharMask_ThreeByte) == CharMask_TwoByte)
		{
			// two byte
			return (char) BitTwiddler.parseShort(sigEnd, firstByte, this.getNextByte(sigEnd));
			//return (char) ((firstByte << 8) | this.getNextByte(sigEnd));
		}
		else
		{
			throw new UnhandledEnumException(NumNodeConversion.toBinString(firstByte));
		}
		*/
	}

	public final char getNextChar_NoWhitespace()
	{
		return this.getNextChar_NoWhitespace(this.inputSigEnd);
	}

	public final char getNextChar_NoWhitespace(EndianSettings sigEnd)
	{
		char result;

		do
		{
			result = this.getNextChar(sigEnd);

			switch (result)
			{
				case ' ':
				case '\t':
				case '\r':
				case '\n':
				{
					break;
				}
				default:
				{
					return result;
				}
			}
		} while (this.hasNextByte());

		throw new IndexOutOfBoundsException();
	}

	public final char[] getNextCharArray(int length)
	{
		return this.getNextCharArray(this.inputSigEnd, length);
	}

	public char[] getNextCharArray(EndianSettings sigEnd, int length) // non-final
	{
		final char[] result = new char[length];

		for (int i = 0; i < length; i++)
		{
			result[i] = this.getNextChar(sigEnd);
		}

		return result;
	}
	
	public final char getNextSimpleChar()
	{
		return this.getNextSimpleChar(this.inputSigEnd);
	}
	
	public final char getNextSimpleChar(EndianSettings sigEnd)
	{
		return (char) this.getNextByte(sigEnd);
	}
	
	public final char[] getNextSimpleCharArray(int length)
	{
		return this.getNextSimpleCharArray(this.inputSigEnd, length);
	}
	
	public final char[] getNextSimpleCharArray(EndianSettings sigEnd, int length)
	{
		final char[] result = new char[length];
		
		for(int i = 0; i < length; i++)
		{
			result[i] = this.getNextSimpleChar(sigEnd);
		}
		
		return result;
	}

	/**
	 * uses utf-8
	 * @return
	 */
	public final int getNextExtendedChar()
	{
		//return this.getNextByte();
		return this.getNextExtendedChar(this.inputSigEnd);
	}
	
	public final int getNextExtendedChar(EndianSettings sigEnd)
	{
		/*
		final int firstByte = this.getNextByte(sigEnd);
		final int result;
		
		if((firstByte & CharMask_OneByte) == 0)
		{
			// one byte
			result = firstByte;
		}
		else if((firstByte & CharMask_TwoByte) == CharMask_OneByte)
		{
			// two byte
			//System.out.println("firstByte: " + ((char) firstByte));
			result = BitTwiddler.parseShort(sigEnd, firstByte, this.getNextByte(sigEnd));
		}
		else if((firstByte & CharMask_ThreeByte) == CharMask_TwoByte)
		{
			result = BitTwiddler.parseThreeBytes(sigEnd, firstByte, this.getNextByte(sigEnd), this.getNextByte(sigEnd));
		}
		else
		{
			result = BitTwiddler.parseInt(sigEnd, firstByte, this.getNextByte(sigEnd), this.getNextByte(sigEnd), this.getNextByte(sigEnd));
			//throw new UnhandledEnumException(NumNodeConversion.toBinString(firstByte));
		}
		
		return result;
		*/
		
		/*
		final int result = CharConverter.getNextChar(this.DefaultCharConversion, this);
		//System.out.println(Integer.toHexString(result));
		return result;
		*/
		
		final char temp = this.getNextChar(sigEnd);
		
		/*
		if(temp > 127)
		{
			System.out.println("wonky char:<" + temp + '>');
		}
		*/
		
		return (temp & 0xffff);
	}

	@Deprecated
	public final int getNextExtendedChar2(EndianSettings sigEnd)
	{
		final int firstByte = this.getNextByte(sigEnd);
		final int result;

		switch (firstByte & CharMask_TwoByte)
		{
			case 0:
			{
				// one byte
				result = firstByte;
				break;
			}
			case 1:
			{
				// two byte
				result = BitTwiddler.parseShort(EndianSettings.LeftBitRightByte, firstByte, this.getNextByte(sigEnd));
				//result = ((firstByte << 8) | this.getNextByte(sigEnd));
				break;
			}
			default:
			{
				if((firstByte & CharMask_ThreeByte) == CharMask_TwoByte)
				{
					// three byte
					//result = (firstByte << 16) | (this.getNextByte(sigEnd) << 8) | (this.getNextByte(sigEnd));
					result = BitTwiddler.parseThreeBytes(sigEnd, firstByte, this.getNextByte(sigEnd), this.getNextByte(sigEnd));
				}
				else// if ((firstByte & CharMask_FourByte) == CharMask_ThreeByte)
				{
					// four byte
					// System.out.println("first: " + NumNodeConversion.toHexString(firstByte << 24));
					//final int Byte1 = this.getNextByte(sigEnd);
					// System.out.println("second: " + NumNodeConversion.toHexString(Byte1 << 16));
					//final int Byte2 = this.getNextByte(sigEnd);
					// System.out.println("third: " + NumNodeConversion.toHexString(Byte2 << 8));
					//final int Byte3 = this.getNextByte(sigEnd);
					// System.out.println("fourth: " + NumNodeConversion.toHexString(Byte3 << 0));
					//result = (firstByte << 24) | (Byte1 << 16) | (Byte2 << 8) | Byte3;
					// System.out.println(NumNodeConversion.toHexString(result));
					result = BitTwiddler.parseInt(sigEnd, firstByte, this.getNextByte(sigEnd), this.getNextByte(sigEnd), this.getNextByte(sigEnd));
				}
				break;
			}
		}

		return result;
		
		//return this.getNextByte(sigEnd);
	}
	
	public final int[] getNextExtendedCharArray(int length)
	{
		return this.getNextExtendedCharArray(this.inputSigEnd, length);
	}
	
	public final int[] getNextExtendedCharArray(EndianSettings sigEnd, int length)
	{
		final int[] result = new int[length];
		
		for(int i = 0; i < length; i++)
		{
			result[i] = this.getNextExtendedChar(sigEnd);
		}
		
		return result;
	}

	public final int getNextShort()
	{
		return this.getNextShort(this.inputSigEnd);
	}

	public final int getNextShort(EndianSettings sigEnd)
	{
		return BitTwiddler.parseShort(sigEnd, this.getNextByte_internal(), this.getNextByte_internal());
	}

	public final int[] getNextShortArray(int length)
	{
		return this.getNextShortArray(this.inputSigEnd, length);
	}

	public final int[] getNextShortArray(EndianSettings sigEnd, int length)
	{
		final int[] result = new int[length];

		for (int i = 0; i < length; i++)
		{
			result[i] = this.getNextShort(sigEnd);
		}

		return result;
	}

	public final int getNext3Byte()
	{
		return this.getNext3Byte(this.inputSigEnd);
	}

	public final int getNext3Byte(EndianSettings sigEnd)
	{
		return BitTwiddler.parseThreeBytes(sigEnd, this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal());
	}

	public final int[] getNext3ByteArray(int length)
	{
		return this.getNext3ByteArray(this.inputSigEnd, length);
	}

	public final int[] getNext3ByteArray(EndianSettings sigEnd, int length)
	{
		final int[] result = new int[length];

		for (int i = 0; i < length; i++)
		{
			result[i] = this.getNext3Byte(sigEnd);
		}

		return result;
	}

	public final int getNextInt()
	{
		return this.getNextInt(this.inputSigEnd);
	}

	public final int getNextInt(EndianSettings sigEnd)
	{
		return BitTwiddler.parseInt(sigEnd, this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal());
	}

	public final int[] getNextIntArray(int length)
	{
		return this.getNextIntArray(this.inputSigEnd, length);
	}

	public final int[] getNextIntArray(EndianSettings sigEnd, int length)
	{
		final int[] result = new int[length];

		for (int i = 0; i < length; i++)
		{
			result[i] = this.getNextInt(sigEnd);
		}

		return result;
	}
	
	public final long getNextUnsignedInt()
	{
		return this.getNextUnsignedInt(this.inputSigEnd);
	}
	
	public final long getNextUnsignedInt(EndianSettings sigEnd)
	{
		return BitTwiddler.parseUnsignedInt(sigEnd, this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal());
	}

	public final float getNextFloat()
	{
		return this.getNextFloat(this.inputSigEnd);
	}

	public final float getNextFloat(EndianSettings sigEnd)
	{
		return BitTwiddler.parseFloat(sigEnd, this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal());
	}

	public final float[] getNextFloatArray(int length)
	{
		return this.getNextFloatArray(this.inputSigEnd, length);
	}

	public final float[] getNextFloatArray(EndianSettings sigEnd, int length)
	{
		final float[] result = new float[length];

		for (int i = 0; i < length; i++)
		{
			result[i] = this.getNextFloat(sigEnd);
		}

		return result;
	}

	public final long getNextLong()
	{
		return this.getNextLong(this.inputSigEnd);
	}

	public final long getNextLong(EndianSettings sigEnd)
	{
		return BitTwiddler.parseLong(sigEnd, this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal());
	}

	public final long[] getNextLongArray(int length)
	{
		return this.getNextLongArray(this.inputSigEnd, length);
	}

	public final long[] getNextLongArray(EndianSettings sigEnd, int length)
	{
		final long[] result = new long[length];

		for (int i = 0; i < length; i++)
		{
			result[i] = this.getNextLong(sigEnd);
		}

		return result;
	}

	public final double getNextDouble()
	{
		return this.getNextDouble(this.inputSigEnd);
	}

	public final double getNextDouble(EndianSettings sigEnd)
	{
		return BitTwiddler.parseDouble(sigEnd, this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal(), this.getNextByte_internal());
	}

	public final double[] getNextDoubleArray(int length)
	{
		return this.getNextDoubleArray(this.inputSigEnd, length);
	}

	public final double[] getNextDoubleArray(EndianSettings sigEnd, int length)
	{
		final double[] result = new double[length];

		for (int i = 0; i < result.length; i++)
		{
			result[i] = this.getNextDouble(sigEnd);
		}

		return result;
	}

	public final CharList getNextString_NullDelimited()
	{
		return this.getNextString_NullDelimited(this.inputSigEnd);
	}

	public final CharList getNextString_NullDelimited(EndianSettings sigEnd)
	{
		final CharList result = new CharList();

		char current = this.getNextChar(sigEnd);

		while (current != 0)
		{
			result.add(current);
			current = this.getNextChar(sigEnd);

			if (result.size() > stringLengthSafetyCutoff)
			{
				throw new IndexOutOfBoundsException("String safety cutoff was exceeded!");
			}
		}

		return result;
	}

	public final CharList[] getNextStringArray_NullDelimited(int length)
	{
		return this.getNextStringArray_NullDelimited(this.inputSigEnd, length);
	}

	public CharList[] getNextStringArray_NullDelimited(EndianSettings sigEnd, int length)
	{
		final CharList[] result = new CharList[length];

		for (int i = 0; i < result.length; i++)
		{
			result[i] = this.getNextString_NullDelimited(sigEnd);
		}

		return result;
	}

	public final char[] getNextString_prependedLength_raw(int numBytesInLengthVar)
	{
		return this.getNextString_prependedLength_raw(numBytesInLengthVar, this.inputSigEnd);
	}

	public final char[] getNextString_prependedLength_raw(int numBytesInLengthVar, EndianSettings lengthVarSigEnd)
	{
		return this.getNextString_prependedLength_raw(this.inputSigEnd, numBytesInLengthVar, lengthVarSigEnd);
	}

	public final char[] getNextString_prependedLength_raw(EndianSettings sigEnd, int numBytesInLengthVar, EndianSettings lengthVarSigEnd)
	{
		final int length;

		switch (numBytesInLengthVar)
		{
			case 1:
			{
				length = this.getNextByte(lengthVarSigEnd);
				break;
			}
			case 2:
			{
				length = this.getNextShort(lengthVarSigEnd);
				break;
			}
			case 3:
			{
				length = this.getNext3Byte(lengthVarSigEnd);
				break;
			}
			case 4:
			{
				length = this.getNextInt(lengthVarSigEnd);
				break;
			}
			case 8:
			{
				length = (int) this.getNextLong(lengthVarSigEnd);
				break;
			}
			default:
			{
				throw new IllegalArgumentException("waaay too many bytes there, bud. (" + numBytesInLengthVar + " bytes)");
			}
		}

		if (length > stringLengthSafetyCutoff)
		{
			throw new IndexOutOfBoundsException("String safety cutoff was exceeded!");
		}

		return this.getNextCharArray(sigEnd, length);
	}

	public final CharList getNextString_prependedLength(int numBytesInLengthVar)
	{
		return this.getNextString_prependedLength(numBytesInLengthVar, this.inputSigEnd);
	}

	public final CharList getNextString_prependedLength(int numBytesInLengthVar, EndianSettings lengthVarSigEnd)
	{
		return this.getNextString_prependedLength(this.inputSigEnd, numBytesInLengthVar, lengthVarSigEnd);
	}

	public final CharList getNextString_prependedLength(EndianSettings sigEnd, int numBytesInLengthVar, EndianSettings lengthVarSigEnd)
	{
		return new CharList(this.getNextString_prependedLength_raw(sigEnd, numBytesInLengthVar, lengthVarSigEnd));
	}

	public final char[][] getNextStringArray_prependedLength_raw(int arrayLength, int numBytesInLengthVar)
	{
		return this.getNextStringArray_prependedLength_raw(arrayLength, numBytesInLengthVar, this.inputSigEnd);
	}

	public final char[][] getNextStringArray_prependedLength_raw(int arrayLength, int numBytesInLengthVar, EndianSettings lengthVarSigEnd)
	{
		return this.getNextStringArray_prependedLength_raw(arrayLength, this.inputSigEnd, numBytesInLengthVar, lengthVarSigEnd);
	}

	public final char[][] getNextStringArray_prependedLength_raw(int arrayLength, EndianSettings sigEnd, int numBytesInLengthVar, EndianSettings lengthVarSigEnd)
	{
		final char[][] result = new char[arrayLength][];

		for (int i = 0; i < arrayLength; i++)
		{
			result[i] = this.getNextString_prependedLength_raw(sigEnd, numBytesInLengthVar, lengthVarSigEnd);
		}

		return result;
	}

	public final CharList[] getNextStringArray_prependedLength(int arrayLength, int numBytesInLengthVar)
	{
		return this.getNextStringArray_prependedLength(arrayLength, numBytesInLengthVar, this.inputSigEnd);
	}

	public final CharList[] getNextStringArray_prependedLength(int arrayLength, int numBytesInLengthVar, EndianSettings lengthVarSigEnd)
	{
		return this.getNextStringArray_prependedLength(arrayLength, this.inputSigEnd, numBytesInLengthVar, lengthVarSigEnd);
	}

	public final CharList[] getNextStringArray_prependedLength(int arrayLength, EndianSettings sigEnd, int numBytesInLengthVar, EndianSettings lengthVarSigEnd)
	{
		final CharList[] result = new CharList[arrayLength];

		for (int i = 0; i < result.length; i++)
		{
			result[i] = this.getNextString_prependedLength(sigEnd, numBytesInLengthVar, lengthVarSigEnd);
		}

		return result;
	}
}
