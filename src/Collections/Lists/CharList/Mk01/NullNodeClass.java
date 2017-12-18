package Collections.Lists.CharList.Mk01;

class NullNodeClass extends Node
{
	//static final NullNodeClass NullNode = new NullNodeClass();
	
	NullNodeClass()
	{
		super();
	}
	
	@Override
	public boolean isNull()
	{
		return true;
	}
	
	@Override
	public boolean isNotNull()
	{
		return false;
	}

	@Override
	public int getSizeInChars()
	{
		return -69;
	}

	@Override
	public char CharAt(int inI)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public char firstChar()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public char lastChar()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	void fillCharArray_internal(char[] inTarget, int inStartIndex)
	{
		
	}

	@Override
	Node copySelf()
	{
		return this;
	}
}
