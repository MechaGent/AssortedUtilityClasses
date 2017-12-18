package Collections.Lists.CharList.Mk02;

class Node_String extends MultiNode
{
	private final String cargo;
	
	Node_String(String inCargo)
	{
		super();
		this.cargo = inCargo;
	}

	Node_String(Node inIn, String inCargo)
	{
		super(inIn);
		this.cargo = inCargo;
	}

	@Override
	final int getLength()
	{
		return this.cargo.length();
	}

	@Override
	final CharList splitIntoNewCharList()
	{
		switch(this.cargo.length())
		{
			case 2:
			{
				final Node_Char tail = new Node_Char(this.cargo.charAt(1));
				return new CharList(new Node_Char(this.cargo.charAt(0), tail), tail, 2);
			}
			default:
			{
				int i = this.cargo.length() - 1;
				final Node_Char tail = new Node_Char(this.cargo.charAt(i--));
				Node_Char current = tail;
				
				for(; i >= 0; i--)
				{
					current = new Node_Char(this.cargo.charAt(i), current);
				}
				
				return new CharList(current, tail, this.cargo.length());
			}
		}
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
		final int length = this.cargo.length();
		this.cargo.getChars(0, length, result, offset);
		return offset + length;
	}

	@Override
	char CharAt(int inIndex)
	{
		return this.cargo.charAt(inIndex);
	}
}
