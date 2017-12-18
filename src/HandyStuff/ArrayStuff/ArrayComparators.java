package HandyStuff.ArrayStuff;

public final class ArrayComparators
{
	public static final boolean areEquivalent(char[] var0, char[] var1)
	{
		if (var0 == var1)
		{
			return true;
		}

		if (var0.length != var1.length)
		{
			return false;
		}

		final int halfLength = var0.length / 2;
		final int lastIndex = var0.length - 1;

		if ((var0.length % 2) == 1) // it's odd
		{
			if (var0[halfLength] != var1[halfLength])
			{
				return false;
			}
		}

		for (int i = 0; i < halfLength; i++)
		{
			final int reverse = lastIndex - i;

			if ((var0[i] != var1[i]) || (var0[reverse] != var1[reverse]))
			{
				return false;
			}
		}

		return true;
	}

	public static final boolean areEquivalent(char[] var0, char[] var1, int startingIndex)
	{
		if (var0 == var1)
		{
			return true;
		}

		if (var0.length != var1.length)
		{
			return false;
		}

		final int offsetLength = var0.length - startingIndex;
		final int lastIndex = var0.length - 1;

		if ((offsetLength % 2) == 1) // it's odd
		{
			if (var0[lastIndex] != var1[lastIndex])
			{
				return false;
			}
		}

		for (int i = 0; i < offsetLength; i += 2)
		{
			final int lesser = startingIndex + i;
			final int greater = lastIndex - i;

			if ((var0[lesser] != var1[lesser]) || (var0[greater] != var1[greater]))
			{
				return false;
			}
		}

		return true;
	}

	public static final boolean areEquivalent(String var0, char[] var1, int startingIndex)
	{
		if (var0.length() != var1.length)
		{
			return false;
		}

		final int offsetLength = var0.length() - startingIndex;
		final int lastIndex = var0.length() - 1;

		if ((offsetLength % 2) == 1) // it's odd
		{
			if (var0.charAt(lastIndex) != var1[lastIndex])
			{
				return false;
			}
		}

		for (int i = 0; i < offsetLength; i += 2)
		{
			final int lesser = startingIndex + i;
			final int greater = lastIndex - i;

			if ((var0.charAt(lesser) != var1[lesser]) || (var0.charAt(greater) != var1[greater]))
			{
				return false;
			}
		}

		return true;
	}

	public static final boolean areEquivalent(char[] var0, int startingIndex0, char[] var1, int startingIndex1)
	{
		if (startingIndex0 == startingIndex1)
		{
			return areEquivalent(var0, var1, startingIndex0);
		}

		final int length = var0.length - startingIndex0;

		if (length != (var1.length - startingIndex1))
		{
			return false;
		}

		final int halfLength = length / 2;

		if ((length % 2) == 1) // it's odd, so check the middle char
		{
			final int midOffset = halfLength + 1;

			if (var0[startingIndex0 + midOffset] != var1[startingIndex1 + midOffset])
			{
				return false;
			}
		}

		final int lastIndex0 = var0.length - 1;
		final int lastIndex1 = var1.length - 1;

		for (int i0 = 0, i1 = 0; i0 < halfLength; i0 += 1, i1 += 1)
		{
			if ((var0[startingIndex0 + i0] != var1[startingIndex1 + i1]) || (var0[lastIndex0 - i0] != var1[lastIndex1 - i1]))
			{
				return false;
			}
		}

		return true;
	}

	public static final boolean startsWith(char[] target, char[] prefix)
	{
		if (target.length < prefix.length)
		{
			throw new IndexOutOfBoundsException("target length: " + target.length + "\r\nprefix length: " + prefix.length);
		}

		final int halfLength = prefix.length / 2;

		if ((prefix.length % 2) == 1) // it's odd, so check the middle char
		{
			if (target[halfLength] != prefix[halfLength])
			{
				return false;
			}
		}
		
		final int lastIndex = prefix.length - 1;
		
		for(int i = 0; i < halfLength; i++)
		{
			final int greater = lastIndex - i;
			
			if((target[i] != prefix[i]) || (target[greater] != prefix[greater]))
			{
				return false;
			}
		}
		
		return true;
	}

