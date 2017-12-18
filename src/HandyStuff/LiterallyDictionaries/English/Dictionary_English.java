package HandyStuff.LiterallyDictionaries.English;

import java.util.Arrays;

import HandyStuff.FileParser;
import HandyStuff.Randomness.Generators;
import HandyStuff.Randomness.XorShiftStar1024;
import HandyStuff.Randomness.XorShiftStar;

public final class Dictionary_English
{
	private static final String dictPath = "/src/HandyStuff/LiterallyDictionaries/English/rawDictionary.txt";

	private final String[] core;
	private final int[] charToc;
	private final XorShiftStar1024 randy;

	private Dictionary_English(String[] inCore, int[] inCharToc, XorShiftStar1024 inRandy)
	{
		this.core = inCore;
		this.charToc = inCharToc;
		this.randy = inRandy;
	}

	public static final Dictionary_English getInstance()
	{
		return getInstance((XorShiftStar1024) XorShiftStar.getNewInstance(Generators.XorShiftStar1024));
	}

	public static final Dictionary_English getInstance(XorShiftStar1024 randy)
	{
		final String[] core = FileParser.parseFileAsStringLines(FileParser.getAbsPathToWorkingDirectory() + dictPath);
		final int[] toc = initCharToc(core);
		return new Dictionary_English(core, toc, randy);
	}
	
	public static enum RandomWordsModes
	{
		RollLetter_RollWord,
		RollLetter_RollWord_ThenAlpha,
		RollWord,
		RollWord_ThenAlpha;
	}

	public final String[] getRandomWords(int numWords)
	{
		return getRandomWords(numWords, XorShiftStar.getNewInstance(Generators.XorShiftStar1024));
	}
	
	public final String[] getRandomWords(int numWords, XorShiftStar rng)
	{
		final String[] result = new String[numWords];
		final int length = result.length;

		for (int i = 0; i < length; i++)
		{
			result[i] = this.core[rng.nextInt(this.core.length)];
		}

		return result;
	}

	public final String getRandomBoundedWord(char lower)
	{
		return getRandomBoundedWord(lower, lower, true);
	}

	public final String getRandomBoundedWord(char lower, char upper, boolean upperIsInclusive)
	{
		final int lBounds = getStartingIndex(lower);
		final int uBounds;

		if (upperIsInclusive)
		{
			uBounds = getStartingIndex((char) (upper + 1));
		}
		else
		{
			uBounds = getStartingIndex(upper);
		}

		return this.core[randy.nextInt(lBounds, uBounds)];
	}

	public final String getRandomWord(RandomWordsModes mode)
	{
		final String result;

		switch (mode)
		{
			case RollLetter_RollWord:
			case RollLetter_RollWord_ThenAlpha:
			{
				final char letter = tocIndexToChar(randy.nextInt(25));
				result = getRandomBoundedWord(letter);
				break;
			}
			case RollWord:
			case RollWord_ThenAlpha:
			{
				result = this.core[randy.nextInt(this.core.length-1)];
				break;
			}
			default:
			{
				throw new IllegalArgumentException();
			}
		}

		return result;
	}

	public final String[] getRandomWords(int numWords, RandomWordsModes mode)
	{
		final String[] result = new String[numWords];
		final int length = result.length;

		for (int i = 0; i < length; i++)
		{
			result[i] = getRandomWord(mode);
		}

		switch (mode)
		{
			case RollLetter_RollWord_ThenAlpha:
			case RollWord_ThenAlpha:
			{
				Arrays.sort(result);
				break;
			}
			default:
			{
				break;
			}
		}

		return result;
	}

	private static int[] initCharToc(String[] inDictionary)
	{
		final int[] result = new int[26];
		int index = 0;

		// loops from letter a to letter z
		for (char i = 0x61; i < 0x7b; i++)
		{
			while ((index < inDictionary.length) && (Character.toLowerCase(inDictionary[index].charAt(0)) != i))
			{
				index++;
			}

			// System.out.println(i + ": " + inDictionary[index]);
			result[charToTocIndex(i)] = index;
			index++;
		}

		return result;
	}
	
