package Collections.Maps.RadixMap.Unlinked.Generic;

/**
 * 
 * for iterators
 *
 * @param <U>
 */
abstract class StackFrame<U>
{
	protected final Node<U> node;
	protected int index;
	private int numKidsLeft;

	protected StackFrame(Node<U> inNode)
	{
		this(inNode, inNode.getNumChildren());
	}

	protected StackFrame(Node<U> inNode, int numKids)
	{
		this.node = inNode;
		this.index = 0;
		this.numKidsLeft = numKids;
	}
	
	protected StackFrame(Node<U> inNode, int numKids, int index)
	{
		this.node = inNode;
		this.index = index;
		this.numKidsLeft = numKids;
	}
	
	protected abstract void incrementIndex();
	
	protected int getAndIncrementIndex()
	{
		final int result = this.index;
		this.incrementIndex();
		return result;
	}

	protected final void initStackFrame()
	{
		switch (this.numKidsLeft)
		{
			case 0:
			{
				this.index = 16;
				break;
			}
			case 1:
			{
				while (this.index < 16 && !this.node.hasChildAt(this.index))
				{
					this.incrementIndex();
				}

				this.numKidsLeft = 0;

				break;
			}
			default:
			{
				while (this.index < 16 && !this.node.hasChildAt(this.index))
				{
					this.incrementIndex();
				}

				this.numKidsLeft -= 1;
				break;
			}
		}
	}
	
	Node<U> getNextNode()
	{
		final Node<U> result = this.node.getChildAt(this.getAndIncrementIndex());
		this.numKidsLeft--;

		while (this.index < 16 && !this.node.hasChildAt(this.index))
		{
			this.incrementIndex();
		}

		return result;
	}

	final boolean hasRemaining()
	{
		return this.index < 16 && this.numKidsLeft != 0;
	}
	
	final int getNodeKeyChunkLength()
	{
		return this.node.getKeyChunkLength();
	}
	
	static class StackFrame_Normal<U> extends StackFrame<U>
	{
		private StackFrame_Normal(Node<U> inNode)
		{
			super(inNode);
		}

		protected StackFrame_Normal(Node<U> inNode, int inNumKids)
		{
			super(inNode, inNumKids);
		}
		
		static final <U> StackFrame_Normal<U> getInstance(Node<U> inNode)
		{
			return getInstance(inNode, inNode.getNumChildren());
		}
		
		static <U> StackFrame_Normal<U> getInstance(Node<U> inNode, int numKids)
		{
			final StackFrame_Normal<U> result = new StackFrame_Normal<U>(inNode, numKids);
			result.initStackFrame();
			return result;
		}

		@Override
		protected final void incrementIndex()
		{
			this.index += 1;
		}
	}
	
	static class StackFrame_Normal_FirstNode<U> extends StackFrame_Normal<U>
	{
		protected StackFrame_Normal_FirstNode(Node<U> inNode, int inNumKids)
		{
			super(inNode, -inNumKids);
		}
		
		static final <U> StackFrame_Normal_FirstNode<U> getInstance(Node<U> inNode, int numKids)
		{
			final StackFrame_Normal_FirstNode<U> result = new StackFrame_Normal_FirstNode<U>(inNode, numKids);
			return result;
		}

		@Override
		protected final int getAndIncrementIndex()
		{
			if(this.index < 0)
			{
				this.index = -this.index;
				return -1;
			}
			else
			{
				return super.getAndIncrementIndex();
			}
		}

		@Override
		final Node<U> getNextNode()
		{
			final int index = this.getAndIncrementIndex();
			
			if(index == -1)
			{
				return this.node;
			}
			
			return super.getNextNode();
		}
	}
	
	static class StackFrame_Root<U> extends StackFrame<U>
	{
		private StackFrame_Root(Node<U> inNode)
		{
			super(inNode);
		}

		private StackFrame_Root(Node<U> inNode, int inNumKids)
		{
			super(inNode, inNumKids);
		}
		
		static final <U> StackFrame_Root<U> getInstance(Node<U> inNode)
		{
			return getInstance(inNode, inNode.getNumChildren());
		}
		
		static final <U> StackFrame_Root<U> getInstance(Node<U> inNode, int numKids)
		{
			final StackFrame_Root<U> result = new StackFrame_Root<U>(inNode, numKids);
			result.initStackFrame();
			return result;
		}
		
		@Override
		protected final void incrementIndex()
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
		}
	}
}
