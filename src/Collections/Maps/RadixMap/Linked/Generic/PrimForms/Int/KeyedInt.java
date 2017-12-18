package Collections.Maps.RadixMap.Linked.Generic.PrimForms.Int;

import Collections.PackedArrays.HalfByte.HalfByteArray;

public class KeyedInt
{
	private final HalfByteArray key;
	private final int value;
	
	public KeyedInt(HalfByteArray inKey, int inValue)
	{
		this.key = inKey;
		this.value = inValue;
	}

	public HalfByteArray getKey()
	{
		return this.key;
	}

	public int getValue()
	{
		return this.value;
	}
}
