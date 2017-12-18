package HandyStuff;

import Collections.Lists.CharList.CharList;
import CustomExceptions.BadParseException;
import CustomExceptions.UnhandledEnumException;
import HandyStuff.ArrayStuff.EmptyPrimitiveArrays;
import Incubator.JSONish.Mk02.LotusParser;
import Streams.BytesStreamer.BytesStreamer;

/**
 * allowed types:
 * <ul>
 * <li>
 * Boolean: valued either {@code true} or {@code false}, with regex for allowed literals:
 * <ul>
 * <li>
 * for value {@code true}:
 * <ul>
 * <li>(T|t)rue</li>
 * </ul>
 * </li>
 * <li>
 * for value {@code false}:
 * <ul>
 * <li>(F|f)alse</li>
 * </ul>
 * </li>
 * </ul>
 * </li>
 * <li>
 * Integer: 32-bit signed integer, with regex for allowed literals:
 * <ul>
 * <li>-?[0-9]* for base-10</li>
 * <li>0b[01]* for base-2(unsigned)</li>
 * <li>0x([0-9]|[a-f]|[A-F]|\s)*([0-9]|[a-f]|[A-F]) for base-16(unsigned)</li>
 * </ul>
 * </li>
 * <li>
 * Floating-Point: 64-bit floating-point number, identical to Java's {@code double} primitive standard, with regex for allowed literals:
 * <ul>
 * <li>-?[0-9]*\.[0-9]*(f|d)</li>
 * </ul>
 * </li>
 * <li>
 * String: a string of characters, enclosed by quotation marks.
 * </li>
 * <li>
 * Reference: a string of characters, not enclosed by quotation marks.
 * </li>
 * </ul>
 *
 */
public class LotusVarParser
{
	private static final char[] BoolParsing_true_ru = "ru".toCharArray();
	private static final char[] BoolParsing_true_rue = "rue".toCharArray();

	private static final char[] BoolParsing_false_al = "al".toCharArray();
	private static final char[] BoolParsing_false_als = "als".toCharArray();
	private static final char[] BoolParsing_false_alse = "alse".toCharArray();

	private static final char[] NullParsing_nu = "nu".toCharArray();
	private static final char[] NullParsing_nul = "nul".toCharArray();
	private static final char[] NullParsing_null = "null".toCharArray();
	// private static final int offset_CharDigitToIntValue = 48;

	private VarTypes parsedType;
	private final CharList stringValue;
	private boolean BoolValue;
	private int IntValue;
	private double FloatValue;

	public LotusVarParser()
	{
		this.parsedType = null;
		this.stringValue = new CharList();
		this.BoolValue = false;
		this.IntValue = 0;
		this.FloatValue = 0.0d;
	}

	public final void resetParser()
	{
		this.stringValue.clear();
		// this.parsedType = null;
		// I think everything else can be ignored
	}

	public final VarTypes getParsedVarType()
	{
		return this.parsedType;
	}

	public final char[] getStringValue()
	{
		switch (this.parsedType)
		{
			case EmptyString:
			case EmptyString_withComma:
			case LastElement_EmptyString:
			{
				return EmptyPrimitiveArrays.emptyCharArr;
			}
			default:
			{
				// System.out.println("getting string value: " + this.stringValue.toString());
				return this.stringValue.toCharArray();
			}
		}
	}

	public final char[] getQuotedStringValue()
	{
		switch (this.parsedType)
		{
			case EmptyString:
			case EmptyString_withComma:
			case LastElement_EmptyString:
			{
				this.stringValue.clear();
			}
			default:
			{
				// System.out.println("getting string value: " + this.stringValue.toString());
				this.stringValue.add('"');
				this.stringValue.push('"');
				return this.stringValue.toCharArray();
			}
		}
	}

	public final boolean getBoolValue()
	{
		// System.out.println("getting boolean value: " + this.BoolValue);
		return this.BoolValue;
	}

	public final int getIntValue()
	{
		// System.out.println("getting int value: " + this.IntValue);
		return this.IntValue;
	}

	public final double getFloatValue()
	{
		// System.out.println("getting float value: " + this.FloatValue);
		return this.FloatValue;
	}

	public final void resetAndParseValue(char Char0, BytesStreamer in)
	{
		this.resetParser();
		this.parseValue(Char0, in);
		// System.out.println("started with '" + Char0 + "', and got " + this.toString());
	}

	public final void resetAndParseValue_asName(char Char0, BytesStreamer in)
	{
		this.resetParser();
		this.parseValue_asName(Char0, in);
	}

	public final void parseValue_asName(char Char0, BytesStreamer in)
	{
		this.stringValue.clear();
		this.stringValue.add(Char0);

		char curr;

		while (in.hasNextByte())
		{
			curr = in.getNextChar();

			if (curr == '=')
			{
				this.parsedType = VarTypes.Name;
				return;
			}
			else
			{
				this.stringValue.add(curr);
			}
		}

		throw new IndexOutOfBoundsException();
	}

