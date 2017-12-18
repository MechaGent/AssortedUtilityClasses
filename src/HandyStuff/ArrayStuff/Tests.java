package HandyStuff.ArrayStuff;

import Collections.Lists.CharList.CharList;

final class Tests
{
	public static void main(String[] args)
	{
		test_CharArrComparators();
	}

	static final void test_CharArrComparators()
	{
		final String[] tests = new String[] {"aabb|aAbb", "aabb|aAba", "aabb|aabb", "aabba|aAbb", "aabba|aAba", "aabba|aabb"};

		for (String test : tests)
		{
			final String[] split = test.split("\\|");
			final int result = ArrayComparators.compareAlphabetically(split[0].toCharArray(), split[1].toCharArray());

			final CharList mssg = new CharList();
			
			
			
			if(result == 0)
			{
				mssg.add(split[0]);
				mssg.add(" == ");
				mssg.add(split[1]);
			}
			else if(result < 0)
			{
				mssg.add(split[0]);
				mssg.add(" < ");
				mssg.add(split[1]);
			}
			else
			{
				mssg.add(split[1]);
				mssg.add(" < ");
				mssg.add(split[0]);
			}
			
			
			
			System.out.println(mssg.toCharArray());
		}
	}
}
