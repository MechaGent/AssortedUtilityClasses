package Collections.Maps.RadixMap.Linked.Generic.Mk01;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import Collections.PackedArrays.HalfByte.HalfByteArray;

class Sets
{
	private Sets()
	{
		// to prevent instantiation
	}

	private static abstract class BaseSet<V> implements Set<V>
	{
		protected BaseSet()
		{

		}
		
		@Override
		public final boolean add(V inE)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public final boolean addAll(Collection<? extends V> inC)
		{
			final Iterator<? extends V> itsy = inC.iterator();
			boolean noChange = true;

			while (itsy.hasNext())
			{
				noChange &= !this.add(itsy.next());
			}

			return !noChange;
		}
		
		@Override
		public final boolean containsAll(Collection<?> inC)
		{
			for (Object entry : inC)
			{
				if (!this.contains(entry))
				{
					return false;
				}
			}

			return true;
		}
		
		@Override
		public final boolean remove(Object inO)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public final boolean removeAll(Collection<?> inC)
		{
			boolean noChange = true;

			for (Object entry : inC)
			{
				noChange &= !this.remove(entry);
			}

			return !noChange;
		}
	}

	static class EntriesSet<U> extends BaseSet<Entry<HalfByteArray, U>>
	{
		private final LinkedRadixMap<U> core;

		EntriesSet(LinkedRadixMap<U> inCore)
		{
			this.core = inCore;
		}

		@Override
		public final void clear()
		{
			this.core.clear();
		}

		@Override
		public final boolean contains(Object inO)
		{
			// incredibly speculative cast, because if it fails, I would've written it to fail anyway
			@SuppressWarnings("unchecked")
			final Entry<HalfByteArray, U> cast = (Entry<HalfByteArray, U>) inO;

			return this.core.get(cast.getKey()) == cast.getValue();
		}

		@Override
		public final boolean isEmpty()
		{
			return this.core.isEmpty();
		}

		@Override
		public final Iterator<Entry<HalfByteArray, U>> iterator()
		{
			return this.core.entriesIterator();
		}

		@Override
		public final boolean retainAll(Collection<?> inC)
		{
			final boolean result = !this.core.isEmpty() || !inC.isEmpty();
			this.core.clear();

			for (Object entry : inC)
			{
				// incredibly speculative cast, because if it fails, I would've written it to fail anyway
				@SuppressWarnings("unchecked")
				final Entry<HalfByteArray, U> cast = (Entry<HalfByteArray, U>) entry;

				this.core.put(cast.getKey(), cast.getValue());
			}

			return result;
		}

		@Override
		public final int size()
		{
			return this.core.size();
		}

		@Override
		public final Object[] toArray()
		{
			@SuppressWarnings("unchecked")
			final Entry<HalfByteArray, U>[] result = new Entry[this.size()];
			final Iterator<Entry<HalfByteArray, U>> itsy = this.core.entriesIterator();

			for (int i = 0; i < result.length; i++)
			{
				result[i] = itsy.next();
			}

			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		public final <T> T[] toArray(T[] inA)
		{
			if (inA.length >= this.size())
			{
				final Iterator<Entry<HalfByteArray, U>> itsy = this.core.entriesIterator();

				for (int i = 0; i < inA.length; i++)
				{
					inA[i] = (T) itsy.next();
				}

				return inA;
			}
			else
			{
				return (T[]) this.toArray();
			}
		}
	}
	
	static class StringKeyedEntriesSet<U> extends BaseSet<Entry<String, U>>
	{
		private final LinkedRadixMap<U> core;

		StringKeyedEntriesSet(LinkedRadixMap<U> inCore)
		{
			this.core = inCore;
		}

		@Override
		public final void clear()
		{
			this.core.clear();
		}

		@Override
		public final boolean contains(Object inO)
		{
			// incredibly speculative cast, because if it fails, I would've written it to fail anyway
			@SuppressWarnings("unchecked")
			final Entry<String, U> cast = (Entry<String, U>) inO;

			return this.core.get(cast.getKey()) == cast.getValue();
		}

		@Override
		public final boolean isEmpty()
		{
			return this.core.isEmpty();
		}

