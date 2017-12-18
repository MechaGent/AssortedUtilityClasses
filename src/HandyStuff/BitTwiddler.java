package HandyStuff;

import CustomExceptions.UnhandledEnumException;
import HandyStuff.HandyEnums.EndianSettings;

public class BitTwiddler
{
	public static final int normalizeByte(int Byte, EndianSettings sigEnd)
	{
		switch (sigEnd)
		{
			case RightBitRightByte:
			case RightBitLeftByte:
			{
				Byte = flipBits_Byte(Byte) & 0xff;
				break;
			}
			case LeftBitLeftByte:
			case LeftBitRightByte:
			{
				Byte &= 0xff;
				break;
			}
			default:
			{
				throw new UnhandledEnumException(sigEnd);
			}
		}

		return Byte;
	}
	
	public static final int normalizeShort(int Short, EndianSettings sigEnd)
	{
		switch (sigEnd)
		{
			case LeftBitLeftByte:
			{
				return Short;
			}
			case LeftBitRightByte:
			{
				return ((Short & 0xff) << 8) | (Short >>> 8);
			}
			case RightBitLeftByte:
			{
				return flipBits_Byte(Short >>> 8) | flipBits_Byte((Short & 0xff) << 8);
			}
			case RightBitRightByte:
			{
				return flipBits_Byte((Short & 0xff) << 8) | flipBits_Byte(Short >>> 8);
			}
			default:
			{
				throw new UnhandledEnumException(sigEnd);
			}
		}
	}
	
	public static int parseShort(EndianSettings sigEnd, int Byte0, int Byte1)
	{
		final int r0; // leftmost
		final int r1;

		switch (sigEnd)
		{
			case LeftBitLeftByte: // this should be default
			{
				r0 = Byte0;
				r1 = Byte1;
				break;
			}
			case LeftBitRightByte:
			{
				r0 = Byte1;
				r1 = Byte0;
				break;
			}
			case RightBitLeftByte:
			{
				r0 = flipBits_Byte(Byte0);
				r1 = flipBits_Byte(Byte1);
				break;
			}
			case RightBitRightByte:
			{
				r0 = flipBits_Byte(Byte1);
				r1 = flipBits_Byte(Byte0);
				break;
			}
			default:
			{
				throw new IllegalArgumentException();
			}
		}

		final int result = (((r1 & 0xFF) << 8) | (r0 & 0xFF)) & 0xffff;

		return result;
	}
	
	public static final int normalizeThreeBytes(int Bytes, EndianSettings sigEnd)
	{
		switch (sigEnd)
		{
			case LeftBitLeftByte:
			{
				return Bytes;
			}
			case LeftBitRightByte:
			{
				return ((Bytes & 0xff) << 16) | (Bytes & 0xff00) | (Bytes >>> 16);
			}
			case RightBitLeftByte:
			{
				final int n0 = flipBits_Nibble(Bytes & 0xf);
				final int n1 = flipBits_Nibble(Bytes & 0xf0);
				final int n2 = flipBits_Nibble(Bytes & 0xf00);
				final int n3 = flipBits_Nibble(Bytes & 0xf000);
				final int n4 = flipBits_Nibble(Bytes & 0xf0000);
				final int n5 = flipBits_Nibble(Bytes & 0xf00000);
				
				return (n4 << 4) | (n5 >>> 4) | (n2 << 4) | (n3 >>> 4) | (n0 << 4) | (n1 >>> 4);
			}
			case RightBitRightByte:
			{
				final int n0 = flipBits_Nibble(Bytes & 0xf);
				final int n1 = flipBits_Nibble(Bytes & 0xf0);
				final int n2 = flipBits_Nibble(Bytes & 0xf00);
				final int n3 = flipBits_Nibble(Bytes & 0xf000);
				final int n4 = flipBits_Nibble(Bytes & 0xf0000);
				final int n5 = flipBits_Nibble(Bytes & 0xf00000);
				
				return (n0 << (16 + 4)) | (n1 << (16 - 4)) | (n2 << 4) | (n3 >>> 4) | (n4 >>> (16 - 4)) | (n5 >>> (16 + 5));
			}
			default:
			{
				throw new UnhandledEnumException(sigEnd);
			}
		}
	}
	
	public static int parseThreeBytes(EndianSettings sigEnd, int inI1, int inI2, int inI3)
	{
		final int r0; // leftmost
		final int r1;
		final int r2;
		
		switch (sigEnd)
		{
			case LeftBitLeftByte: // this should be default
			{
				r0 = inI1;
				r1 = inI2;
				r2 = inI3;
				break;
			}
			case LeftBitRightByte:
			{
				//r0 = inI4;
				r0 = inI3;
				r1 = inI2;
				r2 = inI1;
				break;
			}
			case RightBitLeftByte:
			{
				r0 = flipBits_Byte(inI1);
				r1 = flipBits_Byte(inI2);
				r2 = flipBits_Byte(inI3);
				break;
			}
			case RightBitRightByte:
			{
				//r0 = flipBits(inI4);
				r0 = flipBits_Byte(inI3);
				r1 = flipBits_Byte(inI2);
				r2 = flipBits_Byte(inI1);
				break;
			}
			default:
			{
				throw new IllegalArgumentException();
			}
		}

		int result = ((r0 & 0xFF) << 16) | ((r1 & 0xFF) << 8) | (r2 & 0xFF);
		
		return result;
	}
	
