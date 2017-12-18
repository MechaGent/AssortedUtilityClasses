package Collections.Maps.RadixMap.Linked.Generic;

import CustomExceptions.UnhandledEnumException;

class LinkedStackFrame<U>
{
	private final LinkedNode<U> node;
	private final IncrementModes incrementMode;
	protected int index;
	private int numKidsLeft;

	LinkedStackFrame(LinkedNode<U> inNode)
	{
		this(inNode, IncrementModes.Normal);
	}

	LinkedStackFrame(LinkedNode<U> inNode, LinkedStackFrame.IncrementModes inIncrementMode)
	{
		this.node = inNode;
		this.incrementMode = inIncrementMode;

		switch (inIncrementMode)
		{
			case Normal:
			{
				this.index = 0;
				break;
			}
			case StartAtHalfAndWrap:
			{
				this.index = 8;
				break;
			}
			default:
			{
				throw new UnhandledEnumException(inIncrementMode);
			}
		}
		
		this.numKidsLeft = inNode.getNumChildren();

		while (this.index < 16 && !this.node.hasChildAt(this.index))
		{
			this.incrementIndex();
		}
	}

	protected final int getNodeKeyChunkLength()
	{
		return this.node.getKeyChunkLength();
	}

	protected void incrementIndex()
	{
		switch (this.incrementMode)
		{
			case Normal:
			{
				this.index++;
				break;
			}
			case StartAtHalfAndWrap:
			{
				switch (this.index)
				{
					case 7:
					{
						this.index = 16;
						break;
					}
					case 15:
					{
						this.index = 0;
						break;
					}
					default:
					{
						this.index++;
						break;
					}
				}
				break;
			}
			default:
			{
				throw new UnhandledEnumException(this.incrementMode);
			}
		}
	}

	private final int getAndIncrementIndex()
	{
		final int result = this.index;
		this.incrementIndex();
		return result;
	}

	public final LinkedNode<U> getNextChildNode()
	{
		final LinkedNode<U> result = this.node.getChildAt(this.getAndIncrementIndex());
		this.numKidsLeft--;

		while (this.index < 16 && !this.node.hasChildAt(this.index))
		{
			this.incrementIndex();
		}

		return result;
	}

	public final boolean hasKidsLeft()
	{
		return this.numKidsLeft > 0;
	}

	public static enum IncrementModes
	{
		Normal,
		StartAtHalfAndWrap;
	}
}