	private final int getStartingIndex(char inIn)
	{
		final int result;
		final char in = Character.toLowerCase(inIn);

		if (in > 96)
		{
			if (in < 123)
			{
				result = this.charToc[charToTocIndex(in)];
			}
			else
			{
				result = this.core.length;
			}
		}
		else
		{
			result = 0;
		}
		
		return result;
	}

	private static final int charToTocIndex(char in)
	{
		final int result;

		switch (Character.toLowerCase(in))
		{
			case 'a':
			{
				result = 0;
				break;
			}
			case 'b':
			{
				result = 1;
				break;
			}
			case 'c':
			{
				result = 2;
				break;
			}
			case 'd':
			{
				result = 3;
				break;
			}
			case 'e':
			{
				result = 4;
				break;
			}
			case 'f':
			{
				result = 5;
				break;
			}
			case 'g':
			{
				result = 6;
				break;
			}
			case 'h':
			{
				result = 7;
				break;
			}
			case 'i':
			{
				result = 8;
				break;
			}
			case 'j':
			{
				result = 9;
				break;
			}
			case 'k':
			{
				result = 10;
				break;
			}
			case 'l':
			{
				result = 11;
				break;
			}
			case 'm':
			{
				result = 12;
				break;
			}
			case 'n':
			{
				result = 13;
				break;
			}
			case 'o':
			{
				result = 14;
				break;
			}
			case 'p':
			{
				result = 15;
				break;
			}
			case 'q':
			{
				result = 16;
				break;
			}
			case 'r':
			{
				result = 17;
				break;
			}
			case 's':
			{
				result = 18;
				break;
			}
			case 't':
			{
				result = 19;
				break;
			}
			case 'u':
			{
				result = 20;
				break;
			}
			case 'v':
			{
				result = 21;
				break;
			}
			case 'w':
			{
				result = 22;
				break;
			}
			case 'x':
			{
				result = 23;
				break;
			}
			case 'y':
			{
				result = 24;
				break;
			}
			case 'z':
			{
				result = 25;
				break;
			}
			default:
			{
				throw new IllegalArgumentException("bad char: " + in);
			}
		}

		return result;
	}
	
	private static final char tocIndexToChar(int index)
	{
		final char result;

		switch (index)
		{
			case 0:
			{
				result = 'a';
				break;
			}
			case 1:
			{
				result = 'b';
				break;
			}
			case 2:
			{
				result = 'c';
				break;
			}
			case 3:
			{
				result = 'd';
				break;
			}
			case 4:
			{
				result = 'e';
				break;
			}
			case 5:
			{
				result = 'f';
				break;
			}
			case 6:
			{
				result = 'g';
				break;
			}
			case 7:
			{
				result = 'h';
				break;
			}
			case 8:
			{
				result = 'i';
				break;
			}
			case 9:
			{
				result = 'j';
				break;
			}
			case 10:
			{
				result = 'k';
				break;
			}
			case 11:
			{
				result = 'l';
				break;
			}
			case 12:
			{
				result = 'm';
				break;
			}
			case 13:
			{
				result = 'n';
				break;
			}
			case 14:
			{
				result = 'o';
				break;
			}
			case 15:
			{
				result = 'p';
				break;
			}
			case 16:
			{
				result = 'q';
				break;
			}
			case 17:
			{
				result = 'r';
				break;
			}
			case 18:
			{
				result = 's';
				break;
			}
			case 19:
			{
				result = 't';
				break;
			}
			case 20:
			{
				result = 'u';
				break;
			}
			case 21:
			{
				result = 'v';
				break;
			}
			case 22:
			{
				result = 'w';
				break;
			}
			case 23:
			{
				result = 'x';
				break;
			}
			case 24:
			{
				result = 'y';
				break;
			}
			case 25:
			{
				result = 'z';
				break;
			}
			default:
			{
				throw new IllegalArgumentException("bad index: " + index);
			}
		}

		return result;
	}
}
