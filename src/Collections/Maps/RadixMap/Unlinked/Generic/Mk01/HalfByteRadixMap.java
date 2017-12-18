package Collections.Maps.RadixMap.Unlinked.Generic.Mk01;

import java.util.Iterator;
import java.util.Map.Entry;

import Collections.Lists.CharList.CharList;
import Collections.Lists.Generic.Directed.SingleLinkedList;
import Collections.PackedArrays.HalfByte.HalfByteArray;
import Collections.PackedArrays.HalfByte.HalfByteArrayFactory;

public class HalfByteRadixMap<U> implements Iterable<Entry<HalfByteArray, U>>
{
	private final RootNode<U> root;
	private int numEntries;

	public HalfByteRadixMap()
	{
		this(new RootNode<U>());
	}

	HalfByteRadixMap(RootNode<U> inRoot)
	{
		this.root = inRoot;
		this.numEntries = 0;
	}

	public final int size()
	{
		return this.numEntries;
	}

	public final U get(int key)
	{
		return this.get_internal_noZeroCheck(HalfByteArrayFactory.convertIntoArray(key));
	}

	public final U get(long key)
	{
		return this.get_internal_noZeroCheck(HalfByteArrayFactory.convertIntoArray(key));
	}

	public final U get(char[] key)
	{
		if (this.numEntries == 0)
		{
			return null;
		}
		else if (key.length == 0)
		{
			return this.root.getCargo();
		}
		else
		{
			return this.get_internal_noZeroCheck(HalfByteArrayFactory.wrapIntoArray(key));
		}
	}

	/**
	 * if {@code key} is to be used repeatedly, it is recommended to cache the key in HalfByteArray form, instead of calling this method repeatedly.
	 * 
	 * @param key
	 * @return
	 */
	public final U get(CharList key)
	{
		if (this.numEntries == 0)
		{
			return null;
		}
		else if (key.size() == 0)
		{
			return this.root.getCargo();
		}
		else
		{
			return this.get_internal_noZeroCheck(key.toHalfByteArray());
		}
	}

	public final U get(String key)
	{
		if (this.numEntries == 0)
		{
			return null;
		}
		else if (key.length() == 0)
		{
			return this.root.getCargo();
		}
		else
		{
			return this.get_internal_noZeroCheck(HalfByteArrayFactory.wrapIntoArray(key));
		}
	}

	public final U get(HalfByteArray key)
	{
		if (this.numEntries == 0)
		{
			return null;
		}
		else if (key.length() == 0)
		{
			return this.root.getCargo();
		}
		else
		{
			return this.get_internal_noZeroCheck(key);
		}
	}

	private final U get_internal_noZeroCheck(HalfByteArray key)
	{
		final int firstHalfByte = key.getFirstHalfByte();
		final int keyLength = key.length();

		if (this.root.hasChildAt(firstHalfByte))
		{
			Node<U> current = this.root.getChildAt(firstHalfByte);
			int keyIndex = 0;

			while (true)
			{
				final HalfByteArray curKey = current.getKeyChunk();
				final int curMatch = key.matchAgainst(curKey, keyIndex + 1, 1);

				if (curMatch != curKey.length())
				{
					break;
				}
				else
				{
					// matches chunk, check childIndex
					keyIndex += curMatch;

					if (keyIndex == keyLength)
					{
						return current.getCargo();
					}
					else if (keyIndex > keyLength)
					{
						break;
					}
					else // keyIndex < keyLength
					{
						final int childIndex = key.getHalfByteAt(keyIndex);

						if (current.hasChildAt(childIndex))
						{
							current = current.getChildAt(childIndex);
						}
						else
						{
							break;
						}
					}
				}
			}
		}

		return null;
	}

