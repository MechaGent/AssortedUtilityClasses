package Collections.Maps.RadixMap.Linked.Generic.Mk01;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;

import Collections.Lists.CharList.CharList;
import Collections.PackedArrays.HalfByte.HalfByteArray;
import Collections.PackedArrays.HalfByte.HalfByteArrayFactory;
import HandyStuff.LiterallyDictionaries.English.Mk01.Dictionary_English;
import HandyStuff.Randomness.Generators;
import HandyStuff.Randomness.XorShiftStar;
import HandyStuff.Randomness.XorShiftStarFactory;

class tester
{
	private static final int[] addOffsets_Int = new int[] {
															Integer.MIN_VALUE,
															-65 };

	private static final int[] subOffsets_Int = new int[] {
															Integer.MAX_VALUE };

	private static final long[] addOffsets_Long = new long[] {
																Long.MIN_VALUE,
																Long.MIN_VALUE - 65 };

	private static final long[] subOffsets_Long = new long[] {
																Long.MAX_VALUE,
																Long.MAX_VALUE + 1 };

	public static void main(String[] args)
	{
		// test_PutMethod();
		// test_GetMethod();
		// test_heavyDutyStorage();
		// test_IntStorageOrder();
		//test_natOrderItsy_forInts();
		//test_natOrderItsy_forLongs();
		test_entryIterator();
		//test_backTraceBug();
		 //test_childMaskCollation();
	}
	
	static final void test_childMaskCollation()
	{
		final LinkedRadixMap<String> test;
		
		//test = getTestMap();
		test = getTestMap_fromDictionary(-1);
		
		final LinkedRadixMap<Integer> childrenMasks = test.collateAllChildrenMasks();
		final MaskCountPair[] entries = new MaskCountPair[childrenMasks.size()];
		final Iterator<Entry<HalfByteArray, Integer>> itsy = childrenMasks.entriesIterator();
		
		for(int i = 0; i < entries.length; i++)
		{
			entries[i] = new MaskCountPair(itsy.next());
		}
		
		Arrays.sort(entries);
		
		final int limit = Math.min(entries.length, 10);
		
		for(int i = 0; i < limit; i++)
		{
			System.out.println(entries[i].toString());
		}
	}
	
	private static class MaskCountPair implements Comparable<MaskCountPair>
	{
		private final int mask;
		private final int count;
		
		public MaskCountPair(Entry<HalfByteArray, Integer> in)
		{
			this(in.getKey().interpretAsInt(), in.getValue());
		}
		
		public MaskCountPair(int inMask, int inCount)
		{
			this.mask = inMask;
			this.count = inCount;
		}

		@Override
		public int compareTo(MaskCountPair inArg0)
		{
			//return this.count - inArg0.count;
			return inArg0.count - this.count;
		}

		@Override
		public String toString()
		{
			return '{' + Integer.toBinaryString(this.mask) + '(' + Integer.toHexString(this.mask) + "), " + this.count + '}';
		}
	}
	
	static final void test_backTraceBug()
	{
		final LinkedRadixMap<String> test = new LinkedRadixMap<String>();
		final int probInt = -2147483648;
		
		//result.put(probInt, Integer.toString(probInt));
		
		for(int i = 0; i < 2; i++)
		{
			test.put(i + probInt, Integer.toString(i + probInt));
		}
		
		//System.out.println(test.toHeirarchalCharList(0).toString());
		final CharList result = new CharList();

		for (Entry<HalfByteArray, String> entry : test)
		{
			result.add('{');
			result.add(entry.getKey().toCharList(), true);
			result.add(", ");
			result.add(entry.getValue());
			result.add('}');
			result.addNewLine();
		}

		System.out.println(result.toString());
	}

	static final void test_entryIterator()
	{
		final LinkedRadixMap<String> test = getTestMap();

		final CharList result = new CharList();

		for (Entry<HalfByteArray, String> entry : test)
		{
			final String key = entry.getKey().interpretAsCharString();
			final String value = entry.getValue();
			
			result.add('{');
			result.add(key);
			result.add(", ");
			result.add(value);
			result.add('}');
			result.addNewLine();
		}

		System.out.println(result.toString());
	}
	
