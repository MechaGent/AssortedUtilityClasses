package Collections.Lists.DubList;

class DubNode_Arr extends DubNode
{
	private final int[] cargo;
	
	public DubNode_Arr(int[] inCargo)
	{
		super();
		this.cargo = inCargo;
	}

	@Override
	public final double getCargoAt(int inIndex)
	{
		return this.cargo[inIndex];
	}

	@Override
	public final double getFirstCargo()
	{
		return this.cargo[0];
	}

	@Override
	public final double getLastCargo()
	{
		return this.cargo[this.cargo.length - 1];
	}

	@Override
	public final int getCargoLength()
	{
		return this.cargo.length;
	}

	@Override
	public final DubNode copySelf()
	{
		return new DubNode_Arr(this.cargo);
	}

	@Override
	public final DubList splitSelf()
	{
		final DubNode_Single head = new DubNode_Single(this.cargo[0]);
		DubNode_Single current = head;
		
		for(int i = 1; i < this.cargo.length; i++)
		{
			final DubNode_Single nextNode = new DubNode_Single(this.cargo[i]);
			current.next = nextNode;
			current = nextNode;
		}
		
		return new DubList(head, current, this.cargo.length);
	}

}
