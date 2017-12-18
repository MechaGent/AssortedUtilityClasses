package Collections.Lists.CharList.Mk01;

import Collections.Lists.Generic.Directed.SingleLinkedList;
import Collections.PackedArrays.HalfByte.HalfByteArray;
import Collections.PackedArrays.HalfByte.HalfByteArrayFactory;
import Collections.PrimitiveInterfaceAnalogues.Char.CharIterable;
import Collections.PrimitiveInterfaceAnalogues.Char.AbstractCharIterator;
import CustomExceptions.BadParseException;

public class CharList implements CharIterable
{
	static final boolean activateDefensiveMode = false;
	static final Node NullNode = new NullNodeClass();

	private static final char[] NewLine = new char[] {
														'\r',
														'\n' };

	Node head;
	Node tail;
	int sizeInChars;

	public CharList()
	{
		this(NullNode, NullNode, 0);
	}

	public CharList(char[] in)
	{
		this.head = new Node_Char_Arr(in);
		this.tail = this.head;
		this.sizeInChars = in.length;
	}

	public CharList(String in)
	{
		this.head = new Node_String(in);
		this.tail = this.head;
		this.sizeInChars = in.length();
	}

	CharList(Node inHead, Node inTail, int inSizeInChars)
	{
		super();
		this.head = inHead;
		this.tail = inTail;
		this.sizeInChars = inSizeInChars;
	}

	public final int size()
	{
		return this.sizeInChars;
	}

	public final boolean isEmpty()
	{
		return this.sizeInChars == 0;
	}

	public final boolean isNotEmpty()
	{
		return this.sizeInChars != 0;
	}

	final CharList copyContentDeeply()
	{
		if (this.sizeInChars != 0)
		{
			return this.head.copyCharList(this);
		}
		else
		{
			return new CharList();
		}
	}

	public final char charAt(int index)
	{
		return getCharAt(index, this.head);
	}

	public final char firstChar()
	{
		return this.head.firstChar();
	}

	public final char lastChar()
	{
		return this.tail.lastChar();
	}

	public final linearCharsIterator getLinearCharsIterator()
	{
		return new linearCharsIterator(this);
	}

	@Override
	public final String toString()
	{
		return new String(this.toCharArray());
	}

	public final char[] toCharArray()
	{
		final char[] result = new char[this.sizeInChars];
		this.head.fillCharArray(result, 0);

		return result;
	}

	public final HalfByteArray toHalfByteArray()
	{
		return HalfByteArrayFactory.wrapIntoArray(this.toCharArray());
	}

	public final void push(char in)
	{
		this.push_internal(Node.createNode(in));
	}

	public final void push(char[] in)
	{
		if (in.length != 0)
		{
			this.push_internal(Node.createNode(in));
		}
	}

	public final void push(char in, int numRepeats)
	{
		if (numRepeats > 0)
		{
			this.push_internal(Node.createNode(in, numRepeats));
		}
	}

	public final void push(char[] in, int numRepeats)
	{
		if (numRepeats > 0 && in.length != 0)
		{
			this.push_internal(Node.createNode(in, numRepeats));
		}
	}

	public final void push(String in)
	{
		if (in.length() != 0)
		{
			this.push_internal(Node.createNode(in));
		}
	}

	public final void push(String[] in)
	{
		for (int i = in.length - 1; i >= 0; i++)
		{
			this.push(in[i]);
		}
	}

	public final void push(String in, int numRepeats)
	{
		if (numRepeats > 0 && in.length() != 0)
		{
			this.push_internal(Node.createNode(in, numRepeats));
		}
	}

	public final void push(String[] in, int numRepeats)
	{
		if (numRepeats > 0)
		{
			if (numRepeats == 1)
			{
				this.push(in);
			}
			else
			{
				final CharList saccy = new CharList();

				for (int i = 0; i < in.length; i++)
				{
					saccy.add(in[i]);
				}

				this.push_internal(Node.createNode(saccy.toString(), numRepeats));
			}
		}
	}

	public final void push(String[] in, char delimiter)
	{
		for (int i = in.length - 1; i >= 0; i--)
		{
			this.push(in[i]);

			if (i != 0)
			{
				this.push(delimiter);
			}
		}
	}

	public final void push(String[] in, String delimiter)
	{
		for (int i = in.length - 1; i >= 0; i--)
		{
			this.push(in[i]);

			if (i != 0)
			{
				this.push(delimiter);
			}
		}
	}

