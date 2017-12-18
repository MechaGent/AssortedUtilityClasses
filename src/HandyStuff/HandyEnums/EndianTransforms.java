package HandyStuff.HandyEnums;

import CustomExceptions.UnhandledEnumException;

public enum EndianTransforms
{
	KeepBitsKeepBytes,
	KeepBitsFlipBytes,
	FlipBitsKeepBytes,
	FlipBitsFlipBytes;

	public static final EndianTransforms getTransform(EndianSettings input, EndianSettings output)
	{
		if (input == output)
		{
			return KeepBitsKeepBytes;
		}

		final EndianTransforms result;

		switch (input)
		{
			case LeftBitLeftByte:
			{
				switch (output)
				{
					case LeftBitRightByte:
					{
						result = EndianTransforms.KeepBitsFlipBytes;
						break;
					}
					case RightBitLeftByte:
					{
						result = EndianTransforms.FlipBitsKeepBytes;
						break;
					}
					case RightBitRightByte:
					{
						result = EndianTransforms.FlipBitsFlipBytes;
						break;
					}
					case LeftBitLeftByte:
					default:
					{
						throw new UnhandledEnumException(output);
					}
				}
				
				break;
			}
			case LeftBitRightByte:
			{
				switch (output)
				{
					case LeftBitLeftByte:
					{
						result = EndianTransforms.KeepBitsFlipBytes;
						break;
					}
					case RightBitLeftByte:
					{
						result = EndianTransforms.FlipBitsFlipBytes;
						break;
					}
					case RightBitRightByte:
					{
						result = EndianTransforms.FlipBitsKeepBytes;
						break;
					}
					case LeftBitRightByte:
					default:
					{
						throw new UnhandledEnumException(output);
					}
				}

				break;
			}
			case RightBitLeftByte:
			{
				switch (output)
				{
					case LeftBitLeftByte:
					{
						result = EndianTransforms.FlipBitsKeepBytes;
						break;
					}
					case LeftBitRightByte:
					{
						result = EndianTransforms.FlipBitsFlipBytes;
						break;
					}
					case RightBitRightByte:
					{
						result = EndianTransforms.KeepBitsFlipBytes;
						break;
					}
					case RightBitLeftByte:
					default:
					{
						throw new UnhandledEnumException(output);
					}
				}

				break;
			}
			case RightBitRightByte:
			{
				switch (output)
				{
					case LeftBitLeftByte:
					{
						result = EndianTransforms.FlipBitsFlipBytes;
						break;
					}
					case LeftBitRightByte:
					{
						result = EndianTransforms.FlipBitsKeepBytes;
						break;
					}
					case RightBitLeftByte:
					{
						result = EndianTransforms.KeepBitsFlipBytes;
						break;
					}
					case RightBitRightByte:
					default:
					{
						throw new UnhandledEnumException(output);
					}
				}

				break;
			}
			default:
			{
				throw new UnhandledEnumException(input);
			}
		}

		return result;
	}
}
