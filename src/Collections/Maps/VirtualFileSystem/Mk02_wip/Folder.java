package Collections.Maps.VirtualFileSystem.Mk02_wip;

import java.util.Iterator;
import java.util.Map.Entry;

import Collections.Lists.CharList.CharList;
import Collections.Maps.RadixMap.Linked.Generic.LinkedRadixMap;
import Collections.PackedArrays.HalfByte.HalfByteArray;
import Collections.PackedArrays.HalfByte.HalfByteArrayFactory;
import CustomExceptions.UnhandledEnumException;

public class Folder<U>
{
	private HalfByteArray folderName; // is actually a String-backed HalfByteArray
	private Folder<U> parent;
	private final LinkedRadixMap<U> files;
	private final LinkedRadixMap<Folder<U>> subFolders;

	public Folder(String inName)
	{
		this(inName, null);
	}

	public Folder(String inName, Folder<U> inParent)
	{
		this(HalfByteArrayFactory.wrapIntoArray(inName), inParent);
	}

	public Folder(HalfByteArray inName)
	{
		this(inName, null);
	}

	public Folder(HalfByteArray inName, Folder<U> inParent)
	{
		this.folderName = inName;
		this.parent = inParent;
		this.files = new LinkedRadixMap<U>();
		this.subFolders = new LinkedRadixMap<Folder<U>>();
	}

	public HalfByteArray getFolderName()
	{
		return this.folderName;
	}

	public String getFolderName_asString()
	{
		return this.folderName.interpretAsCharString();
	}
	
	public CharList getFolderNameAndSubCounts()
	{
		final CharList result = new CharList();
		
		result.add(this.getFolderName_asString());
		result.add('(');
		result.add_asDecString(this.files.size());
		result.add(", ");
		result.add_asDecString(this.subFolders.size());
		result.add(')');
		
		return result;
	}

	public void setFolderName(String inFolderName)
	{
		this.folderName = HalfByteArrayFactory.wrapIntoArray(inFolderName);
	}

	public void setFolderName(HalfByteArray inFolderName)
	{
		this.folderName = inFolderName;
	}

	public Folder<U> getParent()
	{
		return this.parent;
	}

	public void setParent(Folder<U> inParent)
	{
		this.parent = inParent;
	}

	public boolean hasFile(String fileName)
	{
		return this.files.containsKey(fileName);
	}

	public boolean hasFile(HalfByteArray fileName)
	{
		return this.files.containsKey(fileName);
	}

	public boolean hasFiles(boolean wantsDirectFilesOnly)
	{
		if (wantsDirectFilesOnly)
		{
			return this.files.isNotEmpty();
		}
		else
		{
			return this.hasFiles_wantsAll();
		}
	}

	private final boolean hasFiles_wantsAll()
	{
		if (this.files.isNotEmpty())
		{
			return true;
		}
		else if (this.subFolders.isEmpty())
		{
			return false;
		}
		else
		{
			final Iterator<Folder<U>> itsy = this.subFolders.valuesIterator();

			while (itsy.hasNext())
			{
				if (itsy.next().hasFiles_wantsAll())
				{
					return true;
				}
			}

			return false;
		}
	}

	public U getFile(String fileName)
	{
		return this.files.get(fileName);
	}

	public U getFile(HalfByteArray fileName)
	{
		return this.files.get(fileName);
	}

	public final void addFile(String fileName, U in)
	{
		this.files.put(fileName, in);
	}

	public final void addFile(HalfByteArray fileName, U in)
	{
		this.files.put(fileName, in);
	}

	public boolean hasSubFolder(String folderName)
	{
		return this.subFolders.containsKey(folderName);
	}

	public boolean hasSubFolder(HalfByteArray folderName)
	{
		return this.subFolders.containsKey(folderName);
	}

	public Folder<U> getSubFolder(String folderName)
	{
		return this.subFolders.get(folderName);
	}

	public Folder<U> getSubFolder(HalfByteArray folderName)
	{
		return this.subFolders.get(folderName);
	}

	public final Folder<U> createSubFolder(String subFolderName)
	{
		return this.addSubFolder(new Folder<U>(subFolderName, this));
	}

	public final Folder<U> addSubFolder(Folder<U> in)
	{
		this.subFolders.put(in.folderName, in);
		return in;
	}

	public final CharList toCharList()
	{
		return this.toCharList(0);
	}

	public final CharList toCharList(int offset)
	{
		return this.toCharList(offset, PrintOptions.Name_Files);
	}

	public final CharList toCharList(int offset, PrintOptions option)
	{
		final CharList result = new CharList();
		
		result.add('\t', offset);

		switch (option)
		{
			case Files:
			{
				this.addFilesToCharList(result, offset);
				
				break;
			}
			case Files_Subfolders:
			{
				this.addFilesToCharList(result, offset);
				result.addNewIndentedLine(offset);
				this.addSubFolderNamesToCharList(result, offset);
				
				break;
			}
			case Name:
			{
				result.add(this.getFolderNameAndSubCounts(), true);
				break;
			}
			case Name_Files:
			{
				result.add(this.getFolderNameAndSubCounts(), true);
				result.addNewIndentedLine(offset);
				this.addFilesToCharList(result, offset);
				break;
			}
			case Name_Files_Subfolders:
			{
				result.add(this.getFolderNameAndSubCounts(), true);
				result.addNewIndentedLine(offset);
				this.addFilesToCharList(result, offset);
				result.addNewIndentedLine(offset);
				this.addSubFolderNamesToCharList(result, offset);
				break;
			}
			case Name_Subfolders:
			{
				result.add(this.getFolderNameAndSubCounts(), true);
				result.addNewIndentedLine(offset);
				this.addSubFolderNamesToCharList(result, offset);
				break;
			}
			case ObjectId:
			{
				result.add(super.toString());
				break;
			}
			case Subfolders:
			{
				this.addSubFolderNamesToCharList(result, offset);
				break;
			}
			default:
			{
				throw new UnhandledEnumException(option);
			}
		}

		return result;
	}
	
	private final void addFilesToCharList(CharList result, int offset)
	{
		result.add('{');
		
		for(Entry<HalfByteArray, U> entry: this.files)
		{
			result.addNewIndentedLine(offset + 1);
			result.add(entry.getKey().interpretAsChars(), true);
			result.add(": ");
			result.add(entry.getValue().toString());
		}
		
		if(this.files.isNotEmpty())
		{
			result.addNewIndentedLine(offset);
		}
		
		result.add('}');
	}
	
	private final void addSubFolderNamesToCharList(CharList result, int offset)
	{
		result.add('{');
		
		for(Entry<HalfByteArray, Folder<U>> entry: this.subFolders)
		{
			result.addNewIndentedLine(offset + 1);
			result.add(entry.getValue().getFolderNameAndSubCounts(), true);
		}
		
		if(this.subFolders.isNotEmpty())
		{
			result.addNewIndentedLine(offset);
		}
		
		result.add('}');
	}

	public static enum PrintOptions
	{
		Name_Files_Subfolders,
		Name_Files,
		Name_Subfolders,
		Name,
		Files_Subfolders,
		Files,
		Subfolders,

		/**
		 * prints this object's Object.toString()
		 */
		ObjectId;
	}
}
