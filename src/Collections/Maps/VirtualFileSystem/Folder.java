package Collections.Maps.VirtualFileSystem;

import java.util.Iterator;
import java.util.Map.Entry;

import Collections.Lists.CharList.CharList;
import Collections.Maps.RadixMap.Linked.Generic.LinkedRadixMap;

public class Folder<U>
{
	private final String name;
	private final Folder<U> parent;
	private final LinkedRadixMap<U> files;
	private final LinkedRadixMap<Folder<U>> subFolders;

	public Folder(String inName)
	{
		this(inName, null);
	}

	public Folder(String inName, Folder<U> inParent)
	{
		this.name = inName;
		this.parent = inParent;
		this.files = new LinkedRadixMap<U>();
		this.subFolders = new LinkedRadixMap<Folder<U>>();
	}

	public Folder<U> addFolder(String name)
	{
		final Folder<U> result = new Folder<U>(name, this);
		this.subFolders.put(name, result);

		return result;
	}

	public Folder<U> addFolder(filePath path, String name)
	{
		Folder<U> current = this;
		final Iterator<String> nameIterator = path.getFolderNameIterator();

		if (path.isRelative())
		{
			final String firstFolder = nameIterator.next();

			while (!firstFolder.equals(current.name))
			{
				current = current.parent;
			}
		}

		boolean isFirst = true;

		while (nameIterator.hasNext())
		{
			final String cur = nameIterator.next();

			if (isFirst)
			{
				isFirst = false;

				if (this.name.equals(cur))
				{
					continue;
				}
			}

			if (current.subFolders.containsKey(cur))
			{
				current = current.subFolders.get(cur);
			}
			else
			{
				Folder<U> next = new Folder<U>(cur, current);
				current.subFolders.put(cur, next);
				current = next;

				while (nameIterator.hasNext())
				{
					final String nextName = nameIterator.next();
					next = new Folder<U>(nextName, current);
					current.subFolders.put(nextName, next);
					current = next;
				}
			}
		}

		if (current.subFolders.containsKey(name))
		{
			current = current.subFolders.get(name);
		}
		else
		{
			Folder<U> next = new Folder<U>(name, current);
			current.subFolders.put(name, next);
			current = next;
		}

		return current;
	}

	public U addFile(String name, U file)
	{
		this.files.put(name, file);
		return file;
	}

	/**
	 * Expects fileName to be appended to path
	 * 
	 * @param path
	 * @param file
	 * @return
	 */
	public U addFile(filePath path, U file)
	{
		Folder<U> current = this;
		final Iterator<String> nameIterator = path.getFolderNameIterator();

		if (path.isRelative())
		{
			final String firstFolder = nameIterator.next();

			while (!firstFolder.equals(current.name))
			{
				current = current.parent;
			}
		}

		boolean isFirst = true;

		while (nameIterator.hasNext())
		{
			final String cur = nameIterator.next();

			if (isFirst)
			{
				isFirst = false;

				if (this.name.equals(cur))
				{
					continue;
				}
			}

			if (current.subFolders.containsKey(cur))
			{
				current = current.subFolders.get(cur);
			}
			else
			{
				Folder<U> next = new Folder<U>(cur, current);
				current.subFolders.put(cur, next);
				current = next;

				while (nameIterator.hasNext())
				{
					final String nextName = nameIterator.next();
					next = new Folder<U>(nextName, current);
					current.subFolders.put(nextName, next);
					current = next;
				}
			}
		}

		current.files.put(path.getFileName(), file);

		return file;
	}

	private Folder<U> getFolder(filePath path, boolean ignoreFirst)
	{
		Folder<U> current = this;
		final Iterator<String> nameIterator = path.getFolderNameIterator();

		if (path.isRelative())
		{
			final String firstFolder = nameIterator.next();

			while (!firstFolder.equals(current.name))
			{
				current = current.parent;
			}
		}

		while (nameIterator.hasNext())
		{
			final String cur = nameIterator.next();

			if (!ignoreFirst)
			{
				if (current.subFolders.containsKey(cur))
				{
					current = current.subFolders.get(cur);
				}
				else
				{
					Folder<U> next = new Folder<U>(cur, current);
					current.subFolders.put(cur, next);
					current = next;

					while (nameIterator.hasNext())
					{
						final String nextName = nameIterator.next();
						next = new Folder<U>(nextName, current);
						current.subFolders.put(nextName, next);
						current = next;
					}
				}
			}
			else
			{
				ignoreFirst = false;
			}
		}

		return current;
	}

	public U getFile(filePath path)
	{
		return this.getFile(path, true);
	}

	public U getFile(filePath path, boolean ignoreFirst)
	{
		final Folder<U> current = this.getFolder(path, ignoreFirst);

		return current.files.get(path.getFileName());
	}

	public U getFile(filePath absoluteReference, filePath relative)
	{
		final Folder<U> ref = this.getFolder(absoluteReference, true);
		return ref.getFile(relative, false);
	}

	public String getName()
	{
		return this.name;
	}

	public CharList toCharList(int offset, boolean startOnNewLine, boolean onlyShowFileNames)
	{
		final CharList result = new CharList();

		if (startOnNewLine)
		{
			result.addNewLine();
			result.add('\t', offset);
		}

		result.add("<Folder> ");
		result.add(this.name);
		result.add('{');

		if (!this.files.isEmpty())
		{
			for (Entry<String, U> entry : this.files.stringKeyedEntrySet())
			{
				result.addNewLine();
				result.add('\t', offset + 1);

				result.add("<File>   ");
				result.add(entry.getKey());

				if (!onlyShowFileNames)
				{
					result.add(": ");
					result.add(entry.getValue().toString());
				}
			}
		}

		if (!this.subFolders.isEmpty())
		{
			for (Entry<String, Folder<U>> entry : this.subFolders.stringKeyedEntrySet())
			{
				result.add(entry.getValue().toCharList(offset + 1, true, onlyShowFileNames), true);
			}
		}

		result.addNewLine();
		result.add('\t', offset);
		result.add('}');

		return result;
	}
}
