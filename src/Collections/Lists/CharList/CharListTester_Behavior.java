package Collections.Lists.CharList;

import Collections.Lists.CharList.BaseCharList.CharListIterator;

final class CharListTester_Behavior implements CharListTester
{
	private final BaseCharList variable;
	private final Collections.Lists.CharList.CharList control;
	private long size;
	
	public CharListTester_Behavior()
	{
		this.variable = new BaseCharList();
		this.control = new Collections.Lists.CharList.CharList();
		this.size = 0;
	}
	
	/* (non-Javadoc)
	 * @see Incubator.CharList.Mk02.CharListTester2#add(char)
	 */
	@Override
	public final void add(char in)
	{
		this.variable.add(in);
		this.control.add(in);
		this.size += 1;
	}
	
	/* (non-Javadoc)
	 * @see Incubator.CharList.Mk02.CharListTester2#add(char, int)
	 */
	@Override
	public final void add(char in, int numRepeats)
	{
		//System.out.println(this.variable.toString());
		this.variable.add(in, numRepeats);
		this.control.add(in, numRepeats);
		this.size += numRepeats;
	}
	
	/* (non-Javadoc)
	 * @see Incubator.CharList.Mk02.CharListTester2#add(char[])
	 */
	@Override
	public final void add(char[] in)
	{
		this.variable.add(in);
		this.control.add(in);
		this.size += in.length;
	}
	
	/* (non-Javadoc)
	 * @see Incubator.CharList.Mk02.CharListTester2#add(char[], int)
	 */
	@Override
	public final void add(char[] in, int numRepeats)
	{
		this.variable.add(in, numRepeats);
		this.control.add(in, numRepeats);
		this.size += in.length * numRepeats;
	}
	
	/* (non-Javadoc)
	 * @see Incubator.CharList.Mk02.CharListTester2#add(java.lang.String)
	 */
	@Override
	public final void add(String in)
	{
		this.variable.add(in);
		this.control.add(in);
		this.size += in.length();
	}
	
	/* (non-Javadoc)
	 * @see Incubator.CharList.Mk02.CharListTester2#add(java.lang.String, int)
	 */
	@Override
	public final void add(String in, int numRepeats)
	{
		this.variable.add(in, numRepeats);
		this.control.add(in, numRepeats);
		this.size += in.length() * numRepeats;
	}
	
	public final void add(String[] in)
	{
		final long sizeDelta = this.variable.add(in);
		this.control.add(in);
		this.size += sizeDelta;
	}
	
	/* (non-Javadoc)
	 * @see Incubator.CharList.Mk02.CharListTester2#charsAgreeAt(long)
	 */
	@Override
	public final boolean charsAgreeAt(long index)
	{
		final char var = this.variable.charAt(index);
		final char con = this.control.charAt((int) index);
		
		return var == con;
	}
	
	/* (non-Javadoc)
	 * @see Incubator.CharList.Mk02.CharListTester2#printResults()
	 */
	@Override
	public final void printResults()
	{
		final String varString = this.variable.toString();
		final String conString = this.control.toString();
		
		printResults("v", varString, "c", conString);
	}
	
	public static final void printResults(String var1Name, String var1, String var2Name, String var2)
	{
		if(var1.length() == var2.length())
		{
			System.out.println("lengths match!");
		}
		else
		{
			System.out.println("lengths:\r\n\t" + var1Name + ": " + var1.length() + "\r\n\t" + var2Name + ": " + var2.length());
		}

		if (var1.equals(var2))
		{
			System.out.println("strings match!");
		}
		else
		{
			System.out.println("strings do not match!\r\nstrings:\r\n\t" + var1Name + ": " + var1 + "\r\n\t" + var2Name + ": " + var2);
		}
	}
	
	public final void push(char in)
	{
		this.variable.push(in);
		this.control.push(in);
		this.size += 1;
	}
	
	public final void push(char in, int numRepeats)
	{
		//System.out.println(this.variable.toString());
		this.variable.push(in, numRepeats);
		this.control.push(in, numRepeats);
		this.size += numRepeats;
	}
	
	public final void push(char[] in)
	{
		this.variable.push(in);
		this.control.push(in);
		this.size += in.length;
	}
	
	public final void push(char[] in, int numRepeats)
	{
		this.variable.push(in, numRepeats);
		this.control.push(in, numRepeats);
		this.size += in.length * numRepeats;
	}
	
	public final void push(String in)
	{
		this.variable.push(in);
		this.control.push(in);
		this.size += in.length();
	}
	
	public final void push(String in, int numRepeats)
	{
		this.variable.push(in, numRepeats);
		this.control.push(in, numRepeats);
		this.size += in.length() * numRepeats;
	}
	
	public final CharListIterator listIterator()
	{
		return this.variable.listIterator();
	}
	
	public final char[] petrifyControl()
	{
		return this.control.toCharArray();
	}
	
	public final void checkVariable_checkActiveStates(int caseNum)
	{
		this.variable.checkActiveStates(caseNum);
	}
	
	/* (non-Javadoc)
	 * @see Incubator.CharList.Mk02.CharListTester2#size()
	 */
	@Override
	public final long size()
	{
		return this.size;
	}
}
