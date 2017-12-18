package Collections.Lists.CharList.Mk01;

class Node_Char_Repeated extends Node implements MultiNode
{
	private final char cargo;
	private final int numRepeats;
	
	Node_Char_Repeated(char inCargo, int inNumRepeats)
	{
		super();
		this.cargo = inCargo;
		this.numRepeats = inNumRepeats;
	}

	public char getCargo()
	{
		return this.cargo;
	}

	public int getNumRepeats()
	{
		return this.numRepeats;
	}

	@Override
	public CharList splitCopyIntoNewCharList()
	{
		final Node first = Node.createNode(this.cargo);
		Node current = first;
		
		for(int i = 1; i < this.numRepeats; i++)
		{
			final Node nextNode = Node.createNode(this.cargo);
			Node.link(current, nextNode);
			current = nextNode;
		}
		
		return new CharList(first, current, this.numRepeats);
	}

	@Override
	public int getSizeInChars()
	{
		return this.numRepeats;
	}

	@Override
	public char CharAt(int inI)
	{
		if (activateDefensiveMode)
		{
			if(inI >= this.numRepeats)
			{
				throw new NodeIndexOutOfBoundsException(inI, this.numRepeats);
			}
		}
		
		return this.cargo;
	}

	@Override
	public char firstChar()
	{
		return this.cargo;
	}

	@Override
	public char lastChar()
	{
		return this.cargo;
	}

	@Override
	void fillCharArray_internal(char[] inTarget, int inStartIndex)
	{
		for(int reps = 0; reps < this.numRepeats; reps++)
		{
			inTarget[inStartIndex++] = this.cargo;
		}
		
		//this.next.fillCharArray_internal(inTarget, inStartIndex);
	}

	@Override
	Node copySelf()
	{
		return new Node_Char_Repeated(this.cargo, this.numRepeats);
	}
	
}
