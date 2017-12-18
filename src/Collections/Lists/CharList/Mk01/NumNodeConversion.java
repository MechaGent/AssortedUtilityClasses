package Collections.Lists.CharList.Mk01;

public class NumNodeConversion
{
	private static final int P01 = 10;
	private static final int P02 = 10 * P01;
	private static final int P03 = 10 * P02;
	private static final int P04 = 10 * P03;
	private static final int P05 = 10 * P04;
	private static final int P06 = 10 * P05;
	private static final int P07 = 10 * P06;
	private static final int P08 = 10 * P07;
	private static final int P09 = 10 * P08;
	private static final int P10 = 10 * P09;
	private static final int P11 = 10 * P10;
	private static final long P12 = 10L * P11;
	private static final char[] digitCharsRef = initDigitCharsRef();

	/*
	 * Binary Stuff
	 */

	public static final String toBinString(int raw)
	{
		final char[] result = new char[32];
		int mask = 0x01;

		for (int i = 31; i >= 0; i--)
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

		return new String(result);
	}

	public static final String toBinString(long raw)
	{
		final char[] result = new char[64];
		long mask = 0x01;

		for (int i = 63; i >= 0; i--)
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

		return new String(result);
	}

	/*
	 * Decimal Stuff
	 */

	/**
	 * all credit to Andrei Alexandrescu: {@link https://www.facebook.com/notes/facebook-engineering/three-optimization-tips-for-c/10151361643253920}
	 * 
	 * @param raw
	 *            needs to be unsigned
	 * @return
	 */
	public static final String toDecString(int raw)
	{
		final int length = getNumDigits(raw);
		final char[] result = new char[length];
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

		/*
		if(result[0] == 0)
		{
			System.err.println("Bad String length detected for input: " + raw + ". Adjusting.");
			int white = 1;
			
			while(result[white] == 0)
			{
				white++;
			}
			
			final char[] redo = new char[result.length - white + 1];
			
			for(int i = 0; i < redo.length; i++)
			{
				redo[i] = result[i + white - 1];
			}
			
			return new String(redo);
		}
		*/
		
		return new String(result);
	}

	/**
	 * all credit to Andrei Alexandrescu: {@link https://www.facebook.com/notes/facebook-engineering/three-optimization-tips-for-c/10151361643253920}
	 * 
	 * @param raw
	 *            needs to be unsigned
	 * @return
	 */
	public static final String toDecString(long raw)
	{
		final int length = getNumDigits(raw);
		final char[] result = new char[length];
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

		return new String(result);
	}
	
	public static final String toDecString(double raw)
	{
		return Double.toString(raw);
	}

	/**
	 * all credit to Andrei Alexandrescu: {@link https://www.facebook.com/notes/facebook-engineering/three-optimization-tips-for-c/10151361643253920}
	 * 
	 * @param raw
	 *            needs to be unsigned
	 * @return
	 */
	private static final int getNumDigits(int raw)
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
			return 12 + getNumDigits(raw / P12);
		}
	}

	/**
	 * all credit to Andrei Alexandrescu: {@link https://www.facebook.com/notes/facebook-engineering/three-optimization-tips-for-c/10151361643253920}
	 * 
	 * @param raw
	 *            needs to be unsigned
	 * @return
	 */
	private static final int getNumDigits(long raw)
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
			return 12 + getNumDigits(raw / P12);
		}
	}

	/**
	 * all credit to Andrei Alexandrescu: {@link https://www.facebook.com/notes/facebook-engineering/three-optimization-tips-for-c/10151361643253920}
	 * 
	 * @param raw
	 * @return
	 */
	private static final char[] initDigitCharsRef()
	{
		final String temp = "0001020304050607080910111213141516171819" + "2021222324252627282930313233343536373839" + "4041424344454647484950515253545556575859" + "6061626364656667686970717273747576777879" + "8081828384858687888990919293949596979899";

		return temp.toCharArray();
	}

	/*
	 * Hex Stuff
	 */

	public static final String toHexString(int raw)
	{
		final char[] result = new char[4];
		
		int mask = 0x0f;
		
		for(int i = 3; i >= 0; i--)
		{
			final int nibble = raw & mask;
			result[i] = getHexChar_inCaps(nibble);
			mask = mask >>> 4;
		}
		
		return new String(result);
	}
	
	public static final String toHexString(long raw)
	{
		final char[] result = new char[8];
		
		int mask = 0x0f;
		
		for(int i = 7; i >= 0; i--)
		{
			final int nibble = (int) (raw & mask);
			result[i] = getHexChar_inCaps(nibble);
			mask = mask >>> 4;
		}
		
		return new String(result);
	}
	
	public static char getHexChar_inCaps(int Nibble)
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
				throw new ArrayIndexOutOfBoundsException();
			}
		}

		return result;
	}

	public static char getHexChar_noCaps(int Nibble)
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
}
