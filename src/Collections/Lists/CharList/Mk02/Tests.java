package Collections.Lists.CharList.Mk02;

class Tests
{
	public static void main(String[] args)
	{
		final TestResult result = check_Add_SingleChar();
		
		System.out.println(result.toString());
	}
	
	private static TestResult check_Add_SingleChar()
	{
		final int numChars = 10;
		final char Char = 'a';
		final char[] control = new char[numChars];
		final CharList test = new CharList();
		
		for(int i = 0; i < numChars; i++)
		{
			test.add(Char);
			control[i] = Char;
		}
		
		final char[] testArr = test.toCharArray();
		
		if(CharArrsMatch(control, testArr))
		{
			return new TestResult(true, "");
		}
		else
		{
			final StringBuilder buildy = new StringBuilder(control.length + 6 + testArr.length);
			
			buildy.append('{');
			
			for(int i = 0; i < control.length; i++)
			{
				buildy.append(control[i]);
			}
			
			buildy.append("}, {");
			
			for(int i = 0; i < testArr.length; i++)
			{
				buildy.append(testArr[i]);
			}
			
			buildy.append('}');
			
			return new TestResult(false, buildy.toString());
		}
	}
	
	private static boolean CharArrsMatch(char[] control, char[] test)
	{
		if(control.length != test.length)
		{
			throw new IllegalArgumentException("mismatched lengths!");
		}
		
		for(int i = 0; i < control.length; i++)
		{
			if(control[i] != test[i])
			{
				return false;
			}
		}
		
		return true;
	}
	
	private static class TestResult
	{
		private final boolean testPassed;
		private final String desc;
		
		public TestResult(boolean inTestPassed, String inDesc)
		{
			this.testPassed = inTestPassed;
			this.desc = inDesc;
		}
		
		public String toString()
		{
			return "testPassed: " + Boolean.toString(this.testPassed) + ", desc: " + this.desc;
		}
	}
}
