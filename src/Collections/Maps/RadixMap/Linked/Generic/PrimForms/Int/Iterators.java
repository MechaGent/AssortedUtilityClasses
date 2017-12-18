package Collections.Maps.RadixMap.Linked.Generic.PrimForms.Int;

import java.util.Iterator;

public class Iterators
{
	public static class EntryIterator implements Iterator<KeyedInt>
	{
		private LinkedIntLeafNode current;
		
		public EntryIterator(LinkedRadixIntMap in)
		{
			this.current = in.getFirstEntry();
		}
		
		@Override
		public boolean hasNext()
		{
			return this.current.hasNext();
		}

		@Override
		public KeyedInt next()
		{
			final KeyedInt result = new KeyedInt(this.current.backtraceKey(), this.current.getValue());
			this.current = this.current.getNext();
			return result;
		}
	}
}
