package Collections.Lists.CharList.Mk01;

interface MultiNode
{
	/**
	 * 
	 * @return a copy of this node's cargo in a CharList, but with every char given its own node
	 */
	public CharList splitCopyIntoNewCharList();
}
