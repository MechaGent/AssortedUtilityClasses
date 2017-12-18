package Collections.Lists.CharList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import Collections.Lists.CharList.BaseCharList.CharListIterator;
import Collections.Lists.CharList.CharListTester_Performance.IgnoreStates;
import CustomExceptions.DecheckedException;
import CustomExceptions.UnhandledEnumException;
import HandyStuff.FileParser;
import HandyStuff.ArrayStuff.MiscArrayStuff;
import HandyStuff.ArrayStuff.PrimArraysCopier;
import HandyStuff.Randomness.Generators;
import HandyStuff.Randomness.XorShiftStar;
import HandyStuff.Randomness.XorShiftStar1024;

final class Tests
{
	public static void main(String[] args)
	{
		// test_addChar();
		// test_addChar_repeating();
		// test_addCharArr();
		// test_addCharArr_repeating();
		// test_addString_repeating();
		// test_assortment();
		// test_assortment_boundByCharCount();
		// benchmark_stringBuilder();
		// test_pushChar();
		// test_pushChar_repeating();
		// test_pushCharArr();
		// test_pushString();
		// test_pushCharArr_repeating();
		// test_fillRangeIsWorking();
		// test_pushString_repeating();
		// test_CharListIterator();
		// test_addStringArr();
		test_CharListIteratorPosSetter();
	}

	static final void test_CharListIteratorPosSetter()
	{
		final CharListTester_Behavior tester = getDefaultTester(100);
		//tester.printResults();

		final XorShiftStar1024 randy = AssortmentSampler.getDefaultGenerator();
		final char[] petrifiedControl = tester.petrifyControl();
		final CharListIterator itsy = tester.listIterator();
		final int testIndex = randy.nextInt((int) tester.size());
		
		
		itsy.addToPosition(testIndex);
		
		for(int i = testIndex; i < petrifiedControl.length; i+=1)
		{
			final char itsyOut = itsy.next();
			
			if(petrifiedControl[i] != itsyOut)
			{
				throw new IllegalArgumentException();
			}
		}
		
		System.out.println("forward-skip test successful!");
	}

	static final void test_addStringArr()
	{
		final XorShiftStar1024 randy = AssortmentSampler.getDefaultGenerator();
		final ArrayList<String> rawSamples = new ArrayList<String>();
		final int maxNumCharsPerString = 10;
		final int maxNumChars = 100;
		int CharCount = 0;

		while (CharCount < maxNumChars)
		{
			final char[] locRaw = new char[randy.nextInt(maxNumCharsPerString)];

			for (int i = 0; i < locRaw.length; i += 1)
			{
				locRaw[i] = randy.nextAlphaNumericCharacter();
			}

			rawSamples.add(new String(locRaw));
			CharCount += locRaw.length;
		}

		final String[] samples = rawSamples.toArray(new String[rawSamples.size()]);

		final CharListTester_Behavior tester = new CharListTester_Behavior();

		tester.add(samples);

		tester.printResults();
	}

	static final void test_CharListIterator()
	{
		final CharListTester_Behavior tester = getDefaultTester(100);

		final CharListIterator itsy = tester.listIterator();
		final BaseCharList forward = new BaseCharList();
		final BaseCharList backward = new BaseCharList();

		while (itsy.hasNext())
		{
			forward.add(itsy.next());
		}

		while (itsy.hasPrevious())
		{
			backward.push(itsy.previous());
		}

		final String forString = forward.toString();
		final String backString = backward.toString();

		CharListTester_Behavior.printResults(" forString", forString, "backString", backString);
	}

	private static final CharListTester_Behavior getDefaultTester(int numMethodCalls)
	{
		final AssortmentSampler sampler = AssortmentSampler.getDefaultInstance();
		final CharListTester_Behavior tester = new CharListTester_Behavior();

		for (int i = 0; i < numMethodCalls; i++)
		{
			sampler.doRandomMethod(tester);
		}

		return tester;
	}

