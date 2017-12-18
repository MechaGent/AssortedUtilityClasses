package HandyStuff.ArrayStuff;

public final class MiscArrayStuff
{
	public static final char[] getInvertedCaseCopy(char[] in)
	{
		final char[] result = new char[in.length];
		
		for(int i = 0; i < result.length; i+=1)
		{
			if(Character.isLowerCase(in[i]))
			{
				result[i] = Character.toUpperCase(in[i]);
			}
			else
			{
				result[i] = Character.toLowerCase(in[i]);
			}
		}
		
		return result;
	}
	
	public static final String getInvertedCaseCopy(String in)
	{
		final char[] result = new char[in.length()];
		
		for(int i = 0; i < result.length; i+=1)
		{
			final char curr = in.charAt(i);
			if(Character.isLowerCase(curr))
			{
				result[i] = Character.toUpperCase(curr);
			}
			else
			{
				result[i] = Character.toLowerCase(curr);
			}
		}
		
		return new String(result);
	}
}
