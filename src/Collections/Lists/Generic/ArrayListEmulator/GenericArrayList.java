package Collections.Lists.Generic.ArrayListEmulator;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.IntFunction;

import CustomExceptions.FatalLazinessException;

public final class GenericArrayList<U> implements List<U>
{
	private static final int DefaultStartCapacity = 16;

	private final IntFunction<U[]> arrConstructor;
	private U[] core;
	private int size;

	public GenericArrayList(IntFunction<U[]> arrConstructor)
	{
		this(DefaultStartCapacity, arrConstructor);
	}

	public GenericArrayList(int startCapacity, IntFunction<U[]> arrConstructor)
	{
		this.arrConstructor = arrConstructor;
		this.core = arrConstructor.apply(startCapacity);
		this.size = 0;
	}

	private final void ensureIncrementalCapacity()
	{
		if ((this.size + 1) < this.core.length)
		{
			return;
		}
		else
		{
			final int newLength = this.core.length * 2;

			final U[] newCore = this.arrConstructor.apply(newLength);

			System.arraycopy(this.core, 0, newCore, 0, newLength);

			this.core = newCore;
		}
	}

	private final void ensureCapacity(int delta)
	{
		final int newMinCap = this.size + delta;

		if (newMinCap < this.core.length)
		{
			return;
		}

		int newLength = this.core.length * 2;

		while (newLength <= newMinCap)
		{
			newLength *= 2;
		}

		final U[] newCore = this.arrConstructor.apply(newLength);

		System.arraycopy(this.core, 0, newCore, 0, newLength);

		this.core = newCore;
	}

	@Override
	public final boolean add(U value)
	{
		this.ensureIncrementalCapacity();

		this.core[this.size] = value;

		return true;
	}

	@Override
	public final void add(int index, U value)
	{
		if (index == this.size + 1)
		{
			this.add(value);
		}
		else
		{
			if (index+1 < this.core.length)
			{
				// shifting
				final int newLength = this.core.length * 2;

				final U[] newCore = this.arrConstructor.apply(newLength);

				System.arraycopy(this.core, 0, newCore, 0, index);
				System.arraycopy(this.core, index, newCore, index+1, this.size-index);

				this.core = newCore;
				this.size += 1;
			}
			else
			{
				int newLength = this.core.length * 2;

				while (newLength <= index)
				{
					newLength *= 2;
				}

				final U[] newCore = this.arrConstructor.apply(newLength);

				System.arraycopy(this.core, 0, newCore, 0, newLength);

				this.core = newCore;
				this.size = index+1;
			}

			this.core[index] = value;
		}
	}

	@Override
	public final boolean addAll(Collection<? extends U> collection)
	{
		if(collection.isEmpty())
		{
			return false;
		}
		
		final Iterator<? extends U> itsy = collection.iterator();
		this.ensureCapacity(collection.size());
		
		while(itsy.hasNext())
		{
			this.add(itsy.next());
		}
		
		return true;
	}

	@Override
	public final boolean addAll(int index, Collection<? extends U> collection)
	{
		throw new FatalLazinessException();
	}

	/**
	 * behaves as if {@link #clear(false)} was called
	 */
	@Override
	public final void clear()
	{
		this.core = this.arrConstructor.apply(this.core.length);
		this.size = 0;
	}
	
	public final void clear(boolean resetSize)
	{
		if(resetSize)
		{
			this.core = this.arrConstructor.apply(DefaultStartCapacity);
		}
		else
		{
			this.core = this.arrConstructor.apply(this.core.length);
		}
		
		this.size = 0;
	}

