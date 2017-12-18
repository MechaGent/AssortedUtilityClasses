package Collections.Lists.ByteList;

abstract class ByteNode
{
	static final ByteNode NullNode = new NullNodeClass();

	protected ByteNode next;

	public ByteNode()
	{
		this(NullNode);
	}

	public ByteNode(ByteNode inNext)
	{
		this.next = inNext;
	}

	public boolean isNull()
	{
		return false;
	}

	public boolean isNotNull()
	{
		return true;
	}

	public abstract byte getCargoAt(int index);

	public abstract byte getFirstCargo();

	public abstract byte getLastCargo();

	public abstract int getCargoLength();

	public abstract ByteNode copySelf();

	public abstract ByteList splitSelf();

	private static class NullNodeClass extends ByteNode
	{
		public NullNodeClass()
		{
			super();
		}

		@Override
		public boolean isNull()
		{
			return true;
		}

		@Override
		public boolean isNotNull()
		{
			return false;
		}

		@Override
		public byte getCargoAt(int inIndex)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public byte getFirstCargo()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public byte getLastCargo()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public int getCargoLength()
		{
			return -1;
		}

		@Override
		public ByteNode copySelf()
		{
			return this;
		}

		@Override
		public ByteList splitSelf()
		{
			return new ByteList();
		}
	}
}
