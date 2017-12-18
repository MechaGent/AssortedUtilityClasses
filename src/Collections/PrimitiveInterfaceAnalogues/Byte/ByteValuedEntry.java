package Collections.PrimitiveInterfaceAnalogues.Byte;

public interface ByteValuedEntry<U>
{
	public U getKey();
	public byte getValue();
	public byte getValue_AsByte();
	public byte setValue(byte in);
}
