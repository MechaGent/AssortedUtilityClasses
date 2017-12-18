package Collections.Lists.Generic.Directed.Mk01;

class SingleNode<U> extends Node<U>
{
	private final U cargo;

	public SingleNode(U inCargo)
	{
		super();
		this.cargo = inCargo;
	}

	@Override
	public U getCargoAt(int inIndex)
	{
		return this.cargo;
	}

	@Override
	public U getFirstCargo()
	{
		return this.cargo;
	}

	@Override
	public U getLastCargo()
	{
		return this.cargo;
	}

	@Override
	public int getCargoLength()
	{
		return 1;
	}

	@Override
	public Node<U> copySelf()
	{
		return new SingleNode<U>(this.cargo);
	}

	@Override
	public SingleLinkedList<U> splitSelf()
	{
		final Node<U> raw = this.copySelf();
		
		return new SingleLinkedList<U>(raw, raw, 1);
	}
}
