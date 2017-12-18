package Collections.Maps.RadixMap.Linked.Generic;

import Collections.Lists.CharList.CharList;
import Collections.Lists.Generic.Directed.SingleLinkedList;
import Collections.PackedArrays.HalfByte.HalfByteArray;

class LinkedNode<U>
{	
	protected static final int mask_numChildren = 0xff;
	protected static final int mask_CargoState = 0b11 << 8;
	protected static final int mask_ExternalFlags = 0xff << 24;
	
	//if ANDed with this.childrenAndState, will clear the bits assigned to numChildren
	protected static final int clearMask_numChildren = ~mask_numChildren;
	
	protected static final int clearMask_CargoState = ~mask_CargoState;
	protected static final int clearMask_ExternalFlags = ~mask_ExternalFlags;
	
	protected static final int numKidsMax = 16;
	
	private LinkedNode<U> parent;
	private HalfByteArray keyChunk;
	protected LinkedNode<U>[] children;
	
	/*
	 * bits 0 to 7 for numChildren		8 bits
	 * bits 8 to 9 for CargoState		2 bits
	 * bits 10 to 15 for <reserved>		6 bits
	 * bits 16 to 23 for <reserved>		8 bits
	 * bits 24 to 31 for ExternalFlag	8 bits
	 */
	protected int childrenAndState;
	protected int otherFlags;
	
	public LinkedNode(HalfByteArray inKeyChunk)
	{
		this(null, inKeyChunk);
	}
	
	public LinkedNode(LinkedNode<U> inParent, HalfByteArray inKeyChunk)
	{
		this(inParent, inKeyChunk, 0);
	}
	
	protected LinkedNode(LinkedNode<U> old)
	{
		this.parent = old.parent;
		this.keyChunk = old.keyChunk;
		this.children = old.children;
		this.childrenAndState = old.childrenAndState;
	}
	
	protected LinkedNode(LinkedNode<U> old, int inChildrenAndState)
	{
		this.parent = old.parent;
		this.keyChunk = old.keyChunk;
		this.children = old.children;
		this.childrenAndState = inChildrenAndState;
	}
	
	protected LinkedNode(LinkedNode<U> inParent, HalfByteArray inKeyChunk, int inChildrenAndState)
	{
		this.parent = inParent;
		this.keyChunk = inKeyChunk;
		this.children = getNewKidsArr();
		this.childrenAndState = inChildrenAndState;
	}
	
	@SuppressWarnings("unchecked")
	protected static <U> LinkedNode<U>[] getNewKidsArr()
	{
		return new LinkedNode[numKidsMax];
	}
	
	public final boolean hasCargo()
	{
		return (this.childrenAndState & mask_CargoState) == CargoStateMasks.HasCargo.value;
	}
	
	public final boolean canHaveCargo()
	{
		return (this.childrenAndState & mask_CargoState) != CargoStateMasks.CannotHaveCargo.value;
	}
	
	public final int getExternalFlags()
	{
		return (this.childrenAndState & mask_ExternalFlags) >>> 24;
	}
	
	public final void setExternalFlags(int in)
	{
		this.childrenAndState = (this.childrenAndState & clearMask_ExternalFlags) | (in << 24);
	}
	
	public final void setExternalFlags_OR(int in)
	{
		this.childrenAndState |= (in << 24);
	}
	
	public final void setExternalFlags_AND(int in)
	{
		this.childrenAndState &= ((in << 24) | 0xffffff);
	}
	
	public final boolean hasChildAt(int index)
	{
		return this.children[index] != null;
	}
	
	public final LinkedNode<U> getChildAt(int index)
	{
		return this.children[index];
	}
	
	public final void setChildAt(int index, LinkedNode<U> in)
	{
		this.children[index] = in;
		this.childrenAndState = (this.childrenAndState & clearMask_numChildren) | ((this.childrenAndState & mask_numChildren) + 1);
	}
	
	public final int getNumChildren()
	{
		return this.childrenAndState & mask_numChildren;
	}
	
	public final int getKeyChunkLength()
	{
		return this.keyChunk.length();
	}
	
	public final HalfByteArray getKeyChunk()
	{
		return this.keyChunk;
	}
	
	public final void setKeyChunk(HalfByteArray in)
	{
		this.keyChunk = in;
	}
	
	public final int getFirstKeyHalfByte()
	{
		return this.keyChunk.getFirstHalfByte();
	}
	
	public final LinkedNode<U> getParent()
	{
		return this.parent;
	}
	
	public final void setParent(LinkedNode<U> inParent)
	{
		this.parent = inParent;
	}
	
	public final void setChildOfParent(int index, LinkedNode<U> child)
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
		LinkedNode<U> current = this;

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
			// System.out.println("numEntries: " + fragments.size());
			final HalfByteArray fragment = fragments.pop();
			// System.out.println("current object: " + fragment);

			result.copyInArray(resultIndex, fragment);

			resultIndex += fragment.length();
		}

		return result;
	}
	
	public CharList toCharList(int offset)
	{
		return this.toCharList(offset, this.keyChunk.toCharList());
	}
	
	public CharList toCharList(int offset, String altName)
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
			result.add((((LinkedLeafNode<U>) this).getCargo()).toString());
		}

		result.add("):{");
		
		this.kidsToCharList(offset + 1, result);

		return result;
	}
	
	public CharList toCharList(int offset, CharList altName)
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
			result.add((((LinkedLeafNode<U>) this).getCargo()).toString());
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
