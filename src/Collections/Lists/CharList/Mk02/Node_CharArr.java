package Collections.Lists.CharList.Mk02;

class Node_CharArr extends MultiNode
{
	private final char[] cargo;

	Node_CharArr(char[] inCargo)
	{
		super();
		this.cargo = inCargo;
	}

	Node_CharArr(Node inIn, char[] inCargo)
	{
		super(inIn);
		this.cargo = inCargo;
	}

	@Override
	final int getLength()
	{
		return this.cargo.length;
	}

	@Override
	final CharList splitIntoNewCharList()
	{
		final Node_Char head = new Node_Char(this.cargo[0]);
		Node_Char current = head;

		for (int i = 1; i < this.cargo.length; i++)
		{
			final Node_Char next = new Node_Char(this.cargo[i]);
			current.setNext(next);
			current = next;
		}

		return new CharList(head, current, this.cargo.length);
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
		System.arraycopy(this.cargo, 0, result, offset, this.cargo.length);
		return offset + this.cargo.length;
	}

	@Override
	final char CharAt(int index)
	{
		return this.cargo[index];
	}
}
