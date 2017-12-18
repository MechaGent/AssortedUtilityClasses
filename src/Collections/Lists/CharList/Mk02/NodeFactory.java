package Collections.Lists.CharList.Mk02;

class NodeFactory
{	
	static final Node createNode(char[] in)
	{
		switch (in.length)
		{
			case 0:
			{
				return null;
			}
			case 1:
			{
				return new Node_Char(in[0]);
			}
			default:
			{
				return new Node_CharArr(in);
			}
		}
	}
	
	static final Node createNode(char[] in, int numRepeats)
	{
		switch(numRepeats)
		{
			case 0:
			{
				return null;
			}
			case 1:
			{
				return createNode(in);
			}
			default:
			{
				return new Node_CharArr_Repeated(numRepeats, in);
			}
		}
	}

	static final Node createNode(String in)
	{
		switch (in.length())
		{
			case 0:
			{
				return null;
			}
			case 1:
			{
				return new Node_Char(in.charAt(0));
			}
			default:
			{
				return new Node_String(in);
			}
		}
	}
}
