package Collections.Maps.RadixMap.Unlinked.Generic.Mk01;

import Collections.Lists.CharList.CharList;
import Collections.PackedArrays.HalfByte.HalfByteArrayFactory;
import HandyStuff.Randomness.XorShiftStar;
import HandyStuff.Randomness.XorShiftStarFactory;

class tester
{
	public static void main(String[] args)
	{
		// test_PutMethod();
		// test_GetMethod();
		test_heavyDutyStorage();
	}

	static final void test_heavyDutyStorage()
	{
		final int numPairs = 10000;
		final int keyLength = 32;

		final TestPair<String, String>[] tests = helper_test_heavyDutyStorage_generatePairs(numPairs, keyLength);
		final HalfByteRadixMap<String> test = helper_test_heavyDutyStorage_generateMap(tests);
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

	private static final HalfByteRadixMap<String> helper_test_heavyDutyStorage_generateMap(TestPair<String, String>[] tests)
	{
		final HalfByteRadixMap<String> result = new HalfByteRadixMap<String>();
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

		final HalfByteRadixMap<String> test = getTestMap(numTestsMax);
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

		final HalfByteRadixMap<String> test = getTestMap(numTests);

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

	@SuppressWarnings("unused")
	private static final HalfByteRadixMap<String> getTestMap()
	{
		return getTestMap(Integer.MAX_VALUE);
	}

	private static final HalfByteRadixMap<String> getTestMap(int limit)
	{
		final HalfByteRadixMap<String> test = new HalfByteRadixMap<String>();
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
