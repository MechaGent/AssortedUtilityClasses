package Collections.Maps.VirtualFileSystem;

import java.util.Iterator;

import Collections.Lists.CharList.CharList;

public class FullArrFilePath implements filePath
{
	private final String[] path;
	private final boolean isRelative;
	private final boolean endsWithFile;

	public FullArrFilePath(String inPath, boolean inIsRelative, boolean inEndsWithFile)
	{
		this(inPath.split("/"), inIsRelative, inEndsWithFile);
	}

	public FullArrFilePath(String[] inPath, boolean inIsRelative, boolean inEndsWithFile)
	{
		this.path = inPath;
		this.isRelative = inIsRelative;
		this.endsWithFile = inEndsWithFile;
	}

	@Override
	public Iterator<String> iterator()
	{
		return new NameIterator(this);
	}

	@Override
	public Iterator<String> getNameIterator()
	{
		return new NameIterator(this);
	}

	@Override
	public Iterator<String> getFolderNameIterator()
	{
		if (this.endsWithFile)
		{
			return new NameIterator(this, true);
		}
		else
		{
			return new NameIterator(this);
		}
	}

	@Override
	public String getRoot()
	{
		if (this.path.length != 0)
		{
			return this.path[0];
		}
		else
		{
			return "";
		}
	}

	@Override
	public String getFileName()
	{
		if (this.path.length != 0)
		{
			return this.path[this.path.length - 1];
		}
		else
		{
			return "";
		}
	}

	@Override
	public boolean isRelative()
	{
		return this.isRelative;
	}

	@Override
	public boolean endsWithFile()
	{
		return this.endsWithFile;
	}

	@Override
	public String toString()
	{
		return this.toCharList().toString();
	}

	@Override
	public CharList toCharList()
	{
		final CharList result = new CharList();

		if (this.isRelative)
		{
			for (int i = 0; i < this.path.length; i++)
			{
				result.add('/');
				result.add(this.path[i]);
			}
		}
		else
		{
			for (int i = 0; i < this.path.length; i++)
			{
				if (i != 0)
				{
					result.add('/');
				}

				result.add(this.path[i]);
			}
		}

		if (!this.endsWithFile)
		{
			result.add('/');
		}

		return result;
	}

	private static class NameIterator implements Iterator<String>
	{
		private final String[] path;
		private int place;
		private final boolean stopBeforeFile;

		public NameIterator(FullArrFilePath in)
		{
			this(in.path);
		}

		public NameIterator(FullArrFilePath in, boolean inStopBeforeFile)
		{
			this(in.path, inStopBeforeFile);
		}

		public NameIterator(String[] in)
		{
			this(in, false);
		}

		public NameIterator(String[] in, boolean inStopBeforeFile)
		{
			this.path = in;
			this.place = 0;
			this.stopBeforeFile = inStopBeforeFile;
		}

		@Override
		public boolean hasNext()
		{
			if (this.stopBeforeFile)
			{
				return this.place < this.path.length-1;
			}
			else
			{
				return this.place < this.path.length;
			}
		}

		@Override
		public String next()
		{
			return this.path[this.place++];
		}
	}
}