	public static final int normalizeInt(int Int, EndianSettings sigEnd)
	{
		switch (sigEnd)
		{
			case LeftBitLeftByte:
			{
				return Int;
			}
			case LeftBitRightByte:
			{
				return (Int << 24) | ((Int & 0xff00) << 8) | ((Int & 0xff0000) >>> 8) | (Int >>> 24);
			}
			case RightBitLeftByte:
			{
				final int n0 = flipBits_Nibble(Int & 0xf);
				final int n1 = flipBits_Nibble(Int & 0xf0);
				final int n2 = flipBits_Nibble(Int & 0xf00);
				final int n3 = flipBits_Nibble(Int & 0xf000);
				final int n4 = flipBits_Nibble(Int & 0xf0000);
				final int n5 = flipBits_Nibble(Int & 0xf00000);
				final int n6 = flipBits_Nibble(Int & 0xf000000);
				final int n7 = flipBits_Nibble(Int & 0xf0000000);
				
				return (n6 << 4) | (n7 >>> 4) | (n4 << 4) | (n5 >>> 4) | (n2 << 4) | (n3 >>> 4) | (n0 << 4) | (n1 >>> 4);
			}
			case RightBitRightByte:
			{
				/*
				 * b76 b54 b32 b10	:	vanilla
				 * b67 b45 b23 b01	:	RightBit, if all nibbles are flipped
				 * b01 b23 b45 b67	:	RightByte, if all nibbles are flipped
				 */
				
				final int n0 = flipBits_Nibble(Int & 0xf);
				final int n1 = flipBits_Nibble(Int & 0xf0);
				final int n2 = flipBits_Nibble(Int & 0xf00);
				final int n3 = flipBits_Nibble(Int & 0xf000);
				final int n4 = flipBits_Nibble(Int & 0xf0000);
				final int n5 = flipBits_Nibble(Int & 0xf00000);
				final int n6 = flipBits_Nibble(Int & 0xf000000);
				final int n7 = flipBits_Nibble(Int & 0xf0000000);
				
				return 	(n0 << (24+4)) | 	(n1 << (24-4)) | 	(n2 << (8+4)) | 	(n3 << (8-4)) |
						(n4 >>> (8-4)) | 	(n5 >>> (8+4)) | 	(n6 >>> (24-4)) | 	(n7 >>> (24+4));
			}
			default:
			{
				throw new UnhandledEnumException(sigEnd);
			}
		}
	}
	
	/**
	 * Bytes should be passed in the order they were read
	 * 
	 * @param inI
	 * @param inI2
	 * @param inI3
	 * @param inI4
	 * @param sigEnd
	 * @return
	 */
	public static int parseInt(EndianSettings sigEnd, int inI, int inI2, int inI3, int inI4)
	{
		final int r0; // leftmost
		final int r1;
		final int r2;
		final int r3;

		switch (sigEnd)
		{
			case LeftBitLeftByte: // this should be default
			{
				r0 = inI;
				r1 = inI2;
				r2 = inI3;
				r3 = inI4;
				break;
			}
			case LeftBitRightByte:
			{
				r0 = inI4;
				r1 = inI3;
				r2 = inI2;
				r3 = inI;
				break;
			}
			case RightBitLeftByte:
			{
				r0 = flipBits_Byte(inI);
				r1 = flipBits_Byte(inI2);
				r2 = flipBits_Byte(inI3);
				r3 = flipBits_Byte(inI4);
				break;
			}
			case RightBitRightByte:
			{
				r0 = flipBits_Byte(inI4);
				r1 = flipBits_Byte(inI3);
				r2 = flipBits_Byte(inI2);
				r3 = flipBits_Byte(inI);
				break;
			}
			default:
			{
				throw new IllegalArgumentException();
			}
		}

		final int result = ((r0 & 0xFF) << 24) | ((r1 & 0xFF) << 16) | ((r2 & 0xFF) << 8) | (r3 & 0xFF);
		
		//System.out.println("Bytes: " + Integer.toHexString(b0) + ", " + Integer.toHexString(b1) + ", " + Integer.toHexString(b2) + ", " + Integer.toHexString(b3) + ", Result: " + Integer.toHexString(result));

		return result;
	}
	
