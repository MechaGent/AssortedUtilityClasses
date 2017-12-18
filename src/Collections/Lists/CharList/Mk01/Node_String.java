package Collections.Lists.CharList.Mk01;

class Node_String extends Node implements MultiNode
{
	private final String cargo;
	
	Node_String(String inCargo)
	{
		super();
		this.cargo = inCargo;
		
		if(this.next == null)
		{
			throw new NullPointerException();
		}
	}

	public String getCargo()
	{
		return this.cargo;
	}

	@Override
	public CharList splitCopyIntoNewCharList()
	{
		final Node first = Node.createNode(this.cargo.charAt(0));
		Node current = first;
		
		for(int i = 1; i < this.cargo.length(); i++)
		{
			final Node nextNode = Node.createNode(this.cargo.charAt(i));
			Node.link(current, nextNode);
			current = nextNode;
		}
		
		return new CharList(first, current, this.cargo.length());
	}

	@Override
	public int getSizeInChars()
	{
		return this.cargo.length();
	}

	@Override
	public char CharAt(int inI)
	{
		if (activateDefensiveMode)
		{
			if(inI >= this.cargo.length())
			{
				throw new NodeIndexOutOfBoundsException(inI, this.cargo.length());
			}
		}
		
		return this.cargo.charAt(inI);
	}

	@Override
	public char firstChar()
	{
		return this.cargo.charAt(0);
	}

	@Override
	public char lastChar()
	{
		return this.cargo.charAt(this.cargo.length() - 1);
	}

	@Override
	void fillCharArray_internal(char[] inTarget, int inStartIndex)
	{
		for(int i = 0; i < this.cargo.length(); i++)
		{
			inTarget[inStartIndex++] = this.cargo.charAt(i);
		}
		
		//this.next.fillCharArray_internal(inTarget, inStartIndex);
	}

	@Override
	Node copySelf()
	{
		return new Node_String(this.cargo);
	}
}