	static final void test_pushString_repeating()
	{
		final String testSeq = "aBcDe";
		final int minNumChars = 68;
		final int numRepeats = 2;

		final CharListTester_Behavior tester = new CharListTester_Behavior();

		while (tester.size() < minNumChars)
		{
			tester.push(testSeq, numRepeats);
		}

		tester.printResults();
	}

	static final void test_fillRangeIsWorking()
	{
		final char[] testSeq = "aBcDe".toCharArray();
		final int numRepeats = 2;
		final char[] result = new char[(testSeq.length * numRepeats) + 1];

		PrimArraysCopier.fillRange(result, 1, testSeq, numRepeats);

		System.out.println("result: " + (new String(result)) + "\r\nof length: " + result.length);
	}

	static final void test_pushCharArr_repeating()
	{
		final char[] testSeq = "aBcDe".toCharArray();
		final int minNumChars = 68;
		final int numRepeats = 4;

		final CharListTester_Behavior tester = new CharListTester_Behavior();

		while (tester.size() < minNumChars)
		{
			tester.push(testSeq, numRepeats);
		}

		tester.printResults();
	}

	static final void test_pushString()
	{
		final String testSeq = "aBcDe";
		final int minNumChars = 68;

		final CharListTester_Behavior tester = new CharListTester_Behavior();

		while (tester.size() < minNumChars)
		{
			tester.push(testSeq);
		}

		tester.printResults();
	}

	static final void test_pushCharArr()
	{
		final char[] testSeq = "aBcDe".toCharArray();
		final int minNumChars = 68;

		final CharListTester_Behavior tester = new CharListTester_Behavior();

		while (tester.size() < minNumChars)
		{
			tester.push(testSeq);
		}

		tester.printResults();
	}

	static final void test_pushChar_repeating()
	{
		final char[] testSeq = "aBcDe".toCharArray();
		final int minNumChars = 68;
		final int numRepeats = 4;

		final CharListTester_Behavior tester = new CharListTester_Behavior();

		int index = 0;

		while (tester.size() < minNumChars)
		{
			tester.push(testSeq[(index++) % testSeq.length], numRepeats);
		}

		tester.printResults();
	}

	static final void test_pushChar()
	{
		final CharListTester_Behavior tester = new CharListTester_Behavior();

		int count = 0;

		while (count < 68)
		{
			tester.push('a');
			tester.push('B');
			tester.push('c');
			tester.push('D');
			count += 4;
		}

		tester.printResults();
	}

	/*
	 * 	ignoreState: ignoreVariable
			numOperations: 1000000
				average add time: 121.711697, 153.017612, 120.642745, 120.337027
			numOperations: 10000000
				average add time: 82.6303016, 83.0121004, 82.9327352, 84.0107211, 84.1639761
		
		ignoreState: ignoreControl
			numOperations: 1000000
				average add time: 182.46853, 207.229323, 164.332861, 200.082194
			numOperations: 10000000
				average add time: 197.1244176, 206.2431745, 196.0486522, 196.9675731, 199.736443
	 */
	static final void benchmark_stringBuilder()
	{
		final AssortmentSampler sampler = AssortmentSampler.getDefaultInstance();
		final boolean boundByNumChars = true;
		final long numCharsMin = 100000;
		// Integer.MAX_VALUE + 100; //TODO: fix so that this can be properly displayed

		final int numOpsMin = 10_000_000;
		int numOperations = 0;
		final IgnoreStates ignoreState = IgnoreStates.ignoreControl;

		final long startTime = System.nanoTime();

		final CharListTester_Performance tester = new CharListTester_Performance(ignoreState);

		if (boundByNumChars)
		{
			while (tester.size() < numCharsMin)
			{
				sampler.doRandomMethod(tester);
				numOperations += 1;
			}
		}
		else
		{
			while (numOperations < numOpsMin)
			{
				sampler.doRandomMethod(tester);
				numOperations += 1;
			}
		}

		final long endOpTime = System.nanoTime();

		final String output = tester.toString();

		final long timeDif_op = endOpTime - startTime;
		final double avgOpTime = ((double) timeDif_op) / numOperations;
		final char designatedChar = output.charAt(output.length() / 2); // this, hopefully, forces tester.toString() to not be optimized out

		System.out.println("ignoreState: " + ignoreState.toString() + "\r\n\tnumOperations: " + numOperations + "\r\n\tnumChars: " + output.length() + "\r\n\taverage add time: " + avgOpTime + "\r\n\tdesignatedChar: " + designatedChar);
	}

