package Collections.Lists.CharList.Mk01;

class Node_Char extends Node
{
	private final char cargo;

	public Node_Char(char inCargo)
	{
		this.cargo = inCargo;
	}

	public char getCargo()
	{
		return this.cargo;
	}

	@Override
	public int getSizeInChars()
	{
		return 1;
	}

	@Override
	public char CharAt(int inI)
	{
		if (activateDefensiveMode)
		{
			if(inI != 0)
			{
				throw new NodeIndexOutOfBoundsException(inI, 1);
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
		inTarget[inStartIndex] = this.cargo;
		
		//this.next.fillCharArray_internal(inTarget, inStartIndex + 1);
	}

	@Override
	Node copySelf()
	{
		return new Node_Char(this.cargo);
	}
}
