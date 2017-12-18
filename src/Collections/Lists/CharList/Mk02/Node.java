package Collections.Lists.CharList.Mk02;

abstract class Node
{
	protected Node next;

	Node()
	{
		this.next = null;
	}

	Node(Node inNext)
	{
		this.next = inNext;
	}

	final Node getNext()
	{
		return this.next;
	}

	final void setNext(Node in)
	{
		this.next = in;
	}

	final char[] toCharArray(int length)
	{
		final char[] result = new char[length];
		int nextOffset = this.toCharArray_internal(result, 0);

		/*
		 * unrolled loop a little to prevent initial redundancy
		 */
		if (nextOffset < result.length)
		{
			Node current = this.next;
			nextOffset = current.toCharArray_internal(result, nextOffset);

			while (nextOffset < result.length)
			{
				current = current.next;
				//System.out.println("type: " + current.getClass());
				nextOffset = current.toCharArray_internal(result, nextOffset);
			}
		}
		
		return result;
	}

	/**
	 * 
	 * @param result
	 * @param offset
	 * @return the new offset
	 */
	protected abstract int toCharArray_internal(char[] result, int offset);

	abstract char firstChar();

	abstract char lastChar();
}
