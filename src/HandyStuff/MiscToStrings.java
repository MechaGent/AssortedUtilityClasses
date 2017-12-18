package HandyStuff;

import Collections.Lists.CharList.CharList;
import HandyStuff.HandyEnums.EndianSettings;

public class MiscToStrings
{
	private static final char[] raw_true = "true".toCharArray();
	private static final char[] raw_false = "false".toCharArray();
	
	public static char[] toCharArr(boolean in)
	{
		if(in)
		{
			return raw_true;
		}
		else
		{
			return raw_false;
		}
	}
	
	public static String toString(String[] in, String delim)
	{
		CharList result = new CharList();

		for (int i = 0; i < in.length; i++)
		{
			if (i != 0)
			{
				result.add(delim);
			}

			result.add(in[i]);
		}

		return result.toString();
	}

	public static String toString(Object[] in, String delim)
	{
		CharList result = new CharList();

		for (int i = 0; i < in.length; i++)
		{
			if (i != 0)
			{
				result.add(delim);
			}

			result.add(in[i].toString());
		}

		return result.toString();
	}

	public static String toString(byte[] in, String delim)
	{
		CharList result = new CharList();

		for (int i = 0; i < in.length; i++)
		{
			if (i != 0)
			{
				result.add(delim);
			}

			result.add(Byte.toString(in[i]));
		}

		return result.toString();
	}

	public static String toString(int[] in, String delim)
	{
		CharList result = new CharList();

		for (int i = 0; i < in.length; i++)
		{
			if (i != 0)
			{
				result.add(delim);
			}

			result.add(Integer.toString(in[i]));
		}

		return result.toString();
	}

	/*
	public static void main(String[] args)
	{
		final int testNum = 0x1867C64E;
		final String homeResult = toHexString(testNum);
		final String javaResult = Integer.toHexString(testNum);
		System.out.println(homeResult + "\r\n" + javaResult);
	}
	*/

	public static String toHexString(int HexNum)
	{
		return toHexString(HexNum, " ", true, true, false);
	}

	/**
	 * assumes a delimiter of 1 space, and that letters should be capitalized
	 * @param HexNum
	 * @param endSet
	 * @return
	 */
	public static String toHexString(int HexNum, EndianSettings endSet)
	{
		return toHexString(HexNum, " ", endSet, true);
	}
	
	public static CharList toHext(int HexNum, EndianSettings endSet)
	{
		return toHext(HexNum, " ", endSet, true);
	}
	
	public static String toHexString(int HexNum, String Delim, EndianSettings endSet, boolean wantsCaps)
	{
		return toHext(HexNum, Delim, endSet, wantsCaps).toString();
	}
	
	public static CharList toHext(int HexNum, String Delim, EndianSettings endSet, boolean wantsCaps)
	{
		final boolean wantsHighLowBits;
		final boolean wantsHighLowBytes;

		switch (endSet)
		{
			case LeftBitLeftByte:
			{
				wantsHighLowBits = true;
				wantsHighLowBytes = true;
				break;
			}
			case LeftBitRightByte:
			{
				wantsHighLowBits = true;
				wantsHighLowBytes = false;
				break;
			}
			case RightBitLeftByte:
			{
				wantsHighLowBits = false;
				wantsHighLowBytes = true;
				break;
			}
			case RightBitRightByte:
			{
				wantsHighLowBits = false;
				wantsHighLowBytes = false;
				break;
			}
			default:
			{
				throw new IllegalArgumentException();
			}
		}

		return toHext(HexNum, Delim, wantsHighLowBits, wantsHighLowBytes, wantsCaps);
	}

	public static String toHexString(int HexNum, String Delim, boolean wantsHighLowBits, boolean wantsHighLowBytes, boolean wantsCaps)
	{
		return toHext(HexNum, Delim, wantsHighLowBits, wantsHighLowBytes, wantsCaps).toString();
	}
	
	public static CharList toHext(int HexNum, String Delim, boolean wantsHighLowBits, boolean wantsHighLowBytes, boolean wantsCaps)
	{
		final byte[] raw = new byte[4];

		raw[0] = (byte) (HexNum >>> 24);
		raw[1] = (byte) (HexNum >>> 16);
		raw[2] = (byte) (HexNum >>> 8);
		raw[3] = (byte) (HexNum);

		return toHext(raw, Delim, wantsHighLowBits, wantsHighLowBytes, wantsCaps);
	}