	public static final int compare(char[] var0, char[] var1)
	{
		if (var0 != var1)
		{
			if (var0.length == var1.length)
			{
				for (int i = 0; i < var0.length; i++)
				{
					final int delta = var0[i] - var1[i];

					if (delta != 0)
					{
						return delta;
					}
				}
			}
			else if (var0.length < var1.length)
			{
				for (int i = 0; i < var0.length; i++)
				{
					final int delta = var0[i] - var1[i];

					if (delta != 0)
					{
						return delta;
					}
				}

				return -1;
			}
			else
			{
				for (int i = 0; i < var1.length; i++)
				{
					final int delta = var0[i] - var1[i];

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

	/**
	 * orders alphabetically, but orders Uppercase letters directly after their lower case equivalents
	 * 
	 * @param var0
	 * @param var1
	 * @return
	 */
	public static final int compareAlphabetically(char[] var0, char[] var1)
	{
		final int result;

		if (var0 == var1)
		{
			result = 0;
		}
		else
		{
			if (var0.length == var1.length)
			{
				result = compareAlphabetically_Equal(var0, var1);
			}
			else if (var0.length > var1.length)
			{
				result = compareAlphabetically_GreaterThan(var0, var1);
			}
			else
			{
				result = compareAlphabetically_LessThan(var0, var1);
			}
		}

		return result;
	}

	private static final int compareAlphabetically_LessThan(char[] var0, char[] var1)
	{
		final int result;

		loop:
		{
			for (int i = 0; i < var0.length; i++)
			{
				if (var0[i] != var1[i])
				{
					final char lower0 = Character.toLowerCase(var0[i]);
					final char lower1 = Character.toLowerCase(var1[i]);

					if (lower0 != lower1)
					{
						result = lower0 - lower1;
					}
					else
					{
						// lower0 == lower1

						if (lower0 == var0[i])
						{
							// var0[i] is lowercase

							if (lower1 != var1[i])
							{
								// var1[i] is uppercase
								i += 1;

								for (; i < var0.length; i++)
								{
									final char locLower0 = Character.toLowerCase(var0[i]);
									final char locLower1 = Character.toLowerCase(var1[i]);

									if (locLower0 != locLower1)
									{
										result = locLower0 - locLower1;
										break loop;
									}
								}

								result = -1;
							}
							else
							{
								// both are lowercase and equal, which is impossible
								result = -69;
							}
						}
						else
						{
							// var0[i] is uppercase

							if (lower1 == var1[i])
							{
								// var1[i] is lowercase

								i += 1;

								for (; i < var0.length; i++)
								{
									final char locLower0 = Character.toLowerCase(var0[i]);
									final char locLower1 = Character.toLowerCase(var1[i]);

									if (locLower0 != locLower1)
									{
										result = locLower0 - locLower1;
										break loop;
									}
								}

								result = 1;
							}
							else
							{
								// both are uppercase and equal, which is impossible
								result = -69;
							}
						}
					}

					break loop;
				}
			}

			result = -1;
		}

		return result;
	}

	private static final int compareAlphabetically_GreaterThan(char[] var0, char[] var1)
	{
		final int result;

		loop:
		{
			for (int i = 0; i < var1.length; i++)
			{
				if (var0[i] != var1[i])
				{
					final char lower0 = Character.toLowerCase(var0[i]);
					final char lower1 = Character.toLowerCase(var1[i]);

					if (lower0 != lower1)
					{
						result = lower0 - lower1;
					}
					else
					{
						// lower0 == lower1

						if (lower0 == var0[i])
						{
							// var0[i] is lowercase

							if (lower1 != var1[i])
							{
								// var1[i] is uppercase
								i += 1;

								for (; i < var1.length; i++)
								{
									final char locLower0 = Character.toLowerCase(var0[i]);
									final char locLower1 = Character.toLowerCase(var1[i]);

									if (locLower0 != locLower1)
									{
										result = locLower0 - locLower1;
										break loop;
									}
								}

								result = 1;
							}
							else
							{
								// both are lowercase and equal, which is impossible
								result = -69;
							}
						}
						else
						{
							// var0[i] is uppercase

							if (lower1 == var1[i])
							{
								// var1[i] is lowercase

								i += 1;

								for (; i < var1.length; i++)
								{
									final char locLower0 = Character.toLowerCase(var0[i]);
									final char locLower1 = Character.toLowerCase(var1[i]);

									if (locLower0 != locLower1)
									{
										result = locLower0 - locLower1;
										break loop;
									}
								}

								result = -1;
							}
							else
							{
								// both are uppercase and equal, which is impossible
								result = -69;
							}
						}
					}

					break loop;
				}
			}

			result = 1;
		}

		return result;
	}

	private static final int compareAlphabetically_Equal(char[] var0, char[] var1)
	{
		final int result;

		loop:
		{
			for (int i = 0; i < var0.length; i++)
			{
				if (var0[i] != var1[i])
				{
					final char lower0 = Character.toLowerCase(var0[i]);
					final char lower1 = Character.toLowerCase(var1[i]);

					if (lower0 != lower1)
					{
						result = lower0 - lower1;
					}
					else
					{
						// lower0 == lower1

						if (lower0 == var0[i])
						{
							// var0[i] is lowercase

							if (lower1 != var1[i])
							{
								// var1[i] is uppercase
								i += 1;

								for (; i < var0.length; i++)
								{
									final char locLower0 = Character.toLowerCase(var0[i]);
									final char locLower1 = Character.toLowerCase(var1[i]);

									if (locLower0 != locLower1)
									{
										result = locLower0 - locLower1;
										break loop;
									}
								}

								result = -1;
							}
							else
							{
								result = -69; // this should be impossible to reach
							}

							break loop;
						}
						else
						{
							// var0[i] is uppercase

							if (lower1 == var1[i])
							{
								// var1[i] is lowercase

								i += 1;

								for (; i < var0.length; i++)
								{
									final char locLower0 = Character.toLowerCase(var0[i]);
									final char locLower1 = Character.toLowerCase(var1[i]);

									if (locLower0 != locLower1)
									{
										result = locLower0 - locLower1;
										break loop;
									}
								}

								result = 1;
							}
							else
							{
								result = -69; // this should be impossible to reach
							}

							break loop;
						}
					}
					break loop;
				}
			}

			result = 0;
		}

		return result;
	}

	public static final boolean areEquivalent(byte[] var0, byte[] var1)
	{
		if (var0 == var1)
		{
			return true;
		}

		if (var0.length != var1.length)
		{
			return false;
		}

		final int halfLength = var0.length / 2;
		final int lastIndex = var0.length - 1;

		if ((var0.length % 2) == 1) // it's odd
		{
			if (var0[halfLength] != var1[halfLength])
			{
				return false;
			}
		}

		for (int i = 0; i < halfLength; i++)
		{
			final int reverse = lastIndex - i;

			if ((var0[i] != var1[i]) || (var0[reverse] != var1[reverse]))
			{
				return false;
			}
		}

		return true;
	}

	public static final boolean areEquivalent(byte[] var0, byte[] var1, int startingIndex)
	{
		if (var0 == var1)
		{
			return true;
		}

		if (var0.length != var1.length)
		{
			return false;
		}

		final int offsetLength = var0.length - startingIndex;
		final int lastIndex = var0.length - 1;

		if ((offsetLength % 2) == 1) // it's odd
		{
			if (var0[lastIndex] != var1[lastIndex])
			{
				return false;
			}
		}

		for (int i = 0; i < offsetLength; i += 1)
		{
			final int lesser = startingIndex + i;
			final int greater = lastIndex - i;

			if ((var0[lesser] != var1[lesser]) || (var0[greater] != var1[greater]))
			{
				return false;
			}
		}

		return true;
	}
	
	public static final boolean startsWith(byte[] target, byte[] prefix)
	{
		if (target.length < prefix.length)
		{
			throw new IndexOutOfBoundsException("target length: " + target.length + "\r\nprefix length: " + prefix.length);
		}

		final int halfLength = prefix.length / 2;

		if ((prefix.length % 2) == 1) // it's odd, so check the middle byte
		{
			if (target[halfLength] != prefix[halfLength])
			{
				return false;
			}
		}
		
		final int lastIndex = prefix.length - 1;
		
		for(int i = 0; i < halfLength; i++)
		{
			final int greater = lastIndex - i;
			
			if((target[i] != prefix[i]) || (target[greater] != prefix[greater]))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static final boolean startsWith_afterOffset(byte[] target, byte[] prefix, int offset)
	{
		if (target.length < prefix.length)
		{
			throw new IndexOutOfBoundsException("target length: " + target.length + "\r\nprefix length: " + prefix.length);
		}
		
		final int prefixLength = prefix.length - offset;
		final int halfLength = prefixLength / 2;
		
		if((prefixLength % 2) == 1) // it's odd, so check the middle byte
		{
			if (target[halfLength] != prefix[halfLength])
			{
				return false;
			}
		}
		
		final int lastIndex = prefixLength - 1;
		
		for(int i = 0; i < halfLength; i++)
		{
			final int lesser = offset + i;
			final int greater = offset + (lastIndex - i);
			
			if((target[lesser] != prefix[lesser]) || (target[greater] != prefix[greater]))
			{
				return false;
			}
		}
		
		return true;
	}

	public static final int compare(byte[] var0, byte[] var1)
	{
		if (var0 != var1)
		{
			if (var0.length == var1.length)
			{
				for (int i = 0; i < var0.length; i++)
				{
					final int delta = var0[i] - var1[i];

					if (delta != 0)
					{
						return delta;
					}
				}
			}
			else if (var0.length < var1.length)
			{
				for (int i = 0; i < var0.length; i++)
				{
					final int delta = var0[i] - var1[i];

					if (delta != 0)
					{
						return delta;
					}
				}

				return -1;
			}
			else
			{
				for (int i = 0; i < var1.length; i++)
				{
					final int delta = var0[i] - var1[i];

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

	public static final boolean areEquivalent(short[] var0, short[] var1)
	{
		if (var0 == var1)
		{
			return true;
		}

		if (var0.length != var1.length)
		{
			return false;
		}

		final int halfLength = var0.length / 2;
		final int lastIndex = var0.length - 1;

		if ((var0.length % 2) == 1) // it's odd
		{
			if (var0[halfLength] != var1[halfLength])
			{
				return false;
			}
		}

		for (int i = 0; i < halfLength; i++)
		{
			final int reverse = lastIndex - i;

			if ((var0[i] != var1[i]) || (var0[reverse] != var1[reverse]))
			{
				return false;
			}
		}

		return true;
	}

	public static final boolean areEquivalent(short[] var0, short[] var1, int startingIndex)
	{
		if (var0 == var1)
		{
			return true;
		}

		if (var0.length != var1.length)
		{
			return false;
		}

		final int offsetLength = var0.length - startingIndex;
		final int lastIndex = var0.length - 1;

		if ((offsetLength % 2) == 1) // it's odd
		{
			if (var0[lastIndex] != var1[lastIndex])
			{
				return false;
			}
		}

		for (int i = 0; i < offsetLength; i += 2)
		{
			final int lesser = startingIndex + i;
			final int greater = lastIndex - i;

			if ((var0[lesser] != var1[lesser]) || (var0[greater] != var1[greater]))
			{
				return false;
			}
		}

		return true;
	}

	public static final int compare(short[] var0, short[] var1)
	{
		if (var0 != var1)
		{
			if (var0.length == var1.length)
			{
				for (int i = 0; i < var0.length; i++)
				{
					final int delta = var0[i] - var1[i];

					if (delta != 0)
					{
						return delta;
					}
				}
			}
			else if (var0.length < var1.length)
			{
				for (int i = 0; i < var0.length; i++)
				{
					final int delta = var0[i] - var1[i];

					if (delta != 0)
					{
						return delta;
					}
				}

				return -1;
			}
			else
			{
				for (int i = 0; i < var1.length; i++)
				{
					final int delta = var0[i] - var1[i];

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

	public static final boolean areEquivalent(int[] var0, int[] var1)
	{
		if (var0 == var1)
		{
			return true;
		}

		if (var0.length != var1.length)
		{
			return false;
		}

		final int halfLength = var0.length / 2;
		final int lastIndex = var0.length - 1;

		if ((var0.length % 2) == 1) // it's odd
		{
			if (var0[halfLength] != var1[halfLength])
			{
				return false;
			}
		}

		for (int i = 0; i < halfLength; i++)
		{
			final int reverse = lastIndex - i;

			if ((var0[i] != var1[i]) || (var0[reverse] != var1[reverse]))
			{
				return false;
			}
		}

		return true;
	}

	public static final boolean areEquivalent(int[] var0, int[] var1, int startingIndex)
	{
		if (var0 == var1)
		{
			return true;
		}

		if (var0.length != var1.length)
		{
			return false;
		}

		final int offsetLength = var0.length - startingIndex;
		final int lastIndex = var0.length - 1;

		if ((offsetLength % 2) == 1) // it's odd
		{
			if (var0[lastIndex] != var1[lastIndex])
			{
				return false;
			}
		}

		for (int i = 0; i < offsetLength; i += 2)
		{
			final int lesser = startingIndex + i;
			final int greater = lastIndex - i;

			if ((var0[lesser] != var1[lesser]) || (var0[greater] != var1[greater]))
			{
				return false;
			}
		}

		return true;
	}

	public static final int compare(int[] var0, int[] var1)
	{
		if (var0 != var1)
		{
			if (var0.length == var1.length)
			{
				for (int i = 0; i < var0.length; i++)
				{
					final int delta = var0[i] - var1[i];

					if (delta != 0)
					{
						return delta;
					}
				}
			}
			else if (var0.length < var1.length)
			{
				for (int i = 0; i < var0.length; i++)
				{
					final int delta = var0[i] - var1[i];

					if (delta != 0)
					{
						return delta;
					}
				}

				return -1;
			}
			else
			{
				for (int i = 0; i < var1.length; i++)
				{
					final int delta = var0[i] - var1[i];

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

	public static final boolean areEquivalent(float[] var0, float[] var1)
	{
		if (var0 == var1)
		{
			return true;
		}

		if (var0.length != var1.length)
		{
			return false;
		}

		final int halfLength = var0.length / 2;
		final int lastIndex = var0.length - 1;

		if ((var0.length % 2) == 1) // it's odd
		{
			if (var0[halfLength] != var1[halfLength])
			{
				return false;
			}
		}

		for (int i = 0; i < halfLength; i++)
		{
			final int reverse = lastIndex - i;

			if ((var0[i] != var1[i]) || (var0[reverse] != var1[reverse]))
			{
				return false;
			}
		}

		return true;
	}

	public static final boolean areEquivalent(float[] var0, float[] var1, int startingIndex)
	{
		if (var0 == var1)
		{
			return true;
		}

		if (var0.length != var1.length)
		{
			return false;
		}

		final int offsetLength = var0.length - startingIndex;
		final int lastIndex = var0.length - 1;

		if ((offsetLength % 2) == 1) // it's odd
		{
			if (var0[lastIndex] != var1[lastIndex])
			{
				return false;
			}
		}

		for (int i = 0; i < offsetLength; i += 2)
		{
			final int lesser = startingIndex + i;
			final int greater = lastIndex - i;

			if ((var0[lesser] != var1[lesser]) || (var0[greater] != var1[greater]))
			{
				return false;
			}
		}

		return true;
	}

	public static final int compare(float[] var0, float[] var1)
	{
		if (var0 != var1)
		{
			if (var0.length == var1.length)
			{
				for (int i = 0; i < var0.length; i++)
				{
					final float delta = var0[i] - var1[i];

					if (delta < 0)
					{
						return -1;
					}
					else if (delta > 0)
					{
						return 1;
					}
				}
			}
			else if (var0.length < var1.length)
			{
				for (int i = 0; i < var0.length; i++)
				{
					final float delta = var0[i] - var1[i];

					if (delta < 0)
					{
						return -1;
					}
					else if (delta > 0)
					{
						return 1;
					}
				}

				return -1;
			}
			else
			{
				for (int i = 0; i < var1.length; i++)
				{
					final float delta = var0[i] - var1[i];

					if (delta < 0)
					{
						return -1;
					}
					else if (delta > 0)
					{
						return 1;
					}
				}

				return 1;
			}
		}

		return 0;
	}

	public static final boolean areEquivalent(long[] var0, long[] var1)
	{
		if (var0 == var1)
		{
			return true;
		}

		if (var0.length != var1.length)
		{
			return false;
		}

		final int halfLength = var0.length / 2;
		final int lastIndex = var0.length - 1;

		if ((var0.length % 2) == 1) // it's odd
		{
			if (var0[halfLength] != var1[halfLength])
			{
				return false;
			}
		}

		for (int i = 0; i < halfLength; i++)
		{
			final int reverse = lastIndex - i;

			if ((var0[i] != var1[i]) || (var0[reverse] != var1[reverse]))
			{
				return false;
			}
		}

		return true;
	}

	public static final boolean areEquivalent(long[] var0, long[] var1, int startingIndex)
	{
		if (var0 == var1)
		{
			return true;
		}

		if (var0.length != var1.length)
		{
			return false;
		}

		final int offsetLength = var0.length - startingIndex;
		final int lastIndex = var0.length - 1;

		if ((offsetLength % 2) == 1) // it's odd
		{
			if (var0[lastIndex] != var1[lastIndex])
			{
				return false;
			}
		}

		for (int i = 0; i < offsetLength; i += 2)
		{
			final int lesser = startingIndex + i;
			final int greater = lastIndex - i;

			if ((var0[lesser] != var1[lesser]) || (var0[greater] != var1[greater]))
			{
				return false;
			}
		}

		return true;
	}

	public static final long compare(long[] var0, long[] var1)
	{
		if (var0 != var1)
		{
			if (var0.length == var1.length)
			{
				for (int i = 0; i < var0.length; i++)
				{
					final long delta = var0[i] - var1[i];

					if (delta != 0)
					{
						return delta;
					}
				}
			}
			else if (var0.length < var1.length)
			{
				for (int i = 0; i < var0.length; i++)
				{
					final long delta = var0[i] - var1[i];

					if (delta != 0)
					{
						return delta;
					}
				}

				return -1;
			}
			else
			{
				for (int i = 0; i < var1.length; i++)
				{
					final long delta = var0[i] - var1[i];

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

	public static final boolean areEquivalent(double[] var0, double[] var1)
	{
		if (var0 == var1)
		{
			return true;
		}

		if (var0.length != var1.length)
		{
			return false;
		}

		final int halfLength = var0.length / 2;
		final int lastIndex = var0.length - 1;

		if ((var0.length % 2) == 1) // it's odd
		{
			if (var0[halfLength] != var1[halfLength])
			{
				return false;
			}
		}

		for (int i = 0; i < halfLength; i++)
		{
			final int reverse = lastIndex - i;

			if ((var0[i] != var1[i]) || (var0[reverse] != var1[reverse]))
			{
				return false;
			}
		}

		return true;
	}

	public static final boolean areEquivalent(double[] var0, double[] var1, int startingIndex)
	{
		if (var0 == var1)
		{
			return true;
		}

		if (var0.length != var1.length)
		{
			return false;
		}

		final int offsetLength = var0.length - startingIndex;
		final int lastIndex = var0.length - 1;

		if ((offsetLength % 2) == 1) // it's odd
		{
			if (var0[lastIndex] != var1[lastIndex])
			{
				return false;
			}
		}

		for (int i = 0; i < offsetLength; i += 2)
		{
			final int lesser = startingIndex + i;
			final int greater = lastIndex - i;

			if ((var0[lesser] != var1[lesser]) || (var0[greater] != var1[greater]))
			{
				return false;
			}
		}

		return true;
	}

	public static final int compare(double[] var0, double[] var1)
	{
		if (var0 != var1)
		{
			if (var0.length == var1.length)
			{
				for (int i = 0; i < var0.length; i++)
				{
					final double delta = var0[i] - var1[i];

					if (delta < 0)
					{
						return -1;
					}
					else if (delta > 0)
					{
						return 1;
					}
				}
			}
			else if (var0.length < var1.length)
			{
				for (int i = 0; i < var0.length; i++)
				{
					final double delta = var0[i] - var1[i];

					if (delta < 0)
					{
						return -1;
					}
					else if (delta > 0)
					{
						return 1;
					}
				}

				return -1;
			}
			else
			{
				for (int i = 0; i < var1.length; i++)
				{
					final double delta = var0[i] - var1[i];

					if (delta < 0)
					{
						return -1;
					}
					else if (delta > 0)
					{
						return 1;
					}
				}

				return 1;
			}
		}

		return 0;
	}
}
