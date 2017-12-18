package Collections.Maps.JSONish.Backend;

import Collections.Lists.CharList.CharList;
import Collections.Lists.CharList.CharListable;

public interface LotusSerializable extends CharListable
{
	public LotusFormat toLotusFormat();
	public CharList toRawLotusFormat();
	public CharList toRawJsonFormat();
}
