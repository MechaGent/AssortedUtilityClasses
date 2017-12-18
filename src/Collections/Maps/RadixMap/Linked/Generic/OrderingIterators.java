package Collections.Maps.RadixMap.Linked.Generic;

import java.util.Iterator;
import java.util.Map.Entry;

import Collections.Lists.Generic.Directed.SingleLinkedList;
import Collections.PackedArrays.HalfByte.HalfByteArray;
import CustomExceptions.UnhandledEnumException;

class OrderingIterators
{
	/**
	 * 
	 * works for both ints and longs
	 *
	 * @param <U>
	 */
	static class NatOrderedEntryItsy_Clean<U> implements Iterator<Entry<HalfByteArray, U>>
	{
		/*
		 * 00000000 to 7FFFFFFF - grows - (0 to 2147483647)
		 * 80000000 to FFFFFFFF - grows - (-2147483648 to -1)
		 */

		private final SingleLinkedList<LinkedStackFrame<U>> stack;
		private int numEntries;

		public NatOrderedEntryItsy_Clean(LinkedLeafNode<U> inRoot, int inNumEntries)
		{
			this.numEntries = inNumEntries;

			if (inNumEntries > 0)
			{
				this.stack = new SingleLinkedList<LinkedStackFrame<U>>();
				this.stack.add(new LinkedStackFrame<U>(inRoot, LinkedStackFrame.IncrementModes.Normal));
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
		public final Entry<HalfByteArray, U> next()
		{
			while (true)
			{
				final LinkedStackFrame<U> first = this.stack.getFirst();

				if (first.hasKidsLeft())
				{
					final LinkedNode<U> child = first.getNextChildNode();

					final LinkedStackFrame<U> next = new LinkedStackFrame<U>(child);
					this.stack.push(next);

					if (child.canHaveCargo())
					{
						this.numEntries--;
						return (LinkedLeafNode<U>) child;
					}
				}
				else
				{
					this.stack.pop();
				}
			}
		}
	}

	static abstract class NatOrderedEntryItsy_Dirty<U> implements Iterator<Entry<HalfByteArray, U>>
	{
		protected final SingleLinkedList<LinkedStackFrame<U>> stack;
		protected int currentMinKeyLength;
		protected int numEntries;
		protected final Entry<HalfByteArray, U>[] buffer;
		protected boolean bufferIndex;

		@SuppressWarnings("unchecked")
		private NatOrderedEntryItsy_Dirty(LinkedLeafNode<U> inRoot, int inNumEntries)
		{
			this.numEntries = inNumEntries;
			this.currentMinKeyLength = 0;

			if (inNumEntries > 0)
			{
				this.stack = new SingleLinkedList<LinkedStackFrame<U>>();
				this.stack.add(new LinkedStackFrame<U>(inRoot, LinkedStackFrame.IncrementModes.StartAtHalfAndWrap));
				this.buffer = new Entry[2];
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

		protected final Entry<HalfByteArray, U> next(int comparConstant)
		{
			while (this.hasNext())
			{
				final LinkedStackFrame<U> first = this.stack.getFirst();

				if (first.hasKidsLeft())
				{
					final LinkedNode<U> child = first.getNextChildNode();

					final LinkedStackFrame<U> next = new LinkedStackFrame<U>(child);

					this.stack.push(next);

					this.currentMinKeyLength += child.getKeyChunkLength();

					if (child.canHaveCargo())
					{
						this.numEntries--;
						final LinkedLeafNode<U> cast = (LinkedLeafNode<U>) child;

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

		private NatOrderedEntryItsy_Ints(LinkedLeafNode<U> inRoot, int inNumEntries)
		{
			super(inRoot, inNumEntries);
		}

		public static final <U> NatOrderedEntryItsy_Ints<U> getInstance(LinkedLeafNode<U> inRoot, int inNumEntries)
		{
			final NatOrderedEntryItsy_Ints<U> result = new NatOrderedEntryItsy_Ints<U>(inRoot, inNumEntries);
			result.next();
			return result;
		}

		@Override
		public final Entry<HalfByteArray, U> next()
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

		private NatOrderedEntryItsy_Longs(LinkedLeafNode<U> inRoot, int inNumEntries)
		{
			super(inRoot, inNumEntries);
		}

		public static final <U> NatOrderedEntryItsy_Longs<U> getInstance(LinkedLeafNode<U> inRoot, int inNumEntries)
		{
			final NatOrderedEntryItsy_Longs<U> result = new NatOrderedEntryItsy_Longs<U>(inRoot, inNumEntries);
			result.next();
			return result;
		}

		@Override
		public final Entry<HalfByteArray, U> next()
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
	static class NatOrderedEntryItsy_IntAndLongs<U> implements Iterator<Entry<HalfByteArray, U>>
	{
		/*
		 * 00000000 to 7FFFFFFF - grows - (0 to 2147483647)
		 * 80000000 to FFFFFFFF - grows - (-2147483648 to -1)
		 */

		private final NatOrderedEntryItsy_Ints<U> IntItsy;
		private final NatOrderedEntryItsy_Longs<U> LongItsy;

		private Entry<HalfByteArray, U> IntBuffer;
		private int primIntBuffer;
		private Entry<HalfByteArray, U> LongBuffer;
		private long primLongBuffer;

		private PopStates popState;

		public NatOrderedEntryItsy_IntAndLongs(LinkedLeafNode<U> inRoot, int inNumEntries)
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
		public final Entry<HalfByteArray, U> next()
		{
			final Entry<HalfByteArray, U> result;

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