	@Override
	public final boolean contains(Object o)
	{
		if(o == null)
		{
			for(int i = 0; i < this.size; i+=1)
			{
				if(this.core[i] == null)
				{
					return true;
				}
			}
		}
		else
		{
			for(int i = 0; i < this.size; i+=1)
			{
				final Object cur = this.core[i];
				
				if(cur != null && o.equals(cur))
				{
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public final boolean containsAll(Collection<?> c)
	{
		final HashSet<U> set = new HashSet<U>();
		
		for(int i = 0; i < this.size; i+=1)
		{
			set.add(this.core[i]);
		}
		
		final Iterator<?> rawItsy = c.iterator();
		
		while(rawItsy.hasNext())
		{
			if(!set.contains(rawItsy.next()))
			{
				return false;
			}
		}
		
		return true;
	}

	@Override
	public final U get(int index)
	{
		return this.core[index];
	}

	@Override
	public final int indexOf(Object o)
	{
		for(int i = 0; i < this.size; i+=1)
		{
			if(o.equals(this.core[i]))
			{
				return i;
			}
		}
		
		return -1;
	}

	@Override
	public final boolean isEmpty()
	{
		return this.size == 0;
	}

	@Override
	public final Iterator<U> iterator()
	{
		return new GenArrListItsy<U>(this.core, this.size);
	}

	@Override
	public final int lastIndexOf(Object o)
	{
		for(int i = this.size; (i--) > 0;)
		{
			if(o.equals(this.core[i]))
			{
				return i;
			}
		}
		
		return -1;
	}

	@Override
	public final ListIterator<U> listIterator()
	{
		return new GenArrListItsy<U>(this.core, this.size);
	}

	@Override
	public final ListIterator<U> listIterator(int index)
	{
		return new GenArrListItsy<U>(this.core, this.size, index);
	}

	@Override
	public final boolean remove(Object o)
	{
		throw new FatalLazinessException();
	}

	@Override
	public final U remove(int index)
	{
		throw new FatalLazinessException();
	}

	@Override
	public final boolean removeAll(Collection<?> c)
	{
		throw new FatalLazinessException();
	}

	@Override
	public final boolean retainAll(Collection<?> c)
	{
		throw new FatalLazinessException();
	}

	@Override
	public final U set(int index, U element)
	{
		final U result = this.core[index];
		this.core[index] = element;
		return result;
	}

	@Override
	public final int size()
	{
		return this.size;
	}

	@Override
	public final List<U> subList(int fromIndex, int toIndex)
	{
		throw new FatalLazinessException();
	}

	@Override
	public final U[] toArray()
	{
		final U[] result = this.arrConstructor.apply(this.size);
		
		System.arraycopy(this.core, 0, result, 0, this.size);
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <T> T[] toArray(T[] a)
	{
		if(a.length < this.size)
		{
			a = (T[]) Array.newInstance(a.getClass().getComponentType(), this.size);
		}
		
		System.arraycopy(this.core, 0, a, 0, this.size);
		
		return a;
	}
	
	public static final class GenArrListItsy<U> implements ListIterator<U>
	{
		private final U[] core;
		private final int size;
		private int index;
		
		public GenArrListItsy(U[] inCore, int inSize)
		{
			this(inCore, inSize, 0);
		}

		public GenArrListItsy(U[] inCore, int inSize, int inIndex)
		{
			this.core = inCore;
			this.size = inSize;
			this.index = inIndex;
		}

		@Override
		public final boolean hasNext()
		{
			return this.index < this.size;
		}

		@Override
		public final U next()
		{
			return this.core[this.index++];
		}

		@Override
		public final void add(U inE)
		{
			throw new FatalLazinessException();
		}

		@Override
		public final boolean hasPrevious()
		{
			return (this.index-1) >= 0;
		}

		@Override
		public final int nextIndex()
		{
			return this.index + 1;
		}

		@Override
		public final U previous()
		{
			return this.core[--this.index];
		}

		@Override
		public final int previousIndex()
		{
			return this.index - 1;
		}

		@Override
		public final void remove()
		{
			throw new FatalLazinessException();
		}

		@Override
		public final void set(U inE)
		{
			throw new FatalLazinessException();
		}
	}
}
