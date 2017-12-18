package Collections.Lists.CharList.Mk01;

class Node_Char_Arr extends Node implements MultiNode
{
	private final char[] cargo;
	
	Node_Char_Arr(char[] inCargo)
	{
		this.cargo = inCargo;
	}

	@Override
	public CharList splitCopyIntoNewCharList()
	{
		final Node first = Node.createNode(this.cargo[0]);
		Node current = first;
		
		for(int i = 1; i < this.cargo.length; i++)
		{
			final Node nextNode = Node.createNode(this.cargo[i]);
			Node.link(current, nextNode);
			current = nextNode;
		}
		
		return new CharList(first, current, this.cargo.length);
	}

	@Override
	public int getSizeInChars()
	{
		return this.cargo.length;
	}

	@Override
	public char CharAt(int inI)
	{
		if (activateDefensiveMode)
		{
			if(inI >= this.cargo.length)
			{
				throw new NodeIndexOutOfBoundsException(inI, this.cargo.length);
			}
		}
		
		return this.cargo[inI];
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
		for(int i = 0; i < this.cargo.length; i++)
		{
			inTarget[inStartIndex++] = this.cargo[i];
		}
		
		//this.next.fillCharArray_internal(inTarget, inStartIndex);
	}

	@Override
	Node copySelf()
	{
		return new Node_Char_Arr(this.cargo);
	}
}
