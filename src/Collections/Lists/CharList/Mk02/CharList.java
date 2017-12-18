package Collections.Lists.CharList.Mk02;

import Collections.Lists.CharList.Mk02.Node_HalfByteArr.PrintModes;
import Collections.Lists.Generic.Directed.SingleLinkedList;
import Collections.PackedArrays.HalfByte.HalfByteArray;
import Collections.PackedArrays.HalfByte.HalfByteArrayFactory;
import Collections.PrimitiveInterfaceAnalogues.Char.CharIterable;
import CustomExceptions.BadParseException;
import HandyStuff.ArrayStuff.EmptyPrimitiveArrays;
import Incubator.BytesStreamer.CompletelyDifferent.CharSetTranslations;

public class CharList implements CharIterable
{
	static final boolean activateDefensiveMode = false;

	private static final char[] NewLine = System.lineSeparator().toCharArray();
	private static final char Tab = '\t';
	private static final char[] BinaryPrefix = "0b".toCharArray();
	private static final char[] HexPrefix = "0x".toCharArray();
	private static final char[] NullString = "null".toCharArray();
	private static final char[] BoolToString_True = "TRUE".toCharArray();
	private static final char[] BoolToString_False = "FALSE".toCharArray();
	private static final char[] spacedEqualSign = " = ".toCharArray();

	private Node head;
	private Node tail;
	private int sizeInChars;

	/**
	 * Package-private constructor, for shortcuts
	 * 
	 * @param inHead
	 * @param inTail
	 * @param inSize
	 */
	CharList(Node inHead, Node inTail, int inSize)
	{
		this.head = inHead;
		this.tail = inTail;
		this.sizeInChars = inSize;
	}

	/**
	 * Vanilla Constructor
	 */
	public CharList()
	{
		this(null, null, 0);
	}

	/**
	 * Functions exactly like instantiating a new CharList(), then calling .add(char[]) on it
	 * 
	 * @param in
	 */
	public CharList(char[] in)
	{
		this.head = NodeFactory.createNode(in);
		this.tail = this.head;
		this.sizeInChars = in.length;
	}

	/**
	 * Functions exactly like instantiating a new CharList(), then calling .add(String) on it
	 * 
	 * @param in
	 */
	public CharList(String in)
	{
		this.head = NodeFactory.createNode(in);
		this.tail = this.head;
		this.sizeInChars = in.length();
	}

	/**
	 * 
	 * @return the number of characters currently contained within the CharList
	 */
	public final int size()
	{
		return this.sizeInChars;
	}

	/**
	 * 
	 * @return {@code true} if (this.size() == 0), false otherwise
	 */
	public final boolean isEmpty()
	{
		return this.sizeInChars == 0;
	}

	/**
	 * 
	 * @return {@code false} if (this.size() == 0), true otherwise
	 */
	public final boolean isNotEmpty()
	{
		return this.sizeInChars != 0;
	}

	/**
	 * Performs a linear search to find the character at the given index. As such, it is extremely inadvisable to use this in an iterative fashion.
	 * 
	 * @param index
	 * @return the character at the given index
	 */
	public final char charAt(int index)
	{
		int total = 0;
		Node current = this.head;

		while (true)
		{
			if (current == null)
			{
				throw new IndexOutOfBoundsException();
			}

			if (current instanceof Node_Char)
			{
				total++;

				if (total == index)
				{
					return current.firstChar();
				}
				else
				{
					current = current.getNext();
				}
			}
			else
			{
				final MultiNode cast = (MultiNode) current;

				final int curSize = cast.getLength();

				if ((total + curSize) > index)
				{
					index = index - total;

					return cast.CharAt(index);
				}
				else
				{
					current = current.next;
					total += curSize;
				}
			}
		}
	}

	/**
	 * 
	 * @return the first character in this CharList, in at least an efficient a manner as calling .charAt(0)
	 */
	public final char firstChar()
	{
		return this.head.firstChar();
	}

	/**
	 * 
	 * @return the last character in this CharList, in a much more efficient manner than calling .charAt(this.size() - 1)
	 */
	public final char lastChar()
	{
		return this.tail.lastChar();
	}

	/**
	 * clears the contents of this CharList, as if it had just been instantiated using {@code new CharList()};
	 */
	public final void clear()
	{
		this.head = null;
		this.tail = null;
		this.sizeInChars = 0;
	}

	/**
	 * @return an Object that is identical an implementation of Iterator&ltchar&gt,
	 *         with the obvious exception that the Object returned does not actually implement this, as primitives cannot be used with generics.
	 */
	@Override
	public final linearCharsIterator getCharIterator()
	{
		return new linearCharsIterator(this.head, this.sizeInChars);
	}

	/**
	 * 
	 * @return exactly like calling .getCharIterator(). Kept in for specificity reasons.
	 */
	public final linearCharsIterator getLinearCharsIterator()
	{
		return new linearCharsIterator(this.head, this.sizeInChars);
	}

	/**
	 * 
	 * @param delim
	 * @return an array of size 2, with both elements always instantiated, though it is possible that at least one is empty.
	 */
	public final CharList[] splitAtFirst(char delim)
	{
		final CharList first = new CharList();
		final CharList second = new CharList();
		this.splitAtFirst(delim, first, second);
		return new CharList[] {
								first,
								second };
	}

	/**
	 * add()s the first section of this CharList to {@code first},
	 * then add()s the remaining characters to {@code second}. This CharList is not effected.
	 * 
	 * @param delim
	 * @param first
	 * @param second
	 */
	final void splitAtFirst(char delim, CharList first, CharList second)
	{
		final linearCharsIterator itsy = this.getCharIterator();
		char curr = 0;

		while (itsy.hasNext() && (curr = itsy.next()) != delim)
		{
			first.add(curr);
		}

		second.add(itsy.remainingToCharArray(curr));
	}

	/**
	 * Splits this CharList into fragments delimited by the given argument,
	 * with the given argument being consumed each time.
	 * NOTE TO SELF: come back and optimize this, current implementation is naive as hell.
	 * 
	 * @param inDelim
	 * @return a list containing all of the fragments delimited by the given argument
	 */
	public final SingleLinkedList<CharList> splitAt(char inDelim)
	{
		final SingleLinkedList<CharList> result = new SingleLinkedList<CharList>();
		final linearCharsIterator itsy = this.getCharIterator();

		while (itsy.hasNext())
		{
			final CharList current = new CharList();

			inner: do
			{
				final char currChar = itsy.next();

				if (currChar == inDelim)
				{
					break inner;
				}
				else
				{
					current.add(currChar);
				}
			} while (itsy.hasNext());

			result.add(current);
		}

		return result;
	}

