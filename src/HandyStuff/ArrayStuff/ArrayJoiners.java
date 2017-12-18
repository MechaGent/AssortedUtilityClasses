package HandyStuff.ArrayStuff;

public class ArrayJoiners
{
	public static final char[] join(char[] arr0, char[] arr1)
	{
		final char[] result = new char[arr0.length + arr1.length];

		System.arraycopy(arr0, 0, result, 0, arr0.length);
		System.arraycopy(arr1, 0, result, arr0.length, arr1.length);

		return result;
	}

	public static final char[] join(char[] arr0, char delimiter, char[] arr1)
	{
		final char[] result = new char[arr0.length + 1 + arr1.length];

		System.arraycopy(arr0, 0, result, 0, arr0.length);
		result[arr0.length + 1] = delimiter;
		System.arraycopy(arr1, 0, result, arr0.length + 1, arr1.length);

		return result;
	}

	public static final char[] join(char[] arr0, char[] delimiter, char[] arr1)
	{
		final char[] result = new char[arr0.length + arr1.length];

		int offset = arr0.length;
		System.arraycopy(arr0, 0, result, 0, offset);
		System.arraycopy(delimiter, 0, result, offset, delimiter.length);
		offset += delimiter.length;
		System.arraycopy(arr1, 0, result, offset, arr1.length);

		return result;
	}
}
