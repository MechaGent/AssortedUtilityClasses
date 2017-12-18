package Collections.Maps.VirtualFileSystem;

import java.util.Iterator;

import Collections.Lists.CharList.CharList;
import Collections.Lists.Generic.Directed.SingleLinkedList;

public class filePathFactory
{
	public static final filePath toRelativeFilePath(String path)
	{
		return new FullArrFilePath(path, true, true);
	}
	
	public static final filePath toRelativeFolderPath(String path)
	{
		return new FullArrFilePath(path, true, false);
	}
	
	public static final filePath toRelativeFilePath(String[] path)
	{
		return new FullArrFilePath(path, true, true);
	}
	
	public static final filePath toRelativeFolderPath(String[] path)
	{
		return new FullArrFilePath(path, true, false);
	}
	
	public static final filePath toAbsoluteFilePath(String path)
	{
		return new FullArrFilePath(path, false, true);
	}
	
	public static final filePath toAbsoluteFolderPath(String path)
	{
		return new FullArrFilePath(path, false, false);
	}
	
	public static final filePath toAbsoluteFilePath(String[] path)
	{
		return new FullArrFilePath(path, false, true);
	}
	
	public static final filePath toAbsoluteFolderPath(String[] path)
	{
		return new FullArrFilePath(path, false, false);
	}
	
	@SuppressWarnings("unused")
	private static final String[] mergeRootToPath(String root, String path)
	{
		final SingleLinkedList<CharList> split = (new CharList(path)).splitAt('/');

		final String[] result = new String[split.size() + 1];

		result[0] = root;
		Iterator<CharList> itsy = split.iterator();
		int i = 1;

		while (itsy.hasNext())
		{
			result[i++] = itsy.next().toString();
		}

		return result;
	}

	@SuppressWarnings("unused")
	private static final String[] mergeRootToPath(String root, String[] path)
	{
		final String[] result = new String[path.length + 1];
		result[0] = root;

		for (int i = 1; i < result.length; i++)
		{
			result[i] = path[i - 1];
		}

		return result;
	}

	@SuppressWarnings("unused")
	private static final String[] mergePathToName(String path, String Name)
	{
		final SingleLinkedList<CharList> split = (new CharList(path)).splitAt('/');

		final String[] result = new String[split.size() + 1];
		Iterator<CharList> itsy = split.iterator();
		
		for(int i = 0; i < split.size(); i++)
		{
			result[i] = itsy.next().toString();
		}
		
		result[result.length - 1] = Name;
		
		return result;
	}

	@SuppressWarnings("unused")
	private static final String[] mergePathToName(String[] path, String Name)
	{
		final String[] result = new String[path.length + 1];

		for (int i = 0; i < path.length; i++)
		{
			result[i] = path[i - 1];
		}
		
		result[path.length] = Name;

		return result;
	}

	@SuppressWarnings("unused")
	private static final String[] mergeAll(String root, String path, String Name)
	{
		final SingleLinkedList<CharList> split = (new CharList(path)).splitAt('/');

		final String[] result = new String[split.size() + 2];

		result[0] = root;
		Iterator<CharList> itsy = split.iterator();
		int i = 1;

		while (itsy.hasNext())
		{
			result[i++] = itsy.next().toString();
		}
		
		result[i] = Name;

		return result;
	}

	@SuppressWarnings("unused")
	private static final String[] mergeAll(String root, String[] path, String Name)
	{
		final String[] result = new String[path.length + 1];
		result[0] = root;

		for (int i = 1; i < path.length; i++)
		{
			result[i] = path[i - 1];
		}
		
		result[path.length] = Name;

		return result;
	}
}
