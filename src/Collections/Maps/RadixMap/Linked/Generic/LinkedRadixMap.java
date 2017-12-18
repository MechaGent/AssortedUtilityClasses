package Collections.Maps.RadixMap.Linked.Generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import Collections.Lists.CharList.CharList;
import Collections.Maps.RadixMap.Linked.Generic.Iterators.EntriesItsy;
import Collections.Maps.RadixMap.Linked.Generic.Iterators.KeysItsy;
import Collections.Maps.RadixMap.Linked.Generic.Iterators.StringKeyedEntryItsy;
import Collections.Maps.RadixMap.Linked.Generic.Iterators.ValuesItsy;
import Collections.Maps.RadixMap.Linked.Generic.OrderingIterators.NatOrderedEntryItsy_Clean;
import Collections.Maps.RadixMap.Linked.Generic.OrderingIterators.NatOrderedEntryItsy_IntAndLongs;
import Collections.Maps.RadixMap.Linked.Generic.OrderingIterators.NatOrderedEntryItsy_Ints;
import Collections.Maps.RadixMap.Linked.Generic.OrderingIterators.NatOrderedEntryItsy_Longs;
import Collections.Maps.RadixMap.Linked.Generic.Sets.EntriesSet;
import Collections.Maps.RadixMap.Linked.Generic.Sets.KeysSet;
import Collections.Maps.RadixMap.Linked.Generic.Sets.StringKeyedEntriesSet;
import Collections.Maps.RadixMap.Linked.Generic.Sets.ValuesSet;
import Collections.PackedArrays.HalfByte.HalfByteArray;
import Collections.PackedArrays.HalfByte.HalfByteArrayFactory;

public class LinkedRadixMap<U> implements Map<HalfByteArray, U>, Iterable<Entry<HalfByteArray, U>>
{
	private final LinkedLeafNode<U> root;
	private int numEntries;

	private LinkedLeafNode<U> firstEntry;
	private LinkedLeafNode<U> lastEntry;

	public LinkedRadixMap()
	{
		this.root = new LinkedLeafNode<U>();
		this.numEntries = 0;
		this.firstEntry = null;
		this.lastEntry = null;
	}

	private final void addEntry(LinkedLeafNode<U> in)
	{
		// System.out.println("adding entry: " + in.toCharList(0).toString());

		/*
		if(in.getPrev() != null)
		{
			throw new IllegalArgumentException();
		}
		
		if(in.getNext() != null)
		{
			throw new IllegalArgumentException();
		}
		*/

		if (this.numEntries == 0)
		{
			this.firstEntry = in;
			this.lastEntry = in;
			this.numEntries = 1;
		}
		else
		{
			this.lastEntry.setNext(in);
			in.setPrev(this.lastEntry);
			// in.setNext(null);
			this.lastEntry = in;
			this.numEntries++;
		}

		/*
		if(this.numEntries > 1)
		{
			final HalfByteArray prevKey = this.lastEntry.getPrev().backtraceKey();
			final HalfByteArray currKey = this.lastEntry.backtraceKey();
			
			if(prevKey.equals(currKey))
			{
				throw new IllegalArgumentException("\r\n\tprevKey(" + prevKey.length() + "): " + prevKey.interpretAsCharString() + "\r\n\tcurrKey(" + currKey.length() + "): " + currKey.interpretAsCharString());
			}
		}
		*/
	}

	private final void removeEntry(LinkedLeafNode<U> in)
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
					final LinkedLeafNode<U> before = in.getPrev();
					final LinkedLeafNode<U> after = in.getNext();

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

