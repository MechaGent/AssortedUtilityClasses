package Collections.Maps.RadixMap.Linked.Generic.PrimForms.Int;

public enum CargoStates
{
	/**
	 * can have cargo, and does have cargo
	 * raw == 2
	 */
	hasCargo,
	
	/**
	 * can have cargo, and doens't have cargo
	 * raw == 1
	 */
	hasNoCargo,
	
	/**
	 * can't have cargo
	 * raw == 0
	 */
	cannotHaveCargo;
}
