package Collections.Lists.Generic.Directed.Mk01;

abstract class Node<U>
{
	@SuppressWarnings("rawtypes")
	static final Node NullNode = new NullNodeClass();
	
	protected Node<U> next;
	
	@SuppressWarnings("unchecked")
	protected Node()
	{
		this.next = NullNode;
	}
	
	public boolean isNull()
	{
		return false;
	}
	
	public boolean isNotNull()
	{
		return true;
	}
	
	public abstract U getCargoAt(int index);
	
	public abstract U getFirstCargo();
	
	public abstract U getLastCargo();
	
	public abstract int getCargoLength();
	
	public abstract Node<U> copySelf();
	
	public abstract SingleLinkedList<U> splitSelf();
	
	@SuppressWarnings("rawtypes")
	private static class NullNodeClass extends Node
	{
		private NullNodeClass()
		{
			super();
		}
		
		public boolean isNull()
		{
			return true;
		}
		
		public boolean isNotNull()
		{
			return false;
		}

		@Override
		public Object getCargoAt(int inIndex)
		{
			return null;
		}

		@Override
		public Object getFirstCargo()
		{
			return null;
		}

		@Override
		public Object getLastCargo()
		{
			return null;
		}

		@Override
		public int getCargoLength()
		{
			return -69;
		}

		@Override
		public Node copySelf()
		{
			return NullNode;
		}

		@Override
		public SingleLinkedList splitSelf()
		{
			return new SingleLinkedList();
		}
	}
}
