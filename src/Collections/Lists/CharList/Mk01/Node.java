package Collections.Lists.CharList.Mk01;

import Collections.PackedArrays.HalfByte.HalfByteArray;

abstract class Node
{
	protected static final boolean activateDefensiveMode = CharList.activateDefensiveMode;
	
	protected Node next;
	
	protected Node()
	{
		this.next = CharList.NullNode;
	}
	
	public Node getNext()
	{
		return this.next;
	}
	
	public boolean isNull()
	{
		return false;
	}
	
	public boolean isNotNull()
	{
		return true;
	}
	
	public abstract int getSizeInChars();

	/**
	 * 
	 * @param i
	 *            relative index
	 * @return
	 */
	public abstract char CharAt(int i);

	public abstract char firstChar();

	public abstract char lastChar();
	
	abstract void fillCharArray_internal(char[] target, int startIndex);
	abstract Node copySelf();
	
	void fillCharArray(char[] target, int startIndex)
	{
		Node current = this;
		
		while(current.isNotNull())
		{
			current.fillCharArray_internal(target, startIndex);
			startIndex += current.getSizeInChars();
			current = current.next;
		}
	}
	
	public final CharList copyCharList(CharList in)
	{
		final CharList result = new CharList();
		Node current = this;
		
		while(current.isNotNull())
		{
			result.add_internal(current.copySelf());
			current = current.next;
		}
		
		return result;
	}

	public static final void link(Node var1, Node var2)
	{
		var1.next = var2;
		
		//System.out.println("var1: " + var1.toString() + ", var2: " + var2.toString());
	}
	
	static final Node createNode(char in)
	{
		return new Node_Char(in);
	}

	/**
	 * arrays of length 0 should be caught before this is called
	 * 
	 * @param in
	 * @return
	 */
	static final Node createNode(char[] in)
	{
		if (activateDefensiveMode)
		{
			if (in.length == 0)
			{
				throw new IllegalArgumentException("Spurious object creation!");
			}
		}

		final Node result;

		if (in.length == 1)
		{
			result = createNode(in[0]);
		}
		else
		{
			result = new Node_Char_Arr(in);
		}
		
		if(result == null)
		{
			throw new NullPointerException();
		}

		return result;
	}

	static final Node createNode(char in, int numRepeats)
	{
		if (activateDefensiveMode)
		{
			if (numRepeats == 0)
			{
				throw new IllegalArgumentException("Spurious object creation!");
			}
		}

		final Node result;

		if (numRepeats == 1)
		{
			result = createNode(in);
		}
		else
		{
			result = new Node_Char_Repeated(in, numRepeats);
		}
		
		if(result == null)
		{
			throw new NullPointerException();
		}

		return result;
	}

	static final Node createNode(char[] in, int numRepeats)
	{
		if (activateDefensiveMode)
		{
			if (numRepeats == 0)
			{
				throw new IllegalArgumentException("Spurious object creation!");
			}
		}

		final Node result;

		if (numRepeats == 1)
		{
			result = createNode(in);
		}
		else
		{
			result = new Node_Char_Arr_Repeated(in, numRepeats);
		}
		
		if(result == null)
		{
			throw new NullPointerException();
		}

		return result;
	}

	static final Node createNode(String in)
	{
		if (activateDefensiveMode)
		{
			if (in == null)
			{
				throw new NullPointerException();
			}
			else if (in.length() == 0)
			{
				throw new IllegalArgumentException("Spurious object creation!");
			}
		}

		final Node result;

		if (in.length() == 1)
		{
			result = createNode(in.charAt(0));
			
			if(result.next == null)
			{
				throw new NullPointerException();
			}
		}
		else
		{
			//System.out.println(in + ", with first char of 0x" + Integer.toHexString(in.charAt(0)));
			result = new Node_String(in);
			
			if(result.next == null)
			{
				throw new NullPointerException();
			}
		}
		
		return result;
	}

	static final Node createNode(String in, int numRepeats)
	{
		if (activateDefensiveMode)
		{
			if (in == null)
			{
				throw new NullPointerException();
			}
			else if (in.length() == 0)
			{
				throw new IllegalArgumentException("Spurious object creation!");
			}
			else if (numRepeats == 0)
			{
				throw new IllegalArgumentException("Spurious object creation!");
			}
		}

		final Node result;

		if (numRepeats == 1)
		{
			if (in.length() == 1)
			{
				result = createNode(in.charAt(0));
			}
			else
			{
				result = createNode(in);
			}
		}
		else
		{
			if (in.length() == 1)
			{
				result = createNode(in.charAt(0), numRepeats);
			}
			else
			{
				result = new Node_String_Repeated(in, numRepeats);
			}
		}

		if(result == null)
		{
			throw new NullPointerException();
		}
		
		return result;
	}
	
	/**
	 * arrays of length 0 should be caught before this is called
	 * 
	 * @param in
	 * @return
	 */
	static final Node createNode(HalfByteArray in)
	{
		if (activateDefensiveMode)
		{
			if (in.length() == 0)
			{
				throw new IllegalArgumentException("Spurious object creation!");
			}
		}

		final Node result;

		if (in.length() == 1)
		{
			result = createNode((char) in.getFirstHalfByte());
		}
		else
		{
			result = new Node_Char_HalfByteArr(in);
		}
		
		if(result == null)
		{
			throw new NullPointerException();
		}

		return result;
	}
}
