package Collections.Lists.CharList;

import CustomExceptions.UnhandledEnumException;

/**
 * 
 * All credit for the core algorithm used in {@link #getNumDecDigits(int)}, {@link #getNumDecDigits(long)}, {@link #toDecString(int)}, and {@link #toDecString(long)} goes to Andrei Alexandrescu: {@link https://www.facebook.com/notes/facebook-engineering/three-optimization-tips-for-c/10151361643253920}
 *
 */
public class NumNodeConversion
{
	private static final char[] digitCharsRef = initDigitCharsRef();
	private static final int nibbleMask = 0x0f;

	private static final int P01 = 10;
	private static final int P02 = 10 * P01;
	private static final int P03 = 10 * P02;
	private static final int P04 = 10 * P03;
	private static final int P05 = 10 * P04;
	private static final int P06 = 10 * P05;
	private static final int P07 = 10 * P06;
	private static final int P08 = 10 * P07;
	private static final int P09 = 10 * P08;
	private static final long P10 = 10L * P09;
	private static final long P11 = 10L * P10;
	private static final long P12 = 10L * P11;

	private static final char[] initDigitCharsRef()
	{
		final CharList result = new CharList();

		result.add("0001020304050607080910111213141516171819");
		result.add("2021222324252627282930313233343536373839");
		result.add("4041424344454647484950515253545556575859");
		result.add("6061626364656667686970717273747576777879");
		result.add("8081828384858687888990919293949596979899");

		return result.toCharArray();
	}

	/*
	 * int-arged methods
	 */

	/**
	 * is prepended by "0b" and is always 34 characters in length
	 * 
	 * @param raw
	 * @return
	 */
	public static final String toBinString(int raw)
	{
		return new String(toBinCharArr_withPrefix(raw));
	}

	public static final String toDecString(int raw)
	{
		return new String(toDecCharArr(raw));
	}

	/**
	 * is prepended by "0x" and is always 6 characters in length
	 * 
	 * @param raw
	 * @return
	 */
	public static final String toHexString(int raw)
	{
		return new String(toHexCharArr_withPrefix(raw));
	}

	/*
	 * long-arged methods
	 */

	/**
	 * is prepended by "0b" and is always 66 characters in length
	 * 
	 * @param raw
	 * @return
	 */
	public static final String toBinString(long raw)
	{
		return new String(toBinCharArr_withPrefix(raw));
	}

	public static final String toDecString(long raw)
	{
		return new String(toDecCharArr(raw));
	}

	/**
	 * is prepended by "0x" and is always 10 characters in length
	 * 
	 * @param raw
	 * @return
	 */
	public static final String toHexString(long raw)
	{
		return new String(toHexCharArr_withPrefix(raw));
	}

	/*
	 * other-arged methods
	 */

	public static final String toDecString(double raw)
	{
		return new String(toDecCharArr(raw));
	}

	/*
	 * char[] methods
	 */

	/**
	 * is always 32 characters in length
	 * 
	 * @param raw
	 * @return
	 */
	public static final char[] toBinCharArr_noPrefix(int raw)
	{
		final char[] result = new char[32 + 2];

		int mask = 0x01;

		for (int i = result.length - 1; i >= 0; i--)
		{
			if ((raw & mask) == 0)
			{
				result[i] = '0';
			}
			else
			{
				result[i] = '1';
			}

			mask = mask << 1;
		}

		return result;
	}

	/**
	 * is prepended by "0b" and is always 34 characters in length
	 * 
	 * @param raw
	 * @return
	 */
	public static final char[] toBinCharArr_withPrefix(int raw)
	{
		final char[] result = new char[32 + 2];
		result[0] = '0';
		result[1] = 'b';

		int mask = 0x01;

		for (int i = result.length - 1; i >= 2; i--)
		{
			if ((raw & mask) == 0)
			{
				result[i] = '0';
			}
			else
			{
				result[i] = '1';
			}

			mask = mask << 1;
		}

		return result;
	}

