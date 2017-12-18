package Collections.Maps.RadixMap.Linked.Generic;

import java.util.Map.Entry;

import Collections.Lists.CharList.CharList;
import Collections.Lists.Generic.Directed.SingleLinkedList;
import Collections.PackedArrays.HalfByte.HalfByteArray;
import Collections.PackedArrays.HalfByte.HalfByteArrayFactory;
import HandyStuff.LiterallyDictionaries.English.Mk01.Dictionary_English;
import HandyStuff.Randomness.Generators;
import HandyStuff.Randomness.XorShiftStar;

class Tests
{
	public static void main(String[] args)
	{
		final SingleLinkedList<TestResult> results = new SingleLinkedList<TestResult>();
		final int numEntries = 100000;
		final LinkedRadixMap<String> test = getTestMap_fromDictionary(numEntries);

		// results.add(test_toHeirarchalCharList(test.copyMap()));
		results.add(test_putMethods_forStrings(test.copyMap()));
		results.add(test_getMethods_forStrings(numEntries, 100));

		printResults(results);
	}

	private static final void printResults(SingleLinkedList<TestResult> results)
	{
		final CharList result = new CharList();
		boolean isFirst = true;

		for (TestResult subresult : results)
		{
			if (isFirst)
			{
				isFirst = false;
			}
			else
			{
				result.addNewLine();
			}

			result.add(subresult.toCharList(), true);
		}

		System.out.println(result.toString());
	}

	private static final LinkedRadixMap<String> getTestMap_fromDictionary(int numWords)
	{
		final XorShiftStar gen = XorShiftStar.getNewInstance(Generators.XorShiftStar1024);
		final String[] dict = Dictionary_English.getEntireGoddamnDictionary();
		final LinkedRadixMap<String> result = new LinkedRadixMap<String>();

		if (numWords == -1)
		{
			for (String word : dict)
			{
				result.put(word, word);
			}
		}
		else
		{
			for (int i = 0; i < numWords; i++)
			{
				final String word = dict[gen.nextInt(dict.length - 1)];
				result.put(word, word);
			}
		}

		return result;
	}
	
	private static final LinkedRadixMap<String> getTestMap_fromDictionary(HalfByteArray[] keys)
	{
		final LinkedRadixMap<String> result = new LinkedRadixMap<String>();
		
		for(HalfByteArray key: keys)
		{
			result.put(key, key.interpretAsCharString());
		}
		
		return result;
	}

	@SuppressWarnings("unused")
	private static final HalfByteArray[] getTestKeys_fromDictionary(int numWords)
	{
		final XorShiftStar gen = XorShiftStar.getNewInstance(Generators.XorShiftStar1024);
		final HalfByteArray[] result = new HalfByteArray[numWords];
		final String[] dict = Dictionary_English.getEntireGoddamnDictionary();

		for (int i = 0; i < numWords; i++)
		{
			final String word = dict[gen.nextInt(dict.length - 1)];
			result[i] = HalfByteArrayFactory.wrapIntoArray(word);
		}

		return result;
	}
	
	private static final HalfByteArray[] getTestKeys_fromDictionary(int numWords, int offset)
	{
		final HalfByteArray[] result = new HalfByteArray[numWords];
		final String[] dict = Dictionary_English.getEntireGoddamnDictionary();
		
		for(int i = 0; i < result.length; i++)
		{
			result[i] = HalfByteArrayFactory.wrapIntoArray(dict[offset + i]);
		}
		
		return result;
	}

	/*
	 * testing methods
	 */

	@SuppressWarnings("unused")
	private static final TestResult test_toHeirarchalCharList(LinkedRadixMap<String> test)
	{
		final CharList result = test.toHeirarchalCharList(1);
		result.pushNewLine();
		return new TestResult(true, "test_toHeirarchalCharList()", result);
	}

