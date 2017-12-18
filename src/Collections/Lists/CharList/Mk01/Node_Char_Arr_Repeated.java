package Collections.Lists.CharList.Mk01;

class Node_Char_Arr_Repeated extends Node implements MultiNode
{
	private final char[] cargo;
	private final int numRepeats;

	Node_Char_Arr_Repeated(char[] inCargo, int inNumRepeats)
	{
		super();
		this.cargo = inCargo;
		this.numRepeats = inNumRepeats;
	}

	@Override
	public CharList splitCopyIntoNewCharList()
	{
		if (activateDefensiveMode)
		{
			if (this.numRepeats < 2)
			{
				throw new IllegalArgumentException("something's wrong here");
			}
		}

		final Node secondary = Node.createNode(this.cargo, this.numRepeats - 1);
		final Node first = Node.createNode(this.cargo[0]);
		Node current = first;

		for (int i = 1; i < this.cargo.length; i++)
		{
			final Node nextNode = Node.createNode(this.cargo[i]);
			Node.link(current, nextNode);
			current = nextNode;
		}

		Node.link(current, secondary);

		return new CharList(first, secondary, this.cargo.length * this.numRepeats);
	}

	@Override
	public int getSizeInChars()
	{
		return this.cargo.length * this.numRepeats;
	}

	@Override
	public char CharAt(int inI)
	{
		final int relIndex = inI % this.cargo.length;

		if (activateDefensiveMode)
		{
			if (relIndex >= this.cargo.length)
			{
				System.err.println("absolute index was " + inI);
				throw new NodeIndexOutOfBoundsException(relIndex, this.cargo.length);
			}

			if (inI >= this.getSizeInChars())
			{
				throw new NodeIndexOutOfBoundsException(inI, this.getSizeInChars());
			}
		}

		return this.cargo[relIndex];
	}

	@Override
	public char firstChar()
	{
		return this.cargo[0];
	}

	@Override
	public char lastChar()
	{
		return this.cargo[this.cargo.length - 1];
	}

	@Override
	void fillCharArray_internal(char[] inTarget, int inStartIndex)
	{
		for(int reps = 0; reps < this.numRepeats; reps++)
		{
			for(int i = 0; i < this.cargo.length; i++)
			{
				inTarget[inStartIndex++] = this.cargo[i];
			}
		}
		
		//this.next.fillCharArray_internal(inTarget, inStartIndex);
	}

	@Override
	Node copySelf()
	{
		//can do superShallow copy because nothing outside this package should have internal node access anyway
		return new Node_Char_Arr_Repeated(this.cargo, this.numRepeats);
	}
}