	/**
	 * is always 64 characters in length
	 * 
	 * @param raw
	 * @return
	 */
	public static final char[] toBinCharArr_noPrefix(long raw)
	{
		final char[] result = new char[64];

		long mask = 0x01;

		for (int i = result.length - 1; i >= 0; i--)
		{
			if ((raw & mask) == 0)
			{
				result[i] = '0';
			}
			else
			{
				result[i] = '1';
			}

			mask = mask << 1;
		}

		return result;
	}

	/**
	 * is prepended by "0b" and is always 66 characters in length
	 * 
	 * @param raw
	 * @return
	 */
	public static final char[] toBinCharArr_withPrefix(long raw)
	{
		final char[] result = new char[64 + 2];
		result[0] = '0';
		result[1] = 'b';

		long mask = 0x01;

		for (int i = result.length - 1; i >= 2; i--)
		{
			if ((raw & mask) == 0)
			{
				result[i] = '0';
			}
			else
			{
				result[i] = '1';
			}

			mask = mask << 1;
		}

		return result;
	}

	public static final char[] toDecCharArr(int raw)
	{
		final int length;
		final char[] result;

		if (raw < 0)
		{
			raw = -raw;
			length = getNumDecDigits_unsigned(raw) + 1;
			result = new char[length];
			result[0] = '-';
		}
		else
		{
			length = getNumDecDigits_unsigned(raw);
			result = new char[length];
		}

		int next = length - 1;

		while (raw >= 100)
		{
			final int i = (raw % 100) * 2;
			raw /= 100;
			result[next--] = digitCharsRef[i + 1];
			result[next--] = digitCharsRef[i];
		}

		// Handle last 1 or 2 digits
		if (raw < 10)
		{
			result[next] = (char) ('0' + raw);
		}
		else
		{
			int i = raw * 2;
			result[next--] = digitCharsRef[i + 1];
			result[next] = digitCharsRef[i];
		}

		return result;
	}

	public static final char[] toDecCharArr(long raw)
	{
		final int length;
		final char[] result;

		if (raw < 0)
		{
			raw = -raw;
			length = getNumDecDigits_unsigned(raw) + 1;
			result = new char[length];
			result[0] = '-';
		}
		else
		{
			length = getNumDecDigits_unsigned(raw);
			result = new char[length];
		}

		int next = length - 1;

		while (raw >= 100)
		{
			final int i = (int) ((raw % 100) * 2);
			raw /= 100;
			result[next--] = digitCharsRef[i + 1];
			result[next--] = digitCharsRef[i];
		}

		// Handle last 1 or 2 digits
		if (raw < 10)
		{
			result[next] = (char) ('0' + raw);
		}
		else
		{
			int i = (int) (raw * 2);
			result[next--] = digitCharsRef[i + 1];
			result[next] = digitCharsRef[i];
		}

		return result;
	}

	/**
	 * terrible implementation - re-implement later
	 * 
	 * @param raw
	 * @return
	 */
	public static final char[] toDecCharArr(double raw)
	{
		return Double.toString(raw).toCharArray();
	}

	public static final char[] toHexCharArr_noPrefix(byte[] rawBytes)
	{
		final char[] result = new char[rawBytes.length * 2];

		int rawBytesIndex = 0;
		int resultIndex = 0;

		while (rawBytesIndex < rawBytes.length)
		{
			final byte curr = rawBytes[rawBytesIndex++];

			result[resultIndex++] = getHexChar_inCaps((curr >>> 4) & 0xf);
			result[resultIndex++] = getHexChar_inCaps(curr & 0xf);
		}

		return result;
	}

	/**
	 * is always 8 characters in length
	 * 
	 * @param raw
	 * @return
	 */
	public static final char[] toHexCharArr_noPrefix(int raw)
	{
		final char[] result = new char[8];

		for (int i = result.length - 1; i >= 0; i--)
		{
			final int nibble = raw & nibbleMask;
			result[i] = getHexChar_inCaps(nibble);
			raw >>>= 4;
		}

		return result;
	}

	public static final char[] toHexCharArr_noPrefix(int raw, int numChars)
	{
		final char[] result = new char[numChars];

		while (numChars > 0)
		{
			final int nibble = raw & nibbleMask;
			result[--numChars] = getHexChar_inCaps(nibble);
			raw >>>= 4;
		}

		return result;
	}

