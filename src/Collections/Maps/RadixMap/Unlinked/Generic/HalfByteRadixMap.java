package Collections.Maps.RadixMap.Unlinked.Generic;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import Collections.Lists.CharList.CharList;
import Collections.Maps.RadixMap.Unlinked.Generic.Iterators.EntriesItsy;
import Collections.Maps.RadixMap.Unlinked.Generic.Iterators.KeysItsy;
import Collections.Maps.RadixMap.Unlinked.Generic.Iterators.StringKeyedEntryItsy;
import Collections.Maps.RadixMap.Unlinked.Generic.Iterators.ValuesItsy;
import Collections.Maps.RadixMap.Unlinked.Generic.OrderingIterators.NatOrderedEntryItsy_Clean;
import Collections.Maps.RadixMap.Unlinked.Generic.OrderingIterators.NatOrderedEntryItsy_IntAndLongs;
import Collections.Maps.RadixMap.Unlinked.Generic.OrderingIterators.NatOrderedEntryItsy_Ints;
import Collections.Maps.RadixMap.Unlinked.Generic.OrderingIterators.NatOrderedEntryItsy_Longs;
import Collections.Maps.RadixMap.Unlinked.Generic.Sets.EntriesSet;
import Collections.Maps.RadixMap.Unlinked.Generic.Sets.KeysSet;
import Collections.Maps.RadixMap.Unlinked.Generic.Sets.StringKeyedEntriesSet;
import Collections.Maps.RadixMap.Unlinked.Generic.Sets.ValuesSet;
import Collections.PackedArrays.HalfByte.HalfByteArray;
import Collections.PackedArrays.HalfByte.HalfByteArrayFactory;

public class HalfByteRadixMap<U> implements Map<HalfByteArray, U>, Iterable<Entry<HalfByteArray, U>>
{
	private final LeafNode<U> root;
	private int numEntries;

	public HalfByteRadixMap()
	{
		this.root = new LeafNode<U>();
		this.numEntries = 0;
	}

