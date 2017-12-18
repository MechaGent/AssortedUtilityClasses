package Collections.Lists.CharList.Mk02;

import Collections.PrimitiveInterfaceAnalogues.Char.AbstractCharIterator;

public class linearCharsIterator implements AbstractCharIterator
{
	private Node_Char current_SingleChar;
	private MultiNode current_Multi;
	private int place_multi; // == -1 when singleChar is active
	private int length_multi;

	private final int length;
	private int place;

	linearCharsIterator(Node head, int inLength)
	{
		if (head instanceof Node_Char)
		{
			this.current_SingleChar = (Node_Char) head;
			this.current_Multi = null;
			this.place_multi = -1;
			this.length_multi = 0;
		}
		else
		{
			this.current_Multi = (MultiNode) head;
			this.current_SingleChar = null;
			this.place_multi = 0;
			this.length_multi = this.current_Multi.getLength();
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
	public final char next()
	{
		return this.next_asChar();
	}

	private final boolean singleCharIsActive()
	{
		return this.place_multi == -1;
	}

	private final void setNext(Node in)
	{
		if (in instanceof Node_Char)
		{
			this.current_SingleChar = (Node_Char) in;
			this.place_multi = -1;

			// commented out to improve performance, but left in to remind
			// this.current_Multi = null;
			// this.length_multi = 0;
		}
		else
		{
			this.current_Multi = (MultiNode) in;
			this.place_multi = 0;
			this.length_multi = this.current_Multi.getLength();

			// commented out to improve performance, but left in to remind
			// this.current_SingleChar = null;
		}
	}

	@Override
	public final char next_asChar()
	{
		final char result;

		if (this.singleCharIsActive())
		{
			result = this.current_SingleChar.getCargo();
			this.setNext(this.current_SingleChar.getNext());
		}
		else
		{
			if (this.place_multi < this.length_multi)
			{
				result = this.current_Multi.CharAt(this.place_multi++);
			}
			else
			{
				final Node nextRaw = this.current_Multi.getNext();

				if (nextRaw instanceof Node_Char)
				{
					result = ((Node_Char) nextRaw).getCargo();
					this.setNext(nextRaw.getNext());
				}
				else
				{
					this.current_Multi = (MultiNode) nextRaw;
					result = this.current_Multi.firstChar();
					this.place_multi = 1;
					this.length_multi = this.current_Multi.getLength();
				}
			}
		}

		this.place++;
		return result;
	}

	public final char[] remainingToCharArray()
	{
		final char[] result = new char[this.length - this.place];

		if (this.singleCharIsActive())
		{
			this.current_SingleChar.toCharArray_internal(result, 0);
		}
		else
		{
			int i = 0;

			for (; i < this.length_multi - this.place_multi; i++)
			{
				result[i] = this.current_Multi.CharAt(i + this.place_multi);
			}

			this.current_Multi.toCharArray_internal(result, i);
		}

		return result;
	}

	public final char[] remainingToCharArray(char first)
	{
		final char[] result = new char[(this.length - this.place) + 1];
		result[0] = first;

		if (this.singleCharIsActive())
		{
			this.current_SingleChar.toCharArray_internal(result, 1);
		}
		else
		{
			int i = 1;

			for (; i < this.length_multi - this.place_multi; i++)
			{
				result[i] = this.current_Multi.CharAt(i + this.place_multi - 1);
			}

			this.current_Multi.toCharArray_internal(result, i);
		}

		return result;
	}
}
