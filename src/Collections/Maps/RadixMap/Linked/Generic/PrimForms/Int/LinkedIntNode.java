package Collections.Maps.RadixMap.Linked.Generic.PrimForms.Int;

import Collections.Lists.CharList.CharList;
import Collections.Lists.Generic.Directed.SingleLinkedList;
import Collections.PackedArrays.HalfByte.HalfByteArray;

class LinkedIntNode
{
	public static final int CargoStateMask_CannotHaveCargo = 0;
	public static final int CargoStateMask_CanHaveCargo = 1 << 2;
	public static final int CargoStateMask_hasCargo = 0b11 << 2;
	
	protected static final int numKidsMax = 16;
	
	private LinkedIntNode parent;
	private HalfByteArray keyChunk;
	protected LinkedIntNode[] children;
	
	protected int childrenAndState;
	
	public LinkedIntNode(HalfByteArray inKeyChunk)
	{
		this(null, inKeyChunk);
	}
	
	public LinkedIntNode(LinkedIntNode inParent, HalfByteArray inKeyChunk)
	{
		this(inParent, inKeyChunk, 0);
	}
	
	protected LinkedIntNode(LinkedIntNode old)
	{
		this.parent = old.parent;
		this.keyChunk = old.keyChunk;
		this.children = old.children;
		this.childrenAndState = old.childrenAndState;
	}
	
	protected LinkedIntNode(LinkedIntNode inParent, HalfByteArray inKeyChunk, int inChildrenAndState)
	{
		this.parent = inParent;
		this.keyChunk = inKeyChunk;
		this.children = new LinkedIntNode[numKidsMax];
		this.childrenAndState = inChildrenAndState;
	}

	public final int getCargoState()
	{
		return this.childrenAndState & 0xff00;
	}
	
	public final boolean hasCargo()
	{
		return (this.childrenAndState & 0xff00) == CargoStateMask_hasCargo;
	}
	
	public final boolean canHaveCargo()
	{
		return (this.childrenAndState & 0xff00) != CargoStateMask_CannotHaveCargo;
	}
	
	final HalfByteArray backtraceKey()
	{
		final SingleLinkedList<HalfByteArray> fragments = new SingleLinkedList<HalfByteArray>();

		// int totalLength = this.getKeyChunk().length();
		int totalLength = 0;

		// fragments.add(this.getKeyChunk());

		// LinkedNode<U> current = this.getParent();
		LinkedIntNode current = this;

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
	
	public final HalfByteArray getKeyChunk()
	{
		return this.keyChunk;
	}

	public final void setKeyChunk(HalfByteArray inKeyChunk)
	{
		this.keyChunk = inKeyChunk;
	}

	public final LinkedIntNode getParent()
	{
		return this.parent;
	}
	
	public final void setParent(LinkedIntNode in)
	{
		this.parent = in;
	}
	
	public final boolean hasChildren()
	{
		return this.getNumChildren() > 0;
	}
	
	public final int getNumChildren()
	{
		return this.childrenAndState & 0xff;
	}
	
	private final void incrementNumChildren()
	{
		this.childrenAndState++;	//I'm pretty sure this should work right
	}
	
	private final void decrementNumChildren()
	{
		this.childrenAndState--;	//I'm pretty sure this should work right
	}
	
	public final boolean hasChildAt(int index)
	{
		return this.children[index] != null;
	}
	
	public final void setChildAt(int index, LinkedIntNode child)
	{
		if(this.children[index] == null)
		{
			this.incrementNumChildren();
		}
		
		this.children[index] = child;
	}
	
	public final void removeChildAt(int index)
	{
		if(this.children[index] != null)
		{
			this.decrementNumChildren();
			this.children[index] = null;
		}
	}
	
	public final LinkedIntNode getChildAt(int index)
	{
		return this.children[index];
	}
	
	public CharList toCharList(int offset)
	{
		final CharList result = new CharList();

		result.add('\t', offset);
		
		if(this.parent == null)
		{
			result.add("root");
		}
		else
		{
			result.add(this.backtraceKey().toCharList(), true);
		}
		
		result.add('(');

		if (this.hasCargo())
		{
			result.add_asDecString(((LinkedIntLeafNode) this).getCargo());
		}

		result.add("):{");
		
		this.kidsToCharList(offset + 1, result);

		return result;
	}
	
	private final void kidsToCharList(int offset, CharList result)
	{
		final int relMax = this.getNumChildren();
		
		if(relMax > 0)
		{
			result.addNewLine();
			this.kidsToCharList_internal(offset, result, relMax + 1);
			result.addNewLine();
			result.add('\t', offset);
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

				result.add(this.getChildAt(i).toCharList(offset), true);
				numKids--;
			}
		}
	}
}