	public final void parseValue(char Char0, BytesStreamer in)
	{
		Whitespacer: do
		{
			switch (Char0)
			{
				case ' ':
				case '\t':
				case '\r':
				case '\n':
				{
					if (in.hasNextByte())
					{
						Char0 = in.getNextChar();
						break;
					}
					else
					{
						this.parsedType = VarTypes.NullReference;
						return;
					}
				}
				case ']':
				case '}':
				{
					switch (this.parsedType)
					{
						case Name:
						{
							this.parseAsRef(Char0, in);
							break;
						}
						default:
						{
							this.parsedType = VarTypes.LastElement_NullReference;
							break;
						}
					}

					return;
				}
				case ',':
				{
					this.parsedType = VarTypes.NullReference_withComma;
					return;
				}
				default:
				{

					break Whitespacer;
				}
			}
		} while (true);

		switch (Char0)
		{
			case '"':
			{
				if (!in.hasNextByte())
				{
					this.stringValue.add('"');
					this.parsedType = VarTypes.Reference;
					return;
				}

				do
				{
					final char curr = in.getNextChar();

					switch (curr)
					{
						case '"':
						{
							if (in.hasNextByte())
							{
								final char next = in.getNextChar();

								switch (next)
								{
									case ',':
									{
										if (this.stringValue.isEmpty())
										{
											this.parsedType = VarTypes.EmptyString_withComma;
										}
										else
										{
											this.parsedType = VarTypes.String_withComma;
										}
										break;
									}
									case '}':
									case ']':
									{
										if (this.stringValue.isEmpty())
										{
											this.parsedType = VarTypes.LastElement_EmptyString;
										}
										else
										{
											this.parsedType = VarTypes.LastElement_String;
										}
										break;
									}
									case ' ':
									case '\t':
									case '\r':
									case '\n':
									{
										final char test = LotusParser.idleToNextNonWhitespace(in);
										
										if((test != ']') && (test != '}'))
										{
											in.advancePosition(-1);
											
											if (this.stringValue.isEmpty())
											{
												this.parsedType = VarTypes.EmptyString;
											}
											else
											{
												this.parsedType = VarTypes.String;
											}
										}
										else
										{
											if (this.stringValue.isEmpty())
											{
												this.parsedType = VarTypes.LastElement_EmptyString;
											}
											else
											{
												this.parsedType = VarTypes.LastElement_EmptyString;
											}
										}
										
										
										break;
									}
									default: // double-quoted without escaping
									{
										this.stringValue.add("\\\"");
										this.stringValue.add(next);

										while (in.hasNextByte())
										{
											final char internal = in.getNextChar();

											switch (internal)
											{
												case '"':
												{
													final char test = in.getNextChar();

													if (test == '"')
													{
														this.stringValue.add('"');
														this.parsedType = VarTypes.String;
														return;
													}
													else
													{
														throw new BadParseException("was not expecting a '" + next + "' at pos: " + in.getPosition());
													}
												}
												case '\\':
												{
													this.stringValue.add('\\');
													this.stringValue.add(in.getNextChar());
													break;
												}
												default:
												{
													this.stringValue.add(internal);
													break;
												}
											}
										}

										throw new BadParseException("was not expecting a '" + next + "' at pos: " + in.getPosition());
									}
								}
							}

							return;
						}
						default:
						{
							this.stringValue.add(curr);
							break;
						}
					}
				} while (in.hasNextByte());
				
				// if it makes it here, it's a reference, because there was no ending quotemark
				this.parsedType = VarTypes.Reference;
				this.stringValue.push('"');
				return;
			}
			case 'n':
			{
				this.parseAsNull(in);
				return;
			}
			case '}':
			case ']':
			{
				this.parsedType = VarTypes.EmptyArray;
				return;
			}
			case '-':
			{
				this.inlinedCase_Hyphen(in);
				return;
			}
			case '+':
			{
				this.parseAsDecimal_General(in);
				return;
			}
			case '.':
			{
				if (!in.hasNextByte())
				{
					this.stringValue.add('.');
					this.parsedType = VarTypes.Reference;
					return;
				}
				else
				{
					this.IntValue = 0;
					this.parseAsDecimal_LastCharPeriod_firstCharAfter(in);
					return;
				}
			}
			case '0':
			{
				this.inlinedCase_Zero(in);
				return;
			}
			case '1':
			{
				this.IntValue = 1;
				break;
			}
			case '2':
			{
				this.IntValue = 2;
				break;
			}
			case '3':
			{
				this.IntValue = 3;
				break;
			}
			case '4':
			{
				this.IntValue = 4;
				break;
			}
			case '5':
			{
				this.IntValue = 5;
				break;
			}
			case '6':
			{
				this.IntValue = 6;
				break;
			}
			case '7':
			{
				this.IntValue = 7;
				break;
			}
			case '8':
			{
				this.IntValue = 8;
				break;
			}
			case '9':
			{
				this.IntValue = 9;
				break;
			}
			case 'f':
			case 'F':
			{
				this.parseAsBool_false(Char0, in);
				return;
			}
			case 't':
			case 'T':
			{
				this.parseAsBool_true(Char0, in);
				return;
			}
			default:
			{
				this.parseAsRef(Char0, in);
				return;
			}
		}

		if (!in.hasNextByte())
		{
			this.parsedType = VarTypes.Integer;
			return;
		}
		else
		{
			this.parseAsDecimal_General(in);
			return;
		}
	}

	private final void inlinedCase_Hyphen(BytesStreamer in)
	{
		if (!in.hasNextByte())
		{
			this.stringValue.add('-');
			this.parsedType = VarTypes.Reference;
			return;
		}

		do
		{
			final char CharCurr = in.getNextChar();

			switch (CharCurr)
			{
				case '.':
				{
					if (in.hasNextByte())
					{
						this.parseAsDecimal_LastCharPeriod_firstCharAfter(in);
						this.FloatValue = -this.FloatValue;
						return;
					}
					else
					{
						this.parsedType = VarTypes.Float;
						this.FloatValue = -this.IntValue;
						return;
					}
				}
				case '0':
				{
					this.IntValue = this.IntValue * 10;
					break;
				}
				case '1':
				{
					this.IntValue = (this.IntValue * 10) + 1;
					break;
				}
				case '2':
				{
					this.IntValue = (this.IntValue * 10) + 2;
					break;
				}
				case '3':
				{
					this.IntValue = (this.IntValue * 10) + 3;
					break;
				}
				case '4':
				{
					this.IntValue = (this.IntValue * 10) + 4;
					break;
				}
				case '5':
				{
					this.IntValue = (this.IntValue * 10) + 5;
					break;
				}
				case '6':
				{
					this.IntValue = (this.IntValue * 10) + 6;
					break;
				}
				case '7':
				{
					this.IntValue = (this.IntValue * 10) + 7;
					break;
				}
				case '8':
				{
					this.IntValue = (this.IntValue * 10) + 8;
					break;
				}
				case '9':
				{
					this.IntValue = (this.IntValue * 10) + 9;
					break;
				}
				case ' ':
				case '\t':
				case '\r':
				case '\n':
				{
					this.parsedType = VarTypes.Integer;
					this.IntValue = -this.IntValue;
					return;
				}
				case ',':
				{
					this.parsedType = VarTypes.Integer_withComma;
					this.IntValue = -this.IntValue;
					return;
				}
				case '}':
				case ']':
				{
					this.parsedType = VarTypes.LastElement_Integer;
					return;
				}
				default:
				{
					this.recoverFromBadWholeNumberParse(CharCurr, in);
					return;
				}
			}
		} while (in.hasNextByte());

		// if code is here or past, it's still an integer
		this.parsedType = VarTypes.Integer;
		this.IntValue = -this.IntValue;
	}

