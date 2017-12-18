package HandyStuff.Randomness.Mk01;

import java.util.Random;

class XorShiftStar64 extends XorShiftStar
{
	private static final int shift0 = 12;
	private static final int shift1 = 25;
	private static final int shift2 = 27;

	private XorShiftStar64(long inSeed)
	{
		super(new long[] {
							inSeed });
	}

	static XorShiftStar64 getNewInstance()
	{
		final Random seedInit = new Random();

		return XorShiftStar64.getNewInstance(seedInit.nextLong());
	}

	static XorShiftStar64 getNewInstance(long inSeed)
	{
		final XorShiftStar64 result = new XorShiftStar64(inSeed);

		for (int i = InitialRevs_64; i > 0; i--)
		{
			result.nextLong();
		}

		return result;
	}
	
	@Override
	public long nextLong()
	{
		this.seed[0] ^= (this.seed[0] >>> shift0);
		this.seed[0] ^= (this.seed[0] << shift1);
		this.seed[0] ^= (this.seed[0] >>> shift2);

		return this.seed[0] * StarConst_64;
	}
}