	/*
	public final void put1(String key, U value)
	{
		if (key.length() == 0)
		{
			this.put1(HalfByteArray.getEmptyInstance(), value);
		}
		else
		{
			this.put1(HalfByteArrayFactory.wrapIntoArray(key), value);
		}
	}
	
	public final void put1(HalfByteArray key, U value)
	{
		this.put_internal1(key, value);
	}
	
	protected final U put_internal1(HalfByteArray key, U value)
	{
		if (key.length() == 0)
		{
			final U old = this.root.getCargo();
			this.root.setCargo(value);
			return old;
		}
		else
		{
			if (this.numEntries == 0)
			{
				return this.put_internal_insertNewRootKey(key, value);
			}
			else
			{
				final int firstHalfByte = key.getFirstHalfByte();
	
				if (!this.root.hasChildAt(firstHalfByte))
				{
					return this.put_internal_insertNewRootKey(key, value);
				}
				else
				{
					return this.put_internal_iterativeSearch(key, value, this.root.getChildAt(firstHalfByte));
				}
			}
		}
	}
	
	protected final U put_internal_insertNewRootKey(HalfByteArray key, U value)
	{
		final int firstHalfByte = key.getFirstHalfByte();
		final LeafNode<U> result = new LeafNode<U>(null, firstHalfByte, key, value);
		this.root.setChildAt(firstHalfByte, result);
		this.numEntries++;
	
		return null;
	}
	
	protected final U put_internal_iterativeSearch(HalfByteArray key, U value, Node<U> node)
	{
		final int keyLength = key.length();
		int keyIndex = 0;
	
		/*
		 * cases:
		 * -no match
		 * -full match
		 * --and continues on
		 * --keyIndex is at key.length()
		 * -partial match
		 * --midKey
		 * --newChild
		 * --replacementChild
		 *
		while (true)
		{
			HalfByteArray nodeKey = node.getKeyChunk();
			int matchResult = key.matchAgainst(nodeKey, keyIndex + 1, 1);
	
			if (matchResult == 0)
			{
				// no match, which means we need a splitter node
				final int firstHalfByte_split = nodeKey.getFirstHalfByte();
	
				final HalfByteArray splitKey = HalfByteArray.getInstance(1);
				splitKey.setHalfByteAt(0, firstHalfByte_split);
	
				final HalfByteArray oldKey = nodeKey.getSubArray(1);
				final int firstHalfByte_old = oldKey.getFirstHalfByte();
	
				final HalfByteArray restKey = key.getSubArray(keyIndex + 1);
				final int firstHalfByte_rest = restKey.getFirstHalfByte();
	
				/*
				System.out.println("splitKey: " + splitKey.toString());
				System.out.println("oldKey: " + oldKey.toString());
				System.out.println("restKey: " + restKey.toString());
				*
	
				final Node<U> parent = node.getParent();
				final Node<U> splitNode = new BranchNode<U>(parent, firstHalfByte_split, splitKey);
				final Node<U> nextNode = new LeafNode<U>(splitNode, firstHalfByte_rest, restKey, value);
	
				if (parent != null)
				{
					parent.setChildAt(firstHalfByte_split, splitNode);
				}
				else
				{
					this.root.setChildAt(firstHalfByte_split, splitNode);
				}
	
				splitNode.setChildAt(firstHalfByte_old, node);
				node.setChildIndex(firstHalfByte_old);
				node.setParent(splitNode);
				node.setKeyChunk(oldKey);
	
				splitNode.setChildAt(firstHalfByte_rest, nextNode);
				nextNode.setChildIndex(firstHalfByte_rest);
	
				this.numEntries++;
				return null;
			}
			else if (matchResult == nodeKey.length())
			{
				// full match so far, check if done
				if (keyIndex + matchResult == keyLength)
				{
					// current node should store cargo
					if (node.canHaveCargo())
					{
						final U oldCargo = node.getCargo();
						node.setCargo(value);
						return oldCargo;
					}
					else
					{
						final LeafNode<U> transNode = new LeafNode<U>(node, value);
						transNode.getParent().setChildAt(nodeKey.getFirstHalfByte(), transNode);
						this.numEntries++;
						return null;
					}
				}
				else if (keyIndex + matchResult + 1 == keyLength)
				{
					// childIndex should store cargo, after split (if needed)
					keyIndex += matchResult;
					final int lastChildIndex = key.getHalfByteAt(keyIndex);
	
					if (node.hasChildAt(lastChildIndex))
					{
						final Node<U> child = node.getChildAt(lastChildIndex);
						final HalfByteArray childKey = child.getKeyChunk();
	
						if (childKey.length() == 1)
						{
							// perfect length, just check if can store cargo
							if (child.canHaveCargo())
							{
								final U oldCargo = child.getCargo();
								child.setCargo(value);
								return oldCargo;
							}
							else
							{
								final LeafNode<U> transNode = new LeafNode<U>(child, value);
								transNode.getParent().setChildAt(childKey.getFirstHalfByte(), transNode);
								this.numEntries++;
								return null;
							}
						}
						else
						{
							final HalfByteArray split = HalfByteArray.getInstance(1);
							split.setHalfByteAt(0, lastChildIndex);
	
							final HalfByteArray restKey = childKey.getSubArray(1);
	
							final LeafNode<U> result = new LeafNode<U>(node, lastChildIndex, split, value);
							node.setChildAt(lastChildIndex, result);
							final int restChildIndex = restKey.getFirstHalfByte();
							child.setKeyChunk(restKey);
							child.setChildIndex(restChildIndex);
							result.setChildAt(restChildIndex, child);
							child.setParent(result);
							this.numEntries++;
	
							return null;
						}
					}
					else
					{
						final HalfByteArray split = HalfByteArray.getInstance(1);
						split.setHalfByteAt(0, lastChildIndex);
	
						final LeafNode<U> result = new LeafNode<U>(node, lastChildIndex, split, value);
						node.setChildAt(lastChildIndex, result);
	
						this.numEntries++;
	
						return null;
					}
				}
				else
				{
					keyIndex += matchResult;
					final int nextChildIndex = key.getHalfByteAt(keyIndex);
	
					if (node.hasChildAt(nextChildIndex))
					{
						node = node.getChildAt(nextChildIndex);
					}
					else
					{
						final HalfByteArray resultKey = key.getSubArray(keyIndex);
						final Node<U> result = new LeafNode<U>(node, nextChildIndex, resultKey, value);
						node.setChildAt(nextChildIndex, result);
						this.numEntries++;
	
						return null;
					}
				}
			}
			else
			{
				// partial match
				final int firstHalfByte_split = nodeKey.getFirstHalfByte();
	
				final HalfByteArray splitKey = nodeKey.getSubArray(0, matchResult, true);
	
				final HalfByteArray oldKey = nodeKey.getSubArray(matchResult + 1);
				final int firstHalfByte_old = oldKey.getFirstHalfByte();
	
				final HalfByteArray restKey = key.getSubArray(keyIndex + matchResult + 1);
				final int firstHalfByte_rest = restKey.getFirstHalfByte();
	
				/*
				System.out.println("matchResult: " + matchResult);
				System.out.println("splitKey: " + splitKey.toString());
				System.out.println("oldKey: " + oldKey.toString());
				System.out.println("restKey: " + restKey.toString());
				*
	
				final Node<U> parent = node.getParent();
				final Node<U> splitNode = new BranchNode<U>(parent, firstHalfByte_split, splitKey);
				final Node<U> nextNode = new LeafNode<U>(splitNode, firstHalfByte_rest, restKey, value);
	
				if (parent != null)
				{
					parent.setChildAt(firstHalfByte_split, splitNode);
				}
				else
				{
					this.root.setChildAt(firstHalfByte_split, splitNode);
				}
	
				splitNode.setChildAt(firstHalfByte_old, node);
				node.setChildIndex(firstHalfByte_old);
				node.setParent(splitNode);
				node.setKeyChunk(oldKey);
	
				splitNode.setChildAt(firstHalfByte_rest, nextNode);
				nextNode.setChildIndex(firstHalfByte_rest);
	
				this.numEntries++;
				return null;
			}
		}
	}
	*/