	private final void inlinedCase_Zero(BytesStreamer in)
	{
		if (!in.hasNextByte())
		{
			this.parsedType = VarTypes.Integer;
			return;
		}

		final char Char1 = in.getNextChar();

		switch (Char1)
		{
			case 'b':
			{
				this.IntValue = 0;
				this.parseAsInt_BinaryFormat(in);
				return;
			}
			case 'x':
			{
				this.IntValue = 0;
				this.parseAsInt_HexFormat(in);
				return;
			}
			case '.':
			{
				this.parseAsDecimal_LastCharPeriod_firstCharAfter(in);
				return;
			}
			case ' ':
			case '\t':
			case '\r':
			case '\n':
			{
				this.IntValue = 0;
				this.parsedType = VarTypes.Integer;
				return;
			}
			case ',':
			{
				this.IntValue = 0;
				this.parsedType = VarTypes.Integer_withComma;
				return;
			}
			case '}':
			case ']':
			{
				this.parsedType = VarTypes.LastElement_Integer;
				return;
			}
			case '0':
			{
				this.IntValue = 0;
				break;
			}
			case '1':
			{
				this.IntValue = 1;
				break;
			}
			case '2':
			{
				this.IntValue = 2;
				break;
			}
			case '3':
			{
				this.IntValue = 3;
				break;
			}
			case '4':
			{
				this.IntValue = 4;
				break;
			}
			case '5':
			{
				this.IntValue = 5;
				break;
			}
			case '6':
			{
				this.IntValue = 6;
				break;
			}
			case '7':
			{
				this.IntValue = 7;
				break;
			}
			case '8':
			{
				this.IntValue = 8;
				break;
			}
			case '9':
			{
				this.IntValue = 9;
				break;
			}
			default:
			{
				this.recoverFromBadWholeNumberParse(Char1, in);
				return;
			}
		}

		this.parseAsDecimal_General(in);
		return;
	}

	/**
	 * kept for reference - only parses String and Reference
	 * 
	 * @param Char0
	 * @param in
	 */
	/*
	private final void parseAsString(char Char0, BytesStreamer in)
	{
		switch (Char0)
		{
			case '"':
			{
				if (!in.hasNextByte())
				{
					this.stringValue.add('"');
					this.parsedType = VarTypes.Reference;
					return;
				}
				else
				{
					do
					{
						final char curr = in.getNextChar();
	
						switch (curr)
						{
							case '"':
							{
								this.parsedType = VarTypes.String;
								return;
							}
							default:
							{
								this.stringValue.add(curr);
								break;
							}
						}
					} while (in.hasNextByte());
	
					// if it makes it here, it's a reference, because there was no ending quotemark
					this.parsedType = VarTypes.Reference;
					this.stringValue.push('"');
					return;
				}
			}
			default:
			{
				this.parseAsRef(Char0, in);
				return;
			}
		}
	}
	*/

	/**
	 * kept for reference - only parses Float and Reference
	 * 
	 * @param Char0
	 * @param in
	 */
	/*
	private final void parseAsDecimal(char Char0, BytesStreamer in)
	{
		switch (Char0)
		{
			case '-':
			{
				if (!in.hasNextByte())
				{
					this.stringValue.add('-');
					this.parsedType = VarTypes.Reference;
					return;
				}
	
				this.parseAsDecimal_unsigned(in.getNextChar(), in);
	
				switch (this.parsedType)
				{
					case Float:
					{
						this.FloatValue = -this.FloatValue;
						return;
					}
					case Integer:
					{
						this.IntValue = -this.IntValue;
						return;
					}
					default:
					{
						return;
					}
				}
			}
			default:
			{
				this.parseAsDecimal_unsigned(Char0, in);
				break;
			}
		}
	}
	
	private final void parseAsDecimal_unsigned(char Char0, BytesStreamer in)
	{
		switch (Char0)
		{
			case '.':
			{
				this.FloatValue = 0;
				this.parseAsDecimal_LastCharPeriod(in);
				return;
			}
			case '0':
			{
				this.IntValue = 0;
				break;
			}
			case '1':
			{
				this.IntValue = 1;
				break;
			}
			case '2':
			{
				this.IntValue = 2;
				break;
			}
			case '3':
			{
				this.IntValue = 3;
				break;
			}
			case '4':
			{
				this.IntValue = 4;
				break;
			}
			case '5':
			{
				this.IntValue = 5;
				break;
			}
			case '6':
			{
				this.IntValue = 6;
				break;
			}
			case '7':
			{
				this.IntValue = 7;
				break;
			}
			case '8':
			{
				this.IntValue = 8;
				break;
			}
			case '9':
			{
				this.IntValue = 9;
				break;
			}
			default:
			{
				this.parseAsRef(Char0, in);
				return;
			}
		}
	
		this.parseAsDecimal_General(in);
	}
	*/

