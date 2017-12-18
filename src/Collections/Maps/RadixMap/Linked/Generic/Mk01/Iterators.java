package Collections.Maps.RadixMap.Linked.Generic.Mk01;

import java.util.Iterator;
import java.util.Map.Entry;

import Collections.PackedArrays.HalfByte.HalfByteArray;

class Iterators
{
	private Iterators()
	{
		//to prevent instantiation
	}
	
	static class EntryItsy<U> implements Iterator<Entry<HalfByteArray, U>>
	{
		private LinkedCargoNode<U> current;
		
		public EntryItsy(LinkedCargoNode<U> inCurrent)
		{
			this.current = inCurrent;
		}

		@Override
		public final boolean hasNext()
		{
			return current != null;
		}

		@Override
		public final Entry<HalfByteArray, U> next()
		{
			final Entry<HalfByteArray, U> result = this.current;
			this.current = this.current.getNext();
			return result;
		}
	}
	
	static class StringKeyedEntryItsy<U> implements Iterator<Entry<String, U>>
	{
		private LinkedCargoNode<U> current;
		
		public StringKeyedEntryItsy(LinkedCargoNode<U> inCurrent)
		{
			this.current = inCurrent;
		}

		@Override
		public final boolean hasNext()
		{
			return current != null;
		}

		@Override
		public final Entry<String, U> next()
		{
			final Entry<String, U> result = new StringKeyedEntry<U>(this.current);
			this.current = this.current.getNext();
			return result;
		}
		
		private static class StringKeyedEntry<U> implements Entry<String, U>
		{
			private final LinkedCargoNode<U> node;
			
			public StringKeyedEntry(LinkedCargoNode<U> inNode)
			{
				this.node = inNode;
			}

			@Override
			public String getKey()
			{
				return this.node.getKey().interpretAsChars().toString();
			}

			@Override
			public U getValue()
			{
				return this.node.getValue();
			}

			@Override
			public U setValue(U inValue)
			{
				throw new UnsupportedOperationException();
			}
		}
	}
	
	static class KeysItsy implements Iterator<HalfByteArray>
	{
		private LinkedCargoNode<?> current;
		
		public KeysItsy(LinkedCargoNode<?> inCurrent)
		{
			this.current = inCurrent;
		}

		@Override
		public final boolean hasNext()
		{
			return this.current != null;
		}

		@Override
		public final HalfByteArray next()
		{
			final HalfByteArray result = this.current.getKey();
			this.current = this.current.getNext();
			return result;
		}
	}
	
	static class ValuesItsy<U> implements Iterator<U>
	{
		private LinkedCargoNode<U> current;
		
		public ValuesItsy(LinkedCargoNode<U> inCurrent)
		{
			this.current = inCurrent;
		}
		
		@Override
		public final boolean hasNext()
		{
			return this.current != null;
		}

		@Override
		public final U next()
		{
			final U result = this.current.getCargo();
			this.current = this.current.getNext();
			return result;
		}
	}
}