		@Override
		public final Iterator<Entry<String, U>> iterator()
		{
			return this.core.stringKeyedEntriesIterator();
		}

		@Override
		public final boolean retainAll(Collection<?> inC)
		{
			final boolean result = !this.core.isEmpty() || !inC.isEmpty();
			this.core.clear();

			for (Object entry : inC)
			{
				// incredibly speculative cast, because if it fails, I would've written it to fail anyway
				@SuppressWarnings("unchecked")
				final Entry<HalfByteArray, U> cast = (Entry<HalfByteArray, U>) entry;

				this.core.put(cast.getKey(), cast.getValue());
			}

			return result;
		}

		@Override
		public final int size()
		{
			return this.core.size();
		}

		@Override
		public final Object[] toArray()
		{
			@SuppressWarnings("unchecked")
			final Entry<HalfByteArray, U>[] result = new Entry[this.size()];
			final Iterator<Entry<HalfByteArray, U>> itsy = this.core.entriesIterator();

			for (int i = 0; i < result.length; i++)
			{
				result[i] = itsy.next();
			}

			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		public final <T> T[] toArray(T[] inA)
		{
			if (inA.length >= this.size())
			{
				final Iterator<Entry<HalfByteArray, U>> itsy = this.core.entriesIterator();

				for (int i = 0; i < inA.length; i++)
				{
					inA[i] = (T) itsy.next();
				}

				return inA;
			}
			else
			{
				return (T[]) this.toArray();
			}
		}
	}

	static class KeysSet extends BaseSet<HalfByteArray>
	{
		private final LinkedRadixMap<?> core;

		KeysSet(LinkedRadixMap<?> inCore)
		{
			this.core = inCore;
		}

		@Override
		public final void clear()
		{
			this.core.clear();
		}

		@Override
		public final boolean contains(Object inO)
		{
			return this.core.containsKey(inO);
		}

		@Override
		public final boolean isEmpty()
		{
			return this.core.isEmpty();
		}

		@Override
		public final Iterator<HalfByteArray> iterator()
		{
			return this.core.keysIterator();
		}

		@Override
		public final boolean retainAll(Collection<?> inC)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public final int size()
		{
			return this.core.size();
		}

		@Override
		public final Object[] toArray()
		{
			final HalfByteArray[] result = new HalfByteArray[this.size()];
			final Iterator<HalfByteArray> itsy = this.iterator();

			for (int i = 0; i < result.length; i++)
			{
				result[i] = itsy.next();
			}

			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		public final <T> T[] toArray(T[] inA)
		{
			if (inA.length >= this.size())
			{
				final Iterator<HalfByteArray> itsy = this.iterator();

				for (int i = 0; i < inA.length; i++)
				{
					inA[i] = (T) itsy.next();
				}

				return inA;
			}
			else
			{
				return (T[]) this.toArray();
			}
		}
	}

	static class ValuesSet<U> extends BaseSet<U>
	{
		private final LinkedRadixMap<U> core;

		ValuesSet(LinkedRadixMap<U> inLinkedRadixMapBase)
		{
			this.core = inLinkedRadixMapBase;
		}

		@Override
		public final void clear()
		{
			this.core.clear();
		}

		@Override
		public final boolean contains(Object inArg0)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public final boolean isEmpty()
		{
			return this.core.isEmpty();
		}

		@Override
		public final Iterator<U> iterator()
		{
			return this.core.valuesIterator();
		}

		@Override
		public final boolean retainAll(Collection<?> inArg0)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public final int size()
		{
			return this.core.size();
		}

		@Override
		public final Object[] toArray()
		{
			@SuppressWarnings("unchecked")
			final U[] result = (U[]) new Object[this.size()];
			final Iterator<U> itsy = this.iterator();

			for (int i = 0; i < result.length; i++)
			{
				result[i] = itsy.next();
			}

			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		public final <T> T[] toArray(T[] inA)
		{
			if (inA.length >= this.size())
			{
				final Iterator<U> itsy = this.iterator();

				for (int i = 0; i < inA.length; i++)
				{
					inA[i] = (T) itsy.next();
				}

				return inA;
			}
			else
			{
				return (T[]) this.toArray();
			}
		}
	}
}
