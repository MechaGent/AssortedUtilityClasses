package Collections.PrimitiveInterfaceAnalogues.Char;

public interface AbstractCharListIterator extends AbstractCharIterator
{
	/**
	 * optional
	 * @param in
	 */
	public void add(char in);
	public boolean hasPrevious();
	
	/**
	 * 
	 * @return the index of the element that would be returned by a subsequent call to {@link #next()}.
	 */
	public long nextIndex();
	public char previous();
	
	/**
	 * 
	 * @return the index of the element that would be returned by a subsequent call to {@link #previous()}.
	 */
	public long previousIndex();
	
	/**
	 * optional
	 */
	public void remove();
	
	/**
	 * replaces the last element returned by next() or previous() with the specified element. Optional operation.
	 * @param in
	 */
	public void set(char in);
}
