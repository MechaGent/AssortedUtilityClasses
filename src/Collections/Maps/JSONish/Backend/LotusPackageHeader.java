package Collections.Maps.JSONish.Backend;

public class LotusPackageHeader
{
	private final String Dir;
	private final String Name;
	
	private final String BaseDir;
	private final String BaseName;
	
	private final int offset;
	private final LotusObject extraData;

	public LotusPackageHeader(String inDir, String inName, String inBaseDir, String inBaseName, int inOffset, LotusObject inExtraData)
	{
		this.Dir = inDir;
		this.Name = inName;
		this.BaseDir = inBaseDir;
		this.BaseName = inBaseName;
		this.offset = inOffset;
		this.extraData = inExtraData;
	}

	public String getDir()
	{
		return this.Dir;
	}

	public String getName()
	{
		return this.Name;
	}

	public boolean hasBasePack()
	{
		return this.BaseName.length() != 0;
	}
	
	public String getBaseDir()
	{
		return this.BaseDir;
	}

	public String getBaseName()
	{
		return this.BaseName;
	}

	public int getOffset()
	{
		return this.offset;
	}

	public LotusObject getExtraData()
	{
		return extraData;
	}
	
	@Override
	public String toString()
	{
		return "LotusPackageHeader [Dir=" + this.Dir + ", Name=" + this.Name + ", BaseDir=" + this.BaseDir + ", BaseName=" + this.BaseName + ", offset=" + this.offset + "]";
	}
}
