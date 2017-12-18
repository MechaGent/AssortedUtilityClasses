package Collections.Maps.RadixMap.Linked.Generic;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import Collections.Maps.RadixMap.Linked.Generic.Iterators.KeysItsy;
import Collections.PackedArrays.HalfByte.HalfByteArray;

public class LinkedRadixSet implements Set<HalfByteArray>
{
	private final LinkedRadixMap<String> core;
	
	public LinkedRadixSet()
	{
		this.core = new LinkedRadixMap<String>();
	}

	public final boolean add(int key)
	{
		return this.core.put(key, "") == null;
	}
	
	public final boolean add(long key)
	{
		return this.core.put(key, "") == null;
	}
	
	public final boolean add(char[] key)
	{
		return this.core.put(key, "") == null;
	}
	
	public final boolean add(String key)
	{
		return this.core.put(key, "") == null;
	}
	
	@Override
	public final boolean add(HalfByteArray key)
	{
		return this.core.put(key, "") == null;
	}

	@Override
	public final boolean addAll(Collection<? extends HalfByteArray> inC)
	{
		final Iterator<? extends HalfByteArray> itsy = inC.iterator();
		
		while(itsy.hasNext())
		{
			if(this.add(itsy.next()))
			{
				while(itsy.hasNext())
				{
					this.add(itsy.next());
				}
				
				return true;
			}
		}
		
		return false;
	}

	@Override
	public final void clear()
	{
		this.core.clear();
	}

	@Override
	public final boolean contains(Object inO)
	{
		return false;
	}
	
	public final boolean contains(int key)
	{
		return this.core.containsKey(key);
	}
	
	public final boolean contains(long key)
	{
		return this.core.containsKey(key);
	}
	
	public final boolean contains(char[] key)
	{
		return this.core.containsKey(key);
	}
	
	public final boolean contains(String key)
	{
		return this.core.containsKey(key);
	}
	
	public final boolean contains(HalfByteArray inE)
	{
		return this.core.containsKey(inE);
	}

	@Override
	public final boolean containsAll(Collection<?> inC)
	{
		for(Object obj: inC)
		{
			if(!this.contains(obj))
			{
				return false;
			}
		}
		
		return true;
	}

	@Override
	public final boolean isEmpty()
	{
		return this.core.isEmpty();
	}
	
	public final boolean isNotEmpty()
	{
		return this.core.isNotEmpty();
	}

	@Override
	public final KeysItsy iterator()
	{
		return this.core.keysIterator();
	}

	@Override
	public final boolean remove(Object inO)
	{
		return this.core.remove(inO) != null;
	}

	@Override
	public final boolean removeAll(Collection<?> inC)
	{
		final Iterator<?> itsy = inC.iterator();
		
		while(itsy.hasNext())
		{
			if(this.remove(itsy.next()))
			{
				while(itsy.hasNext())
				{
					this.remove(itsy.next());
				}
				
				return true;
			}
		}
		
		return false;
	}

	@Override
	public final boolean retainAll(Collection<?> inC)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public final int size()
	{
		return this.size();
	}

	@Override
	public final Object[] toArray()
	{
		final Object[] result = new Object[this.size()];
		final KeysItsy itsy = this.core.keysIterator();
		int i = 0;
		
		while(itsy.hasNext())
		{
			result[i] = itsy.next();
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <T> T[] toArray(T[] in)
	{
		if (in.length != this.size())
		{
			in = (T[]) new Object[this.size()];
		}
		
		if(this.size() == 0)
		{
			return in;
		}

		final KeysItsy itsy = this.core.keysIterator();

		for (int i = 0; i < this.size(); i++)
		{
			in[i] = (T) itsy.next();
		}

		return in;
	}
}
