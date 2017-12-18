package HandyStuff;

import CustomExceptions.UnhandledEnumException;

public class BinarySlotMasks
{
	public static final int mask_ByteSlot0 = 0xff;
	public static final int mask_ByteSlot1 = mask_ByteSlot0 << 8;
	public static final int mask_ByteSlot2 = mask_ByteSlot1 << 8;
	public static final int mask_ByteSlot3 = mask_ByteSlot2 << 8;
	public static final int mask_ByteSlot4 = mask_ByteSlot3 << 8;
	public static final int mask_ByteSlot5 = mask_ByteSlot4 << 8;
	public static final int mask_ByteSlot6 = mask_ByteSlot5 << 8;
	public static final int mask_ByteSlot7 = mask_ByteSlot6 << 8;

	public static final int mask_HalfByteSlot00 = 0xf;
	public static final int mask_HalfByteSlot01 = mask_HalfByteSlot00 << 4;
	public static final int mask_HalfByteSlot02 = mask_HalfByteSlot01 << 4;
	public static final int mask_HalfByteSlot03 = mask_HalfByteSlot02 << 4;
	public static final int mask_HalfByteSlot04 = mask_HalfByteSlot03 << 4;
	public static final int mask_HalfByteSlot05 = mask_HalfByteSlot04 << 4;
	public static final int mask_HalfByteSlot06 = mask_HalfByteSlot05 << 4;
	public static final int mask_HalfByteSlot07 = mask_HalfByteSlot06 << 4;
	public static final int mask_HalfByteSlot08 = mask_HalfByteSlot07 << 4;
	public static final int mask_HalfByteSlot09 = mask_HalfByteSlot08 << 4;
	public static final int mask_HalfByteSlot10 = mask_HalfByteSlot09 << 4;
	public static final int mask_HalfByteSlot11 = mask_HalfByteSlot10 << 4;
	public static final int mask_HalfByteSlot12 = mask_HalfByteSlot11 << 4;
	public static final int mask_HalfByteSlot13 = mask_HalfByteSlot12 << 4;
	public static final int mask_HalfByteSlot14 = mask_HalfByteSlot13 << 4;
	public static final int mask_HalfByteSlot15 = mask_HalfByteSlot14 << 4;

	public static final int getMask(MaskLengths maskLength, int slotIndex)
	{
		switch (maskLength)
		{
			case Byte:
			{
				return getByteMask(slotIndex);
			}
			case HalfByte:
			{
				return getHalfByteMask(slotIndex);
			}
			default:
			{
				throw new UnhandledEnumException(maskLength);
			}
		}
	}

	public static final int getByteMask(int slotIndex)
	{
		final int result;

		switch (slotIndex)
		{
			case 0:
			{
				result = mask_ByteSlot0;
				break;
			}
			case 1:
			{
				result = mask_ByteSlot1;
				break;
			}
			case 2:
			{
				result = mask_ByteSlot2;
				break;
			}
			case 3:
			{
				result = mask_ByteSlot3;
				break;
			}
			case 4:
			{
				result = mask_ByteSlot4;
				break;
			}
			case 5:
			{
				result = mask_ByteSlot5;
				break;
			}
			case 6:
			{
				result = mask_ByteSlot6;
				break;
			}
			case 7:
			{
				result = mask_ByteSlot7;
				break;
			}
			default:
			{
				throw new IndexOutOfBoundsException("bad index: " + slotIndex);
			}
		}

		return result;
	}

	public static final int getHalfByteMask(int slotIndex)
	{
		final int result;
		
		switch(slotIndex)
		{
			case 0:
			{
				result = mask_HalfByteSlot00;
				break;
			}
			case 1:
			{
				result = mask_HalfByteSlot01;
				break;
			}
			case 2:
			{
				result = mask_HalfByteSlot02;
				break;
			}
			case 3:
			{
				result = mask_HalfByteSlot03;
				break;
			}
			case 4:
			{
				result = mask_HalfByteSlot04;
				break;
			}
			case 5:
			{
				result = mask_HalfByteSlot05;
				break;
			}
			case 6:
			{
				result = mask_HalfByteSlot06;
				break;
			}
			case 7:
			{
				result = mask_HalfByteSlot07;
				break;
			}
			case 8:
			{
				result = mask_HalfByteSlot08;
				break;
			}
			case 9:
			{
				result = mask_HalfByteSlot09;
				break;
			}
			case 10:
			{
				result = mask_HalfByteSlot10;
				break;
			}
			case 11:
			{
				result = mask_HalfByteSlot11;
				break;
			}
			case 12:
			{
				result = mask_HalfByteSlot12;
				break;
			}
			case 13:
			{
				result = mask_HalfByteSlot13;
				break;
			}
			case 14:
			{
				result = mask_HalfByteSlot14;
				break;
			}
			case 15:
			{
				result = mask_HalfByteSlot15;
				break;
			}
			default:
			{
				throw new IndexOutOfBoundsException("bad index: " + slotIndex);
			}
		}
		
		return result;
	}

	public static enum MaskLengths
	{
		Byte,
		HalfByte;
	}
}
