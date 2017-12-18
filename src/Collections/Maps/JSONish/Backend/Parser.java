package Collections.Maps.JSONish.Backend;

import Collections.Lists.Generic.Directed.SingleLinkedList;
import Collections.Maps.JSONish.Backend.Token.TokenTypes;
import Collections.Maps.JSONish.LotusJSON.LotusTokenizer;
import Collections.Maps.RadixMap.Linked.Generic.LinkedRadixMap;
import CustomExceptions.UnhandledEnumException;
import HandyStuff.HandyEnums.EndianSettings;
import Streams.BytesStreamer.BytesStreamer;
import Streams.BytesStreamer.BytesStreamerFactory;
import Streams.BytesStreamer.BytesStreamerFactory.StringInterpretations;

public class Parser
{
	/**
	 * inner workings of parseLotusObject
	 * 
	 * @param tokey
	 * @param itsy
	 * @return
	 */
	private static LinkedRadixMap<LotusFormat> parseMap(Tokenizer tokey, BytesStreamer itsy)
	{
		boolean isDone = !itsy.hasNextByte();
		final LinkedRadixMap<LotusFormat> result = new LinkedRadixMap<LotusFormat>();

		while (!isDone)
		{
			Token testToken = tokey.peekAtNextToken(itsy);

			if (testToken != null)
			{
				if (testToken.getType() == TokenTypes.Comma)
				{
					tokey.parseNextToken(itsy);
					testToken = tokey.peekAtNextToken(itsy);
				}

				switch (testToken.getType())
				{
					case Name:
					{
						final String name = testToken.getValue().toString();
						final LotusFormat nextVar = parseNextVar(tokey, itsy);
						result.put(name, nextVar);
						break;
					}
					case ClosingBrace:
					{
						tokey.parseNextToken(itsy);
						isDone = true;
						break;
					}
					default:
					{
						tokey.parseNextToken(itsy);
						throw new UnhandledEnumException(testToken.toString() + "\r\n" + tokey.peekAtNextToken(itsy).toString());
					}
				}
			}
			else
			{
				isDone = true;
			}
		}

		return result;
	}

	public static LotusObject parseLotusObject(String name, String raw)
	{
		return parseLotusObject(name, raw, new LotusTokenizer());
	}

	public static LotusObject parseLotusObject(String name, String raw, Tokenizer tokey)
	{
		return parseLotusObject(tokey, BytesStreamerFactory.getInstance(EndianSettings.LeftBitLeftByte, raw, StringInterpretations.Literal), name);
	}

	public static LotusObject parseLotusObject(Tokenizer tokey, BytesStreamer itsy, String name)
	{
		return LotusFactory.getInstance(name, parseMap(tokey, itsy));
	}

	public static LotusArray parseLotusArray(Tokenizer tokey, BytesStreamer itsy, String name)
	{
		final SingleLinkedList<LotusFormat> core = new SingleLinkedList<LotusFormat>();
		boolean isDone = false;

		while (!isDone)
		{
			final Token currToken = tokey.peekAtNextToken(itsy);

			switch (currToken.getType())
			{
				case ClosingBrace:
				{
					tokey.parseNextToken(itsy);
					isDone = true;
					break;
				}
				case Comma:
				{
					tokey.parseNextToken(itsy);
					break;
				}
				default:
				{
					core.add(parseNextVar(tokey, itsy, ""));
					break;
				}
			}
		}

		return LotusFactory.getInstance(name, core.iterator(), core.size());
	}

	public static LotusFormat parseNextVar(Tokenizer tokey, BytesStreamer itsy)
	{
		final Token nameToken = tokey.peekAtNextToken(itsy);

		if (nameToken.getType() == TokenTypes.Name)
		{
			tokey.parseNextToken(itsy);
			return parseNextVar(tokey, itsy, nameToken.getValue().toString());
		}
		else
		{
			return parseNextVar(tokey, itsy, "");
		}
	}

	public static LotusFormat parseNextVar(Tokenizer tokey, BytesStreamer itsy, String rawName)
	{
		final LotusFormat result;
		Token currToken = tokey.peekAtNextToken(itsy);

		if (currToken.getType() == TokenTypes.Name)
		{
			tokey.parseNextToken(itsy);
			currToken = tokey.peekAtNextToken(itsy);
		}

		switch (currToken.getType())
		{
			case Array:
			{
				tokey.parseNextToken(itsy);
				result = parseLotusArray(tokey, itsy, rawName);

				break;
			}
			case Object:
			{
				tokey.parseNextToken(itsy);
				result = parseLotusObject(tokey, itsy, rawName);

				break;
			}
			case Var:
			{
				result = parseLotusVar(tokey, itsy, rawName);

				break;
			}
			case Package:
			case ClosingBrace:
			default:
			{
				throw new UnhandledEnumException(currToken.toString());
			}
		}

		return result;
	}

	public static LotusVar parseLotusVar(Tokenizer tokey, BytesStreamer itsy, String name)
	{
		final Token value = tokey.parseNextToken(itsy);
		return LotusFactory.getInstance(name, value.getValue().toString(), value.wasString());
	}
}
