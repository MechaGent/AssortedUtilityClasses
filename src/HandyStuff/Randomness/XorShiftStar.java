package HandyStuff.Randomness;

public abstract class XorShiftStar
{
	private static final double NORM_53 = 1. / (1L << 53);
	private static final char[] alphaNumericPool = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	
	protected XorShiftStar()
	{
		
	}
	
	/**
	 * only use this sparingly, as it creates a new Random() seed every time
	 * @return
	 */
	public static XorShiftStar getNewInstance(Generators inType)
	{
		return XorShiftStarFactory.getNewInstance(inType);
	}

	public static XorShiftStar getNewInstance(Generators inType, long inSeed)
	{
		return XorShiftStarFactory.getNewInstance(inType, inSeed);
	}

	/**
	 * 
	 * @param inType
	 *            must be XorShiftStar1024
	 * @param seed
	 * @return
	 */
	public static XorShiftStar getNewInstance(Generators inType, long[] inSeed)
	{
		return XorShiftStarFactory.getNewInstance(inType, inSeed);
	}
	
	public abstract long nextLong();
	
	/**
	 * this one should only be called in constructors and the like
	 */
	abstract void revGenerator_init();
	
	abstract void revGenerator(int numRevs);
	
	public final double nextDouble()
	{
		return (this.nextLong() >>> 11) * NORM_53;
	}

	/**
	 * generates a double between 0 and inMax
	 * 
	 * @param inMax
	 * @return
	 */
	public final double nextDouble(double inMax)
	{
		return this.nextDouble() * inMax;
	}

	public final boolean flipCoin()
	{
		return this.flipCoin(0.5);
	}

	public final boolean flipCoin(double PositiveOdds)
	{
		return this.nextDouble() < PositiveOdds;
	}
	
	public final int nextInt()
	{
		return (int) Math.round(this.nextDouble(Integer.MAX_VALUE));
	}

	/**
	 * 
	 * @param inMax
	 * @return an int within the range [0, inMax)
	 */
	public final int nextInt(int inMax)
	{
		return (int) (this.nextDouble(inMax + 1) - 0.00001d);
	}
	
	/**
	 * 
	 * @param inMax
	 * @return a long within the range [0, inMax)
	 */
	public final long nextLong(long inMax)
	{
		return (long) (this.nextDouble(inMax + 1) - 0.00001d);
	}

	/**
	 * both params need to be positive
	 * 
	 * @param inMin
	 * @param inMax
	 * @return
	 */
	public final int nextInt(int inMin, int inMax)
	{
		final int delta = inMax - inMin;

		return this.nextInt(delta) + inMin;
	}
	
	/**
	 * will return a char from within one of the following ranges:
	 * <ul>
	 * <li>'0'-'9'</li>
	 * <li>'a'-'z'</li>
	 * <li>'A'-'Z'</li>
	 * </ul>
	 * @return
	 */
	public final char nextAlphaNumericCharacter()
	{
		return alphaNumericPool[this.nextInt(alphaNumericPool.length - 1)];
	}
	
	/*
	 * Table Operations
	 */
	
	public final <U> U pickElement(U[] in)
	{
		return in[this.nextInt(in.length - 1)];
	}

	/**
	 * Odds must be normed to sum to 1 first
	 * 
	 * @param Odds
	 * @return
	 */
	public final int getIndexGivenWeightedOdds_Normed(double[] Odds)
	{
		double Dice = this.nextDouble();
		int i = 0;
		boolean ZeroFlag = true;

		while ((i < Odds.length) && (Dice > Odds[i]))
		{
			Dice = Dice - Odds[i];
			ZeroFlag = Odds[i] == 0 && ZeroFlag;
			i++;
		}

		if (ZeroFlag)
		{
			return -1;
		}
		else
		{
			return i;
		}
	}

	/**
	 * Odds must be normed to sum to 100 first
	 * 
	 * @param Odds
	 * @return
	 */
	public final int getIndexGivenWeightedOdds_Normed(int[] Odds)
	{
		int Dice = this.nextInt(100);
		int i = 0;
		boolean isEmpty = true;

		while (i < Odds.length && Dice > Odds[i])
		{
			if (!isEmpty)
			{
				Dice -= Odds[i];
			}
			else
			{
				if (Odds[i] != 0)
				{
					isEmpty = false;
				}
			}

			i++;
		}

		return i;
	}

	public final int getIndexGivenWeightedOdds_unNormed(double[] Odds)
	{
		final double Total = sumArray(Odds);

		if (Total != 0)
		{
			double Dice = this.nextDouble(Total);
			int i = 0;

			while (i < Odds.length && Dice > Odds[i])
			{
				Dice -= Odds[i];
				i++;
			}

			return i;
		}
		else
		{
			return -1;
		}
	}

	public final int getIndexGivenWeightedOdds_unNormed(int[] Odds)
	{
		final int Total = sumArray(Odds);

		if (Total != 0)
		{
			int Dice = this.nextInt(Total);
			int i = 0;

			while (i < Odds.length && Dice > Odds[i])
			{
				Dice -= Odds[i];
				i++;
			}

			return i;
		}
		else
		{
			return -1;
		}
	}

	/*
	 * Static Methods
	 */

	private static final int sumArray(int[] in)
	{
		int result = 0;

		for (int curr : in)
		{
			result += curr;
		}

		return result;
	}

	private static final double sumArray(double[] in)
	{
		double result = 0;

		for (double curr : in)
		{
			result += curr;
		}

		return result;
	}
	
	/**
	 * 
	 * @param numGens
	 * @return an array of XorShiftStar1024s
	 */
	public abstract XorShiftStar[] bootstrapNewInstances(int numGens);
}
