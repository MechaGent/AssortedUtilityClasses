package Collections.Maps.RadixMap.Linked.Generic.Mk01;

import Collections.Lists.CharList.CharList;
import Collections.PackedArrays.HalfByte.HalfByteArray;

class LinkedBranchNode<U> extends LinkedNode<U>
{
	private LinkedNode<U> parent;

	private HalfByteArray keyChunk;
	
	public LinkedBranchNode(HalfByteArray inKeyChunk)
	{
		this(null, inKeyChunk);
	}
	
	protected LinkedBranchNode(LinkedNode<U> old)
	{
		super(old);
	}
	
	protected LinkedBranchNode(LinkedBranchNode<U> old)
	{
		super(old);
		this.parent = old.parent;
		this.keyChunk = old.keyChunk;
	}

	public LinkedBranchNode(LinkedNode<U> inParent, HalfByteArray inKeyChunk)
	{
		super();
		this.parent = inParent;
		this.keyChunk = inKeyChunk;
	}
	
	public static final <U> LinkedBranchNode<U> transformFromLeafToBranch(LinkedBranchNode<U> in)
	{
		final LinkedBranchNode<U> result = new LinkedBranchNode<U>(in);
		result.resetParentOfChildrenTo(result);
		return result;
	}

	@Override
	public LinkedCargoNode<U> getPrev()
	{
		return null;
	}

	@Override
	public LinkedCargoNode<U> getNext()
	{
		return null;
	}

	@Override
	public void setPrev(LinkedCargoNode<U> inIn)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void setNext(LinkedCargoNode<U> inIn)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public final HalfByteArray getKeyChunk()
	{
		return this.keyChunk;
	}
	
	@Override
	public final int getKeyChunkLength()
	{
		return this.keyChunk.length();
	}

	@Override
	public final void setKeyChunk(HalfByteArray inNewKeyChunk)
	{
		this.keyChunk = inNewKeyChunk;
	}

	@Override
	public final LinkedNode<U> getParent()
	{
		return this.parent;
	}

	@Override
	public final void setParent(LinkedNode<U> inParent)
	{
		this.parent = inParent;
	}

	@Override
	public CharList toCharList(int offset)
	{
		final CharList result = new CharList();

		result.add(this.keyChunk.toCharList(offset), true);
		result.add(":{");
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
