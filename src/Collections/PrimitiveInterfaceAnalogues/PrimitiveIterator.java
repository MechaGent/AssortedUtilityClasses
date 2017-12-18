package Collections.PrimitiveInterfaceAnalogues;

public interface PrimitiveIterator
{
	public boolean hasNext();
	
	public boolean next_asBool();
	public byte next_asByte();
	public short next_asShort();
	public int next_asInt();
	public long next_asLong();
	public float next_asFloat();
	public double next_asDouble();
}
