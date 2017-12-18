package Collections.Lists.CharList.Mk01;

import Collections.PackedArrays.HalfByte.HalfByteArray;

class Node_Char_HalfByteArr extends Node implements MultiNode
{
	private final HalfByteArray cargo;

	Node_Char_HalfByteArr(HalfByteArray inCargo)
	{
		super();
		this.cargo = inCargo;
	}

	@Override
	public CharList splitCopyIntoNewCharList()
	{
		final Node head = new Node_Char(this.firstChar());
		Node current = head;
		final int length = this.getSizeInChars();
		
		for(int i = 1; i < length; i++)
		{
			current.next = new Node_Char(this.CharAt(i));
			current = current.next;
		}
		
		return new CharList(head, current, length);
	}

	@Override
	public int getSizeInChars()
	{
		return this.cargo.length();
	}

	@Override
	public char CharAt(int inI)
	{
		return (char) this.cargo.getHalfByteAsByteAt(inI);
	}

	@Override
	public char firstChar()
	{
		return (char) this.cargo.getFirstHalfByte();
	}

	@Override
	public char lastChar()
	{
		return (char) this.cargo.getLastHalfByte();
	}

	@Override
	void fillCharArray_internal(char[] inTarget, int inStartIndex)
	{
		for(int i = 0; i < this.cargo.length(); i++)
		{
			inTarget[i + inStartIndex] = (char) this.cargo.getHalfByteAsByteAt(i);
		}
	}

	@Override
	Node copySelf()
	{
		return new Node_Char_HalfByteArr(this.cargo);
	}
}
