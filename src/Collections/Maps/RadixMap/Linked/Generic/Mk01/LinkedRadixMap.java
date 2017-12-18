package Collections.Maps.RadixMap.Linked.Generic.Mk01;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import Collections.Lists.CharList.CharList;
import Collections.Lists.Generic.Directed.SingleLinkedList;
import Collections.PackedArrays.HalfByte.HalfByteArray;
import Collections.PackedArrays.HalfByte.HalfByteArrayFactory;

public class LinkedRadixMap<U> implements Iterable<Entry<HalfByteArray, U>>, Map<HalfByteArray, U>
{
	protected final LinkedRootNode<U> root;
	private int numEntries;

	private LinkedCargoNode<U> firstEntry;
	private LinkedCargoNode<U> lastEntry;

	public LinkedRadixMap()
	{
		this(new LinkedRootNode<U>());
	}

	LinkedRadixMap(LinkedRootNode<U> inRoot)
	{
		this.root = inRoot;
		this.numEntries = 0;
	}

	private final void addEntry(LinkedCargoNode<U> in)
	{
		if (this.numEntries == 0)
		{
			this.firstEntry = in;
			this.lastEntry = in;
		}
		else
		{
			this.lastEntry.setNext(in);
			in.setPrev(this.lastEntry);
			this.lastEntry = in;
		}

		this.numEntries++;
	}

