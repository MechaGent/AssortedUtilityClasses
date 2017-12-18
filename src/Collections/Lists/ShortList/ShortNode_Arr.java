package Collections.Lists.ShortList;

class ShortNode_Arr extends ShortNode
{
	private final short[] cargo;
	
	public ShortNode_Arr(short[] inCargo)
	{
		super();
		this.cargo = inCargo;
	}

	@Override
	public short getCargoAt(int inIndex)
	{
		return this.cargo[inIndex];
	}

	@Override
	public short getFirstCargo()
	{
		return this.cargo[0];
	}

	@Override
	public short getLastCargo()
	{
		return this.cargo[this.cargo.length - 1];
	}

	@Override
	public int getCargoLength()
	{
		return this.cargo.length;
	}

	@Override
	public ShortNode copySelf()
	{
		return new ShortNode_Arr(this.cargo);
	}

	@Override
	public ShortList splitSelf()
	{
		final ShortNode_Single head = new ShortNode_Single(this.cargo[0]);
		ShortNode_Single current = head;
		
		for(int i = 1; i < this.cargo.length; i++)
		{
			final ShortNode_Single nextNode = new ShortNode_Single(this.cargo[i]);
			current.next = nextNode;
			current = nextNode;
		}
		
		return new ShortList(head, current, this.cargo.length);
	}

}