	static final void test_natOrderItsy_forLongs()
	{
		final boolean includeInts = true;
		final boolean includeLongs = true;
		final LinkedRadixMap<String> test = generateTestMap_ofPrimKeys(includeInts, includeLongs);
		test.put("non-int key", "non-int value");
		final Iterator<Entry<HalfByteArray, String>> itsy;
		
		//itsy = test.naturallyOrderedEntriesIterator_forCleanKeys();
		//itsy = test.naturallyOrderedEntriesIterator_forDirtyLongKeys();
		itsy = test.naturallyOrderedEntriesIterator_forDirtyIntAndLongKeys();
		
		System.out.println(test.toHeirarchalCharList(0));
		
		final CharList result = new CharList();
		final long[] buffer = new long[2];
		int bufferIndex = 1;
		boolean isFirst = true;

		while (itsy.hasNext())
		{
			final Entry<HalfByteArray, String> entry = itsy.next();
			final HalfByteArray key = entry.getKey();
			
			if(isFirst)
			{
				isFirst = false;
				
				if(key.length() == 8)
				{
					buffer[0] = key.interpretAsInt();
				}
				else if(key.length() == 16)
				{
					buffer[0] = key.interpretAsLong();
				}
				else
				{
					throw new IllegalArgumentException();
				}
			}
			else
			{
				if(key.length() == 8)
				{
					buffer[bufferIndex] = key.interpretAsInt();
				}
				else if(key.length() == 16)
				{
					buffer[bufferIndex] = key.interpretAsLong();
				}
				else
				{
					throw new IllegalArgumentException();
				}
				
				if(buffer[bufferIndex] < buffer[bufferIndex ^ 1])
				{
					System.err.println("mismatch detected! elements: {" + buffer[bufferIndex] + ", " + buffer[bufferIndex ^ 1] + "}, from key " + key.toCharList(0).toString());
				}
				
				bufferIndex ^= 1;
			}

			/*
			result.add('{');
			result.add(key.toCharList(), true);
			result.add(", ");
			result.add(entry.getValue());
			result.add('}');
			result.addNewLine();
			*/
		}

		System.out.println(result.toString());
	}
	
	static final void test_natOrderItsy_forInts()
	{
		final boolean includeInts = true;
		final boolean includeLongs = false;
		final LinkedRadixMap<String> test = generateTestMap_ofPrimKeys(includeInts, includeLongs);
		final Iterator<Entry<HalfByteArray, String>> itsy;
		
		itsy = test.naturallyOrderedEntriesIterator_forCleanKeys();
		
		
		//System.out.println(test.toHeirarchalCharList(0));
		
		final CharList result = new CharList();
		final int[] buffer = new int[2];
		int bufferIndex = 1;
		boolean isFirst = true;

		while (itsy.hasNext())
		{
			final Entry<HalfByteArray, String> entry = itsy.next();
			final HalfByteArray key = entry.getKey();
			
			if(isFirst)
			{
				isFirst = false;
				buffer[0] = key.interpretAsInt();
			}
			else
			{
				buffer[bufferIndex] = key.interpretAsInt();
				
				if(buffer[bufferIndex] < buffer[bufferIndex ^ 1])
				{
					System.err.println("mismatch detected! elements: {" + buffer[bufferIndex] + ", " + buffer[bufferIndex ^ 1] + "}, from key " + key.toCharList(0).toString());
				}
				
				bufferIndex ^= 1;
			}

			/*
			result.add('{');
			result.add(key.toCharList(), true);
			result.add(", ");
			result.add(entry.getValue());
			result.add('}');
			result.addNewLine();
			*/
		}

		System.out.println(result.toString());
	}

	static final void test_IntStorageOrder()
	{
		final LinkedRadixMap<String> test = generateTestMap_ofPrimKeys(false, true);

		System.out.println(test.toHeirarchalCharList(0));
	}