	private final void removeEntry(LinkedCargoNode<U> in)
	{
		switch (this.numEntries)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				if (this.firstEntry == in)
				{
					this.firstEntry = null;
					this.lastEntry = null;
					this.numEntries = 0;
				}
				else
				{
					throw new NullPointerException();
				}

				break;
			}
			case 2:
			{
				if (this.firstEntry == in)
				{
					this.firstEntry = this.lastEntry;
					this.lastEntry.setPrev(null);
					this.numEntries = 1;
				}
				else if (this.lastEntry == in)
				{
					this.lastEntry = this.firstEntry;
					this.firstEntry.setNext(null);
					this.numEntries = 1;
				}
				else
				{
					throw new NullPointerException();
				}

				break;
			}
			default:
			{
				if (this.firstEntry == in)
				{
					this.firstEntry = this.firstEntry.getNext();
					this.firstEntry.setPrev(null);
				}
				else if (this.lastEntry == in)
				{
					this.lastEntry = this.lastEntry.getPrev();
					this.lastEntry.setNext(null);
				}
				else
				{
					final LinkedCargoNode<U> before = in.getPrev();
					final LinkedCargoNode<U> after = in.getNext();

					if (before != null)
					{
						before.setNext(after);
					}

					if (after != null)
					{
						after.setPrev(before);
					}
				}

				this.numEntries--;

				break;
			}
		}
	}

	private final void reinsertEntry(LinkedRootNode<U> in)
	{
		this.reinsertEntry((LinkedCargoNode<U>) in);
	}

	private final void reinsertEntry(LinkedCargoNode<U> in)
	{
		switch (this.numEntries)
		{
			case 0:
			{
				this.firstEntry = in;
				this.lastEntry = in;

				this.numEntries = 1;
				break;
			}
			case 1:
			{
				if (this.firstEntry == in)
				{
					// do nothing
				}
				else
				{
					throw new NullPointerException();
				}

				break;
			}
			case 2:
			{
				if (this.firstEntry == in)
				{
					this.lastEntry.setPrev(null);
					this.lastEntry.setNext(in);
					in.setPrev(this.lastEntry);
					in.setNext(null);

					this.firstEntry = this.lastEntry;
					this.lastEntry = in;
				}
				else if (this.lastEntry == in)
				{
					// do nothing
				}
				else
				{
					throw new NullPointerException();
				}

				break;
			}
			default:
			{
				if (this.firstEntry == in)
				{
					this.lastEntry.setNext(in);
					in.setPrev(this.lastEntry);
					this.lastEntry = in;
					this.firstEntry = this.firstEntry.getNext();
					this.firstEntry.setPrev(null);
				}
				else if (this.lastEntry == in)
				{
					// do nothing
				}
				else
				{
					final LinkedCargoNode<U> before = in.getPrev();
					final LinkedCargoNode<U> after = in.getNext();

					if (before != null)
					{
						before.setNext(after);
					}

					if (after != null)
					{
						after.setPrev(before);
					}

					this.lastEntry.setNext(in);
					in.setPrev(this.lastEntry);
					this.lastEntry = in;
				}

				break;
			}
		}
	}

	protected final boolean containsKey_internal(String key)
	{
		if (this.numEntries == 0)
		{
			return false;
		}
		else if (key.length() == 0)
		{
			return this.root.cargoIsSet();
		}
		else
		{
			return this.containsKey_internal_noZeroChecks(HalfByteArrayFactory.wrapIntoArray(key));
		}
	}

	protected final boolean containsKey_internal(HalfByteArray key)
	{
		if (this.numEntries == 0)
		{
			return false;
		}
		else if (key.length() == 0)
		{
			return this.root.cargoIsSet();
		}
		else
		{
			return this.containsKey_internal_noZeroChecks(key);
		}
	}

	protected final boolean containsKey_internal_noZeroChecks(HalfByteArray key)
	{
		final int firstHalfByte = key.getFirstHalfByte();

		if (this.root.hasChildAt(firstHalfByte))
		{
			LinkedNode<U> current = this.root.getChildAt(firstHalfByte);
			int keyIndex = 0;
			final int keyLength = key.length();

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
						return true;
					}
					else if (keyIndex < keyLength)
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
					else
					{
						break;
					}
				}
			}
		}

		return false;
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
			LinkedNode<U> current = this.root.getChildAt(firstHalfByte);
			int keyIndex = 0;

			while (true)
			{
				final HalfByteArray curKey = current.getKeyChunk();
				final int curMatch = key.matchAgainst(curKey, keyIndex + 1, 1);

				if (curMatch != curKey.length())
				{
					break;
				}
				else	// matches chunk, check childIndex
				{
					keyIndex += curMatch;
					
					if (keyIndex == keyLength)
					{
						return current.getCargo();
					}
					else if(keyIndex > keyLength)
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
			return this.putHelper_root(value);
		}
		else
		{
			return this.put_internal_noZeroCheck(HalfByteArrayFactory.wrapIntoArray(key), value);
		}
	}
	
	/**
	 * if {@code key} is to be used repeatedly, it is recommended to cache the key in HalfByteArray form, instead of calling this method repeatedly.
	 * @param key
	 * @param value
	 * @return
	 */
	public final U put(CharList key, U value)
	{
		if (key.size() == 0)
		{
			return this.putHelper_root(value);
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
			return this.putHelper_root(value);
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
			return this.putHelper_root(value);
		}
		else
		{
			return this.put_internal_noZeroCheck(key, value);
		}
	}
	
	private final U putHelper_root(U value)
	{
		final U old = this.root.getCargo();
		this.root.setCargo(value);
		this.reinsertEntry(this.root);
		return old;
	}
	
	@SuppressWarnings("unchecked")
	private final U putHelper_preExisting(LinkedNode<U> node, U value)
	{
		final U oldCargo = node.getCargo();
		node.setCargo(value);
		this.reinsertEntry((LinkedCargoNode<U>) node);
		return oldCargo;
	}
	
	private final U putHelper_transNode(LinkedNode<U> node, HalfByteArray nodeKey, U value)
	{
		final LinkedLeafNode<U> transNode = new LinkedLeafNode<U>(node, value);
		transNode.getParent().setChildAt(nodeKey.getFirstHalfByte(), transNode);
		this.addEntry(transNode);
		return null;
	}
	
	private final U putHelper_Leaf(LinkedNode<U> parentNode, HalfByteArray leafKeyChunk, U value, int nextChildIndex)
	{
		final LinkedLeafNode<U> result = new LinkedLeafNode<U>(parentNode, leafKeyChunk, value);
		parentNode.setChildAt(nextChildIndex, result);
		this.addEntry(result);
		return null;
	}

	/**
	 * the 'ZeroCheck' refers to the length of the key
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	protected final U put_internal_noZeroCheck(HalfByteArray key, U value)
	{
		final int firstHalfByte = key.getFirstHalfByte();

		if (this.numEntries == 0 || !this.root.hasChildAt(firstHalfByte))
		{
			final LinkedLeafNode<U> result = new LinkedLeafNode<U>(null, key, value);
			this.root.setChildAt(firstHalfByte, result);
			this.addEntry(result);
			return null;
		}
		else
		{
			LinkedNode<U> node = this.root.getChildAt(firstHalfByte);
			int keyIndex = 0;

			while (true)
			{
				HalfByteArray nodeKey = node.getKeyChunk();
				int matchResult = key.matchAgainst(nodeKey, keyIndex + 1, 1);

				if (matchResult == 0)	// no match, which means we need a splitter node
				{
					final int firstHalfByte_split = nodeKey.getFirstHalfByte();

					return this.put_internal_split(node, value, firstHalfByte_split, wrapSingleHalfByte(firstHalfByte_split), nodeKey.getSubArray(1), key.getSubArray(keyIndex + 1), node.getParent());
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
								return this.putHelper_preExisting(node, value);
							}
							else
							{
								return this.putHelper_transNode(node, nodeKey, value);
							}
						}
						case 1:
						{
							// childIndex should store cargo, after split (if needed)
							final int lastChildIndex = key.getHalfByteAt(keyIndex);

							if (node.hasChildAt(lastChildIndex))
							{
								final LinkedNode<U> child = node.getChildAt(lastChildIndex);
								final HalfByteArray childKey = child.getKeyChunk();

								if (childKey.length() == 1)
								{
									// perfect length, just check if can store cargo
									if (child.canHaveCargo())
									{
										return this.putHelper_preExisting(child, value);
									}
									else
									{
										return this.putHelper_transNode(child, childKey, value);
									}
								}
								else
								{
									final HalfByteArray restKey = childKey.getSubArray(1);

									final LinkedLeafNode<U> result = new LinkedLeafNode<U>(node, wrapSingleHalfByte(lastChildIndex), value);
									node.setChildAt(lastChildIndex, result);

									child.setKeyChunk(restKey);
									result.setChildAt(restKey.getFirstHalfByte(), child);
									child.setParent(result);

									this.addEntry(result);
									return null;
								}
							}
							else
							{
								final HalfByteArray split = HalfByteArray.getInstance(1);
								split.setHalfByteAt(0, lastChildIndex);

								return this.putHelper_Leaf(node, wrapSingleHalfByte(lastChildIndex), value, lastChildIndex);
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
								return this.putHelper_Leaf(node, key.getSubArray(keyIndex), value, nextChildIndex);
							}
						}
					}
				}
				else
				{
					// partial match
					final HalfByteArray var0 = nodeKey.getSubArray(0, matchResult, true);
					final HalfByteArray var1 = nodeKey.getSubArray(matchResult + 1);
					final HalfByteArray var2 = key.getSubArray(keyIndex + matchResult + 1);
					return this.put_internal_split(node, value, nodeKey.getFirstHalfByte(), var0, var1, var2, node.getParent());
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
	private final U put_internal_split(final LinkedNode<U> node, final U value, final int firstHalfByte_split, final HalfByteArray splitKey, final HalfByteArray oldKey, final HalfByteArray restKey, final LinkedNode<U> parent)
	{
		final LinkedNode<U> splitNode = new LinkedBranchNode<U>(parent, splitKey);
		final LinkedLeafNode<U> nextNode = new LinkedLeafNode<U>(splitNode, restKey, value);

		if (parent != null)
		{
			parent.setChildAt(firstHalfByte_split, splitNode);
		}
		else
		{
			this.root.setChildAt(firstHalfByte_split, splitNode);
		}

		splitNode.setChildAt(oldKey.getFirstHalfByte(), node);
		splitNode.setChildAt(restKey.getFirstHalfByte(), nextNode);
		node.setParent(splitNode);
		node.setKeyChunk(oldKey);

		this.addEntry(nextNode);

		return null;
	}
	
	private static final HalfByteArray wrapSingleHalfByte(int payload)
	{
		final HalfByteArray result = HalfByteArray.getInstance(1);
		result.setHalfByteAt(0, payload);
		return result;
	}

	public final U remove(int key)
	{
		return this.remove_internal_noZeroCheck(HalfByteArrayFactory.convertIntoArray(key));
	}
	
	public final U remove(long key)
	{
		return this.remove_internal_noZeroCheck(HalfByteArrayFactory.convertIntoArray(key));
	}
	
	public final U remove(char[] key)
	{
		if (this.numEntries == 0)
		{
			return null;
		}
		else if (key.length == 0)
		{
			return this.removeHelper_root();
		}
		else
		{
			return this.remove_internal_noZeroCheck(HalfByteArrayFactory.wrapIntoArray(key));
		}
	}
	
	/**
	 * if {@code key} is to be used frequently (or even more than once), it is recommended to cache the key in HalfByteArray form, instead of calling this method repeatedly.
	 * @param key
	 * @return
	 */
	public final U remove(CharList key)
	{
		if (this.numEntries == 0)
		{
			return null;
		}
		else if (key.size() == 0)
		{
			return this.removeHelper_root();
		}
		else
		{
			return this.remove_internal_noZeroCheck(key.toHalfByteArray());
		}
	}
	
	public final U remove(String key)
	{
		if (this.numEntries == 0)
		{
			return null;
		}
		else if (key.length() == 0)
		{
			return this.removeHelper_root();
		}
		else
		{
			return this.remove_internal_noZeroCheck(HalfByteArrayFactory.wrapIntoArray(key));
		}
	}

	public final U remove(HalfByteArray key)
	{
		if (this.numEntries == 0)
		{
			return null;
		}
		else if (key.length() == 0)
		{
			return this.removeHelper_root();
		}
		else
		{
			return this.remove_internal_noZeroCheck(key);
		}
	}
	
	private final U removeHelper_root()
	{
		final U result = this.root.getCargo();
		this.root.clearCargo();
		this.removeEntry(this.root);
		return result;
	}

	@SuppressWarnings("unchecked")
	private final U remove_internal_noZeroCheck(HalfByteArray key)
	{
		final U result;
		final int firstHalfByte = key.getFirstHalfByte();

		if (!this.root.hasChildAt(firstHalfByte))
		{
			return null;
		}
		else
		{
			LinkedNode<U> current = this.root.getChildAt(firstHalfByte);
			int keyIndex = 0;

			while (true)
			{
				final HalfByteArray curKeyChunk = current.getKeyChunk();
				final int curMatch = key.matchAgainst(curKeyChunk, keyIndex + 1, 1);

				if (curMatch != curKeyChunk.length())
				{
					return null;
				}
				else	// matches chunk, check childIndex
				{
					keyIndex += curMatch;

					if (keyIndex == key.length())
					{
						result = current.getCargo();

						switch (current.getNumChildren())
						{
							case 0:
							{
								break;
							}
							case 1:
							{
								// current needs to be folded into its child node
								int childIndex = 0;
								LinkedNode<U> child = current.getChildAt(childIndex);

								while (child == null)
								{
									child = current.getChildAt(++childIndex);
								}

								// concatenating keyChunks
								final int curKeyChunkLength = curKeyChunk.length();
								final HalfByteArray childKeyChunk = child.getKeyChunk();
								final int childKeyChunkLength = childKeyChunk.length();
								final HalfByteArray newKeyChunk = HalfByteArray.getInstance(curKeyChunkLength + childKeyChunkLength);

								for (int i = 0; i < curKeyChunkLength; i++)
								{
									newKeyChunk.setHalfByteAt(i, curKeyChunk.getHalfByteAt(i));
								}

								for (int i = 0; i < childKeyChunkLength; i++)
								{
									newKeyChunk.setHalfByteAt(i + curKeyChunkLength, childKeyChunk.getHalfByteAt(i));
								}

								// setting new keyChunk, and correcting parent/child links
								child.setKeyChunk(newKeyChunk);

								final LinkedNode<U> parent = current.getParent();

								if (parent != null)
								{
									parent.setChildAt(newKeyChunk.getFirstHalfByte(), child);
									child.setParent(parent);
								}

								this.removeEntry((LinkedCargoNode<U>) current);

								break;
							}
							default:
							{
								// current needs to be transformed into a branch node
								final LinkedBranchNode<U> transform = LinkedBranchNode.transformFromLeafToBranch((LinkedBranchNode<U>) current);

								final LinkedNode<U> parent = current.getParent();

								if (parent != null)
								{
									parent.setChildAt(curKeyChunk.getFirstHalfByte(), transform);
								}

								break;
							}
						}
						break;
					}
					else if (keyIndex < key.length())
					{
						final int childIndex = key.getHalfByteAt(keyIndex);

						if (current.hasChildAt(childIndex))
						{
							current = current.getChildAt(childIndex);
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

		return result;
	}

	public final void clear()
	{
		if (this.numEntries > 0)
		{
			this.root.clearAll();
			this.numEntries = 0;
			this.firstEntry = null;
			this.lastEntry = null;
		}
	}

	public final int size()
	{
		return this.numEntries;
	}

	public final boolean isEmpty()
	{
		return this.numEntries == 0;
	}

	public final boolean isNotEmpty()
	{
		return this.numEntries != 0;
	}

	public final CharList toHeirarchalCharList(int offset)
	{
		return this.root.toCharList(offset);
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

	@Override
	public final Iterator<Entry<HalfByteArray, U>> iterator()
	{
		return this.entriesIterator();
	}

	public final Iterator<Entry<HalfByteArray, U>> entriesIterator()
	{
		return new Iterators.EntryItsy<U>(this.firstEntry);
	}
	
	/**
	 * assumes keys are Strings
	 * @return
	 */
	public final Iterator<Entry<String, U>> stringKeyedEntriesIterator()
	{
		return new Iterators.StringKeyedEntryItsy<U>(this.firstEntry);
	}

	public final Iterator<HalfByteArray> keysIterator()
	{
		return new Iterators.KeysItsy(this.firstEntry);
	}

	public final Iterator<U> valuesIterator()
	{
		return new Iterators.ValuesItsy<U>(this.firstEntry);
	}
	
	/**
	 * Only use this iterator if all keys were of the same type, and that type was {@code int | long}. Otherwise, use the 'dirty' operator.
	 * @return
	 */
	public final Iterator<Entry<HalfByteArray, U>> naturallyOrderedEntriesIterator_forCleanKeys()
	{
		return new OrderingIterators.NatOrderedEntryItsy_Clean<U>(this.root, this.numEntries);
	}
	
	/**
	 * Caution: this iterator may return entries which were not originally keyed to an int, if the entry's keyLength == 8. To predict entry keyLength, divide byteLength by 4.
	 * @return
	 */
	public final Iterator<Entry<HalfByteArray, U>> naturallyOrderedEntriesIterator_forDirtyIntKeys()
	{
		return OrderingIterators.NatOrderedEntryItsy_Ints.getInstance(this.root, this.numEntries);
	}
	
	/**
	 * Caution: this iterator may return entries which were not originally keyed to a long, if the entry's keyLength == 16. To predict entry keyLength, divide byteLength by 4.
	 * @return
	 */
	public final Iterator<Entry<HalfByteArray, U>> naturallyOrderedEntriesIterator_forDirtyLongKeys()
	{
		return OrderingIterators.NatOrderedEntryItsy_Longs.getInstance(this.root, this.numEntries);
	}
	
	public final Iterator<Entry<HalfByteArray, U>> naturallyOrderedEntriesIterator_forDirtyIntAndLongKeys()
	{
		return new OrderingIterators.NatOrderedEntryItsy_IntAndLongs<U>(this.root, this.numEntries);
	}
	
	/*
	 * Map<HalfByteArray, U> stuff
	 */
	
	@Override
	public final U get(Object in)
	{
		if(in instanceof HalfByteArray)
		{
			return this.get((HalfByteArray) in);
		}
		else if(in instanceof String)
		{
			return this.get((String) in);
		}
		else
		{
			throw new IllegalArgumentException("Could not process key of type " + in.getClass());
		}
	}

	public final void putAll_ViaStringKeys(Map<String, ? extends U> inArg0)
	{
		for(Entry<String, ? extends U> entry: inArg0.entrySet())
		{
			this.put(entry.getKey(), entry.getValue());
		}
	}
	
	@Override
	public final void putAll(Map<? extends HalfByteArray, ? extends U> inArg0)
	{
		for(Entry<? extends HalfByteArray, ? extends U> entry: inArg0.entrySet())
		{
			this.put(entry.getKey(), entry.getValue());
		}
	}

	public final boolean containsKey(String in)
	{
		return this.containsKey_internal(in);
	}
	
	public final boolean containsKey(HalfByteArray in)
	{
		return this.containsKey_internal(in);
	}
	
	@Override
	public final boolean containsKey(Object in)
	{
		if(in instanceof HalfByteArray)
		{
			return this.containsKey_internal((HalfByteArray) in);
		}
		else if(in instanceof String)
		{
			return this.containsKey_internal((String) in);
		}
		else
		{
			throw new IllegalArgumentException("Could not process key of type " + in.getClass());
		}
	}

	@Override
	public final boolean containsValue(Object inArg0)
	{
		final Iterator<U> itsy = this.valuesIterator();
		
		while(itsy.hasNext())
		{
			if(itsy.next() == inArg0)
			{
				return true;
			}
		}
		
		return false;
	}

	@Override
	public final Set<Entry<HalfByteArray, U>> entrySet()
	{
		return new Sets.EntriesSet<U>(this);
	}
	
	public final Set<Entry<String, U>> stringKeyedEntrySet()
	{
		return new Sets.StringKeyedEntriesSet<U>(this);
	}

	@Override
	public final Set<HalfByteArray> keySet()
	{
		return new Sets.KeysSet(this);
	}
	
	public final Set<U> valueSet()
	{
		return new Sets.ValuesSet<U>(this);
	}

	@Override
	public final U remove(Object key)
	{
		if(key instanceof HalfByteArray)
		{
			return this.remove((HalfByteArray) key);
		}
		else if(key instanceof String)
		{
			return this.remove((String) key);
		}
		else
		{
			throw new IllegalArgumentException("Could not process key of type " + key.getClass());
		}
	}

	@Override
	public final Collection<U> values()
	{
		return this.valueSet();
	}
	
	public final LinkedRadixMap<Integer> collateAllChildrenMasks()
	{
		final LinkedRadixMap<Integer> result = new LinkedRadixMap<Integer>();
		final SingleLinkedList<LinkedNode<U>> queue = new SingleLinkedList<LinkedNode<U>>();
		queue.add(this.root);
		
		while(queue.isNotEmpty())
		{
			final LinkedNode<U> current = queue.pop();
			int childrenMask = 0;
			
			for(int i = 0; i < 16; i++)
			{
				if(current.hasChildAt(i))
				{
					queue.add(current.getChildAt(i));
					childrenMask |= 1 << i;
				}
			}
			
			Integer value = result.get(childrenMask);
			
			if(value == null)
			{
				result.put(childrenMask, 1);
			}
			else
			{
				result.put(childrenMask, value + 1);
			}
		}
		
		return result;
	}
}
