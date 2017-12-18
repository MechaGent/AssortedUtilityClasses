package Collections.Lists.CharList;

import Collections.Lists.Generic.Directed.SingleLinkedList;
import Collections.PackedArrays.HalfByte.HalfByteArray;
import Collections.PackedArrays.HalfByte.HalfByteArrayFactory;
import CustomExceptions.BadParseException;
import Incubator.BytesStreamer.CompletelyDifferent.CharSetTranslations;

public class CharList extends BaseCharList
{
	private static final char[] NewLine = System.lineSeparator().toCharArray();
	private static final char Tab = '\t';
	private static final char[] BinaryPrefix = "0b".toCharArray();
	private static final char[] HexPrefix = "0x".toCharArray();
	private static final char[] NullString = "null".toCharArray();
	private static final char[] BoolToString_True = "TRUE".toCharArray();
	private static final char[] BoolToString_False = "FALSE".toCharArray();
	private static final char[] spacedEqualSign = " = ".toCharArray();
	
	/**
	 * 
	 * @param delim
	 * @return an array of size 2, with both elements always instantiated, though it is possible that at least one is empty.
	 */
	public final CharList[] splitAtFirst(char in)
	{
		final CharList preceding = new CharList();
		final CharList remaining = new CharList();

		this.splitAtFirst(in, preceding, remaining);

		return new CharList[] {
								preceding,
								remaining };
	}

	public final void splitAtFirst(char in, CharList preceding, CharList remaining)
	{
		final CharListIterator itsy = this.listIterator();

		while (itsy.hasNext())
		{
			if (itsy.next() == in)
			{
				final char[] rawPreceding = itsy.precedingToCharArray();
				final char[] rawRemaining = itsy.remainingToCharArray();
				
				preceding.add(rawPreceding);
				remaining.add(rawRemaining);
				
				return;
			}
		}
		
		preceding.add(this);
		return;
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
		final CharListIterator itsy = this.listIterator();

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
	 * @return A {@code HalfByteArray} view of this CharList, backed by this CharList.
	 *         Intended for use with one of the {@code RadixMap} implementations.
	 */
	public final HalfByteArray toHalfByteArray()
	{
		return HalfByteArrayFactory.wrapIntoArray(this.toCharArray());
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
			this.add('0', minNumChars - raw.length);
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
			this.add('0', minNumChars - raw.length);
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
			this.add('0', minNumChars - raw.length());
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
			this.add('0', minNumChars - raw.length - 2);
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
			this.add('0', minNumChars - raw.length - 2);
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
			for(int i = 0; i < in.length(); i += 1)
			{
				this.add(NumNodeConversion.getHexChar_inCaps(in.getHalfByteAt(i)));
			}
		}
	}

	public final void addAsString_ofChars(HalfByteArray in)
	{
		if (in.length() != 0)
		{
			this.add(in.interpretAsCharArr());
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
			this.add(NullString);
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
			this.push('0', minNumChars - raw.length);
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
			this.push('0', minNumChars - raw.length);
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
			this.push('0', minNumChars - raw.length - 2);
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
			this.push('0', minNumChars - raw.length - 2);
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
			for(int i = in.length(); i--> 0;)
			{
				this.push(NumNodeConversion.getHexChar_inCaps(in.getHalfByteAt(i)));
			}
		}
	}

	public final void pushAsString_ofChars(HalfByteArray in)
	{
		if (in.length() != 0)
		{
			this.push(in.interpretAsCharArr());
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
			this.push(in.toString());
		}
		else
		{
			this.push(NullString);
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
			this.push((char) (codepoint & 0xffff));
			this.push((char) ((codepoint >> 16) & 0xffff));
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
		switch ((int) this.size)
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
				final CharListIterator itsy = this.listIterator();

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
				final CharListIterator itsy = this.listIterator();

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
		Logic: switch ((int) this.size)
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
				final CharListIterator itsy = this.listIterator();

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
				final CharListIterator itsy = this.listIterator();

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
				final CharListIterator itsy = this.listIterator();

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
		switch ((int) this.size)
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
				final CharListIterator itsy = this.listIterator();
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
								int scalar = 1 << (((int) this.size) - 3) * 4;

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
		switch ((int) this.size)
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
				final CharListIterator itsy = this.listIterator();
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
	
	private static final double handleRemainingChars_double(double initial, CharListIterator itsy, boolean treatAsFractional)
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
}