	private final void parseAsDecimal_General(BytesStreamer in)
	{
		do
		{
			final char CharCurr = in.getNextChar();

			switch (CharCurr)
			{
				case ' ':
				case '\t':
				case '\r':
				case '\n':
				{
					this.parsedType = VarTypes.Integer;
					return;
				}
				case '}':
				case ']':
				{
					this.parsedType = VarTypes.LastElement_Integer;
					return;
				}
				case ',':
				{
					this.parsedType = VarTypes.Integer_withComma;
					return;
				}
				case '.':
				{
					this.FloatValue = this.IntValue;

					if (in.hasNextByte())
					{
						this.parseAsDecimal_LastCharPeriod_firstCharAfter(in);
						return;
					}
					else
					{
						this.parsedType = VarTypes.Float;
						return;
					}
				}
				case 'e':
				case 'E':
				{
					this.parseAsDecimal_Scientific(in, CharCurr, this.IntValue);
					return;
				}
				case '0':
				{
					this.IntValue = this.IntValue * 10;
					break;
				}
				case '1':
				{
					this.IntValue = (this.IntValue * 10) + 1;
					break;
				}
				case '2':
				{
					this.IntValue = (this.IntValue * 10) + 2;
					break;
				}
				case '3':
				{
					this.IntValue = (this.IntValue * 10) + 3;
					break;
				}
				case '4':
				{
					this.IntValue = (this.IntValue * 10) + 4;
					break;
				}
				case '5':
				{
					this.IntValue = (this.IntValue * 10) + 5;
					break;
				}
				case '6':
				{
					this.IntValue = (this.IntValue * 10) + 6;
					break;
				}
				case '7':
				{
					this.IntValue = (this.IntValue * 10) + 7;
					break;
				}
				case '8':
				{
					this.IntValue = (this.IntValue * 10) + 8;
					break;
				}
				case '9':
				{
					this.IntValue = (this.IntValue * 10) + 9;
					break;
				}
				case '+':
				{
					break;
				}
				default:
				{
					this.recoverFromBadWholeNumberParse(CharCurr, in);
					return;
				}
			}
		} while (in.hasNextByte());

	}

	private final void parseAsDecimal_LastCharPeriod_firstCharAfter(BytesStreamer in)
	{
		/*
		if (!in.hasNextByte())
		{
			this.recoverFromBadWholeNumberParse('.', in);
			return;
		}
		*/

		char Char1 = in.getNextChar();

		if (Char1 == '+')
		{
			Char1 = in.getNextChar();
		}

		switch (Char1)
		{
			case ' ':
			case '\t':
			case '\r':
			case '\n':
			{
				this.parsedType = VarTypes.Float;
				this.FloatValue = this.IntValue;
				return;
			}
			case ',':
			{
				this.parsedType = VarTypes.Float_withComma;
				this.FloatValue = this.IntValue;
				return;
			}
			case '}':
			case ']':
			{
				this.parsedType = VarTypes.LastElement_Float;
				return;
			}
			case 'e':
			case 'E':
			{
				this.parseAsDecimal_Scientific(in, Char1, this.IntValue);
				return;
			}
			case 'f':
			case 'F':
			case 'd':
			case 'D':
			{
				if (in.hasNextByte())
				{
					final char lookahead = in.getNextChar();

					switch (lookahead)
					{
						case ' ':
						case '\t':
						case '\r':
						case '\n':
						{
							break;
						}
						case ',':
						{
							this.parsedType = VarTypes.Float_withComma;
							this.FloatValue = this.IntValue;
							return;
						}
						default:
						{
							this.recoverFromBadDecimalParse(lookahead, in);
							return;
						}
					}
				}

				this.parsedType = VarTypes.Float;
				this.FloatValue = this.IntValue;
				return;
			}
			case '0':
			{
				this.FloatValue = this.IntValue;
				break;
			}
			case '1':
			{
				this.FloatValue = this.IntValue + (1.0d / 10.0d);
				break;
			}
			case '2':
			{
				this.FloatValue = this.IntValue + (2.0d / 10.0d);
				break;
			}
			case '3':
			{
				this.FloatValue = this.IntValue + (3.0d / 10.0d);
				break;
			}
			case '4':
			{
				this.FloatValue = this.IntValue + (4.0d / 10.0d);
				break;
			}
			case '5':
			{
				this.FloatValue = this.IntValue + (5.0d / 10.0d);
				break;
			}
			case '6':
			{
				this.FloatValue = this.IntValue + (6.0d / 10.0d);
				break;
			}
			case '7':
			{
				this.FloatValue = this.IntValue + (7.0d / 10.0d);
				break;
			}
			case '8':
			{
				this.FloatValue = this.IntValue + (8.0d / 10.0d);
				break;
			}
			case '9':
			{
				this.FloatValue = this.IntValue + (9.0d / 10.0d);
				break;
			}
			default:
			{
				this.recoverFromBadWholeNumberParse('.', in);
				return;
			}
		}

		if (in.hasNextByte())
		{
			this.IntValue = 10;
			this.parseAsDecimal_LastCharPeriod_loop(in);
		}
		else
		{
			this.parsedType = VarTypes.Float;
			return;
		}
	}

