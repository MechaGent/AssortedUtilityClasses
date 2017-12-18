package Collections.Maps.RadixMap.Unlinked.Generic;

import java.util.Iterator;

import Collections.Lists.Generic.Directed.SingleLinkedList;
import Collections.Maps.RadixMap.Unlinked.Generic.StackFrame.StackFrame_Normal;
import Collections.Maps.RadixMap.Unlinked.Generic.StackFrame.StackFrame_Root;
import CustomExceptions.UnhandledEnumException;

class OrderingIterators
{
	/**
	 * 
	 * works for both ints and longs
	 *
	 * @param <U>
	 */
	static class NatOrderedEntryItsy_Clean<U> implements Iterator<LeafNode<U>>
	{
		/*
		 * 00000000 to 7FFFFFFF - grows - (0 to 2147483647)
		 * 80000000 to FFFFFFFF - grows - (-2147483648 to -1)
		 */

		private final SingleLinkedList<StackFrame<U>> stack;
		private int numEntries;

		NatOrderedEntryItsy_Clean(LeafNode<U> inRoot, int inNumEntries)
		{
			this.numEntries = inNumEntries;

			if (inNumEntries > 0)
			{
				this.stack = new SingleLinkedList<StackFrame<U>>();
				this.stack.add(StackFrame_Root.getInstance(inRoot));
			}
			else
			{
				this.stack = null;
			}
		}

		@Override
		public final boolean hasNext()
		{
			return this.numEntries > 0;
		}

		@Override
		public final LeafNode<U> next()
		{
			while (true)
			{
				final StackFrame<U> first = this.stack.getFirst();

				if (first.hasRemaining())
				{
					final Node<U> child = first.getNextNode();

					final StackFrame_Normal<U> next = StackFrame_Normal.getInstance(child, 0);
					this.stack.push(next);

					if (child.canHaveCargo())
					{
						this.numEntries--;
						return (LeafNode<U>) child;
					}
				}
				else
				{
					this.stack.pop();
				}
			}
		}
	}
	
	static abstract class NatOrderedEntryItsy_Dirty<U> implements Iterator<LeafNode<U>>
	{
		protected final SingleLinkedList<StackFrame<U>> stack;
		protected int currentMinKeyLength;
		protected int numEntries;
		protected final LeafNode<U>[] buffer;
		protected boolean bufferIndex;
		
		@SuppressWarnings("unchecked")
		private NatOrderedEntryItsy_Dirty(LeafNode<U> inRoot, int inNumEntries)
		{
			this.numEntries = inNumEntries;
			this.currentMinKeyLength = 0;

			if (inNumEntries > 0)
			{
				this.stack = new SingleLinkedList<StackFrame<U>>();
				this.stack.add(StackFrame_Root.getInstance(inRoot));
				this.buffer = new LeafNode[2];
				this.bufferIndex = true;
			}
			else
			{
				this.stack = null;
				this.buffer = null;
				this.bufferIndex = false;
			}
		}
		
		@Override
		public final boolean hasNext()
		{
			return this.numEntries > 0;
		}
		
		protected final LeafNode<U> next(int comparConstant)
		{
			while (this.hasNext())
			{
				final StackFrame<U> first = this.stack.getFirst();

				if (first.hasRemaining())
				{
					final Node<U> child = first.getNextNode();

					final StackFrame<U> next = StackFrame_Normal.getInstance(child, 0);

					this.stack.push(next);

					this.currentMinKeyLength += child.getKeyChunkLength();

					if (child.canHaveCargo())
					{
						this.numEntries--;
						final LeafNode<U> cast = (LeafNode<U>) child;

						if (this.currentMinKeyLength == comparConstant)
						{
							if (this.bufferIndex)
							{
								this.bufferIndex = false;
								this.buffer[1] = cast;
								return this.buffer[0];
							}
							else
							{
								this.bufferIndex = true;
								this.buffer[0] = cast;
								return this.buffer[1];
							}
						}
					}
				}
				else
				{
					this.stack.pop();
					this.currentMinKeyLength -= first.getNodeKeyChunkLength();
				}
			}

			if (this.bufferIndex)
			{
				this.bufferIndex = false;
				return this.buffer[0];
			}
			else
			{
				this.bufferIndex = true;
				return this.buffer[1];
			}
		}
	}
	
	static class NatOrderedEntryItsy_Ints<U> extends NatOrderedEntryItsy_Dirty<U>
	{
		/*
		 * 00000000 to 7FFFFFFF - grows - (0 to 2147483647)
		 * 80000000 to FFFFFFFF - grows - (-2147483648 to -1)
		 */
		
		private NatOrderedEntryItsy_Ints(LeafNode<U> inRoot, int inNumEntries)
		{
			super(inRoot, inNumEntries);
		}

		static final <U> NatOrderedEntryItsy_Ints<U> getInstance(LeafNode<U> inRoot, int inNumEntries)
		{
			final NatOrderedEntryItsy_Ints<U> result = new NatOrderedEntryItsy_Ints<U>(inRoot, inNumEntries);
			result.next();
			return result;
		}
		
		@Override
		public final LeafNode<U> next()
		{
			return this.next(8);
		}
	}

