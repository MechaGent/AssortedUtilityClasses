package Collections.Maps.RadixMap.Unlinked.Generic.Mk01;

import java.util.Map.Entry;

import Collections.Lists.CharList.CharList;
import Collections.Lists.Generic.Directed.SingleLinkedList;
import Collections.PackedArrays.HalfByte.HalfByteArray;

abstract class Node<U> implements Entry<HalfByteArray, U>
{
	static final Object NullCargoObject = "NullCargoObject";
	private static final int numKidsMax = 16;
	
	private final Node<U>[] children;
	private int numChildren;
	
	@SuppressWarnings("unchecked")
	public Node()
	{
		this.children = new Node[numKidsMax];
		this.numChildren = 0;
	}
	
	protected Node(Node<U> old)
	{
		this.children = old.children;
		this.numChildren = old.numChildren;
	}
	
	public boolean isRoot()
	{
		return false;
	}
	
	public U getCargo()
	{
		return null;
	}
	
	public void setCargo(U in)
	{
		
	}
	
	@Override
	public HalfByteArray getKey()
	{
		final SingleLinkedList<HalfByteArray> fragments = new SingleLinkedList<HalfByteArray>();

		int totalLength = this.getKeyChunk().length();
		
		fragments.add(this.getKeyChunk());

		Node<U> current = this.getParent();

		while (current != null)
		{
			totalLength += current.getKeyChunk().length();
			fragments.push(current.getKeyChunk());
			current = current.getParent();
		}

		final HalfByteArray result = HalfByteArray.getInstance(totalLength);
		int resultIndex = 0;

		while (fragments.isNotEmpty())
		{
			final HalfByteArray fragment = fragments.pop();
			result.copyInArray(resultIndex, fragment);
			resultIndex += fragment.length();
		}

		return result;
	}

	@Override
	public U getValue()
	{
		return this.getCargo();
	}

	@Override
	public U setValue(U inArg0)
	{
		throw new UnsupportedOperationException();
	}
	
	public abstract HalfByteArray getKeyChunk();
	public abstract void setKeyChunk(HalfByteArray newKeyChunk);
	
	public abstract Node<U> getParent();

	public abstract void setParent(Node<U> inParent);
	
	public abstract CharList toCharList(int offset);
	
	public boolean canHaveCargo()
	{
		return false;
	}
	
	public final boolean hasChildAt(int index)
	{
		return this.children[index] != null;
	}
	
	public final Node<U> getChildAt(int index)
	{
		return this.children[index];
	}
	
	public final Node<U> setChildAt(int index, Node<U> newChild)
	{
		final Node<U> oldChild;
		
		if(this.hasChildAt(index))
		{
			 oldChild = this.children[index];
		}
		else
		{
			oldChild = null;
			this.numChildren++;
		}
		
		this.children[index] = newChild;
		return oldChild;
	}
	
	protected final CharList kidsToCharList(int offset)
	{
		final CharList result = new CharList();
		
		int numKids = this.numChildren;
		
		for(int i = 0; i < numKidsMax && numKids > 0; i++)
		{
			if(this.hasChildAt(i))
			{
				if(numKids != this.numChildren)
				{
					result.addNewLine();
				}
				
				result.add(this.getChildAt(i).toCharList(offset), true);
				numKids--;
			}
		}
		
		return result;
	}
}
