package Collections.Lists.Generic.Directed;

import java.util.Iterator;

public class ListItsy<U> implements Iterator<U>
{
	private SingleNode<U> current_single;

	private MultiNode<U> current_multi;
	private int place_multi; // == -1 when current_single is active
	private int length_multi;

	private final int length;
	private int place;

	ListItsy(Node<U> head, int inLength)
	{
		if (head instanceof SingleNode)
		{
			this.current_single = (SingleNode<U>) head;

			this.current_multi = null;
			this.place_multi = -1;
			this.length_multi = 0;
		}
		else
		{
			this.current_single = null;

			this.current_multi = (MultiNode<U>) head;
			this.place_multi = 0;
			this.length_multi = this.current_multi.length();
		}

		this.length = inLength;
		this.place = 0;
	}

	@Override
	public final boolean hasNext()
	{
		return this.place < this.length;
	}

	@Override
	public final U next()
	{
		final U result;

		if (this.singleIsActive())
		{
			result = this.current_single.getCargo();
			this.setNext(this.current_single.getNext());
		}
		else
		{
			if (this.place_multi < this.length_multi)
			{
				result = this.current_multi.cargoAt(this.place_multi++);
			}
			else
			{
				final Node<U> raw = this.current_multi.getNext();

				if (raw instanceof SingleNode)
				{
					result = ((SingleNode<U>) raw).getCargo();
					this.setNext(raw.getNext());
				}
				else
				{
					this.current_multi = (MultiNode<U>) raw;
					result = this.current_multi.firstCargo();
					this.place_multi = 1;
					this.length_multi = this.current_multi.length();
				}
			}
		}

		this.place++;
		return result;
	}

	final int getNextIndex()
	{
		return this.place;
	}

	private final boolean singleIsActive()
	{
		return this.place_multi == -1;
	}

	private final void setNext(Node<U> in)
	{
		if (in != null)
		{
			if (in instanceof SingleNode)
			{
				this.current_single = (SingleNode<U>) in;
				this.place_multi = -1;
			}
			else
			{
				this.current_multi = (MultiNode<U>) in;
				this.place_multi = 0;
				this.length_multi = this.current_multi.length();
			}
		}
		else
		{
			this.place = this.length;
		}
	}
}
