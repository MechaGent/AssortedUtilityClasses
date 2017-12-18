package Collections.Lists.Generic.Directed.Mk01;

class MultiNode<U> extends Node<U>
{
	private final U[] cargo;
	
	MultiNode(U[] inCargo)
	{
		super();
		this.cargo = inCargo;
	}
	
	@Override
	public SingleLinkedList<U> splitSelf()
	{
		final SingleNode<U> head = new SingleNode<U>(this.cargo[0]);
		Node<U> current = head;
		
		for(int i = 1; i < this.cargo.length; i++)
		{
			current.next = new SingleNode<U>(this.cargo[i]);
			current = current.next;
		}
		
		return new SingleLinkedList<U>(head, current, this.cargo.length);
	}

	@Override
	public U getCargoAt(int index)
	{
		return this.cargo[index];
	}

	@Override
	public U getFirstCargo()
	{
		return this.cargo[0];
	}

	@Override
	public U getLastCargo()
	{
		return this.cargo[this.cargo.length - 1];
	}

	@Override
	public int getCargoLength()
	{
		return this.cargo.length;
	}

	@Override
	public Node<U> copySelf()
	{
		return new MultiNode<U>(this.cargo);
	}
}