	private static final LinkedRadixMap<String> generateTestMap_ofPrimKeys(boolean addInts, boolean addLongs)
	{
		final LinkedRadixMap<String> result = new LinkedRadixMap<String>();

		for (int i = 0; i <= 64; i++)
		{
			if (addInts)
			{
				result.put(i, "Int: " + Integer.toString(i));
				
				for (int j : addOffsets_Int)
				{
					final int val = j + i;
					result.put(val, "Int: " + Integer.toString(val));
				}

				for (int j : subOffsets_Int)
				{
					final int val = j - i;
					result.put(val, "Int: " + Integer.toString(val));
				}
			}

			if (addLongs)
			{
				result.put((long) i, "Long: " + Integer.toString(i));
				
				for (long j : addOffsets_Long)
				{
					final long val = j + i;
					result.put(val, "Long: " + Long.toString(val));
				}

				for (long j : subOffsets_Long)
				{
					final long val = j - i;
					result.put(val, "Long: " + Long.toString(val));
				}
			}
		}

		return result;
	}

	static final void test_heavyDutyStorage()
	{
		final int numPairs = 10000;
		final int keyLength = 32;

		final TestPair<String, String>[] tests = helper_test_heavyDutyStorage_generatePairs(numPairs, keyLength);
		final LinkedRadixMap<String> test = helper_test_heavyDutyStorage_generateMap(tests);
		final CharList result = new CharList();
		int failedTests = 0;

		for (int i = 0; i < numPairs; i++)
		{
			final TestPair<String, String> pair = tests[i];

			final String controlKey = pair.getKey();
			final String controlValue = pair.getValue();

			final String testValue = test.get(controlKey);

			if (!controlValue.equals(testValue))
			{
				failedTests++;
				result.add("Key pair {");
				result.add(controlKey);
				result.add(", ");
				result.add(controlValue);
				result.add("} failed! Retrieved value was ");

				if (testValue != null)
				{
					result.add(testValue);
				}
				else
				{
					result.add("<nullValue>");
				}

				result.addNewLine();
			}
		}

		result.add("Result: of ");
		result.add_asDecString(numPairs);
		result.add(" tests, ");
		result.add_asDecString(failedTests);
		result.add(" failed.");
		result.addNewLine();
		result.add("numEntries: ");
		result.add_asDecString(test.size());
		result.addNewLine(2);
		result.add(test.toHeirarchalCharList(0), true);

		System.out.println(result.toString());
	}

	private static final LinkedRadixMap<String> helper_test_heavyDutyStorage_generateMap(TestPair<String, String>[] tests)
	{
		final LinkedRadixMap<String> result = new LinkedRadixMap<String>();
		int i = 0;

		for (TestPair<String, String> entry : tests)
		{
			try
			{
				result.put(entry.getKey(), entry.getValue());
			}
			catch (NullPointerException e)
			{
				e.printStackTrace();

				throw new NullPointerException("Null Pointer exception on entry " + i + ": {" + entry.getKey() + ", " + entry.getValue() + "}");
			}
			i++;
		}

		return result;
	}

	private static final TestPair<String, String>[] helper_test_heavyDutyStorage_generatePairs(int numPairs, int keyLength)
	{
		final XorShiftStar randy = XorShiftStarFactory.getInstance_1024bit();

		@SuppressWarnings("unchecked")
		final TestPair<String, String>[] result = new TestPair[numPairs];

		for (int i = 0; i < numPairs; i++)
		{
			result[i] = new TestPair<String, String>(helper_test_heavyDutyStorage_generateString(randy, keyLength), "value_" + i);
		}

		return result;
	}

	private static final String helper_test_heavyDutyStorage_generateString(XorShiftStar randy, int keyLength)
	{
		final char[] result = new char[keyLength];

		for (int i = 0; i < keyLength; i++)
		{
			result[i] = (char) randy.nextInt('!', '~');
		}

		return new String(result);
	}

