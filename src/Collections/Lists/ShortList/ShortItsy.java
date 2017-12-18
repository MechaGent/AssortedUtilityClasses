package Collections.Lists.ShortList;

import Collections.PrimitiveInterfaceAnalogues.Short.ShortIterator;

public class ShortItsy implements ShortIterator
{
	private ShortNode current;
	private int subTotal;
	private int subIndex;

	public ShortItsy(ShortList in)
	{
		this(in.getHead_internal());
	}
	
	ShortItsy(ShortNode inCurrent)
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
	public short next()
	{
		return this.next_asShort();
	}

	@Override
	public short next_asShort()
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
