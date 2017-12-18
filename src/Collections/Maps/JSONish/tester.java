package Collections.Maps.JSONish;

import Collections.Maps.JSONish.Backend.LotusObject;
import Collections.Maps.JSONish.Backend.LotusPackage;
import Collections.Maps.JSONish.Backend.LotusRepo;
import Collections.Maps.JSONish.Backend.Parser;
import Collections.Maps.JSONish.Backend.Tokenizer;
import Collections.Maps.JSONish.JsonJSON.JsonTokenizer;
import Collections.Maps.JSONish.LotusJSON.LotusTokenizer;
import Streams.BytesStreamer.BytesStreamer;
import Streams.BytesStreamer.BytesStreamerFactory;
import Streams.BytesStreamer.BytesStreamerFactory.StringInterpretations;

public class tester
{
	public static void main(String[] args)
	{
		test_parseFromWorldState();
		//testRepo_fetch();
	}
	
	@SuppressWarnings("unused")
	private static void testRepo_fetch()
	{
		final LotusRepo test = generateTestRepo();
		final String targetDir = "/Lotus/Weapons/Tenno/Rifle/BratonPrime";
		
		final LotusPackage target = test.getPackage(test.getHeader(targetDir));
		
		System.out.println(target.toString());
	}
	
	private static LotusRepo generateTestRepo()
	{
		final String inDir = "<first part>/outPackages2.txt";
		final Tokenizer tokey = new LotusTokenizer();
		final BytesStreamer itsy = BytesStreamerFactory.getInstance(inDir, StringInterpretations.FilePath);
		
		return new LotusRepo(itsy, tokey);
	}
	
	private static void test_parseFromWorldState()
	{
		final String dir = "<first part>/Worldstate.txt";
		final Tokenizer tokey = new JsonTokenizer();
		final BytesStreamer stream = BytesStreamerFactory.getInstance(dir, StringInterpretations.FilePath);
		stream.getNextByte();	//because it can't handle an open bracket being the first char
		final LotusObject test = Parser.parseLotusObject(tokey, stream, "test");
		System.out.println(test.toCharList(0, false, false, true));
	}

	@SuppressWarnings("unused")
	private static void test_parseLotusObject()
	{
		final Tokenizer tokey = new LotusTokenizer();
		BytesStreamer itsy = getSanitizedStream();

		final LotusObject test = Parser.parseLotusObject(tokey, itsy, "test");
		System.out.println(test.toCharList(0, false, false, true));
	}

	private static BytesStreamer getSanitizedStream()
	{
		final String testFileDir = "Fill Me In";
		BytesStreamer itsy = BytesStreamerFactory.getInstance(testFileDir, StringInterpretations.FilePath);
		final int headerCharsOffset = 154;
		itsy.getNextByteArray(headerCharsOffset);

		return itsy;
	}
}
