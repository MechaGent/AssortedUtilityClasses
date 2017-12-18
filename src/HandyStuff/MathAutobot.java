package HandyStuff;

public class MathAutobot
{
	public static final float max(float var1, float var2, float var3)
	{
		return Math.max(Math.max(var1, var2), var3);
	}

	public static final int max(int var1, int var2, int var3, int var4)
	{
		return Math.max(Math.max(var1, var2), Math.max(var3, var4));
	}

	public static final int roundUpToNearestPowerOf2(int value)
	{
		if (value == 0)
		{
			return 1;
		}
		else if (value < 0)
		{
			return -roundDownToNearestPowerOf2(-value);
		}
		else
		{
			value -= 1;

			value |= value >>> 1;
			value |= value >>> 2;
			value |= value >>> 4;
			value |= value >>> 8;
			value |= value >>> 16;

			value += 1;
			
			return value;
		}
	}
	
	public static final long roundUpUnsignedIntToNearestPowerOf2(int unsignedValue)
	{
		if (unsignedValue == 0)
		{
			return 1;
		}
		else if (unsignedValue < 0)
		{
			return -roundDownToNearestPowerOf2(-unsignedValue);
		}
		else
		{
			long value = unsignedValue - 1;

			value |= value >>> 1;
			value |= value >>> 2;
			value |= value >>> 4;
			value |= value >>> 8;
			value |= value >>> 16;

			value += 1;
			
			return value;
		}
	}
	
	public static final int roundDownToNearestPowerOf2(int value)
	{
		if(value == 0)
		{
			return 0;
		}
		else if(value < 0)
		{
			return -roundUpToNearestPowerOf2(-value);
		}
		else
		{
			return roundUpToNearestPowerOf2(value) >>> 1;
		}
	}
	
	public static final long roundUpToNearestPowerOf2(long value)
	{
		if (value == 0)
		{
			return 1;
		}
		else if (value < 0)
		{
			return -roundDownToNearestPowerOf2(-value);
		}
		else
		{
			value -= 1;

			value |= value >>> 1;
			value |= value >>> 2;
			value |= value >>> 4;
			value |= value >>> 8;
			value |= value >>> 16;
			value |= value >>> 32;

			value += 1;
			
			return value;
		}
	}
	
	public static final long roundDownToNearestPowerOf2(long value)
	{
		if(value == 0)
		{
			return 0;
		}
		else if(value < 0)
		{
			return -roundUpToNearestPowerOf2(-value);
		}
		else
		{
			return roundUpToNearestPowerOf2(value) >>> 1;
		}
	}
}
