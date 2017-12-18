package Collections.Lists.IntList;

public class OneLinkIntList
{
	private static final Node NullNode = new SingleNode(Integer.MAX_VALUE);

	private Node head;
	private Node tail;
	private int size;

	public OneLinkIntList()
	{
		this.head = NullNode;
		this.tail = NullNode;
		this.size = 0;
	}

	public OneLinkIntList(int in)
	{
		this(new SingleNode(in));
	}

	public OneLinkIntList(int[] in)
	{
		this((in.length != 0) ? new ArrNode(in) : NullNode);
	}

	public OneLinkIntList(OneLinkIntList old, boolean canConsume)
	{
		if (canConsume)
		{
			this.head = old.head;
			this.tail = old.tail;
		}
		else
		{
			this.head = old.head.cloneSelf();
			Node curNew = this.head;
			Node curOld = old.head.next;

			while (!curOld.isNull())
			{
				final Node next = curOld.cloneSelf();
				curNew.next = next;
				curNew = next;
				curOld = curOld.next;
			}

			this.tail = curNew;
		}

		this.size = old.size;
	}

	private OneLinkIntList(Node first)
	{
		if (!first.isNull())
		{
			this.head = first;
			this.tail = first;
			this.size = first.getNodeLength();
		}
		else
		{
			this.head = NullNode;
			this.tail = NullNode;
			this.size = 0;
		}
	}

	private OneLinkIntList(Node inHead, Node inTail, int inSize)
	{
		this.head = inHead;
		this.tail = inTail;
		this.size = inSize;
	}

	/*
	 * Push-Pop Ops
	 */

	public final void push(int in)
	{
		this.push_internal(new SingleNode(in));
	}

