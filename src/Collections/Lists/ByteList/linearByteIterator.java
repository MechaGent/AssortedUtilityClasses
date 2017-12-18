package Collections.Lists.ByteList;

import Collections.PrimitiveInterfaceAnalogues.Byte.ByteIterator;

public class linearByteIterator implements ByteIterator
{
	private ByteNode current;
	private int subTotal;
	private int subIndex;

	public linearByteIterator(ByteList in)
	{
		this(in.getHead_internal());
	}
	
	linearByteIterator(ByteNode inCurrent)
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
	public byte next()
	{
		return this.next_asByte();
	}

	@Override
	public byte next_asByte()
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
