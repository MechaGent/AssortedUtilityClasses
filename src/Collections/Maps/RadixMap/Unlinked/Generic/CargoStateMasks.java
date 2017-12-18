package Collections.Maps.RadixMap.Unlinked.Generic;

enum CargoStateMasks
{
	CannotHaveCargo(0),
	CanHaveCargo(1 << 8),
	HasCargo(0b11 << 8)
	;
	
	public final int value;
	
	private CargoStateMasks(int inValue)
	{
		this.value = inValue;
	}
}