	static final void test_assortment_boundByCharCount()
	{
		final AssortmentSampler sampler = AssortmentSampler.getDefaultInstance();
		final CharListTester_Performance tester = new CharListTester_Performance();
		final int numCharsMin = 100000;

		while (tester.size() < numCharsMin)
		{
			sampler.doRandomMethod(tester);
		}

		System.out.println("isUsed:\r\n\t" + Arrays.toString(sampler.isUsed));
		tester.printResults();
	}

	static final void test_assortment()
	{
		final AssortmentSampler sampler = AssortmentSampler.getDefaultInstance();
		final CharListTester_Performance tester = new CharListTester_Performance();
		final int numMethodCalls = 100;

		for (int i = 0; i < numMethodCalls; i++)
		{
			sampler.doRandomMethod(tester);
		}

		System.out.println("isUsed:\r\n\t" + Arrays.toString(sampler.isUsed));

		tester.printResults();
	}

	static final void test_addString_repeating()
	{
		final String testSeq = "aBcDe";
		final int minNumChars = 68;
		final int numRepeats = 4;

		final CharListTester tester = new CharListTester_Behavior();

		while (tester.size() < minNumChars)
		{
			tester.add(testSeq, numRepeats);
		}

		tester.printResults();
	}

	static final void test_addCharArr_repeating()
	{
		final char[] testSeq = "aBcDe".toCharArray();

		final CharListTester_Performance tester = new CharListTester_Performance();

		int count = 0;

		while (count < 15)
		{
			tester.add(testSeq, 5);
			count++;
		}

		tester.printResults();
	}

	static final void test_addCharArr()
	{
		final char[] testSeq = "aBcDe".toCharArray();

		final CharListTester_Performance tester = new CharListTester_Performance();

		int count = 0;

		while (count < 15)
		{
			tester.add(testSeq);
			count++;
		}

		tester.printResults();
	}

	static final void test_addChar_repeating()
	{
		final char[] testSeq = "aBcDe".toCharArray();
		final int numRepeats = (70 / testSeq.length) + 1;

		final CharListTester_Performance tester = new CharListTester_Performance();

		int count = 0;
		int index = 0;

		while (count < 15)
		{
			tester.add(testSeq[(index++) % testSeq.length], numRepeats);
			count++;
		}

		tester.printResults();
	}

	static final void test_addChar()
	{
		final CharListTester_Performance tester = new CharListTester_Performance();

		int count = 0;

		while (count < 68)
		{
			tester.add('a');
			tester.add('B');
			tester.add('c');
			tester.add('D');
			count += 4;
		}

		tester.printResults();
	}

	private static final class AssortmentSampler
	{
		private static final int maxCaseNum_performance = 5;
		private static final int maxCaseNum_behavior = 5;

		private final XorShiftStar1024 randy;
		private final boolean[] isUsed;
		private final char[] CharSamples;
		private final char[] RepeatCharSamples;
		private final char[][] CharArrSamples;
		private final char[][] RepeatCharArrSamples;
		private final String[] StringSamples;
		private final String[] RepeatStringSamples;

