package HandyStuff.HandyEnums;

import CustomExceptions.UnhandledEnumException;

public enum EndianSettings
{
	/**
	 * Java default
	 * big-endian
	 */
	LeftBitLeftByte,
	
	/**
	 * C++ default
	 * little-endian
	 */
	LeftBitRightByte,
	RightBitLeftByte,
	RightBitRightByte;

	/**
	 * 
	 * @param mode
	 * @return
	 */
	public static final EndianSettings getDefault(String mode)
	{
		final EndianSettings result;

		switch (mode)
		{
			case "Java":
			case "Big Endian":
			{
				result = LeftBitLeftByte;
				break;
			}
			case "C++":
			case "Little Endian":
			{
				result = LeftBitRightByte;
				break;
			}
			default:
			{
				throw new IllegalArgumentException("Unverified mode: " + mode);
			}
		}

		return result;
	}

	public final EndianSettings getFlippedByteSetting()
	{
		final EndianSettings result;

		switch (this)
		{
			case LeftBitLeftByte:
			{
				result = LeftBitRightByte;
				break;
			}
			case LeftBitRightByte:
			{
				result = LeftBitLeftByte;
				break;
			}
			case RightBitLeftByte:
			{
				result = RightBitRightByte;
				break;
			}
			case RightBitRightByte:
			{
				result = RightBitLeftByte;
				break;
			}
			default:
			{
				throw new UnhandledEnumException(this);
			}

		}

		return result;
	}
}