	/**
	 * 
	 * @return removes the first character in this CharList, then returns that character.
	 */
	public final char pop()
	{
		switch (this.sizeInChars)
		{
			case 0:
			{
				throw new IndexOutOfBoundsException();
			}
			case 1:
			{
				final char result = this.firstChar();
				this.head = null;
				this.tail = null;
				this.sizeInChars = 0;
				return result;
			}
			default:
			{
				final char result = this.firstChar();

				if (this.head instanceof Node_Char)
				{
					this.head = this.head.next;
				}
				else
				{
					final MultiNode cast = (MultiNode) this.head;
					final CharList saccy = cast.splitIntoNewCharList_skipFirst();

					if (this.head == this.tail)
					{
						this.head = saccy.head;
						this.tail = saccy.tail;
					}
					else
					{
						saccy.tail.setNext(this.head.getNext());
						this.head = saccy.head;
					}
				}

				this.sizeInChars--;
				return result;
			}
		}
	}

	@Override
	public final String toString()
	{
		return new String(this.toCharArray());
	}

	/**
	 * 
	 * @return all the characters within this CharList, copied into an array.
	 */
	public final char[] toCharArray()
	{
		if (this.sizeInChars > 0)
		{
			return this.head.toCharArray(this.sizeInChars);
		}
		else
		{
			return EmptyPrimitiveArrays.emptyCharArr;
		}
	}

	/**
	 * 
	 * @return A {@code HalfByteArray} view of this CharList, backed by this CharList.
	 *         Intended for use with one of the {@code RadixMap} implementations.
	 */
	public final HalfByteArray toHalfByteArray()
	{
		return HalfByteArrayFactory.wrapIntoArray(this.toCharArray());
	}

	/**
	 * adds the given argument to the end of this CharList
	 * 
	 * @param in
	 */
	public final void add(char in)
	{
		this.add_internal(new Node_Char(in), 1);
	}

