package Collections.Lists.CharList;

import CustomExceptions.UnhandledEnumException;

final class CharListTester_Performance
{
	private final BaseCharList variable;
	private final StringBuilder control;
	private final IgnoreStates ignoreState;
	private long size;

	public CharListTester_Performance()
	{
		this(IgnoreStates.ignoreNone);
	}
	
	public CharListTester_Performance(IgnoreStates ignoreState)
	{
		this.variable = new BaseCharList();
		this.control = new StringBuilder();
		this.ignoreState = ignoreState;
		this.size = 0;
	}

	@Override
	public final String toString()
	{
		final String result;
		
		switch (this.ignoreState)
		{
			case ignoreControl:
			{
				result = this.variable.toString();
				break;
			}
			case ignoreNone:
			{
				result = this.variable.toString() + "\r\n" + this.control.toString();
				break;
			}
			case ignoreVariable:
			{
				result = this.control.toString();
				break;
			}
			default:
			{
				throw new UnhandledEnumException(this.ignoreState);
			}
		}
		
		return result;
	}

	public final void add(char in)
	{
		switch (this.ignoreState)
		{
			case ignoreControl:
			{
				this.variable.add(in);
				break;
			}
			case ignoreNone:
			{
				this.control.append(in);
				this.variable.add(in);
				break;
			}
			case ignoreVariable:
			{
				this.control.append(in);
				break;
			}
			default:
			{
				throw new UnhandledEnumException(this.ignoreState);
			}
		}

		this.size += 1;
	}

	public final void add(char in, int numRepeats)
	{
		switch (this.ignoreState)
		{
			case ignoreControl:
			{
				this.variable.add(in, numRepeats);
				break;
			}
			case ignoreNone:
			{
				for (int i = 0; i < numRepeats; i++)
				{
					this.control.append(in);
				}

				this.variable.add(in, numRepeats);
				break;
			}
			case ignoreVariable:
			{
				for (int i = 0; i < numRepeats; i++)
				{
					this.control.append(in);
				}
				break;
			}
			default:
			{
				throw new UnhandledEnumException(this.ignoreState);
			}
		}

		this.size += numRepeats;
	}

	public final void add(char[] in)
	{
		switch (this.ignoreState)
		{
			case ignoreControl:
			{
				this.variable.add(in);
				break;
			}
			case ignoreNone:
			{
				this.control.append(in);
				this.variable.add(in);
				break;
			}
			case ignoreVariable:
			{
				this.control.append(in);
				break;
			}
			default:
			{
				throw new UnhandledEnumException(this.ignoreState);
			}
		}

		this.size += in.length;
	}

	public final void add(char[] in, int numRepeats)
	{
		switch (this.ignoreState)
		{
			case ignoreControl:
			{
				this.variable.add(in, numRepeats);
				break;
			}
			case ignoreNone:
			{
				for (int i = 0; i < numRepeats; i++)
				{
					this.control.append(in);
				}

				this.variable.add(in, numRepeats);
				break;
			}
			case ignoreVariable:
			{
				for (int i = 0; i < numRepeats; i++)
				{
					this.control.append(in);
				}
				break;
			}
			default:
			{
				throw new UnhandledEnumException(this.ignoreState);
			}
		}

		this.size += in.length * numRepeats;
	}

	public final void add(String in)
	{
		switch (this.ignoreState)
		{
			case ignoreControl:
			{
				this.variable.add(in);
				break;
			}
			case ignoreNone:
			{
				this.control.append(in);
				this.variable.add(in);
				break;
			}
			case ignoreVariable:
			{
				this.control.append(in);
				break;
			}
			default:
			{
				throw new UnhandledEnumException(this.ignoreState);
			}
		}

		this.size += in.length();
	}

	public final void add(String in, int numRepeats)
	{
		switch (this.ignoreState)
		{
			case ignoreControl:
			{
				this.variable.add(in, numRepeats);
				break;
			}
			case ignoreNone:
			{
				for (int i = 0; i < numRepeats; i++)
				{
					this.control.append(in);
				}

				this.variable.add(in, numRepeats);
				break;
			}
			case ignoreVariable:
			{
				for (int i = 0; i < numRepeats; i++)
				{
					this.control.append(in);
				}
				
				break;
			}
			default:
			{
				throw new UnhandledEnumException(this.ignoreState);
			}
		}

		this.size += in.length() * numRepeats;
	}

	public final long size()
	{
		if ((this.ignoreState == IgnoreStates.ignoreNone) && (this.variable.size() != this.control.length()))
		{
			throw new IndexOutOfBoundsException("size_variable: " + this.variable.size() + "\r\nsize_control: " + this.control.length());
		}

		return this.size;
	}

	public final void printResults()
	{
		switch (this.ignoreState)
		{
			case ignoreControl:
			{
				System.out.println("string test successful!");
				break;
			}
			case ignoreNone:
			{
				final String varString = this.variable.toString();
				final String conString = this.control.toString();

				if (varString.equals(conString))
				{
					System.out.println("strings match!");
				}
				else
				{
					System.out.println("strings do not match!\r\n\t" + varString + "\r\n\t" + conString);
				}
				break;
			}
			case ignoreVariable:
			{
				System.out.println("string test successful!");
				break;
			}
			default:
			{
				throw new UnhandledEnumException(this.ignoreState);
			}
		}
	}

	public static enum IgnoreStates
	{
		ignoreNone,
		ignoreVariable,
		ignoreControl;
	}
}
