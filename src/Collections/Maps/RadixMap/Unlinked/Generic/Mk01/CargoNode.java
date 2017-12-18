package Collections.Maps.RadixMap.Unlinked.Generic.Mk01;

import java.util.Map.Entry;

import Collections.PackedArrays.HalfByte.HalfByteArray;

public interface CargoNode<U> extends Entry<HalfByteArray, U>
{
	public U getCargo();

	public void setCargo(U in);
}
