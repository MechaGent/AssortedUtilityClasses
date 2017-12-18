package Collections.Maps.JSONish.Backend;

import Collections.Lists.CharList.CharList;
import Collections.Maps.VirtualFileSystem.VirtualFileSystem;
import Collections.Maps.VirtualFileSystem.filePath;
import Collections.Maps.VirtualFileSystem.filePathFactory;
import Streams.BytesStreamer.BytesStreamer;

public class LotusRepo
{
	private static final char[] LotusPackOpener = "~FullPackageName=".toCharArray();
	private static final char[] BasePackName = "BasePackage".toCharArray();
	private static final char[] ExtraInfoName = "ExtraHeaderInfo".toCharArray();
	
	private final BytesStreamer rawData;
	private final Tokenizer tokey;
	private final VirtualFileSystem<LotusPackageHeader> headers;
	
	public LotusRepo(BytesStreamer inRawData, Tokenizer parseType)
	{
		this.rawData = inRawData;
		this.tokey = parseType;
		this.headers = initHeaders(inRawData, parseType);
	}
	
	public LotusPackage getPackage(LotusPackageHeader in)
	{
		final LotusPackageHeader baseHeader = this.getHeader(in.getDir() + in.getName(), in.getBaseDir() + in.getBaseName());
		final LotusPackage basePack;
		
		if(baseHeader != null)
		{
			basePack = this.getPackage(baseHeader);
		}
		else
		{
			basePack = null;
		}
		
		this.rawData.setToPosition(in.getOffset());
		final LotusObject cargo = Parser.parseLotusObject(this.tokey, this.rawData, in.getName());
		return new LotusPackage(in, basePack, cargo);
	}
	
	public LotusPackageHeader getHeader(String in)
	{
		return this.getHeader(filePathFactory.toAbsoluteFilePath(in));
	}
	
	public LotusPackageHeader getHeader(filePath in)
	{
		return this.headers.getFile(in);
	}
	
	public LotusPackageHeader getHeader(String absPath, String relPath)
	{
		return this.getHeader(filePathFactory.toAbsoluteFilePath(absPath), filePathFactory.toRelativeFilePath(relPath));
	}
	
	public LotusPackageHeader getHeader(filePath absPath, filePath relPath)
	{
		return this.headers.getFile(absPath, relPath);
	}
	
	private static VirtualFileSystem<LotusPackageHeader> initHeaders(BytesStreamer raw, Tokenizer parseType)
	{
		final VirtualFileSystem<LotusPackageHeader> result = VirtualFileSystem.getInstance();
		
		while (raw.hasNextByte())
		{
			if (raw.getNextByte() == '~')
			{
				final LotusPackageHeader next = parseNextHeader(raw, parseType);
				String path = next.getDir() + next.getName();
				
				result.addFile(path, next);
			}
		}

		return result;
	}
	
	private static LotusPackageHeader parseNextHeader(BytesStreamer raw, Tokenizer parseType)
	{
		final CharList check = new CharList();
		
		for (int i = 1; i < LotusPackOpener.length; i++)
		{
			final char current = raw.getNextChar();
			check.add(current);
			if (LotusPackOpener[i] != current)
			{
				throw new IllegalArgumentException("character mismatch!\r\n" + new String(LotusPackOpener) + "\r\n" + check.toString());
			}
		}

		final String[] DirAndName = getNextDirAndName(raw);

		final char[] nameArr = DirAndName[1].toCharArray();
		int options;
		String[] BaseDirAndName = new String[2];
		LotusObject extraData = null;
		
		while ((options = parseNextHeader_parseNextField(raw, nameArr)) != 2)
		{
			switch (options)
			{
				case 0:
				{
					BaseDirAndName = getNextDirAndName(raw);

					break;
				}
				case 1:
				{
					extraData = (LotusObject) Parser.parseNextVar(parseType, raw);
					break;
				}
				default:
				{
					throw new IllegalArgumentException("illegal option: " + options);
				}
			}
		}

		return new LotusPackageHeader(DirAndName[0], DirAndName[1], BaseDirAndName[0], BaseDirAndName[1], raw.getPosition(), extraData);
	}
	
	private static String[] getNextDirAndName(BytesStreamer raw)
	{
		final CharList rawDir = new CharList();
		CharList possibleName = new CharList();
		boolean hasDirAndName = false;

		while (raw.hasNextByte() && !hasDirAndName)
		{
			final char next = raw.getNextChar();

			switch (next)
			{
				case '\r':
				{
					break;
				}
				case '\n':
				{
					hasDirAndName = true;
					break;
				}
				case '/':
				{
					rawDir.add(possibleName, true);
					rawDir.add('/');
					possibleName = new CharList();

					break;
				}
				default:
				{
					possibleName.add(next);
					break;
				}
			}
		}

		return new String[] {
								rawDir.toString(),
								possibleName.toString() };
	}

	/**
	 * 
	 * @param in
	 * @param Name
	 * @return 0 == BasePack, 1 == ExtraInfo, 2 == mainData
	 */
	private static int parseNextHeader_parseNextField(BytesStreamer in, char[] Name)
	{
		//final CharList diagBuffer = new CharList();
		boolean possBasePack = true;
		boolean possExtraInfo = true;
		//boolean possName = true;
		int result = -1;
		int i = 0;
		char curr = 0;

		while (result == -1 && (possBasePack || possExtraInfo))
		{
			curr = in.getNextChar();
			//diagBuffer.add(curr);
			//int activeCount = 0;

			if (possBasePack)
			{
				//activeCount++;

				if (i < BasePackName.length)
				{
					possBasePack = curr == BasePackName[i];
				}
				else
				{
					result = 0; // curr will be '=' at this point
				}
			}

			if (possExtraInfo)
			{
				//activeCount++;

				if (i < ExtraInfoName.length)
				{
					possExtraInfo = curr == ExtraInfoName[i];
				}
				else
				{
					result = 1; // curr will be '=' at this point
				}
			}

			i++;
		}
		
		if(result == -1)
		{
			while(curr != '{')
			{
				curr = in.getNextChar();
				//diagBuffer.add(curr);
			}
			
			result = 2;
		}
		else
		{
			while(curr != '=')
			{
				curr = in.getNextChar();
				//diagBuffer.add(curr);
			}
		}

		//System.out.println("Result: " + result + " from: " + diagBuffer.toString());
		
		return result;
	}
}
