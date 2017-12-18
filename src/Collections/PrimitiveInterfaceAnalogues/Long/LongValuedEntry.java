package Collections.PrimitiveInterfaceAnalogues.Long;

public interface LongValuedEntry<U>
{
	public U getKey();
	public long getValue();
	public long getValue_AsLong();
	public long setValue(long in);
}
