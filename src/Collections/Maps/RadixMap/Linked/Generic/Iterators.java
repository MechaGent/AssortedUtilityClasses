package Collections.Maps.RadixMap.Linked.Generic;

import java.util.Iterator;
import java.util.Map.Entry;

import Collections.PackedArrays.HalfByte.HalfByteArray;

public class Iterators
{
	public static class EntriesItsy<U> implements Iterator<Entry<HalfByteArray, U>>
	{
		private LinkedLeafNode<U> current;
		
		public EntriesItsy(LinkedLeafNode<U> first)
		{
			this.current = first;
		}
		
		@Override
		public boolean hasNext()
		{
			return this.current != null;
		}

		@Override
		public LinkedLeafNode<U> next()
		{
			final LinkedLeafNode<U> result = this.current;
			this.current = this.current.getNext();
			return result;
		}
	}
	
	static class StringKeyedEntryItsy<U> implements Iterator<Entry<String, U>>
	{
		private LinkedLeafNode<U> current;
		
		public StringKeyedEntryItsy(LinkedLeafNode<U> inCurrent)
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
			private final LinkedLeafNode<U> node;
			
			public StringKeyedEntry(LinkedLeafNode<U> inNode)
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
		private LinkedLeafNode<?> current;
		
		public KeysItsy(LinkedLeafNode<?> inCurrent)
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
	
	public static class ValuesItsy<U> implements Iterator<U>
	{
		private LinkedLeafNode<U> current;
		
		ValuesItsy(LinkedLeafNode<U> inCurrent)
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
			
			/*
			if(this.current.getNext() != null && this.current.backtraceKey().equals(this.current.getNext().backtraceKey()))
			{
				throw new IllegalArgumentException(this.current.toString() + " | " + this.current.getNext().toString());
			}
			*/
			
			this.current = this.current.getNext();
			return result;
		}
	}
}
