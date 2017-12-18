package HandyStuff.HandyEnums;

public enum Primitives
{
	Byte("byte", 1),
	Char("char", 1),
	Short("short", 2),
	Int("int", 4),
	Float("float", 4),
	Long("long", 8),
	Double("double", 8);
	
	private final String codeKeyword;
	private final int sizeInBytes;

	private Primitives(String inCodeKeyword, int inSizeInBytes)
	{
		this.codeKeyword = inCodeKeyword;
		this.sizeInBytes = inSizeInBytes;
	}

	public String getCodeKeyword()
	{
		return this.codeKeyword;
	}

	public int getSizeInBytes()
	{
		return this.sizeInBytes;
	}
}
