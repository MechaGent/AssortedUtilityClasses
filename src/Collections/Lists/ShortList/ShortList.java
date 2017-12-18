package Collections.Lists.ShortList;

import Collections.PrimitiveInterfaceAnalogues.Short.ShortIterable;
import Collections.PrimitiveInterfaceAnalogues.Short.ShortIterator;

public class ShortList implements ShortIterable
{
	static final int preferredErrorPrimitive = -69;
	private static final ShortNode NullNode = ShortNode.NullNode;
	
	private ShortNode head;
	private ShortNode tail;
	private int size;
	
	public ShortList()
	{
		this(NullNode, NullNode, 0);
	}

	ShortList(ShortNode inHead, ShortNode inTail, int inSize)
	{
		this.head = inHead;
		this.tail = inTail;
		this.size = inSize;
	}
	
	public int size()
	{
		return this.size;
	}
	
	public boolean isEmpty()
	{
		return this.size == 0;
	}
	
	public boolean isNotEmpty()
	{
		return this.size != 0;
	}

	public ShortList copySelf()
	{
		if (this.size > 0)
		{
			final ShortNode nextHead = this.head.copySelf();
			ShortNode current = nextHead;
			ShortNode nextOld = this.head.next;

			while (nextOld.isNotNull())
			{
				current.next = nextOld.copySelf();
				nextOld = nextOld.next;
				current = current.next;
			}

			return new ShortList(nextHead, current, this.size);
		}
		else
		{
			return new ShortList();
		}
	}

	public final void push(short in)
	{
		this.push_internal(new ShortNode_Single(in));
	}

	public final void push(short[] in)
	{
		switch (in.length)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.add_internal(new ShortNode_Single(in[0]));
				break;
			}
			default:
			{
				this.add_internal(new ShortNode_Arr(in));
				break;
			}
		}
	}

	public final void push(ShortList in, boolean canConsume)
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

	private final void push_internal(ShortNode in)
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

	public final void add(short in)
	{
		this.add_internal(new ShortNode_Single(in));
	}

	public final void add(short[] in)
	{
		if (in.length == 1)
		{
			this.add_internal(new ShortNode_Single(in[0]));
		}
		else
		{
			this.add_internal(new ShortNode_Arr(in));
		}
	}

	public final void add(ShortList in, boolean canConsume)
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

	private final void add_internal(ShortNode in)
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

	public short pop()
	{
		return this.pop_internal().getFirstCargo();
	}

	private ShortNode pop_internal()
	{
		ShortNode result = this.head;

		if (result.getCargoLength() > 1)
		{
			final ShortList saccy = result.splitSelf();

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
	public void push_poppedElementFrom(ShortList source)
	{
		this.push_internal(source.pop_internal());
	}
	
	/**
	 * pops the first element of {@code source}, and adds it into this
	 * @param source
	 */
	public void add_poppedElementFrom(ShortList source)
	{
		this.add_internal(source.pop_internal());
	}

	public short getFirst()
	{
		return this.head.getFirstCargo();
	}

	public short getLast()
	{
		return this.tail.getLastCargo();
	}

	@Override
	public ShortIterator getShortIterator()
	{
		return new ShortItsy(this.head);
	}
	
	ShortNode getHead_internal()
	{
		return this.head;
	}
}
