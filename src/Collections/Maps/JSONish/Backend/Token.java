package Collections.Maps.JSONish.Backend;

import Collections.Lists.CharList.CharList;

public class Token
{
	private final CharList value;
	private final TokenTypes type;
	private final boolean wasString;

	public Token(CharList inValue, TokenTypes inType, boolean inWasString)
	{
		this.value = inValue;
		this.type = inType;
		this.wasString = inWasString;
	}

	public CharList getValue()
	{
		return this.value;
	}

	public TokenTypes getType()
	{
		return this.type;
	}

	public boolean wasString()
	{
		return this.wasString;
	}

	@Override
	public String toString()
	{
		return this.toCharList().toString();
	}

	public CharList toCharList()
	{
		final CharList result = new CharList();

		if (this.value != null)
		{
			result.add(this.value, false);
		}

		if (this.wasString)
		{
			result.push('"');
			result.add('"');
		}

		result.push('\t');
		result.push(this.type.toString());

		return result;
	}

	public static enum TokenTypes
	{
		Package,
		Object,
		Array,
		Name,
		Var,
		ClosingBrace,
		Comma;
	}
}