	public static final void intoHexCharArr_noPrefix(int raw, char[] result, int startIndex)
	{
		for (int i = startIndex + 8 - 1; i >= startIndex; i--)
		{
			final int nibble = raw & nibbleMask;
			result[i] = getHexChar_inCaps(nibble);
			raw >>>= 4;
		}
	}
	
	public static final void intoHexCharArr_noPrefix(byte raw, char[] result, int startIndex)
	{
		result[startIndex++] = getHexChar_inCaps((raw >>> 4) & 0xf);
		result[startIndex] = getHexChar_inCaps(raw & 0xf);
	}

	/**
	 * is prepended by "0x" and is always 10 characters in length
	 * 
	 * @param raw
	 * @return
	 */
	public static final char[] toHexCharArr_withPrefix(int raw)
	{
		// System.out.println(Integer.toHexString(raw));
		final char[] result = new char[8 + 2];
		result[0] = '0';
		result[1] = 'x';

		for (int i = result.length - 1; i >= 2; i--)
		{
			final int nibble = raw & nibbleMask;
			result[i] = getHexChar_inCaps(nibble);
			raw >>>= 4;
		}

		return result;
	}

	public static final void intoHexCharArr_withPrefix(int raw, char[] result, int startIndex)
	{
		int i = startIndex + 8 + 2 - 1;
		result[startIndex++] = '0';
		result[startIndex++] = 'x';

		for (; i >= startIndex; i--)
		{
			final int nibble = raw & nibbleMask;
			result[i] = getHexChar_inCaps(nibble);
			raw >>>= 4;
		}
	}

	/**
	 * is always 16 characters in length
	 * 
	 * @param raw
	 * @return
	 */
	public static final char[] toHexCharArr_noPrefix(long raw)
	{
		final char[] result = new char[16];

		for (int i = result.length - 1; i >= 0; i--)
		{
			final int nibble = (int) (raw & nibbleMask);
			result[i] = getHexChar_inCaps(nibble);
			raw = raw >>> 4;
		}

		return result;
	}

	/**
	 * is prepended by "0x" and is always 18 characters in length
	 * 
	 * @param raw
	 * @return
	 */
	public static final char[] toHexCharArr_withPrefix(long raw)
	{
		final char[] result = new char[16 + 2];
		result[0] = '0';
		result[1] = 'x';

		for (int i = result.length - 1; i >= 2; i--)
		{
			final int nibble = (int) (raw & nibbleMask);
			result[i] = getHexChar_inCaps(nibble);
			raw = raw >>> 4;
		}

		// System.out.println("processed:\r\n\traw: " + Long.toHexString(raw) + "\r\n\tpro: " + new String(result));
		return result;
	}

