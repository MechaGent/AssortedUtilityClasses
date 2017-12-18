package Collections.Maps.RadixMap.Unlinked.Generic;

import Collections.Lists.CharList.CharList;
import Collections.Lists.Generic.Directed.SingleLinkedList;
import Collections.PackedArrays.HalfByte.HalfByteArray;

class Node<U>
{	
	protected static final int numKidsMax = 16;
	
	private Node<U> parent;
	private HalfByteArray keyChunk;
	protected Node<U>[] children;
	
	protected int childrenAndState;
	
	Node(HalfByteArray inKeyChunk)
	{
		this(null, inKeyChunk);
	}
	
	Node(Node<U> inParent, HalfByteArray inKeyChunk)
	{
		this(inParent, inKeyChunk, 0);
	}
	
	protected Node(Node<U> old)
	{
		this.parent = old.parent;
		this.keyChunk = old.keyChunk;
		this.children = old.children;
		this.childrenAndState = old.childrenAndState;
	}
	
	protected Node(Node<U> old, int inChildrenAndState)
	{
		this.parent = old.parent;
		this.keyChunk = old.keyChunk;
		this.children = old.children;
		this.childrenAndState = inChildrenAndState;
	}
	
	protected Node(Node<U> inParent, HalfByteArray inKeyChunk, int inChildrenAndState)
	{
		this.parent = inParent;
		this.keyChunk = inKeyChunk;
		this.children = getNewKidsArr();
		this.childrenAndState = inChildrenAndState;
	}
	
	@SuppressWarnings("unchecked")
	protected static <U> Node<U>[] getNewKidsArr()
	{
		return new Node[numKidsMax];
	}
	
	final boolean hasCargo()
	{
		return (this.childrenAndState & 0xff00) == CargoStateMasks.HasCargo.value;
	}
	
	final boolean canHaveCargo()
	{
		return (this.childrenAndState & 0xff00) != CargoStateMasks.CannotHaveCargo.value;
	}
	
	final boolean hasChildAt(int index)
	{
		return this.children[index] != null;
	}
	
	final Node<U> getChildAt(int index)
	{
		return this.children[index];
	}
	
	final void setChildAt(int index, Node<U> in)
	{
		this.children[index] = in;
		this.childrenAndState = (this.childrenAndState & 0xff00) | (this.getNumChildren() + 1);
	}
	
	final int getNumChildren()
	{
		return this.childrenAndState & 0xff;
	}
	
	final int getKeyChunkLength()
	{
		return this.keyChunk.length();
	}
	
	final HalfByteArray getKeyChunk()
	{
		return this.keyChunk;
	}
	
	final void setKeyChunk(HalfByteArray in)
	{
		this.keyChunk = in;
	}
	
	final int getFirstKeyHalfByte()
	{
		return this.keyChunk.getFirstHalfByte();
	}
	
	final Node<U> getParent()
	{
		return this.parent;
	}
	
	final void setParent(Node<U> inParent)
	{
		this.parent = inParent;
	}
	
	final void setChildOfParent(int index, Node<U> child)
	{
		this.parent.setChildAt(index, child);
	}
	
	final HalfByteArray backtraceKey()
	{
		final SingleLinkedList<HalfByteArray> fragments = new SingleLinkedList<HalfByteArray>();

		// int totalLength = this.getKeyChunk().length();
		int totalLength = 0;

		// fragments.add(this.getKeyChunk());

		// LinkedNode<U> current = this.getParent();
		Node<U> current = this;

		while (current != null)
		{
			final HalfByteArray curKeyChunk = current.getKeyChunk();
			final int curLength = curKeyChunk.length();

			//System.out.println("current length: " + curLength);
			if (curLength > 0)
			{
				totalLength += curLength;
				fragments.push(curKeyChunk);
				//System.out.println("pushing: " + curKeyChunk.toCharList(0).toString() + " with length " + curLength);
			}

			current = current.getParent();
		}

		final HalfByteArray result = HalfByteArray.getInstance(totalLength);
		int resultIndex = 0;
		// System.out.println("diag: " + fragments.toDiagCharList(0).toString());

		while (fragments.isNotEmpty())
		{
			final HalfByteArray fragment = fragments.pop();

			result.copyInArray(resultIndex, fragment);

			resultIndex += fragment.length();
		}

		return result;
	}
	
	final CharList toCharList(int offset)
	{
		return this.toCharList(offset, this.keyChunk.toCharList());
	}
	
	CharList toCharList(int offset, String altName)
	{
		final CharList result = new CharList();

		result.add('\t', offset);
		
		result.add(altName);
		
		result.add('[');
		result.add_asDecString(this.getNumChildren());
		result.add(']');
		
		result.add('(');

		if (this.hasCargo())
		{
			result.add((((LeafNode<U>) this).getCargo()).toString());
		}

		result.add("):{");
		
		this.kidsToCharList(offset + 1, result);

		return result;
	}
	
	final CharList toCharList(int offset, CharList altName)
	{
		final CharList result = new CharList();

		result.add('\t', offset);
		
		result.add(altName, true);
		
		result.add('[');
		result.add_asDecString(this.getNumChildren());
		result.add(']');
		
		result.add('(');

		if (this.hasCargo())
		{
			result.add((((LeafNode<U>) this).getCargo()).toString());
		}

		result.add("):{");
		
		this.kidsToCharList(offset + 1, result);

		return result;
	}
	
	protected final void kidsToCharList(int offset, CharList result)
	{
		final int relMax = this.getNumChildren();
		
		if(relMax > 0)
		{
			result.addNewLine();
			this.kidsToCharList_internal(offset, result, relMax);
			result.addNewLine();
			result.add('\t', offset-1);
		}
		
		result.add('}');
	}
	
	private final void kidsToCharList_internal(int offset, CharList result, int relMax)
	{
		int numKids = relMax;
		
		for (int i = 0; i < numKidsMax && numKids > 0; i++)
		{
			if (this.hasChildAt(i))
			{
				if (numKids < relMax)
				{
					result.addNewLine();
				}

				result.add(this.getChildAt(i).toCharList(offset + 1), true);
				numKids--;
			}
		}
	}
}
