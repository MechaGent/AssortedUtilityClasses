package Collections.Lists.ShortList;

abstract class ShortNode
{
	static final ShortNode NullNode = new NullNodeClass();

	protected ShortNode next;

	public ShortNode()
	{
		this(NullNode);
	}

	public ShortNode(ShortNode inNext)
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

	public abstract short getCargoAt(int index);

	public abstract short getFirstCargo();

	public abstract short getLastCargo();

	public abstract int getCargoLength();

	public abstract ShortNode copySelf();

	public abstract ShortList splitSelf();

	private static class NullNodeClass extends ShortNode
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
		public short getCargoAt(int inIndex)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public short getFirstCargo()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public short getLastCargo()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public int getCargoLength()
		{
			return -1;
		}

		@Override
		public ShortNode copySelf()
		{
			return this;
		}

		@Override
		public ShortList splitSelf()
		{
			return new ShortList();
		}
	}
}
