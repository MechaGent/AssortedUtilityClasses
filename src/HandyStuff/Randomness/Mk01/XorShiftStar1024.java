package HandyStuff.Randomness.Mk01;

import java.util.Random;

class XorShiftStar1024 extends XorShiftStar
{
	private static final int shift0 = 31;
	private static final int shift1 = 11;
	private static final int shift2 = 30;

	private int index;

	private XorShiftStar1024(long[] inSeed)
	{
		super(inSeed);
		this.index = 0;
	}

	static XorShiftStar1024 getNewInstance()
	{
		final Random smallSeedInit = new Random();
		final long smallSeed = smallSeedInit.nextLong();
		return XorShiftStar1024.getNewInstance(smallSeed);
	}

	static XorShiftStar1024 getNewInstance(long inSeed)
	{
		final long[] genSeed = new long[Proper1024SeedLength];
		final XorShiftStar64 seeder = XorShiftStar64.getNewInstance(inSeed);

		for (int i = Proper1024SeedLength - 1; i != 0; i--)
		{
			genSeed[i] = seeder.nextLong();
		}

		return XorShiftStar1024.getNewInstance(genSeed);
	}

	static XorShiftStar1024 getNewInstance(long[] inSeed)
	{
		if (inSeed.length == Proper1024SeedLength)
		{
			final XorShiftStar1024 result = new XorShiftStar1024(inSeed);

			for (int i = InitialRevs_1024; i > 0; i--)
			{
				result.nextLong();
			}

			return result;
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public long nextLong()
	{
		final long seed0 = this.seed[this.index++];
		this.index &= 15;
		long seed1 = this.seed[this.index];
		seed1 ^= (seed1 << shift0);
		final long result = seed1 ^ seed0 ^ (seed1 >>> shift1) ^ (seed0 >>> shift2);

		this.seed[this.index] = result;
		return result * StarConst_1024;
	}
}