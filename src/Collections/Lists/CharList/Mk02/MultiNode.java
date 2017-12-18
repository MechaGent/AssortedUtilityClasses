package Collections.Lists.CharList.Mk02;

abstract class MultiNode extends Node
{
	MultiNode()
	{
		super();
	}
	
	MultiNode(Node in)
	{
		super(in);
	}
	
	abstract char CharAt(int index);
	abstract int getLength();
	abstract CharList splitIntoNewCharList();
	abstract CharList splitIntoNewCharList_skipFirst();
	
	@Override
	final char firstChar()
	{
		return this.CharAt(0);
	}
	
	char lastChar()
	{
		return this.CharAt(this.getLength() - 1);
	}
}