	public final U put(int key, U value)
	{
		return this.put_internal_noZeroCheck(HalfByteArrayFactory.convertIntoArray(key), value);
	}

	public final U put(long key, U value)
	{
		return this.put_internal_noZeroCheck(HalfByteArrayFactory.convertIntoArray(key), value);
	}

	public final U put(char[] key, U value)
	{
		if (key.length == 0)
		{
			return this.put_internal_root(value);
		}
		else
		{
			return this.put_internal_noZeroCheck(HalfByteArrayFactory.wrapIntoArray(key), value);
		}
	}

	/**
	 * if {@code key} is to be used repeatedly, it is recommended to cache the key in HalfByteArray form, instead of calling this method repeatedly.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public final U put(CharList key, U value)
	{
		if (key.size() == 0)
		{
			return this.put_internal_root(value);
		}
		else
		{
			return this.put_internal_noZeroCheck(key.toHalfByteArray(), value);
		}
	}

	public final U put(String key, U value)
	{
		if (key.length() == 0)
		{
			return this.put_internal_root(value);
		}
		else
		{
			return this.put_internal_noZeroCheck(HalfByteArrayFactory.wrapIntoArray(key), value);
		}
	}

	public final U put(HalfByteArray key, U value)
	{
		if (key.length() == 0)
		{
			return this.put_internal_root(value);
		}
		else
		{
			return this.put_internal_noZeroCheck(key, value);
		}
	}

	private final U put_internal_root(U value)
	{
		final U old;

		if (this.root.cargoIsSet())
		{
			old = this.root.getCargo();
		}
		else
		{
			old = null;
			this.numEntries++;
		}

		this.root.setCargo(value);

		return old;
	}

	/**
	 * the 'ZeroCheck' refers to the length of the key
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected final U put_internal_noZeroCheck(HalfByteArray key, U value)
	{
		final int firstHalfByte = key.getFirstHalfByte();

		if (this.numEntries == 0 || !this.root.hasChildAt(firstHalfByte))
		{
			final LeafNode<U> result = new LeafNode<U>(null, key, value);
			this.root.setChildAt(firstHalfByte, result);
			this.numEntries++;

			return null;
		}
		else
		{
			Node<U> node = this.root.getChildAt(firstHalfByte);
			int keyIndex = 0;

			while (true)
			{
				HalfByteArray nodeKey = node.getKeyChunk();
				int matchResult = key.matchAgainst(nodeKey, keyIndex + 1, 1);

				if (matchResult == 0)
				{
					// no match, which means we need a splitter node
					final int firstHalfByte_split = nodeKey.getFirstHalfByte();

					final HalfByteArray splitKey = HalfByteArray.getInstance(1);
					splitKey.setHalfByteAt(0, firstHalfByte_split);

					return this.put_internal_split(node, value, firstHalfByte_split, splitKey, nodeKey.getSubArray(1), key.getSubArray(keyIndex + 1), node.getParent());
				}
				else if (matchResult == nodeKey.length())
				{
					keyIndex += matchResult;
					final int dif = key.length() - keyIndex;

					// full match so far, check if done
					switch (dif)
					{
						case 0:
						{
							// current node should store cargo
							if (node.canHaveCargo())
							{
								final U oldCargo = node.getCargo();
								node.setCargo(value);
								
								return oldCargo;
							}
							else
							{
								final LeafNode<U> transNode = new LeafNode<U>(node, value);
								transNode.getParent().setChildAt(nodeKey.getFirstHalfByte(), transNode);
								this.numEntries++;
								return null;
							}
						}
						case 1:
						{
							// childIndex should store cargo, after split (if needed)
							final int lastChildIndex = key.getHalfByteAt(keyIndex);

							if (node.hasChildAt(lastChildIndex))
							{
								final Node<U> child = node.getChildAt(lastChildIndex);
								final HalfByteArray childKey = child.getKeyChunk();

								if (childKey.length() == 1)
								{
									// perfect length, just check if can store cargo
									if (child.canHaveCargo())
									{
										final CargoNode<U> cast = (CargoNode<U>) child;
										final U oldCargo = cast.getCargo();
										cast.setCargo(value);

										return oldCargo;
									}
									else
									{
										final LeafNode<U> transNode = new LeafNode<U>(child, value);
										transNode.getParent().setChildAt(childKey.getFirstHalfByte(), transNode);

										this.numEntries++;
										return null;
									}
								}
								else
								{
									final HalfByteArray split = HalfByteArray.getInstance(1);
									split.setHalfByteAt(0, lastChildIndex);

									final HalfByteArray restKey = childKey.getSubArray(1);

									final LeafNode<U> result = new LeafNode<U>(node, split, value);
									node.setChildAt(lastChildIndex, result);

									child.setKeyChunk(restKey);
									result.setChildAt(restKey.getFirstHalfByte(), child);
									child.setParent(result);

									this.numEntries++;
									return null;
								}
							}
							else
							{
								final HalfByteArray split = HalfByteArray.getInstance(1);
								split.setHalfByteAt(0, lastChildIndex);

								final LeafNode<U> result = new LeafNode<U>(node, split, value);
								node.setChildAt(lastChildIndex, result);

								this.numEntries++;
								return null;
							}
						}
						default:
						{
							final int nextChildIndex = key.getHalfByteAt(keyIndex);

							if (node.hasChildAt(nextChildIndex))
							{
								node = node.getChildAt(nextChildIndex);
								break;
							}
							else
							{
								final HalfByteArray resultKey = key.getSubArray(keyIndex);
								final LeafNode<U> result = new LeafNode<U>(node, resultKey, value);
								node.setChildAt(nextChildIndex, result);

								this.numEntries++;

								return null;
							}
						}
					}
				}
				else
				{
					// partial match
					// System.out.println("partial match on key: " + key.toCharList(0).toString());
					return this.put_internal_split(node, value, nodeKey.getFirstHalfByte(), nodeKey.getSubArray(0, matchResult, true), nodeKey.getSubArray(matchResult + 1), key.getSubArray(keyIndex + matchResult + 1), node.getParent());
				}
			}
		}
	}

	/**
	 * 
	 * @param node
	 * @param value
	 * @param firstHalfByte_split
	 * @param splitKey
	 * @param oldKey
	 * @param restKey
	 * @param parent
	 * @return {@code null} every time - wrote it like this to hopefully take advantage of tail-call optimization
	 */
	private final U put_internal_split(final Node<U> node, final U value, final int firstHalfByte_split, final HalfByteArray splitKey, final HalfByteArray oldKey, final HalfByteArray restKey, final Node<U> parent)
	{
		final Node<U> splitNode = new BranchNode<U>(parent, splitKey);
		final LeafNode<U> nextNode = new LeafNode<U>(splitNode, restKey, value);

		if (parent != null)
		{
			parent.setChildAt(firstHalfByte_split, splitNode);
		}
		else
		{
			this.root.setChildAt(firstHalfByte_split, splitNode);
		}

		splitNode.setChildAt(oldKey.getFirstHalfByte(), node);
		node.setParent(splitNode);
		node.setKeyChunk(oldKey);

		splitNode.setChildAt(restKey.getFirstHalfByte(), nextNode);

		this.numEntries++;

		return null;
	}

