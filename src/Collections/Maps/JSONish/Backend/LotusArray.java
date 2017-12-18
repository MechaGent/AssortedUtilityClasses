package Collections.Maps.JSONish.Backend;

public interface LotusArray extends LotusFormat
{
	/**
	 * 
	 * @return true if all elements are of the same type, false otherwise
	 */
	public boolean isPureArray();
	public LotusDataTypes getElementType();
	public int length();
	
	public LotusFormat[] extrudeToArray();
	public LotusObject[] extrudeToObjectArray();
	public LotusArray[] extrudeToArrayArray();
	public LotusVar[] extrudeToVarArray();
	
	public LotusFormat elementAt(int index);
	public LotusObject objectAt(int index);
	public LotusArray arrayAt(int index);
	public LotusVar varAt(int index);
}
