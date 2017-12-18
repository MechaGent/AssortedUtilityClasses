package Streams.BytesSaver;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import HandyStuff.FileParser;

public class BytesSaverFactory
{
	private static final int DefaultBufferSize = 8192;
	
	public static final BytesSaver getInstance(String filePath)
	{
		return BytesSaverFactory.getInstance(FileParser.getFileOutputStream(filePath));
	}
	
	public static final BytesSaver getInstance(FileOutputStream in)
	{
		return BytesSaverFactory.getInstance(in, DefaultBufferSize);
	}
	
	public static final BytesSaver getInstance(FileOutputStream in, int bufferSize)
	{
		return BytesSaverFactory.getInstance(new BufferedOutputStream(in, bufferSize));
	}
	
	public static final BytesSaver getInstance(BufferedOutputStream inCore)
	{
		return new BytesSaver_BufferedOutputStream(inCore);
	}
}