	private static final TestResult test_putMethods_forStrings(LinkedRadixMap<String> test)
	{
		// System.out.println(test.toHeirarchalCharList(0).toString() + "\r\n*****\r\n");
		final CharList result = new CharList();
		boolean passedTest = true;

		for (Entry<HalfByteArray, String> entry : test)
		{
			final String key = entry.getKey().interpretAsCharString();
			final String value = entry.getValue();

			if (!key.equals(value))
			{
				passedTest = false;
				
				if (result.isEmpty())
				{
					result.add("Set containing mismatches: [");
				}
				else
				{
					result.add(", ");
				}

				result.add('{');
				result.add(key);
				result.add(", ");
				result.add(value);
				result.add('}');
			}
		}

		if (result.isNotEmpty())
		{
			result.add(']');
		}

		return new TestResult(passedTest, "test_putMethods_forStrings()", result);
	}

	private static final TestResult test_getMethods_forStrings(int numKeys, int numBouncyKeys)
	{
		final HalfByteArray[] keys = getTestKeys_fromDictionary(numKeys, 0);
		final LinkedRadixMap<String> test = getTestMap_fromDictionary(keys);
		final HalfByteArray[] bouncyKeys = getTestKeys_fromDictionary(numBouncyKeys, numKeys);
		
		final CharList result_bounce = new CharList();
		final CharList result_mismatch = new CharList();
		boolean passedTest = true;

		for(HalfByteArray key: keys)
		{
			final String testValue = test.get(key);
			final String strungKey = key.interpretAsCharString();
			
			if(testValue == null)
			{
				passedTest = false;
				
				if(result_bounce.isEmpty())
				{
					result_bounce.add("\r\n\tSet containing bounced keys: [");
				}
				else
				{
					result_bounce.add(", ");
				}
				
				result_bounce.add(strungKey);
			}
			else if(!testValue.equals(strungKey))
			{
				passedTest = false;
				
				if(result_mismatch.isEmpty())
				{
					result_mismatch.add("\r\n\tSet containing mismatched keys: [");
				}
				else
				{
					result_mismatch.add(", ");
				}
				
				result_mismatch.add('{');
				result_mismatch.add(strungKey);
				result_mismatch.add(", ");
				result_mismatch.add(testValue);
				result_mismatch.add('}');
			}
		}
		
		if(result_bounce.isNotEmpty())
		{
			result_bounce.add(']');
			//result_bounce.addNewLine();
		}
		
		for(HalfByteArray key: bouncyKeys)
		{
			final String testValue = test.get(key);
			final String strungKey = key.interpretAsCharString();
			
			if(testValue != null)
			{
				passedTest = false;
				
				if(result_bounce.isEmpty())
				{
					result_bounce.add("\r\n\tSet containing unbounced keys: [");
				}
				else
				{
					result_bounce.add(", ");
				}
				
				result_bounce.add('{');
				result_bounce.add(strungKey);
				result_bounce.add(", ");
				result_bounce.add(testValue);
				result_bounce.add('}');
			}
		}
		
		if(result_bounce.isNotEmpty())
		{
			result_bounce.add(']');
			
			if(result_mismatch.isNotEmpty())
			{
				result_bounce.addNewLine();
				result_bounce.add(result_mismatch, true);
				result_bounce.add(']');
			}
		}
		else
		{
			if(result_mismatch.isNotEmpty())
			{
				result_bounce.add(result_mismatch, true);
				result_bounce.add(']');
			}
		}

		return new TestResult(passedTest, "test_getMethods_forStrings()", result_bounce);
	}

	private static class TestResult
	{
		private final boolean testPassed;
		private final String sourceMethod;
		private final CharList details;

		@SuppressWarnings("unused")
		public TestResult(boolean inTestPassed, String inSourceMethod)
		{
			this(inTestPassed, inSourceMethod, new CharList());
		}

		public TestResult(boolean inTestPassed, String inSourceMethod, CharList inDetails)
		{
			this.testPassed = inTestPassed;
			this.sourceMethod = inSourceMethod;
			this.details = inDetails;
		}

		public final CharList toCharList()
		{
			final CharList result = new CharList();

			result.add(this.sourceMethod);
			result.add(": ");

			if (this.testPassed)
			{
				result.add("PASSED");
			}
			else
			{
				result.add("FAILED");
			}

			result.add(" Details: ");
			result.add(this.details, true);

			return result;
		}
	}
}
