package Streams.BytesStreamer;

import HandyStuff.HandyEnums.EndianSettings;
import HandyStuff.HandyEnums.Primitives;

class BytesStreamer_String extends BytesStreamer_indexed
{
	private final String core;

	BytesStreamer_String(String inCore)
	{
		super();
		this.core = inCore;
	}

	BytesStreamer_String(EndianSettings inInputSigEnd, String inCore)
	{
		super(inInputSigEnd);
		this.core = inCore;
	}

	BytesStreamer_String(EndianSettings inInputSigEnd, EndianSettings inOutputSigEnd, String inCore)
	{
		super(inInputSigEnd, inOutputSigEnd);
		this.core = inCore;
	}

	@Override
	protected final boolean canSafelyConsume_internal(int inQuantity, Primitives inType)
	{
		return this.index + ((inQuantity * inType.getSizeInBytes())-1) < this.core.length();
	}

	@Override
	protected final byte getNextByte_internal()
	{
		return (byte) this.getNextChar(EndianSettings.LeftBitLeftByte);
	}
	
	@Override
	public final char getNextChar(EndianSettings sigEnd)
	{
		//final char result = this.core.charAt(this.index++);
		//System.out.println(result);
		//return result;
		return this.core.charAt(this.index++);
	}
	
	@Override
	public final char[] getNextCharArray(EndianSettings sigEnd, int length)
	{
		final char[] result = new char[length];
		this.core.getChars(this.index, this.index + length, result, 0);
		return result;
	}
}
