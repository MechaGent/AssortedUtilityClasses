package Collections.PackedArrays.HalfByte;

import Collections.Lists.CharList.CharList;

class tester
{
	public static void main(String[] args)
	{
		//test_IntLongStorageEquiv();
		test_StringCharArrEquiv();
	}
	
	private static final void test_StringCharArrEquiv()
	{
		final String testString = "testingLaDeeDah";
		final char[] testArr = testString.toCharArray();
		
		final HalfByteArray_StringWrapper stringWrap = (HalfByteArray_StringWrapper) HalfByteArrayFactory.wrapIntoArray(testString);
		final HalfByteArray_CharArrWrapper arrWrap = (HalfByteArray_CharArrWrapper) HalfByteArrayFactory.wrapIntoArray(testArr);
		
		final CharList result = new CharList();
		result.add("back to strings:");
		result.addNewIndentedLine(1);
		result.add(stringWrap.interpretAsCharArr());
		result.addNewIndentedLine(1);
		result.add(arrWrap.interpretAsCharArr());
		
		result.addNewLine();
		result.add("comparison tests:");
		result.addNewIndentedLine(1);
		result.add("calling stringWrap.compareTo(arrWrap) returns ");
		result.add_asDecString(stringWrap.compareTo(arrWrap));
		result.addNewIndentedLine(1);
		result.add("calling arrWrap.compareTo(stringWrap) returns ");
		result.add_asDecString(arrWrap.compareTo(stringWrap));
		
		System.out.println(result.toString());
	}
	
	private static final void test_IntLongStorageEquiv()
	{
		final String testString = "testingLaDeeDah";
		final HalfByteArray_StringWrapper wrap = (HalfByteArray_StringWrapper) HalfByteArrayFactory.wrapIntoArray(testString);
		final HalfByteArray_IntBack testI = new HalfByteArray_IntBack(wrap.lengthInHalfBytes);
		final HalfByteArray_LongBack testL = new HalfByteArray_LongBack(wrap.lengthInHalfBytes);
		
		//System.out.println("wrap length: " + wrap.lengthInHalfBytes + "\r\ntestI length: " + testI.lengthInHalfBytes);
		
		for(int i = 0; i < wrap.lengthInHalfBytes; i++)
		{
			final int val = wrap.getHalfByteAt(i);
			
			System.out.println("setting index " + i + " to 0x" + Integer.toHexString(val));
			
			testI.setHalfByteAt(i, val);
			testL.setHalfByteAt(i, val);
		}
		
		final CharList result = new CharList();
		
		for(int i = 0; i < testI.spine.length; i++)
		{
			if(i != 0)
			{
				result.add(' ');
			}
			
			result.add(Integer.toHexString(testI.spine[i]));
		}
		
		result.addNewLine();
		
		for(int i = 0; i < testL.spine.length; i++)
		{
			if(i != 0)
			{
				result.add(' ');
			}
			
			result.add(Long.toHexString(testL.spine[i]));
		}
		
		System.out.println(result.toString());
	}
}