	public final CharList toHeirarchalCharList(int offset)
	{
		return this.root.toCharList(offset);
	}

	public final Iterator<Entry<HalfByteArray, U>> iterator()
	{
		return new Itsy<U>(this.root, this.numEntries);
	}

	public final CharList toFlatCharList(int offset)
	{
		final CharList result = new CharList();
		boolean isFirst = true;

		for (Entry<HalfByteArray, U> entry : this)
		{
			if (isFirst)
			{
				isFirst = false;
				result.add('\t', offset);
			}
			else
			{
				result.addNewIndentedLine(offset);
			}

			result.add('{');
			result.add(entry.getKey().toCharList(), true);
			result.add(", ");
			result.add(entry.getValue().toString());
			result.add('}');
		}

		return result;
	}

	private static class Itsy<U> implements Iterator<Entry<HalfByteArray, U>>
	{
		private final SingleLinkedList<StackFrame<U>> stack;
		private int numEntriesLeft;

		public Itsy(RootNode<U> root, int numEntries)
		{
			this.stack = new SingleLinkedList<StackFrame<U>>();
			this.stack.add(new StackFrame<U>(root));
			this.numEntriesLeft = numEntries;
		}

		@Override
		public boolean hasNext()
		{
			return this.numEntriesLeft > 0;
		}

