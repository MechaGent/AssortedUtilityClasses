package Collections.Maps.JSONish.Backend;

import java.util.Iterator;
import java.util.Map.Entry;

import Collections.Lists.CharList.CharList;

import java.util.Set;

public interface LotusObject extends LotusFormat
{
	public LotusFormat getVar(String key);
	public LotusObject getVarAsObject(String key);
	public LotusArray getVarAsArray(String key);
	public LotusVar getVarAsVar(String key);
	
	public LotusFormat getVar(CharList key);
	public LotusObject getVarAsObject(CharList key);
	public LotusArray getVarAsArray(CharList key);
	public LotusVar getVarAsVar(CharList key);
	
	public int size();
	
	public Set<Entry<String, LotusFormat>> getEntrySet_StringKeys();
	public Iterator<Entry<String, LotusFormat>> getEntryIterator_StringKeys();
	public Set<Entry<CharList, LotusFormat>> getEntrySet_CharListKeys();
	public Iterator<Entry<CharList, LotusFormat>> getEntryIterator_CharListKeys();
	
	/*
	public static LotusObject getUnionOfComplements(LotusObject objA, LotusObject objB)
	{
		final Iterator<Entry<String, LotusFormat>> itsyA = objA.getEntryIterator_StringKeys();
		final Iterator<Entry<String, LotusFormat>> itsyB = objB.getEntryIterator_StringKeys();
		final HalfByteRadixMap<LotusFormat> result = new HalfByteRadixMap<LotusFormat>();
		
		int hybridState = getComboNextState(itsyA, itsyB);
		
		while(hybridState != 0)
		{
			switch(hybridState)
			{
				case 1:	//A, but not B, has next
				{
					break;
				}
				case 2:	//B, but not A, has next
				{
					break;
				}
				case 3:	//both have next
				{
					break;
				}
				default:
				{
					throw new IllegalArgumentException();
				}
			}
			
			hybridState = getComboNextState(itsyA, itsyB);
		}
	}
	
	/**
	 * cases:
	 * <br> 0 == itsyA and itsyB both haveNext
	 * 
	 * @param itsyA
	 * @param itsyB
	 * @return
	 *
	static int getComboNextState(Iterator<Entry<String, LotusFormat>> itsyA, Iterator<Entry<String, LotusFormat>> itsyB)
	{
		int result = 0;
		
		if(itsyA.hasNext())
		{
			result |= 0x01;
		}
		
		if(itsyB.hasNext())
		{
			result |= 0x02;
		}
		
		return result;
	}
	*/
}
