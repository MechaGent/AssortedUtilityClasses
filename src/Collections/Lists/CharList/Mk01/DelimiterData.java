package Collections.Lists.CharList.Mk01;

public class DelimiterData
{
	private final String delimiter;
	private final int frequency;
	private final boolean hasFrontBookend;
	private final boolean hasBackBookend;
	
	public DelimiterData(String inDelimiter, int inFrequency, boolean inHasFrontBookend, boolean inHasBackBookend)
	{
		this.delimiter = inDelimiter;
		this.frequency = inFrequency;
		this.hasFrontBookend = inHasFrontBookend;
		this.hasBackBookend = inHasBackBookend;
	}

	public String getDelimiter()
	{
		return this.delimiter;
	}
	
	public int getDelimiterLength()
	{
		return this.delimiter.length();
	}

	public int getFrequency()
	{
		return this.frequency;
	}

	public boolean hasFrontBookend()
	{
		return this.hasFrontBookend;
	}

	public boolean hasBackBookend()
	{
		return this.hasBackBookend;
	}
}
