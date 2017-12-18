package Collections.Maps.VirtualFileSystem;

import java.util.Iterator;

import Collections.Lists.CharList.CharList;

public interface filePath extends Iterable<String>
{
	public String getRoot();
	
	public String getFileName();
	
	public boolean isRelative();
	
	public boolean endsWithFile();
	
	public String toString();
	
	public CharList toCharList();
	
	public Iterator<String> getNameIterator();
	
	public Iterator<String> getFolderNameIterator();
}
