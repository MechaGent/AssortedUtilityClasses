package Collections.Lists.DubList;

abstract class DubNode
{
	static final DubNode NullNode = new NullNodeClass();

	protected DubNode next;

	public DubNode()
	{
		this(NullNode);
	}

	public DubNode(DubNode inNext)
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

	public abstract double getCargoAt(int index);

	public abstract double getFirstCargo();

	public abstract double getLastCargo();

	public abstract int getCargoLength();

	public abstract DubNode copySelf();

	public abstract DubList splitSelf();

	private static class NullNodeClass extends DubNode
	{
		public NullNodeClass()
		{
			super();
		}

		@Override
		public final boolean isNull()
		{
			return true;
		}

		@Override
		public final boolean isNotNull()
		{
			return false;
		}

		@Override
		public final double getCargoAt(int inIndex)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public final double getFirstCargo()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public final double getLastCargo()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public final int getCargoLength()
		{
			return -1;
		}

		@Override
		public final DubNode copySelf()
		{
			return this;
		}

		@Override
		public final DubList splitSelf()
		{
			return new DubList();
		}
	}
}