	private final void reinsertEntry(LinkedLeafNode<U> in)
	{
		// System.out.println("reinserting entry: " + in.toCharList(0).toString());

		switch (this.numEntries)
		{
			case 0:
			{
				this.firstEntry = in;
				this.lastEntry = in;

				/*
				if(in.getPrev() != null)
				{
					throw new IllegalArgumentException();
				}
				
				if(in.getNext() != null)
				{
					throw new IllegalArgumentException();
				}
				*/

				this.numEntries = 1;
				break;
			}
			case 1:
			{
				if (this.firstEntry != in)
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
					this.lastEntry = in;
					this.firstEntry.setNext(in);
					this.firstEntry.setPrev(null);
					in.setPrev(this.firstEntry);
					in.setNext(null);
				}
				else if (this.lastEntry != in)
				{
					throw new NullPointerException();
				}

				break;
			}
			default:
			{
				if (this.firstEntry == in)
				{
					this.firstEntry = in.getNext();
					this.lastEntry.setNext(in);
					in.setPrev(this.lastEntry);
					this.lastEntry = in;
					this.firstEntry.setPrev(null);
					in.setNext(null);
				}
				else if (this.lastEntry != in)
				{
					final LinkedLeafNode<U> before = in.getPrev();
					final LinkedLeafNode<U> after = in.getNext();

					if (before != null)
					{
						before.setNext(after);
					}

					if (after != null)
					{
						after.setPrev(before);
						in.setNext(null);
					}

					this.lastEntry.setNext(in);
					in.setPrev(this.lastEntry);
					this.lastEntry = in;
				}

				break;
			}
		}

