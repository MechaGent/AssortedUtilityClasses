package Collections.Maps.JSONish.Backend;

import java.util.Iterator;
import java.util.Map.Entry;

import java.util.Set;

import Collections.Lists.CharList.CharList;
import Collections.Maps.RadixMap.Linked.Generic.LinkedRadixMap;
import Collections.PackedArrays.HalfByte.HalfByteArray;

public class LotusObjectPrime_Mk01 implements LotusObject
{
	private final String name;
	private final LinkedRadixMap<LotusFormat> core;

	public LotusObjectPrime_Mk01(String inName, LinkedRadixMap<LotusFormat> inCore)
	{
		this.name = inName;
		this.core = inCore;
	}

	@Override
	public String getNameAsString()
	{
		return this.name;
	}

	@Override
	public CharList getNameAsCharList()
	{
		return new CharList(this.name);
	}

	@Override
	public LotusDataTypes getLotusDataType()
	{
		return LotusDataTypes.Object;
	}

	@Override
	public LotusFormat toLotusFormat()
	{
		return this;
	}

	@Override
	public CharList toRawLotusFormat()
	{
		return this.toCharList(0, false);
	}

	@Override
	public String toString()
	{
		return this.toCharList().toString();
	}

	@Override
	public CharList toCharList()
	{
		return this.toCharList(0);
	}

	@Override
	public CharList toCharList(int inTabOffset)
	{
		return this.toCharList(inTabOffset, false);
	}

	@Override
	public CharList toCharList(int inTabOffset, boolean inLabelDataTypes)
	{
		return this.toCharList(inTabOffset, inLabelDataTypes, true);
	}

	@Override
	public CharList toCharList(int inTabOffset, boolean inLabelDataTypes, boolean showName)
	{
		return this.toCharList(inTabOffset, inLabelDataTypes, showName, showName);
	}

	@Override
	public CharList toCharList(int inTabOffset, boolean inLabelDataTypes, boolean showName, boolean showSubNames)
	{
		final CharList result = new CharList();
		result.add('\t', inTabOffset);

		if (showName)
		{
			result.add(this.name);
		}

		if (inLabelDataTypes)
		{
			result.push(' ');
			result.push('>');
			result.push(this.getLotusDataType().toString());
			result.push('<');

			if (showName)
			{
				result.add(" = ");
			}
		}
		else if (showName && this.name.length() > 0)
		{
			result.add('=');
		}

		result.add('{');

		for (Entry<HalfByteArray, LotusFormat> entry : this.core)
		{
			result.addNewLine();
			final LotusFormat value = entry.getValue();
			if (value != null)
			{
				final CharList toCharList = value.toCharList(inTabOffset + 1, inLabelDataTypes, showSubNames);
				result.add(toCharList, true);
			}
			else
			{
				//System.out.println("Null value: " + entry.getKey());
				result.add(entry.getKey().interpretAsChars(), true);
				result.add("=<NULL VALUE>");
			}
		}

		if (this.core.size() > 0)
		{
			result.addNewLine();
			result.add('\t', inTabOffset);
		}

		result.add('}');

		return result;
	}

	@Override
	public LotusFormat getVar(String inKey)
	{
		return this.core.get(inKey);
	}

	@Override
	public LotusObject getVarAsObject(String inKey)
	{
		return (LotusObject) this.core.get(inKey);
	}

	@Override
	public LotusArray getVarAsArray(String inKey)
	{
		return (LotusArray) this.core.get(inKey);
	}

	@Override
	public LotusVar getVarAsVar(String inKey)
	{
		return (LotusVar) this.core.get(inKey);
	}

	@Override
	public LotusFormat getVar(CharList inKey)
	{
		return this.core.get(inKey);
	}

	@Override
	public LotusObject getVarAsObject(CharList inKey)
	{
		return (LotusObject) this.core.get(inKey);
	}

	@Override
	public LotusArray getVarAsArray(CharList inKey)
	{
		return (LotusArray) this.core.get(inKey);
	}

	@Override
	public LotusVar getVarAsVar(CharList inKey)
	{
		return (LotusVar) this.core.get(inKey);
	}

	@Override
	public int size()
	{
		return this.core.size();
	}

	@Override
	public Set<Entry<String, LotusFormat>> getEntrySet_StringKeys()
	{
		return this.core.stringKeyedEntrySet();
	}

	@Override
	public Iterator<Entry<String, LotusFormat>> getEntryIterator_StringKeys()
	{
		return this.core.stringKeyedEntriesIterator();
	}

	@Override
	public Set<Entry<CharList, LotusFormat>> getEntrySet_CharListKeys()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Entry<CharList, LotusFormat>> getEntryIterator_CharListKeys()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public CharList toRawJsonFormat()
	{
		throw new UnsupportedOperationException();
	}
}