	public static final long parseUnsignedInt(EndianSettings sigEnd, int inI, int inI2, int inI3, int inI4)
	{
		final long r0; // leftmost
		final long r1;
		final long r2;
		final long r3;

		switch (sigEnd)
		{
			case LeftBitLeftByte: // this should be default
			{
				r0 = inI;
				r1 = inI2;
				r2 = inI3;
				r3 = inI4;
				break;
			}
			case LeftBitRightByte:
			{
				r0 = inI4;
				r1 = inI3;
				r2 = inI2;
				r3 = inI;
				break;
			}
			case RightBitLeftByte:
			{
				r0 = flipBits_Byte(inI);
				r1 = flipBits_Byte(inI2);
				r2 = flipBits_Byte(inI3);
				r3 = flipBits_Byte(inI4);
				break;
			}
			case RightBitRightByte:
			{
				r0 = flipBits_Byte(inI4);
				r1 = flipBits_Byte(inI3);
				r2 = flipBits_Byte(inI2);
				r3 = flipBits_Byte(inI);
				break;
			}
			default:
			{
				throw new IllegalArgumentException();
			}
		}
		
		final long result = (((r0 & 0xFF) << 24) | ((r1 & 0xFF) << 16) | ((r2 & 0xFF) << 8) | (r3 & 0xFF)) & 0xffffffff;
		
		if(result < 0)
		{
			throw new IllegalArgumentException("bad parse, this should not be negative! " + result);
		}
		else if(result > 0xffffffffL)
		{
			throw new IllegalArgumentException("bad parse, this is too big! " + result);
		}
		
		return result;
	}
	
	public static final float parseFloat(EndianSettings sigEnd, int inI, int inI2, int inI3, int inI4)
	{
		return Float.intBitsToFloat(parseInt(sigEnd, inI, inI2, inI3, inI4));
	}
	
	public static long parseLong(EndianSettings sigEnd, int inI, int inI2, int inI3, int inI4, int inI5, int inI6, int inI7, int inI8)
	{
		final long r0; // leftmost
		final long r1;
		final long r2;
		final long r3;
		final long r4;
		final long r5;
		final long r6;
		final long r7;

		switch (sigEnd)
		{
			case LeftBitLeftByte: // this should be default
			{
				r0 = inI;
				r1 = inI2;
				r2 = inI3;
				r3 = inI4;
				r4 = inI5;
				r5 = inI6;
				r6 = inI7;
				r7 = inI8;
				break;
			}
			case LeftBitRightByte:
			{
				r0 = inI8;
				r1 = inI7;
				r2 = inI6;
				r3 = inI5;
				r4 = inI4;
				r5 = inI3;
				r6 = inI2;
				r7 = inI;
				break;
			}
			case RightBitLeftByte:
			{
				r0 = flipBits_Byte(inI);
				r1 = flipBits_Byte(inI2);
				r2 = flipBits_Byte(inI3);
				r3 = flipBits_Byte(inI4);
				r4 = flipBits_Byte(inI5);
				r5 = flipBits_Byte(inI6);
				r6 = flipBits_Byte(inI7);
				r7 = flipBits_Byte(inI8);
				break;
			}
			case RightBitRightByte:
			{
				r0 = flipBits_Byte(inI8);
				r1 = flipBits_Byte(inI7);
				r2 = flipBits_Byte(inI6);
				r3 = flipBits_Byte(inI5);
				r4 = flipBits_Byte(inI4);
				r5 = flipBits_Byte(inI3);
				r6 = flipBits_Byte(inI2);
				r7 = flipBits_Byte(inI);
				break;
			}
			default:
			{
				throw new IllegalArgumentException();
			}
		}

		long result = ((r0 & 0xFF) << 56) | ((r1 & 0xFF) << 48) | ((r2 & 0xFF) << 40) | ((r3 & 0xFF) << 32) | ((r4 & 0xFF) << 24) | ((r5 & 0xFF) << 16) | ((r6 & 0xFF) << 8) | (r7 & 0xFF);

		return result;
	}
	
	public static final double parseDouble(EndianSettings sigEnd, int inI, int inI2, int inI3, int inI4, int inI5, int inI6, int inI7, int inI8)
	{
		return Double.longBitsToDouble(parseLong(sigEnd, inI, inI2, inI3, inI4, inI5, inI6, inI7, inI8));
	}
	
	public static final int setNibble(int arrayOfNibbles, int index, byte cargo)
	{
		return setNibble(arrayOfNibbles, index, (int) cargo);
	}
	
