package Streams.BytesStreamer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

import CustomExceptions.UnhandledEnumException;
import HandyStuff.FileParser;
import HandyStuff.HandyEnums.EndianSettings;

public class BytesStreamerFactory
{
	public static final BytesStreamer getInstance(byte[] inCargo)
	{
		return new BytesStreamer_ByteArr(inCargo);
	}

	public static final BytesStreamer getInstance(EndianSettings inInputSigEnd, byte[] inCargo)
	{
		return new BytesStreamer_ByteArr(inInputSigEnd, inCargo);
	}

	public static final BytesStreamer getInstance(EndianSettings inInputSigEnd, EndianSettings inOutputSigEnd, byte[] inCargo)
	{
		return new BytesStreamer_ByteArr(inInputSigEnd, inOutputSigEnd, inCargo);
	}

	public static final BytesStreamer getInstance(String inCargo, StringInterpretations interpretation)
	{
		switch (interpretation)
		{
			case FilePath:
			{
				final byte[] raw = FileParser.parseFileAsByteArray(inCargo);
				return new BytesStreamer_ByteArr(raw);
				//return new BytesStreamer_MemoryMappedBuffer(parseFileAsMappedByteBuffer(inCargo));
			}
			case Literal:
			{
				return new BytesStreamer_String(inCargo);
			}
			default:
			{
				throw new UnhandledEnumException(interpretation);
			}
		}
	}
	
	public static final BytesStreamer getInstance(EndianSettings inInputSigEnd, String inCargo, StringInterpretations interpretation)
	{
		switch (interpretation)
		{
			case FilePath:
			{
				return new BytesStreamer_MemoryMappedBuffer(inInputSigEnd, parseFileAsMappedByteBuffer(inCargo));
			}
			case Literal:
			{
				return new BytesStreamer_String(inInputSigEnd, inCargo);
			}
			default:
			{
				throw new UnhandledEnumException(interpretation);
			}
		}
	}
	
	public static final BytesStreamer getInstance(EndianSettings inInputSigEnd, EndianSettings inOutputSigEnd, String inCargo, StringInterpretations interpretation)
	{
		switch (interpretation)
		{
			case FilePath:
			{
				return new BytesStreamer_MemoryMappedBuffer(inInputSigEnd, inOutputSigEnd, parseFileAsMappedByteBuffer(inCargo));
			}
			case Literal:
			{
				return new BytesStreamer_String(inInputSigEnd, inOutputSigEnd, inCargo);
			}
			default:
			{
				throw new UnhandledEnumException(interpretation);
			}
		}
	}
	
	public static final BytesStreamer getInstance(Path inCargo)
	{
		return new BytesStreamer_MemoryMappedBuffer(parseFileAsMappedByteBuffer(inCargo));
	}
	
	public static final BytesStreamer getInstance(EndianSettings inInputSigEnd, Path inCargo)
	{
		return new BytesStreamer_MemoryMappedBuffer(inInputSigEnd, parseFileAsMappedByteBuffer(inCargo));
	}
	
	public static final BytesStreamer getInstance(EndianSettings inInputSigEnd, EndianSettings inOutputSigEnd, Path inCargo)
	{
		return new BytesStreamer_MemoryMappedBuffer(inInputSigEnd, inOutputSigEnd, parseFileAsMappedByteBuffer(inCargo));
	}
	
	private static final MappedByteBuffer parseFileAsMappedByteBuffer(String FileDirName)
	{
		final FileInputStream inputStream = helperMethod_MappedByteBuffer_getFileInputStream(FileDirName);
		final FileChannel channel = inputStream.getChannel();
		return helperMethod_MappedByteBuffer_getMappedByteBuffer(channel);
	}
	
	private static MappedByteBuffer parseFileAsMappedByteBuffer(Path inFullPath)
	{
		return helperMethod_MappedByteBuffer_getMappedByteBuffer(helperMethod_MappedByteBuffer_getFileInputStream(inFullPath).getChannel());
	}
	
	private static FileInputStream helperMethod_MappedByteBuffer_getFileInputStream(String dir)
	{
		try
		{
			return new FileInputStream(dir);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}
	
	private static FileInputStream helperMethod_MappedByteBuffer_getFileInputStream(Path fullPath)
	{
		try
		{
			return new FileInputStream(fullPath.toFile());
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}
	
	private static MappedByteBuffer helperMethod_MappedByteBuffer_getMappedByteBuffer(FileChannel channel)
	{
		try
		{
			return channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static enum StringInterpretations
	{
		Literal,
		FilePath;
	}
}
