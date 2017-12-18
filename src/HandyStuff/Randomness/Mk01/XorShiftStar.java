package HandyStuff.Randomness.Mk01;

import HandyStuff.Randomness.Generators;

public abstract class XorShiftStar
{
	static final long StarConst_64 = Long.parseUnsignedLong("2685821657736338717");
	static final long StarConst_1024 = 1181783497276652981L;
	private static final double NORM_53 = 1. / (1L << 53);
	static final int Proper1024SeedLength = 16;
	static final int InitialRevs_64 = 50;
	static final int InitialRevs_1024 = 500;

	// private static final long Gen_Min = Long.MIN_VALUE;
	// private static final long Gen_Max = Long.MAX_VALUE;

	protected final long[] seed;

	protected XorShiftStar(long[] inSeed)
	{
		this.seed = inSeed;
	}

	public long[] getSeed()
	{
		return this.seed;
	}

	public abstract long nextLong();

	public double nextDouble()
	{
		return (this.nextLong() >>> 11) * NORM_53;
	}

	/**
	 * generates a double between 0 and inMax
	 * 
	 * @param inMax
	 * @return
	 */
	public double nextDouble(double inMax)
	{
		return this.nextDouble() * inMax;
	}

	public boolean flipCoin()
	{
		return this.flipCoin(0.5);
	}

	public boolean flipCoin(double PositiveOdds)
	{
		return this.nextDouble() < PositiveOdds;
	}

	public int nextInt()
	{
		return (int) Math.round(this.nextDouble(Integer.MAX_VALUE));
	}

	public int nextInt(int inMax)
	{
		return (int) Math.round(this.nextDouble(inMax));
	}

	/**
	 * both params need to be positive
	 * 
	 * @param inMin
	 * @param inMax
	 * @return
	 */
	public int nextInt(int inMin, int inMax)
	{
		final int delta = inMax - inMin;

		return this.nextInt(delta) + inMin;
	}

	/*
	 * Table Operations
	 */

	/**
	 * Odds must be normed to sum to 1 first
	 * 
	 * @param Odds
	 * @return
	 */
	public int getIndexGivenWeightedOdds_Normed(double[] Odds)
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
	public int getIndexGivenWeightedOdds_Normed(int[] Odds)
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

	public int getIndexGivenWeightedOdds_unNormed(double[] Odds)
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

	public int getIndexGivenWeightedOdds_unNormed(int[] Odds)
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

	private static int sumArray(int[] in)
	{
		int result = 0;

		for (int curr : in)
		{
			result += curr;
		}

		return result;
	}

	private static double sumArray(double[] in)
	{
		double result = 0;

		for (double curr : in)
		{
			result += curr;
		}

		return result;
	}

	/**
	 * Deprecated, because I think multiple serial calls to this will produce coupled generators
	 * @param inType
	 * @return
	 */
	@Deprecated
	public XorShiftStar bootstrapNewInstance(Generators inType)
	{
		XorShiftStar result;

		switch (inType)
		{
			case XorShiftStar1024:
			{
				final long[] bootSeed = new long[Proper1024SeedLength];

				for (int i = 0; i < Proper1024SeedLength; i++)
				{
					bootSeed[i] = this.nextLong();
				}

				result = XorShiftStar1024.getNewInstance(bootSeed);
				break;
			}
			case XorShiftStar64:
			{
				final long bootSeed = this.nextLong();
				result = XorShiftStar64.getNewInstance(bootSeed);
				break;
			}
			default:
			{
				throw new IllegalArgumentException();
			}
		}

		return result;
	}

	/**
	 * 
	 * @param numGens
	 * @return an array of XorShiftStar1024s
	 */
	public XorShiftStar[] bootstrapNewInstances(int numGens)
	{
		final long[][] rawResult = new long[numGens][Proper1024SeedLength];

		for (int i = 0; i < Proper1024SeedLength; i++)
		{
			for (int genNum = 0; genNum < numGens; genNum++)
			{
				rawResult[genNum][i] = this.nextLong();
			}
		}
		
		final XorShiftStar[] result = new XorShiftStar[numGens];
		
		for(int i = 0; i < numGens; i++)
		{
			result[i] = XorShiftStar1024.getNewInstance(rawResult[i]);
		}
		
		return result;
	}

	public static XorShiftStar getNewInstance(Generators inType)
	{
		XorShiftStar result;

		switch (inType)
		{
			case XorShiftStar1024:
			{
				result = XorShiftStar1024.getNewInstance();
				break;
			}
			case XorShiftStar64:
			{
				result = XorShiftStar64.getNewInstance();
				break;
			}
			default:
			{
				throw new IllegalArgumentException();
			}
		}

		return result;
	}

	public static XorShiftStar getNewInstance(Generators inType, long inSeed)
	{
		XorShiftStar result;

		switch (inType)
		{
			case XorShiftStar1024:
			{
				result = XorShiftStar1024.getNewInstance(inSeed);
				break;
			}
			case XorShiftStar64:
			{
				result = XorShiftStar64.getNewInstance(inSeed);
				break;
			}
			default:
			{
				throw new IllegalArgumentException();
			}
		}

		return result;
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
		if (inType == Generators.XorShiftStar1024)
		{
			return XorShiftStar1024.getNewInstance(inSeed);
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}
}
