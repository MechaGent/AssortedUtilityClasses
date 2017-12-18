package Collections.Maps.JSONish.Backend;

import java.util.Iterator;

import Collections.Maps.RadixMap.Linked.Generic.LinkedRadixMap;

public class LotusFactory
{
	public static LotusVar getInstance(String name, String in)
	{
		return getInstance(name, in, false);
	}
	
	public static LotusVar getInstance(String name, String in, boolean wasString)
	{
		return LotusVarPrime_Mk01.getInstance(name, in, wasString);
	}
	
	public static LotusVar getInstance(String name, int in)
	{
		return LotusVarPrime_Mk01.getInstance(name, in);
	}
	
	public static LotusVar getInstance(String name, double in)
	{
		return LotusVarPrime_Mk01.getInstance(name, in);
	}
	
	public static LotusArray getInstance(String name, LotusFormat[] in)
	{
		return LotusArrayPrime_Mk01.getGeneralInstance(name, in);
	}
	
	public static LotusArray getInstance(String name, Iterator<LotusFormat> in, int length)
	{
		return LotusArrayPrime_Mk01.getGeneralInstance(name, in, length);
	}
	
	public static LotusObject getInstance(String name, LinkedRadixMap<LotusFormat> in)
	{
		return new LotusObjectPrime_Mk01(name, in);
	}
}
