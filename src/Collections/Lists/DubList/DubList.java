package Collections.Lists.DubList;

import Collections.PrimitiveInterfaceAnalogues.Double.DoubleIterable;
import Collections.PrimitiveInterfaceAnalogues.Double.DoubleIterator;

public class DubList implements DoubleIterable
{
	static final int preferredErrorPrimitive = -69;
	private static final DubNode NullNode = DubNode.NullNode;
	
	private DubNode head;
	private DubNode tail;
	private int size;
	
	public DubList()
	{
		this(NullNode, NullNode, 0);
	}

	DubList(DubNode inHead, DubNode inTail, int inSize)
	{
		this.head = inHead;
		this.tail = inTail;
		this.size = inSize;
	}
	
	public final int size()
	{
		return this.size;
	}
	
	public final boolean isEmpty()
	{
		return this.size == 0;
	}
	
	public final boolean isNotEmpty()
	{
		return this.size != 0;
	}

	public final DubList copySelf()
	{
		if (this.size > 0)
		{
			final DubNode nextHead = this.head.copySelf();
			DubNode current = nextHead;
			DubNode nextOld = this.head.next;

			while (nextOld.isNotNull())
			{
				current.next = nextOld.copySelf();
				nextOld = nextOld.next;
				current = current.next;
			}

			return new DubList(nextHead, current, this.size);
		}
		else
		{
			return new DubList();
		}
	}

	public final void push(int in)
	{
		this.push_internal(new DubNode_Single(in));
	}

	public final void push(int[] in)
	{
		switch (in.length)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.add_internal(new DubNode_Single(in[0]));
				break;
			}
			default:
			{
				this.add_internal(new DubNode_Arr(in));
				break;
			}
		}
	}

	public final void push(DubList in, boolean canConsume)
	{
		if (!canConsume)
		{
			in = in.copySelf();
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

	private final void push_internal(DubNode in)
	{
		if (this.size == 0)
		{
			this.head = in;
			this.tail = in;
			this.size = in.getCargoLength();
		}
		else
		{
			in.next = this.head;
			this.head = in;
			this.size += in.getCargoLength();
		}
	}

	public final void add(int in)
	{
		this.add_internal(new DubNode_Single(in));
	}

	public final void add(int[] in)
	{
		if (in.length == 1)
		{
			this.add_internal(new DubNode_Single(in[0]));
		}
		else
		{
			this.add_internal(new DubNode_Arr(in));
		}
	}

	public final void add(DubList in, boolean canConsume)
	{
		if (!canConsume)
		{
			in = in.copySelf();
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

	private final void add_internal(DubNode in)
	{
		if (this.size == 0)
		{
			this.head = in;
			this.tail = in;
			this.size = in.getCargoLength();
		}
		else
		{
			this.tail.next = in;
			this.tail = in;
			this.size += in.getCargoLength();
		}
	}

	public final double pop()
	{
		return this.pop_internal().getFirstCargo();
	}

	private final DubNode pop_internal()
	{
		DubNode result = this.head;

		if (result.getCargoLength() > 1)
		{
			final DubList saccy = result.splitSelf();

			saccy.tail.next = this.head.next;
			result = saccy.head;
			this.head = result.next;
		}

		this.size--;
		return result;
	}
	
	/**
	 * pops the first element of {@code source}, and pushes it into this
	 * @param source
	 */
	public final void push_poppedElementFrom(DubList source)
	{
		this.push_internal(source.pop_internal());
	}
	
	/**
	 * pops the first element of {@code source}, and adds it into this
	 * @param source
	 */
	public final void add_poppedElementFrom(DubList source)
	{
		this.add_internal(source.pop_internal());
	}

	public final double getFirst()
	{
		return this.head.getFirstCargo();
	}

	public final double getLast()
	{
		return this.tail.getLastCargo();
	}

	@Override
	public final DoubleIterator getDoubleIterator()
	{
		return new DubItsy(this.head);
	}
	
	final DubNode getHead_internal()
	{
		return this.head;
	}
}
