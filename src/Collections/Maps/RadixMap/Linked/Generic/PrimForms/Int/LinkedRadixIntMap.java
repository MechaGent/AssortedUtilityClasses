package Collections.Maps.RadixMap.Linked.Generic.PrimForms.Int;

import java.util.Iterator;

import Collections.Lists.CharList.CharList;
import Collections.PackedArrays.HalfByte.HalfByteArray;
import Collections.PackedArrays.HalfByte.HalfByteArrayFactory;

public class LinkedRadixIntMap implements Iterable<KeyedInt>
{
	private static final int pseudoNull = -69;
	
	private final LinkedIntLeafNode root;
	private int numEntries;
	
	private LinkedIntLeafNode firstEntry;
	private LinkedIntLeafNode lastEntry;
	
	public LinkedRadixIntMap()
	{
		this.root = new LinkedIntLeafNode();
		this.numEntries = 0;
		
		this.firstEntry = null;
		this.lastEntry = null;
	}
	
	/**
	 * for use by iterator only
	 * @return
	 */
	final LinkedIntLeafNode getFirstEntry()
	{
		return this.firstEntry;
	}
	
	private final void addEntry(LinkedIntLeafNode in)
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

	private final void removeEntry(LinkedIntLeafNode in)
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
					final LinkedIntLeafNode before = in.getPrev();
					final LinkedIntLeafNode after = in.getNext();

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

	private final void reinsertEntry(LinkedIntLeafNode in)
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
					final LinkedIntLeafNode before = in.getPrev();
					final LinkedIntLeafNode after = in.getNext();

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
			return this.root.hasCargo();
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
			return this.root.hasCargo();
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
			LinkedIntNode current = this.root.getChildAt(firstHalfByte);
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
	
	public final int get(int key)
	{
		return this.get_internal_noZeroCheck(HalfByteArrayFactory.convertIntoArray(key));
	}
	
	public final int get(long key)
	{
		return this.get_internal_noZeroCheck(HalfByteArrayFactory.convertIntoArray(key));
	}

