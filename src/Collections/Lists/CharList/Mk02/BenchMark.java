package Collections.Lists.CharList.Mk02;

import Collections.PackedArrays.HalfByte.HalfByteArray;
import Collections.PackedArrays.HalfByte.HalfByteArrayFactory;
import HandyStuff.Randomness.Generators;
import HandyStuff.Randomness.XorShiftStar;

class BenchMark
{
	private static final char testChar = '@';
	private static final int numRepeats_Char = 10;

	private static final char[] testCharArr = "testCharArr".toCharArray();
	private static final int numRepeats_CharArr = 10;

	private static final String testString = "testString";
	private static final int numRepeats_String = 10;

	private static final HalfByteArray testHalfByteArray = HalfByteArrayFactory.wrapIntoArray("testHalfByteArray");

	// randomly generated
	private static final long[] seed = new long[] {
													7400776534575121807L,
													-7350818975994336107L,
													7370281990059586871L,
													-5599270475683248217L,
													-5656760521364369301L,
													-197069455807311027L,
													253055833779114960L,
													8792261750468674249L,
													6656212745237863579L,
													-5417852977900623787L,
													372231568736467945L,
													2273632547162773938L,
													589344982797789793L,
													-1947829366619723639L,
													362664856916187648L,
													6295190296046056416L };
	private static final XorShiftStar RNG = XorShiftStar.getNewInstance(Generators.XorShiftStar1024, seed);

	private static final int numReps = 10000;

	public static void main(String[] args)
	{
		//benchmark_StringBuilder();
		benchmark_CharList();
	}

	/*
	 * @numReps = 10,000
	 * 	average time spent: 881.1694
	 *	with string length of: 374058
	 *
	 *@numReps = 100,000
	 *	average time spent: 204.81631
	 *	with string length of: 3690261
	 *
	 *@numReps = 1,000,000
	 *	average time spent: 110.163974
	 *	with string length of: 36977026
	 *
	 *@numReps = 10,000,000	(threw an OutOfMemoryError)
	 *	average time spent: <unknown>
	 *	with string length of: <unknown>
	 */
	/*
	 * @numReps = 10,000
	 * 	average time per addOp: 384.3231
	 *	outputTime: 259,176
	 *	string length: 374058
	 *	total Runtime: 4,102,407
	 *	designatedChar: C
	 *
	 * @numReps = 100,000
	 * 	average time per addOp: 188.69675
	 *	outputTime: 3,252,784
	 *	string length: 3690261
	 *	total Runtime: 22,122,459
	 *	designatedChar: e
	 *
	 * @numReps = 1,000,000
	 * 	average time per addOp: 107.297454
	 *	outputTime: 29,339,241
	 *	string length: 36,977,026
	 *	total Runtime: 129,676,352
	 *	designatedChar: A
	 */
	static final void benchmark_StringBuilder()
	{
		final long startTime = System.nanoTime();
		final StringBuilder target = new StringBuilder();

		for (int i = numReps; i > 0; i--)
		{
			switch (RNG.nextInt(6))
			{
				case 0: // char
				{
					target.append(testChar);
					break;
				}
				case 1: // repChar
				{
					for(int j = numRepeats_Char; j > 0; j--)
					{
						target.append(testChar);
					}
					break;
				}
				case 2: // charArr
				{
					target.append(testCharArr);
					break;
				}
				case 3: // repCharArr
				{
					for(int j = numRepeats_CharArr; j > 0; j--)
					{
						target.append(testCharArr);
					}
					break;
				}
				case 4: // String
				{
					target.append(testString);
					break;
				}
				case 5: // repString
				{
					for(int j = numRepeats_String; j > 0; j--)
					{
						target.append(testString);
					}
					break;
				}
				case 6: // halfByteArray
				{
					target.append(testHalfByteArray.interpretAsCharString());
					break;
				}
				default:
				{
					throw new IndexOutOfBoundsException();
				}
			}
		}
		
		final long endAppendTime = System.nanoTime();

		final String output = target.toString();
		final char temp = output.charAt(target.length() / 2);
		
		final long endOutputTime = System.nanoTime();
		
		final double avgAppendTime = ((double) (endAppendTime - startTime)) / ((double) numReps);
		final long outputDelta = endOutputTime - endAppendTime;
		System.out.println("average time per addOp: " + avgAppendTime + "\r\noutputTime: " + outputDelta + "\r\nstring length: " + target.length() + "\r\ntotal Runtime: " + (endOutputTime - startTime) + "\r\ndesignatedChar: " + temp);
	}

	/*
	 * @numReps = 10,000
	 * 	average time spent: 1098.2753
	 *	with string length of: 374058
	 *
	 *@numReps = 100,000
	 *	average time spent: 191.09698
	 *	with string length of: 3690261
	 *
	 *@numReps = 1,000,000
	 *	average time spent: 45.927158
	 *	with string length of: 36977026
	 *
	 *@numReps = 10,000,000
	 *	average time spent: 234.5290533
	 *	with string length of: 370072440
	 *
	 *@numReps = 20,000,000
	 *	average time spent: 253.7732985
	 *	with string length of: 740174518
	 */
	/*
	 * @numReps = 10,000
	 * 	average time per addOp: 788.9058
	 * 	above / totalRuntime: 0.93685418813
	 *	outputTime: 531,738
	 *	above / totalRuntime: 0.06314581186
	 *	string length: 374,058
	 *	total Runtime: 8,420,796
	 *	designatedChar: 
	 *
	 * @numReps = 100,000
	 * 	average time per addOp: 158.68159
	 *	outputTime: 4,859,251
	 *	string length: 3,690,261
	 *	total runtime: 20,727,410
	 *	designatedChar: 
	 *
	 * @numReps = 1,000,000
	 * 	average time per addOp: 44.253435
	 *	outputTime: 47,108,634
	 *	string length: 36,977,026
	 *	total Runtime: 91,362,069
	 *	designatedChar: 
	 */
	static final void benchmark_CharList()
	{
		final long startAppendTime = System.nanoTime();
		final CharList target = new CharList();

		for (int i = numReps; i > 0; i--)
		{
			switch (RNG.nextInt(6))
			{
				case 0: // char
				{
					target.add(testChar);
					break;
				}
				case 1: // repChar
				{
					target.add(testChar, numRepeats_Char);
					break;
				}
				case 2: // charArr
				{
					target.add(testCharArr);
					break;
				}
				case 3: // repCharArr
				{
					target.add(testCharArr, numRepeats_CharArr);
					break;
				}
				case 4: // String
				{
					target.add(testString);
					break;
				}
				case 5: // repString
				{
					target.add(testString, numRepeats_String);
					break;
				}
				case 6: // halfByteArray
				{
					target.addAsString_ofChars(testHalfByteArray);
					break;
				}
				default:
				{
					throw new IndexOutOfBoundsException();
				}
			}
		}

		final long endAppendTime = System.nanoTime();
		
		final String output = target.toString();
		final char temp = output.charAt(target.size() / 2);
		
		final long endOutputTime = System.nanoTime();
		
		final double avgAppendTime = ((double) (endAppendTime - startAppendTime)) / ((double) numReps);
		final long outputDelta = endOutputTime - endAppendTime;
		System.out.println("average time per addOp: " + avgAppendTime + "\r\noutputTime: " + outputDelta + "\r\nstring length: " + target.size() + "\r\ntotal Runtime: " + (endOutputTime - startAppendTime) + "\r\ndesignatedChar: " + temp);
	}
}
