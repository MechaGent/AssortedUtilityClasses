package HandyStuff.JavaObjectLayout;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import Collections.Lists.CharList.CharList;
import HandyStuff.Randomness.XorShiftStar1024;

public class Commander
{
	public static void main(String[] args)
	{
		final CharList result = new CharList();
		
		result.add(VM.current().details());
		result.addNewLine();
		result.add(ClassLayout.parseClass(getTargetClass()).toPrintable());
		
		System.out.println(result.toString());
	}
	
	private static final Class<?> getTargetClass()
	{
		final Class<?> result;
		
		//result = Commander.class;
		//result = HalfByteRadixMap.class;
		//result = BranchNode.class;
		//result = LeafNode.class;
		result = XorShiftStar1024.class;
		//result = LinkedIntNode.class;
		//result = LinkedIntLeafNode.class;
		//result = LinkedNode.class;
		//result = LinkedLeafNode.class;
		//result = LotusPackage2.class;
		
		return result;
	}
}