		public AssortmentSampler(XorShiftStar1024 inRandy, char[] inCharSamples, char[] inRepeatCharSamples, char[][] inCharArrSamples, char[][] inRepeatCharArrSamples, String[] inStringSamples, String[] inRepeatStringSamples)
		{
			this.randy = inRandy;
			this.isUsed = new boolean[maxCaseNum_performance + 1];
			this.CharSamples = inCharSamples;
			this.RepeatCharSamples = inRepeatCharSamples;
			this.CharArrSamples = inCharArrSamples;
			this.RepeatCharArrSamples = inRepeatCharArrSamples;
			this.StringSamples = inStringSamples;
			this.RepeatStringSamples = inRepeatStringSamples;
		}

		public static final AssortmentSampler getDefaultInstance()
		{
			final XorShiftStar1024 randy = getDefaultGenerator();
			final char[] CharSamples = "aEiOu".toCharArray();
			final char[] RepeatCharSamples = "AeIoU".toCharArray();
			final char[][] CharArrSamples = new char[][] {
															"testVal_charArr_0".toCharArray(),
															"testVal_charArr_00".toCharArray(),
															"testVal_charArr_000".toCharArray() };

			final char[][] RepeatCharArrSamples = new char[CharArrSamples.length][];

			for (int i = 0; i < CharArrSamples.length; i += 1)
			{
				RepeatCharArrSamples[i] = MiscArrayStuff.getInvertedCaseCopy(CharArrSamples[i]);
			}

			final String[] StringSamples = new String[] {
															"testVal_string_0",
															"testVal_string_00",
															"testVal_string_000" };

			final String[] RepeatStringSamples = new String[StringSamples.length];

			for (int i = 0; i < RepeatStringSamples.length; i += 1)
			{
				RepeatStringSamples[i] = MiscArrayStuff.getInvertedCaseCopy(StringSamples[i]);
			}

			return new AssortmentSampler(randy, CharSamples, RepeatCharSamples, CharArrSamples, RepeatCharArrSamples, StringSamples, RepeatStringSamples);
		}

		public static final XorShiftStar1024 getDefaultGenerator()
		{
			final long[] currentSeed = new long[] {
													-6352897202794084187L,
													-3741150689060158469L,
													3438719988986086281L,
													8653003299751606628L,
													-1341617352925180321L,
													8627788452605982709L,
													-7815326744549865769L,
													6083611603246758416L,
													-5122172436599972157L,
													1302269772471497484L,
													3695263493707629984L,
													-4635951172366051115L,
													-1348085446720703356L,
													2900274629210557854L,
													2527531007783226389L,
													-1909011710945773568L };

			return (XorShiftStar1024) XorShiftStar.getNewInstance(Generators.XorShiftStar1024, currentSeed);
		}

		private static final int maxNumRepeats = 5;

		public final void doRandomMethod(CharListTester_Performance tester)
		{
			final int currCase = this.randy.nextInt(maxCaseNum_performance);
			final int sampleCase;

			switch (currCase)
			{
				case 0: // single char
				{
					sampleCase = this.randy.nextInt(this.CharSamples.length - 1);
					final char sample = this.CharSamples[sampleCase];
					tester.add(sample);
					this.isUsed[0] = true;
					break;
				}
				case 1: // single char, repeated
				{
					sampleCase = this.randy.nextInt(this.CharSamples.length - 1);
					final char sample = this.CharSamples[sampleCase];
					final int numRepeats = this.randy.nextInt(maxNumRepeats);
					tester.add(sample, numRepeats);
					this.isUsed[1] = true;
					break;
				}
				case 2: // char arr
				{
					sampleCase = this.randy.nextInt(this.CharArrSamples.length - 1);
					final char[] sample = this.CharArrSamples[sampleCase];
					tester.add(sample);
					this.isUsed[2] = true;
					break;
				}
				case 3: // char arr, repeated
				{
					sampleCase = this.randy.nextInt(this.CharArrSamples.length - 1);
					final char[] sample = this.CharArrSamples[sampleCase];
					final int numRepeats = this.randy.nextInt(maxNumRepeats);
					tester.add(sample, numRepeats);
					this.isUsed[3] = true;
					break;
				}
				case 4: // string
				{
					sampleCase = this.randy.nextInt(this.StringSamples.length - 1);
					final String sample = this.StringSamples[sampleCase];
					tester.add(sample);
					this.isUsed[4] = true;
					break;
				}
				case 5: // string, repeated
				{
					sampleCase = this.randy.nextInt(this.StringSamples.length - 1);
					final String sample = this.StringSamples[sampleCase];
					final int numRepeats = this.randy.nextInt(maxNumRepeats);
					tester.add(sample, numRepeats);
					this.isUsed[5] = true;
					break;
				}
				default:
				{
					throw new UnhandledEnumException(currCase);
				}
			}
		}

