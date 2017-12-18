package Collections.Maps.JSONish.Backend;

import java.util.Iterator;

import Collections.Lists.CharList.CharList;

public abstract class LotusArrayPrime_Mk01 implements LotusArray
{
	private final String name;

	public LotusArrayPrime_Mk01(String inName)
	{
		this.name = inName;
	}

	/*
	public static LotusArrayPrime_Mk01 getSanitizedInstance(String name, Iterator<LotusFormat> in, int length, boolean makeBespoke)
	{
		final LotusArrayPrime_Mk01 result;

		if (makeBespoke && length > 0)
		{
			final LotusFormat[] temp = new LotusFormat[length];
			int i = 0;
			temp[i] = in.next();
			final LotusDataTypes tentativeType = temp[i].getLotusDataType();
			boolean isClean = true;
			i++;

			while (in.hasNext() && isClean)
			{
				final LotusFormat cur = in.next();
				isClean = cur.getLotusDataType() == tentativeType;
			}

			if (isClean)
			{
				switch (tentativeType)
				{
					case Var:
					{
						final LotusVar[] core = new LotusVar[length];

						for (int j = 0; j < length; j++)
						{
							core[j] = (LotusVar) temp[j];
						}

						result = new LotusArrayPrime_Mk01_OfVars(name, core);
						break;
					}
					case Array:
						break;
					case Object:
						break;
					case Package:
						break;
					default:
						break;
				}
			}
			else
			{
				result = new LotusArrayPrime_Mk01_MixedBag(name, temp);
			}
		}
		else
		{
			result = new LotusArrayPrime_Mk01_MixedBag(name, in, length);
		}

		return result;
	}
	*/

	public static LotusArrayPrime_Mk01 getPureInstance(String name, Iterator<LotusVar> in, int length)
	{
		return new LotusArrayPrime_Mk01_OfVars(name, in, length);
	}
	
	public static LotusArrayPrime_Mk01 getGeneralInstance(String name, Iterator<LotusFormat> in, int length)
	{
		final LotusFormat[] result = new LotusFormat[length];
		
		for(int i = 0; i < length; i++)
		{
			result[i] = in.next();
		}
		
		return new LotusArrayPrime_Mk01_MixedBag(name, result);
	}
	
	public static LotusArrayPrime_Mk01 getGeneralInstance(String name, LotusFormat[] in)
	{
		return new LotusArrayPrime_Mk01_MixedBag(name, in);
	}

	@Override
	public LotusDataTypes getLotusDataType()
	{
		return LotusDataTypes.Array;
	}

	@Override
	public CharList toCharList()
	{
		return this.toCharList(0);
	}

	@Override
	public CharList toCharList(int tabOffset)
	{
		return this.toCharList(tabOffset, false);
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
		
		if(showName)
		{
			result.add(this.name);
		}

		if (inLabelDataTypes)
		{
			result.push(' ');
			result.push('>');
			result.push(this.getLotusDataType().toString());
			result.push('<');
			
			if(showName)
			{
				result.add(" = ");
			}
		}
		else if(showName && this.name.length() > 0)
		{
			result.add('=');
		}

		result.add('[');
		//int newLines = 0;

		for (int i = 0; i < this.length(); i++)
		{
			if(i != 0)
			{
				result.add(',');
			}
			
			final LotusFormat elem = this.elementAt(i);
			
			result.addNewLine();

			if(elem instanceof LotusVar)
			{
				result.add(this.elementAt(i).toCharList(inTabOffset + 1, inLabelDataTypes, false), true);
			}
			else
			{
				//newLines++;
				result.add(this.elementAt(i).toCharList(inTabOffset + 1, inLabelDataTypes, showName && showSubNames, showSubNames), true);
			}
		}
		
		if(this.length() > 0)
		{
			result.addNewLine();
			
			result.add('\t', inTabOffset);
		}
		
		result.add(']');

		return result;
	}
	
	@Override
	public CharList toRawLotusFormat()
	{
		return this.toCharList(0, false);
	}
	
	@Override
	public LotusFormat toLotusFormat()
	{
		return this;
	}
	
