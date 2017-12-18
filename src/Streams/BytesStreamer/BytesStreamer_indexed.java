package Streams.BytesStreamer;

import HandyStuff.HandyEnums.EndianSettings;

abstract class BytesStreamer_indexed extends BytesStreamer
{
	protected int index;
	
	BytesStreamer_indexed()
	{
		super();
		this.index = 0;
	}
	
	BytesStreamer_indexed(EndianSettings inInputSigEnd)
	{
		super(inInputSigEnd);
		this.index = 0;
	}
	
	BytesStreamer_indexed(EndianSettings inInputSigEnd, EndianSettings inOutputSigEnd)
	{
		super(inInputSigEnd, inOutputSigEnd);
		this.index = 0;
	}
	
	@Override
	public final long getPosition()
	{
		return this.index;
	}

	@Override
	public final void setToPosition(long inPosition)
	{
		this.index = (int) inPosition;
	}
	
	@Override
	public final void advancePosition(long delta)
	{
		this.index += delta;
	}
}
