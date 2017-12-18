package Collections.Lists.IntList;

class IntNode_Single extends IntNode
{
	private final int cargo;

	public IntNode_Single(int inCargo)
	{
		super();
		this.cargo = inCargo;
	}

	@Override
	public int getCargoAt(int inIndex)
	{
		if(inIndex == 0)
		{
			return this.cargo;
		}
		else
		{
			throw new IndexOutOfBoundsException("tried for: " + inIndex + ", when length was: 1");
		}
	}

	@Override
	public int getFirstCargo()
	{
		return this.cargo;
	}

	@Override
	public int getLastCargo()
	{
		return this.cargo;
	}

	@Override
	public int getCargoLength()
	{
		return 1;
	}

	@Override
	public IntNode copySelf()
	{
		return new IntNode_Single(this.cargo);
	}

	@Override
	public IntList splitSelf()
	{
		final IntNode copy = this.copySelf();
		
		return new IntList(copy, copy, 1);
	}
}
