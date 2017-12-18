package Collections.Lists.Generic.Directed;

class SingleNode<U> extends Node<U>
{
	private final U cargo;

	SingleNode(U inCargo)
	{
		this(null, inCargo);
	}
	
	SingleNode(Node<U> inNext, U inCargo)
	{
		super(inNext);
		this.cargo = inCargo;
	}
	
	final U getCargo()
	{
		return this.cargo;
	}

	@Override
	final U firstCargo()
	{
		return this.cargo;
	}

	@Override
	final U lastCargo()
	{
		return this.cargo;
	}

	@Override
	final SingleNode<U> copySelf()
	{
		return new SingleNode<U>(this.cargo);
	}
}