	private final void parseAsDecimal_LastCharPeriod_loop(BytesStreamer in)
	{
		Loop: do
		{
			final char CharCurr = in.getNextChar();
			this.IntValue *= 10;

			switch (CharCurr)
			{
				case ' ':
				case '\t':
				case '\r':
				case '\n':
				{
					break Loop;
				}
				case ',':
				{
					this.parsedType = VarTypes.Float_withComma;
					return;
				}
				case '}':
				case ']':
				{
					this.parsedType = VarTypes.LastElement_Float;
					return;
				}
				case 'e':
				case 'E':
				{
					this.parseAsDecimal_Scientific(in, CharCurr, this.FloatValue);
					return;
				}
				case 'f':
				case 'F':
				case 'd':
				case 'D':
				{
					if (in.hasNextByte())
					{
						final char lookahead = in.getNextChar();

						switch (lookahead)
						{
							case ' ':
							case '\t':
							case '\r':
							case '\n':
							{
								break Loop;
							}
							case ',':
							{
								this.parsedType = VarTypes.Float_withComma;
								return;
							}
							default:
							{
								this.recoverFromBadDecimalParse(lookahead, in);
								return;
							}
						}
					}

					break Loop;
				}
				case '0':
				{
					break;
				}
				case '1':
				{
					this.FloatValue += (1.0d / this.IntValue);
					break;
				}
				case '2':
				{
					this.FloatValue += (2.0d / this.IntValue);
					break;
				}
				case '3':
				{
					this.FloatValue += (3.0d / this.IntValue);
					break;
				}
				case '4':
				{
					this.FloatValue += (4.0d / this.IntValue);
					break;
				}
				case '5':
				{
					this.FloatValue += (5.0d / this.IntValue);
					break;
				}
				case '6':
				{
					this.FloatValue += (6.0d / this.IntValue);
					break;
				}
				case '7':
				{
					this.FloatValue += (7.0d / this.IntValue);
					break;
				}
				case '8':
				{
					this.FloatValue += (8.0d / this.IntValue);
					break;
				}
				case '9':
				{
					this.FloatValue += (9.0d / this.IntValue);
					break;
				}
				default:
				{
					if (this.IntValue == 10)
					{
						this.recoverFromBadWholeNumberParse('.', in);
					}
					else
					{
						this.recoverFromBadDecimalParse(CharCurr, in);
					}
					return;
				}
			}
		} while (in.hasNextByte());

		this.parsedType = VarTypes.Float;
	}

	private final void parseAsDecimal_Scientific(BytesStreamer in, char letterCase, double scalar)
	{
		this.parseValue(in.getNextChar(), in);

		switch (this.parsedType)
		{
			case Integer:
			{
				this.parsedType = VarTypes.Float;
				break;
			}
			case Integer_withComma:
			{
				this.parsedType = VarTypes.Float_withComma;
				break;
			}
			case LastElement_Integer:
			{
				this.parsedType = VarTypes.LastElement_Float;
				break;
			}
			case Float:
			case Float_withComma:
			case LastElement_Float:
			{
				throw new IllegalArgumentException("the power should not be a decimal!");
			}
			case Reference:
			case Reference_withComma:
			case LastElement_Reference:
			{
				this.stringValue.push(letterCase);
				this.stringValue.push_asDecString((int) scalar);
				return;
			}
			default:
			{
				throw new UnhandledEnumException(this.toString() + " @pos: " + in.getPosition());
			}
		}

		this.FloatValue = scalar * Math.pow(10, this.IntValue);
	}

	private final void recoverFromBadDecimalParse(char lastChar, BytesStreamer in)
	{
		this.stringValue.add_asDecString(this.FloatValue, 0);
		this.parseAsRef(lastChar, in);
	}