	public final int get(char[] key)
	{
		if (this.numEntries == 0)
		{
			throw new IndexOutOfBoundsException();
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
	public final int get(CharList key)
	{
		if (this.numEntries == 0)
		{
			throw new IndexOutOfBoundsException();
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
	
	public final int get(String key)
	{
		if (this.numEntries == 0)
		{
			throw new IndexOutOfBoundsException();
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

	public final int get(HalfByteArray key)
	{
		if (this.numEntries == 0)
		{
			throw new IndexOutOfBoundsException();
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

	private final int get_internal_noZeroCheck(HalfByteArray key)
	{
		final int firstHalfByte = key.getFirstHalfByte();
		final int keyLength = key.length();

		if (this.root.hasChildAt(firstHalfByte))
		{
			LinkedIntNode current = this.root.getChildAt(firstHalfByte);
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
						return ((LinkedIntLeafNode) current).getCargo();
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

		throw new IndexOutOfBoundsException();
	}
	
	public final int put(int key, int value)
	{
		return this.put_internal_noZeroCheck(HalfByteArrayFactory.convertIntoArray(key), value);
	}
	
	public final int put(long key, int value)
	{
		return this.put_internal_noZeroCheck(HalfByteArrayFactory.convertIntoArray(key), value);
	}

	public final int put(char[] key, int value)
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
	public final int put(CharList key, int value)
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
	
	public final int put(String key, int value)
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

	public final int put(HalfByteArray key, int value)
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
	
	private final int putHelper_root(int value)
	{
		final int old = this.root.getCargo();
		this.root.setCargo(value);
		this.reinsertEntry(this.root);
		return old;
	}
	
	private final int putHelper_preExisting(LinkedIntNode node, int value)
	{
		return this.putHelper_preExisting((LinkedIntLeafNode) node, value);
	}
	
	private final int putHelper_preExisting(LinkedIntLeafNode node, int value)
	{
		final int oldCargo = node.getCargo();
		node.setCargo(value);
		this.reinsertEntry(node);
		return oldCargo;
	}
	
	private final int putHelper_transNode(LinkedIntNode node, HalfByteArray nodeKey, int value)
	{
		final LinkedIntLeafNode transNode = new LinkedIntLeafNode(node, value);
		transNode.getParent().setChildAt(nodeKey.getFirstHalfByte(), transNode);
		this.addEntry(transNode);
		return pseudoNull;
		//return null;
	}
	
	private final int putHelper_Leaf(LinkedIntNode parentNode, HalfByteArray leafKeyChunk, int value, int nextChildIndex)
	{
		final LinkedIntLeafNode result = new LinkedIntLeafNode(parentNode, leafKeyChunk, value);
		parentNode.setChildAt(nextChildIndex, result);
		this.addEntry(result);
		return pseudoNull;
		//return null;
	}

	/**
	 * the 'ZeroCheck' refers to the length of the key
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	protected final int put_internal_noZeroCheck(HalfByteArray key, int value)
	{
		final int firstHalfByte = key.getFirstHalfByte();

		if (this.numEntries == 0 || !this.root.hasChildAt(firstHalfByte))
		{
			final LinkedIntLeafNode result = new LinkedIntLeafNode(null, key, value);
			this.root.setChildAt(firstHalfByte, result);
			this.addEntry(result);
			return pseudoNull;
			//return null;
		}
		else
		{
			LinkedIntNode node = this.root.getChildAt(firstHalfByte);
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
								final LinkedIntNode child = node.getChildAt(lastChildIndex);
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

									final LinkedIntLeafNode result = new LinkedIntLeafNode(node, wrapSingleHalfByte(lastChildIndex), value);
									node.setChildAt(lastChildIndex, result);

									child.setKeyChunk(restKey);
									result.setChildAt(restKey.getFirstHalfByte(), child);
									child.setParent(result);

									this.addEntry(result);
									return pseudoNull;
									//return null;
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
	private final int put_internal_split(final LinkedIntNode node, final int value, final int firstHalfByte_split, final HalfByteArray splitKey, final HalfByteArray oldKey, final HalfByteArray restKey, final LinkedIntNode parent)
	{
		final LinkedIntNode splitNode = new LinkedIntNode(parent, splitKey);
		final LinkedIntLeafNode nextNode = new LinkedIntLeafNode(splitNode, restKey, value);

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

		return pseudoNull;
		//return null;
	}
	
	private static final HalfByteArray wrapSingleHalfByte(int payload)
	{
		final HalfByteArray result = HalfByteArray.getInstance(1);
		result.setHalfByteAt(0, payload);
		return result;
	}
	
	public final int remove(int key)
	{
		return this.remove_internal_noZeroCheck(HalfByteArrayFactory.convertIntoArray(key));
	}
	
	public final int remove(long key)
	{
		return this.remove_internal_noZeroCheck(HalfByteArrayFactory.convertIntoArray(key));
	}
	
	public final int remove(char[] key)
	{
		if (this.numEntries == 0)
		{
			return pseudoNull;
			//return null;
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
	public final int remove(CharList key)
	{
		if (this.numEntries == 0)
		{
			return pseudoNull;
			//return null;
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
	
	public final int remove(String key)
	{
		if (this.numEntries == 0)
		{
			return pseudoNull;
			//return null;
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

	public final int remove(HalfByteArray key)
	{
		if (this.numEntries == 0)
		{
			return pseudoNull;
			//return null;
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
	
	private final int removeHelper_root()
	{
		final int result = this.root.getCargo();
		this.root.clearCargo();
		this.removeEntry(this.root);
		return result;
	}

	private final int remove_internal_noZeroCheck(HalfByteArray key)
	{
		final int result;
		final int firstHalfByte = key.getFirstHalfByte();

		if (!this.root.hasChildAt(firstHalfByte))
		{
			return pseudoNull;
			//return null;
		}
		else
		{
			LinkedIntNode current = this.root.getChildAt(firstHalfByte);
			int keyIndex = 0;

			while (true)
			{
				final HalfByteArray curKeyChunk = current.getKeyChunk();
				final int curMatch = key.matchAgainst(curKeyChunk, keyIndex + 1, 1);

				if (curMatch != curKeyChunk.length())
				{
					return pseudoNull;
					//return null;
				}
				else	// matches chunk, check childIndex
				{
					keyIndex += curMatch;

					if (keyIndex == key.length())
					{
						final LinkedIntLeafNode cast = (LinkedIntLeafNode) current;
						result = cast.getCargo();

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
								LinkedIntNode child = current.getChildAt(childIndex);

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

								final LinkedIntNode parent = current.getParent();

								if (parent != null)
								{
									parent.setChildAt(newKeyChunk.getFirstHalfByte(), child);
									child.setParent(parent);
								}

								this.removeEntry(cast);

								break;
							}
							default:
							{
								// current needs to be transformed into a branch node
								final LinkedIntNode transform = cast.transformIntoBranchNode();

								final LinkedIntNode parent = current.getParent();

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
							return pseudoNull;
							//return null;
						}
					}
					else
					{
						return pseudoNull;
						//return null;
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

		for(KeyedInt entry: this)
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
			result.add_asDecString(entry.getValue());
			result.add('}');
		}

		return result;
	}

	@Override
	public Iterator<KeyedInt> iterator()
	{
		return new Iterators.EntryIterator(this);
	}
}
