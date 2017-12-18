package Collections.Maps.JSONish.Backend;

import Collections.Lists.CharList.CharList;

public interface LotusVar extends LotusFormat
{
	public String getValueAsString();
	public String getValueAsStringInQuotes();
	public CharList getValueAsCharList();
	public CharList getValueAsCharListInQuotes();
	public boolean getValueAsBoolean();
	public int getValueAsInteger();
	public double getValueAsDouble();
	
	public boolean isNull();
}
