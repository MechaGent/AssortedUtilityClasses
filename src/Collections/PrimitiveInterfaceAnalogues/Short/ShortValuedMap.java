package Collections.PrimitiveInterfaceAnalogues.Short;

public interface ShortValuedMap<U>
{
	public boolean containsKey(U key);
	public short get(U key);
	public boolean cachedValueIsNotEmpty();
	public short getCachedValue();
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @return true if is replacing value, which can then be checked via {@code getCachedValue()}
	 */
	public boolean put(U key, short value);
}
