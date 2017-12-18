package Collections.Maps.RadixMap.Unlinked.Generic;

import java.util.Iterator;
import java.util.Map.Entry;

import Collections.Lists.Generic.Directed.SingleLinkedList;
import Collections.Maps.RadixMap.Unlinked.Generic.LeafNode.StringKeyedEntry;
import Collections.Maps.RadixMap.Unlinked.Generic.StackFrame.StackFrame_Normal;
import Collections.Maps.RadixMap.Unlinked.Generic.StackFrame.StackFrame_Normal_FirstNode;
import Collections.PackedArrays.HalfByte.HalfByteArray;

public class Iterators
{
	private Iterators()
	{
		//to prevent instantiation
	}
	
	private static class BaseEntriesItsy<U>
	{
		private final SingleLinkedList<StackFrame_Normal<U>> stack;
		private int numEntriesLeft;

		BaseEntriesItsy(LeafNode<U> root, int numEntries)
		{
			this.stack = new SingleLinkedList<StackFrame_Normal<U>>();
			
			
			if(root.hasCargo())
			{
				this.stack.add(StackFrame_Normal_FirstNode.getInstance(root, root.getNumChildren()));
			}
			else
			{
				this.stack.add(StackFrame_Normal.getInstance(root));
			}
			
			this.numEntriesLeft = numEntries;
			//System.out.println("creating itsy with size " + this.numEntriesLeft);
		}

		public final boolean hasNext()
		{
			return this.numEntriesLeft != 0;
		}

		protected final LeafNode<U> nextNode()
		{
			while (this.stack.isNotEmpty())
			{
				StackFrame_Normal<U> current = this.stack.getFirst();

				if (current.hasRemaining())
				{
					final Node<U> next = current.getNextNode();

					int i = 0;

					for (; i < 16; i++)
					{
						if (next.hasChildAt(i))
						{
							final StackFrame_Normal<U> frame = StackFrame_Normal.getInstance(next);
							this.stack.add(frame);
							break;
						}
					}

					if (next.hasCargo())
					{
						this.numEntriesLeft--;
						return (LeafNode<U>) next;
					}
				}
				else
				{
					this.stack.pop();
				}
			}

			return null;
		}
	}

	public static class EntriesItsy<U> extends BaseEntriesItsy<U> implements Iterator<Entry<HalfByteArray, U>>
	{
		EntriesItsy(LeafNode<U> inRoot, int inNumEntries)
		{
			super(inRoot, inNumEntries);
		}

		@Override
		public final LeafNode<U> next()
		{
			return this.nextNode();
		}
	}

	public static class StringKeyedEntryItsy<U> extends BaseEntriesItsy<U> implements Iterator<Entry<String, U>>
	{
		StringKeyedEntryItsy(LeafNode<U> inRoot, int inNumEntries)
		{
			super(inRoot, inNumEntries);
		}

		@Override
		public final StringKeyedEntry<U> next()
		{
			return this.nextNode().wrapAsStringKeyedEntry();
		}
	}

	@SuppressWarnings("rawtypes")
	public static class KeysItsy extends BaseEntriesItsy implements Iterator<HalfByteArray>
	{
		@SuppressWarnings("unchecked")
		KeysItsy(LeafNode<?> inRoot, int inNumEntries)
		{
			super(inRoot, inNumEntries);
		}

		@Override
		public final HalfByteArray next()
		{
			return this.nextNode().backtraceKey();
		}
	}

	public static class ValuesItsy<U> extends BaseEntriesItsy<U> implements Iterator<U>
	{
		ValuesItsy(LeafNode<U> inRoot, int inNumEntries)
		{
			super(inRoot, inNumEntries);
		}

		@Override
		public final U next()
		{
			final LeafNode<U> next = this.nextNode();
			System.out.println("entries left: " + next);
			return next.getValue();
		}
	}
}
