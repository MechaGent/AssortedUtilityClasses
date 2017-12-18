package Collections.Lists.CharList.Mk02;

class Node_Char_Repeated extends RepeatNode
{
	final char cargo;

	Node_Char_Repeated(int inNumRepeats, char inCargo)
	{
		super(inNumRepeats);
		this.cargo = inCargo;
	}

	Node_Char_Repeated(Node inIn, int inNumRepeats, char inCargo)
	{
		super(inIn, inNumRepeats);
		this.cargo = inCargo;
	}

	@Override
	final int getLength()
	{
		return this.getNumRepeats();
	}

	@Override
	final CharList splitIntoNewCharList()
	{
		switch (this.numRepeats)
		{
			case 2:
			{
				final Node_Char head = new Node_Char(this.cargo);
				final Node_Char tail = new Node_Char(this.cargo);
				head.setNext(tail);
				return new CharList(head, tail, 2);
			}
			default:
			{
				final Node_Char head = new Node_Char(this.cargo);
				head.setNext(this);
				return new CharList(head, this, this.getAndDecrementNumRepeats());
			}
		}
	}
	
	@Override
	final CharList splitIntoNewCharList_skipFirst()
	{
		switch(this.numRepeats)
		{
			case 2:
			{
				final Node_Char head = new Node_Char(this.cargo);
				return new CharList(head, head, 1);
			}
			case 3:
			{
				final Node_Char head = new Node_Char(this.cargo);
				final Node_Char tail = new Node_Char(this.cargo);
				head.setNext(tail);
				return new CharList(head, tail, 2);
			}
			default:
			{
				final Node_Char head = new Node_Char(this.cargo);
				head.setNext(this);
				return new CharList(head, this, this.decrementAndGetNumRepeats());
			}
		}
	}

	@Override
	protected final int toCharArray_internal(char[] result, int offset)
	{
		for (int i = this.numRepeats; i > 0; i--)
		{
			result[offset++] = this.cargo;
		}

		return offset;
	}

	@Override
	final char CharAt(int index)
	{
		if(index < 0 || index > this.numRepeats)
		{
			throw new IndexOutOfBoundsException();
		}
		
		return this.cargo;
	}

	@Override
	final char lastChar()
	{
		return this.cargo;
	}
}