		@Override
		public Entry<HalfByteArray, U> next()
		{
			while (this.stack.isNotEmpty())
			{
				StackFrame<U> current = this.stack.getFirst();

				if (current.hasRemaining())
				{
					final Node<U> next = current.getNextChildNode();

					int i = 0;

					for (; i < 16; i++)
					{
						if (next.hasChildAt(i))
						{
							final StackFrame<U> frame = new StackFrame<U>(next);
							this.stack.add(frame);
							break;
						}
					}

					if (next.canHaveCargo())
					{
						this.numEntriesLeft--;
						return next;
					}
				}
				else
				{
					this.stack.pop();
				}
			}

			return null;
		}

		private static class StackFrame<U>
		{
			private final Node<U> node;
			private int index;

			public StackFrame(Node<U> inNode)
			{
				this.node = inNode;
				this.index = 0;

				while (this.index < 16 && !this.node.hasChildAt(this.index))
				{
					this.index++;
				}
			}

			public Node<U> getNextChildNode()
			{
				if (this.index == -1)
				{
					this.index++;
					return this.node;
				}
				else
				{
					final Node<U> result = this.node.getChildAt(this.index++);

					while (this.index < 16 && !this.node.hasChildAt(this.index))
					{
						this.index++;
					}

					return result;
				}
			}

