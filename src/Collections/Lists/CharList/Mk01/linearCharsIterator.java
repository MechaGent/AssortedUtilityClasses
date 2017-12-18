package Collections.Lists.CharList.Mk01;

import Collections.PrimitiveInterfaceAnalogues.Char.AbstractCharIterator;

public class linearCharsIterator implements AbstractCharIterator
{
	protected Node currNode;
	protected int subIndex;
	protected int subTotal;

	public linearCharsIterator(CharList in)
	{
		this.currNode = in.head;
		this.subIndex = 0;
		this.subTotal = in.head.getSizeInChars();
	}

	public boolean hasNext()
	{
		return (this.subIndex < this.subTotal) || !this.currNode.next.isNull();
	}

	@SuppressWarnings("unused")
	private boolean nextCharIncrementsNode()
	{
		return !(this.subIndex < this.subTotal) && !this.currNode.next.isNull();
	}

	public char next()
	{
		return this.next_asChar();
	}

	@Override
	public char next_asChar()
	{
		final char result;

		if (this.subIndex < this.subTotal)
		{
			result = this.currNode.CharAt(this.subIndex);
			this.subIndex++;
		}
		else
		{
			this.currNode = this.currNode.next;

			if (!this.currNode.isNull())
			{
				result = this.currNode.CharAt(0);
				this.subIndex = 1;
				this.subTotal = this.currNode.getSizeInChars();
			}
			else
			{
				throw new IndexOutOfBoundsException();
			}
		}

		return result;
	}
}