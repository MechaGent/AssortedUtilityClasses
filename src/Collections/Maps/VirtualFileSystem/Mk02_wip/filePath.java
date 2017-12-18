package Collections.Maps.VirtualFileSystem.Mk02_wip;

import java.util.regex.Pattern;

public class filePath
{
	private static final Pattern pathSplitter = Pattern.compile("/");
	
	private final String[] path;
	private final boolean isRelative;
	private final boolean endsWithFile;
	
	public filePath(String inPath, boolean inIsRelative, boolean inEndsWithFile)
	{
		this(pathSplitter.split(inPath, 0), inIsRelative, inEndsWithFile);
	}
	
	public filePath(String[] inSplitPath, boolean inIsRelative, boolean inEndsWithFile)
	{
		this.path = inSplitPath;
		this.isRelative = inIsRelative;
		this.endsWithFile = inEndsWithFile;
	}
}