	/**
	 * 
	 * this one checks keylength
	 *
	 * @param <U>
	 */
	static class NatOrderedEntryItsy_Longs<U> extends NatOrderedEntryItsy_Dirty<U>
	{
		/*
		 * 00000000 to 7FFFFFFF - grows - (0 to 2147483647)
		 * 80000000 to FFFFFFFF - grows - (-2147483648 to -1)
		 */
		
		private NatOrderedEntryItsy_Longs(LeafNode<U> inRoot, int inNumEntries)
		{
			super(inRoot, inNumEntries);
		}

		static final <U> NatOrderedEntryItsy_Longs<U> getInstance(LeafNode<U> inRoot, int inNumEntries)
		{
			final NatOrderedEntryItsy_Longs<U> result = new NatOrderedEntryItsy_Longs<U>(inRoot, inNumEntries);
			result.next();
			return result;
		}

		@Override
		public final LeafNode<U> next()
		{
			return this.next(16);
		}
	}

	/**
	 * 
	 * this one checks keylength
	 *
	 * @param <U>
	 */
	static class NatOrderedEntryItsy_IntAndLongs<U> implements Iterator<LeafNode<U>>
	{
		/*
		 * 00000000 to 7FFFFFFF - grows - (0 to 2147483647)
		 * 80000000 to FFFFFFFF - grows - (-2147483648 to -1)
		 */

		private final NatOrderedEntryItsy_Ints<U> IntItsy;
		private final NatOrderedEntryItsy_Longs<U> LongItsy;

		private LeafNode<U> IntBuffer;
		private int primIntBuffer;
		private LeafNode<U> LongBuffer;
		private long primLongBuffer;

		private PopStates popState;

		NatOrderedEntryItsy_IntAndLongs(LeafNode<U> inRoot, int inNumEntries)
		{
			this.IntItsy = NatOrderedEntryItsy_Ints.getInstance(inRoot, inNumEntries);
			this.LongItsy = NatOrderedEntryItsy_Longs.getInstance(inRoot, inNumEntries);

			if (inNumEntries > 0)
			{
				this.IntBuffer = this.IntItsy.next();

				if (this.IntBuffer != null)
				{
					this.primIntBuffer = this.IntBuffer.getKey().interpretAsInt();

					this.LongBuffer = this.LongItsy.next();

					if (this.LongBuffer != null)
					{
						this.primLongBuffer = this.LongBuffer.getKey().interpretAsLong();
						this.popState = PopStates.Both;
					}
					else
					{
						this.primLongBuffer = 0;
						this.popState = PopStates.IntOnly;
					}
				}
				else
				{
					this.primIntBuffer = 0;

					this.LongBuffer = this.LongItsy.next();

					if (this.LongBuffer != null)
					{
						this.primLongBuffer = this.LongBuffer.getKey().interpretAsLong();
						this.popState = PopStates.LongOnly;
					}
					else
					{
						this.primLongBuffer = 0;
						this.popState = PopStates.Neither;
					}
				}
			}
			else
			{
				this.IntBuffer = null;
				this.primIntBuffer = 0;
				this.LongBuffer = null;
				this.primLongBuffer = 0;
				this.popState = PopStates.Neither;
			}
		}

		@Override
		public final boolean hasNext()
		{
			return this.popState != PopStates.Neither;
		}

		@Override
		public final LeafNode<U> next()
		{
			final LeafNode<U> result;

			switch (this.popState)
			{
				case Both:
				{
					if (this.primIntBuffer <= this.primLongBuffer)
					{
						result = this.IntBuffer;

						if (this.IntItsy.hasNext())
						{
							this.IntBuffer = this.IntItsy.next();
							this.primIntBuffer = this.IntBuffer.getKey().interpretAsInt();
						}
						else
						{
							this.IntBuffer = null;
							this.popState = PopStates.LongOnly;
						}
					}
					else
					{
						result = this.LongBuffer;

						if (this.LongItsy.hasNext())
						{
							this.LongBuffer = this.LongItsy.next();
							this.primLongBuffer = this.LongBuffer.getKey().interpretAsLong();
						}
						else
						{
							this.LongBuffer = null;
							this.popState = PopStates.IntOnly;
						}
					}

					break;
				}
				case IntOnly:
				{
					result = this.IntBuffer;

					if (this.IntItsy.hasNext())
					{
						this.IntBuffer = this.IntItsy.next();
						this.primIntBuffer = this.IntBuffer.getKey().interpretAsInt();
					}
					else
					{
						this.IntBuffer = null;
						this.popState = PopStates.Neither;
					}

					break;
				}
				case LongOnly:
				{
					result = this.LongBuffer;

					if (this.LongItsy.hasNext())
					{
						this.LongBuffer = this.LongItsy.next();
						this.primLongBuffer = this.LongBuffer.getKey().interpretAsLong();
					}
					else
					{
						this.LongBuffer = null;
						this.popState = PopStates.Neither;
					}
					break;
				}
				case Neither:
				{
					result = null;
					break;
				}
				default:
				{
					throw new UnhandledEnumException(this.popState);
				}
			}

			return result;
		}

		private static enum PopStates
		{
			IntOnly,
			LongOnly,
			Both,
			Neither;
		}
	}
}
