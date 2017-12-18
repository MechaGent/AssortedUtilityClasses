package HandyStuff;

import java.util.Iterator;

public final class EmptyIterator<U> implements Iterator<U>
{
	public EmptyIterator()
	{
		
	}
	
	@Override
	public final boolean hasNext()
	{
		return false;
	}

	@Override
	public final U next()
	{
		throw new IndexOutOfBoundsException();
	}
}