	/**
	 * kept for reference, only parses Integer and Reference
	 * 
	 * @param Char0
	 * @param in
	 */
	/*
	private final void parseAsInt(char Char0, BytesStreamer in)
	{
		switch (Char0)
		{
			case '-':
			{
				this.IntValue = 0;
				this.parseAsInt_FirstCharHyphen(in);
				return;
			}
			case '0':
			{
				this.IntValue = 0;
				this.parseAsInt_FirstCharZero(in);
				return;
			}
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			{
				// inline this individually later
				this.IntValue = Char0 - offset_CharDigitToIntValue;
				this.parseAsInt_UnsignedGeneral(in);
				return;
			}
			default:
			{
				this.parseAsRef(Char0, in);
				break;
			}
		}
	}
	
	private final void parseAsInt_FirstCharHyphen(BytesStreamer in)
	{
		if (!in.hasNextByte())
		{
			this.recoverFromBadWholeNumberParse('-', in);
			return;
		}
	
		final char Char1 = in.getNextChar();
	
		switch (Char1)
		{
	
		}
	
		Loop: while (in.hasNextByte())
		{
			final char CharCurr = in.getNextChar();
	
			switch (CharCurr)
			{
				case '0':
				{
					this.IntValue = 1;
					break;
				}
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				{
					// inline this individually later
					this.IntValue = (this.IntValue * 10) + (CharCurr - offset_CharDigitToIntValue);
					break;
				}
				case ' ':
				case '\t':
				case '\r':
				case '\n':
				{
					break Loop;
				}
				default:
				{
					this.recoverFromBadWholeNumberParse(CharCurr, in);
					return;
				}
			}
	
			this.parseAsInt_UnsignedGeneral(in);
		}
	
		this.parsedType = VarTypes.Integer;
		this.IntValue = -this.IntValue;
	}
	
	private final void parseAsDecimal_FirstCharZero(BytesStreamer in)
	{
		if (in.hasNextByte())
		{
			final char Char1 = in.getNextChar();
	
			switch (Char1)
			{
				case 'b':
				{
					this.parseAsInt_BinaryFormat(in);
					return;
				}
				case 'x':
				{
					this.parseAsInt_HexFormat(in);
					return;
				}
				case '0':
				{
					this.IntValue = 0;
					this.parseAsDecimal_General(in);
					return;
				}
				case '1':
				{
					this.IntValue = 1;
					this.parseAsDecimal_General(in);
					return;
				}
				case '2':
				{
					this.IntValue = 2;
					this.parseAsDecimal_General(in);
					return;
				}
				case '3':
				{
					this.IntValue = 3;
					this.parseAsDecimal_General(in);
					return;
				}
				case '4':
				{
					this.IntValue = 4;
					this.parseAsDecimal_General(in);
					return;
				}
				case '5':
				{
					this.IntValue = 5;
					this.parseAsDecimal_General(in);
					return;
				}
				case '6':
				{
					this.IntValue = 6;
					this.parseAsDecimal_General(in);
					return;
				}
				case '7':
				{
					this.IntValue = 7;
					this.parseAsDecimal_General(in);
					return;
				}
				case '8':
				{
					this.IntValue = 8;
					this.parseAsDecimal_General(in);
					return;
				}
				case '9':
				{
					this.IntValue = 9;
					this.parseAsDecimal_General(in);
					return;
				}
				default:
				{
					this.recoverFromBadWholeNumberParse(Char1, in);
					return;
				}
			}
		}
		else
		{
			this.parsedType = VarTypes.Integer;
		}
	}
	
	private final void parseAsInt_FirstCharZero(BytesStreamer in)
	{
		if (in.hasNextByte())
		{
			final char Char1 = in.getNextChar();
	
			switch (Char1)
			{
				case 'b':
				{
					this.parseAsInt_BinaryFormat(in);
					return;
				}
				case 'x':
				{
					this.parseAsInt_HexFormat(in);
					return;
				}
				case '0':
				{
					this.IntValue = this.IntValue * 10;
					this.parseAsInt_UnsignedGeneral(in);
					return;
				}
				case '1':
				{
					this.IntValue = (this.IntValue * 10) + 1;
					this.parseAsInt_UnsignedGeneral(in);
					return;
				}
				case '2':
				{
					this.IntValue = (this.IntValue * 10) + 2;
					this.parseAsInt_UnsignedGeneral(in);
					return;
				}
				case '3':
				{
					this.IntValue = (this.IntValue * 10) + 3;
					this.parseAsInt_UnsignedGeneral(in);
					return;
				}
				case '4':
				{
					this.IntValue = (this.IntValue * 10) + 4;
					this.parseAsInt_UnsignedGeneral(in);
					return;
				}
				case '5':
				{
					this.IntValue = (this.IntValue * 10) + 5;
					this.parseAsInt_UnsignedGeneral(in);
					return;
				}
				case '6':
				{
					this.IntValue = (this.IntValue * 10) + 6;
					this.parseAsInt_UnsignedGeneral(in);
					return;
				}
				case '7':
				{
					this.IntValue = (this.IntValue * 10) + 7;
					this.parseAsInt_UnsignedGeneral(in);
					return;
				}
				case '8':
				{
					this.IntValue = (this.IntValue * 10) + 8;
					this.parseAsInt_UnsignedGeneral(in);
					return;
				}
				case '9':
				{
					this.IntValue = (this.IntValue * 10) + 9;
					this.parseAsInt_UnsignedGeneral(in);
					return;
				}
				default:
				{
					this.recoverFromBadWholeNumberParse(Char1, in);
					return;
				}
			}
		}
		else
		{
			this.parsedType = VarTypes.Integer;
		}
	}
	*/

	private final void recoverFromBadWholeNumberParse(char lastChar, BytesStreamer in)
	{
		this.stringValue.add_asDecString(this.IntValue, 0);
		this.parseAsRef(lastChar, in);
	}

	private final void parseAsInt_BinaryFormat(BytesStreamer in)
	{
		while (in.hasNextByte())
		{
			final char CharCurr = in.getNextChar();

			switch (CharCurr)
			{
				case ' ':
				case '\t':
				case '\r':
				case '\n':
				{
					this.parsedType = VarTypes.Integer;
					return;
				}
				case ',':
				{
					this.parsedType = VarTypes.Integer_withComma;
					return;
				}
				case '}':
				case ']':
				{
					this.parsedType = VarTypes.LastElement_Integer;
					return;
				}
				case '0':
				{
					this.IntValue = this.IntValue << 1;
					break;
				}
				case '1':
				{
					this.IntValue = (this.IntValue << 1) | 1;
					break;
				}
				default:
				{
					this.recoverFromBadWholeNumberParse(CharCurr, in);
					return;
				}
			}
		}
	}

	private final void parseAsInt_HexFormat(BytesStreamer in)
	{
		while (in.hasNextByte())
		{
			final char CharCurr = in.getNextChar();

			switch (CharCurr)
			{
				case ' ':
				case '\t':
				case '\r':
				case '\n':
				{
					this.parsedType = VarTypes.Integer;
					return;
				}
				case ',':
				{
					this.parsedType = VarTypes.Integer_withComma;
					return;
				}
				case '}':
				case ']':
				{
					this.parsedType = VarTypes.LastElement_Integer;
					return;
				}
				case '0':
				{
					this.IntValue = this.IntValue * 16;
					break;
				}
				case '1':
				{
					this.IntValue = (this.IntValue * 16) + 1;
					break;
				}
				case '2':
				{
					this.IntValue = (this.IntValue * 16) + 2;
					break;
				}
				case '3':
				{
					this.IntValue = (this.IntValue * 16) + 3;
					break;
				}
				case '4':
				{
					this.IntValue = (this.IntValue * 16) + 4;
					break;
				}
				case '5':
				{
					this.IntValue = (this.IntValue * 16) + 5;
					break;
				}
				case '6':
				{
					this.IntValue = (this.IntValue * 16) + 6;
					break;
				}
				case '7':
				{
					this.IntValue = (this.IntValue * 16) + 7;
					break;
				}
				case '8':
				{
					this.IntValue = (this.IntValue * 16) + 8;
					break;
				}
				case '9':
				{
					this.IntValue = (this.IntValue * 16) + 9;
					break;
				}
				case 'a':
				case 'A':
				{
					this.IntValue = (this.IntValue * 16) + 10;
					break;
				}
				case 'b':
				case 'B':
				{
					this.IntValue = (this.IntValue * 16) + 11;
					break;
				}
				case 'c':
				case 'C':
				{
					this.IntValue = (this.IntValue * 16) + 12;
					break;
				}
				case 'd':
				case 'D':
				{
					this.IntValue = (this.IntValue * 16) + 13;
					break;
				}
				case 'e':
				case 'E':
				{
					this.IntValue = (this.IntValue * 16) + 14;
					break;
				}
				case 'f':
				case 'F':
				{
					this.IntValue = (this.IntValue * 16) + 15;
					break;
				}
				default:
				{
					this.recoverFromBadWholeNumberParse(CharCurr, in);
					return;
				}
			}
		}
	}

