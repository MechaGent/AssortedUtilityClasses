package Collections.Maps.RadixMap.Linked.Generic.Mk01;

import Collections.Lists.CharList.CharList;
import Collections.PackedArrays.HalfByte.HalfByteArray;

class LinkedLeafNode<U> extends LinkedBranchNode<U> implements LinkedCargoNode<U>
{
	private U cargo;
	
	private LinkedCargoNode<U> prev;
	private LinkedCargoNode<U> next;

	public LinkedLeafNode(HalfByteArray inKeyChunk, U inCargo)
	{
		this(null, inKeyChunk, inCargo);
	}
	
	public LinkedLeafNode(LinkedNode<U> old, U inCargo)
	{
		super(old);
	}
	
	public LinkedLeafNode(LinkedNode<U> inParent, HalfByteArray inKeyChunk, U inCargo)
	{
		super(inParent, inKeyChunk);
		this.cargo = inCargo;
		
		this.prev = null;
		this.next = null;
	}
	
	@Override
	public final LinkedCargoNode<U> getPrev()
	{
		return this.prev;
	}

	@Override
	public final LinkedCargoNode<U> getNext()
	{
		return this.next;
	}

	@Override
	public final void setPrev(LinkedCargoNode<U> inIn)
	{
		this.prev = inIn;
	}

	@Override
	public final void setNext(LinkedCargoNode<U> inIn)
	{
		this.next = inIn;
	}
	
	@Override
	public final boolean canHaveCargo()
	{
		return true;
	}

	@Override
	public final U getCargo()
	{
		return this.cargo;
	}

	@Override
	public final void setCargo(U inCargo)
	{
		this.cargo = inCargo;
	}

	@Override
	public final CharList toCharList(int offset)
	{
		final CharList result = new CharList();

		result.add(this.getKeyChunk().toCharList(offset), true);
		result.add('(');

		if (this.cargo == null)
		{
			result.add("<null>");
		}
		else
		{
			result.add(this.cargo.toString());
		}

		result.add("):{");

		final CharList kidsCharList = this.kidsToCharList(offset + 1);

		if (kidsCharList.size() > 0)
		{
			result.addNewLine();
			result.add(kidsCharList, true);
			result.addNewLine();
			result.add('\t', offset);
		}

		result.add('}');

		return result;
	}
}
