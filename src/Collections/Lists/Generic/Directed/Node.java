package Collections.Lists.Generic.Directed;

abstract class Node<U>
{
	private Node<U> next;

	Node()
	{
		this.next = null;
	}

	Node(Node<U> inNext)
	{
		this.next = inNext;
	}

	final boolean hasNext()
	{
		return this.next != null;
	}

	final Node<U> getNext()
	{
		return this.next;
	}

	final void setNext(Node<U> in)
	{
		this.next = in;
	}

	/**
	 * assumes this Node is to be the first in a new list of nodes, and iteratively copies {@code oldNext} and its children, returning the tail of the new list
	 * 
	 * @param old
	 * @return
	 */
	final Node<U> cascadeCopy(Node<U> old)
	{
		if (old.hasNext())
		{
			old = old.next;
			Node<U> newNext = old.copySelf();
			this.next = newNext;

			while (old.hasNext())
			{
				old = old.next;
				newNext.next = old.copySelf();
				newNext = newNext.next;
			}

			return newNext;
		}
		else
		{
			return this;
		}
	}

	abstract U firstCargo();

	abstract U lastCargo();

	abstract Node<U> copySelf();
}
