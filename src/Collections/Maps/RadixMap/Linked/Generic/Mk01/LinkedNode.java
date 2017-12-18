package Collections.Maps.RadixMap.Linked.Generic.Mk01;

import java.util.Map.Entry;

import Collections.Lists.CharList.CharList;
import Collections.Lists.Generic.Directed.SingleLinkedList;
import Collections.PackedArrays.HalfByte.HalfByteArray;

abstract class LinkedNode<U> implements Entry<HalfByteArray, U>
{
	private static final int numKidsMax = 16;

	private final LinkedNode<U>[] children;
	private int numChildren;

	@SuppressWarnings("unchecked")
	public LinkedNode()
	{
		this.children = new LinkedNode[numKidsMax];
		this.numChildren = 0;
	}

	protected LinkedNode(LinkedNode<U> old)
	{
		this.children = old.children;
		this.numChildren = old.numChildren;
	}
	
	public final int getChildrenMask()
	{
		int result = 0;
		
		for(int i = 0; i < 16; i++)
		{
			if(this.hasChildAt(i))
			{
				final int mask = 1 << i;
				result |= mask;
			}
		}
		
		return result;
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
		// default implementation
	}

	public final boolean hasChildren()
	{
		return this.numChildren != 0;
	}

	public final int getNumChildren()
	{
		return this.numChildren;
	}

	public abstract LinkedCargoNode<U> getPrev();

	public abstract LinkedCargoNode<U> getNext();

	public abstract void setPrev(LinkedCargoNode<U> in);

	public abstract void setNext(LinkedCargoNode<U> in);

	@Override
	public final HalfByteArray getKey()
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

	@Override
	public final U getValue()
	{
		return this.getCargo();
	}

	@Override
	public final U setValue(U inArg0)
	{
		throw new UnsupportedOperationException();
	}

	public abstract HalfByteArray getKeyChunk();
	
	public abstract int getKeyChunkLength();

	public abstract void setKeyChunk(HalfByteArray newKeyChunk);

	public abstract LinkedNode<U> getParent();

	public abstract void setParent(LinkedNode<U> inParent);

	public abstract CharList toCharList(int offset);

	public boolean canHaveCargo()
	{
		return false;
	}

	public final boolean hasChildAt(int index)
	{
		return this.children[index] != null;
	}

	public final LinkedNode<U> getChildAt(int index)
	{
		return this.children[index];
	}

	public final LinkedNode<U> setChildAt(int index, LinkedNode<U> newChild)
	{
		final LinkedNode<U> oldChild;

		if (this.hasChildAt(index))
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

	public final void clearChildren()
	{
		for (int i = 0; i < numKidsMax; i++)
		{
			this.children[i++] = null;
		}

		this.numChildren = 0;
	}

	protected final void resetParentOfChildrenTo(LinkedNode<U> stepParent)
	{
		switch (this.numChildren)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				int i = 0;

				while (this.children[i] == null)
				{
					i++;
				}

				this.children[i].setParent(stepParent);
				break;
			}
			default:
			{
				int kidsLeft = this.numChildren;

				for (int i = 0; i < numKidsMax; i++)
				{
					if (kidsLeft == 0)
					{
						break;
					}
					else
					{
						kidsLeft--;
					}

					this.children[i].setParent(stepParent);
				}

				break;
			}
		}
	}

	protected final CharList kidsToCharList(int offset)
	{
		final CharList result = new CharList();

		int numKids = this.numChildren;

		for (int i = 0; i < numKidsMax && numKids > 0; i++)
		{
			if (this.hasChildAt(i))
			{
				if (numKids != this.numChildren)
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