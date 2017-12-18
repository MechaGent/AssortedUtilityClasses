package Collections.Lists.IntList;

import Collections.PrimitiveInterfaceAnalogues.Int.IntIterator;

public class IntItsy implements IntIterator
{
	private IntNode current;
	private int subTotal;
	private int subIndex;

	public IntItsy(IntList in)
	{
		this(in.getHead_internal());
	}
	
	IntItsy(IntNode inCurrent)
	{
		this.current = inCurrent;
		this.subIndex = 0;
		this.subTotal = this.current.getCargoLength();
	}

	@Override
	public boolean hasNext()
	{
		return (this.subIndex < this.subTotal) || (this.current.next.isNotNull());
	}

	@Override
	public int next()
	{
		return this.next_asInt();
	}

	@Override
	public int next_asInt()
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