	static final void test_GetMethod()
	{
		final TestPair<String, String>[] tests = getTestPairs();
		final int numTestsMax = tests.length;

		final LinkedRadixMap<String> test = getTestMap(numTestsMax);
		System.out.println(test.toHeirarchalCharList(0).toString() + "\r\n");
		boolean allTestsPassed = true;
		int numTests = numTestsMax;

		for (TestPair<String, String> entry : tests)
		{
			if (numTests <= 0)
			{
				break;
			}
			else
			{
				numTests--;
			}
			System.out.println(HalfByteArrayFactory.wrapIntoArray(entry.getKey()).toString());
			final String entryKey = entry.getKey();
			final String entryValue = entry.getValue();
			final String testValue = test.get(entryKey);

			if (!entryValue.equals(testValue))
			{
				allTestsPassed = false;
				System.out.println("unfound keyValue pair: " + entry.getKey() + "\r\n\tentryValue: " + entryValue + "\r\n\ttestValue: " + testValue);
			}
		}

		if (allTestsPassed)
		{
			System.out.println("all " + numTestsMax + " tests passed.");
		}
	}

	static final void test_PutMethod()
	{
		final int numTests;

		// numTests = 11;
		numTests = Integer.MAX_VALUE;

		final TestPair<String, String>[] tests = getTestPairs();

		final LinkedRadixMap<String> test = getTestMap(numTests);

		final CharList result = new CharList();

		result.add("keys:");
		int numKeys = 0;

		for (TestPair<String, String> entry : tests)
		{
			if (numKeys < numTests)
			{
				numKeys++;
			}
			else
			{
				break;
			}

			result.addNewIndentedLine(1);
			result.add(HalfByteArrayFactory.wrapIntoArray(entry.getKey()).toCharList(), true);
		}

		result.addNewLine();
		result.add("map:");
		result.addNewLine();
		result.add(test.toHeirarchalCharList(0), true);

		System.out.println(result.toString());
	}

	private static final LinkedRadixMap<String> getTestMap()
	{
		return getTestMap(Integer.MAX_VALUE);
	}

	private static final LinkedRadixMap<String> getTestMap(int limit)
	{
		final LinkedRadixMap<String> test = new LinkedRadixMap<String>();
		final TestPair<String, String>[] pairs = getTestPairs();

		for (TestPair<String, String> entry : pairs)
		{
			if (limit > 0)
			{
				limit--;
			}
			else
			{
				break;
			}

			test.put(entry.getKey(), entry.getValue());
		}

		return test;
	}
	
	private static final LinkedRadixMap<String> getTestMap_fromDictionary(int numWords)
	{
		final XorShiftStar gen = XorShiftStar.getNewInstance(Generators.XorShiftStar1024);
		final String[] dict = Dictionary_English.getEntireGoddamnDictionary();
		final LinkedRadixMap<String> result = new LinkedRadixMap<String>();
		
		if(numWords == -1)
		{
			for(String word: dict)
			{
				result.put(word, word);
			}
		}
		else
		{
			for(int i = 0; i < numWords; i++)
			{
				final String word = dict[gen.nextInt(dict.length - 1)];
				result.put(word, word);
			}
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	private static final TestPair<String, String>[] getTestPairs()
	{
		return new TestPair[] {
								new TestPair<String, String>("asdfghjkl", "value0"),
								new TestPair<String, String>("asdqwe", "value1"),
								new TestPair<String, String>("asdzxc", "value2"),
								new TestPair<String, String>("astoyu", "value3"),
								new TestPair<String, String>("astiii", "value4"),
								new TestPair<String, String>("testString1", "value5"),
								new TestPair<String, String>("test2", "value6"),
								new TestPair<String, String>("bingo3", "value7"),
								new TestPair<String, String>("bongo4", "value8"),
								new TestPair<String, String>("tentative5", "value9"),
								new TestPair<String, String>("zanzibar6", "value10"),
								new TestPair<String, String>("why7", "value11"),
								new TestPair<String, String>("", "rootStorage"), };
	}

	private static class TestPair<U, V>
	{
		private final U key;
		private final V value;

		public TestPair(U inKey, V inValue)
		{
			this.key = inKey;
			this.value = inValue;
		}

		public U getKey()
		{
			return this.key;
		}

		public V getValue()
		{
			return this.value;
		}
	}
}
