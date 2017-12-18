package Collections.Maps.RadixMap.Unlinked.Generic.Mk01;

import Collections.Lists.CharList.CharList;
import Collections.PackedArrays.HalfByte.HalfByteArray;

class BranchNode<U> extends Node<U>
{
	private Node<U> parent;

	private HalfByteArray keyChunk;

	public BranchNode(Node<U> inParent, HalfByteArray inKeyChunk)
	{
		super();
		this.parent = inParent;
		this.keyChunk = inKeyChunk;
	}

	public BranchNode(Node<U> old)
	{
		super(old);

		this.parent = null;
		this.keyChunk = null;
		//throw new IllegalArgumentException();
	}

	public BranchNode(BranchNode<U> old)
	{
		super(old);
		this.parent = old.parent;
		this.keyChunk = old.keyChunk;
	}

	@Override
	public final HalfByteArray getKeyChunk()
	{
		return this.keyChunk;
	}

	@Override
	public final void setKeyChunk(HalfByteArray newKeyChunk)
	{		
		this.keyChunk = newKeyChunk;
	}

	@Override
	public final Node<U> getParent()
	{
		return this.parent;
	}

	@Override
	public final void setParent(Node<U> inParent)
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