	public static String toHexString(byte[] HexNums)
	{
		return toHext(HexNums, " ", true, false, true).toString();
	}

	public static CharList toHext(byte[] HexNums, String Delim, boolean wantsHighLowBits, boolean wantsHighLowBytes, boolean wantsCaps)
	{
		final CharList result = new CharList("0x");

		if (wantsHighLowBytes)
		{
			for (int i = 0; i < HexNums.length; i++)
			{
				if (i != 0)
				{
					result.add(Delim);
				}

				result.add(toHext(HexNums[i], wantsHighLowBits, wantsCaps), true);
			}
		}
		else
		{
			for (int i = HexNums.length - 1; i > -1; i--)
			{
				if (i != HexNums.length - 1)
				{
					result.add(Delim);
				}

				result.add(toHext(HexNums[i], wantsHighLowBits, wantsCaps), true);
			}
		}

		return result;
	}

	public static String toHexString(byte HexNum)
	{
		return toHext(HexNum).toString();
	}

	public static CharList toHext(byte HexNum)
	{
		return toHext(HexNum, true, true);
	}

	public static String toHexString(byte HexNum, boolean wantsCaps)
	{
		return toHexString(HexNum, true, wantsCaps);
	}

	public static CharList toHext(byte HexNum, boolean wantsHighLow, boolean wantsCaps)
	{
		final byte DigitHigh = (byte) ((HexNum >>> 4) & 0xF);
		final byte DigitLow = (byte) (HexNum & 0xF);
		final char High;
		final char Low;

		if (wantsHighLow)
		{
			High = fromNibbleToHexString(DigitHigh, wantsCaps);
			Low = fromNibbleToHexString(DigitLow, wantsCaps);
		}
		else
		{
			Low = fromNibbleToHexString(DigitHigh, wantsCaps);
			High = fromNibbleToHexString(DigitLow, wantsCaps);
		}

		final CharList result = new CharList();

		result.add(High);
		result.add(Low);

		return result;
	}

	public static String toHexString(byte HexNum, boolean wantsHighLow, boolean wantsCaps)
	{
		return toHext(HexNum, wantsHighLow, wantsCaps).toString();
	}

	public static char fromNibbleToHexString(int Nibble, boolean wantsCaps)
	{
		final char result;

		switch (Nibble)
		{
			case 0:
			{
				result = '0';
				break;
			}
			case 1:
			{
				result = '1';
				break;
			}
			case 2:
			{
				result = '2';
				break;
			}
			case 3:
			{
				result = '3';
				break;
			}
			case 4:
			{
				result = '4';
				break;
			}
			case 5:
			{
				result = '5';
				break;
			}
			case 6:
			{
				result = '6';
				break;
			}
			case 7:
			{
				result = '7';
				break;
			}
			case 8:
			{
				result = '8';
				break;
			}
			case 9:
			{
				result = '9';
				break;
			}
			case 10:
			{
				if (wantsCaps)
				{
					result = 'A';
				}
				else
				{
					result = 'a';
				}
				break;
			}
			case 11:
			{
				if (wantsCaps)
				{
					result = 'B';
				}
				else
				{
					result = 'b';
				}
				break;
			}
			case 12:
			{
				if (wantsCaps)
				{
					result = 'C';
				}
				else
				{
					result = 'c';
				}
				break;
			}
			case 13:
			{
				if (wantsCaps)
				{
					result = 'D';
				}
				else
				{
					result = 'd';
				}
				break;
			}
			case 14:
			{
				if (wantsCaps)
				{
					result = 'E';
				}
				else
				{
					result = 'e';
				}
				break;
			}
			case 15:
			{
				if (wantsCaps)
				{
					result = 'F';
				}
				else
				{
					result = 'f';
				}
				break;
			}
			default:
			{
				throw new ArrayIndexOutOfBoundsException();
			}
		}

		return result;
	}

	public static String toNumericChars(String in, String Delim)
	{
		final CharList result = new CharList();
		final char[] raw = in.toCharArray();

		for (int i = 0; i < raw.length; i++)
		{
			if (i != 0)
			{
				result.add(Delim);
			}

			result.add(Integer.toHexString(raw[i]));
		}

		return result.toString();
	}
}