	public final void push(CharList in, boolean canConsume)
	{
		if (in.sizeInChars > 0)
		{
			if (in == this)
			{
				throw new IllegalArgumentException();
			}

			if (!canConsume)
			{
				in = in.copyContentDeeply();
			}

			if (this.sizeInChars > 0)
			{
				Node.link(in.tail, this.head);
				this.head = in.head;
				this.sizeInChars += in.sizeInChars;
			}
			else
			{
				this.head = in.head;
				this.tail = in.tail;
				this.sizeInChars = in.sizeInChars;
			}
		}
	}

	/**
	 * presumes non-consumption
	 * 
	 * @param in
	 * @param numRepeats
	 */
	public final void push(CharList in, int numRepeats)
	{
		if (numRepeats > 0)
		{
			if (in.sizeInChars > 0)
			{
				this.push(in.toCharArray(), numRepeats);
			}
		}
	}

	private final void push_internal(Node in)
	{
		if (in.next == null)
		{
			throw new NullPointerException();
		}

		if (this.sizeInChars == 0)
		{
			this.head = in;
			this.tail = in;
			this.sizeInChars = in.getSizeInChars();
		}
		else
		{
			Node.link(in, this.head);
			this.head = in;
			this.sizeInChars += in.getSizeInChars();
		}
	}

	public final void add(char in)
	{
		this.add_internal(Node.createNode(in));
	}

	public final void add(char[] in)
	{
		if (in.length != 0)
		{
			this.add_internal(Node.createNode(in));
		}
	}

	public final void add(char in, int numRepeats)
	{
		if (numRepeats > 0)
		{
			this.add_internal(Node.createNode(in, numRepeats));
		}
	}

	public final void add(char[] in, int numRepeats)
	{
		if (numRepeats > 0 && in.length != 0)
		{
			this.add_internal(Node.createNode(in, numRepeats));
		}
	}

	public final void add(String in)
	{
		if (in.length() != 0)
		{
			final Node out = Node.createNode(in);

			if (out.next == null)
			{
				throw new NullPointerException();
			}

			this.add_internal(out);
		}
	}

	public final void add(String[] in)
	{
		if (in.length != 0)
		{
			for (int i = 0; i < in.length; i++)
			{
				this.add(in[i]);
			}
		}
	}

	public final void add(String in, int numRepeats)
	{
		if (numRepeats > 0 && in.length() != 0)
		{
			this.add_internal(Node.createNode(in, numRepeats));
		}
	}

	public final void add(String[] in, int numRepeats)
	{
		if (numRepeats > 0)
		{
			if (numRepeats == 1)
			{
				this.add(in);
			}
			else
			{
				final CharList saccy = new CharList();

				for (int i = 0; i < in.length; i++)
				{
					saccy.add(in[i]);
				}

				this.add_internal(Node.createNode(saccy.toString(), numRepeats));
			}
		}
	}

	public final void add(String[] in, char delimiter)
	{
		for (int i = 0; i < in.length; i++)
		{
			if (i != 0)
			{
				this.add(delimiter);
			}

			this.add(in[i]);
		}
	}

	public final void add(String[] in, String delimiter)
	{
		for (int i = 0; i < in.length; i++)
		{
			if (i != 0)
			{
				this.add(delimiter);
			}

			this.add(in[i]);
		}
	}

	public final void add(CharList in, boolean canConsume)
	{
		if (in.sizeInChars > 0)
		{
			if (in == this)
			{
				throw new IllegalArgumentException();
			}

			if (!canConsume)
			{
				in = in.copyContentDeeply();
			}

			if (this.sizeInChars > 0)
			{
				Node.link(this.tail, in.head);
				this.tail = in.tail;
				this.sizeInChars += in.sizeInChars;
			}
			else
			{
				this.head = in.head;
				this.tail = in.tail;
				this.sizeInChars = in.sizeInChars;
			}
		}
	}

	/**
	 * presumes non-consumption
	 * 
	 * @param in
	 * @param numRepeats
	 */
	public final void add(CharList in, int numRepeats)
	{
		if (numRepeats > 0)
		{
			if (in.sizeInChars > 0)
			{
				this.add(in.toCharArray(), numRepeats);
			}
		}
	}

