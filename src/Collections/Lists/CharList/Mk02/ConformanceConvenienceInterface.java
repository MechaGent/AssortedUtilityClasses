package Collections.Lists.CharList.Mk02;

import Collections.Lists.Generic.Directed.SingleLinkedList;
import Collections.PackedArrays.HalfByte.HalfByteArray;
import Collections.PrimitiveInterfaceAnalogues.Char.AbstractCharIterator;

interface ConformanceConvenienceInterface
{
	int size();

	boolean isEmpty();

	boolean isNotEmpty();

	char charAt(int index);

	char firstChar();

	char lastChar();

	linearCharsIterator getLinearCharsIterator();

	String toString();

	char[] toCharArray();

	HalfByteArray toHalfByteArray();

	void push(char in);

	void push(char[] in);

	void push(char in, int numRepeats);

	void push(char[] in, int numRepeats);

	void push(String in);

	void push(String[] in);

	void push(String in, int numRepeats);

	void push(String[] in, int numRepeats);

	void push(String[] in, char delimiter);

	void push(String[] in, String delimiter);

	void push(CharList in, boolean canConsume);

	/**
	 * presumes non-consumption
	 * 
	 * @param in
	 * @param numRepeats
	 */
	void push(CharList in, int numRepeats);
	
	void push_asBinString(int in);

	void push_asBinString(long in);

	void push_asDecString(int in);

	void push_asDecString(long in);

	void push_asHexString(int in);

	void push_asHexString(long in);

	void push_asBinString(int in, int minNumChars);

	void push_asBinString(long in, int minNumChars);

	void push_asDecString(int in, int minNumChars);

	void push_asDecString(long in, int minNumChars);

	void push_asHexString(int in, int minNumChars);

	void push_asHexString(long in, int minNumChars);

	void pushAsString(HalfByteArray in);

	void pushNewLine();

	void pushNewLine(int numRepeats);

	void pushNewIndentedLine(int offset);

	void add(char in);

	void add(char[] in);

	void add(char in, int numRepeats);

	void add(char[] in, int numRepeats);

	void add(String in);

	void add(String[] in);

	void add(String in, int numRepeats);

	void add(String[] in, int numRepeats);

	void add(String[] in, char delimiter);

	void add(String[] in, String delimiter);

	void add(CharList in, boolean canConsume);

	/**
	 * presumes non-consumption
	 * 
	 * @param in
	 * @param numRepeats
	 */
	void add(CharList in, int numRepeats);

	void add_asBinString(int in);

	void add_asBinString(long in);

	void add_asDecString(int in);

	void add_asDecString(long in);

	void add_asHexString(int in);

	void add_asHexString(long in);

	void add_asBinString(int in, int minNumChars);

	void add_asBinString(long in, int minNumChars);

	void add_asDecString(int in, int minNumChars);

	void add_asDecString(long in, int minNumChars);

	void add_asHexString(int in, int minNumChars);

	void add_asHexString(long in, int minNumChars);

	void addAsString(boolean in);

	void addAsString(int in);

	void addAsString(long in);

	void addAsString(double in);

	void addAsString(HalfByteArray in);

	void addAsString(Object in);

	void addAsPaddedString(String in, int minLength, boolean padToFront);

	void addAsPaddedString(String in, char pad, int minLength, boolean padToFront);

	void addNewLine();

	void addNewLine(int numRepeats);

	void addNewIndentedLine(int offset);

	AbstractCharIterator getCharIterator();

	CharList[] splitAtFirst(char delim);

	SingleLinkedList<CharList> splitAt(char delim);

	/**
	 * 
	 * @return the same as calling parseSelfAsBoolean(true);
	 */
	boolean parseSelfAsBoolean();

	boolean parseSelfAsBoolean(boolean parseRigidly);

	/**
	 * 
	 * @return {@code false} if {@code this} == "f", "F", "false", "False", "0", or is empty, {@code true} otherwise
	 */
	boolean parseSelfAsLooseBoolean();

	/**
	 * 
	 * @return {@code true} if {@code this} == "t", "T", "true", "True", "1", {@code false} if {@code this} == "f", "F", "false", "False", "0", throws BadParseException otherwise
	 */
	boolean parseSelfAsRigidBoolean();

}