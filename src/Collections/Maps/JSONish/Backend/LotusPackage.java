package Collections.Maps.JSONish.Backend;

public class LotusPackage
{
	private final LotusPackageHeader header;
	private final LotusPackage basePack;
	private final LotusObject cargo;
	
	public LotusPackage(LotusPackageHeader inHeader, LotusPackage inBasePack, LotusObject inCargo)
	{
		this.header = inHeader;
		this.basePack = inBasePack;
		this.cargo = inCargo;
	}
	
	public String getName()
	{
		return this.header.getName();
	}
	
	public LotusFormat getVar(String name)
	{
		LotusFormat result = this.cargo.getVar(name);
		
		if(result == null)
		{
			result = this.basePack.getVar(name);
		}
		
		return result;
	}
	
	public LotusObject getVarAsObject(String name)
	{
		return (LotusObject) this.getVar(name);
	}
	
	public LotusArray getVarAsArray(String name)
	{
		return (LotusArray) this.getVar(name);
	}
	
	public LotusVar getVarAsVar(String name)
	{
		return (LotusVar) this.getVar(name);
	}

	@Override
	public String toString()
	{
		return "LotusPackage [header=" + this.header + ", basePack=" + this.basePack + ", cargo=" + this.cargo + "]";
	}
	
	/*
	public static final HalfByteRadixMap<LotusFormat> findExclusives(HalfByteRadixMap<LotusPackage> set1, HalfByteRadixMap<LotusPackage> set2)
	{
		
	}
	
	public static final LotusObject findExclusives(LotusPackage set1, LotusPackage set2)
	{
		final LotusObject 
	}
	*/
}