	final void add_internal(Node in)
	{
		if (in.next == null)
		{
			throw new NullPointerException();
		}

		if (this.sizeInChars == 0)
		{
			this.head = in;
			this.tail = in;
			this.sizeInChars = in.getSizeInChars();
		}
		else
		{
			Node.link(this.tail, in);
			this.tail = in;
			this.sizeInChars += in.getSizeInChars();
		}
	}

	public final void push_asBinString(int in)
	{
		this.push(NumNodeConversion.toBinString(in));
		this.push("0b");
	}

	public final void push_asBinString(long in)
	{
		this.push(NumNodeConversion.toBinString(in));
		this.push("0b");
	}

	public final void push_asDecString(int in)
	{
		this.push(NumNodeConversion.toDecString(in));
	}

	public final void push_asDecString(long in)
	{
		this.push(NumNodeConversion.toDecString(in));
	}

	public final void push_asHexString(int in)
	{
		this.push(NumNodeConversion.toHexString(in));
		this.push("0x");
	}

	public final void push_asHexString(long in)
	{
		this.push(NumNodeConversion.toHexString(in));
		this.push("0x");
	}

	public final void push_asBinString(int in, int minNumChars)
	{
		final String raw = NumNodeConversion.toBinString(in);
		this.push(raw);
		this.push('0', minNumChars - raw.length() - 2);
		this.push("0b");
	}

	public final void push_asBinString(long in, int minNumChars)
	{
		final String raw = NumNodeConversion.toBinString(in);
		this.push(raw);
		this.push('0', minNumChars - raw.length() - 2);
		this.push("0b");
	}

	public final void push_asDecString(int in, int minNumChars)
	{
		final String raw = NumNodeConversion.toDecString(in);
		this.push(raw);
		this.push('0', minNumChars - raw.length());
	}

	public final void push_asDecString(long in, int minNumChars)
	{
		final String raw = NumNodeConversion.toDecString(in);
		this.push(raw);
		this.push('0', minNumChars - raw.length());
	}

	public final void push_asHexString(int in, int minNumChars)
	{
		final String raw = NumNodeConversion.toHexString(in);
		this.push(raw);
		this.push('0', minNumChars - raw.length() - 2);
		this.push("0x");
	}

	public final void push_asHexString(long in, int minNumChars)
	{
		final String raw = NumNodeConversion.toHexString(in);
		this.push(raw);
		this.push('0', minNumChars - raw.length() - 2);
		this.push("0x");
	}

	public final void pushAsString(HalfByteArray in)
	{
		if (in.length() != 0)
		{
			this.push_internal(Node.createNode(in));
		}
	}

	public final void pushNewLine()
	{
		this.push(NewLine);
	}

	public final void pushNewLine(int numRepeats)
	{
		this.push(NewLine, numRepeats);
	}

	public final void pushNewIndentedLine(int offset)
	{
		this.push('\t', offset);
		this.push(NewLine);
	}

	/*
	 * after this
	 */

	public final void add_asBinString(int in)
	{
		this.add("0b");
		this.add(NumNodeConversion.toBinString(in));
	}

	public final void add_asBinString(long in)
	{
		this.add("0b");
		this.add(NumNodeConversion.toBinString(in));
	}

	public final void add_asDecString(int in)
	{
		this.add(NumNodeConversion.toDecString(in));
	}

	public final void add_asDecString(long in)
	{
		this.add(NumNodeConversion.toDecString(in));
	}

	public final void add_asHexString(int in)
	{
		this.add("0x");
		this.add(NumNodeConversion.toHexString(in));
	}

	public final void add_asHexString(long in)
	{
		this.add("0x");
		this.add(NumNodeConversion.toHexString(in));
	}

	public final void add_asBinString(int in, int minNumChars)
	{
		final String raw = NumNodeConversion.toBinString(in);
		this.add("0b");
		this.add('0', minNumChars - raw.length() - 2);
		this.add(raw);
	}

	public final void add_asBinString(long in, int minNumChars)
	{
		final String raw = NumNodeConversion.toBinString(in);
		this.add("0b");
		this.add('0', minNumChars - raw.length() - 2);
		this.add(raw);
	}

	public final void add_asDecString(int in, int minNumChars)
	{
		final String raw = NumNodeConversion.toDecString(in);
		this.add('0', minNumChars - raw.length());
		this.add(raw);
	}

