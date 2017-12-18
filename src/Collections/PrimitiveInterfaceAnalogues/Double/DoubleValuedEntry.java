package Collections.PrimitiveInterfaceAnalogues.Double;

public interface DoubleValuedEntry<U>
{
	public U getKey();
	public double getValue();
	public double getValue_AsDouble();
	public double setValue(double in);
}