	@Override
	public CharList toRawJsonFormat()
	{
		throw new UnsupportedOperationException();
	}

	private static class LotusArrayPrime_Mk01_MixedBag extends LotusArrayPrime_Mk01
	{
		private final LotusFormat[] core;

		@SuppressWarnings("unused")
		public LotusArrayPrime_Mk01_MixedBag(String name, Iterator<LotusFormat> in, int length)
		{
			this(name, crunchArrayFromIterator(in, length));
		}

		public LotusArrayPrime_Mk01_MixedBag(String name, LotusFormat[] inCore)
		{
			super(name);
			this.core = inCore;
		}

		private static LotusFormat[] crunchArrayFromIterator(Iterator<LotusFormat> in, int length)
		{
			final LotusFormat[] result = new LotusFormat[length];

			for (int i = 0; i < length; i++)
			{
				result[i] = in.next();
			}

			return result;
		}

		@Override
		public boolean isPureArray()
		{
			return false;
		}

		@Override
		public LotusDataTypes getElementType()
		{
			return null;
		}

		@Override
		public int length()
		{
			return this.core.length;
		}

		@Override
		public LotusFormat[] extrudeToArray()
		{
			return this.core;
		}

		@Override
		public LotusObject[] extrudeToObjectArray()
		{
			final LotusObject[] result = new LotusObject[this.core.length];

			for (int i = 0; i < result.length; i++)
			{
				result[i] = (LotusObject) this.core[i];
			}

			return result;
		}

		@Override
		public LotusArray[] extrudeToArrayArray()
		{
			final LotusArray[] result = new LotusArray[this.core.length];

			for (int i = 0; i < result.length; i++)
			{
				result[i] = (LotusArray) this.core[i];
			}

			return result;
		}

		@Override
		public LotusVar[] extrudeToVarArray()
		{
			final LotusVar[] result = new LotusVar[this.core.length];

			for (int i = 0; i < result.length; i++)
			{
				result[i] = (LotusVar) this.core[i];
			}

			return result;
		}

		@Override
		public LotusFormat elementAt(int inIndex)
		{
			return this.core[inIndex];
		}

		@Override
		public LotusObject objectAt(int inIndex)
		{
			return (LotusObject) this.core[inIndex];
		}

		@Override
		public LotusArray arrayAt(int inIndex)
		{
			return (LotusArray) this.core[inIndex];
		}

		@Override
		public LotusVar varAt(int inIndex)
		{
			return (LotusVar) this.core[inIndex];
		}
	}

	private static class LotusArrayPrime_Mk01_OfVars extends LotusArrayPrime_Mk01
	{
		private final LotusVar[] core;

		public LotusArrayPrime_Mk01_OfVars(String name, Iterator<LotusVar> in, int length)
		{
			this(name, crunchArrayFromIterator(in, length));
		}

		public LotusArrayPrime_Mk01_OfVars(String name, LotusVar[] inCore)
		{
			super(name);
			this.core = inCore;
		}

		private static LotusVar[] crunchArrayFromIterator(Iterator<LotusVar> in, int length)
		{
			final LotusVar[] result = new LotusVar[length];

			for (int i = 0; i < length; i++)
			{
				result[i] = in.next();
			}

			return result;
		}

		@Override
		public boolean isPureArray()
		{
			return true;
		}

		@Override
		public LotusDataTypes getElementType()
		{
			return LotusDataTypes.Var;
		}

		@Override
		public int length()
		{
			return this.core.length;
		}

		@Override
		public LotusFormat[] extrudeToArray()
		{
			return this.core;
		}

		@Override
		public LotusObject[] extrudeToObjectArray()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public LotusArray[] extrudeToArrayArray()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public LotusVar[] extrudeToVarArray()
		{
			return this.core;
		}

		@Override
		public LotusFormat elementAt(int inIndex)
		{
			return this.core[inIndex];
		}

		@Override
		public LotusObject objectAt(int inIndex)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public LotusArray arrayAt(int inIndex)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public LotusVar varAt(int inIndex)
		{
			return this.core[inIndex];
		}
	}
}