	public static final int setNibble(int arrayOfNibbles, int index, int cargo)
	{
		final int result;
		// System.out.println(cargo + "");

		switch (index)
		{
			case 0:
			{
				result = (cargo & 0x0f) | (arrayOfNibbles & 0xFFFFFFF0); // 0xFFFFFFFFFFFFFFFF
				break;
			}
			case 1:
			{
				result = ((cargo & 0x0f) << 4) | (arrayOfNibbles & 0xFFFFFF0F);
				break;
			}
			case 2:
			{
				result = ((cargo & 0x0f) << 8) | (arrayOfNibbles & 0xFFFFF0FF);
				break;
			}
			case 3:
			{
				result = ((cargo & 0x0f) << 12) | (arrayOfNibbles & 0xFFFF0FFF);
				break;
			}
			case 4:
			{
				result = ((cargo & 0x0f) << 16) | (arrayOfNibbles & 0xFFF0FFFF);
				break;
			}
			case 5:
			{
				result = ((cargo & 0x0f) << 20) | (arrayOfNibbles & 0xFF0FFFFF);
				break;
			}
			case 6:
			{
				result = ((cargo & 0x0f) << 24) | (arrayOfNibbles & 0xF0FFFFFF);
				break;
			}
			case 7:
			{
				result = ((cargo & 0x0f) << 28) | (arrayOfNibbles & 0x0FFFFFFF);
				break;
			}
			default:
			{
				throw new IndexOutOfBoundsException("bad subSlot: " + index);
			}
		}

		// System.out.println("cargo: " + cargo + " in subslot: " + subSlot + " with result: " + toHext(result).toString());
		return result;
	}

	public static final long setNibble(long arrayOfNibbles, int index, byte cargo)
	{
		return setNibble(arrayOfNibbles, index, (int) cargo);
	}
	
	public static final long setNibble(long arrayOfNibbles, int index, int cargo)
	{
		final long result;

		switch (index)
		{
			case 0:
			{
				result = ((cargo & 0x0FL) << 0) | (arrayOfNibbles & 0xFFFFFFFFFFFFFFF0L);
				break;
			}
			case 1:
			{
				result = ((cargo & 0x0FL) << 4) | (arrayOfNibbles & 0xFFFFFFFFFFFFFF0FL);
				break;
			}
			case 2:
			{
				result = ((cargo & 0x0FL) << 8) | (arrayOfNibbles & 0xFFFFFFFFFFFFF0FFL);
				break;
			}
			case 3:
			{
				result = ((cargo & 0x0FL) << 12) | (arrayOfNibbles & 0xFFFFFFFFFFFF0FFFL);
				break;
			}
			case 4:
			{
				result = ((cargo & 0x0FL) << 16) | (arrayOfNibbles & 0xFFFFFFFFFFF0FFFFL);
				break;
			}
			case 5:
			{
				result = ((cargo & 0x0FL) << 20) | (arrayOfNibbles & 0xFFFFFFFFFF0FFFFFL);
				break;
			}
			case 6:
			{
				result = ((cargo & 0x0FL) << 24) | (arrayOfNibbles & 0xFFFFFFFFF0FFFFFFL);
				break;
			}
			case 7:
			{
				result = ((cargo & 0x0FL) << 28) | (arrayOfNibbles & 0xFFFFFFFF0FFFFFFFL);
				break;
			}
			case 8:
			{
				result = ((cargo & 0x0FL) << 32) | (arrayOfNibbles & 0xFFFFFFF0FFFFFFFFL);
				break;
			}
			case 9:
			{
				result = ((cargo & 0x0FL) << 36) | (arrayOfNibbles & 0xFFFFFF0FFFFFFFFFL);
				break;
			}
			case 10:
			{
				result = ((cargo & 0x0FL) << 40) | (arrayOfNibbles & 0xFFFFF0FFFFFFFFFFL);
				break;
			}
			case 11:
			{
				result = ((cargo & 0x0FL) << 44) | (arrayOfNibbles & 0xFFFF0FFFFFFFFFFFL);
				break;
			}
			case 12:
			{
				result = ((cargo & 0x0FL) << 48) | (arrayOfNibbles & 0xFFF0FFFFFFFFFFFFL);
				break;
			}
			case 13:
			{
				result = ((cargo & 0x0FL) << 52) | (arrayOfNibbles & 0xFF0FFFFFFFFFFFFFL);
				break;
			}
			case 14:
			{
				result = ((cargo & 0x0FL) << 56) | (arrayOfNibbles & 0xF0FFFFFFFFFFFFFFL);
				break;
			}
			case 15:
			{
				result = ((cargo & 0x0FL) << 60) | (arrayOfNibbles & 0x0FFFFFFFFFFFFFFFL);
				break;
			}
			default:
			{
				throw new IndexOutOfBoundsException("bad subSlot: " + index);
			}
		}

		return result;
	}
	
	public static final byte getNibble(byte arrayOfNibbles, int index)
	{
		return getNibble(arrayOfNibbles, index, true);
	}
	
