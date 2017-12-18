package Collections.Maps.RadixMap.Unlinked.Generic.Mk01;

import Collections.Lists.CharList.CharList;
import Collections.PackedArrays.HalfByte.HalfByteArray;

class RootNode<U> extends Node<U> implements CargoNode<U>
{
	private U cargo;
	private boolean hasCargo;
	
	public RootNode()
	{
		super();
		this.cargo = null;
		this.hasCargo = false;
	}
	
	@Override
	public final boolean isRoot()
	{
		return true;
	}

	@Override
	public final boolean canHaveCargo()
	{
		return true;
	}
	
	public final boolean cargoIsSet()
	{
		return this.hasCargo;
	}
	
	@Override
	public final U getCargo()
	{
		return this.cargo;
	}
	
	@Override
	public final void setCargo(U in)
	{
		this.cargo = in;
		
		this.hasCargo = true;
	}

	@Override
	public final HalfByteArray getKeyChunk()
	{
		return HalfByteArray.getEmptyInstance();
	}

	@Override
	public final void setKeyChunk(HalfByteArray inNewKeyChunk)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public final Node<U> getParent()
	{
		return null;
	}

	@Override
	public final void setParent(Node<U> inParent)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public CharList toCharList(int offset)
	{
		final CharList result = new CharList();
		
		result.add('\t', offset);
		result.add("<root>(");
		
		if(this.hasCargo)
		{
			if(this.cargo == null)
			{
				result.add("<null>");
			}
			else
			{
				result.add(this.cargo.toString());
			}
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