	/*
	private final void parseAsInt_UnsignedGeneral(BytesStreamer in)
	{
		Loop: while (in.hasNextByte())
		{
			final char CharCurr = in.getNextChar();
	
			switch (CharCurr)
			{
				case '0':
				{
					this.IntValue = this.IntValue * 10;
					break;
				}
				case '1':
				{
					this.IntValue = (this.IntValue * 10) + 1;
					break;
				}
				case '2':
				{
					this.IntValue = (this.IntValue * 10) + 2;
					break;
				}
				case '3':
				{
					this.IntValue = (this.IntValue * 10) + 3;
					break;
				}
				case '4':
				{
					this.IntValue = (this.IntValue * 10) + 4;
					break;
				}
				case '5':
				{
					this.IntValue = (this.IntValue * 10) + 5;
					break;
				}
				case '6':
				{
					this.IntValue = (this.IntValue * 10) + 6;
					break;
				}
				case '7':
				{
					this.IntValue = (this.IntValue * 10) + 7;
					break;
				}
				case '8':
				{
					this.IntValue = (this.IntValue * 10) + 8;
					break;
				}
				case '9':
				{
					this.IntValue = (this.IntValue * 10) + 9;
					break;
				}
				case ' ':
				case '\t':
				case '\r':
				case '\n':
				{
					break Loop;
				}
				default:
				{
					this.recoverFromBadWholeNumberParse(CharCurr, in);
					return;
				}
			}
		}
	
		this.parsedType = VarTypes.Integer;
	}
	*/

	/**
	 * kept as reference - only parses Boolean and Reference
	 * 
	 * @param Char0
	 * @param in
	 */
	/*
	private final void parseAsBool(char Char0, BytesStreamer in)
	{
		switch (Char0)
		{
			case 'f':
			case 'F':
			{
				this.parseAsBool_false(Char0, in);
				return;
			}
			case 't':
			case 'T':
			{
				this.parseAsBool_true(Char0, in);
				return;
			}
			default:
			{
				this.parseAsRef(Char0, in);
				break;
			}
		}
	}
	*/

	/**
	 * kept for reference - only parses Boolean_false and Reference
	 * 
	 * @param Char0
	 * @param in
	 */
	private final void parseAsBool_false(char Char0, BytesStreamer in)
	{
		if (!in.hasNextByte())
		{
			this.parseAsRef(Char0, in);
			return;
		}

		final char Char1 = in.getNextChar();

		if (Char1 != 'a' && Char1 != 'A')
		{
			this.stringValue.add(Char0);
			this.parseAsRef(Char1, in);
			return;
		}
		else if (!in.hasNextByte())
		{
			this.stringValue.add(Char0);
			this.parseAsRef('a', in);
			return;
		}

		final char Char2 = in.getNextChar();

		if (Char2 != 'l' && Char2 != 'L')
		{
			this.stringValue.add(Char0);
			this.stringValue.add('a');
			this.parseAsRef(Char2, in);
			return;
		}
		else if (!in.hasNextByte())
		{
			this.stringValue.add(Char0);
			this.stringValue.add(BoolParsing_false_al);
			this.parseAsRef(in);
			return;
		}

		final char Char3 = in.getNextChar();

		if (Char3 != 's' && Char3 != 'S')
		{
			this.stringValue.add(Char0);
			this.stringValue.add(BoolParsing_false_al);
			this.parseAsRef(Char3, in);
			return;
		}
		else if (!in.hasNextByte())
		{
			this.stringValue.add(Char0);
			this.stringValue.add(BoolParsing_false_als);
			this.parseAsRef(in);
			return;
		}

		final char Char4 = in.getNextChar();

		if (Char4 != 'e' && Char4 != 'E')
		{
			this.stringValue.add(Char0);
			this.stringValue.add(BoolParsing_false_als);
			this.parseAsRef(Char4, in);
			return;
		}
		else if (in.hasNextByte())
		{
			final char Char5 = in.getNextChar();

			switch (Char5)
			{
				case ' ':
				case '\t':
				case '\r':
				case '\n':
				{
					this.parsedType = VarTypes.Boolean;
					break;
				}
				case ',':
				{
					this.parsedType = VarTypes.Boolean_withComma;
					break;
				}
				case '}':
				case ']':
				{
					this.parsedType = VarTypes.LastElement_Boolean;
					break;
				}
				default:
				{
					this.stringValue.add(Char0);
					this.stringValue.add(BoolParsing_false_alse);
					this.parseAsRef(Char5, in);
					return;
				}
			}
		}

		// successful parse - Boolean - false
		this.BoolValue = false;
		return;
	}

