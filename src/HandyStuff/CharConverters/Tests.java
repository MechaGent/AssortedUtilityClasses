package HandyStuff.CharConverters;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Collections.Maps.RadixMap.Linked.Generic.Iterators.ValuesItsy;
import Collections.Lists.CharList.CharList;
import Collections.Maps.RadixMap.Linked.Generic.LinkedRadixMap;
import HandyStuff.FileParser;

public class Tests
{
	public static void main(String[] args)
	{
		final String basePath = "H:/Users/Thrawnboo/workspace - Java/MatureStuff/src/HandyStuff/CharConverters/";
		final String path1 = basePath + "Utf8ToUnicode.txt";
		final String path2 = basePath + "Utf16ToUnicode.txt";
		
		final String result = mergeTwoCharsetLists(path1, path2);
		System.out.println(result);
	}
	
	private static final Pattern splitter = Pattern.compile("(\\(U\\+....\\)) \t(.+) \t(.+)\r\n");
	
	static final String mergeTwoCharsetLists(String path1, String path2)
	{
		final String raw1 = FileParser.parseFileAsString(path1);
		final String raw2 = FileParser.parseFileAsString(path2);
		
		final LinkedRadixMap<mergeObj> map = new LinkedRadixMap<mergeObj>();
		
		final Matcher matcher = splitter.matcher(raw1);
		
		while(matcher.find())
		{
			final String unicode = matcher.group(1);
			final String var1 = matcher.group(2);
			final String def = matcher.group(3);
			
			map.put(unicode, new mergeObj(unicode, var1, def));
		}
		
		final Matcher matcher2 = splitter.matcher(raw2);
		
		while(matcher2.find())
		{
			final String unicode = matcher2.group(1);
			final String var2 = matcher2.group(2);
			final String def = matcher2.group(3);
			
			final mergeObj merger = map.get(unicode);
			
			if(merger == null)
			{
				map.put(unicode, new mergeObj(unicode, mergeObj.Default_var2, var2, def));
			}
			else
			{
				merger.var2 = var2;
			}
		}
		
		final mergeObj[] array = new mergeObj[map.size()];
		final ValuesItsy<mergeObj> itsy = map.valuesIterator();
		int i = 0;
		
		while(itsy.hasNext())
		{
			array[i++] = itsy.next();
		}
		
		Arrays.sort(array);
		
		final CharList result = new CharList();
		
		for(mergeObj obj: array)
		{
			obj.addToCharList(result);
			result.addNewLine();
		}
		
		return result.toString();
	}
	
	private static final class mergeObj implements Comparable<mergeObj>
	{
		private static final String Default_var2 = "";
		
		private final String unicode;
		private final String var1;
		private String var2;
		private final String Def;
		
		public mergeObj(String inUnicode, String inVar1, String inDef)
		{
			this.unicode = inUnicode;
			this.var1 = inVar1;
			this.var2 = Default_var2;
			this.Def = inDef;
		}

		public mergeObj(String inUnicode, String inVar1, String inVar2, String inDef)
		{
			this.unicode = inUnicode;
			this.var1 = inVar1;
			this.var2 = inVar2;
			this.Def = inDef;
		}

		@Override
		public final int compareTo(mergeObj inArg0)
		{
			return this.unicode.compareTo(inArg0.unicode);
		}
		
		public final void addToCharList(CharList result)
		{
			result.add(this.unicode);
			result.addTab();
			result.add(this.var1);
			result.addTab();
			result.add(this.var2);
			result.addTab();
			result.add(this.Def);
		}
		
		@Override
		public final String toString()
		{
			return this.unicode + "\t" + this.var1 + "\t" + this.var2 + "\t" + this.Def;
		}
	}
}
