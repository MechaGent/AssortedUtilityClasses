package Collections.Lists.Generic.Directed;

class MultiNode<U> extends Node<U>
{
	private final U[] cargo;
	
	MultiNode(U[] inCargo)
	{
		this(null, inCargo);
	}
	
	MultiNode(Node<U> inNext, U[] inCargo)
	{
		super(inNext);
		this.cargo = inCargo;
	}
	
	final int length()
	{
		return this.cargo.length;
	}

	@Override
	final U firstCargo()
	{
		return this.cargo[0];
	}

	@Override
	final U lastCargo()
	{
		return this.cargo[this.cargo.length - 1];
	}
	
	final U cargoAt(int index)
	{
		return this.cargo[index];
	}

	@Override
	final MultiNode<U> copySelf()
	{
		return new MultiNode<U>(this.cargo);
	}
	
	/**
	 * used by {@link SingleLinkedList#pop()} method
	 * @return a {@link SingleNode} wrapped around the element at index 1
	 */
	final SingleNode<U> wrapElementOne()
	{
		return new SingleNode<U>(this.cargo[1]);
	}
	
	/**
	 * used by {@link SingleLinkedList#pop()} method
	 * @param wrappedOne should be a {@link SingleNode} wrapped around the element at index 1
	 * @return
	 */
	final SingleNode<U> separateAllButFirst(SingleNode<U> wrappedOne)
	{
		if(this.cargo.length > 2)
		{
			SingleNode<U> tail = new SingleNode<U>(this.cargo[2]);
			wrappedOne.setNext(tail);
			
			for(int i = 3; i < this.cargo.length; i++)
			{
				final SingleNode<U> next = new SingleNode<U>(this.cargo[i]);
				tail.setNext(next);
				tail = next;
			}

			return tail;
		}
		else
		{
			return wrappedOne;
		}
	}
}
