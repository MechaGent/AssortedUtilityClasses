package Collections.Lists.ByteList;

class ByteNode_Single extends ByteNode
{
	private final byte cargo;

	public ByteNode_Single(byte inCargo)
	{
		super();
		this.cargo = inCargo;
	}

	@Override
	public byte getCargoAt(int inIndex)
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
	public byte getFirstCargo()
	{
		return this.cargo;
	}

	@Override
	public byte getLastCargo()
	{
		return this.cargo;
	}

	@Override
	public int getCargoLength()
	{
		return 1;
	}

	@Override
	public ByteNode copySelf()
	{
		return new ByteNode_Single(this.cargo);
	}

	@Override
	public ByteList splitSelf()
	{
		final ByteNode copy = this.copySelf();
		
		return new ByteList(copy, copy, 1);
	}
}
