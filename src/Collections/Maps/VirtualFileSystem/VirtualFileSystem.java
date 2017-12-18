package Collections.Maps.VirtualFileSystem;

import Collections.Maps.RadixMap.Linked.Generic.LinkedRadixMap;

public abstract class VirtualFileSystem<U>
{
	public static <U> VirtualFileSystem<U> getInstance()
	{
		return getInstance("");
	}
	
	public static <U> VirtualFileSystem<U> getInstance(String rootName)
	{
		return new VirtualFileSystem_SingleRoot<U>(new Folder<U>(rootName));
	}
	
	public static <U> VirtualFileSystem<U> getInstance(String... rootNames)
	{
		return new VirtualFileSystem_MultiRoots<U>(rootNames);
	}
	
	protected abstract Folder<U> getRootFolder(String in);
	
	public U addFile(String rawFilePath, U inFile)
	{
		return this.addFile(filePathFactory.toAbsoluteFilePath(rawFilePath), inFile);
	}
	
	public U addFile(filePath inPath, U inFile)
	{
		this.getRootFolder(inPath.getRoot()).addFile(inPath, inFile);
		
		return inFile;
	}

	public U getFile(String rawFilePath)
	{
		return this.getFile(filePathFactory.toAbsoluteFilePath(rawFilePath));
	}
	
	public U getFile(filePath in)
	{
		//System.out.println("inPath: " + in.toString() + " with root: " + in.getRoot());
		final Folder<U> curFolder = this.getRootFolder(in.getRoot());
		return curFolder.getFile(in, true);
	}
	
	public U getFile(String absoluteReference, String relative)
	{
		return this.getFile(filePathFactory.toAbsoluteFilePath(absoluteReference), filePathFactory.toRelativeFilePath(relative));
	}
	
	public U getFile(filePath absoluteReference, filePath relative)
	{
		final Folder<U> curFolder = this.getRootFolder(absoluteReference.getRoot());
		
		return curFolder.getFile(absoluteReference, relative);
	}
	
	private static class VirtualFileSystem_SingleRoot<U> extends VirtualFileSystem<U>
	{
		private final Folder<U> root;
		
		public VirtualFileSystem_SingleRoot(Folder<U> inRoot)
		{
			this.root = inRoot;
		}

		@Override
		protected Folder<U> getRootFolder(String inIn)
		{
			if(inIn.equals(this.root.getName()))
			{
				return this.root;
			}
			else
			{
				throw new IllegalArgumentException("Root: " + this.root.getName() + " tested against: " + inIn);
			}
		}
	}
	
	private static class VirtualFileSystem_MultiRoots<U> extends VirtualFileSystem<U>
	{
		private final LinkedRadixMap<Folder<U>> roots;
		
		public VirtualFileSystem_MultiRoots(String... roots)
		{
			final LinkedRadixMap<Folder<U>> result = new LinkedRadixMap<Folder<U>>();
			
			for(String root: roots)
			{
				result.put(root, new Folder<U>(root));
			}
			
			this.roots = result;
		}
		
		@Override
		protected Folder<U> getRootFolder(String inIn)
		{
			return this.roots.get(inIn);
		}
	}
}
