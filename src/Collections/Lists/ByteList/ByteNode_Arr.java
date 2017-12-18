package Collections.Lists.ByteList;

class ByteNode_Arr extends ByteNode
{
	private final byte[] cargo;
	
	public ByteNode_Arr(byte[] inCargo)
	{
		super();
		this.cargo = inCargo;
	}

	@Override
	public byte getCargoAt(int inIndex)
	{
		return this.cargo[inIndex];
	}

	@Override
	public byte getFirstCargo()
	{
		return this.cargo[0];
	}

	@Override
	public byte getLastCargo()
	{
		return this.cargo[this.cargo.length - 1];
	}

	@Override
	public int getCargoLength()
	{
		return this.cargo.length;
	}

	@Override
	public ByteNode copySelf()
	{
		return new ByteNode_Arr(this.cargo);
	}

	@Override
	public ByteList splitSelf()
	{
		final ByteNode_Single head = new ByteNode_Single(this.cargo[0]);
		ByteNode_Single current = head;
		
		for(int i = 1; i < this.cargo.length; i++)
		{
			final ByteNode_Single nextNode = new ByteNode_Single(this.cargo[i]);
			current.next = nextNode;
			current = nextNode;
		}
		
		return new ByteList(head, current, this.cargo.length);
	}

}
