package Collections.Maps.JSONish.Backend;

import Collections.Lists.CharList.CharList;

public interface LotusFormat extends LotusSerializable
{
	public String getNameAsString();
	public CharList getNameAsCharList();
	public LotusDataTypes getLotusDataType();
	public CharList toCharList(int tabOffset, boolean labelDataTypes);
	public CharList toCharList(int tabOffset, boolean labelDataTypes, boolean showName);
	public CharList toCharList(int tabOffset, boolean labelDataTypes, boolean showName, boolean showSubNames);
}