	public final void add_asDecString(long in, int minNumChars)
	{
		final String raw = NumNodeConversion.toDecString(in);
		this.add('0', minNumChars - raw.length());
		this.add(raw);
	}

	public final void add_asHexString(int in, int minNumChars)
	{
		final String raw = NumNodeConversion.toHexString(in);
		this.add("0x");
		this.add('0', minNumChars - raw.length() - 2);
		this.add(raw);
	}

	public final void add_asHexString(long in, int minNumChars)
	{
		final String raw = NumNodeConversion.toHexString(in);
		this.add("0x");
		this.add('0', minNumChars - raw.length() - 2);
		this.add(raw);
	}

	public final void addAsString(boolean in)
	{
		final String raw;

		if (in)
		{
			raw = "TRUE";
		}
		else
		{
			raw = "FALSE";
		}

		this.add_internal(Node.createNode(raw));
	}

	public final void addAsString(int in)
	{
		this.add_internal(Node.createNode(NumNodeConversion.toDecString(in)));
	}

	public final void addAsString(long in)
	{
		this.add_internal(Node.createNode(NumNodeConversion.toDecString(in)));
	}

	public final void addAsString(double in)
	{
		this.add_internal(Node.createNode(NumNodeConversion.toDecString(in)));
	}

	public final void addAsString(HalfByteArray in)
	{
		if (in.length() != 0)
		{
			this.add_internal(Node.createNode(in));
		}
	}

	public final void addAsString(Object in)
	{
		if (in != null)
		{
			this.add_internal(Node.createNode(in.toString()));
		}
		else
		{
			this.add_internal(Node.createNode("null"));
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
			final Node padding = Node.createNode(pad, dif);

			if (padToFront)
			{
				this.add_internal(padding);
				this.add_internal(Node.createNode(in));
			}
			else
			{
				this.add_internal(Node.createNode(in));
				this.add_internal(padding);
			}
		}
	}

	public final void addNewLine()
	{
		this.add(NewLine);
	}

	public final void addNewLine(int numRepeats)
	{
		this.add(NewLine, numRepeats);
	}

	public final void addNewIndentedLine(int offset)
	{
		this.add(NewLine);
		this.add('\t', offset);
	}

	static final char getCharAt(int index, Node head)
	{
		int total = 0;
		Node current = head;

		while (true)
		{
			if (current == NullNode)
			{
				throw new IndexOutOfBoundsException();
			}

			final int curSize = current.getSizeInChars();

			if ((total + curSize) > index)
			{
				index = index - total;

				return current.CharAt(index);
			}
			else
			{
				current = current.next;
				total += curSize;
			}
		}
	}

	@Override
	public final AbstractCharIterator getCharIterator()
	{
		return new linearCharsIterator(this);
	}

	public final CharList[] splitAtFirst(char delim)
	{
		final linearCharsIterator itsy = this.getLinearCharsIterator();
		final CharList var1 = new CharList();
		final CharList var2 = new CharList();
		char curr;

		while (itsy.hasNext() && (curr = itsy.next()) != delim)
		{
			var1.add(curr);
		}

		while (itsy.hasNext())
		{
			var2.add(itsy.next());
		}

		return new CharList[] {
								var1,
								var2 };
	}

	public final SingleLinkedList<CharList> splitAt(char delim)
	{
		SingleLinkedList<CharList> result = new SingleLinkedList<CharList>();
		final linearCharsIterator itsy = this.getLinearCharsIterator();
		CharList currList = new CharList();

		while (itsy.hasNext())
		{
			final char curr = itsy.next();

			if (curr == delim)
			{
				result.add(currList);
				currList = new CharList();
			}
			else
			{
				currList.add(curr);
			}
		}

		return result;
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
		if(parseRigidly)
		{
			return this.parseSelfAsRigidBoolean();
		}
		else
		{
			return this.parseSelfAsLooseBoolean();
		}
	}
	
	/**
	 * 
	 * @return {@code false} if {@code this} == "f", "F", "false", "False", "0", or is empty, {@code true} otherwise
	 */
	public final boolean parseSelfAsLooseBoolean()
	{
		switch(this.sizeInChars)
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
				
				while(itsy.hasNext())
				{
					if(itsy.next() != '0')
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

				while(itsy.hasNext())
				{
					switch(itsy.next())
					{
						case 0:
						{
							break;
						}
						case 1:
						{
							if(!itsy.hasNext())
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
}
