package Collections.Lists.Generic.Directed;

import java.util.Collection;

import Collections.Lists.CharList.CharList;
import Collections.Lists.CharList.CharListable;
import CustomExceptions.UnhandledEnumException;

public class SingleLinkedList<U> implements CharListable, Iterable<U>
{
	private Node<U> head;
	private Node<U> tail;
	private int size;

	public SingleLinkedList()
	{
		this(null, 0);
	}

	private SingleLinkedList(Node<U> inHeadAndTail, int inSize)
	{
		this.head = inHeadAndTail;
		this.tail = inHeadAndTail;
		this.size = inSize;
	}

	private SingleLinkedList(Node<U> inHead, Node<U> inTail, int inSize)
	{
		this.head = inHead;
		this.tail = inTail;
		this.size = inSize;
	}

	public final boolean isEmpty()
	{
		return this.size == 0;
	}

	public final boolean isNotEmpty()
	{
		return this.size != 0;
	}

	public final int size()
	{
		return this.size;
	}

	public final U getFirst()
	{
		switch (this.size)
		{
			case 0:
			{
				throw new NullPointerException("List is empty!");
			}
			default:
			{
				return this.head.firstCargo();
			}
		}
	}

	public final U getLast()
	{
		switch (this.size)
		{
			case 0:
			{
				throw new NullPointerException("List is empty!");
			}
			default:
			{
				return this.tail.lastCargo();
			}
		}
	}

	public final U pop()
	{
		return this.pop_internal().getCargo();
	}

	private final SingleNode<U> pop_internal()
	{
		final SingleNode<U> result;

		switch (this.size)
		{
			case 0:
			{
				throw new NullPointerException("Nothing left to pop!");
			}
			case 1:
			{
				result = ((SingleNode<U>) this.head);
				this.head = null;
				this.tail = null;
				this.size = 0;
				break;
			}
			default:
			{
				if (this.head instanceof SingleNode)
				{
					result = ((SingleNode<U>) this.head);
					this.head = this.head.getNext();
				}
				else
				{
					final MultiNode<U> cast = (MultiNode<U>) this.head;
					result = new SingleNode<U>(cast.firstCargo());
					final SingleNode<U> newHead = cast.wrapElementOne();
					cast.separateAllButFirst(newHead).setNext(cast);
					this.head = newHead;
				}

				this.size--;
				break;
			}
		}

		return result;
	}

	public static enum ListCopyModes
	{
		/**
		 * copies the list exactly, node by node
		 */
		AsIs,

		/**
		 * copies the list into an array, then wraps it
		 */
		Compress,

		/**
		 * copies every element of the list into a separate node
		 */
		Separate;
	}

	public final SingleLinkedList<U> copySelf()
	{
		switch (this.size)
		{
			case 0:
			{
				return new SingleLinkedList<U>();
			}
			case 1:
			{
				return new SingleLinkedList<U>(new SingleNode<U>(((SingleNode<U>) this.head).getCargo()), 1);
			}
			default:
			{
				return this.copySelf_noSizeCheck(ListCopyModes.AsIs);
			}
		}
	}

	public final SingleLinkedList<U> copySelf(ListCopyModes mode)
	{
		switch (this.size)
		{
			case 0:
			{
				return new SingleLinkedList<U>();
			}
			case 1:
			{
				return new SingleLinkedList<U>(new SingleNode<U>(((SingleNode<U>) this.head).getCargo()), 1);
			}
			default:
			{
				return this.copySelf_noSizeCheck(mode);
			}
		}
	}

	private final SingleLinkedList<U> copySelf_noSizeCheck(ListCopyModes mode)
	{
		final SingleLinkedList<U> result;

		switch (mode)
		{
			case AsIs:
			{
				final Node<U> newHead = this.head.copySelf();

				if (this.head == this.tail)
				{
					result = new SingleLinkedList<U>(newHead, this.size);
				}
				else
				{
					result = new SingleLinkedList<U>(newHead, newHead.cascadeCopy(this.head), this.size);
				}
				break;
			}
			case Compress:
			{
				result = new SingleLinkedList<U>(new MultiNode<U>(this.toArray()), this.size);
				break;
			}
			case Separate:
			{
				result = new SingleLinkedList<U>();
				final ListItsy<U> itsy = this.iterator();

				while (itsy.hasNext())
				{
					result.add(itsy.next());
				}

				break;
			}
			default:
			{
				throw new UnhandledEnumException(mode);
			}
		}

		return result;
	}

	public final void add(U in)
	{
		this.add_internal(new SingleNode<U>(in), 1);
		//return true;
	}

	public final void add(U[] in)
	{
		this.add_internal(new MultiNode<U>(in), 1);
	}

	public final void add(SingleLinkedList<U> in, boolean canConsume)
	{
		if (in.size != 0)
		{
			if (in.head == in.tail)
			{
				if (canConsume)
				{
					this.add_internal(in.head, in.size);
				}
				else
				{
					this.add_internal(in.head.copySelf(), in.size);
				}
			}
			else
			{
				if (canConsume)
				{
					this.add_internal(in.head, in.tail, in.size);
				}
				else
				{
					final Node<U> newHead = in.head.copySelf();
					this.add_internal(newHead, newHead.cascadeCopy(in.head), in.size);
				}
			}
		}
	}

