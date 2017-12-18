package HandyStuff.Randomness;

public class XorShiftStar64 extends XorShiftStar
{
	private static final long StarConst_64 = Long.parseUnsignedLong("2685821657736338717");
	private static final int shift0 = 12;
	private static final int shift1 = 25;
	private static final int shift2 = 27;
	
	private static final int InitialRevs_64 = 50;
	
	private long seed;
	
	XorShiftStar64(long inSeed)
	{
		super();
		this.seed = inSeed;
	}
	
	public final long getSeed()
	{
		return this.seed;
	}
	
	@Override
	public XorShiftStar[] bootstrapNewInstances(int numGens)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public final long nextLong()
	{
		final long stage0 = this.seed ^ (this.seed >>> shift0);
		final long stage1 = stage0 ^ (stage0 << shift1);
		final long stage2 = stage1 ^ (stage1 >>> shift2);
		
		this.seed = stage2;

		return stage2 * StarConst_64;
	}
	
	@Override
	void revGenerator_init()
	{
		this.revGenerator(InitialRevs_64 + 1);
	}

	@Override
	final void revGenerator(int numRevs)
	{
		long tempSeed = this.seed;
		
		for(; numRevs > 0; numRevs--)
		{
			final long stage0 = tempSeed ^ (tempSeed >>> shift0);
			final long stage1 = stage0 ^ (stage0 << shift1);
			tempSeed = stage1 ^ (stage1 >>> shift2);
		}
		
		this.seed = tempSeed;
	}
}
