package Collections.Maps.JSONish.Backend;

public enum LotusDataTypes
{
	Package(false),
	Object(false),
	Array(false),
	Var(false);
	
	private final boolean defaultBehavior_shouldLabelDataTypes;
	
	private LotusDataTypes(boolean shouldLabelDataTypes)
	{
		this.defaultBehavior_shouldLabelDataTypes = shouldLabelDataTypes;
	}
	
	public boolean defaultBehavior_shouldLabelDataTypes()
	{
		return this.defaultBehavior_shouldLabelDataTypes;
	}
}
