package Collections.Maps.RadixMap.Linked.Generic.Mk01;

class LinkedStackFrame<U>
{
	private final LinkedNode<U> node;
	protected int index;
	private int numKidsLeft;

	LinkedStackFrame(LinkedNode<U> inNode, int inIndex)
	{
		this.node = inNode;
		this.index = inIndex;
		this.numKidsLeft = inNode.getNumChildren();
		
		while (this.index < 16 && !this.node.hasChildAt(this.index))
		{
			this.incrementIndex();
		}
	}
	
	protected final int getNodeKeyChunkLength()
	{
		return this.node.getKeyChunkLength();
	}
	
	protected void incrementIndex()
	{
		this.index++;
	}
	
	private final int getAndIncrementIndex()
	{
		final int result = this.index;
		this.incrementIndex();
		return result;
	}
	
	public final LinkedNode<U> getNextChildNode()
	{
		final LinkedNode<U> result = this.node.getChildAt(this.getAndIncrementIndex());
		this.numKidsLeft--;

		while (this.index < 16 && !this.node.hasChildAt(this.index))
		{
			this.incrementIndex();
		}

		return result;
	}
	
	public final boolean hasKidsLeft()
	{
		return this.numKidsLeft > 0;
	}
}
