package Collections.Maps.RadixMap.Unlinked.Generic.Mk01;

import Collections.Lists.CharList.CharList;
import Collections.PackedArrays.HalfByte.HalfByteArray;

class LeafNode<U> extends BranchNode<U> implements CargoNode<U>
{
	private U cargo;

	public LeafNode(Node<U> inParent, HalfByteArray inKeyChunk, U inCargo)
	{
		super(inParent, inKeyChunk);
		this.cargo = inCargo;
	}

	/**
	 * copy constructor
	 * 
	 * @param old
	 */
	public LeafNode(Node<U> old, U inCargo)
	{
		super(old);

		this.cargo = inCargo;
	}

	public LeafNode(RootNode<U> old, U inCargo)
	{
		super(old);
		throw new UnsupportedOperationException();
	}

	@Override
	public final boolean canHaveCargo()
	{
		return true;
	}

	@Override
	public final U getCargo()
	{
		return this.cargo;
	}

	@Override
	public final void setCargo(U inCargo)
	{
		this.cargo = inCargo;
	}

	@Override
	public CharList toCharList(int offset)
	{
		final CharList result = new CharList();

		result.add(this.getKeyChunk().toCharList(offset), true);
		result.add('(');

		if (this.cargo == null)
		{
			result.add("<null>");
		}
		else
		{
			result.add(this.cargo.toString());
		}

		result.add("):{");

		final CharList kidsCharList = this.kidsToCharList(offset + 1);

		if (kidsCharList.size() > 0)
		{
			result.addNewLine();
			result.add(kidsCharList, true);
			result.addNewLine();
			result.add('\t', offset);
		}

		result.add('}');

		return result;
	}
}