	public final boolean containsKey(char[] key)
	{
		if(this.numEntries == 0)
		{
			return false;
		}
		else if(key.length == 0)
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
			Node<U> current = this.root.getChildAt(firstHalfByte);
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
				else // matches chunk, check childIndex
				{
					keyIndex += curMatch;

					if (keyIndex == keyLength)
					{
						return ((LeafNode<U>) current).getCargo();
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
		//throw new PartyGoneOutOfBoundsException();
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
		final U result;
		
		if (key.length() == 0)
		{
			result = this.putHelper_rootValue(value);
		}
		else
		{
			result = this.put_internal_noZeroCheck(HalfByteArrayFactory.wrapIntoArray(key), value);
		}
		
		return result;
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

	@SuppressWarnings("unchecked")
	public final U put(Entry<? extends HalfByteArray, ? extends U> entry)
	{
		if (entry instanceof LeafNode)
		{
			return this.put((LeafNode<U>) entry);
		}
		else
		{
			return this.put(entry.getKey(), entry.getValue());
		}
	}

	private final U put(LeafNode<U> entry)
	{
		return this.put(entry.backtraceKey(), entry.getCargo());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final void putAll(Map<? extends HalfByteArray, ? extends U> in)
	{
		if (in instanceof HalfByteRadixMap)
		{
			this.putAll((HalfByteRadixMap<U>) in);
		}
		else
		{
			for (Entry<? extends HalfByteArray, ? extends U> entry : in.entrySet())
			{
				this.put(entry.getKey(), entry.getValue());
			}
		}
	}

	public final void putAll(HalfByteRadixMap<U> in)
	{
		if (in.numEntries > 0)
		{
			final EntriesItsy<U> itsy = this.entriesIterator();

			while (itsy.hasNext())
			{
				final LeafNode<U> current = itsy.next();
				this.put(current);
			}
		}
	}

	private final U putHelper_rootValue(U value)
	{
		if (this.root.hasCargo())
		{
			final U old = this.root.getCargo();
			this.root.setCargo(value);
			return old;
		}
		else
		{
			this.root.setCargo(value);
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
	private final void putHelper_createLeaf(Node<U> parentNode, HalfByteArray leafKeyChunk, U value, int nextChildIndex)
	{
		final LeafNode<U> result = new LeafNode<U>(parentNode, leafKeyChunk, value);
		parentNode.setChildAt(nextChildIndex, result);
		this.numEntries += 1;
	}

	private final U putHelper_ConvertToStorageNode(Node<U> node, U value)
	{
		if (node.hasCargo())
		{
			final LeafNode<U> cast = (LeafNode<U>) node;
			final U oldCargo = cast.getCargo();
			cast.setCargo(value);
			return oldCargo;
		}
		else
		{
			final LeafNode<U> transNode = new LeafNode<U>(node, value);
			transNode.setChildOfParent(node.getFirstKeyHalfByte(), transNode);
			this.numEntries += 1;
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
			final LeafNode<U> result = new LeafNode<U>(null, key, value);
			this.root.setChildAt(firstHalfByte, result);
			this.numEntries += 1;
			return null;
		}
		else
		{
			Node<U> node = this.root.getChildAt(firstHalfByte);
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
								final Node<U> child = node.getChildAt(lastChildIndex);
								final HalfByteArray childKey = child.getKeyChunk();

								if (childKey.length() == 1) // perfect length, just check if can store cargo
								{
									return this.putHelper_ConvertToStorageNode(child, value);
								}
								else
								{
									final HalfByteArray restKey = childKey.getSubArray(1);
									final LeafNode<U> result = new LeafNode<U>(node, wrapSingleHalfByte(lastChildIndex), value);
									node.setChildAt(lastChildIndex, result);
									child.setKeyChunk(restKey);
									result.setChildAt(restKey.getFirstHalfByte(), child);
									child.setParent(result);
									this.numEntries += 1;
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

						final Node<U> parent = node.getParent();
						final LeafNode<U> stub = new LeafNode<U>(parent, var0, value);

						if (parent != null)
						{
							parent.setChildAt(var0.getFirstHalfByte(), stub);
						}
						else	// apparently this is a super-edge case
						{
							this.root.setChildAt(var0.getFirstHalfByte(), stub);
						}

						this.numEntries += 1;

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
	private final U put_internal_split(final Node<U> node, final U value, final int firstHalfByte_split, final HalfByteArray splitKey, final HalfByteArray oldKey, final HalfByteArray restKey, final Node<U> parent)
	{
		final Node<U> splitNode = new Node<U>(parent, splitKey);
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
		splitNode.setChildAt(restKey.getFirstHalfByte(), nextNode);
		node.setParent(splitNode);
		node.setKeyChunk(oldKey);

		this.numEntries += 1;

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
		this.numEntries -= 1;
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
			Node<U> current = this.root.getChildAt(firstHalfByte);
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
						final LeafNode<U> cast = (LeafNode<U>) current;

						switch (current.getNumChildren())
						{
							case 0:
							{
								break;
							}
							case 1: // current needs to be folded into its child node
							{
								int childIndex = 0;
								Node<U> child = current.getChildAt(childIndex);

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
								final Node<U> parent = current.getParent();

								if (parent != null)
								{
									parent.setChildAt(newKeyChunk.getFirstHalfByte(), child);
									child.setParent(parent);
								}

								this.numEntries -= 1;
								break;
							}
							default: // current needs to be transformed into a branch node
							{
								final Node<U> parent = current.getParent();

								if (parent != null)
								{
									parent.setChildAt(curKeyChunk.getFirstHalfByte(), cast.transformIntoBranchNode());
								}
								else
								{
									this.root.setChildAt(curKeyChunk.getFirstHalfByte(), cast.transformIntoBranchNode());
								}

								this.numEntries -= 1;
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

		if (this.numEntries > 0)
		{
			final EntriesItsy<U> itsy = this.entriesIterator();
			boolean isFirst = true;

			while (itsy.hasNext())
			{
				final LeafNode<U> entry = itsy.next();

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
				result.add(entry.backtraceKey().toCharList(), true);
				result.add(", ");
				result.addAsString(entry.getCargo());
				result.add('}');
			}
		}

		return result;
	}

	public final HalfByteRadixMap<U> copyMap()
	{
		final HalfByteRadixMap<U> result = new HalfByteRadixMap<U>();
		result.putAll(this);
		return result;
	}

	@Override
	public final EntriesItsy<U> iterator()
	{
		return new EntriesItsy<U>(this.root, this.numEntries);
	}

	public final EntriesItsy<U> entriesIterator()
	{
		return new EntriesItsy<U>(this.root, this.numEntries);
	}

	public final StringKeyedEntryItsy<U> stringKeyedEntriesIterator()
	{
		return new StringKeyedEntryItsy<U>(this.root, this.numEntries);
	}

	public final KeysItsy keysIterator()
	{
		return new KeysItsy(this.root, this.numEntries);
	}

	public final ValuesItsy<U> valuesIterator()
	{
		return new ValuesItsy<U>(this.root, this.numEntries);
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
		if (this.numEntries > 0)
		{
			final EntriesItsy<U> itsy = this.entriesIterator();

			while (itsy.hasNext())
			{
				if (itsy.next().getCargo() == inArg0)
				{
					return true;
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
}