	/**
	 * 
	 * @param Nibble
	 * @return the character representing the hex digit given by {@code Nibble}, with values 10 through 15 represented by 'A' through 'F'
	 */
	public static final char getHexChar_inCaps(int Nibble)
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
				result = 'A';
				break;
			}
			case 11:
			{
				result = 'B';
				break;
			}
			case 12:
			{
				result = 'C';
				break;
			}
			case 13:
			{
				result = 'D';
				break;
			}
			case 14:
			{
				result = 'E';
				break;
			}
			case 15:
			{
				result = 'F';
				break;
			}
			default:
			{
				throw new ArrayIndexOutOfBoundsException("" + Nibble);
			}
		}

		return result;
	}

	/**
	 * 
	 * @param Nibble
	 * @return the character representing the hex digit given by {@code Nibble}, with values 10 through 15 represented by 'a' through 'f'
	 */
	public static final char getHexChar_noCaps(int Nibble)
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
				result = 'a';
				break;
			}
			case 11:
			{
				result = 'b';
				break;
			}
			case 12:
			{
				result = 'c';
				break;
			}
			case 13:
			{
				result = 'd';
				break;
			}
			case 14:
			{
				result = 'e';
				break;
			}
			case 15:
			{
				result = 'f';
				break;
			}
			default:
			{
				throw new ArrayIndexOutOfBoundsException();
			}
		}

		return result;
	}

	/**
	 * 
	 * @param raw
	 *            is assumed to be limited in magnitude as if signed, but, practically speaking, should always be positive
	 * @return
	 */
	public static final int getNumDecDigits_unsigned(int raw)
	{
		if (raw < P01)
		{
			return 1;
		}
		else if (raw < P02)
		{
			return 2;
		}
		else if (raw < P03)
		{
			return 3;
		}
		else if (raw < P12)
		{

			if (raw < P08)
			{
				if (raw < P06)
				{
					if (raw < P04)
					{
						return 4;
					}
					else
					{
						if (raw < P05)
						{
							return 5;
						}
						else
						{
							return 6;
						}
					}
				}
				else
				{
					if (raw < P07)
					{
						return 7;
					}
					else
					{
						return 7 + 1;
					}
				}
			}
			else if (raw < P10)
			{
				if (raw < P09)
				{
					return 9;
				}
				else
				{
					return 10;
				}
			}
			else
			{
				if (raw < P11)
				{
					return 11;
				}
				else
				{
					return 12;
				}
			}
		}
		else
		{
			return 12 + getNumDecDigits_unsigned(raw / P12);
		}
	}

	/**
	 * 
	 * @param raw
	 *            is assumed to be limited in magnitude as if signed, but, practically speaking, should always be positive
	 * @return
	 */
	public static final int getNumDecDigits_unsigned(long raw)
	{
		if (raw < P01)
		{
			return 1;
		}
		else if (raw < P02)
		{
			return 2;
		}
		else if (raw < P03)
		{
			return 3;
		}
		else if (raw < P12)
		{

			if (raw < P08)
			{
				if (raw < P06)
				{
					if (raw < P04)
					{
						return 4;
					}
					else
					{
						if (raw < P05)
						{
							return 5;
						}
						else
						{
							return 6;
						}
					}
				}
				else
				{
					if (raw < P07)
					{
						return 7;
					}
					else
					{
						return 7 + 1;
					}
				}
			}
			else if (raw < P10)
			{
				if (raw < P09)
				{
					return 9;
				}
				else
				{
					return 10;
				}
			}
			else
			{
				if (raw < P11)
				{
					return 11;
				}
				else
				{
					return 12;
				}
			}
		}
		else
		{
			return 12 + getNumDecDigits_unsigned(raw / P12);
		}
	}

	/**
	 * assumes the front is to be padded, not the back
	 * 
	 * @param raw
	 * @param minPad
	 * @return
	 */
	public static char[] toDecCharArr_Padded(int raw, int minPad)
	{
		int numDigits = getNumDecDigits_unsigned(raw);
		final char[] result;

		/*
		 * nested this instead of linearizing it so as to not need two different (raw < 0) compares.
		 */
		if (raw < 0)
		{
			final int dif = minPad - (++numDigits);

			if (dif > 0)
			{
				result = new char[minPad];
				result[0] = '-';

				// might need to do ++dif here
				for (int i = 1; i < dif; i++)
				{
					result[i] = '0';
				}
			}
			else
			{
				result = new char[numDigits];
				result[0] = '-';
			}
		}
		else
		{
			final int dif = minPad - (++numDigits);

			if (dif > 0)
			{
				result = new char[minPad];

				for (int i = 0; i < dif - 1; i++)
				{
					result[i] = '0';
				}
			}
			else
			{
				result = new char[numDigits];
			}
		}

		int next = result.length - 1;

		while (raw >= 100)
		{
			final int i = (raw % 100) * 2;
			raw /= 100;
			result[next--] = digitCharsRef[i + 1];
			result[next--] = digitCharsRef[i];
		}

		// Handle last 1 or 2 digits
		if (raw < 10)
		{
			result[next] = (char) ('0' + raw);
		}
		else
		{
			int i = raw * 2;
			result[next--] = digitCharsRef[i + 1];
			result[next] = digitCharsRef[i];
		}

		return result;
	}

	public static final void addCodepointToStringBuilder(StringBuilder in, int codepoint)
	{
		if ((codepoint & 0xffff0000) != 0)
		{
			final char[] raw = new char[2];
			raw[0] = (char) ((codepoint >> 16) & 0xffff);
			raw[1] = (char) (codepoint & 0xffff);

			/*
			if((((int) raw[0]) != ((codepoint >> 16) & 0xffff)) || (((int) raw[1]) != (codepoint & 0xffff)))
			{
				throw new IllegalArgumentException();
			}
			*/

			in.append(raw);
		}
		else
		{
			in.append((char) codepoint);
		}
	}

	public static final long parseLong(char[] raw)
	{
		return parseLong(raw, 0);
	}

	public static final long parseLong(char[] raw, int start)
	{
		if (raw.length < start + 1)
		{
			throw new IllegalArgumentException();
		}

		switch (raw[start])
		{
			case '-':
			{
				return -parseLong(raw, start + 1);
			}
			case '+':
			{
				return parseLong(raw, start + 1);
			}
			case '0':
			{
				if ((++start) < raw.length)
				{
					switch (raw[start])
					{
						case 'x':
						{
							return parseLong_base16(raw, start + 1);
						}
						case 'b':
						{
							return parseLong_base2(raw, start + 1);
						}
						default:
						{
							return parseLong_base10(raw, start);
						}
					}
				}
				else
				{
					return 0;
				}
			}
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			{
				return parseLong_base10(raw, start);
			}
			default:
			{
				throw new UnhandledEnumException(raw);
			}
		}
	}

	public static final long parseLong(String raw)
	{
		return parseLong(raw, 0);
	}

	public static final long parseLong(String raw, int start)
	{
		if (raw.length() < start + 1)
		{
			throw new IllegalArgumentException();
		}

		switch (raw.charAt(start))
		{
			case '-':
			{
				return -parseLong(raw, start + 1);
			}
			case '+':
			{
				return parseLong(raw, start + 1);
			}
			case '0':
			{
				if ((++start) < raw.length())
				{
					switch (raw.charAt(start))
					{
						case 'b':
						{
							return parseLong_base16(raw, start + 1);
						}
						case 'x':
						{
							return parseLong_base2(raw, start + 1);
						}
						default:
						{
							return parseLong_base10(raw, start);
						}
					}
				}
				else
				{
					return 0;
				}
			}
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			{
				return parseLong_base10(raw, start);
			}
			default:
			{
				throw new UnhandledEnumException(raw);
			}
		}
	}

	public static final long parseLong_base10(char[] raw, int start)
	{
		long result = 0;

		while (start < raw.length)
		{
			switch (raw[start++])
			{
				case '0':
				{
					result *= 10;
					break;
				}
				case '1':
				{
					result = (result * 10) + 1;
					break;
				}
				case '2':
				{
					result = (result * 10) + 2;
					break;
				}
				case '3':
				{
					result = (result * 10) + 3;
					break;
				}
				case '4':
				{
					result = (result * 10) + 4;
					break;
				}
				case '5':
				{
					result = (result * 10) + 5;
					break;
				}
				case '6':
				{
					result = (result * 10) + 6;
					break;
				}
				case '7':
				{
					result = (result * 10) + 7;
					break;
				}
				case '8':
				{
					result = (result * 10) + 8;
					break;
				}
				case '9':
				{
					result = (result * 10) + 9;
					break;
				}
				default:
				{
					throw new UnhandledEnumException(raw);
				}
			}
		}

		return result;
	}

	public static final long parseLong_base10(String raw, int start)
	{
		long result = 0;

		while (start < raw.length())
		{
			switch (raw.charAt(start++))
			{
				case '0':
				{
					result *= 10;
					break;
				}
				case '1':
				{
					result = (result * 10) + 1;
					break;
				}
				case '2':
				{
					result = (result * 10) + 2;
					break;
				}
				case '3':
				{
					result = (result * 10) + 3;
					break;
				}
				case '4':
				{
					result = (result * 10) + 4;
					break;
				}
				case '5':
				{
					result = (result * 10) + 5;
					break;
				}
				case '6':
				{
					result = (result * 10) + 6;
					break;
				}
				case '7':
				{
					result = (result * 10) + 7;
					break;
				}
				case '8':
				{
					result = (result * 10) + 8;
					break;
				}
				case '9':
				{
					result = (result * 10) + 9;
					break;
				}
				default:
				{
					throw new UnhandledEnumException(raw);
				}
			}
		}

		return result;
	}

	public static final long parseLong_base16(char[] raw)
	{
		return parseLong_base16(raw, 0);
	}

	public static final long parseLong_base16(char[] raw, int start)
	{
		long result = 0;

		while (start < raw.length)
		{
			switch (raw[start++])
			{
				case '0':
				{
					result = result * 16;
					break;
				}
				case '1':
				{
					result = (result * 16) + 1;
					break;
				}
				case '2':
				{
					result = (result * 16) + 2;
					break;
				}
				case '3':
				{
					result = (result * 16) + 3;
					break;
				}
				case '4':
				{
					result = (result * 16) + 4;
					break;
				}
				case '5':
				{
					result = (result * 16) + 5;
					break;
				}
				case '6':
				{
					result = (result * 16) + 6;
					break;
				}
				case '7':
				{
					result = (result * 16) + 7;
					break;
				}
				case '8':
				{
					result = (result * 16) + 8;
					break;
				}
				case '9':
				{
					result = (result * 16) + 9;
					break;
				}
				case 'a':
				case 'A':
				{
					result = (result * 16) + 10;
					break;
				}
				case 'b':
				case 'B':
				{
					result = (result * 16) + 11;
					break;
				}
				case 'c':
				case 'C':
				{
					result = (result * 16) + 12;
					break;
				}
				case 'd':
				case 'D':
				{
					result = (result * 16) + 13;
					break;
				}
				case 'e':
				case 'E':
				{
					result = (result * 16) + 14;
					break;
				}
				case 'f':
				case 'F':
				{
					result = (result * 16) + 15;
					break;
				}
				default:
				{
					throw new UnhandledEnumException(raw);
				}
			}
		}

		return result;
	}

	public static final long parseLong_base16(String raw)
	{
		return parseLong_base16(raw, 0);
	}

	public static final long parseLong_base16(String raw, int start)
	{
		long result = 0;

		while (start < raw.length())
		{
			switch (raw.charAt(start++))
			{
				case '0':
				{
					result = result * 16;
					break;
				}
				case '1':
				{
					result = (result * 16) + 1;
					break;
				}
				case '2':
				{
					result = (result * 16) + 2;
					break;
				}
				case '3':
				{
					result = (result * 16) + 3;
					break;
				}
				case '4':
				{
					result = (result * 16) + 4;
					break;
				}
				case '5':
				{
					result = (result * 16) + 5;
					break;
				}
				case '6':
				{
					result = (result * 16) + 6;
					break;
				}
				case '7':
				{
					result = (result * 16) + 7;
					break;
				}
				case '8':
				{
					result = (result * 16) + 8;
					break;
				}
				case '9':
				{
					result = (result * 16) + 9;
					break;
				}
				case 'a':
				case 'A':
				{
					result = (result * 16) + 10;
					break;
				}
				case 'b':
				case 'B':
				{
					result = (result * 16) + 11;
					break;
				}
				case 'c':
				case 'C':
				{
					result = (result * 16) + 12;
					break;
				}
				case 'd':
				case 'D':
				{
					result = (result * 16) + 13;
					break;
				}
				case 'e':
				case 'E':
				{
					result = (result * 16) + 14;
					break;
				}
				case 'f':
				case 'F':
				{
					result = (result * 16) + 15;
					break;
				}
				default:
				{
					throw new UnhandledEnumException(raw);
				}
			}
		}

		return result;
	}

	public static final long parseLong_base2(char[] raw, int start)
	{
		long result = 0;

		while (start < raw.length)
		{
			switch (raw[start++])
			{
				case '0':
				{
					result <<= 1;
					break;
				}
				case '1':
				{
					result = (result << 1) | 1;
					break;
				}
				default:
				{
					throw new UnhandledEnumException(raw);
				}
			}
		}

		return result;
	}

	public static final long parseLong_base2(String raw, int start)
	{
		long result = 0;

		while (start < raw.length())
		{
			switch (raw.charAt(start++))
			{
				case '0':
				{
					result <<= 1;
					break;
				}
				case '1':
				{
					result = (result << 1) | 1;
					break;
				}
				default:
				{
					throw new UnhandledEnumException(raw);
				}
			}
		}

		return result;
	}
}