		/*
		if(this.numEntries > 1)
		{
			final HalfByteArray prevKey = this.lastEntry.getPrev().backtraceKey();
			final HalfByteArray currKey = this.lastEntry.backtraceKey();
			
			if(prevKey.equals(currKey))
			{
				throw new IllegalArgumentException("\r\n\tprevKey(" + prevKey.length() + "): " + prevKey.interpretAsCharString() + "\r\n\tcurrKey(" + currKey.length() + "): " + currKey.interpretAsCharString() + "\r\n\tcaseNum: " + caseNum);
			}
		}
		*/
	}
	
	public final boolean containsKey(int key)
	{
		if(this.numEntries == 0)
		{
			return false;
		}
		else
		{
			return this.containsKey_internal_noZeroChecks(HalfByteArrayFactory.convertIntoArray(key));
		}
	}
	
	public final boolean containsKey(long key)
	{
		if(this.numEntries == 0)
		{
			return false;
		}
		else
		{
			return this.containsKey_internal_noZeroChecks(HalfByteArrayFactory.convertIntoArray(key));
		}
	}

	public final boolean containsKey(char[] key)
	{
		if (this.numEntries == 0)
		{
			return false;
		}
		else if (key.length == 0)
		{
			return this.root.hasCargo();
		}
		else
		{
			return this.containsKey_internal_noZeroChecks(HalfByteArrayFactory.wrapIntoArray(key));
		}
	}

	public final boolean containsKey(String key)
	{
		if (this.numEntries == 0)
		{
			return false;
		}
		else if (key.length() == 0)
		{
			return this.root.hasCargo();
		}
		else
		{
			return this.containsKey_internal_noZeroChecks(HalfByteArrayFactory.wrapIntoArray(key));
		}
	}

	public final boolean containsKey(HalfByteArray key)
	{
		if (this.numEntries == 0)
		{
			return false;
		}
		else if (key.length() == 0)
		{
			return this.root.hasCargo();
		}
		else
		{
			return this.containsKey_internal_noZeroChecks(key);
		}
	}

	@Override
	public final boolean containsKey(Object in)
	{
		if (in instanceof HalfByteArray)
		{
			return this.containsKey((HalfByteArray) in);
		}
		else if (in instanceof String)
		{
			return this.containsKey((String) in);
		}
		else
		{
			throw new IllegalArgumentException("Unhandleable type: " + in.getClass());
		}
	}

	private final boolean containsKey_internal_noZeroChecks(HalfByteArray key)
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
				else // matches chunk, check childIndex
				{
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

	@Override
	public final U get(Object in)
	{
		if (in instanceof HalfByteArray)
		{
			return this.get((HalfByteArray) in);
		}
		else if (in instanceof String)
		{
			return this.get((String) in);
		}
		else
		{
			throw new IllegalArgumentException("Unhandleable type: " + in.getClass());
		}
	}

	private final U get_internal_noZeroCheck(HalfByteArray key)
	{
		return this.getNode_internal_noZeroCheck(key).getCargo();
	}

	@SuppressWarnings("unchecked")
	private final LinkedLeafNode<U> getNode_internal_noZeroCheck(HalfByteArray key)
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
				else // matches chunk, check childIndex
				{
					keyIndex += curMatch;

					if (keyIndex == keyLength)
					{
						if (current.canHaveCargo())
						{
							return (LinkedLeafNode<U>) current;
						}
						else
						{
							return LinkedLeafNode.getEmptyLinkedLeafNode();
						}
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

		return LinkedLeafNode.getEmptyLinkedLeafNode();
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
			return this.putHelper_rootValue(value);
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
			return this.putHelper_rootValue(value);
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
			return this.putHelper_rootValue(value);
		}
		else
		{
			return this.put_internal_noZeroCheck(HalfByteArrayFactory.wrapIntoArray(key), value);
		}
	}

	@Override
	public final U put(HalfByteArray key, U value)
	{
		if (key.length() == 0)
		{
			return this.putHelper_rootValue(value);
		}
		else
		{
			return this.put_internal_noZeroCheck(key, value);
		}
	}

	public final U put(Entry<? extends HalfByteArray, ? extends U> entry)
	{
		if (entry instanceof LinkedLeafNode)
		{
			@SuppressWarnings("unchecked")
			final LinkedLeafNode<U> cast = (LinkedLeafNode<U>) entry;
			return this.put(cast.backtraceKey(), cast.getCargo());
		}
		else
		{
			return this.put(entry.getKey(), entry.getValue());
		}
	}
	
	public final void put(LinkedLeafNode<U> entry)
	{
		this.put(entry.getKey(), entry.getValue());
	}

	private final U putHelper_rootValue(U value)
	{
		if (this.root.hasCargo())
		{
			final U old = this.root.getCargo();
			this.root.setCargo(value);
			this.reinsertEntry(this.root);
			return old;
		}
		else
		{
			this.root.setCargo(value);
			this.reinsertEntry(this.root);
			return null;
		}
	}

	/**
	 * does not return anything, as it would only return {@code null}.
	 * 
	 * @param parentNode
	 * @param leafKeyChunk
	 * @param value
	 * @param nextChildIndex
	 */
	private final void putHelper_createLeaf(LinkedNode<U> parentNode, HalfByteArray leafKeyChunk, U value, int nextChildIndex)
	{
		final LinkedLeafNode<U> result = new LinkedLeafNode<U>(parentNode, leafKeyChunk, value);
		parentNode.setChildAt(nextChildIndex, result);
		this.addEntry(result);
	}

	private final U putHelper_ConvertToStorageNode(LinkedNode<U> node, U value)
	{
		if (node.canHaveCargo())
		{
			final LinkedLeafNode<U> cast = (LinkedLeafNode<U>) node;

			if (cast.hasCargo())
			{
				final U oldCargo = cast.getCargo();
				cast.setCargo(value);
				this.reinsertEntry(cast);
				return oldCargo;
			}
			else
			{
				cast.setCargo(value);
				this.reinsertEntry(cast);
				return null;
			}
		}
		else
		{
			// System.out.println(Integer.toBinaryString(node.childrenAndState));
			final LinkedLeafNode<U> transNode = new LinkedLeafNode<U>(node, value);
			final LinkedNode<U> parent = transNode.getParent();

			if (parent != null)
			{
				parent.setChildAt(node.getFirstKeyHalfByte(), transNode);
			}

			this.addEntry(transNode);
			// this.reinsertEntry(transNode);
			return null;
		}
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
				final HalfByteArray nodeKey = node.getKeyChunk();
				final int matchResult = key.matchAgainst(nodeKey, keyIndex + 1, 1);

				if (matchResult == 0) // no match, which means we need a splitter node
				{
					final int firstHalfByte_split = nodeKey.getFirstHalfByte();

					return this.put_internal_split(node, value, firstHalfByte_split, wrapSingleHalfByte(firstHalfByte_split), nodeKey.getSubArray(1), key.getSubArray(keyIndex + 1), node.getParent());
				}
				else if (matchResult == nodeKey.length()) // full match so far, check if done
				{
					keyIndex += matchResult;

					switch (key.length() - keyIndex)
					{
						case 0: // current node should store cargo
						{
							return this.putHelper_ConvertToStorageNode(node, value);
						}
						case 1: // childIndex should store cargo, after split (if needed)
						{
							final int lastChildIndex = key.getHalfByteAt(keyIndex);

							if (node.hasChildAt(lastChildIndex))
							{
								final LinkedNode<U> child = node.getChildAt(lastChildIndex);
								final HalfByteArray childKey = child.getKeyChunk();

								if (childKey.length() == 1) // perfect length, just check if can store cargo
								{
									return this.putHelper_ConvertToStorageNode(child, value);
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
								this.putHelper_createLeaf(node, wrapSingleHalfByte(lastChildIndex), value, lastChildIndex);
								return null;
							}
						}
						default:
						{
							final int nextChildIndex = key.getHalfByteAt(keyIndex);

							if (node.hasChildAt(nextChildIndex))
							{
								/*
								 * This is where the loop loops
								 */
								node = node.getChildAt(nextChildIndex);
								break;
							}
							else
							{
								// System.out.println(matchResult + ", " + keyIndex + ", " + key.length() + "\r\n\t" + key.getSubArray(keyIndex).interpretAsCharString());
								this.putHelper_createLeaf(node, key.getSubArray(keyIndex), value, nextChildIndex);
								return null;
							}
						}
					}
				}
				else // partial match
				{
					if (keyIndex + matchResult == key.length())
					{
						final HalfByteArray var0 = nodeKey.getSubArray(0, matchResult, false); // can't just use 'key' because it needs to have one less element
						final HalfByteArray var1 = nodeKey.getSubArray(matchResult);

						final LinkedNode<U> parent = node.getParent();
						final LinkedLeafNode<U> stub = new LinkedLeafNode<U>(parent, var0, value);

						if (parent == null) // apparently this is a super-edge case
						{
							this.root.setChildAt(var0.getFirstHalfByte(), stub);
						}
						else
						{
							parent.setChildAt(var0.getFirstHalfByte(), stub);
						}

						this.addEntry(stub);

						node.setKeyChunk(var1);
						node.setParent(stub);
						stub.setChildAt(var1.getFirstHalfByte(), node);
						return null;
					}
					else
					{
						final HalfByteArray var0 = nodeKey.getSubArray(0, matchResult, true);
						final HalfByteArray var1 = nodeKey.getSubArray(matchResult + 1);
						final HalfByteArray var2 = key.getSubArray(keyIndex + matchResult + 1);
						return this.put_internal_split(node, value, nodeKey.getFirstHalfByte(), var0, var1, var2, node.getParent());
					}
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
		final LinkedNode<U> splitNode = new LinkedNode<U>(parent, splitKey);
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
		if (this.numEntries == 0)
		{
			return null;
		}
		else
		{
			return this.remove_internal_noZeroCheck(HalfByteArrayFactory.convertIntoArray(key));
		}
	}

	public final U remove(long key)
	{
		if (this.numEntries == 0)
		{
			return null;
		}
		else
		{
			return this.remove_internal_noZeroCheck(HalfByteArrayFactory.convertIntoArray(key));
		}
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
	 * 
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

	@Override
	public final U remove(Object in)
	{
		if (in instanceof HalfByteArray)
		{
			return this.remove((HalfByteArray) in);
		}
		else if (in instanceof String)
		{
			return this.remove((String) in);
		}
		else
		{
			throw new IllegalArgumentException("Unhandleable type: " + in.getClass());
		}
	}

	private final U removeHelper_root()
	{
		final U result = this.root.getCargo();
		this.root.clearCargo();
		this.removeEntry(this.root);
		return result;
	}

	private final U remove_internal_noZeroCheck(HalfByteArray key)
	{
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
				else // matches chunk, check childIndex
				{
					keyIndex += curMatch;

					if (keyIndex == key.length())
					{
						final LinkedLeafNode<U> cast = (LinkedLeafNode<U>) current;

						switch (current.getNumChildren())
						{
							case 0:
							{
								break;
							}
							case 1: // current needs to be folded into its child node
							{
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

								this.removeEntry(cast);
								break;
							}
							default: // current needs to be transformed into a branch node
							{
								final LinkedNode<U> parent = current.getParent();

								if (parent != null)
								{
									parent.setChildAt(curKeyChunk.getFirstHalfByte(), cast.transformIntoBranchNode());
								}
								else
								{
									this.root.setChildAt(curKeyChunk.getFirstHalfByte(), cast.transformIntoBranchNode());
								}

								this.removeEntry(cast);
								break;
							}
						}

						return cast.getCargo();
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
		return this.root.toCharList(offset, "root");
	}

	public final CharList toFlatCharList(int offset)
	{
		final CharList result = new CharList();

		switch (this.numEntries)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				addFirstEntryStuff(result, offset, this.firstEntry);
				break;
			}
			case 2:
			{
				addFirstEntryStuff(result, offset, this.firstEntry);

				result.addNewIndentedLine(offset);
				result.add('{');
				result.add(this.lastEntry.backtraceKey().toCharList(), true);
				result.add(", ");
				result.addAsString(this.lastEntry.getCargo());
				result.add('}');
				break;
			}
			default:
			{
				addFirstEntryStuff(result, offset, this.firstEntry);
				LinkedLeafNode<U> current = this.firstEntry.getNext();

				for (int i = 1; i < this.numEntries; i++)
				{
					result.addNewIndentedLine(offset);
					result.add('{');
					result.add(current.backtraceKey().toCharList(), true);
					result.add(", ");
					result.addAsString(current.getCargo());
					result.add('}');
					current = current.getNext();
				}
				break;
			}
		}

		return result;
	}

	private static final <U> void addFirstEntryStuff(CharList result, int offset, LinkedLeafNode<U> firstEntry)
	{
		result.add('\t', offset);
		result.add('{');
		result.add(firstEntry.backtraceKey().toCharList(), true);
		result.add(", ");
		result.addAsString(firstEntry.getCargo());
		result.add('}');
	}

	public final LinkedRadixMap<U> copyMap()
	{
		final LinkedRadixMap<U> result = new LinkedRadixMap<U>();
		result.putAll(this);
		return result;
	}

	@Override
	public final EntriesItsy<U> iterator()
	{
		return new EntriesItsy<U>(this.firstEntry);
	}

	public final EntriesItsy<U> entriesIterator()
	{
		return new EntriesItsy<U>(this.firstEntry);
	}

	public final StringKeyedEntryItsy<U> stringKeyedEntriesIterator()
	{
		return new StringKeyedEntryItsy<U>(this.firstEntry);
	}

	public final KeysItsy keysIterator()
	{
		return new KeysItsy(this.firstEntry);
	}

	public final Iterators.ValuesItsy<U> valuesIterator()
	{
		return new ValuesItsy<U>(this.firstEntry);
	}

	/**
	 * Only use this iterator if all keys were of the same type, and that type was {@code int | long}. Otherwise, use the 'dirty' operator.
	 * 
	 * @return
	 */
	public final NatOrderedEntryItsy_Clean<U> naturallyOrderedEntriesIterator_forCleanKeys()
	{
		return new NatOrderedEntryItsy_Clean<U>(this.root, this.numEntries);
	}

	/**
	 * Caution: this iterator may return entries which were not originally keyed to an int, if the entry's keyLength == 8. To predict entry keyLength, divide byteLength by 4.
	 * 
	 * @return
	 */
	public final NatOrderedEntryItsy_Ints<U> naturallyOrderedEntriesIterator_forDirtyIntKeys()
	{
		return NatOrderedEntryItsy_Ints.getInstance(this.root, this.numEntries);
	}

	/**
	 * Caution: this iterator may return entries which were not originally keyed to a long, if the entry's keyLength == 16. To predict entry keyLength, divide byteLength by 4.
	 * 
	 * @return
	 */
	public final NatOrderedEntryItsy_Longs<U> naturallyOrderedEntriesIterator_forDirtyLongKeys()
	{
		return NatOrderedEntryItsy_Longs.getInstance(this.root, this.numEntries);
	}

	public final NatOrderedEntryItsy_IntAndLongs<U> naturallyOrderedEntriesIterator_forDirtyIntAndLongKeys()
	{
		return new NatOrderedEntryItsy_IntAndLongs<U>(this.root, this.numEntries);
	}

	@Override
	public final boolean containsValue(Object inArg0)
	{
		if (this.numEntries != 0)
		{
			LinkedLeafNode<U> current = this.firstEntry;

			for (int i = 0; i < this.numEntries; i++)
			{
				if (current.getCargo() == inArg0)
				{
					return true;
				}
				else
				{
					current = current.getNext();
				}
			}
		}

		return false;
	}

	@Override
	public final EntriesSet<U> entrySet()
	{
		return new EntriesSet<U>(this);
	}

	public final StringKeyedEntriesSet<U> stringKeyedEntrySet()
	{
		return new StringKeyedEntriesSet<U>(this);
	}

	@Override
	public final KeysSet keySet()
	{
		return new KeysSet(this);
	}

	@Override
	public final Collection<U> values()
	{
		return new ValuesSet<U>(this);
	}

	public final ValuesSet<U> valueSet()
	{
		return new ValuesSet<U>(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void putAll(Map<? extends HalfByteArray, ? extends U> in)
	{
		if (in instanceof LinkedRadixMap)
		{
			this.putAll((LinkedRadixMap<U>) in);
		}
		else
		{
			for (Entry<? extends HalfByteArray, ? extends U> entry : in.entrySet())
			{
				this.put(entry.getKey(), entry.getValue());
			}
		}
	}

	public final void putAll(LinkedRadixMap<U> in)
	{
		if (in.numEntries > 0)
		{
			LinkedLeafNode<U> current = in.firstEntry;

			for (int i = 0; i < in.numEntries; i++)
			{
				this.put(current.backtraceKey(), current.getCargo());
				current = current.getNext();
			}
		}
	}

	/**
	 * 
	 * @param maps
	 * @param smallestSize
	 *            the size of the smallest map, which should be in the first index
	 * @return
	 */
	public static final <U> ArrayList<U> getIntersection_asArrayList(LinkedRadixMap<U>[] maps, int smallestSize)
	{	
		if(smallestSize == 0)
		{
			return new ArrayList<U>();
		}
		
		switch(maps.length)
		{
			case 0:
			{
				return new ArrayList<U>();
			}
			case 1:
			{
				final ArrayList<U> intersection = new ArrayList<U>(maps[0].size());
				final EntriesItsy<U> itsy = maps[0].entriesIterator();

				while(itsy.hasNext())
				{
					final LinkedLeafNode<U> entry = itsy.next();
					
					intersection.add(entry.getCargo());
				}
				
				return intersection;
			}
			default:
			{
				break;
			}
		}
		
		final ArrayList<U> intersection = new ArrayList<U>(maps[0].size());
		final EntriesItsy<U> itsy = maps[0].entriesIterator();

		while(itsy.hasNext())
		{
			final LinkedLeafNode<U> entry = itsy.next();
			final HalfByteArray key = entry.getKey();
			
			check: if(maps[1].containsKey(key))
			{
				for(int i = 2; i < maps.length; i++)
				{
					if(!maps[i].containsKey(key))
					{
						break check;
					}
				}
				
				intersection.add(entry.getCargo());
			}
		}

		return intersection;
	}

	/**
	 * 
	 * @param maps
	 * @param smallestSize
	 *            the size of the smallest map, which should be in the first index
	 * @return
	 */
	public static final <U> LinkedRadixMap<U> getIntersection_asLinkedRadixMap(LinkedRadixMap<U>[] maps, int smallestSize)
	{
		if(smallestSize == 0)
		{
			return new LinkedRadixMap<U>();
		}
		
		switch(maps.length)
		{
			case 0:
			{
				return new LinkedRadixMap<U>();
			}
			case 1:
			{
				return maps[0].copyMap();
			}
			default:
			{
				break;
			}
		}
		
		final LinkedRadixMap<U> intersection = new LinkedRadixMap<U>();
		final EntriesItsy<U> itsy = maps[0].entriesIterator();

		while(itsy.hasNext())
		{
			final LinkedLeafNode<U> entry = itsy.next();
			final HalfByteArray key = entry.getKey();
			
			check: if(maps[1].containsKey(key))
			{
				for(int i = 2; i < maps.length; i++)
				{
					if(!maps[i].containsKey(key))
					{
						break check;
					}
				}
				
				intersection.put(key, entry.getCargo());
			}
		}

		return intersection;
	}
}
