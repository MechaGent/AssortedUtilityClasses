package HandyStuff;

import Collections.Lists.CharList.CharList;
import HandyStuff.HandyEnums.EndianSettings;

public class EnumSwitchGenerator
{
	public static void main(String[] args)
	{
		generateCaseStatements(EndianSettings.LeftBitLeftByte, "test");
	}
	
	public static final <U extends Enum<U>> void generateCaseStatements(U targetEnum, String varName)
	{
		final CharList result = new CharList();
		
		final U[] values = targetEnum.getDeclaringClass().getEnumConstants();
		
		for(U eeny: values)
		{
			result.add("case ");
			result.add(eeny.name());
			result.add(": { break; }");
			result.addNewLine();
		}
		
		result.add("default: { throw new UnhandledEnumException(");
		result.add(varName);
		result.add("); }");
		
		System.out.println(result.toString());
	}
}
