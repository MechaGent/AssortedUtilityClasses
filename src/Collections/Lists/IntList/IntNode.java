package Collections.Lists.IntList;

abstract class IntNode
{
	static final IntNode NullNode = new NullNodeClass();

	protected IntNode next;

	public IntNode()
	{
		this(NullNode);
	}

	public IntNode(IntNode inNext)
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

	public abstract int getCargoAt(int index);

	public abstract int getFirstCargo();

	public abstract int getLastCargo();

	public abstract int getCargoLength();

	public abstract IntNode copySelf();

	public abstract IntList splitSelf();

	private static class NullNodeClass extends IntNode
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
		public int getCargoAt(int inIndex)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public int getFirstCargo()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public int getLastCargo()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public int getCargoLength()
		{
			return -1;
		}

		@Override
		public IntNode copySelf()
		{
			return this;
		}

		@Override
		public IntList splitSelf()
		{
			return new IntList();
		}
	}
}
