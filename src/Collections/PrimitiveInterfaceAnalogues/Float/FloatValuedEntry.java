package Collections.PrimitiveInterfaceAnalogues.Float;

public interface FloatValuedEntry<U>
{
	public U getKey();
	public float getValue();
	public float getValue_AsFloat();
	public float setValue(float in);
}
