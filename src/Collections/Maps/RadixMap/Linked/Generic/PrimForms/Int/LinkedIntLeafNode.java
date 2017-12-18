package Collections.Maps.RadixMap.Linked.Generic.PrimForms.Int;

import Collections.PackedArrays.HalfByte.HalfByteArray;

class LinkedIntLeafNode extends LinkedIntNode
{
	private int cargo;
	
	private LinkedIntLeafNode prev;
	private LinkedIntLeafNode next;
	
	/**
	 * constructor for root nodes
	 */
	public LinkedIntLeafNode()
	{
		super(null, HalfByteArray.getEmptyInstance(), LinkedIntNode.CargoStateMask_CanHaveCargo);
		this.cargo = 0;
		this.prev = null;
		this.next = null;
	}
	
	public LinkedIntLeafNode(HalfByteArray inKeyChunk, int inCargo)
	{
		this(null, inKeyChunk, inCargo);
	}
	
	public LinkedIntLeafNode(LinkedIntNode old, int inCargo)
	{
		super(old);
		this.cargo = inCargo;
		this.prev = null;
		this.next = null;
	}
	
	public LinkedIntLeafNode(LinkedIntNode parent, HalfByteArray inKeyChunk, int inCargo)
	{
		super(parent, inKeyChunk, LinkedIntNode.CargoStateMask_hasCargo);
		this.cargo = inCargo;
		this.prev = null;
		this.next = null;
	}
	
	public final int getValue()
	{
		return this.cargo;
	}
	
	public final int getCargo()
	{
		return this.cargo;
	}
	
	public final void setCargo(int inCargo)
	{
		this.cargo = inCargo;
	}
	
	final LinkedIntLeafNode getPrev()
	{
		return this.prev;
	}
	
	final boolean hasPrev()
	{
		return this.prev != null;
	}

	final LinkedIntLeafNode getNext()
	{
		return this.next;
	}
	
	final boolean hasNext()
	{
		return this.next != null;
	}

	final void setNext(LinkedIntLeafNode inNext)
	{
		this.next = inNext;
	}

	final void setPrev(LinkedIntLeafNode inPrev)
	{
		this.prev = inPrev;
	}
	
	/**
	 * use only for root node cargo stuff
	 * @param mask
	 */
	public final void setCargoState(int mask)
	{
		this.childrenAndState |= mask;
	}
	
	/**
	 * use only for root node cargo stuff
	 */
	public final void clearCargo()
	{
		this.childrenAndState = (this.childrenAndState & 0xff) | LinkedIntNode.CargoStateMask_CanHaveCargo;
	}
	
	/**
	 * use only for root node cargo stuff
	 */
	public final void clearAll()
	{
		this.childrenAndState = LinkedIntNode.CargoStateMask_CanHaveCargo;
		this.children = new LinkedIntNode[LinkedIntNode.numKidsMax];
		this.prev = null;
		this.next = null;
	}
	
	public final LinkedIntNode transformIntoBranchNode()
	{
		return new LinkedIntNode(this);
	}
}