			public boolean hasRemaining()
			{
				return this.index < 16;
			}
		}
	}

	/*
	 * original get algo
	 * 
	 private final U get_internal(HalfByteArray key)
	{
		if (this.numEntries == 0)
		{
			return null;
		}
		else if (key.length() == 0)
		{
			return this.root.getCargo();
		}
		else
		{
			final int firstHalfByte = key.getFirstHalfByte();
	
			if (!this.root.hasChildAt(firstHalfByte))
			{
				return null;
			}
			else
			{
				Node<U> current = this.root.getChildAt(firstHalfByte);
				int keyIndex = 0;
	
				while (true)
				{
					final HalfByteArray curKey = current.getKeyChunk();
					final int curMatch = key.matchAgainst(curKey, keyIndex + 1, 1);
					//System.out.println("curMatch: " + curMatch);
					
					//System.out.println(curKey.getClass());
					//System.out.println("curMatch: " + curMatch + " curKeyLength: " + curKey.length() + "\r\n\tOffset: " + (keyIndex + 1) + "\r\n\tkey: " + key.toString() + "\r\n\tcurKey: " + curKey.toString());
	
					if (curMatch == curKey.length())
					{
						// matches chunk, check childIndex
						keyIndex += curMatch;
	
						if (keyIndex == key.length())
						{
							return current.getCargo();
						}
						else if(keyIndex < key.length())
						{
							final int childIndex = key.getHalfByteAt(keyIndex);
	
							if (current.hasChildAt(childIndex))
							{
								current = current.getChildAt(childIndex);
								//keyIndex++;
							}
							else
							{
								//System.out.println("triggered when key was length " + key.length() + ", but index was " + keyIndex + ", and childIndex was " + childIndex);
								return null;
							}
						}
						else
						{
							
							return null;
						}
					}
					else
					{
						return null;
					}
				}
			}
		}
	}
	 */
}
