package Streams.BytesSaver;

import java.io.BufferedOutputStream;
import java.io.IOException;

import Collections.Lists.CharList.Mk01.NumNodeConversion;
import CustomExceptions.UnhandledEnumException;
import HandyStuff.BitTwiddler;

class BytesSaver_BufferedOutputStream extends BytesSaver
{
	private final BufferedOutputStream core;

	public BytesSaver_BufferedOutputStream(BufferedOutputStream inCore)
	{
		super();
		this.core = inCore;
	}

	@Override
	public final void appendByte(int inIn)
	{
		try
		{
			this.core.write(inIn);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public final void appendChar(char in)
	{
		try
		{
			this.core.write((char) in);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public final void appendInteger(int in)
	{
		this.appendBytes(BitTwiddler.splitIntIntoBytes(in));
	}

	@Override
	public final void appendBytes(int packedStack, int inNumBytes)
	{
		switch (inNumBytes)
		{
			case 1:
			{
				this.appendByte(packedStack & 0xff);
				break;
			}
			case 2:
			{
				this.appendByte((packedStack >>> 8) & 0xff);
				this.appendByte(packedStack & 0xff);
				break;
			}
			case 3:
			{
				this.appendByte((packedStack >>> 16) & 0xff);
				this.appendByte((packedStack >>> 8) & 0xff);
				this.appendByte(packedStack & 0xff);
				break;
			}
			case 4:
			{
				this.appendByte((packedStack >>> 24) & 0xff);
				this.appendByte((packedStack >>> 16) & 0xff);
				this.appendByte((packedStack >>> 8) & 0xff);
				this.appendByte(packedStack & 0xff);
				break;
			}
			default:
			{
				throw new UnhandledEnumException(NumNodeConversion.toDecString(inNumBytes));
			}
		}
	}

	public final void appendBytes(byte[] in)
	{
		try
		{
			this.core.write(in);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public final void appendBytes(int[] in)
	{
		for(int i = 0; i < in.length; i++)
		{
			this.appendByte(in[i]);
		}
	}
	
	@Override
	public final void appendLongs(long[] in)
	{
		for(int i = 0; i < in.length; i++)
		{
			this.appendBytes(BitTwiddler.splitLongIntoBytes(in[i]));
		}
	}

	@Override
	public final void flush()
	{
		try
		{
			this.core.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public final void close()
	{
		try
		{
			this.core.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public final void flushAndClose()
	{
		try
		{
			this.core.flush();
			this.core.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public final void appendLong(long in)
	{
		this.appendBytes(BitTwiddler.splitLongIntoBytes(in));
	}
}
