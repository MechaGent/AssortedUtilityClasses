package Collections.Maps.JSONish.JsonJSON;

import Collections.Lists.CharList.CharList;
import Collections.Lists.Generic.Directed.SingleLinkedList;
import Collections.Maps.JSONish.Backend.LotusObject;
import Collections.Maps.JSONish.Backend.Parser;
import Collections.Maps.JSONish.Backend.Token;
import Collections.Maps.JSONish.Backend.Tokenizer;
import Collections.Maps.JSONish.Backend.Token.TokenTypes;
import Streams.BytesStreamer.BytesStreamer;
import Streams.BytesStreamer.BytesStreamerFactory;
import Streams.BytesStreamer.BytesStreamerFactory.StringInterpretations;

public class JsonTokenizer implements Tokenizer
{
	private final SingleLinkedList<Token> tokenBuffer;

	public JsonTokenizer()
	{
		this.tokenBuffer = new SingleLinkedList<Token>();
	}

	@Override
	public Token parseNextToken(BytesStreamer in)
	{
		final Token result = this.parseNextToken_Internal(in, true);

		//System.out.println(result + "");

		return result;
	}

	@Override
	public Token peekAtNextToken(BytesStreamer in)
	{
		if (this.tokenBuffer.isEmpty())
		{
			final Token next = this.parseNextToken(in);
			this.tokenBuffer.push(next);
			return next;
		}
		else
		{
			return this.tokenBuffer.getFirst();
		}
	}

	private Token parseNextToken_Internal(BytesStreamer in, boolean treatEndOfLineAsBreaker)
	{
		Token result = null;

		if (this.tokenBuffer.isEmpty())
		{
			final CharList running = new CharList();
			boolean isDone = false;
			boolean isString = false;

			while (!isDone && in.hasNextByte())
			{
				final char curChar = in.getNextChar();

				switch (curChar)
				{
					case '\\':
					{
						running.add(curChar);
						running.add(in.getNextChar());
						break;
					}
					case ':':
					{
						if (!isString)
						{
							result = new Token(running, TokenTypes.Name, false);
							isDone = true;
						}
						else
						{
							running.add(':');
						}
						break;
					}
					case '\r':
					case '\n':
					{
						if (treatEndOfLineAsBreaker && running.size() > 0)
						{
							result = new Token(running, TokenTypes.Var, true);
							isDone = true;
						}

						break;
					}
					case '\t':
					case ' ':
					{
						if (isString)
						{
							running.add(curChar);
						}

						break;
					}
					case '{':
					{
						if (!isString)
						{
							result = new Token(null, TokenTypes.Object, false);
							isDone = true;
						}
						else
						{
							running.add('{');
						}

						break;
					}
					case '[':
					{
						if (!isString)
						{
							result = new Token(null, TokenTypes.Array, false);
							isDone = true;
						}
						else
						{
							running.add('[');
						}
						break;
					}
					case ',':
					{
						if (!isString)
						{
							if (running.size() > 0)
							{
								result = new Token(running, TokenTypes.Var, true);
								this.tokenBuffer.add(new Token(null, TokenTypes.Comma, false));
							}
							else
							{
								result = new Token(null, TokenTypes.Comma, false);
							}

							isDone = true;
						}
						else
						{
							running.add(',');
						}

						break;
					}
					case '}':
					case ']':
					{
						if (!isString)
						{
							if (running.size() > 0)
							{
								result = new Token(running, TokenTypes.Var, true);
								this.tokenBuffer.add(new Token(null, TokenTypes.ClosingBrace, false));
							}
							else
							{
								result = new Token(null, TokenTypes.ClosingBrace, false);
							}
							isDone = true;
						}
						else
						{
							running.add(curChar);
						}

						break;
					}
					case '"':
					{
						if (running.size() == 0)
						{
							if(!isString)
							{
							isString = true;
							}
							else
							{
								isString = false;
								running.add((char) 0); 
							}
						}
						else
						{
							if(!isString)
							{
								throw new IllegalArgumentException("midString quote mark!");
							}
							else
							{
								isString = false;
							}
						}

						break;
					}
					default:
					{
						running.add(curChar);
						break;
					}
				}
			}
		}
		else
		{
			result = this.tokenBuffer.pop();
		}

		return result;
	}

	public static void main(String[] args)
	{
		final String testDir = "Fill Me In";
		final BytesStreamer stream = BytesStreamerFactory.getInstance(testDir, StringInterpretations.FilePath);
		stream.getNextByte();
		final Tokenizer tokey = new JsonTokenizer();
		final LotusObject target = Parser.parseLotusObject(tokey, stream, "Languages");

		System.out.println(target.toCharList(0, false, true, true));
	}
}
