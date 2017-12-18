package Collections.Maps.RadixMap.Linked.Generic.Mk01;

import java.util.Map.Entry;

import Collections.PackedArrays.HalfByte.HalfByteArray;

interface LinkedCargoNode<U> extends Entry<HalfByteArray, U>
{
	public U getCargo();

	public void setCargo(U in);
	
	public abstract LinkedCargoNode<U> getPrev();

	public abstract LinkedCargoNode<U> getNext();

	public abstract void setPrev(LinkedCargoNode<U> in);

	public abstract void setNext(LinkedCargoNode<U> in);
}