	public static final byte getNibble(byte arrayOfNibbles, int index, boolean ascendsFromRightToLeft)
	{
		final byte result;
		
		switch(index)
		{
			case 0:
			{
				if(ascendsFromRightToLeft)
				{
					result = (byte) (arrayOfNibbles & 0x0F);
				}
				else
				{
					result = (byte) ((arrayOfNibbles >>> 4) & 0x0F);
				}
				
				break;
			}
			case 1:
			{
				if(ascendsFromRightToLeft)
				{
					result = (byte) ((arrayOfNibbles >>> 4) & 0x0F);
				}
				else
				{
					result = (byte) (arrayOfNibbles & 0x0F);
				}
				
				break;
			}
			default:
			{
				throw new IndexOutOfBoundsException(index + " is greater than or equal to 2");
			}
		}
		
		return result;
	}
	
	public static final byte getNibbleAsByte(int arrayOfNibbles, int index)
	{
		return getNibbleAsByte(arrayOfNibbles, index, true);
	}
	
	public static final int getNibble(int arrayOfNibbles, int index)
	{
		return getNibble(arrayOfNibbles, index, true);
	}
	
	public static final byte getNibbleAsByte(int arrayOfNibbles, int index, boolean ascendsFromRightToLeft)
	{
		return (byte) getNibble(arrayOfNibbles, index, ascendsFromRightToLeft);
	}
	
	public static final int getNibble(int arrayOfNibbles, int index, boolean ascendsFromRightToLeft)
	{
		final int result;

		if(!ascendsFromRightToLeft)
		{
			index = 7 - index;
		}
		
		switch (index)
		{
			case 0:
			{
				result = arrayOfNibbles & 0x0f;
				break;
			}
			case 1:
			{
				result = (arrayOfNibbles >>> 4) & 0x0f;
				break;
			}
			case 2:
			{
				result = (arrayOfNibbles >>> 8) & 0x0f;
				break;
			}
			case 3:
			{
				result = (arrayOfNibbles >>> 12) & 0x0f;
				break;
			}
			case 4:
			{
				result = (arrayOfNibbles >>> 16) & 0x0f;
				break;
			}
			case 5:
			{
				result = (arrayOfNibbles >>> 20) & 0x0f;
				break;
			}
			case 6:
			{
				result = (arrayOfNibbles >>> 24) & 0x0f;
				break;
			}
			case 7:
			{
				result = (arrayOfNibbles >>> 28) & 0x0f;
				break;
			}
			default:
			{
				throw new IndexOutOfBoundsException("bad index: " + index);
			}
		}

		return result;
	}
	
	public static final byte getNibble(long arrayOfNibbles, int index)
	{
		return getNibble(arrayOfNibbles, index, true);
	}

	public static final byte getNibble(long arrayOfNibbles, int index, boolean ascendsFromRightToLeft)
	{
		final byte result;
		
		if(!ascendsFromRightToLeft)
		{
			index = 7 - index;
		}

		switch (index)
		{
			case 0:
			{
				result = (byte) (arrayOfNibbles & 0x0fL);
				break;
			}
			case 1:
			{
				result = (byte) ((arrayOfNibbles >>> 4) & 0x0fL);
				break;
			}
			case 2:
			{
				result = (byte) ((arrayOfNibbles >>> 8) & 0x0fL);
				break;
			}
			case 3:
			{
				result = (byte) ((arrayOfNibbles >>> 12) & 0x0fL);
				break;
			}
			case 4:
			{
				result = (byte) ((arrayOfNibbles >>> 16) & 0x0fL);
				break;
			}
			case 5:
			{
				result = (byte) ((arrayOfNibbles >>> 20) & 0x0fL);
				break;
			}
			case 6:
			{
				result = (byte) ((arrayOfNibbles >>> 24) & 0x0fL);
				break;
			}
			case 7:
			{
				result = (byte) ((arrayOfNibbles >>> 28) & 0x0fL);
				break;
			}
			case 8:
			{
				result = (byte) ((arrayOfNibbles >>> 32) & 0x0fL);
				break;
			}
			case 9:
			{
				result = (byte) ((arrayOfNibbles >>> 36) & 0x0fL);
				break;
			}
			case 10:
			{
				result = (byte) ((arrayOfNibbles >>> 40) & 0x0fL);
				break;
			}
			case 11:
			{
				result = (byte) ((arrayOfNibbles >>> 44) & 0x0fL);
				break;
			}
			case 12:
			{
				result = (byte) ((arrayOfNibbles >>> 48) & 0x0fL);
				break;
			}
			case 13:
			{
				result = (byte) ((arrayOfNibbles >>> 52) & 0x0fL);
				break;
			}
			case 14:
			{
				result = (byte) ((arrayOfNibbles >>> 56) & 0x0fL);
				break;
			}
			case 15:
			{
				result = (byte) ((arrayOfNibbles >>> 60) & 0x0fL);
				break;
			}
			default:
			{
				throw new IndexOutOfBoundsException("bad index: " + index);
			}
		}

		return result;
	}
	
