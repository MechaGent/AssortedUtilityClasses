package Collections.Lists.IntList;

import Collections.PrimitiveInterfaceAnalogues.Int.IntIterable;
import Collections.PrimitiveInterfaceAnalogues.Int.IntIterator;

public class IntList implements IntIterable
{
	static final int preferredErrorPrimitive = -69;
	private static final IntNode NullNode = IntNode.NullNode;
	
	private IntNode head;
	private IntNode tail;
	private int size;
	
	public IntList()
	{
		this(NullNode, NullNode, 0);
	}

	IntList(IntNode inHead, IntNode inTail, int inSize)
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

	public IntList copySelf()
	{
		if (this.size > 0)
		{
			final IntNode nextHead = this.head.copySelf();
			IntNode current = nextHead;
			IntNode nextOld = this.head.next;

			while (nextOld.isNotNull())
			{
				current.next = nextOld.copySelf();
				nextOld = nextOld.next;
				current = current.next;
			}

			return new IntList(nextHead, current, this.size);
		}
		else
		{
			return new IntList();
		}
	}

	public final void push(int in)
	{
		this.push_internal(new IntNode_Single(in));
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
				this.add_internal(new IntNode_Single(in[0]));
				break;
			}
			default:
			{
				this.add_internal(new IntNode_Arr(in));
				break;
			}
		}
	}

	public final void push(IntList in, boolean canConsume)
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

	private final void push_internal(IntNode in)
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
		this.add_internal(new IntNode_Single(in));
	}

	public final void add(int[] in)
	{
		if (in.length == 1)
		{
			this.add_internal(new IntNode_Single(in[0]));
		}
		else
		{
			this.add_internal(new IntNode_Arr(in));
		}
	}

	public final void add(IntList in, boolean canConsume)
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

	private final void add_internal(IntNode in)
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

	public int pop()
	{
		return this.pop_internal().getFirstCargo();
	}

	private IntNode pop_internal()
	{
		IntNode result = this.head;

		if (result.getCargoLength() > 1)
		{
			final IntList saccy = result.splitSelf();

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
	public void push_poppedElementFrom(IntList source)
	{
		this.push_internal(source.pop_internal());
	}
	
	/**
	 * pops the first element of {@code source}, and adds it into this
	 * @param source
	 */
	public void add_poppedElementFrom(IntList source)
	{
		this.add_internal(source.pop_internal());
	}

	public int getFirst()
	{
		return this.head.getFirstCargo();
	}

	public int getLast()
	{
		return this.tail.getLastCargo();
	}

	@Override
	public IntIterator getIntIterator()
	{
		return new IntItsy(this.head);
	}
	
	IntNode getHead_internal()
	{
		return this.head;
	}
}
