package Collections.Maps.JSONish.Backend;

import Collections.Lists.CharList.CharList;

public abstract class LotusVarPrime_Mk01 implements LotusVar
{
	protected final String name;

	public LotusVarPrime_Mk01(String inName)
	{
		this.name = inName;
	}

	public static LotusVarPrime_Mk01 getInstance(String name, String in, boolean wasString)
	{
		return new LotusVarPrime_Mk01_StringSpine(name, in, wasString);
	}

	public static LotusVarPrime_Mk01 getInstance(String name, int in)
	{
		return new LotusVarPrime_Mk01_IntSpine(name, in);
	}

	public static LotusVarPrime_Mk01 getInstance(String name, double in)
	{
		return new LotusVarPrime_Mk01_DoubleSpine(name, in);
	}

	@Override
	public LotusDataTypes getLotusDataType()
	{
		return LotusDataTypes.Var;
	}

	@Override
	public LotusFormat toLotusFormat()
	{
		return this;
	}

	protected CharList toCharList_Internal(String in, int inTabOffset, boolean inLabelDataTypes)
	{
		return this.toCharList_Internal(new CharList(in), inTabOffset, inLabelDataTypes);
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

	/**
	 * 
	 * @param ins
	 *            is assumed to be consumable
	 * @param inTabOffset
	 * @param inLabelDataTypes
	 * @return
	 */
	protected CharList toCharList_Internal(CharList result, int inTabOffset, boolean inLabelDataTypes)
	{
		if (inLabelDataTypes)
		{
			result.add(')');
			result.push('(');

			result.push(" = ");
			result.push(this.name);
			result.push(' ');

			result.push('>');
			result.push(this.getLotusDataType().toString());
			result.push('<');
		}
		else
		{
			result.push('=');
			result.push(this.name);
		}

		result.push('\t', inTabOffset);
		return result;
	}

	@Override
	public abstract String toString();

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
		return this.toCharList_Internal(inTabOffset, inLabelDataTypes, showName, (char) 0);
	}
	
	@Override
	public CharList toCharList(int inTabOffset, boolean inLabelDataTypes, boolean showName, boolean showSubNames)
	{
		return this.toCharList(inTabOffset, inLabelDataTypes, showName && showSubNames);
	}

	protected CharList toCharList_Internal(int inTabOffset, boolean inLabelDataTypes, boolean showName, char wrapper)
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
		else if(showName)
		{
			result.add('=');
		}

		final CharList val = this.getValueAsCharList();

		if (wrapper != 0)
		{
			//val.bookend(wrapper);
			val.push(wrapper);
			val.add(wrapper);
		}

		result.add(val, true);

