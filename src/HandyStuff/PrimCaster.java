package HandyStuff;

public class PrimCaster
{
	private static final boolean useUnsignedCast = false;
	
	/*
	 * toByte methods
	 */
	
	public static final byte toByte(short in)
	{
		return toByte_Short(in);
	}
	
	public static final byte toByte(char in)
	{
		return toByte_Char(in);
	}
	
	public static final byte toByte_Short(short in)
	{
		if(useUnsignedCast)
		{
			return toByte_uShort(in);
		}
		else
		{
			return toByte_sShort(in);
		}
	}
	
	public static final byte toByte_uShort(short in)
	{
		return (byte) in;
	}
	
	public static final byte toByte_sShort(short in)
	{
		return (byte) in;
	}
	
	public static final byte toByte_Char(char in)
	{
		if(useUnsignedCast)
		{
			return toByte_uChar(in);
		}
		else
		{
			return toByte_sChar(in);
		}
	}
	
	public static final byte toByte_uChar(char in)
	{
		return (byte) in;
	}
	
	public static final byte toByte_sChar(char in)
	{
		return (byte) in;
	}
	
	/*
	 * toInt methods
	 */
	
	public static final int toInt(byte in)
	{
		return toInt_Byte(in);
	}
	
	public static final int toInt(short in)
	{
		return toInt_Short(in);
	}
	
	public static final int toInt(char in)
	{
		return toInt_Char(in);
	}
	
	public static final int toInt_Byte(byte in)
	{
		if(useUnsignedCast)
		{
			return toInt_uByte(in);
		}
		else
		{
			return toInt_sByte(in);
		}
	}
	
	public static final int toInt_uByte(byte in)
	{
		return ((int) in) & 0xff;
	}
	
	public static final int toInt_sByte(byte in)
	{
		return (int) in;
	}
	
	public static final int toInt_Short(short in)
	{
		if(useUnsignedCast)
		{
			return toInt_uShort(in);
		}
		else
		{
			return toInt_sShort(in);
		}
	}
	
	public static final int toInt_uShort(short in)
	{
		return ((int) in) & 0xffff;
	}
	
	public static final int toInt_sShort(short in)
	{
		return (int) in;
	}
	
	public static final int toInt_Char(char in)
	{
		if(useUnsignedCast)
		{
			return toInt_uChar(in);
		}
		else
		{
			return toInt_sChar(in);
		}
	}
	
	public static final int toInt_uChar(char in)
	{
		return ((int) in) & 0xffff;
	}
	
	public static final int toInt_sChar(char in)
	{
		return (int) in;
	}
	
	/*
	 * toLong methods
	 */
	
	public static final long toLong(int in)
	{
		return toLong_Int(in);
	}
	
	public static final long toLong_Int(int in)
	{
		if(useUnsignedCast)
		{
			return toLong_uInt(in);
		}
		else
		{
			return toLong_sInt(in);
		}
	}
	
	public static final long toLong_uInt(int in)
	{
		return ((long) in) & 0xffffffffL;
	}
	
	public static final long toLong_sInt(int in)
	{
		return (long) in;
	}
}