	public final void add_poppedElementFrom(SingleLinkedList<U> source)
	{
		this.add_internal(source.pop_internal(), 1);
	}

	private final void add_internal(Node<U> in, int nodeSize)
	{
		switch (this.size)
		{
			case 0:
			{
				this.head = in;
				this.tail = in;
				this.size = nodeSize;
				break;
			}
			default:
			{
				this.tail.setNext(in);
				this.tail = in;
				this.size += nodeSize;
				break;
			}
		}
	}

	private final void add_internal(Node<U> in, Node<U> inEnd, int nodesSize)
	{
		switch (this.size)
		{
			case 0:
			{
				this.head = in;
				this.tail = inEnd;
				this.size = nodesSize;
				break;
			}
			default:
			{
				this.tail.setNext(in);
				this.tail = inEnd;
				this.size += nodesSize;
				break;
			}
		}
	}

	public final void push(U in)
	{
		this.push_internal(new SingleNode<U>(in), 1);
	}

	public final void push(U[] in)
	{
		this.push_internal(new MultiNode<U>(in), 1);
	}

	public final void push(SingleLinkedList<U> in, boolean canConsume)
	{
		if (in.size != 0)
		{
			if (in.head == in.tail)
			{
				if (canConsume)
				{
					this.push_internal(in.head, in.size);
				}
				else
				{
					this.push_internal(in.head.copySelf(), in.size);
				}
			}
			else
			{
				if (canConsume)
				{
					this.push_internal(in.head, in.tail, in.size);
				}
				else
				{
					final Node<U> newHead = in.head.copySelf();
					this.push_internal(newHead, newHead.cascadeCopy(in.head), in.size);
				}
			}
		}
	}

	public final void push_poppedElementFrom(SingleLinkedList<U> source)
	{
		this.push_internal(source.pop_internal(), 1);
	}

	private final void push_internal(Node<U> in, int nodeSize)
	{
		switch (this.size)
		{
			case 0:
			{
				this.head = in;
				this.tail = in;
				this.size = nodeSize;
				break;
			}
			default:
			{
				in.setNext(this.head);
				this.head = in;
				this.size += nodeSize;
				break;
			}
		}
	}

	private final void push_internal(Node<U> in, Node<U> inEnd, int nodesSize)
	{
		switch (this.size)
		{
			case 0:
			{
				this.head = in;
				this.tail = inEnd;
				this.size = nodesSize;
				break;
			}
			default:
			{
				inEnd.setNext(this.head);
				this.head = in;
				this.size += nodesSize;
				break;
			}
		}
	}

	public final U[] toArray()
	{
		@SuppressWarnings("unchecked")
		final U[] rawResult = (U[]) new Object[this.size];
		final ListItsy<U> itsy = this.iterator();

		for (int i = 0; i < this.size; i++)
		{
			rawResult[i] = itsy.next();
		}

		return rawResult;
	}

	@SuppressWarnings("unchecked")
	public final <T> T[] toArray(T[] in)
	{
		if (in.length != this.size)
		{
			in = (T[]) new Object[this.size];
		}
		
		if(this.size == 0)
		{
			return in;
		}

		final ListItsy<U> itsy = this.iterator();

		for (int i = 0; i < this.size; i++)
		{
			in[i] = (T) itsy.next();
		}

		return in;
	}

	@Override
	public final CharList toCharList()
	{
		return this.toCharList(0);
	}

	@Override
	public final CharList toCharList(int inTabOffset)
	{
		final CharList result = new CharList();

		if (this.size > 0)
		{
			final ListItsy<U> itsy = this.iterator();
			result.add('\t', inTabOffset);
			result.add(itsy.next().toString());

			while (itsy.hasNext())
			{
				result.addNewIndentedLine(inTabOffset);
				result.add(itsy.next().toString());
			}
		}

		return result;
	}

	@Override
	public final ListItsy<U> iterator()
	{
		return new ListItsy<U>(this.head, this.size);
	}

	public final void addAll(Collection<? extends U> inArg0)
	{
		for (U curr : inArg0)
		{
			this.add(curr);
		}
		//return true;
	}

	public final void clear()
	{
		this.head = null;
		this.tail = null;
		this.size = 0;
	}

	public final boolean contains(Object inArg0)
	{
		if (inArg0 != null)
		{
			for (U curr : this)
			{
				if (inArg0.equals(curr))
				{
					return true;
				}
			}
		}

		return false;
	}

	public final boolean containsAll(Collection<?> inArg0)
	{
		/*
		 * terrible implementation...
		 */
		for (U curr : this)
		{
			if (!inArg0.contains(curr))
			{
				return false;
			}
		}

		return true;
	}

	public final U get(int inArg0)
	{
		int i = 0;

		for (U curr : this)
		{
			if (i != inArg0)
			{
				i++;
			}
			else
			{
				return curr;
			}
		}

		return null;
	}

	public final int indexOf(Object inArg0)
	{
		int i = 0;

		for (U curr : this)
		{
			if (curr != inArg0)
			{
				i++;
			}
			else
			{
				return i;
			}
		}

		return -1;
	}

	public final int lastIndexOf(Object inArg0)
	{
		int result = -1;
		int i = 0;
		
		for (U curr : this)
		{
			if (curr == inArg0)
			{
				result = i;
			}
			
			i++;
		}
		
		return result;
	}
}