		public final void doRandomMethod(CharListTester_Behavior tester)
		{
			final int currCase = this.randy.nextInt(maxCaseNum_behavior);
			final boolean isAdd = true;
			// this.randy.flipCoin();
			final int sampleCase;

			switch (currCase)
			{
				case 0: // single char
				{
					sampleCase = this.randy.nextInt(this.CharSamples.length - 1);
					final char sample = this.CharSamples[sampleCase];

					if (isAdd)
					{
						tester.add(sample);
					}
					else
					{
						tester.push(sample);
					}

					this.isUsed[0] = true;
					break;
				}
				case 1: // single char, repeated
				{
					sampleCase = this.randy.nextInt(this.CharSamples.length - 1);
					final char sample = this.RepeatCharSamples[sampleCase];
					final int numRepeats = this.randy.nextInt(maxNumRepeats);

					if (isAdd)
					{
						tester.add(sample, numRepeats);
					}
					else
					{
						tester.push(sample, numRepeats);
					}

					this.isUsed[1] = true;
					break;
				}
				case 2: // char arr
				{
					sampleCase = this.randy.nextInt(this.CharArrSamples.length - 1);
					final char[] sample = this.CharArrSamples[sampleCase];

					if (isAdd)
					{
						tester.add(sample);
					}
					else
					{
						tester.push(sample);
					}

					this.isUsed[2] = true;
					break;
				}
				case 3: // char arr, repeated
				{
					sampleCase = this.randy.nextInt(this.CharArrSamples.length - 1);
					final char[] sample = this.RepeatCharArrSamples[sampleCase];
					final int numRepeats = this.randy.nextInt(maxNumRepeats);

					if (isAdd)
					{
						tester.add(sample, numRepeats);
					}
					else
					{
						tester.push(sample, numRepeats);
					}

					this.isUsed[3] = true;
					break;
				}
				case 4: // string
				{
					sampleCase = this.randy.nextInt(this.StringSamples.length - 1);
					final String sample = this.StringSamples[sampleCase];

					if (isAdd)
					{
						tester.add(sample);
					}
					else
					{
						tester.push(sample);
					}

					this.isUsed[4] = true;
					break;
				}
				case 5: // string, repeated
				{
					sampleCase = this.randy.nextInt(this.StringSamples.length - 1);
					final String sample = this.RepeatStringSamples[sampleCase];
					final int numRepeats = this.randy.nextInt(maxNumRepeats);

					if (isAdd)
					{
						tester.add(sample, numRepeats);
					}
					else
					{
						tester.push(sample, numRepeats);
					}

					this.isUsed[5] = true;
					break;
				}
				default:
				{
					throw new UnhandledEnumException(currCase);
				}
			}

			//System.out.println("finished case: " + currCase);
			
			//tester.checkVariable_checkActiveStates(currCase);
		}

		public final void printSeed()
		{
			System.out.println(Arrays.toString(this.randy.getSeed()));
		}
	}
}
