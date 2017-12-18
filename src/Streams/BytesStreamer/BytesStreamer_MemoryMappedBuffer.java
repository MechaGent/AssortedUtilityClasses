package Streams.BytesStreamer;

import java.nio.MappedByteBuffer;

import HandyStuff.HandyEnums.EndianSettings;
import HandyStuff.HandyEnums.Primitives;

class BytesStreamer_MemoryMappedBuffer extends BytesStreamer
{
	private final MappedByteBuffer core;

	BytesStreamer_MemoryMappedBuffer(MappedByteBuffer inCore)
	{
		super();
		this.core = inCore;
	}

	BytesStreamer_MemoryMappedBuffer(EndianSettings inInputSigEnd, MappedByteBuffer inCore)
	{
		super(inInputSigEnd);
		this.core = inCore;
	}

	BytesStreamer_MemoryMappedBuffer(EndianSettings inInputSigEnd, EndianSettings inOutputSigEnd, MappedByteBuffer inCore)
	{
		super(inInputSigEnd, inOutputSigEnd);
		this.core = inCore;
	}

	@Override
	protected boolean canSafelyConsume_internal(int inQuantity, Primitives inType)
	{
		return this.core.position() - 1 + (inQuantity * inType.getSizeInBytes()) < this.core.limit();
	}

	@Override
	public final long getPosition()
	{
		return this.core.position();
	}

	@Override
	public final void setToPosition(long inPosition)
	{
		this.core.position((int) inPosition);
	}
	
	@Override
	public final void advancePosition(long delta)
	{
		this.core.position((int) (this.core.position() + delta));
	}

	@Override
	protected final byte getNextByte_internal()
	{
		return this.core.get();
	}
}
