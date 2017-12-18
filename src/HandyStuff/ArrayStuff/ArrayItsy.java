package HandyStuff.ArrayStuff;

import java.util.Iterator;

public class ArrayItsy<U> implements Iterator<U>
{
	private final U[] core;
	private int index;

	public ArrayItsy(U[] inCore)
	{
		this.core = inCore;
		this.index = 0;
	}

	@Override
	public final boolean hasNext()
	{
		return this.index < this.core.length;
	}

	@Override
	public final U next()
	{
		return this.core[this.index++];
	}
}
