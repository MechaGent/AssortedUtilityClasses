package Collections.Lists.CharList.Mk02;

class Node_CharArr_Repeated extends RepeatNode
{
	private final char[] cargo;

	Node_CharArr_Repeated(int inNumRepeats, char[] inCargo)
	{
		super(inNumRepeats);
		this.cargo = inCargo;
	}

	Node_CharArr_Repeated(Node inIn, int inNumRepeats, char[] inCargo)
	{
		super(inIn, inNumRepeats);
		this.cargo = inCargo;
	}

	@Override
	final int getLength()
	{
		return this.cargo.length * this.numRepeats;
	}

	@Override
	final CharList splitIntoNewCharList()
	{
		switch (this.cargo.length)
		{
			case 2:
			{
				switch (this.numRepeats)
				{
					case 2:
					{
						final Node_CharArr tail = new Node_CharArr(this.cargo);
						final Node_Char middle = new Node_Char(this.cargo[1], tail);
						final Node_Char head = new Node_Char(this.cargo[0], middle);

						return new CharList(head, tail, 4); // this.cargo.length * this.numRepeats = 4
					}
					default:
					{
						final Node_Char middle = new Node_Char(this.cargo[1], this);
						final Node_Char head = new Node_Char(this.cargo[0], middle);

						return new CharList(head, this, this.numRepeats-- * this.cargo.length);
					}
				}
			}
			default:
			{
				switch (this.numRepeats)
				{
					case 2:
					{
						final Node_CharArr tail = new Node_CharArr(this.cargo);
						Node current = tail;

						for (int i = this.cargo.length - 1; i >= 0; i--)
						{
							current = new Node_Char(this.cargo[i], current);
						}

						return new CharList(current, tail, this.cargo.length * 2);
					}
					default:
					{
						Node current = this;

						for (int i = this.cargo.length - 1; i >= 0; i--)
						{
							current = new Node_Char(this.cargo[i], current);
						}

						return new CharList(current, this, this.cargo.length * this.numRepeats);
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
		for (int i = this.numRepeats; i > 0; i--)
		{
			System.arraycopy(this.cargo, 0, result, offset, this.cargo.length);
			offset += this.cargo.length;
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
		
		return this.cargo[index % this.cargo.length];
	}

	@Override
	final char lastChar()
	{
		return this.cargo[this.cargo.length - 1];
	}
}
