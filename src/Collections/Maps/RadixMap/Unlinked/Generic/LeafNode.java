package Collections.Maps.RadixMap.Unlinked.Generic;

import java.util.Map.Entry;

import Collections.Lists.CharList.CharList;
import Collections.PackedArrays.HalfByte.HalfByteArray;

class LeafNode<U> extends Node<U> implements Entry<HalfByteArray, U>
{
	private U cargo;

	/**
	 * constructor for root nodes
	 */
	LeafNode()
	{
		super(null, HalfByteArray.getEmptyInstance(), CargoStateMasks.CanHaveCargo.value);
		this.cargo = null;
	}

	LeafNode(HalfByteArray inKeyChunk, U inCargo)
	{
		this(null, inKeyChunk, inCargo);
	}

	LeafNode(Node<U> old, U inCargo)
	{
		super(old, old.getNumChildren() & CargoStateMasks.HasCargo.value);
		this.cargo = inCargo;
	}

	LeafNode(Node<U> parent, HalfByteArray inKeyChunk, U inCargo)
	{
		super(parent, inKeyChunk, CargoStateMasks.HasCargo.value);
		this.cargo = inCargo;
	}

	final U getCargo()
	{
		return this.cargo;
	}

	final void setCargo(U inCargo)
	{
		this.cargo = inCargo;
		this.childrenAndState |= this.getNumChildren() & CargoStateMasks.HasCargo.value;
	}

	/**
	 * use only for root node cargo stuff
	 */
	final void clearCargo()
	{
		this.childrenAndState = (this.childrenAndState & 0xff) | CargoStateMasks.CanHaveCargo.value;
		this.cargo = null;
	}

	final void clearAll()
	{
		this.childrenAndState = CargoStateMasks.CanHaveCargo.value;
		this.children = Node.getNewKidsArr();
	}

	final Node<U> transformIntoBranchNode()
	{
		return new Node<U>(this);
	}

	@Override
	final CharList toCharList(int offset, String altName)
	{
		final CharList result = new CharList();

		result.add('\t', offset);
		result.add(altName);

		result.add('[');
		result.add_asDecString(this.getNumChildren());
		result.add(']');

		result.add('(');

		if (!this.hasCargo())
		{
			result.add("<null>");
		}
		else
		{
			result.add(this.cargo.toString());
		}

		result.add("):{");

		this.kidsToCharList(offset + 1, result);

		return result;
	}

	@Override
	public final HalfByteArray getKey()
	{
		return this.backtraceKey();
	}

	@Override
	public final U getValue()
	{
		return this.cargo;
	}

	@Override
	public final U setValue(U inCargo)
	{
		final U result;
		
		if (this.hasCargo())
		{
			result = this.cargo;
		}
		else
		{
			result = null;
		}
		
		this.cargo = inCargo;
		this.childrenAndState |= this.getNumChildren() & CargoStateMasks.HasCargo.value;
		
		return result;
	}
	
	final StringKeyedEntry<U> wrapAsStringKeyedEntry()
	{
		return new StringKeyedEntry<U>(this);
	}

	static class StringKeyedEntry<U> implements Entry<String, U>
	{
		private final LeafNode<U> core;

		private StringKeyedEntry(LeafNode<U> inCore)
		{
			this.core = inCore;
		}

		@Override
		public final String getKey()
		{
			return this.core.getKey().interpretAsCharString();
		}

		@Override
		public final U getValue()
		{
			return this.core.getValue();
		}

		@Override
		public final U setValue(U inValue)
		{
			return this.core.setValue(inValue);
		}
	}
}