	/**
	 * currently, cannot change Endianness
	 * @param in
	 * @return
	 */
	public static byte[] splitIntIntoBytes(int in)
	{
		final byte[] result = new byte[4];
		
		result[0] = (byte) in;
		in >>>= 8;
		result[1] = (byte) in;
		in >>>= 8;
		result[2] = (byte) in;
		in >>>= 8;
		result[3] = (byte) in;
		
		return result;
	}
	
	public static final byte[] splitLongIntoBytes(long in)
	{
		final byte[] result = new byte[8];
		
		result[0] = (byte) in;
		in >>>= 8;
		result[1] = (byte) in;
		in >>>= 8;
		result[2] = (byte) in;
		in >>>= 8;
		result[3] = (byte) in;
		in >>>= 8;
		result[4] = (byte) in;
		in >>>= 8;
		result[5] = (byte) in;
		in >>>= 8;
		result[6] = (byte) in;
		in >>>= 8;
		result[7] = (byte) in;
		
		return result;
	}
	
	public static byte[] splitCharIntoNibbles(char in)
	{
		return splitCharIntoNibbles(in, true);
	}

	public static byte[] splitCharIntoNibbles(char in, boolean ascendsLeftToRight)
	{
		return splitByteIntoNibbles((byte) in, ascendsLeftToRight);
	}

	public static byte[] splitByteIntoNibbles(byte in)
	{
		return splitByteIntoNibbles(in, true);
	}

	public static byte[] splitByteIntoNibbles(byte in, boolean ascendsLeftToRight)
	{
		final byte var1;
		final byte var2;

		if (ascendsLeftToRight)
		{
			var1 = (byte) ((in >>> 4) & 0x0F);
			var2 = (byte) (in & 0x0F);
		}
		else
		{
			var1 = (byte) (in & 0x0F);
			var2 = (byte) ((in >>> 4) & 0x0F);
		}

		// System.out.println("char: " + in + " turned into: " + MiscToStrings.toHexString(var1) + " " + MiscToStrings.toHexString(var2));

		return new byte[] {
							var1,
							var2 };
	}
	
	/**
	 * should be masked, but which byte-index it's in doesn't matter
	 * @param Byte
	 * @return
	 */
	public static final int flipBits_Byte(int Byte)
	{
		// final int Nibble0 = (Byte >>> 4);
		// final int Nibble1 = Byte & 0xf;
		final int FlippedNibble0 = flipBits_Nibble(Byte >>> 4);
		final int FlippedNibble1 = flipBits_Nibble(Byte & 0xf);

		// System.out.println(getPaddedBinary(Byte, 2) + " breaks into:\r\n\t" + getPaddedBinary(Nibble0, 1) + "\r\n\t" + getPaddedBinary(Nibble1, 1));

		return (FlippedNibble1 << 4) | FlippedNibble0;
	}

	/**
	 * flips all bits, does not respect byte order
	 * 
	 * @param Short
	 * @return
	 */
	public static final int flipBits_Short(int Short)
	{
		final int FlippedNibble0 = flipBits_Nibble(Short & 0xf);
		final int FlippedNibble1 = flipBits_Nibble((Short >>> 4) & 0xf);
		final int FlippedNibble2 = flipBits_Nibble((Short >>> 8) & 0xf);
		final int FlippedNibble3 = flipBits_Nibble((Short >>> 12) & 0xf);

		return (FlippedNibble0 << 12) | (FlippedNibble1 << 8) | (FlippedNibble2 << 4) | (FlippedNibble3);
	}
	
