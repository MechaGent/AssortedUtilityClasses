package HandyStuff.BinaryBuffers;

public class LongBuffer
{
	private long[] buffer;
	private int bufferIndex;
	
	private LongBuffer()
	{
		this.buffer = new long[2];
		this.bufferIndex = 0;
	}
	
	public final void swap()
	{
		this.bufferIndex ^= 1;
	}
	
	public final long getCurrent()
	{
		return this.buffer[this.bufferIndex];
	}
	
	public final long getOld()
	{
		return this.buffer[this.bufferIndex^1];
	}
}
