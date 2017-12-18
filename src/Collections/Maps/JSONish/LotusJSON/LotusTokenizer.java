package Collections.Maps.JSONish.LotusJSON;

import Collections.Lists.CharList.CharList;
import Collections.Lists.Generic.Directed.SingleLinkedList;
import Collections.Maps.JSONish.Backend.Token;
import Collections.Maps.JSONish.Backend.Tokenizer;
import Collections.Maps.JSONish.Backend.Token.TokenTypes;
import Streams.BytesStreamer.BytesStreamer;

public class LotusTokenizer implements Tokenizer
{
	private final SingleLinkedList<Token> tokenBuffer;

	public LotusTokenizer()
	{
		this.tokenBuffer = new SingleLinkedList<Token>();
	}

	@Override
	public Token parseNextToken(BytesStreamer in)
	{
		return this.parseNextToken_Internal(in, true);
	}

	@Override
	public Token peekAtNextToken(BytesStreamer in)
	{
		if (this.tokenBuffer.isEmpty())
		{
			final Token next = this.parseNextToken_Internal(in, true);
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
			CharList running = new CharList();
			boolean isDone = false;
			boolean isString = false;

			while (!isDone && in.canSafelyConsume(1))
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
					case '=':
					{
						result = new Token(running, TokenTypes.Name, false);
						isDone = true;

						break;
					}
					case '\r':
					case '\n':
					{
						if (treatEndOfLineAsBreaker && running.size() > 0)
						{
							result = new Token(running, TokenTypes.Var, isString);
							isDone = true;
						}

						break;
					}
					case '\t':
					case ' ':
					{
						if(isString || running.size() > 0)
						{
							running.add(curChar);
						}
						break;
					}
					case '{':
					{
						final Token temp = this.parseNextToken_Internal(in, false); // if the next thing is a name, this thing is an object

						if (temp.getType() == TokenTypes.Name)
						{
							result = new Token(null, TokenTypes.Object, false);
							isDone = true;
						}
						else
						{
							result = new Token(null, TokenTypes.Array, false);
							isDone = true;
						}

						this.tokenBuffer.push(temp);

						break;
					}
					case '[':
					{
						if (running.size() == 0)
						{
							result = new Token(null, TokenTypes.Array, false);
							isDone = true;
						}
						else
						{
							running.add(curChar);
						}
						break;
					}
					case ',':
					{
						if (running.size() > 0)
						{
							result = new Token(running, TokenTypes.Var, isString);
							this.tokenBuffer.add(new Token(null, TokenTypes.Comma, false));
						}
						else
						{
							result = new Token(null, TokenTypes.Comma, false);
						}

						isDone = true;
						break;
					}
					case '}':
					case ']':
					{
						if (running.size() > 0)
						{
							if (running.lastChar() != '+')
							{
								result = new Token(running, TokenTypes.Var, isString);
								this.tokenBuffer.add(new Token(null, TokenTypes.ClosingBrace, false));
							}
							else
							{
								running.add(curChar);
							}
						}
						else
						{
							result = new Token(null, TokenTypes.ClosingBrace, false);
						}
						isDone = true;

						break;
					}
					case '"':
					{
						if (running.size() == 0 && !isString)
						{
							isString = true;
						}
						else
						{
							result = new Token(running, TokenTypes.Var, isString);
							isDone = true;
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
}
