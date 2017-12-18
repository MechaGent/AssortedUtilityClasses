package Collections.Maps.RadixMap.Linked.Generic;

import java.util.Map.Entry;

import Collections.Lists.CharList.CharList;
import Collections.PackedArrays.HalfByte.HalfByteArray;

final class LinkedLeafNode<U> extends LinkedNode<U> implements Entry<HalfByteArray, U>
{
	@SuppressWarnings({
		"unchecked",
		"rawtypes" })
	private static final LinkedLeafNode EmptyLinkedLeafNode = new LinkedLeafNode(HalfByteArray.getEmptyInstance(), null);
	
	private U cargo;
	
	private LinkedLeafNode<U> prev;
	private LinkedLeafNode<U> next;
	
	/**
	 * constructor for root nodes
	 */
	public LinkedLeafNode()
	{
		super(null, HalfByteArray.getEmptyInstance(), CargoStateMasks.CanHaveCargo.value);
		this.cargo = null;
		this.prev = null;
		this.next = null;
	}
	
	public LinkedLeafNode(HalfByteArray inKeyChunk, U inCargo)
	{
		this(null, inKeyChunk, inCargo);
	}
	
	public LinkedLeafNode(LinkedNode<U> old, U inCargo)
	{
		super(old, old.getNumChildren() | CargoStateMasks.HasCargo.value);
		this.cargo = inCargo;
		this.prev = null;
		this.next = null;
	}
	
	public LinkedLeafNode(LinkedNode<U> parent, HalfByteArray inKeyChunk, U inCargo)
	{
		super(parent, inKeyChunk, CargoStateMasks.HasCargo.value);
		this.cargo = inCargo;
		this.prev = null;
		this.next = null;
	}
	
	@SuppressWarnings("rawtypes")
	public static final LinkedLeafNode getEmptyLinkedLeafNode()
	{
		return EmptyLinkedLeafNode;
	}
	
	public final U getCargo()
	{
		return this.cargo;
	}

	public final void setCargo(U inCargo)
	{
		this.cargo = inCargo;
		this.childrenAndState = (this.childrenAndState & LinkedNode.clearMask_CargoState) | CargoStateMasks.HasCargo.value;
	}

	public final boolean hasPrev()
	{
		return this.prev != null;
	}
	
	public final LinkedLeafNode<U> getPrev()
	{
		return this.prev;
	}

	public final void setPrev(LinkedLeafNode<U> inPrev)
	{
		this.prev = inPrev;
	}
	
	public final boolean hasNext()
	{
		return this.next != null;
	}

	public final LinkedLeafNode<U> getNext()
	{
		return this.next;
	}

	public final void setNext(LinkedLeafNode<U> inNext)
	{
		this.next = inNext;
	}
	
	/**
	 * use only for root node cargo stuff
	 */
	public final void clearCargo()
	{
		this.childrenAndState = (this.childrenAndState & LinkedNode.clearMask_CargoState) | CargoStateMasks.CanHaveCargo.value;
		this.cargo = null;
	}
	
	public final void clearAll()
	{
		this.childrenAndState = CargoStateMasks.CanHaveCargo.value;
		this.children = LinkedNode.getNewKidsArr();
		this.prev = null;
		this.next = null;
	}

	public final LinkedNode<U> transformIntoBranchNode()
	{
		return new LinkedNode<U>(this);
	}
	
	@Override
	public final CharList toCharList(int offset, String altName)
	{
		final CharList result = new CharList();

		result.add('\t', offset);
		result.add(altName);
		
		result.add('[');
		result.add_asDecString(this.getNumChildren());
		result.add(']');
		
		result.add('(');

		if (!this.hasCargo())
		{
			result.add("<null>");
		}
		else
		{
			result.add(this.cargo.toString());
		}

		result.add("):{");

		this.kidsToCharList(offset + 1, result);

		return result;
	}

	@Override
	public HalfByteArray getKey()
	{
		return this.backtraceKey();
	}

	@Override
	public U getValue()
	{
		return this.cargo;
	}

	@Override
	public U setValue(U inArg0)
	{
		throw new UnsupportedOperationException();
	}
}
