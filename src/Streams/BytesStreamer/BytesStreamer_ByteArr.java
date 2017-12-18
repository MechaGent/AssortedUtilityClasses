package Streams.BytesStreamer;

import HandyStuff.HandyEnums.EndianSettings;
import HandyStuff.HandyEnums.Primitives;

class BytesStreamer_ByteArr extends BytesStreamer_indexed
{
	private final byte[] cargo;

	BytesStreamer_ByteArr(byte[] inCargo)
	{
		super();
		this.cargo = inCargo;
	}

	BytesStreamer_ByteArr(EndianSettings inInputSigEnd, byte[] inCargo)
	{
		super(inInputSigEnd);
		this.cargo = inCargo;
	}

	BytesStreamer_ByteArr(EndianSettings inInputSigEnd, EndianSettings inOutputSigEnd, byte[] inCargo)
	{
		super(inInputSigEnd, inOutputSigEnd);
		this.cargo = inCargo;
	}

	@Override
	public final boolean canSafelyConsume_internal(int inQuantity, Primitives inType)
	{
		return ((inQuantity * inType.getSizeInBytes())-1) + this.index < this.cargo.length;
	}

	@Override
	protected final byte getNextByte_internal()
	{
		return this.cargo[this.index++];
	}
}
