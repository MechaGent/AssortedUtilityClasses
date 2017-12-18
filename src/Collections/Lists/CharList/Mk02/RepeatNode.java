package Collections.Lists.CharList.Mk02;

abstract class RepeatNode extends MultiNode
{
	protected int numRepeats;

	RepeatNode(int inNumRepeats)
	{
		super();
		this.numRepeats = inNumRepeats;
	}

	RepeatNode(Node inIn, int inNumRepeats)
	{
		super(inIn);
		this.numRepeats = inNumRepeats;
	}

	final int getNumRepeats()
	{
		return this.numRepeats;
	}
	
	final int decrementAndGetNumRepeats()
	{
		return --this.numRepeats;
	}
	
	final int getAndDecrementNumRepeats()
	{
		return this.numRepeats--;
	}
	
	@Override
	abstract char lastChar();
}
