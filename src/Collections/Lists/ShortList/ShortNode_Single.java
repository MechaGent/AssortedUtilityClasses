package Collections.Lists.ShortList;

class ShortNode_Single extends ShortNode
{
	private final short cargo;

	public ShortNode_Single(short inCargo)
	{
		super();
		this.cargo = inCargo;
	}

	@Override
	public short getCargoAt(int inIndex)
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
	public short getFirstCargo()
	{
		return this.cargo;
	}

	@Override
	public short getLastCargo()
	{
		return this.cargo;
	}

	@Override
	public int getCargoLength()
	{
		return 1;
	}

	@Override
	public ShortNode copySelf()
	{
		return new ShortNode_Single(this.cargo);
	}

	@Override
	public ShortList splitSelf()
	{
		final ShortNode copy = this.copySelf();
		
		return new ShortList(copy, copy, 1);
	}
}
