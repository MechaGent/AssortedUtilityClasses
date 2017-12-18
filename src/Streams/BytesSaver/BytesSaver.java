package Streams.BytesSaver;

import Collections.Lists.CharList.CharList;
import HandyStuff.HandyEnums.EndianSettings;

public abstract class BytesSaver
{
	private static final char[] Newline = "\r\n".toCharArray();

	/**
	 * this is used as the default, but can be re-specified locally via method argument
	 */
	protected final EndianSettings inputSigEnd;

	/**
	 * this is always going to be the output format.
	 */
	protected final EndianSettings outputSigEnd;

	public BytesSaver()
	{
		this(EndianSettings.LeftBitLeftByte);
	}

	BytesSaver(EndianSettings inInputSigEnd)
	{
		this(inInputSigEnd, EndianSettings.LeftBitLeftByte);
	}

	BytesSaver(EndianSettings inInputSigEnd, EndianSettings inOutputSigEnd)
	{
		this.inputSigEnd = inInputSigEnd;
		this.outputSigEnd = inOutputSigEnd;
	}

	public abstract void appendByte(int in);

	public abstract void appendBytes(byte[] in);

	public abstract void appendBytes(int[] in);

	public abstract void appendBytes(int in, int numBytes);

	public abstract void appendChar(char in);

	public abstract void appendInteger(int in);
	public abstract void appendLong(long in);
	public abstract void appendLongs(long[] in);

	public abstract void flush();

	public abstract void close();

	public abstract void flushAndClose();

	/**
	 * assumes utf-8
	 * 
	 * @param in
	 */
	public final void appendAsCharCodepoint(int in)
	{
		if ((in & 0xffffff00) == 0)
		{
			// one byte
			this.appendBytes(in, 1);
		}
		else if ((in & 0xffff0000) == 0)
		{
			// two byte
			this.appendBytes(in, 2);
		}
		else if ((in & 0xff000000) == 0)
		{
			// three byte
			this.appendBytes(in, 3);
		}
		else
		{
			// four byte
			this.appendBytes(in, 4);
		}
	}

	public final void append(char[] in)
	{
		for (int i = 0; i < in.length; i++)
		{
			this.appendAsCharCodepoint(in[i]);
		}
	}

	public final void append(char[] in, boolean isInUtf8)
	{
		if (isInUtf8)
		{
			for (int i = 0; i < in.length; i++)
			{
				this.appendByte(in[i] & 0xff);
			}
		}
		else
		{
			this.append(in);
		}
	}

	public final void append(String in)
	{
		for (int i = 0; i < in.length(); i++)
		{
			this.appendAsCharCodepoint(in.charAt(i));
		}
	}

	public final void append(CharList in)
	{
		this.append(in.toCharArray());
	}

	public final void append(CharList in, boolean isInUtf8)
	{
		if(isInUtf8)
		{
			this.append(in.toCharArray(), true);
		}
		else
		{
			this.append(in.toCharArray());
		}
	}

	public final void appendNewLine()
	{
		this.append(Newline);
	}
}
