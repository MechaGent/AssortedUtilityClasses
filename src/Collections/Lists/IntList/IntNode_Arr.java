package Collections.Lists.IntList;

class IntNode_Arr extends IntNode
{
	private final int[] cargo;
	
	public IntNode_Arr(int[] inCargo)
	{
		super();
		this.cargo = inCargo;
	}

	@Override
	public int getCargoAt(int inIndex)
	{
		return this.cargo[inIndex];
	}

	@Override
	public int getFirstCargo()
	{
		return this.cargo[0];
	}

	@Override
	public int getLastCargo()
	{
		return this.cargo[this.cargo.length - 1];
	}

	@Override
	public int getCargoLength()
	{
		return this.cargo.length;
	}

	@Override
	public IntNode copySelf()
	{
		return new IntNode_Arr(this.cargo);
	}

	@Override
	public IntList splitSelf()
	{
		final IntNode_Single head = new IntNode_Single(this.cargo[0]);
		IntNode_Single current = head;
		
		for(int i = 1; i < this.cargo.length; i++)
		{
			final IntNode_Single nextNode = new IntNode_Single(this.cargo[i]);
			current.next = nextNode;
			current = nextNode;
		}
		
		return new IntList(head, current, this.cargo.length);
	}

}