	public static final int flipBits_Nibble(int Nibble)
	{
		final int result;
		
		switch(Nibble)
		{
			case 0b0000:
			case 0b0110:
			case 0b1001:
			case 0b1111:
			//case 0b00000000:
			case 0b01100000:
			case 0b10010000:
			case 0b11110000:
			//case 0b000000000000:
			case 0b011000000000:
			case 0b100100000000:
			case 0b111100000000:
			//case 0b0000000000000000:
			case 0b0110000000000000:
			case 0b1001000000000000:
			case 0b1111000000000000:
			//case 0b00000000000000000000:
			case 0b01100000000000000000:
			case 0b10010000000000000000:
			case 0b11110000000000000000:
			//case 0b000000000000000000000000:
			case 0b011000000000000000000000:
			case 0b100100000000000000000000:
			case 0b111100000000000000000000:
			//case 0b0000000000000000000000000000:
			case 0b0110000000000000000000000000:
			case 0b1001000000000000000000000000:
			case 0b1111000000000000000000000000:
			//case 0b00000000000000000000000000000000:
			case 0b01100000000000000000000000000000:
			case 0b10010000000000000000000000000000:
			case 0b11110000000000000000000000000000:
			//case 0b0000:
			{
				result = Nibble;
				break;
			}
			case 0b0001:
			{
				result = 0b1000;
				break;
			}
			case 0b0010:
			{
				result = 0b0100;
				break;
			}
			case 0b0011:
			{
				result = 0b1100;
				break;
			}
			case 0b0100:
			{
				result = 0b0010;
				break;
			}
			case 0b0101:
			{
				result = 0b1010;
				break;
			}
			// case 0b0110:
			case 0b0111:
			{
				result = 0b1110;
				break;
			}
			case 0b1000:
			{
				result = 0b0001;
				break;
			}
			// case 0b1001:
			case 0b1010:
			{
				result = 0b0101;
				break;
			}
			case 0b1011:
			{
				result = 0b1101;
				break;
			}
			case 0b1100:
			{
				result = 0b0011;
				break;
			}
			case 0b1101:
			{
				result = 0b1011;
				break;
			}
			case 0b1110:
			{
				result = 0b0111;
				break;
			}
			// case 0b1111:
			case 0b00010000:
			{
				result = 0b10000000;
				break;
			}
			case 0b00100000:
			{
				result = 0b01000000;
				break;
			}
			case 0b00110000:
			{
				result = 0b11000000;
				break;
			}
			case 0b01000000:
			{
				result = 0b00100000;
				break;
			}
			case 0b01010000:
			{
				result = 0b10100000;
				break;
			}
			// case 0b0110:
			case 0b01110000:
			{
				result = 0b11100000;
				break;
			}
			case 0b10000000:
			{
				result = 0b00010000;
				break;
			}
			// case 0b1001:
			case 0b10100000:
			{
				result = 0b01010000;
				break;
			}
			case 0b10110000:
			{
				result = 0b11010000;
				break;
			}
			case 0b11000000:
			{
				result = 0b00110000;
				break;
			}
			case 0b11010000:
			{
				result = 0b10110000;
				break;
			}
			case 0b11100000:
			{
				result = 0b01110000;
				break;
			}
			// case 0b1111:
			case 0b000100000000:
			{
				result = 0b100000000000;
				break;
			}
			case 0b001000000000:
			{
				result = 0b010000000000;
				break;
			}
			case 0b001100000000:
			{
				result = 0b110000000000;
				break;
			}
			case 0b010000000000:
			{
				result = 0b001000000000;
				break;
			}
			case 0b010100000000:
			{
				result = 0b101000000000;
				break;
			}
			// case 0b0110:
			case 0b011100000000:
			{
				result = 0b111000000000;
				break;
			}
			case 0b100000000000:
			{
				result = 0b000100000000;
				break;
			}
			// case 0b1001:
			case 0b101000000000:
			{
				result = 0b010100000000;
				break;
			}
			case 0b101100000000:
			{
				result = 0b110100000000;
				break;
			}
			case 0b110000000000:
			{
				result = 0b001100000000;
				break;
			}
			case 0b110100000000:
			{
				result = 0b101100000000;
				break;
			}
			case 0b111000000000:
			{
				result = 0b011100000000;
				break;
			}
			// case 0b1111:
			case 0b0001000000000000:
			{
				result = 0b1000000000000000;
				break;
			}
			case 0b0010000000000000:
			{
				result = 0b0100000000000000;
				break;
			}
			case 0b0011000000000000:
			{
				result = 0b1100000000000000;
				break;
			}
			case 0b0100000000000000:
			{
				result = 0b0010000000000000;
				break;
			}
			case 0b0101000000000000:
			{
				result = 0b1010000000000000;
				break;
			}
			// case 0b0110:
			case 0b0111000000000000:
			{
				result = 0b1110000000000000;
				break;
			}
			case 0b1000000000000000:
			{
				result = 0b0001000000000000;
				break;
			}
			// case 0b1001:
			case 0b1010000000000000:
			{
				result = 0b0101000000000000;
				break;
			}
			case 0b1011000000000000:
			{
				result = 0b1101000000000000;
				break;
			}
			case 0b1100000000000000:
			{
				result = 0b0011000000000000;
				break;
			}
			case 0b1101000000000000:
			{
				result = 0b1011000000000000;
				break;
			}
			case 0b1110000000000000:
			{
				result = 0b0111000000000000;
				break;
			}
			// case 0b1111:
			case 0b00010000000000000000:
			{
				result = 0b10000000000000000000;
				break;
			}
			case 0b00100000000000000000:
			{
				result = 0b01000000000000000000;
				break;
			}
			case 0b00110000000000000000:
			{
				result = 0b11000000000000000000;
				break;
			}
			case 0b01000000000000000000:
			{
				result = 0b00100000000000000000;
				break;
			}
			case 0b01010000000000000000:
			{
				result = 0b10100000000000000000;
				break;
			}
			// case 0b0110:
			case 0b01110000000000000000:
			{
				result = 0b11100000000000000000;
				break;
			}
			case 0b10000000000000000000:
			{
				result = 0b00010000000000000000;
				break;
			}
			// case 0b1001:
			case 0b10100000000000000000:
			{
				result = 0b01010000000000000000;
				break;
			}
			case 0b10110000000000000000:
			{
				result = 0b11010000000000000000;
				break;
			}
			case 0b11000000000000000000:
			{
				result = 0b00110000000000000000;
				break;
			}
			case 0b11010000000000000000:
			{
				result = 0b10110000000000000000;
				break;
			}
			case 0b11100000000000000000:
			{
				result = 0b01110000000000000000;
				break;
			}
			// case 0b1111:
			case 0b000100000000000000000000:
			{
				result = 0b100000000000000000000000;
				break;
			}
			case 0b001000000000000000000000:
			{
				result = 0b010000000000000000000000;
				break;
			}
			case 0b001100000000000000000000:
			{
				result = 0b110000000000000000000000;
				break;
			}
			case 0b010000000000000000000000:
			{
				result = 0b001000000000000000000000;
				break;
			}
			case 0b010100000000000000000000:
			{
				result = 0b101000000000000000000000;
				break;
			}
			// case 0b0110:
			case 0b011100000000000000000000:
			{
				result = 0b111000000000000000000000;
				break;
			}
			case 0b100000000000000000000000:
			{
				result = 0b000100000000000000000000;
				break;
			}
			// case 0b1001:
			case 0b101000000000000000000000:
			{
				result = 0b010100000000000000000000;
				break;
			}
			case 0b101100000000000000000000:
			{
				result = 0b110100000000000000000000;
				break;
			}
			case 0b110000000000000000000000:
			{
				result = 0b001100000000000000000000;
				break;
			}
			case 0b110100000000000000000000:
			{
				result = 0b101100000000000000000000;
				break;
			}
			case 0b111000000000000000000000:
			{
				result = 0b011100000000000000000000;
				break;
			}
			// case 0b1111:
			case 0b0001000000000000000000000000:
			{
				result = 0b1000000000000000000000000000;
				break;
			}
			case 0b0010000000000000000000000000:
			{
				result = 0b0100000000000000000000000000;
				break;
			}
			case 0b0011000000000000000000000000:
			{
				result = 0b1100000000000000000000000000;
				break;
			}
			case 0b0100000000000000000000000000:
			{
				result = 0b0010000000000000000000000000;
				break;
			}
			case 0b0101000000000000000000000000:
			{
				result = 0b1010000000000000000000000000;
				break;
			}
			// case 0b0110:
			case 0b0111000000000000000000000000:
			{
				result = 0b1110000000000000000000000000;
				break;
			}
			case 0b1000000000000000000000000000:
			{
				result = 0b0001000000000000000000000000;
				break;
			}
			// case 0b1001:
			case 0b1010000000000000000000000000:
			{
				result = 0b0101000000000000000000000000;
				break;
			}
			case 0b1011000000000000000000000000:
			{
				result = 0b1101000000000000000000000000;
				break;
			}
			case 0b1100000000000000000000000000:
			{
				result = 0b0011000000000000000000000000;
				break;
			}
			case 0b1101000000000000000000000000:
			{
				result = 0b1011000000000000000000000000;
				break;
			}
			case 0b1110000000000000000000000000:
			{
				result = 0b0111000000000000000000000000;
				break;
			}
			// case 0b1111:
			case 0b00010000000000000000000000000000:
			{
				result = 0b10000000000000000000000000000000;
				break;
			}
			case 0b00100000000000000000000000000000:
			{
				result = 0b01000000000000000000000000000000;
				break;
			}
			case 0b00110000000000000000000000000000:
			{
				result = 0b11000000000000000000000000000000;
				break;
			}
			case 0b01000000000000000000000000000000:
			{
				result = 0b00100000000000000000000000000000;
				break;
			}
			case 0b01010000000000000000000000000000:
			{
				result = 0b10100000000000000000000000000000;
				break;
			}
			// case 0b0110:
			case 0b01110000000000000000000000000000:
			{
				result = 0b11100000000000000000000000000000;
				break;
			}
			case 0b10000000000000000000000000000000:
			{
				result = 0b00010000000000000000000000000000;
				break;
			}
			// case 0b1001:
			case 0b10100000000000000000000000000000:
			{
				result = 0b01010000000000000000000000000000;
				break;
			}
			case 0b10110000000000000000000000000000:
			{
				result = 0b11010000000000000000000000000000;
				break;
			}
			case 0b11000000000000000000000000000000:
			{
				result = 0b00110000000000000000000000000000;
				break;
			}
			case 0b11010000000000000000000000000000:
			{
				result = 0b10110000000000000000000000000000;
				break;
			}
			case 0b11100000000000000000000000000000:
			{
				result = 0b01110000000000000000000000000000;
				break;
			}
			// case 0b1111:
			default:
			{
				throw new IndexOutOfBoundsException("bad case: " + Nibble);
			}
		}
		
		return result;
	}
}
