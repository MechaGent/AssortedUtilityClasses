package Collections.Lists.DubList;

import Collections.PrimitiveInterfaceAnalogues.Double.DoubleIterator;

public class DubItsy implements DoubleIterator
{
	private DubNode current;
	private int subTotal;
	private int subIndex;

	public DubItsy(DubList in)
	{
		this(in.getHead_internal());
	}
	
	DubItsy(DubNode inCurrent)
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
	public final double next()
	{
		return this.next_asDouble();
	}

	@Override
	public final double next_asDouble()
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
