package Collections.Lists.CharList.Mk02;

import Collections.PackedArrays.HalfByte.HalfByteArray;
import CustomExceptions.UnhandledEnumException;

class Node_HalfByteArr extends MultiNode
{
	private final HalfByteArray cargo;
	private final PrintModes printMode;

	Node_HalfByteArr(HalfByteArray inCargo, PrintModes inMode)
	{
		super();
		this.cargo = inCargo;
		this.printMode = inMode;
	}

	Node_HalfByteArr(Node inIn, HalfByteArray inCargo, PrintModes inMode)
	{
		super(inIn);
		this.cargo = inCargo;
		this.printMode = inMode;
	}

	@Override
	final int getLength()
	{
		switch (this.printMode)
		{
			case AsChars:
			{
				return this.cargo.length() / 2;
			}
			case AsValues:
			{
				return this.cargo.length();
			}
			default:
			{
				throw new UnhandledEnumException(this.printMode);
			}
		}
	}

	@Override
	final CharList splitIntoNewCharList()
	{
		switch (this.printMode)
		{
			case AsChars:
			{
				final Node_Char head = new Node_Char(this.cargo.interpretAsChar(0));
				Node_Char current = head;

				for (int i = 2; i < this.cargo.length(); i += 2)
				{
					final Node_Char next = new Node_Char(this.cargo.interpretAsChar(i));
					current.setNext(next);
					current = next;
				}

				return new CharList(head, current, this.cargo.length());
			}
			case AsValues:
			{
				final Node_Char head = new Node_Char(NumNodeConversion.getHexChar_inCaps(this.cargo.getFirstHalfByte()));
				Node_Char current = head;

				for (int i = 1; i < this.cargo.length(); i++)
				{
					final Node_Char next = new Node_Char(NumNodeConversion.getHexChar_inCaps(this.cargo.getHalfByteAt(i)));
					current.setNext(next);
					current = next;
				}

				return new CharList(head, current, this.cargo.length());
			}
			default:
			{
				throw new UnhandledEnumException(this.printMode);
			}
		}
	}

	@Override
	final CharList splitIntoNewCharList_skipFirst()
	{
		switch (this.printMode)
		{
			case AsChars:
			{
				final Node_Char head = new Node_Char(this.cargo.interpretAsChar(2));
				Node_Char current = head;

				for (int i = 4; i < this.cargo.length(); i += 2)
				{
					final Node_Char next = new Node_Char(this.cargo.interpretAsChar(i));
					current.setNext(next);
					current = next;
				}

				return new CharList(head, current, this.cargo.length());
			}
			case AsValues:
			{
				final Node_Char head = new Node_Char(NumNodeConversion.getHexChar_inCaps(this.cargo.getHalfByteAt(1)));
				Node_Char current = head;

				for (int i = 2; i < this.cargo.length(); i++)
				{
					final Node_Char next = new Node_Char(NumNodeConversion.getHexChar_inCaps(this.cargo.getHalfByteAt(i)));
					current.setNext(next);
					current = next;
				}

				return new CharList(head, current, this.cargo.length());
			}
			default:
			{
				throw new UnhandledEnumException(this.printMode);
			}
		}
	}

	@Override
	protected final int toCharArray_internal(char[] result, int offset)
	{
		switch (this.printMode)
		{
			case AsChars:
			{
				offset = this.cargo.interpretAsCharsIntoCharArr(result, offset);
				break;
			}
			case AsValues:
			{
				for (int i = 0; i < this.cargo.length(); i++)
				{
					result[offset++] = NumNodeConversion.getHexChar_inCaps(this.cargo.getHalfByteAt(i));
				}
				break;
			}
			default:
			{
				throw new UnhandledEnumException(this.printMode);
			}
		}

		return offset;
	}

	@Override
	final char CharAt(int index)
	{
		switch (this.printMode)
		{
			case AsChars:
			{
				return this.cargo.interpretAsChar(index / 2);
			}
			case AsValues:
			{
				return NumNodeConversion.getHexChar_inCaps(this.cargo.getHalfByteAt(index));
			}
			default:
			{
				throw new UnhandledEnumException(this.printMode);
			}
		}
	}

	static enum PrintModes
	{
		AsValues,
		AsChars;
	}
}
