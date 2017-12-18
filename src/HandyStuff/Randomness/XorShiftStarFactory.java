package HandyStuff.Randomness;

import java.util.Random;

public class XorShiftStarFactory
{
	/**
	 * only use this sparingly, as it creates a new Random() seed every time
	 * @return
	 */
	public static XorShiftStar getNewInstance(Generators inType)
	{
		XorShiftStar result;

		switch (inType)
		{
			case XorShiftStar1024:
			{
				result = getInstance_1024bit();
				break;
			}
			case XorShiftStar64:
			{
				result = getInstance_64bit();
				break;
			}
			default:
			{
				throw new IllegalArgumentException();
			}
		}
		
		result.revGenerator_init();

		return result;
	}

	public static XorShiftStar getNewInstance(Generators inType, long inSeed)
	{
		XorShiftStar result;

		switch (inType)
		{
			case XorShiftStar1024:
			{
				result = getInstance_1024bit(inSeed);
				break;
			}
			case XorShiftStar64:
			{
				result = getInstance_64bit(inSeed);
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
			return getInstance_1024bit(inSeed);
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * only use this sparingly, as it creates a new Random() seed every time
	 * @return
	 */
	public static final XorShiftStar64 getInstance_64bit()
	{
		return getInstance_64bit((new Random()).nextLong());
	}
	
	public static final XorShiftStar64 getInstance_64bit(long seed)
	{
		return new XorShiftStar64(seed);
	}
	
	/**
	 * only use this sparingly, as it creates a new Random() seed every time
	 * @return
	 */
	public static final XorShiftStar1024 getInstance_1024bit()
	{
		return getInstance_1024bit((new Random()).nextLong());
	}
	
	public static final XorShiftStar1024 getInstance_1024bit(long shortSeed)
	{
		return new XorShiftStar1024(shortSeed);
	}
	
	public static final XorShiftStar1024 getInstance_1024bit(long[] seed)
	{
		return new XorShiftStar1024(seed);
	}
}
