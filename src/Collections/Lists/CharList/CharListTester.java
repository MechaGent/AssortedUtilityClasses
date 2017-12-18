package Collections.Lists.CharList;

interface CharListTester
{
	void add(char in);

	void add(char in, int numRepeats);

	void add(char[] in);

	void add(char[] in, int numRepeats);

	void add(String in);

	void add(String in, int numRepeats);

	boolean charsAgreeAt(long index);

	void printResults();

	long size();
}