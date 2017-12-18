package HandyStuff.Randomness;

import java.util.Random;

public class XorShiftStar1024 extends XorShiftStar
{
	private static final long StarConst_1024 = 1181783497276652981L;
	private static final int Proper1024SeedLength = 16;
	private static final int InitialRevs_1024 = 500;
	private static final boolean shouldRevInitBooter = true;
	
	private static final int shift0 = 31;
	private static final int shift1 = 11;
	private static final int shift2 = 30;
	
	private final long[] seeds;
	private int index;
	
	XorShiftStar1024()
	{
		this((new Random()).nextLong());
	}
	
	XorShiftStar1024(long shortSeed)
	{
		super();
		
		final long[] tempSeed = new long[Proper1024SeedLength];
		final XorShiftStar64 booter = new XorShiftStar64(shortSeed);
		
		if(shouldRevInitBooter)
		{
			booter.revGenerator_init();
		}
		
		for(int i = 0; i < Proper1024SeedLength; i++)
		{
			tempSeed[i] = booter.nextLong();
		}
		
		this.seeds = tempSeed;
		this.index = 0;
	}
	
	/**
	 * presumes {@code inSeeds} has been completely initialized properly
	 * @param inSeeds
	 */
	XorShiftStar1024(long[] inSeeds)
	{
		super();
		this.seeds = inSeeds;
		this.index = 0;
	}
	
	/**
	 * 
	 * @param numGens
	 * @return an array of XorShiftStar1024s
	 */
	@Override
	public XorShiftStar1024[] bootstrapNewInstances(int numGens)
	{
		final long[][] rawResult = new long[numGens][Proper1024SeedLength];

		for (int i = 0; i < Proper1024SeedLength; i++)
		{
			for (int genNum = 0; genNum < numGens; genNum++)
			{
				rawResult[genNum][i] = this.nextLong();
			}
		}
		
		final XorShiftStar1024[] result = new XorShiftStar1024[numGens];
		
		for(int i = 0; i < numGens; i++)
		{
			result[i] = new XorShiftStar1024(rawResult[i]);
			result[i].revGenerator_init();
		}
		
		return result;
	}
	
	public final long[] getSeed()
	{
		return this.seeds;
	}

	@Override
	public long nextLong()
	{
		final long seed0 = this.seeds[this.index++];
		this.index &= 15;
		final long seed1 = this.seeds[this.index];
		
		final long stage0 = seed1 ^ (seed1 << shift0);
		final long result = stage0 ^ seed0 ^ (stage0 >>> shift1) ^ (seed0 >>> shift2);

		this.seeds[this.index] = result;
		return result * StarConst_1024;
	}
	
	@Override
	void revGenerator_init()
	{
		this.revGenerator(InitialRevs_1024 + 1);
	}

	@Override
	void revGenerator(int numRevs)
	{
		final long[] tempSeeds = this.seeds;
		int tempIndex = this.index;
		
		for(; numRevs > 0; numRevs--)
		{
			final long seed0 = tempSeeds[tempIndex++];
			tempIndex &= 15;
			
			final long stage0 = tempSeeds[tempIndex] ^ (tempSeeds[tempIndex] << shift0);
			tempSeeds[tempIndex] = stage0 ^ seed0 ^ (stage0 >>> shift1) ^ (seed0 >>> shift2);
		}
		
		this.index = tempIndex;
	}
}
