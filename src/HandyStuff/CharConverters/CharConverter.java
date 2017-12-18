package HandyStuff.CharConverters;

import CustomExceptions.UnhandledEnumException;
import HandyStuff.BitTwiddler;
import HandyStuff.FileParser;
import HandyStuff.HandyEnums.EndianSettings;
import Streams.BytesStreamer.BytesStreamer;

public class CharConverter
{
	private static final int[] Windows1252ToUtf8Table = initTable("Win1252ToUtf8");

	private static final int[] initTable(String tableName)
	{
		final String pathStub = "H:/Users/Thrawnboo/workspace - Java/MatureStuff/src/HandyStuff/CharConverters/";
		final String fullPath = pathStub + tableName + ".txt";
		final String[] lines = FileParser.parseFileAsStringLines(fullPath);

		final int[] result = new int[lines.length];

		for (String line : lines)
		{
			//System.out.println(line);
			final String[] split = line.split("\t+");

			if(split.length == 3)
			{
				final int index = Integer.parseInt(split[0].substring(2), 16);
				final int value = Integer.parseInt(split[1].substring(2), 16);
				result[index] = value;
			}
		}

		return result;
	}
	
	/**
	 * used for parsing bytes into UTF-8 codepoints
	 */
	public static final int CharMask_OneByte = 		0b10000000;
	
	/**
	 * used for parsing bytes into UTF-8 codepoints
	 */
	public static final int CharMask_TwoByte = 		0b11000000;
	
	/**
	 * used for parsing bytes into UTF-8 codepoints
	 */
	public static final int CharMask_ThreeByte = 	0b11100000;

	public static final int getNextChar(Conversions conversion, BytesStreamer in)
	{
		final int result;

		switch (conversion)
		{
			case Windows1252ToUtf8:
			{
				result = CharConverter.Windows1252ToUtf8Table[in.getNextByte()];
				break;
			}
			case Windows1252:
			{
				result = in.getNextByte();
				break;
			}
			case Utf8:
			{
				final int firstByte = in.getNextByte();
				
				if((firstByte & CharMask_OneByte) == 0)
				{
					// one byte
					result = firstByte;
				}
				else if((firstByte & CharMask_TwoByte) == CharMask_OneByte)
				{
					// two byte
					//System.out.println("firstByte: " + ((char) firstByte));
					result = BitTwiddler.parseShort(EndianSettings.LeftBitLeftByte, firstByte, in.getNextByte());
				}
				else if((firstByte & CharMask_ThreeByte) == CharMask_TwoByte)
				{
					result = BitTwiddler.parseThreeBytes(EndianSettings.LeftBitLeftByte, firstByte, in.getNextByte(), in.getNextByte());
				}
				else
				{
					result = BitTwiddler.parseInt(EndianSettings.LeftBitLeftByte, firstByte, in.getNextByte(), in.getNextByte(), in.getNextByte());
					//throw new UnhandledEnumException(NumNodeConversion.toBinString(firstByte));
				}
				break;
			}
			case Utf16:
			{
				result = BitTwiddler.parseShort(EndianSettings.LeftBitLeftByte, in.getNextByte(), in.getNextByte());
				break;
			}
			case Default:
			{
				result = in.getNextChar() & 0xff;
				break;
			}
			default:
			{
				throw new UnhandledEnumException(conversion);
			}
		}

		return result;
	}
	
	/**
	 * 
	 * @param conversion
	 * @param CharByte MUST be at most 0xFF
	 * @return
	 */
	public static final int getNextChar(Conversions conversion, int CharByte)
	{
		final int result;

		switch (conversion)
		{
			case Windows1252ToUtf8:
			{
				result = CharConverter.Windows1252ToUtf8Table[CharByte];
				break;
			}
			default:
			{
				throw new UnhandledEnumException(conversion);
			}
		}

		return result;
	}

	public static enum Conversions
	{
		Windows1252ToUtf8,
		Utf8,
		Windows1252,
		Utf16,
		Default;
	}
}
