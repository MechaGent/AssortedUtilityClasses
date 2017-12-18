package Collections.Lists.ByteList;

import Collections.PrimitiveInterfaceAnalogues.Byte.ByteIterable;
import Collections.PrimitiveInterfaceAnalogues.Byte.ByteIterator;

public class ByteList implements ByteIterable
{
	static final int preferredErrorPrimitive = -69;
	private static final ByteNode NullNode = ByteNode.NullNode;
	
	private ByteNode head;
	private ByteNode tail;
	private int size;
	
	public ByteList()
	{
		this(NullNode, NullNode, 0);
	}

	ByteList(ByteNode inHead, ByteNode inTail, int inSize)
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

	public ByteList copySelf()
	{
		if (this.size > 0)
		{
			final ByteNode nextHead = this.head.copySelf();
			ByteNode current = nextHead;
			ByteNode nextOld = this.head.next;

			while (nextOld.isNotNull())
			{
				current.next = nextOld.copySelf();
				nextOld = nextOld.next;
				current = current.next;
			}

			return new ByteList(nextHead, current, this.size);
		}
		else
		{
			return new ByteList();
		}
	}

	public final void push(byte in)
	{
		this.push_internal(new ByteNode_Single(in));
	}

	public final void push(byte[] in)
	{
		switch (in.length)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.add_internal(new ByteNode_Single(in[0]));
				break;
			}
			default:
			{
				this.add_internal(new ByteNode_Arr(in));
				break;
			}
		}
	}

	public final void push(ByteList in, boolean canConsume)
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

	private final void push_internal(ByteNode in)
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

	public final void add(byte in)
	{
		this.add_internal(new ByteNode_Single(in));
	}

	public final void add(byte[] in)
	{
		if (in.length == 1)
		{
			this.add_internal(new ByteNode_Single(in[0]));
		}
		else
		{
			this.add_internal(new ByteNode_Arr(in));
		}
	}

	public final void add(ByteList in, boolean canConsume)
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

	private final void add_internal(ByteNode in)
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

	public byte pop()
	{
		return this.pop_internal().getFirstCargo();
	}

	private ByteNode pop_internal()
	{
		ByteNode result = this.head;

		if (result.getCargoLength() > 1)
		{
			final ByteList saccy = result.splitSelf();

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
	public void push_poppedElementFrom(ByteList source)
	{
		this.push_internal(source.pop_internal());
	}
	
	/**
	 * pops the first element of {@code source}, and adds it into this
	 * @param source
	 */
	public void add_poppedElementFrom(ByteList source)
	{
		this.add_internal(source.pop_internal());
	}

	public byte getFirst()
	{
		return this.head.getFirstCargo();
	}

	public byte getLast()
	{
		return this.tail.getLastCargo();
	}

	@Override
	public ByteIterator getByteIterator()
	{
		return new linearByteIterator(this.head);
	}
	
	ByteNode getHead_internal()
	{
		return this.head;
	}
}