	/**
	 * functionally equivalent to calling {@code this.add(in)}, {@code numRepeats} times.
	 * If {@code numRepeats} is less than or equal to 0, nothing happens.
	 * 
	 * @param in
	 * @param numRepeats
	 */
	public final void add(char in, int numRepeats)
	{
		switch (numRepeats)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.add(in);
				break;
			}
			default:
			{
				this.add_internal_ZeroCheck(in, numRepeats);
				break;
			}
		}
	}

	/**
	 * checks to make sure {@code numRepeats} > 0
	 * 
	 * @param in
	 * @param numRepeats
	 */
	private final void add_internal_ZeroCheck(char in, int numRepeats)
	{
		if (numRepeats > 0)
		{
			this.add_internal_noZeroCheck(in, numRepeats);
		}
	}

	private final void add_internal_noZeroCheck(char in, int numRepeats)
	{
		this.add_internal(new Node_Char_Repeated(numRepeats, in), numRepeats);
	}

	/**
	 * adds the given argument to the end of this CharList.
	 * Prefer using this over {@code this.add(String)}, if possible, as this will allow faster concatenation.
	 * 
	 * @param in
	 */
	public final void add(char[] in)
	{
		switch (in.length)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.add(in[0]);
				break;
			}
			default:
			{
				this.add_internal_noLengthCheck(in);
				break;
			}
		}
	}

	/**
	 * presumes {@code in.length} > 1
	 * 
	 * @param in
	 */
	private final void add_internal_noLengthCheck(char[] in)
	{
		this.add_internal(new Node_CharArr(in), in.length);
	}

	/**
	 * functionally equivalent to calling {@code this.add(in)}, {@code numRepeats} times.
	 * If {@code numRepeats} is less than or equal to 0, nothing happens.
	 * 
	 * @param in
	 * @param numRepeats
	 */
	public final void add(char[] in, int numRepeats)
	{
		switch (numRepeats)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.add(in); // this is efficient, because no checks overlap
				break;
			}
			default:
			{
				switch (in.length)
				{
					case 0:
					{
						break;
					}
					case 1:
					{
						this.add_internal_ZeroCheck(in[0], numRepeats);
						break;
					}
					default:
					{
						this.add_internal(in, numRepeats);
						break;
					}
				}
				break;
			}
		}
	}

	/**
	 * checks to make sure {@code numRepeats} > 0
	 * 
	 * @param in
	 * @param numRepeats
	 */
	private final void add_internal(char[] in, int numRepeats)
	{
		if (numRepeats > 0)
		{
			this.add_internal(new Node_CharArr_Repeated(numRepeats, in), in.length * numRepeats);
		}
	}

	/**
	 * adds the given argument to the end of this CharList.
	 * Prefer using {@code this.add(char[])} over this, if possible, as this will allow faster concatenation.
	 * 
	 * @param in
	 */
	public final void add(String in)
	{
		switch (in.length())
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.add(in.charAt(0));
				break;
			}
			default:
			{
				this.add_internal(new Node_String(in), in.length());
				break;
			}
		}
	}

	/**
	 * functionally equivalent to calling {@code this.add(in)}, {@code numRepeats} times.
	 * If {@code numRepeats} is less than or equal to 0, nothing happens.
	 * 
	 * @param in
	 * @param numRepeats
	 */
	public final void add(String in, int numRepeats)
	{
		switch (numRepeats)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.add(in); // no checks overlap
				break;
			}
			default:
			{
				switch (in.length())
				{
					case 0:
					{
						break;
					}
					case 1:
					{
						this.add_internal_ZeroCheck(in.charAt(0), numRepeats);
						break;
					}
					default:
					{
						if (numRepeats > 0)
						{
							this.add_internal(new Node_String_Repeated(numRepeats, in), numRepeats * in.length());
						}
						break;
					}
				}
				break;
			}
		}
	}

	/**
	 * functionally equivalent to iteratively calling {@code this.add(in[x])}.
	 * Does not store as a single unit to maintain accuracy for {@code this.size()}.
	 * 
	 * @param in
	 */
	public final void add(String[] in)
	{
		switch (in.length)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.add(in[0]); // no checks redundancy
				break;
			}
			default:
			{
				for (String curr : in)
				{
					this.add(curr);
				}
				break;
			}
		}
	}

	/**
	 * functionally equivalent to copying the contents of {@code in} into a character array, then calling {@code this.add(in_asCharArray, numRepeats)}.
	 * 
	 * @param in
	 * @param numRepeats
	 */
	public final void add(String[] in, int numRepeats)
	{
		switch (in.length)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.add(in[0], numRepeats); // no checks overlap
				break;
			}
			default:
			{
				switch (numRepeats)
				{
					case 0:
					{
						break;
					}
					case 1:
					{
						this.add(in); // no checks redundancy - could be an array of empty strings
						break;
					}
					default:
					{
						if (numRepeats > 0)
						{
							final char[] temp = StringArrToCharArr(in);
							this.add(temp, numRepeats); // no checks redundancy - could be an array of empty strings
						}
					}
				}
				break;
			}
		}
	}

	public static final char[] StringArrToCharArr(String[] in)
	{
		int length = in[0].length();

		for (int i = in.length - 1; i > 0; i--)
		{
			length += in[i].length();
		}

		final char[] result = new char[length];

		for (int stringIndex = in.length; stringIndex > 0; --stringIndex)
		{
			final int stringLength = in[stringIndex].length();
			length -= stringLength;
			in[stringIndex].getChars(0, stringLength, result, length); // hopefully this uses System.ArrayCpy under the hood
		}

		return result;
	}

	/**
	 * adds {@code in} as a list of {@code delimiterToInsert}-separated values.
	 * 
	 * @param in
	 * @param delimiterToInsert
	 */
	public final void add(String[] in, char delimiterToInsert)
	{
		switch (in.length)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.add(in[0]);
				break;
			}
			default:
			{
				this.add(in[0]);

				for (int i = 1; i < in.length; i++)
				{
					this.add(delimiterToInsert);
					this.add(in[i]);
				}
				break;
			}
		}
	}

	/**
	 * adds {@code in} as a list of {@code delimiterToInsert}-separated values.
	 * 
	 * @param in
	 * @param delimiterToInsert
	 */
	public final void add(String[] in, String delimiterToInsert)
	{
		switch (in.length)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.add(in[0]);
				break;
			}
			default:
			{
				this.add(in[0]);

				for (int i = 1; i < in.length; i++)
				{
					this.add(delimiterToInsert);
					this.add(in[i]);
				}
				break;
			}
		}
	}

	/**
	 * adds {@code in} to this CharList, consuming {@code in} if instructed to do so.
	 * There is no {@code canConsume}-less overload of this method to attempt to ensure
	 * that the programmer pays close attention to which CharLists are being consumed.
	 * 
	 * @param in
	 * @param canConsume
	 */
	public final void add(CharList in, boolean canConsume)
	{
		switch (in.sizeInChars)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				if (canConsume)
				{
					this.add_internal(in.head, 1);
				}
				else
				{
					this.add(in.firstChar());
				}
				break;
			}
			default:
			{
				if (in.sizeInChars != 0)
				{
					if (!canConsume)
					{
						this.add(in.toCharArray());
					}
					else
					{
						this.add_internal(in);
					}
				}
				break;
			}
		}

	}

	/**
	 * {@code in} is not consumed by this method, as there is no point
	 * 
	 * @param in
	 * @param numRepeats
	 */
	public final void add(CharList in, int numRepeats)
	{
		switch (numRepeats)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.add(in.toCharArray());
				break;
			}
			default:
			{
				this.add(in.toCharArray(), numRepeats);
				break;
			}
		}
	}

	/**
	 * {@code in} is assumed to be of size > 0, and is consumed by this method
	 * 
	 * @param in
	 */
	private final void add_internal(CharList in)
	{
		if (this.sizeInChars == 0)
		{
			this.head = in.head;
			this.sizeInChars = in.sizeInChars;

			if (in.sizeInChars == 1)
			{
				this.tail = in.head;
			}
			else
			{
				this.tail = in.tail;
			}
		}
		else
		{
			this.tail.setNext(in.head);
			this.tail = in.tail;
			this.sizeInChars += in.sizeInChars;
		}
	}

	private final void add_internal(Node in, int nodeLength)
	{
		if (nodeLength != 0)
		{
			if (this.sizeInChars == 0)
			{
				this.head = in;
				this.tail = in;
				this.sizeInChars = nodeLength;
			}
			else
			{
				this.tail.setNext(in);
				this.tail = in;
				this.sizeInChars += nodeLength;
			}
		}
	}

	public final void add_asBinString(int in)
	{
		this.add(NumNodeConversion.toBinCharArr_withPrefix(in));
	}

	public final void add_asBinString(int in, int minNumChars)
	{
		final char[] raw = NumNodeConversion.toBinCharArr_noPrefix(in);
		this.add(BinaryPrefix);
		this.add('0', minNumChars - 2 - raw.length);
		this.add(raw);
	}

	public final void add_asBinString(long in)
	{
		this.add(NumNodeConversion.toBinCharArr_withPrefix(in));
	}

	public final void add_asBinString(long in, int minNumChars)
	{
		final char[] raw = NumNodeConversion.toBinCharArr_noPrefix(in);
		this.add(BinaryPrefix);
		this.add('0', minNumChars - 2 - raw.length);
		this.add(raw);
	}

	public final void add_asDecString(int in)
	{
		this.add(NumNodeConversion.toDecCharArr(in));
	}

	/**
	 * 
	 * @param in
	 * @param minNumChars
	 *            if this equals 0, no padding will be added
	 */
	public final void add_asDecString(int in, int minNumChars)
	{
		final char[] raw = NumNodeConversion.toDecCharArr(in);

		if (minNumChars > 0)
		{
			this.add_internal_noZeroCheck('0', minNumChars - raw.length);
		}

		this.add(raw);
	}

	public final void add_asDecString(long in)
	{
		this.add(NumNodeConversion.toDecCharArr(in));
	}

	/**
	 * 
	 * @param in
	 * @param minNumChars
	 *            if this equals 0, no padding will be added
	 */
	public final void add_asDecString(long in, int minNumChars)
	{
		final char[] raw = NumNodeConversion.toDecCharArr(in);

		if (minNumChars > 0)
		{
			this.add_internal_noZeroCheck('0', minNumChars - raw.length);
		}

		this.add(raw);
	}

	/**
	 * 
	 * @param in
	 * @param minNumChars
	 *            if this equals 0, no padding will be added
	 */
	public final void add_asDecString(double in)
	{
		this.add(NumNodeConversion.toDecString(in));
	}

	/**
	 * currently uses String internally, as opposed to char[], because NumNodeConversion.toDecCharArr(double)'s implementation needs to be redone
	 * 
	 * @param in
	 * @param minNumChars
	 */
	public final void add_asDecString(double in, int minNumChars)
	{
		final String raw = NumNodeConversion.toDecString(in);

		if (minNumChars > 0)
		{
			this.add_internal_noZeroCheck('0', minNumChars - raw.length());
		}

		this.add(raw);
	}

	public final void add_asHexString(int in)
	{
		this.add(NumNodeConversion.toHexCharArr_withPrefix(in));
	}

	public final void add_asHexString(int in, int minNumChars)
	{
		final char[] raw = NumNodeConversion.toHexCharArr_noPrefix(in);
		this.add(HexPrefix);

		if (minNumChars > 0)
		{
			this.add_internal_noZeroCheck('0', minNumChars - raw.length - 2);
		}

		this.add(raw);
	}

	public final void add_asHexString(long in)
	{
		this.add(NumNodeConversion.toHexCharArr_withPrefix(in));
	}

	public final void add_asHexString(long in, int minNumChars)
	{
		final char[] raw = NumNodeConversion.toHexCharArr_noPrefix(in);
		this.add(HexPrefix);

		if (minNumChars > 0)
		{
			this.add_internal_noZeroCheck('0', minNumChars - raw.length - 2);
		}

		this.add(raw);
	}

	/**
	 * adds "TRUE" if(in), else adds "FALSE"
	 * 
	 * @param in
	 */
	public final void addAsString(boolean in)
	{
		if (in)
		{
			this.add(BoolToString_True);
		}
		else
		{
			this.add(BoolToString_False);
		}
	}

	public final void addAsString_ofValues(HalfByteArray in)
	{
		if (in.length() != 0)
		{
			this.add_internal(new Node_HalfByteArr(in, PrintModes.AsValues), in.length() / 2);
		}
	}

	public final void addAsString_ofChars(HalfByteArray in)
	{
		if (in.length() != 0)
		{
			this.add_internal(new Node_HalfByteArr(in, PrintModes.AsChars), in.length() / 2);
		}
	}

	/**
	 * will add the string "null" if {@code in} is null, otherwise adds the output of the object's {@link Object#toString()} method
	 * 
	 * @param in
	 */
	public final void addAsString(Object in)
	{
		if (in != null)
		{
			this.add(in.toString());
		}
		else
		{
			this.add_internal_noLengthCheck(NullString);
		}
	}

	public final void addAsPaddedString(String in, int minLength, boolean padToFront)
	{
		this.addAsPaddedString(in, ' ', minLength, padToFront);
	}

	public final void addAsPaddedString(String in, char pad, int minLength, boolean padToFront)
	{
		final int dif = minLength - in.length();

		if (dif > 0)
		{
			if (padToFront)
			{
				this.add(pad, dif);
				this.add(in);
			}
			else
			{
				this.add(in);
				this.add(pad, dif);
			}
		}
	}

	/**
	 * assumes unicode codepoint, and translates to utf-8
	 * 
	 * @param codepoint
	 */
	public final void add_asCodepoint(int codepoint)
	{
		this.add_asCodepoint(codepoint, false);
	}

	public final void add_asCodepoint(int codepoint, boolean isAlreadyEncoded)
	{
		if (!isAlreadyEncoded)
		{
			codepoint = CharSetTranslations.encode_Utf8(codepoint);
		}

		if ((codepoint & 0xffff0000) == 0)
		{
			// 1 or 2
			if ((codepoint & 0x0000ff00) == 0)
			{
				// 1
				this.add((char) codepoint);
			}
			else
			{
				// 2
				final char[] in = new char[2];
				in[0] = (char) ((codepoint >>> 8) & 0xff);
				in[1] = (char) (codepoint & 0xff);
				this.add(in);
				// System.out.println(Integer.toHexString(codepoint));
			}
		}
		else
		{
			// System.out.println(Integer.toHexString(codepoint));
			// 3 or 4
			if ((codepoint & 0xff000000) == 0)
			{
				// 3
				final char[] in = new char[3];
				in[0] = (char) ((codepoint >>> 16) & 0xff);
				in[1] = (char) ((codepoint >>> 8) & 0xff);
				in[2] = (char) (codepoint & 0xff);
				this.add(in);
			}
			else
			{
				// 4
				final char[] in = new char[4];
				in[0] = (char) ((codepoint >>> 24) & 0xff);
				in[1] = (char) ((codepoint >>> 16) & 0xff);
				in[2] = (char) ((codepoint >>> 8) & 0xff);
				in[3] = (char) (codepoint & 0xff);
				this.add(in);
			}
		}

		/*
		if((codepoint & 0xffff0000) != 0)
		{
			final char[] raw = new char[2];
			raw[0] = (char) ((codepoint >> 16) & 0xffff);
			raw[1] = (char) (codepoint & 0xffff);
			this.add(raw);
			//System.out.println("converted " + Integer.toBinaryString(codepoint) + " to " + raw[0] + " and " + raw[1]);
		}
		else
		{
			this.add((char) codepoint);
		}
		*/
	}
	
	private final char[] unicodePrefix = "\\u".toCharArray();
	
	/**
	 * must be in copepoint form, not encoded form. Will encode directly if 1 byte, otherwise will be of the form &#92u&lthexChars&gt&lt &gt
	 * @param codepoint
	 */
	public final void add_asCodepoint_withCharEscapes(int codepoint)
	{
		if ((codepoint & 0xffff0000) == 0)
		{
			// 1 or 2
			if ((codepoint & 0x0000ff00) == 0)
			{
				// 1
				this.add((char) codepoint);
			}
			else
			{
				// 2
				this.add(unicodePrefix);
				final char[] unicoded = NumNodeConversion.toHexCharArr_noPrefix(codepoint, 4);
				this.add(unicoded);
				this.add(' ');
			}
		}
		else
		{
			// 3 or 4
			if ((codepoint & 0xff000000) == 0)
			{
				// 3
				this.add(unicodePrefix);
				final char[] unicoded = NumNodeConversion.toHexCharArr_noPrefix(codepoint, 6);
				this.add(unicoded);
				this.add(' ');
			}
			else
			{
				// 4
				this.add(unicodePrefix);
				final char[] unicoded = NumNodeConversion.toHexCharArr_noPrefix(codepoint, 8);
				this.add(unicoded);
				this.add(' ');
			}
		}
	}

	/**
	 * Convenience method: just like calling {@link #add(String)} with an argument of "\r\n"
	 */
	public final void addNewLine()
	{
		this.add(NewLine);
	}

	/**
	 * Convenience method: just like calling {@link #add(String, int)} with arguments of "\r\n" and {@code numRepeats}, respectively
	 */
	public final void addNewLine(int numRepeats)
	{
		this.add(NewLine, numRepeats);
	}

	/**
	 * Convenience method: just like calling {@link #addNewLine()}, then calling {@link #add(char, int)} with arguments '\t' and {@code offset}
	 * 
	 * @param offset
	 */
	public final void addNewIndentedLine(int offset)
	{
		this.add(NewLine);
		this.addTab(offset);
	}

	/**
	 * Convenience method: just like calling {@link #add(char)} with an argument of {@link CharList#Tab}
	 */
	public final void addTab()
	{
		this.add(Tab);
	}

	/**
	 * Convenience method: just like calling {@link #add(char, int)} with arguments of {@link CharList#Tab} and {@code numRepeats}
	 */
	public final void addTab(int numRepeats)
	{
		this.add(Tab, numRepeats);
	}

	/**
	 * Convenience method: just like calling {@link #add(char[])} with an argument of {@link CharList#spacedEqualSign}
	 */
	public final void addSpacedEqualSign()
	{
		this.add(spacedEqualSign);
	}

	/*
	 * Push Ops
	 */

	/**
	 * prepends {@code in} to the beginning of the CharList
	 * 
	 * @param in
	 */
	public final void push(char in)
	{
		this.push_internal(new Node_Char(in), 1);
	}

	/**
	 * functionally equivalent to generating a character array of length {@code numRepeats},
	 * filling it with {@code in}, then prepending that array to the beginning of the CharList
	 * 
	 * @param in
	 * @param numRepeats
	 */
	public final void push(char in, int numRepeats)
	{
		switch (numRepeats)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.push(in);
				break;
			}
			default:
			{
				this.push_internal_ZeroCheck(in, numRepeats);
				break;
			}
		}
	}

	private final void push_internal_ZeroCheck(char in, int numRepeats)
	{
		if (numRepeats > 0)
		{
			this.push_internal_noZeroCheck(in, numRepeats);
		}
	}

	private final void push_internal_noZeroCheck(char in, int numRepeats)
	{
		this.push_internal(new Node_Char_Repeated(numRepeats, in), numRepeats);
	}

	/**
	 * prepends the given argument to the beginning of this CharList.
	 * Prefer using this over {@code this.push(String)}, if possible, as this will allow faster concatenation.
	 * Please note: this is *not* functionally equivalent to iterating positively through {@code in}, calling {@link #push(char)} on each element.
	 * 
	 * @param in
	 */
	public final void push(char[] in)
	{
		switch (in.length)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.push(in[0]);
				break;
			}
			default:
			{
				this.push_internal_noLengthCheck(in);
				break;
			}
		}
	}

	/**
	 * presumes {@code in.length} > 1
	 * 
	 * @param in
	 */
	private final void push_internal_noLengthCheck(char[] in)
	{
		this.push_internal(new Node_CharArr(in), in.length);
	}

	/**
	 * functionally equivalent to calling {@code this.push(in)}, {@code numRepeats} times.
	 * If {@code numRepeats} is less than or equal to 0, nothing happens.
	 * 
	 * @param in
	 * @param numRepeats
	 */
	public final void push(char[] in, int numRepeats)
	{
		switch (numRepeats)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.push(in);
				break;
			}
			default:
			{
				switch (in.length)
				{
					case 0:
					{
						break;
					}
					case 1:
					{
						this.push_internal_ZeroCheck(in[0], numRepeats);
						break;
					}
					default:
					{
						this.push_internal(in, numRepeats);
						break;
					}
				}
				break;
			}
		}
	}

	/**
	 * checks to make sure {@code numRepeats} > 0
	 * 
	 * @param in
	 * @param numRepeats
	 */
	private final void push_internal(char[] in, int numRepeats)
	{
		if (numRepeats > 0)
		{
			this.push_internal(new Node_CharArr_Repeated(numRepeats, in), in.length * numRepeats);
		}
	}

	/**
	 * prepends the given argument to the beginning of this CharList.
	 * Prefer using {@code this.add(char[])} over this, if possible, as this will allow faster concatenation.
	 * 
	 * @param in
	 */
	public final void push(String in)
	{
		switch (in.length())
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.push(in.charAt(0));
				break;
			}
			default:
			{
				this.push_internal(new Node_String(in), in.length());
				break;
			}
		}
	}

	/**
	 * functionally equivalent to calling {@code this.push(in)}, {@code numRepeats} times.
	 * If {@code numRepeats} is less than or equal to 0, nothing happens.
	 * 
	 * @param in
	 * @param numRepeats
	 */
	public final void push(String in, int numRepeats)
	{
		switch (numRepeats)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.push(in);
				break;
			}
			default:
			{
				switch (in.length())
				{
					case 0:
					{
						break;
					}
					case 1:
					{
						this.push_internal_ZeroCheck(in.charAt(0), numRepeats);
						break;
					}
					default:
					{
						if (numRepeats > 0)
						{
							this.push_internal(new Node_String_Repeated(numRepeats, in), numRepeats * in.length());
						}
						break;
					}
				}
				break;
			}
		}
	}

	/**
	 * functionally equivalent to concatenating all elements in positive order, then prepending that to this CharList
	 * 
	 * @param in
	 */
	public final void push(String[] in)
	{
		switch (in.length)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.push(in[0]);
				break;
			}
			default:
			{
				if (this.sizeInChars == 0)
				{
					for (String curr : in)
					{
						this.add(curr);
					}
				}
				else
				{
					for (int i = in.length; i >= 0; --i)
					{
						this.push(in[i]);
					}
				}
				break;
			}
		}
	}

	/**
	 * functionally equivalent to calling {@link #push(String[])} {@code numRepeats} times, but with some under-the-hood optimization.
	 * 
	 * @param in
	 * @param numRepeats
	 */
	public final void push(String[] in, int numRepeats)
	{
		switch (in.length)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.push(in[0], numRepeats); // no checks overlap
				break;
			}
			default:
			{
				switch (numRepeats)
				{
					case 0:
					{
						break;
					}
					case 1:
					{
						this.push(in); // no checks redundancy - could be an array of empty strings
						break;
					}
					default:
					{
						if (numRepeats > 0)
						{
							this.push(StringArrToCharArr(in), numRepeats); // no checks redundancy - could be an array of empty strings
						}
					}
				}
				break;
			}
		}
	}

	/**
	 * concatenates {@code in} into a list of {@code delimiterToInsert}-separated values, then prepends to this CharList
	 * 
	 * @param in
	 * @param delimiterToInsert
	 */
	public final void push(String[] in, char delimiterToInsert)
	{
		switch (in.length)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.push(in[0]);
				break;
			}
			default:
			{
				int i = in.length - 1;
				this.push(in[i--]);

				for (; i >= 0; i--)
				{
					this.push(delimiterToInsert);
					this.push(in[i]);
				}
				break;
			}
		}
	}

	/**
	 * concatenates {@code in} into a list of {@code delimiterToInsert}-separated values, then prepends to this CharList
	 * 
	 * @param in
	 * @param delimiterToInsert
	 */
	public final void push(String[] in, String delimiterToInsert)
	{
		switch (in.length)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.push(in[0]);
				break;
			}
			default:
			{
				int i = in.length - 1;
				this.push(in[i--]);

				for (; i >= 0; i--)
				{
					this.push(delimiterToInsert);
					this.push(in[i]);
				}
				break;
			}
		}
	}

	/**
	 * prepends {@code in} to this CharList, consuming {@code in} if instructed to do so.
	 * There is no {@code canConsume}-less overload of this method to attempt to ensure
	 * that the programmer pays close attention to which CharLists are being consumed.
	 * 
	 * @param in
	 * @param canConsume
	 */
	public final void push(CharList in, boolean canConsume)
	{
		switch (in.sizeInChars)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				if (canConsume)
				{
					this.push_internal(in.head, 1);
				}
				else
				{
					this.push(in.firstChar());
				}
				break;
			}
			default:
			{
				if (in.sizeInChars != 0)
				{
					if (!canConsume)
					{
						this.push(in.toCharArray());
					}
					else
					{
						this.push_internal(in);
					}
				}
				break;
			}
		}
	}

	/**
	 * {@code in} is not consumed by this method, as there is no point
	 * 
	 * @param in
	 * @param numRepeats
	 */
	public final void push(CharList in, int numRepeats)
	{
		switch (numRepeats)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.push(in, false);
				break;
			}
			default:
			{
				switch (in.sizeInChars)
				{
					case 0:
					{
						break;
					}
					case 1:
					{
						this.push(in.firstChar(), numRepeats);
						break;
					}
					default:
					{
						this.push_internal(in.toCharArray(), numRepeats);
						break;
					}
				}
				break;
			}
		}
	}

	/**
	 * {@code in} is assumed to be of size > 0, and is consumed by this method
	 * 
	 * @param in
	 */
	private final void push_internal(CharList in)
	{
		if (this.sizeInChars == 0)
		{
			this.add_internal(in);
		}
		else
		{
			this.push_internal_noZeroCheck(in);
		}
	}

	/**
	 * {@code in} is consumed by this method
	 * 
	 * @param in
	 */
	private final void push_internal_noZeroCheck(CharList in)
	{
		in.tail.setNext(this.head);
		this.head = in.head;
		this.sizeInChars += in.sizeInChars;
	}

	private final void push_internal(Node in, int nodeLength)
	{
		if (nodeLength != 0)
		{
			if (this.sizeInChars == 0)
			{
				this.head = in;
				this.tail = in;
				this.sizeInChars = nodeLength;
			}
			else
			{
				in.setNext(this.head);
				this.head = in;
				this.sizeInChars += nodeLength;
			}
		}
	}

	public final void push_asBinString(int in)
	{
		this.push(NumNodeConversion.toBinCharArr_withPrefix(in));
	}

	public final void push_asBinString(int in, int minNumChars)
	{
		final char[] raw = NumNodeConversion.toBinCharArr_noPrefix(in);
		this.push(raw);
		this.push('0', minNumChars - raw.length - 2);
		this.push(BinaryPrefix);
	}

	public final void push_asBinString(long in)
	{
		this.push(NumNodeConversion.toBinCharArr_withPrefix(in));
	}

	public final void push_asBinString(long in, int minNumChars)
	{
		final char[] raw = NumNodeConversion.toBinCharArr_noPrefix(in);
		this.push(raw);
		this.push('0', minNumChars - raw.length - 2);
		this.push(BinaryPrefix);
	}

	public final void push_asDecString(int in)
	{
		this.push(NumNodeConversion.toDecCharArr(in));
	}

	/**
	 * 
	 * @param in
	 * @param minNumChars
	 *            if this equals 0, no padding will be added
	 */
	public final void push_asDecString(int in, int minNumChars)
	{
		final char[] raw = NumNodeConversion.toDecCharArr(in);
		this.push(raw);

		if (minNumChars > 0)
		{
			this.push_internal_noZeroCheck('0', minNumChars - raw.length);
		}
	}

	public final void push_asDecString(long in)
	{
		this.push(NumNodeConversion.toDecCharArr(in));
	}

	/**
	 * 
	 * @param in
	 * @param minNumChars
	 *            if this equals 0, no padding will be added
	 */
	public final void push_asDecString(long in, int minNumChars)
	{
		final char[] raw = NumNodeConversion.toDecCharArr(in);
		this.push(raw);

		if (minNumChars > 0)
		{
			this.push_internal_noZeroCheck('0', minNumChars - raw.length);
		}
	}

	public final void push_asDecString(double in)
	{
		this.push(NumNodeConversion.toDecString(in));
	}

	public final void push_asHexString(int in)
	{
		this.push(NumNodeConversion.toHexCharArr_withPrefix(in));
	}

	public final void push_asHexString(int in, int minNumChars)
	{
		final char[] raw = NumNodeConversion.toHexCharArr_noPrefix(in);
		this.push(raw);

		if (minNumChars - 2 > 0)
		{
			this.push_internal_noZeroCheck('0', minNumChars - raw.length - 2);
		}

		this.push(HexPrefix);
	}

	public final void push_asHexString(long in)
	{
		this.push(NumNodeConversion.toHexCharArr_withPrefix(in));
	}

	public final void push_asHexString(long in, int minNumChars)
	{
		final char[] raw = NumNodeConversion.toHexCharArr_noPrefix(in);
		this.push(raw);

		if (minNumChars - 2 > 0)
		{
			this.push_internal_noZeroCheck('0', minNumChars - raw.length - 2);
		}

		this.push(HexPrefix);
	}

	/**
	 * pushes "TRUE" if(in), else adds "FALSE"
	 * 
	 * @param in
	 */
	public final void pushAsString(boolean in)
	{
		if (in)
		{
			this.push(BoolToString_True);
		}
		else
		{
			this.push(BoolToString_False);
		}
	}

	public final void pushAsString_ofValues(HalfByteArray in)
	{
		if (in.length() != 0)
		{
			this.push_internal(new Node_HalfByteArr(in, PrintModes.AsValues), in.length() / 2);
		}
	}

	public final void pushAsString_ofChars(HalfByteArray in)
	{
		if (in.length() != 0)
		{
			this.push_internal(new Node_HalfByteArr(in, PrintModes.AsChars), in.length() / 2);
		}
	}

	/**
	 * will push the string "null" if {@code in} is null, otherwise adds the output of the object's {@link Object#toString()} method
	 * 
	 * @param in
	 */
	public final void pushAsString(Object in)
	{
		if (in != null)
		{
			this.add(in.toString());
		}
		else
		{
			this.add_internal_noLengthCheck(NullString);
		}
	}

	public final void pushAsPaddedString(String in, int minLength, boolean padToFront)
	{
		this.pushAsPaddedString(in, ' ', minLength, padToFront);
	}

	public final void pushAsPaddedString(String in, char pad, int minLength, boolean padToFront)
	{
		final int dif = minLength - in.length();

		if (dif > 0)
		{
			if (padToFront)
			{
				this.push(in);
				this.push(pad, dif);
			}
			else
			{
				this.push(pad, dif);
				this.push(in);
			}
		}
	}

	public final void push_asCodepoint(int codepoint)
	{
		if ((codepoint & 0xffff0000) != 0)
		{
			final char[] raw = new char[2];
			raw[0] = (char) ((codepoint >> 16) & 0xffff);
			raw[1] = (char) (codepoint & 0xffff);
			this.push(raw);
		}
		else
		{
			this.push((char) codepoint);
		}
	}

	/**
	 * Convenience method: just like calling {@link #push(String)} with an argument of "\r\n"
	 */
	public final void pushNewLine()
	{
		this.push(NewLine);
	}

	/**
	 * Convenience method: just like calling {@link #push(String, int)} with arguments of "\r\n" and {@code numRepeats}, respectively
	 */
	public final void pushNewLine(int numRepeats)
	{
		this.push(NewLine, numRepeats);
	}

	/**
	 * Convenience method: just like calling {@link #push(char, int)} with arguments '\t' and {@code offset}, then calling {@link #pushNewLine()}
	 * 
	 * @param offset
	 */
	public final void pushNewIndentedLine(int offset)
	{
		this.push('\t', offset);
		this.push(NewLine);
	}

	/**
	 * Convenience method: just like calling {@link #push(char)} with an argument of {@link CharList#Tab}
	 */
	public final void pushTab()
	{
		this.push(Tab);
	}

	/**
	 * Convenience method: just like calling {@link #push(char, int)} with arguments of {@link CharList#Tab} and {@code numRepeats}
	 */
	public final void pushTab(int numRepeats)
	{
		this.push(Tab);
	}

	/**
	 * Convenience method: just like calling {@link #push(char[])} with an argument of {@link CharList#spacedEqualSign}
	 */
	public final void pushSpacedEqualSign()
	{
		this.push(spacedEqualSign);
	}

	public final void insert(boolean shouldAdd, char in)
	{
		if (shouldAdd)
		{
			this.add_internal(new Node_Char(in), 1);
		}
		else
		{
			this.push_internal(new Node_Char(in), 1);
		}
	}

	public final void insert(boolean shouldAdd, char in, int numRepeats)
	{
		if (shouldAdd)
		{
			this.add(in, numRepeats);
		}
		else
		{
			this.push(in, numRepeats);
		}
	}

	public final void insert(boolean shouldAdd, char[] in)
	{
		if (shouldAdd)
		{
			this.add(in);
		}
		else
		{
			this.push(in);
		}
	}

	public final void insert(boolean shouldAdd, char[] in, int numRepeats)
	{
		if (shouldAdd)
		{
			this.add(in, numRepeats);
		}
		else
		{
			this.push(in, numRepeats);
		}
	}

	public final void insert(boolean shouldAdd, String in)
	{
		if (shouldAdd)
		{
			this.add(in);
		}
		else
		{
			this.push(in);
		}
	}

	public final void insert(boolean shouldAdd, String in, int numRepeats)
	{
		if (shouldAdd)
		{
			this.add(in, numRepeats);
		}
		else
		{
			this.push(in, numRepeats);
		}
	}

	public final void insert(boolean shouldAdd, String[] in)
	{
		if (shouldAdd)
		{
			this.add(in);
		}
		else
		{
			this.push(in);
		}
	}

	public final void insert(boolean shouldAdd, String[] in, int numRepeats)
	{
		if (shouldAdd)
		{
			this.add(in, numRepeats);
		}
		else
		{
			this.push(in, numRepeats);
		}
	}

	public final void insert(boolean shouldAdd, String[] in, char delimiterToInsert)
	{
		if (shouldAdd)
		{
			this.add(in, delimiterToInsert);
		}
		else
		{
			this.push(in, delimiterToInsert);
		}
	}

	public final void insert(boolean shouldAdd, String[] in, String delimiterToInsert)
	{
		if (shouldAdd)
		{
			this.add(in, delimiterToInsert);
		}
		else
		{
			this.push(in, delimiterToInsert);
		}
	}

	public final void insert(boolean shouldAdd, CharList in, boolean canConsume)
	{
		if (shouldAdd)
		{
			this.add(in, canConsume);
		}
		else
		{
			this.push(in, canConsume);
		}
	}

	/**
	 * {@code in} is not consumed by this method, as there is no point
	 * 
	 * @param in
	 * @param numRepeats
	 */
	public final void insert(boolean shouldAdd, CharList in, int numRepeats)
	{
		if (shouldAdd)
		{
			this.add(in.toCharArray(), numRepeats);
		}
		else
		{
			this.push(in.toCharArray(), numRepeats);
		}
	}

	/**
	 * functionally equivalent to calling both push(bookend) and add(bookend)
	 * 
	 * @param bookend
	 */
	public final void bookend(char bookend)
	{
		this.push(bookend);
		this.add(bookend);
	}

	/**
	 * 
	 * @return the same as calling parseSelfAsBoolean(true);
	 */
	public final boolean parseSelfAsBoolean()
	{
		return this.parseSelfAsBoolean(true);
	}

	public final boolean parseSelfAsBoolean(boolean parseRigidly)
	{
		if (parseRigidly)
		{
			return this.parseSelfAsRigidBoolean();
		}
		else
		{
			return this.parseSelfAsLooseBoolean();
		}
	}

	/**
	 * this method sponsored by Finite State Machines. Finite State Machines: Who Needs Memory!
	 * 
	 * @return {@code false} if {@code this} == "f", "F", "false", "False", "0", or is empty, {@code true} otherwise
	 */
	public final boolean parseSelfAsLooseBoolean()
	{
		switch (this.sizeInChars)
		{
			case 0:
			{
				return false;
			}
			case 1:
			{
				return this.firstChar() != '0';
			}
			case 5:
			{
				final linearCharsIterator itsy = this.getLinearCharsIterator();

				switch (itsy.next())
				{
					case 'f':
					case 'F':
					{
						return !((itsy.next() == 'a') && (itsy.next() == 'l') && (itsy.next() == 's') && (itsy.next() == 'e'));
					}
					case '0':
					{
						if ((itsy.next() == '0') && (itsy.next() == '0') && (itsy.next() == '0') && (itsy.next() == '0'))
						{
							return false;
						}
						else
						{
							return true;
						}
					}
					default:
					{
						return true;
					}
				}
			}
			default:
			{
				final linearCharsIterator itsy = this.getLinearCharsIterator();

				while (itsy.hasNext())
				{
					if (itsy.next() != '0')
					{
						return true;
					}
				}

				return false;
			}
		}
	}

	/**
	 * 
	 * @return {@code true} if {@code this} == "t", "T", "true", "True", "1", {@code false} if {@code this} == "f", "F", "false", "False", "0", throws BadParseException otherwise
	 */
	public final boolean parseSelfAsRigidBoolean()
	{
		Logic: switch (this.sizeInChars)
		{
			case 0:
			{
				throw new BadParseException("CharList was of size 0!");
			}
			case 1:
			{
				switch (this.firstChar())
				{
					case 't':
					case 'T':
					case '1':
					{
						return true;
					}
					case 'f':
					case 'F':
					case '0':
					{
						return false;
					}
					default:
					{
						throw new BadParseException("CharList had dirty char!");
					}
				}
			}
			case 4: // must match "true", "True", "0000", or "0001"
			{
				final linearCharsIterator itsy = this.getLinearCharsIterator();

				switch (itsy.next())
				{
					case 't':
					case 'T':
					{
						return (itsy.next() == 'r') && (itsy.next() == 'u') && (itsy.next() == 'e');
					}
					case '0':
					{
						if ((itsy.next() == '0') && (itsy.next() == '0'))
						{
							switch (itsy.next())
							{
								case '0':
								{
									return false;
								}
								case '1':
								{
									return true;
								}
								default:
								{
									break Logic;
								}
							}
						}
					}
					default:
					{
						break Logic;
					}
				}
			}
			case 5: // must match "false", "False", "00000", or "00001"
			{
				final linearCharsIterator itsy = this.getLinearCharsIterator();

				switch (itsy.next())
				{
					case 'f':
					case 'F':
					{
						return !((itsy.next() == 'a') && (itsy.next() == 'l') && (itsy.next() == 's') && (itsy.next() == 'e'));
					}
					case '0':
					{
						if ((itsy.next() == '0') && (itsy.next() == '0') && (itsy.next() == '0'))
						{
							switch (itsy.next())
							{
								case '0':
								{
									return false;
								}
								case '1':
								{
									return true;
								}
								default:
								{
									break Logic;
								}
							}
						}
					}
					default:
					{
						break Logic;
					}
				}
			}
			default:
			{
				final linearCharsIterator itsy = this.getLinearCharsIterator();

				while (itsy.hasNext())
				{
					switch (itsy.next())
					{
						case 0:
						{
							break;
						}
						case 1:
						{
							if (!itsy.hasNext())
							{
								return true;
							}
							else
							{
								break Logic;
							}
						}
						default:
						{
							break Logic;
						}
					}
				}

				return false;
			}
		}

		throw new BadParseException("CharList had dirty char!");
	}

	public final byte parseSelfAsByte()
	{
		final int result = this.parseSelfAsInt();

		if (result > Byte.MAX_VALUE || result < Byte.MIN_VALUE)
		{
			throw new BadParseException();
		}
		else
		{
			return (byte) this.parseSelfAsInt();
		}
	}

	public final short parseSelfAsShort()
	{
		final int result = this.parseSelfAsInt();

		if (result > Short.MAX_VALUE || result < Short.MIN_VALUE)
		{
			throw new BadParseException();
		}
		else
		{
			return (short) this.parseSelfAsInt();
		}
	}

	public final int parseSelfAsInt()
	{
		switch (this.sizeInChars)
		{
			case 0:
			{
				throw new IllegalArgumentException("Empty CharList!");
			}
			case 1:
			{
				final char first = this.firstChar();
				return parseSingleChar(first, true);
			}
			case 2:
			{
				final char first = this.firstChar();

				if (first == '-')
				{
					return parseSingleChar(this.charAt(1), false);
				}
				else
				{
					return (parseSingleChar(first, true) * 10) + parseSingleChar(this.charAt(1), true);
				}
			}
			case 3:
			{
				final char first = this.firstChar();
				final char second = this.charAt(1);
				final char third = this.lastChar();

				switch (first)
				{
					case '-':
					{
						return (parseSingleChar(second, false) * 10) + parseSingleChar(third, false);
					}
					case '0':
					{
						switch (second)
						{
							case 'x':
							{
								return parseSingleHexChar(third);
							}
							case 'b':
							{
								return (third == '0' ? 0 : 1);
							}
							default:
							{
								return (parseSingleChar(second, true) * 10) + parseSingleChar(third, true);
							}
						}
					}
					default:
					{
						return (parseSingleChar(first, true) * 100) + (parseSingleChar(second, true) * 10) + parseSingleChar(third, true);
					}
				}
			}
			default:
			{
				final linearCharsIterator itsy = this.getCharIterator();
				final char first = itsy.next();
				final boolean isPositive;
				int result;

				switch (first)
				{
					case '-':
					{
						isPositive = false;
						result = 0;
						break;
					}
					case '0':
					{
						result = 0;
						isPositive = true;
						final char second = itsy.next();

						switch (second)
						{
							case 'x':
							{
								int scalar = 1 << (this.sizeInChars - 3) * 4;

								while (itsy.hasNext())
								{
									result += (parseSingleHexChar(itsy.next()) * scalar);
									scalar >>>= 4;
								}

								return result;
							}
							case 'b':
							{
								while (itsy.hasNext())
								{
									result <<= 1;

									if (itsy.next() == '1')
									{
										result++;
									}
								}

								return result;
							}
							default:
							{
								result = parseSingleChar(first, true);
								break;
							}
						}
						break;
					}
					default:
					{
						isPositive = true;
						result = parseSingleChar(first, true);
						break;
					}
				}

				while (itsy.hasNext())
				{
					result *= 10;
					result += parseSingleChar(itsy.next(), isPositive);
				}

				return result;
			}
		}
	}

	public final double parseSelfAsDouble()
	{
		switch (this.sizeInChars)
		{
			case 0:
			{
				throw new IllegalArgumentException("Empty CharList!");
			}
			case 1:
			{
				return parseSingleChar(this.firstChar(), true);
			}
			case 2:
			{
				final char firstChar = this.firstChar();
				final char secondChar = this.lastChar();

				switch (firstChar)
				{
					case '-':
					{
						return parseSingleChar(secondChar, false);
					}
					case '.':
					{
						return ((double) parseSingleChar(secondChar, true)) / 10;
					}
					default:
					{
						if (secondChar != '.')
						{
							return (parseSingleChar(firstChar, true) * 10) + parseSingleChar(secondChar, true);
						}
						else
						{
							return parseSingleChar(firstChar, true);
						}
					}
				}
			}
			default:
			{
				final linearCharsIterator itsy = this.getCharIterator();
				final char firstChar = itsy.next();

				switch (firstChar)
				{
					case '-':
					{
						return handleRemainingChars_double(0.0d, itsy, false) * -1.0d;
					}
					case '.':
					{
						return handleRemainingChars_double(0.0d, itsy, true);
					}
					default:
					{
						return handleRemainingChars_double(parseSingleChar(firstChar, true), itsy, false);
					}
				}
			}
		}
	}

	public static final char[] getNewLineChars()
	{
		return CharList.NewLine;
	}

	private static final double handleRemainingChars_double(double initial, linearCharsIterator itsy, boolean treatAsFractional)
	{
		if (treatAsFractional)
		{
			double fraction = 10;

			while (itsy.hasNext())
			{
				initial += ((double) parseSingleChar(itsy.next(), true)) / fraction;
				fraction *= 10;
			}
		}
		else
		{
			while (itsy.hasNext())
			{
				final char curr = itsy.next();

				if (curr == '.')
				{
					return handleRemainingChars_double(initial, itsy, true);
				}
				else
				{
					initial = (initial * 10) + parseSingleChar(curr, true);
				}
			}
		}

		return initial;
	}

	private static int parseSingleChar(char in, boolean wasPositive)
	{
		if (in == '0')
		{
			return 0;
		}
		else
		{
			if (wasPositive)
			{
				switch (in)
				{
					case '1':
					{
						return 1;
					}
					case '2':
					{
						return 2;
					}
					case '3':
					{
						return 3;
					}
					case '4':
					{
						return 4;
					}
					case '5':
					{
						return 5;
					}
					case '6':
					{
						return 6;
					}
					case '7':
					{
						return 7;
					}
					case '8':
					{
						return 8;
					}
					case '9':
					{
						return 9;
					}
					default:
					{
						throw new BadParseException("CharList had dirty char!");
					}
				}
			}
			else
			{
				switch (in)
				{
					case '1':
					{
						return -1;
					}
					case '2':
					{
						return -2;
					}
					case '3':
					{
						return -3;
					}
					case '4':
					{
						return -4;
					}
					case '5':
					{
						return -5;
					}
					case '6':
					{
						return -6;
					}
					case '7':
					{
						return -7;
					}
					case '8':
					{
						return -8;
					}
					case '9':
					{
						return -9;
					}
					default:
					{
						throw new BadParseException("CharList had dirty char!");
					}
				}
			}
		}
	}

	private static int parseSingleHexChar(char in)
	{
		switch (in)
		{
			case '0':
			{
				return 0;
			}
			case '1':
			{
				return 1;
			}
			case '2':
			{
				return 2;
			}
			case '3':
			{
				return 3;
			}
			case '4':
			{
				return 4;
			}
			case '5':
			{
				return 5;
			}
			case '6':
			{
				return 6;
			}
			case '7':
			{
				return 7;
			}
			case '8':
			{
				return 8;
			}
			case '9':
			{
				return 9;
			}
			case 'a':
			case 'A':
			{
				return 10;
			}
			case 'b':
			case 'B':
			{
				return 11;
			}
			case 'c':
			case 'C':
			{
				return 12;
			}
			case 'd':
			case 'D':
			{
				return 13;
			}
			case 'e':
			case 'E':
			{
				return 14;
			}
			case 'f':
			case 'F':
			{
				return 15;
			}
			default:
			{
				throw new BadParseException("CharList had dirty char!");
			}
		}
	}
}