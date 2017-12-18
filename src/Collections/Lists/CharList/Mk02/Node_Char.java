package Collections.Lists.CharList.Mk02;

class Node_Char extends Node
{
	private final char cargo;

	Node_Char(char inCargo)
	{
		super();
		this.cargo = inCargo;
	}

	Node_Char(char inCargo, Node inNext)
	{
		super(inNext);
		this.cargo = inCargo;
	}

	final char getCargo()
	{
		return this.cargo;
	}

	@Override
	protected final int toCharArray_internal(char[] result, int offset)
	{
		result[offset] = this.cargo;
		return offset + 1;
	}

	@Override
	final char firstChar()
	{
		return this.cargo;
	}

	@Override
	final char lastChar()
	{
		return this.cargo;
	}
}
