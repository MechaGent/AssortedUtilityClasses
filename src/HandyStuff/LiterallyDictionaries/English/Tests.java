package HandyStuff.LiterallyDictionaries.English;

import java.util.Arrays;

public final class Tests
{
	public static void main(String[] args)
	{
		final Dictionary_English dictionary = Dictionary_English.getInstance();
		final String[] words = dictionary.getRandomWords(5);
		
		System.out.println(Arrays.toString(words));
	}
}