	public final void push(int[] in)
	{
		switch(in.length)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.push_internal(new SingleNode(in[0]));
				break;
			}
			default:
			{
				this.push_internal(new ArrNode(in));
				break;
			}
		}
	}

	public final void push(OneLinkIntList in, boolean canConsume)
	{
		if (in.size != 0)
		{
			if (!canConsume)
			{
				in = new OneLinkIntList(in, false);
			}

			if (this.size != 0)
			{
				in.tail.next = this.head;
				this.head = in.head;
				this.size += in.size;
			}
			else
			{
				this.head = in.head;
				this.tail = in.tail;
				this.size = in.size;
			}
		}
	}

	private final void push_internal(Node in)
	{
		if (this.size != 0)
		{
			in.next = this.head;
			this.head = in;
			this.size += in.getNodeLength();
		}
		else
		{
			this.add_internal_first(in);
		}
	}

	public int pop()
	{
		if (this.size != 0)
		{
			final int result = this.head.getFirstCargo();

			final Node first = this.head;
			this.head = this.head.next;
			this.size--;

			if (this.head.getNodeLength() != 1)
			{
				final OneLinkIntList saccy = ((ArrNode) first).splitNode();
				this.size += saccy.size;
				saccy.tail = this.head;
				this.head = saccy.head.next;
			}

			return result;
		}
		else
		{
			throw new IndexOutOfBoundsException("tried to access index 0 of arrLength 0");
		}
	}

	/*
	 * Add Ops
	 */

	public final void add(int in)
	{
		this.add_internal(new SingleNode(in));
	}

	public final void add(int[] in)
	{
		switch(in.length)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.add_internal(new SingleNode(in[0]));
				break;
			}
			default:
			{
				this.add_internal(new ArrNode(in));
				break;
			}
		}
	}

	public final void add(OneLinkIntList in, boolean canConsume)
	{
		if (in.size != 0)
		{
			if (!canConsume)
			{
				in = new OneLinkIntList(in, false);
			}

			if (this.size != 0)
			{
				this.tail.next = in.head;
				this.tail = in.tail;
				this.size += in.size;
			}
			else
			{
				this.head = in.head;
				this.tail = in.tail;
				this.size = in.size;
			}
		}
	}

	private final void add_internal(Node in)
	{
		if (this.size != 0)
		{
			this.tail.next = in;
			this.tail = in;
			this.size += in.getNodeLength();
		}
		else
		{
			this.add_internal_first(in);
		}
	}

	private final void add_internal_first(Node in)
	{
		this.head = in;
		this.tail = in;
		this.size = in.getNodeLength();
	}

	/*
	 * Get methods
	 */

	/**
	 * ONLY use this if you really need to do so, it's inefficent as balls, except for First and Last cases
	 * 
	 * @param index
	 * @return
	 */
	public int getAt(int index)
	{
		final int result;

		if (index == 0)
		{
			result = this.head.getFirstCargo();
		}
		else if (index == this.size - 1)
		{
			result = this.tail.getLastCargo();
		}
		else if (index < this.size)
		{
			final Interator itsy = new Interator(this);
			int current = itsy.next();

			for (int i = 1; i < index; i++)
			{
				current = itsy.next();
			}

			result = current;
		}
		else
		{
			throw new IndexOutOfBoundsException("tried to access index " + index + " when arrLength is " + this.size);
		}

		return result;
	}

	public int getFirst()
	{
		if (this.size != 0)
		{
			return this.head.getFirstCargo();
		}
		else
		{
			throw new IndexOutOfBoundsException("tried to access index 0 of arrLength 0");
		}
	}

	public int getLast()
	{
		if (this.size != 0)
		{
			return this.tail.getLastCargo();
		}
		else
		{
			throw new IndexOutOfBoundsException("tried to access index 0 of arrLength 0");
		}
	}

	/*
	 * Misc
	 */

	public int size()
	{
		return this.size;
	}

	public boolean isEmpty()
	{
		return this.size == 0;
	}

	public int[] toArray()
	{
		final int[] result = new int[this.size];

		final Interator itsy = new Interator(this);

		for (int i = 0; i < result.length; i++)
		{
			result[i] = itsy.next();
		}

		return result;
	}
	
	public boolean contains(int search)
	{
		final Interator itsy = new Interator(this);
		boolean isFound = false;
		
		while(!isFound && itsy.hasNext())
		{
			isFound = search == itsy.next();
		}
		
		return isFound;
	}

	/*
	 * Nodes
	 */

	private static abstract class Node
	{
		private Node next;

		protected Node()
		{
			this.next = NullNode;
		}

		public boolean isNull()
		{
			return this == NullNode;
		}

		public abstract int getNodeLength();

		public abstract int getCargoAt(int i);

		public abstract int getFirstCargo();

		public abstract int getLastCargo();

		public abstract Node cloneSelf();
	}

	private static class SingleNode extends Node
	{
		private final int cargo;

		public SingleNode(int inCargo)
		{
			super();
			this.cargo = inCargo;
		}

		@Override
		public int getNodeLength()
		{
			return 1;
		}

		@Override
		public int getCargoAt(int i)
		{
			if (i == 0)
			{
				return this.cargo;
			}
			else
			{
				throw new IndexOutOfBoundsException();
			}
		}

		@Override
		public int getFirstCargo()
		{
			return this.cargo;
		}

		@Override
		public int getLastCargo()
		{
			return this.cargo;
		}

		@Override
		public Node cloneSelf()
		{
			return new SingleNode(this.cargo);
		}
	}

	private static class ArrNode extends Node
	{
		private final int[] cargo;

		public ArrNode(int[] inCargo)
		{
			super();
			this.cargo = inCargo;
		}

		@Override
		public int getNodeLength()
		{
			return this.cargo.length;
		}

		@Override
		public int getCargoAt(int i)
		{
			return this.cargo[i];
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
		public Node cloneSelf()
		{
			return new ArrNode(this.cargo);
		}

		public OneLinkIntList splitNode()
		{
			final Node first = new SingleNode(this.cargo[0]);
			Node current = first;

			for (int i = 1; i < this.cargo.length; i++)
			{
				final Node next = new SingleNode(this.cargo[i]);
				current.next = next;
				current = next;
			}

			return new OneLinkIntList(first, current, this.cargo.length);
		}
	}

	/*
	 * Iterators
	 */

	public static class Interator
	{
		private Node current;
		private int subPlace;
		private int currentNodeLength;

		private Interator(OneLinkIntList in)
		{
			this(in.head);
		}

		private Interator(Node in)
		{
			this.current = in;
			this.subPlace = 0;
			this.currentNodeLength = in.getNodeLength();
		}

		public boolean hasNext()
		{
			return (this.subPlace == 0) ? !this.current.isNull() : ((this.currentNodeLength == 1) ? this.subPlace == 0 : this.subPlace < this.current.getNodeLength());
		}

		public int next()
		{
			final int result;

			if (this.currentNodeLength == 1)
			{
				result = this.current.getFirstCargo();
				this.current = this.current.next;
				this.currentNodeLength = this.current.getNodeLength();
			}
			else
			{
				result = this.current.getCargoAt(this.subPlace++);

				if (this.subPlace == this.currentNodeLength)
				{
					this.current = this.current.next;
					this.currentNodeLength = this.current.getNodeLength();
					this.subPlace = 0;
				}
			}

			return result;
		}
	}
}
