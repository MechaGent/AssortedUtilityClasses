package Collections.Maps.RadixMap.Linked.Generic.Mk01;

import Collections.Lists.CharList.CharList;
import Collections.PackedArrays.HalfByte.HalfByteArray;

class LinkedRootNode<U> extends LinkedNode<U> implements LinkedCargoNode<U>
{
	private U cargo;
	private boolean hasCargo;	//in case the cargo is actually supposed to be null-valued
	
	private LinkedCargoNode<U> prev;
	private LinkedCargoNode<U> next;

	LinkedRootNode()
	{
		this.cargo = null;
		this.hasCargo = false;
		
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
	public final boolean isRoot()
	{
		return true;
	}
	
	@Override
	public final U getCargo()
	{
		return this.cargo;
	}
	
	public final boolean cargoIsSet()
	{
		return this.hasCargo;
	}
	
	@Override
	public final void setCargo(U in)
	{
		this.cargo = in;
		this.hasCargo = true;
	}
	
	public final void clearCargo()
	{
		this.cargo = null;
		this.hasCargo = false;
	}
	
	public final void clearAll()
	{
		this.clearCargo();
		this.clearChildren();
	}

	@Override
	public final HalfByteArray getKeyChunk()
	{
		return HalfByteArray.getEmptyInstance();
	}
	
	@Override
	public final int getKeyChunkLength()
	{
		return 0;
	}

	@Override
	public final void setKeyChunk(HalfByteArray inNewKeyChunk)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public final LinkedNode<U> getParent()
	{
		return null;
	}

	@Override
	public final void setParent(LinkedNode<U> inParent)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public final CharList toCharList(int offset)
	{
		final CharList result = new CharList();

		result.add('\t', offset);
		result.add("<root>(");

		if (this.hasCargo)
		{
			if (this.cargo == null)
			{
				result.add("<null>");
			}
			else
			{
				result.add(this.cargo.toString());
			}
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
