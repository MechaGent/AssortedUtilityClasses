package Collections.Lists.CharList.Mk02;

class Node_String_Repeated extends RepeatNode
{
	private final String cargo;

	Node_String_Repeated(int inNumRepeats, String inCargo)
	{
		super(inNumRepeats);
		this.cargo = inCargo;
	}

	Node_String_Repeated(Node inIn, int inNumRepeats, String inCargo)
	{
		super(inIn, inNumRepeats);
		this.cargo = inCargo;
	}

	@Override
	final int getLength()
	{
		return this.cargo.length() * this.numRepeats;
	}

	@Override
	final CharList splitIntoNewCharList()
	{
		switch (this.cargo.length())
		{
			case 2:
			{
				switch (this.numRepeats)
				{
					case 2:
					{
						final Node_String tail = new Node_String(this.cargo);
						return new CharList(new Node_Char(this.cargo.charAt(0), new Node_Char(this.cargo.charAt(1), tail)), tail, 4); // 2 * 2 = 4
					}
					default:
					{
						return new CharList(new Node_Char(this.cargo.charAt(0), new Node_Char(this.cargo.charAt(1), this)), this, this.cargo.length() * this.numRepeats--);
					}
				}
			}
			default:
			{
				switch (this.numRepeats)
				{
					case 2:
					{
						final Node_String tail = new Node_String(this.cargo);
						Node current = tail;

						for (int i = this.cargo.length() - 1; i >= 0; i--)
						{
							current = new Node_Char(this.cargo.charAt(i), current);
						}

						return new CharList(current, tail, this.cargo.length() * 2);
					}
					default:
					{
						int i = this.cargo.length() - 1;
						Node_Char current = new Node_Char(this.cargo.charAt(i--), this);

						for (; i >= 0; i--)
						{
							current = new Node_Char(this.cargo.charAt(i), current);
						}

						return new CharList(current, this, this.cargo.length() * this.numRepeats--);
					}
				}
			}
		}
	}
	
	@Override
	final CharList splitIntoNewCharList_skipFirst()
	{
		final CharList result = this.splitIntoNewCharList();
		result.pop();
		return result;
	}

	@Override
	protected final int toCharArray_internal(char[] result, int offset)
	{
		final int length = this.cargo.length();
		
		for (int i = this.numRepeats; i > 0; i--)
		{
			this.cargo.getChars(0, length, result, offset);
			offset += length;
		}

		return offset;
	}

	@Override
	final char CharAt(int index)
	{
		final int length = this.getLength();
		
		if(index < 0 || index >= length)
		{
			throw new IndexOutOfBoundsException();
		}
		
		return this.cargo.charAt(index % length);
	}

	@Override
	final char lastChar()
	{
		return this.cargo.charAt(this.cargo.length() - 1);
	}
}
