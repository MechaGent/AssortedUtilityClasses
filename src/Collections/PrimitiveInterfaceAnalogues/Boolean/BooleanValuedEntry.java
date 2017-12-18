package Collections.PrimitiveInterfaceAnalogues.Boolean;

public interface BooleanValuedEntry<U>
{
	public U getKey();
	public boolean getValue();
	public boolean getValue_AsBoolean();
	public boolean setValue(boolean in);
}