		return result;
	}

	@Override
	public CharList toRawLotusFormat()
	{
		final CharList result = new CharList();

		result.add(this.name);
		result.add('=');
		result.add(this.getValueAsString());

		return result;
	}

	private static class LotusVarPrime_Mk01_StringSpine extends LotusVarPrime_Mk01
	{
		private final String core;
		private final boolean wasString;

		public LotusVarPrime_Mk01_StringSpine(String inName, String inCore, boolean inWasString)
		{
			super(inName);
			this.core = inCore;
			this.wasString = inWasString;
		}

		@Override
		public String toString()
		{
			return this.core;
		}
		
		@Override
		public CharList toCharList(int inTabOffset, boolean inLabelDataTypes, boolean showName)
		{
			if(this.wasString)
			{
				return this.toCharList_Internal(inTabOffset, inLabelDataTypes, showName, '"');
			}
			else
			{
				return super.toCharList(inTabOffset, inLabelDataTypes, showName);
			}
		}

		@Override
		public String getValueAsString()
		{
			return this.core;
		}

		@Override
		public String getValueAsStringInQuotes()
		{
			return '"' + this.core + '"';
		}

		@Override
		public CharList getValueAsCharList()
		{
			return new CharList(this.core);
		}

		@Override
		public CharList getValueAsCharListInQuotes()
		{
			final CharList result = new CharList(this.core);
			//result.bookend('"');
			result.push('"');
			result.add('"');
			
			return result;
		}

		@Override
		public boolean getValueAsBoolean()
		{
			final boolean result;

			switch (this.core.toLowerCase())
			{
				case "true":
				case "1":
				{
					result = true;
					break;
				}
				case "false":
				case "0":
				{
					result = false;
					break;
				}
				default:
				{
					throw new IllegalArgumentException("Not a boolean: " + this.core);
				}
			}

			return result;
		}

		@Override
		public int getValueAsInteger()
		{
			return Integer.parseInt(this.core);
		}

		@Override
		public double getValueAsDouble()
		{
			return Double.parseDouble(this.core);
		}

		/**
		 * @return true if value's length == 0, false otherwise
		 */
		@Override
		public boolean isNull()
		{
			return this.core.length() == 0;
		}

		@Override
		public CharList toRawJsonFormat()
		{
			final CharList result = new CharList();

			result.add('"');
			result.add(this.name);
			result.add("\":\"");
			result.add(this.core);
			result.add('"');

			return result;
		}
	}

	private static class LotusVarPrime_Mk01_IntSpine extends LotusVarPrime_Mk01
	{
		private final int core;

		public LotusVarPrime_Mk01_IntSpine(String inName, int inCore)
		{
			super(inName);
			this.core = inCore;
		}

		@Override
		public String getValueAsString()
		{
			return Integer.toString(this.core);
		}

		@Override
		public String getValueAsStringInQuotes()
		{
			return '"' + Integer.toString(this.core) + '"';
		}

		@Override
		public CharList getValueAsCharList()
		{
			return new CharList(this.getValueAsString());
		}

		@Override
		public CharList getValueAsCharListInQuotes()
		{
			return new CharList(this.getValueAsStringInQuotes());
		}

		@Override
		public boolean getValueAsBoolean()
		{
			final boolean result;

			switch (this.core)
			{
				case 0:
				{
					result = false;
					break;
				}
				case 1:
				{
					result = true;
					break;
				}
				default:
				{
					throw new IllegalArgumentException("Not a boolean: " + this.core);
				}
			}

			return result;
		}

		@Override
		public int getValueAsInteger()
		{
			return this.core;
		}

		@Override
		public double getValueAsDouble()
		{
			return (double) this.core;
		}

		/**
		 * @return true if value == 0, false otherwise
		 */
		@Override
		public boolean isNull()
		{
			return this.core == 0;
		}

		@Override
		public String toString()
		{
			return this.toCharList().toString();
		}

		@Override
		public CharList toRawJsonFormat()
		{
			final CharList result = new CharList();

			result.add('"');
			result.add(this.name);
			result.add("\":");
			result.add_asDecString(this.core);

			return result;
		}
	}

	private static class LotusVarPrime_Mk01_DoubleSpine extends LotusVarPrime_Mk01
	{
		private final double core;

		public LotusVarPrime_Mk01_DoubleSpine(String inName, double inCore)
		{
			super(inName);
			this.core = inCore;
		}

		@Override
		public String getValueAsString()
		{
			return Double.toString(this.core);
		}

		@Override
		public String getValueAsStringInQuotes()
		{
			return '"' + Double.toString(this.core) + '"';
		}

		@Override
		public CharList getValueAsCharList()
		{
			return new CharList(Double.toString(this.core));
		}

		@Override
		public CharList getValueAsCharListInQuotes()
		{
			final CharList result = new CharList(Double.toString(this.core));

			//result.bookend('"');
			result.push('"');
			result.add('"');

			return result;
		}

		@Override
		public boolean getValueAsBoolean()
		{
			if (this.core == 0.0)
			{
				return false;
			}
			else if (this.core == 1.0)
			{
				return true;
			}
			else
			{
				throw new IllegalArgumentException("Not a boolean: " + this.core);
			}
		}

		@Override
		public int getValueAsInteger()
		{
			return (int) this.core;
		}

		@Override
		public double getValueAsDouble()
		{
			return this.core;
		}

		/**
		 * @return true if value == 0.0, false otherwise
		 */
		@Override
		public boolean isNull()
		{
			return this.core == 0.0;
		}

		@Override
		public String toString()
		{
			return Double.toString(this.core);
		}

		@Override
		public CharList toRawJsonFormat()
		{
			final CharList result = new CharList();

			result.add('"');
			result.add(this.name);
			result.add("\":");
			result.add_asDecString(this.core);

			return result;
		}
	}
}