	/**
	 * only parses Boolean_true and Reference
	 * 
	 * @param Char0
	 * @param in
	 */
	private final void parseAsBool_true(char Char0, BytesStreamer in)
	{
		if (!in.hasNextByte())
		{
			this.parseAsRef(Char0, in);
			return;
		}

		final char Char1 = in.getNextChar();

		if (Char1 != 'r' && Char1 != 'R')
		{
			this.stringValue.add(Char0);
			this.parseAsRef(Char1, in);
			return;
		}
		else if (!in.hasNextByte())
		{
			this.stringValue.add(Char0);
			this.parseAsRef('r', in);
			return;
		}

		final char Char2 = in.getNextChar();

		if (Char2 != 'u' && Char2 != 'U')
		{
			this.stringValue.add(Char0);
			this.stringValue.add('r');
			this.parseAsRef(Char2, in);
			return;
		}
		else if (!in.hasNextByte())
		{
			this.stringValue.add(Char0);
			this.stringValue.add(BoolParsing_true_ru);
			this.parseAsRef(in);
			return;
		}

		final char Char3 = in.getNextChar();

		if (Char3 != 'e' && Char3 != 'E')
		{
			this.stringValue.add(Char0);
			this.stringValue.add(BoolParsing_true_ru);
			this.parseAsRef(Char3, in);
			return;
		}
		else if (in.hasNextByte())
		{
			final char Char4 = in.getNextChar();

			switch (Char4)
			{
				case ' ':
				case '\t':
				case '\r':
				case '\n':
				{
					this.parsedType = VarTypes.Boolean;
					break;
				}
				case ',':
				{
					this.parsedType = VarTypes.Boolean_withComma;
					break;
				}
				case '}':
				case ']':
				{
					this.parsedType = VarTypes.LastElement_Boolean;
					break;
				}
				default:
				{
					this.stringValue.add(Char0);
					this.stringValue.add(BoolParsing_true_rue);
					this.parseAsRef(Char4, in);
					return;
				}
			}
		}

		// successful parse - Boolean - true
		this.BoolValue = true;
		return;
	}

	private final void parseAsNull(BytesStreamer in)
	{
		if (!in.hasNextByte())
		{
			this.parseAsRef('n', in);
			return;
		}

		final char Char1 = in.getNextChar();

		if (Char1 != 'u')
		{
			this.stringValue.add('n');
			this.parseAsRef(Char1, in);
			return;
		}
		else if (!in.hasNextByte())
		{
			this.stringValue.add(NullParsing_nu);
			this.parseAsRef(in);
			return;
		}

		final char Char2 = in.getNextChar();

		if (Char2 != 'l')
		{
			this.stringValue.add(NullParsing_nu);
			this.parseAsRef(Char2, in);
			return;
		}
		else if (!in.hasNextByte())
		{
			this.stringValue.add(NullParsing_nul);
			this.parseAsRef(in);
			return;
		}

		final char Char3 = in.getNextChar();

		if (Char3 != 'l')
		{
			this.stringValue.add(NullParsing_nul);
			this.parseAsRef(Char3, in);
			return;
		}
		else if (in.hasNextByte())
		{
			final char Char4 = in.getNextChar();

			switch (Char4)
			{
				case ' ':
				case '\t':
				case '\r':
				case '\n':
				{
					this.parsedType = VarTypes.NullReference;
					break;
				}
				case ',':
				{
					this.parsedType = VarTypes.NullReference_withComma;
					break;
				}
				case '}':
				case ']':
				{
					this.parsedType = VarTypes.LastElement_NullReference;
					break;
				}
				default:
				{
					this.stringValue.add(NullParsing_null);
					this.parseAsRef(Char4, in);
					return;
				}
			}
		}

		// successful parse - Null Reference
		return;
	}

	private final void parseAsRef(char Char0, BytesStreamer in)
	{
		// System.out.println("broke on char: " + Char0);
		this.stringValue.add(Char0);
		this.parseAsRef(in);
	}

	private final void parseAsRef(BytesStreamer in)
	{
		boolean braceOverride_square = false;
		boolean braceOverride_curly = false;

		while (in.hasNextByte())
		{
			final char curr = in.getNextChar();

			switch (curr)
			{
				case '\\':
				{
					this.stringValue.add(curr);
					this.stringValue.add(in.getNextChar());
					break;
				}
				case ' ':
				case '\t':
				case '\r':
				case '\n':
				{
					this.parsedType = VarTypes.Reference;
					return;
				}
				case '=':
				{
					if (this.stringValue.size() == 0)
					{
						throw new IllegalArgumentException();
					}

					this.parsedType = VarTypes.Name;
					// this.stringValue.clear();
					return;
				}
				case ',':
				{
					this.parsedType = VarTypes.Reference_withComma;
					return;
				}
				case '}':
				{
					if (braceOverride_curly)
					{
						this.stringValue.add(curr);
						braceOverride_curly = false;
						break;
					}
					else
					{
						this.parsedType = VarTypes.LastElement_Reference;
						return;
					}
				}
				case ']':
				{
					if (braceOverride_square)
					{
						this.stringValue.add(curr);
						braceOverride_square = false;
						break;
					}
					else
					{
						this.parsedType = VarTypes.LastElement_Reference;
						return;
					}
				}
				case '{':
				{
					braceOverride_curly = true;
					this.stringValue.add(curr);
					break;
				}
				case '[':
				{
					braceOverride_square = true;
					this.stringValue.add(curr);
					break;
				}
				default:
				{
					this.stringValue.add(curr);
					break;
				}
			}
		}
	}

	@Override
	public final String toString()
	{
		final CharList result = new CharList();

		result.add("{parsed type: ");
		result.add(this.parsedType.toString());
		result.add(", BoolValue: ");
		result.addAsString(this.BoolValue);
		result.add(", IntValue: ");
		result.add_asDecString(this.IntValue);
		result.add(", FloatValue: ");
		result.add_asDecString(this.FloatValue);
		result.add(", StringValue: ");
		result.add(this.stringValue, false);
		result.add('}');

		return result.toString();
	}

	public static enum VarTypes
	{
		Boolean,
		Integer,
		Float,
		String,
		Reference,
		NullReference,
		Name,
		EmptyString,
		EmptyArray,

		Boolean_withComma,
		Integer_withComma,
		Float_withComma,
		EmptyString_withComma,
		String_withComma,
		Reference_withComma,
		NullReference_withComma,

		LastElement_Boolean,
		LastElement_Integer,
		LastElement_Float,
		LastElement_String,
		LastElement_EmptyString,
		LastElement_Reference,
		LastElement_NullReference,;
	}
}