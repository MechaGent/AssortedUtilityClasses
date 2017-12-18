package Collections.Lists.DubList;

class DubNode_Single extends DubNode
{
	private final double cargo;

	public DubNode_Single(double inCargo)
	{
		super();
		this.cargo = inCargo;
	}

	@Override
	public final double getCargoAt(int inIndex)
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
	public final double getFirstCargo()
	{
		return this.cargo;
	}

	@Override
	public final double getLastCargo()
	{
		return this.cargo;
	}

	@Override
	public final int getCargoLength()
	{
		return 1;
	}

	@Override
	public final DubNode copySelf()
	{
		return new DubNode_Single(this.cargo);
	}

	@Override
	public final DubList splitSelf()
	{
		final DubNode copy = this.copySelf();
		
		return new DubList(copy, copy, 1);
	}
}
