package Collections.Lists.Generic.Directed.Mk01;

import java.util.Iterator;

import Collections.Lists.CharList.CharList;

public class SingleLinkedList<U> implements Iterable<U>
{
	private Node<U> head;
	private Node<U> tail;
	private int size;

	@SuppressWarnings("unchecked")
	public SingleLinkedList()
	{
		this.head = Node.NullNode;
		this.tail = Node.NullNode;
		this.size = 0;
	}

	SingleLinkedList(Node<U> inHead, Node<U> inTail, int inSize)
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

	public final SingleLinkedList<U> copySelf()
	{
		if (this.size > 0)
		{
			final Node<U> nextHead = this.head.copySelf();
			Node<U> current = nextHead;
			Node<U> nextOld = this.head.next;

			while (nextOld.isNotNull())
			{
				current.next = nextOld.copySelf();
				nextOld = nextOld.next;
				current = current.next;
			}

			return new SingleLinkedList<U>(nextHead, current, this.size);
		}
		else
		{
			return new SingleLinkedList<U>();
		}
	}

	public final void push(U in)
	{
		this.push_internal(new SingleNode<U>(in));
	}

	public final void push(U[] in)
	{
		switch (in.length)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				this.add_internal(new SingleNode<U>(in[0]));
				break;
			}
			default:
			{
				this.add_internal(new MultiNode<U>(in));
				break;
			}
		}
	}

	public final void push(SingleLinkedList<U> in, boolean canConsume)
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

	private final void push_internal(Node<U> in)
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

	public final void add(U in)
	{
		this.add_internal(new SingleNode<U>(in));
	}

	public final void add(U[] in)
	{
		if (in.length == 1)
		{
			this.add_internal(new SingleNode<U>(in[0]));
		}
		else
		{
			this.add_internal(new MultiNode<U>(in));
		}
	}

	public final void add(SingleLinkedList<U> in, boolean canConsume)
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

	private final void add_internal(Node<U> in)
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

	public final U pop()
	{
		return this.pop_internal().getFirstCargo();
	}

	@SuppressWarnings("unchecked")
	private final Node<U> pop_internal()
	{
		final Node<U> result;
		
		switch(this.size)
		{
			case 0:
			{
				throw new IndexOutOfBoundsException();
			}
			case 1:
			{
				result = this.head;
				this.head = Node.NullNode;
				this.tail = Node.NullNode;
				this.size = 0;
				break;
			}
			case 2:
			{
				if(this.head != this.tail)
				{
					//two nodes
					result = this.head;
					this.head = this.tail;
				}
				else
				{
					//one node
					result = new SingleNode<U>(this.head.getFirstCargo());
					this.tail = new SingleNode<U>(this.head.getLastCargo());
					this.head = this.tail;
				}
				
				this.size = 1;
				
				break;
			}
			default:
			{
				if(this.head != this.tail)
				{
					//multiple nodes
					
					if(this.head.getCargoLength() > 1)
					{
						final SingleLinkedList<U> saccy = this.head.splitSelf();
						result = saccy.head;
						saccy.tail.next = this.head.next;
						this.head = saccy.head.next;
					}
					else
					{
						result = this.head;
						this.head = this.head.next;
					}
					
					this.size--;
				}
				else
				{
					//single node
					
					final SingleLinkedList<U> saccy = this.head.splitSelf();
					result = saccy.head;
					saccy.tail.next = this.head.next;
					this.head = saccy.head.next;
					
					this.size--;
				}
				
				break;
			}
		}

		return result;
	}

	/**
	 * pops the first element of {@code source}, and pushes it into this
	 * 
	 * @param source
	 */
	public final void push_poppedElementFrom(SingleLinkedList<U> source)
	{
		this.push_internal(source.pop_internal());
	}

	/**
	 * pops the first element of {@code source}, and adds it into this
	 * 
	 * @param source
	 */
	public final void add_poppedElementFrom(SingleLinkedList<U> source)
	{
		this.add_internal(source.pop_internal());
	}

	public final U getFirst()
	{
		return this.head.getFirstCargo();
	}

	public final U getLast()
	{
		return this.tail.getLastCargo();
	}

	@Override
	public final Iterator<U> iterator()
	{
		return new ListItsy<U>(this.head);
	}

	public final CharList toDiagCharList(int offset)
	{
		final CharList result = new CharList();

		if (this.isNotEmpty())
		{
			Node<U> current = this.head;
			
			while(current != null)
			{
				result.addNewLine();
				result.add(current.toString());
				current = current.next;
			}
		}

		return result;
	}

	private static class ListItsy<U> implements Iterator<U>
	{
		private Node<U> current;
		private int subTotal;
		private int subIndex;

		public ListItsy(Node<U> inCurrent)
		{
			this.current = inCurrent;
			this.subIndex = 0;
			this.subTotal = this.current.getCargoLength();
		}

		@Override
		public final boolean hasNext()
		{
			return (this.subIndex < this.subTotal) || (this.current.next.isNotNull());
		}

		@Override
		public final U next()
		{
			if (this.subIndex >= this.subTotal)
			{
				this.current = this.current.next;
				this.subIndex = 0;
				this.subTotal = this.current.getCargoLength();
			}

			return this.current.getCargoAt(this.subIndex++);
		}
	}
}
