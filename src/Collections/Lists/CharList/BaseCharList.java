package Collections.Lists.CharList;

import Collections.PrimitiveInterfaceAnalogues.Char.CharListIterable;

import java.util.Arrays;

import Collections.PrimitiveInterfaceAnalogues.Char.AbstractCharListIterator;
import CustomExceptions.EnhancedArrayIndexOutOfBoundsException;
import CustomExceptions.FatalLazinessException;
import CustomExceptions.UnhandledEnumException;
import HandyStuff.MathAutobot;
import HandyStuff.ArrayStuff.EmptyPrimitiveArrays;
import HandyStuff.ArrayStuff.PrimArraysCopier;

public class BaseCharList implements CharListIterable
{
	private static final boolean diag_checkActiveStates = false;

	private static final int minBufferLength = 16;

	/**
	 * at any value less than {@link Integer#MAX_VALUE}, this is really more of a suggestion than a hard limit
	 */
	private static final int maxBufferLength = 64;
	// Integer.MAX_VALUE;

	private CharNode head;

	private CharNode tail;
	protected long size;

	private char[] addBuffer;
	private int addPos; // holds the next free index into addBuffer

	private char[] pushBuffer;

	private int pushPos; // holds the next free index into pushBuffer

	private ActiveStates activeStates;

	public BaseCharList()
	{
		this(minBufferLength);
	}

	public BaseCharList(int initialCapacity)
	{
		this.head = null;
		this.tail = null;
		this.size = 0;

		this.addBuffer = new char[initialCapacity];
		this.addPos = 0;

		this.pushBuffer = new char[initialCapacity];
		this.pushPos = initialCapacity - 1;

		this.activeStates = ActiveStates.hasNotBeenPushed_hasNoNodes_hasNotBeenAdded;
	}

	public final void add(char in)
	{
		this.add_internal_sizeUnchanged(in);
		this.size += 1;
	}

	final void checkActiveStates()
	{
		if (this.hasBeenPushed() && (this.pushPos == this.pushBuffer.length - 1))
		{
			throw new IllegalStateException("bad push state");
		}

		if (this.hasNodes() && (this.head == null))
		{
			throw new IllegalStateException("bad nodes state");
		}

		if (this.hasBeenAdded() && (this.addPos == 0))
		{
			throw new IllegalStateException("bad add state");
		}
	}

	final void checkActiveStates(int caseNum)
	{
		if (this.hasBeenPushed() && ((this.pushPos == this.pushBuffer.length - 1) || this.hasNullChar(this.pushBuffer, this.pushPos, this.pushBuffer.length - this.pushPos)))
		{
			throw new IllegalStateException("bad push state, case " + caseNum);
		}

		if (this.hasNodes() && (this.head == null))
		{
			throw new IllegalStateException("bad nodes state, case " + caseNum);
		}

		if (this.hasBeenAdded() && ((this.addPos == 0) || this.hasNullChar(this.addBuffer, 0, this.addPos)))
		{
			throw new IllegalStateException("bad add state, case " + caseNum);
		}
	}

	private final boolean hasNullChar(char[] in, int startPos, int length)
	{
		final int excludedIndex = startPos + length;

		while (startPos < excludedIndex)
		{
			if (in[startPos] == 0)
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * this method adds the array without changing the size of this object
	 * 
	 * @param in
	 */
	private final void add_internal_sizeUnchanged(char in)
	{
		if (this.addPos < this.addBuffer.length)
		{
			this.addBuffer[this.addPos] = in;

			if (this.addPos == 0)
			{
				this.activeStates = this.activeStates.toggleAdded();
			}
			else if (diag_checkActiveStates)
			{
				if (!this.activeStates.hasBeenAdded)
				{
					throw new IllegalArgumentException("\r\n\taddPos: " + this.addPos + "\r\n\tcargo: " + in);
				}
			}

			this.addPos += 1;
		}
		else
		{
			if (this.addPos < maxBufferLength)
			{
				final char[] nextBuffer = new char[this.addBuffer.length * 2];
				System.arraycopy(this.addBuffer, 0, nextBuffer, 0, this.addBuffer.length);
				nextBuffer[this.addPos++] = in;
				this.setAddBuffer(nextBuffer);
			}
			else // if (this.addPos >= maxBufferLength)
			{
				this.addNode_internal(this.addBuffer);
				this.setAddBuffer(new char[minBufferLength]);
				this.addBuffer[0] = in;
				this.addPos = 1;
			}
		}
	}

	public final void add(char in, int numRepeats)
	{
		// System.out.println("BEFORE:\r\n\taddBuffer length: " + this.addBuffer.length + "\r\n\taddPos: " + this.addPos);

		if (numRepeats <= 0)
		{
			return;
		}
		else if (numRepeats == 1)
		{
			this.add(in);
			return;
		}

		if (this.addPos == 0)
		{
			this.activeStates = this.activeStates.toggleAdded();
		}
		else if (diag_checkActiveStates)
		{
			if (!this.activeStates.hasBeenAdded)
			{
				throw new IllegalArgumentException("\r\n\taddPos: " + this.addPos + "\r\n\tcargo, x" + numRepeats + ": " + in);
			}
		}

		final long afterPos = this.addPos + numRepeats;

		if (afterPos <= this.addBuffer.length)
		{
			PrimArraysCopier.fillRange(this.addBuffer, this.addPos, in, numRepeats);
			this.addPos = (int) afterPos;
		}
		else if (afterPos <= maxBufferLength)
		{
			final char[] nextBuffer = getNewBuffer((int) afterPos);

			// just for diagnostics
			// PrimArraysCopier.arrayCopy_boundsChecked(this.addBuffer, 0, nextBuffer, 0, this.addPos);

			System.arraycopy(this.addBuffer, 0, nextBuffer, 0, this.addPos);
			PrimArraysCopier.fillRange(nextBuffer, this.addPos, in, numRepeats);
			this.addPos = (int) afterPos;
			this.setAddBuffer(nextBuffer);
		}
		else
		{
			// needs to be split into multiple arrays
			final char[] oldBuffer;

			if (this.addBuffer.length >= maxBufferLength)
			{
				oldBuffer = this.addBuffer;
			}
			else
			{
				oldBuffer = new char[maxBufferLength];
				// PrimArraysCopier.arrayCopy_boundsChecked(this.addBuffer, 0, oldBuffer, 0, this.addBuffer.length);
				System.arraycopy(this.addBuffer, 0, oldBuffer, 0, this.addBuffer.length);
			}

			final int remainingPartialLength = oldBuffer.length - this.addPos;
			PrimArraysCopier.fillRange(oldBuffer, this.addPos, in, remainingPartialLength);

			this.addNode_internal(oldBuffer);

			final int nextCopyLength = numRepeats - remainingPartialLength;
			final char[] nextBuffer = getNewBuffer(nextCopyLength);
			PrimArraysCopier.fillRange(nextBuffer, 0, in, nextCopyLength);

			this.setAddBuffer(nextBuffer);
			this.addPos = nextCopyLength;
		}

		this.size += numRepeats;

		// System.out.println("AFTER:\r\n\taddBuffer length: " + this.addBuffer.length + "\r\n\taddPos: " + this.addPos);
	}

	public final void add(char[] in)
	{
		this.add_internal_sizeUnchanged(in);
		this.size += in.length;
	}

	/**
	 * this method adds the array without changing the size of this object
	 * 
	 * @param in
	 */
	private final void add_internal_sizeUnchanged(char[] in)
	{
		final long afterPos = this.addPos + in.length;

		if (this.addPos == 0)
		{
			this.activeStates = this.activeStates.toggleAdded();
		}
		else if (diag_checkActiveStates)
		{
			if (!this.activeStates.hasBeenAdded)
			{
				throw new IllegalArgumentException("\r\n\taddPos: " + this.addPos + "\r\n\tcargo: " + new String(in));
			}
		}

		if (afterPos <= this.addBuffer.length)
		{
			System.arraycopy(in, 0, this.addBuffer, this.addPos, in.length);
			this.addPos = (int) afterPos;
		}
		else if (afterPos <= maxBufferLength)
		{
			// can still fit in 1 array
			final char[] nextBuffer = getNewBuffer((int) afterPos);
			System.arraycopy(this.addBuffer, 0, nextBuffer, 0, this.addPos);
			System.arraycopy(in, 0, nextBuffer, this.addPos, in.length);
			this.addPos = (int) afterPos;
			this.setAddBuffer(nextBuffer);
		}
		else
		{
			// needs to be split into multiple arrays
			final char[] oldBuffer;

			if (this.addBuffer.length >= maxBufferLength)
			{
				oldBuffer = this.addBuffer;
			}
			else
			{
				oldBuffer = new char[maxBufferLength];
				System.arraycopy(this.addBuffer, 0, oldBuffer, 0, this.addBuffer.length);
			}

			final int remainingPartialLength = oldBuffer.length - this.addPos;
			System.arraycopy(in, 0, oldBuffer, this.addPos, remainingPartialLength);

			this.addNode_internal(oldBuffer);

			final int nextCopyLength = in.length - remainingPartialLength;
			final char[] nextBuffer = getNewBuffer(nextCopyLength);
			System.arraycopy(in, remainingPartialLength, nextBuffer, 0, nextCopyLength);

			this.setAddBuffer(nextBuffer);
			this.addPos = nextCopyLength;
		}
	}

	public final void add(char[] in, int numRepeats)
	{
		if (numRepeats <= 0)
		{
			return;
		}
		else if (numRepeats == 1)
		{
			this.add(in);
			return;
		}

		if (this.addPos == 0)
		{
			this.activeStates = this.activeStates.toggleAdded();
		}
		else if (diag_checkActiveStates)
		{
			if (!this.activeStates.hasBeenAdded)
			{
				throw new IllegalArgumentException("\r\n\taddPos: " + this.addPos + "\r\n\tcargo, repeated x" + numRepeats + ": " + new String(in));
			}
		}

		final long fullLength = in.length * numRepeats;
		final long afterPos = this.addPos + fullLength;

		if (afterPos <= this.addBuffer.length)
		{
			// fits in current buffer

			PrimArraysCopier.fillRange(this.addBuffer, this.addPos, in, numRepeats);
			this.addPos = (int) afterPos;
		}
		else if (afterPos <= maxBufferLength)
		{
			// can still fit in 1 array

			final char[] nextBuffer = getNewBuffer((int) afterPos);
			System.arraycopy(this.addBuffer, 0, nextBuffer, 0, this.addPos);
			PrimArraysCopier.fillRange(nextBuffer, this.addPos, in, numRepeats);
			this.addPos = (int) afterPos;
			this.setAddBuffer(nextBuffer);
		}
		else
		{
			// needs to be split into multiple arrays
			final char[] oldBuffer;

			if (this.addBuffer.length >= maxBufferLength)
			{
				oldBuffer = this.addBuffer;
			}
			else
			{
				oldBuffer = new char[maxBufferLength];
				System.arraycopy(this.addBuffer, 0, oldBuffer, 0, this.addBuffer.length);
			}

			final int remainingPartialLength = oldBuffer.length - this.addPos;

			final int div = remainingPartialLength / in.length;
			final int mod = remainingPartialLength % in.length;

			// System.out.println("remainingPartialLength: " + remainingPartialLength + "\r\n\tinLength: " + in.length + "\r\n\tdiv: " + div + "\r\n\tmod: " + mod);

			PrimArraysCopier.fillRange(oldBuffer, this.addPos, in, div);

			final int nextCopyLength = (int) (fullLength - remainingPartialLength);
			// System.out.println("nextCopyLength: " + nextCopyLength);
			final char[] nextBuffer = getNewBuffer(nextCopyLength);

			final int frontPartial;
			final int remainingRepeats;

			if (mod != 0)
			{
				System.arraycopy(in, 0, oldBuffer, this.addPos + (in.length * div), mod);

				frontPartial = in.length - mod;
				System.arraycopy(in, mod, nextBuffer, 0, frontPartial);
				remainingRepeats = numRepeats - div - 1;
			}
			else
			{
				frontPartial = 0;
				remainingRepeats = numRepeats - div;
			}

			this.addNode_internal(oldBuffer);

			// final int remainingLength = in.length * remainingRepeats;

			// System.out.println("addPos: " + this.addPos + "\r\n\tinLength: " + in.length + "\r\n\tnumRepeats: " + numRepeats + "\r\n\taddBufferLength: " + this.addBuffer.length + "\r\n\tremainingRepeats: " + remainingRepeats);

			// System.out.println("in.length: " + in.length);
			// EnhancedArrayIndexOutOfBoundsException.checkArrayContents(nextBuffer, frontPartial + remainingLength);

			PrimArraysCopier.fillRange(nextBuffer, frontPartial, in, remainingRepeats);

			this.setAddBuffer(nextBuffer);
			this.addPos = nextCopyLength;
		}

		this.size += fullLength;
	}

	public final void add(String in)
	{
		if (this.addPos == 0)
		{
			this.activeStates = this.activeStates.toggleAdded();
		}
		else if (diag_checkActiveStates)
		{
			if (!this.activeStates.hasBeenAdded)
			{
				throw new IllegalArgumentException("\r\n\taddPos: " + this.addPos + "\r\n\tcargo: " + in);
			}
		}

		final int inLength = in.length();
		final long afterPos = this.addPos + inLength;

		if (afterPos <= this.addBuffer.length)
		{
			in.getChars(0, inLength, this.addBuffer, this.addPos);
			// System.arraycopy(in, 0, this.addBuffer, this.addPos, inLength);
			this.addPos = (int) afterPos;
		}
		else if (afterPos <= maxBufferLength)
		{
			// can still fit in 1 array
			final char[] nextBuffer = getNewBuffer((int) afterPos);
			System.arraycopy(this.addBuffer, 0, nextBuffer, 0, this.addPos);
			in.getChars(0, inLength, nextBuffer, this.addPos);
			// System.arraycopy(in, 0, nextBuffer, this.addPos, inLength);
			this.addPos = (int) afterPos;
			this.setAddBuffer(nextBuffer);
		}
		else
		{
			// needs to be split into multiple arrays
			final char[] oldBuffer;

			if (this.addBuffer.length >= maxBufferLength)
			{
				oldBuffer = this.addBuffer;
			}
			else
			{
				oldBuffer = new char[maxBufferLength];
				System.arraycopy(this.addBuffer, 0, oldBuffer, 0, this.addBuffer.length);
			}

			final int remainingPartialLength = this.addBuffer.length - this.addPos;

			in.getChars(0, remainingPartialLength, oldBuffer, this.addPos);
			// System.arraycopy(in, 0, oldBuffer, this.addPos, remainingPartialLength);

			this.addNode_internal(oldBuffer);

			final int nextCopyLength = inLength - remainingPartialLength;
			final char[] nextBuffer = getNewBuffer(nextCopyLength);

			in.getChars(remainingPartialLength, inLength, nextBuffer, 0);
			// System.arraycopy(in, remainingPartialLength, nextBuffer, 0, nextCopyLength);

			this.setAddBuffer(nextBuffer);
			this.addPos = nextCopyLength;
		}

		this.size += inLength;
	}

	public final void add(String in, int numRepeats)
	{
		if (numRepeats > 0)
		{
			if (numRepeats == 1)
			{
				this.add(in);
				return;
			}

			if (this.addPos == 0)
			{
				this.activeStates = this.activeStates.toggleAdded();
			}
			else if (diag_checkActiveStates)
			{
				if (!this.activeStates.hasBeenAdded)
				{
					throw new IllegalArgumentException("\r\n\taddPos: " + this.addPos + "\r\n\tcargo, repeated x" + numRepeats + ": " + in);
				}
			}

			final int inLength = in.length();
			final long fullLength = inLength * numRepeats;
			final long afterPos = this.addPos + fullLength;

			if (afterPos <= this.addBuffer.length)
			{
				// fits in current buffer

				PrimArraysCopier.fillRange(this.addBuffer, this.addPos, in, numRepeats);
				this.addPos = (int) afterPos;
			}
			else if (afterPos <= maxBufferLength)
			{
				// can still fit in 1 array

				final char[] nextBuffer = getNewBuffer((int) afterPos);
				System.arraycopy(this.addBuffer, 0, nextBuffer, 0, this.addPos);
				PrimArraysCopier.fillRange(nextBuffer, this.addPos, in, numRepeats);
				this.addPos = (int) afterPos;
				this.setAddBuffer(nextBuffer);
			}
			else
			{
				// needs to be split into multiple arrays
				final char[] oldBuffer;

				if (this.addBuffer.length >= maxBufferLength)
				{
					oldBuffer = this.addBuffer;
				}
				else
				{
					oldBuffer = new char[maxBufferLength];
					System.arraycopy(this.addBuffer, 0, oldBuffer, 0, this.addBuffer.length);
				}

				final int remainingPartialLength = oldBuffer.length - this.addPos;

				final int div = remainingPartialLength / inLength;
				final int mod = remainingPartialLength % inLength;

				// System.out.println("remainingPartialLength: " + remainingPartialLength + "\r\n\tinLength: " + inLength + "\r\n\tdiv: " + div + "\r\n\tmod: " + mod);

				PrimArraysCopier.fillRange(oldBuffer, this.addPos, in, div);

				final int nextCopyLength = (int) (fullLength - remainingPartialLength);
				// System.out.println("nextCopyLength: " + nextCopyLength);
				final char[] nextBuffer = getNewBuffer(nextCopyLength);

				final int frontPartial;
				final int remainingRepeats;

				if (mod != 0)
				{
					in.getChars(0, mod, oldBuffer, this.addPos + (inLength * div));
					// System.arraycopy(in, 0, oldBuffer, this.addPos + (inLength * div), mod);

					frontPartial = inLength - mod;

					// EnhancedArrayIndexOutOfBoundsException.checkArrayContents(in, frontPartial);
					// EnhancedArrayIndexOutOfBoundsException.checkArrayContents(nextBuffer, frontPartial);
					in.getChars(mod, inLength, nextBuffer, 0);
					// System.arraycopy(in, mod, nextBuffer, 0, frontPartial);
					remainingRepeats = numRepeats - div - 1;
				}
				else
				{
					frontPartial = 0;
					remainingRepeats = numRepeats - div;
				}

				this.addNode_internal(oldBuffer);

				// final int remainingLength = inLength * remainingRepeats;

				// System.out.println("addPos: " + this.addPos + "\r\n\tinLength: " + inLength + "\r\n\tnumRepeats: " + numRepeats + "\r\n\taddBufferLength: " + this.addBuffer.length + "\r\n\tremainingRepeats: " + remainingRepeats);

				// System.out.println("inLength: " + inLength);
				// EnhancedArrayIndexOutOfBoundsException.checkArrayContents(nextBuffer, frontPartial + remainingLength);

				PrimArraysCopier.fillRange(nextBuffer, frontPartial, in, remainingRepeats);

				this.setAddBuffer(nextBuffer);
				this.addPos = nextCopyLength;
			}

			this.size += fullLength;
		}
	}

	/**
	 * functionally equivalent to iteratively calling {@code this.add(in[x])}.
	 * Does not store as a single unit to maintain accuracy for {@code this.size()}.
	 * 
	 * @param in
	 * @return the total length, in chars, of all of the strings
	 */
	public final long add(String[] in)
	{
		if (in.length == 0)
		{
			return 0;
		}
		else if (in.length == 1)
		{
			this.add(in[0]);
			return in[0].length();
		}

		if (this.addPos == 0)
		{
			this.activeStates = this.activeStates.toggleAdded();
		}
		else if (diag_checkActiveStates)
		{
			if (!this.activeStates.hasBeenAdded)
			{
				throw new IllegalArgumentException("\r\n\taddPos: " + this.addPos + "\r\n\tcargo: " + Arrays.toString(in));
			}
		}

		final long neededLength = getTotalLength(in);
		final long afterPos = this.addPos + neededLength;

		if (afterPos <= this.addBuffer.length)
		{
			this.addPos = copyArray(in, this.addBuffer, this.addPos);
		}
		else if (afterPos <= maxBufferLength)
		{
			// can still fit in 1 array
			final char[] nextBuffer = getNewBuffer((int) afterPos);
			System.arraycopy(this.addBuffer, 0, nextBuffer, 0, this.addPos);
			this.addPos = copyArray(in, nextBuffer, this.addPos);
			this.setAddBuffer(nextBuffer);
		}
		else
		{
			// needs to be split into multiple arrays
			final char[] oldBuffer;

			if (this.addBuffer.length >= maxBufferLength)
			{
				oldBuffer = this.addBuffer;
			}
			else
			{
				oldBuffer = new char[maxBufferLength];
				System.arraycopy(this.addBuffer, 0, oldBuffer, 0, this.addPos);
			}

			final int remainingPartialLength = oldBuffer.length - this.addPos;

			int arrIndex = 0;
			final int[] addPosBuffer = new int[2];
			int bufferIndex = 0;
			addPosBuffer[1] = this.addPos;

			loop: while (arrIndex < in.length)
			{
				final String curr = in[arrIndex];

				addPosBuffer[bufferIndex] = addPosBuffer[bufferIndex ^ 1] + curr.length();

				if (addPosBuffer[bufferIndex] < oldBuffer.length)
				{
					curr.getChars(0, curr.length(), oldBuffer, addPosBuffer[bufferIndex ^= 1]);
					arrIndex += 1;
				}
				else
				{
					break loop;
				}
			}

			final int length_before = in[arrIndex].length() - (addPosBuffer[bufferIndex] - oldBuffer.length);
			final int length_after = in[arrIndex].length() - length_before;
			in[arrIndex].getChars(0, length_before, oldBuffer, addPosBuffer[bufferIndex ^ 1]);

			this.addNode_internal(oldBuffer);

			final int nextCopyLength = (int) (neededLength - remainingPartialLength);
			final char[] nextBuffer = getNewBuffer(nextCopyLength);
			in[arrIndex].getChars(length_before, in[arrIndex].length(), nextBuffer, 0);
			addPosBuffer[bufferIndex ^ 1] = length_after;
			arrIndex += 1;
			// System.out.println("oldbuffer:\r\n\t" + new String(oldBuffer) + "\r\nnextBuffer:\r\n\t" + new String(nextBuffer));

			while (arrIndex < in.length)
			{
				final String curr = in[arrIndex];
				addPosBuffer[bufferIndex] = addPosBuffer[bufferIndex ^ 1] + curr.length();
				curr.getChars(0, curr.length(), nextBuffer, addPosBuffer[bufferIndex ^= 1]);
				arrIndex += 1;
			}

			this.setAddBuffer(nextBuffer);
			this.addPos = nextCopyLength;
		}

		this.size += neededLength;
		return neededLength;
	}

	private static final int copyArray(String src, char[] dest, int destBegin)
	{
		src.getChars(0, src.length(), dest, destBegin);
		return destBegin + src.length();
	}

	/**
	 * copies each string completely
	 * 
	 * @param src
	 * @param dest
	 * @param destBegin
	 * @return
	 */
	private static final int copyArray(String[] src, char[] dest, int destBegin)
	{
		for (int i = 0; i < src.length; i += 1)
		{
			destBegin = copyArray(src[i], dest, destBegin);
		}

		return destBegin;
	}

	private static final long getTotalLength(String[] in)
	{
		if (in.length == 0)
		{
			return 0;
		}

		long sum = in[0].length();

		for (int i = 1; i < in.length; i += 1)
		{
			sum += in[i].length();
		}

		return sum;
	}

	/**
	 * functionally equivalent to copying the contents of {@code in} into a character array, then calling {@code this.add(in_asCharArray, numRepeats)}.
	 * 
	 * @param in
	 * @param numRepeats
	 */
	public final void add(String[] in, int numRepeats)
	{
		if (numRepeats > 0)
		{
			if (numRepeats == 1)
			{
				this.add(in);
			}
			else
			{
				final char[] inline = StringArrToCharArr(in);
				this.add(inline, numRepeats);
			}
		}
	}

	public static final char[] StringArrToCharArr(String[] in)
	{
		int length = in[0].length();

		for (int i = in.length - 1; i > 0; i--)
		{
			length += in[i].length();
		}

		final char[] result = new char[length];

		for (int stringIndex = in.length; stringIndex > 0; --stringIndex)
		{
			final int stringLength = in[stringIndex].length();
			length -= stringLength;
			in[stringIndex].getChars(0, stringLength, result, length); // hopefully this uses System.ArrayCpy under the hood
		}

		return result;
	}

	public final void add2(BaseCharList in)
	{
		// reference implementation
		this.add(in.toCharArray());
	}

	public final void add(BaseCharList in)
	{
		if (in.size == 0)
		{
			return;
		}
		else if (in.size == 1)
		{
			this.add(in.firstChar());
			return;
		}

		if (this.addPos == 0)
		{
			this.activeStates = this.activeStates.toggleAdded();
		}
		else if (diag_checkActiveStates)
		{
			if (!this.activeStates.hasBeenAdded)
			{
				throw new IllegalArgumentException("\r\n\taddPos: " + this.addPos + "\r\n\tcargo: " + in.toString());
			}
		}

		switch (this.activeStates)
		{
			case hasBeenPushed_hasNoNodes_hasBeenAdded:
			{
				this.add_internal_pushed(in);
				this.add_internal_added(in);
				break;
			}
			case hasBeenPushed_hasNoNodes_hasNotBeenAdded:
			{
				this.add_internal_pushed(in);
				break;
			}
			case hasBeenPushed_hasNodes_hasBeenAdded:
			{
				final long nodesLength = in.size - (in.pushBuffer.length - in.pushPos) - in.addPos;
				this.add_internal_pushed(in);
				this.add_internal_noded(in, nodesLength);
				this.add_internal_added(in);
				break;
			}
			case hasBeenPushed_hasNodes_hasNotBeenAdded:
			{
				final long nodesLength = in.size - (in.pushBuffer.length - in.pushPos);
				this.add_internal_pushed(in);
				this.add_internal_noded(in, nodesLength);
				break;
			}
			case hasNotBeenPushed_hasNoNodes_hasBeenAdded:
			{
				this.add_internal_added(in);
				break;
			}
			case hasNotBeenPushed_hasNoNodes_hasNotBeenAdded:
			{
				break;
			}
			case hasNotBeenPushed_hasNodes_hasBeenAdded:
			{
				final long nodesLength = in.size - in.addPos;
				this.add_internal_noded(in, nodesLength);
				this.add_internal_added(in);
				break;
			}
			case hasNotBeenPushed_hasNodes_hasNotBeenAdded:
			{
				this.add_internal_noded(in, in.size);
				break;
			}
			default:
			{
				throw new UnhandledEnumException(this.activeStates);
			}
		}

		this.size += in.size;
	}

	private final void add_internal_pushed(BaseCharList in)
	{
		this.add_internal_limitedCopy(in.pushBuffer, in.pushPos, in.pushBuffer.length - in.pushPos);
	}

	private final void add_internal_noded(BaseCharList in, long nodesLength)
	{
		if (nodesLength == 0)
		{
			return;
		}
		else if (nodesLength == 1)
		{
			this.add_internal_sizeUnchanged(in.head.core[0]);
			return;
		}
		else if (in.head == in.tail)
		{
			this.add_internal_sizeUnchanged(in.head.core);
			return;
		}

		if (nodesLength < (this.addBuffer.length - this.addPos))
		{
			// can fit in addBuffer directly
			CharNode currNode = in.head;

			do
			{
				System.arraycopy(currNode.core, 0, this.addBuffer, this.addPos, currNode.core.length);
				this.addPos += currNode.core.length;

			} while (currNode != null);
		}
		else
		{
			final long afterPos = this.addPos + nodesLength;

			if (afterPos > maxBufferLength)
			{
				// requires noding

				if (this.addBuffer.length < maxBufferLength)
				{
					final char[] oldBuffer = new char[maxBufferLength];
					System.arraycopy(this.addBuffer, 0, oldBuffer, 0, this.addPos);
					this.addBuffer = oldBuffer;
				}

				CharNode currNode = in.head;

				do
				{
					this.add_internal_sizeUnchanged(currNode.core);
				} while (currNode != null);
			}
			else
			{
				// can fit in addBuffer, after expansion
				final char[] nextBuffer;

				if (afterPos < maxBufferLength)
				{
					nextBuffer = this.addBuffer;
				}
				else
				{
					nextBuffer = getNewBuffer((int) nodesLength);
					System.arraycopy(this.addBuffer, 0, nextBuffer, 0, this.addBuffer.length);
					this.addBuffer = nextBuffer;
				}

				CharNode currNode = in.head;
				int offset = 0;

				do
				{
					System.arraycopy(currNode.core, 0, nextBuffer, offset, currNode.core.length);
					offset += currNode.core.length;

				} while (currNode != null);

				this.addPos = offset;
			}
		}
	}

	private final void add_internal_added(BaseCharList in)
	{
		this.add_internal_limitedCopy(in.addBuffer, 0, in.addPos);
	}

	private final void add_internal_limitedCopy(char[] src, int srcBegin, int length)
	{
		final long afterPos = this.addPos + length;

		if (afterPos < this.addBuffer.length)
		{
			if (this.addPos == 0)
			{
				this.activeStates = this.activeStates.toggleAdded();
			}

			System.arraycopy(src, srcBegin, this.addBuffer, this.addPos, length);
			this.addPos += length;
		}
		else if (afterPos < maxBufferLength)
		{
			final char[] nextBuffer = getNewBuffer((int) afterPos);
			System.arraycopy(src, srcBegin, nextBuffer, this.addPos, length);
			this.addPos += length;
			this.addBuffer = nextBuffer;
		}
		else
		{
			final int remainingChars = this.addBuffer.length - this.addPos;
			final int extraChars = length - remainingChars;
			System.arraycopy(src, srcBegin, this.addBuffer, this.addPos, remainingChars);
			this.addNode_internal(this.addBuffer);
			this.addBuffer = getNewBuffer(extraChars);
			this.addPos = 0;
		}
	}

	/**
	 * does not alter {@link #size} at all
	 * 
	 * @param core
	 */
	private final void addNode_internal(char[] core)
	{
		final CharNode node = new CharNode(core);

		if (this.head == null)
		{
			this.activeStates = this.activeStates.toggleNoded();

			this.head = node;
			this.tail = node;
		}
		else
		{
			CharNode.linkNodes(this.tail, node);
			this.tail = node;
		}
	}

	public final void push(char in)
	{
		if (this.pushPos >= 0)
		{
			if (this.pushPos == this.pushBuffer.length - 1)
			{
				this.activeStates = this.activeStates.togglePushed();
			}

			this.pushBuffer[this.pushPos--] = in;
		}
		else
		{
			if (this.pushBuffer.length < maxBufferLength)
			{
				final char[] nextBuffer = getNewBuffer(this.pushBuffer.length + 1);
				this.pushPos = nextBuffer.length - this.pushBuffer.length;
				System.arraycopy(this.pushBuffer, 0, nextBuffer, this.pushPos--, this.pushBuffer.length);
				nextBuffer[this.pushPos--] = in;
				this.setPushBuffer(nextBuffer);
			}
			else // if (this.pushBuffer.length >= maxBufferLength)
			{
				this.pushNode_internal(this.pushBuffer);
				this.setPushBuffer(new char[minBufferLength]);
				this.pushBuffer[minBufferLength - 1] = in;
				this.pushPos = minBufferLength - 2;
			}
		}

		this.size += 1;
	}

	public final void push(char in, int numRepeats)
	{
		if (numRepeats <= 0)
		{
			return;
		}
		else if (numRepeats == 1)
		{
			this.push(in);
			return;
		}

		final long afterPos = this.pushPos - numRepeats;

		if (afterPos >= -1)
		{
			if (this.pushPos == this.pushBuffer.length - 1)
			{
				this.activeStates = this.activeStates.togglePushed();
			}

			this.pushPos = (int) afterPos;
			PrimArraysCopier.fillRange(this.pushBuffer, this.pushPos + 1, in, numRepeats);
		}
		else
		{
			final int remaining = this.pushPos + 1;
			final int filled = this.pushBuffer.length - remaining;
			final int neededLength = filled + numRepeats;

			if (neededLength < maxBufferLength)
			{
				final char[] nextBuffer = getNewBuffer(neededLength);
				this.pushPos = nextBuffer.length - this.pushBuffer.length + remaining;
				System.arraycopy(this.pushBuffer, remaining, nextBuffer, this.pushPos, filled);
				this.pushPos -= numRepeats;
				PrimArraysCopier.fillRange(nextBuffer, this.pushPos--, in, numRepeats);
				this.setPushBuffer(nextBuffer);
			}
			else // needs to be split into multiple arrays
			{
				final int extraRepeats = numRepeats - remaining;
				PrimArraysCopier.fillRange(this.pushBuffer, 0, in, this.pushPos + 1);
				this.pushNode_internal(this.pushBuffer);
				final char[] nextBuffer = getNewBuffer(extraRepeats);
				this.pushPos = nextBuffer.length - 1 - extraRepeats;
				PrimArraysCopier.fillRange(nextBuffer, this.pushPos--, in, extraRepeats);
				this.setPushBuffer(nextBuffer);
			}
		}

		this.size += numRepeats;
	}

	public final void push(char[] in)
	{
		switch (in.length)
		{
			case 0:
			{
				return;
			}
			case 1:
			{
				this.push(in[0]);
				return;
			}
			default:
			{
				break;
			}
		}

		final long afterPos = this.pushPos - in.length;

		if (afterPos >= -1)
		{
			if (this.pushPos == this.pushBuffer.length - 1)
			{
				this.activeStates = this.activeStates.togglePushed();
			}

			this.pushPos = (int) afterPos;
			System.arraycopy(in, 0, this.pushBuffer, this.pushPos + 1, in.length);
		}
		else
		{
			final int remaining = this.pushPos + 1;
			final int filled = this.pushBuffer.length - remaining;
			final int neededLength = filled + in.length;

			if (neededLength < maxBufferLength)
			{
				final char[] nextBuffer = getNewBuffer(neededLength);
				this.pushPos = nextBuffer.length - this.pushBuffer.length + remaining;
				System.arraycopy(this.pushBuffer, remaining, nextBuffer, this.pushPos, filled);
				this.pushPos -= in.length;
				System.arraycopy(in, 0, nextBuffer, this.pushPos--, in.length);
				this.setPushBuffer(nextBuffer);
			}
			else // needs to be split into multiple arrays
			{
				final int extraRepeats = in.length - remaining;
				System.arraycopy(in, extraRepeats, this.pushBuffer, 0, this.pushPos + 1);
				this.pushNode_internal(this.pushBuffer);
				final char[] nextBuffer = getNewBuffer(extraRepeats);
				this.pushPos = nextBuffer.length - 1 - extraRepeats;
				System.arraycopy(in, 0, nextBuffer, this.pushPos--, extraRepeats);
				this.setPushBuffer(nextBuffer);
			}
		}

		this.size += in.length;
	}

	public final void push(char[] in, int numRepeats)
	{
		if (numRepeats <= 0)
		{
			return;
		}
		else if (numRepeats == 1)
		{
			this.push(in);
			return;
		}

		final long fullLength = in.length * numRepeats;
		final long afterPos = this.pushPos - fullLength;

		if (afterPos >= -1)
		{
			if (this.pushPos == this.pushBuffer.length - 1)
			{
				this.activeStates = this.activeStates.togglePushed();
			}

			// System.out.println("triggered");
			this.pushPos = (int) afterPos;
			PrimArraysCopier.fillRange(this.pushBuffer, this.pushPos + 1, in, numRepeats);
			// System.out.println(new String(this.pushBuffer));
		}
		else // expansion is needed
		{
			final int remaining = this.pushPos + 1;
			final int filled = this.pushBuffer.length - remaining;
			final long tempNeededLength = filled + fullLength;

			if (tempNeededLength <= Integer.MAX_VALUE) // no node push required
			{
				final char[] nextBuffer = getNewBuffer((int) tempNeededLength);
				this.pushPos = nextBuffer.length - this.pushBuffer.length + remaining;
				System.arraycopy(this.pushBuffer, remaining, nextBuffer, this.pushPos, filled);
				this.pushPos -= fullLength;
				PrimArraysCopier.fillRange(nextBuffer, this.pushPos--, in, numRepeats);
				this.setPushBuffer(nextBuffer);
			}
			else // split into multiple arrays
			{
				final char[] oldBuffer;

				if (this.pushBuffer.length < Integer.MAX_VALUE)
				{
					oldBuffer = new char[Integer.MAX_VALUE];
					this.pushPos = Integer.MAX_VALUE - this.pushBuffer.length;
					System.arraycopy(this.pushBuffer, 0, oldBuffer, this.pushPos, this.pushBuffer.length);
				}
				else
				{
					oldBuffer = this.pushBuffer;
				}

				final int numRepeats_inOld = this.pushPos / in.length;
				final int mod = this.pushPos % in.length;
				final int oldLength = (numRepeats_inOld * in.length) + mod;
				PrimArraysCopier.fillRange(oldBuffer, mod, in, numRepeats_inOld);
				final int extraLength = (int) (fullLength - oldLength);
				final char[] nextBuffer = getNewBuffer(extraLength);
				final int numRepeats_inNew;

				if (mod != 0)
				{
					final int inverseMod = in.length - mod;
					System.arraycopy(in, inverseMod, oldBuffer, 0, mod);

					numRepeats_inNew = numRepeats - (numRepeats_inOld + 1);
					System.arraycopy(in, 0, nextBuffer, nextBuffer.length - inverseMod - 1, inverseMod);
				}
				else
				{
					numRepeats_inNew = numRepeats - numRepeats_inOld;
				}

				this.pushNode_internal(oldBuffer);

				this.pushPos = nextBuffer.length - extraLength;
				PrimArraysCopier.fillRange(nextBuffer, this.pushPos--, in, numRepeats_inNew);
				this.setPushBuffer(nextBuffer);
			}
		}

		this.size += fullLength;
	}

	public final void push(String in)
	{
		final int inLength = in.length();

		switch (inLength)
		{
			case 0:
			{
				return;
			}
			case 1:
			{
				this.push(in.charAt(0));
				return;
			}
			default:
			{
				break;
			}
		}

		final long afterPos = this.pushPos - inLength;

		if (afterPos >= -1)
		{
			if (this.pushPos == this.pushBuffer.length - 1)
			{
				this.activeStates = this.activeStates.togglePushed();
			}

			this.pushPos = (int) afterPos;
			in.getChars(0, inLength, this.pushBuffer, this.pushPos + 1);
		}
		else
		{
			final int remaining = this.pushPos + 1;
			final int filled = this.pushBuffer.length - remaining;
			final int neededLength = filled + inLength;

			if (neededLength < maxBufferLength)
			{
				final char[] nextBuffer = getNewBuffer(neededLength);
				this.pushPos = nextBuffer.length - this.pushBuffer.length + remaining;
				System.arraycopy(this.pushBuffer, remaining, nextBuffer, this.pushPos, filled);
				this.pushPos -= inLength;
				in.getChars(0, inLength, nextBuffer, this.pushPos--);
				this.setPushBuffer(nextBuffer);
			}
			else // needs to be split into multiple arrays
			{
				final int extraRepeats = inLength - remaining;
				in.getChars(extraRepeats, extraRepeats + this.pushPos + 1, this.pushBuffer, 0);
				this.pushNode_internal(this.pushBuffer);
				final char[] nextBuffer = getNewBuffer(extraRepeats);
				this.pushPos = nextBuffer.length - 1 - extraRepeats;
				in.getChars(0, extraRepeats, nextBuffer, this.pushPos--);
				this.setPushBuffer(nextBuffer);
			}
		}

		this.size += inLength;
	}

	public final void push(String in, int numRepeats)
	{
		if (numRepeats <= 0)
		{
			return;
		}
		else if (numRepeats == 1)
		{
			this.push(in);
			return;
		}

		final int inLength = in.length();
		final long fullLength = inLength * numRepeats;
		final long afterPos = this.pushPos - fullLength;

		if (afterPos >= -1)
		{
			if (this.pushPos == this.pushBuffer.length - 1)
			{
				this.activeStates = this.activeStates.togglePushed();
			}

			// System.out.println("triggered");
			this.pushPos = (int) afterPos;
			PrimArraysCopier.fillRange(this.pushBuffer, this.pushPos + 1, in, numRepeats);
			// System.out.println(new String(this.pushBuffer));
		}
		else // expansion is needed
		{
			final int remaining = this.pushPos + 1;
			final int filled = this.pushBuffer.length - remaining;
			final long tempNeededLength = filled + fullLength;

			if (tempNeededLength <= Integer.MAX_VALUE) // no node push required
			{
				final char[] nextBuffer = getNewBuffer((int) tempNeededLength);
				this.pushPos = nextBuffer.length - this.pushBuffer.length + remaining;
				System.arraycopy(this.pushBuffer, remaining, nextBuffer, this.pushPos, filled);
				this.pushPos -= fullLength;
				PrimArraysCopier.fillRange(nextBuffer, this.pushPos--, in, numRepeats);
				this.setPushBuffer(nextBuffer);
			}
			else // split into multiple arrays
			{
				final char[] oldBuffer;

				if (this.pushBuffer.length < Integer.MAX_VALUE)
				{
					oldBuffer = new char[Integer.MAX_VALUE];
					this.pushPos = Integer.MAX_VALUE - this.pushBuffer.length;
					System.arraycopy(this.pushBuffer, 0, oldBuffer, this.pushPos, this.pushBuffer.length);
				}
				else
				{
					oldBuffer = this.pushBuffer;
				}

				final int numRepeats_inOld = this.pushPos / inLength;
				final int mod = this.pushPos % inLength;
				final int oldLength = (numRepeats_inOld * inLength) + mod;
				PrimArraysCopier.fillRange(oldBuffer, mod, in, numRepeats_inOld);
				final int extraLength = (int) (fullLength - oldLength);
				final char[] nextBuffer = getNewBuffer(extraLength);
				final int numRepeats_inNew;

				if (mod != 0)
				{
					final int inverseMod = inLength - mod;
					in.getChars(inverseMod, inLength, oldBuffer, 0);

					numRepeats_inNew = numRepeats - (numRepeats_inOld + 1);
					in.getChars(0, inverseMod, nextBuffer, nextBuffer.length - inverseMod - 1);
				}
				else
				{
					numRepeats_inNew = numRepeats - numRepeats_inOld;
				}

				this.pushNode_internal(oldBuffer);

				this.pushPos = nextBuffer.length - extraLength;
				PrimArraysCopier.fillRange(nextBuffer, this.pushPos--, in, numRepeats_inNew);
				this.setPushBuffer(nextBuffer);
			}
		}

		this.size += fullLength;
	}

	/**
	 * functionally equivalent to copying the contents of {@code in} into a character array, then calling {@code this.push(in_asCharArray)}.
	 * Does not store as a single unit to maintain accuracy for {@code this.size()}.
	 * 
	 * @param in
	 */
	public final void push(String[] in)
	{
		// TODO: optimize this

		for (String string : in)
		{
			this.push(string);
		}
	}

	/**
	 * functionally equivalent to copying the contents of {@code in} into a character array, then calling {@code this.push(in_asCharArray, numRepeats)}.
	 * 
	 * @param in
	 * @param numRepeats
	 */
	public final void push(String[] in, int numRepeats)
	{
		if (numRepeats > 0)
		{
			if (numRepeats == 1)
			{
				this.push(in);
			}
			else
			{
				final char[] inline = StringArrToCharArr(in);
				this.push(inline, numRepeats);
			}
		}
	}

	public final void push(BaseCharList in)
	{
		// TODO: optimize this
		this.push(in.toCharArray());
	}

	/**
	 * does not alter {@link #size} at all
	 * 
	 * @param core
	 */
	private final void pushNode_internal(char[] core)
	{
		final CharNode node = new CharNode(core);

		if (this.head == null)
		{
			this.activeStates = this.activeStates.toggleNoded();
			this.head = node;
			this.tail = node;
		}
		else
		{
			CharNode.linkNodes(node, this.head);
			this.head = node;
		}
	}

	private final void setAddBuffer(char[] newAddBuffer)
	{
		/*
		if(newAddBuffer.length > maxBufferLength)
		{
			throw new IndexOutOfBoundsException();
		}
		*/

		this.addBuffer = newAddBuffer;
	}

	private final void setPushBuffer(char[] newPushBuffer)
	{
		this.pushBuffer = newPushBuffer;
	}

	private static final char[] getNewBuffer(int neededLength)
	{
		final char[] result;

		if (neededLength < minBufferLength)
		{
			result = new char[minBufferLength];
		}
		else if (neededLength == Integer.MAX_VALUE)
		{
			result = new char[Integer.MAX_VALUE];
		}
		else
		{
			final int maybe = MathAutobot.roundUpToNearestPowerOf2(neededLength);

			if (maybe < 0)
			{
				result = new char[Integer.MAX_VALUE];
			}
			else if (maybe < minBufferLength)
			{
				result = new char[minBufferLength];
			}
			else
			{
				result = new char[maybe];
			}
		}

		// System.out.println("given needed length of " + neededLength + ", returning result of length " + result.length);
		return result;
	}

	public final long size()
	{
		return this.size;
	}

	public final boolean isEmpty()
	{
		return this.size == 0;
	}

	public final boolean isNotEmpty()
	{
		return this.size != 0;
	}

	private final boolean hasBeenPushed()
	{
		return this.activeStates.hasBeenPushed;
	}

	private final boolean hasNodes()
	{
		return this.activeStates.hasBeenNoded;
	}

	private final boolean hasBeenAdded()
	{
		return this.activeStates.hasBeenAdded;
	}

	public final char charAt(long index)
	{
		if (index >= this.size)
		{
			throw new EnhancedArrayIndexOutOfBoundsException(this.size, index);
		}

		if (this.hasBeenPushed())
		{
			final long temp = this.pushPos + 1 + index;

			if (temp < this.pushBuffer.length)
			{
				return this.pushBuffer[(int) temp];
			}
			else
			{
				index -= this.pushPos + 1;
			}
		}

		if (this.head != null)
		{
			if (index < this.head.core.length)
			{
				return this.head.core[(int) index];
			}

			index -= this.head.core.length;

			CharNode next = this.head.next;

			while (next != null)
			{
				if (index < next.core.length)
				{
					return next.core[(int) index];
				}
				else
				{
					index -= next.core.length;
				}
			}
		}

		// by this point, index will implicitly work directly into addBuffer
		return this.addBuffer[(int) index];
	}

	public final char firstChar()
	{
		switch (this.activeStates)
		{
			case hasBeenPushed_hasNoNodes_hasBeenAdded:
			case hasBeenPushed_hasNoNodes_hasNotBeenAdded:
			case hasBeenPushed_hasNodes_hasBeenAdded:
			case hasBeenPushed_hasNodes_hasNotBeenAdded:
			{
				return this.pushBuffer[this.pushPos + 1];
			}
			case hasNotBeenPushed_hasNoNodes_hasBeenAdded:
			{
				return this.addBuffer[0];
			}
			case hasNotBeenPushed_hasNoNodes_hasNotBeenAdded:
			{
				throw new IndexOutOfBoundsException();
			}
			case hasNotBeenPushed_hasNodes_hasBeenAdded:
			case hasNotBeenPushed_hasNodes_hasNotBeenAdded:
			{
				return this.head.firstChar();
			}
			default:
			{
				throw new UnhandledEnumException(this.activeStates);
			}
		}
	}

	public final char lastChar()
	{
		switch (this.activeStates)
		{
			case hasBeenPushed_hasNoNodes_hasBeenAdded:
			case hasBeenPushed_hasNodes_hasBeenAdded:
			case hasNotBeenPushed_hasNoNodes_hasBeenAdded:
			case hasNotBeenPushed_hasNodes_hasBeenAdded:
			{
				return this.addBuffer[this.addPos - 1];
			}
			case hasBeenPushed_hasNoNodes_hasNotBeenAdded:
			{
				return this.pushBuffer[this.pushBuffer.length - 1];
			}
			case hasNotBeenPushed_hasNoNodes_hasNotBeenAdded:
			{
				throw new IndexOutOfBoundsException();
			}
			case hasBeenPushed_hasNodes_hasNotBeenAdded:
			case hasNotBeenPushed_hasNodes_hasNotBeenAdded:
			{
				return this.head.lastChar();
			}
			default:
			{
				throw new UnhandledEnumException(this.activeStates);
			}
		}
	}

	public final void clear()
	{
		this.clear(minBufferLength);
	}

	public final void clear(int initialCapacity)
	{
		this.head = null;
		this.tail = null;
		this.size = 0;

		this.pushBuffer = new char[initialCapacity];
		this.pushPos = initialCapacity - 1;

		this.addBuffer = new char[initialCapacity];
		this.addPos = 0;

		this.activeStates = ActiveStates.hasNotBeenPushed_hasNoNodes_hasNotBeenAdded;
	}

	public final char[] toCharArray()
	{
		if (this.size > Integer.MAX_VALUE)
		{
			throw new IndexOutOfBoundsException();
		}

		if (this.size == 0)
		{
			return EmptyPrimitiveArrays.emptyCharArr;
		}

		final char[] result = new char[(int) this.size];
		int offset = 0;

		if (this.hasBeenPushed())
		{
			final int length = this.pushBuffer.length - 1 - this.pushPos;
			// EnhancedArrayIndexOutOfBoundsException.checkArrayContents(this.pushBuffer, this.pushPos+1 + length - 1);
			// EnhancedArrayIndexOutOfBoundsException.checkArrayContents(result, offset + length - 1);

			System.arraycopy(this.pushBuffer, this.pushPos + 1, result, offset, length);
			offset += length;
		}

		if (this.head != null)
		{
			offset = this.head.arrayCopy(result, offset);

			if (this.head != this.tail)
			{
				CharNode curr = this.head.next;

				do
				{
					offset = curr.arrayCopy(result, offset);
					curr = curr.next;
				} while (curr != null);
			}
		}

		if (this.hasBeenAdded())
		{
			System.arraycopy(this.addBuffer, 0, result, offset, this.addPos);

			if (diag_checkActiveStates)
			{
				offset += this.addPos;
			}
			// System.out.println("triggered");
		}

		if (diag_checkActiveStates)
		{
			if (result.length != offset)
			{
				if (offset + this.addPos == result.length)
				{
					throw new IllegalArgumentException("addBuffer state is bad!\r\n\tgood chars: " + new String(result) + "\r\n\tchars missing: " + this.addPos + "\r\n\t actual chars: \"" + new String(this.addBuffer, 0, this.addPos) + '"');
				}

				throw new IllegalArgumentException("active states: " + this.activeStates);
			}
		}

		return result;
	}

	public final char[][] toCharArrays()
	{
		if (this.size <= Integer.MAX_VALUE)
		{
			return new char[][] {
									this.toCharArray() };
		}

		final int div = (int) (this.size / Integer.MAX_VALUE);
		final int mod = (int) (this.size % Integer.MAX_VALUE);
		final CharListIterator itsy = this.listIterator();
		final char[][] result;

		if (mod == 0)
		{
			result = new char[div][];
			
			for (int i = 0; i < div; i += 1)
			{
				itsy.addToPosition(Integer.MAX_VALUE);
				result[i] = itsy.precedingToCharArray();
			}
		}
		else
		{
			result = new char[div + 1][];
			
			for (int i = 0; i < div; i += 1)
			{
				itsy.addToPosition(Integer.MAX_VALUE);
				result[i] = itsy.precedingToCharArray();
			}
			
			itsy.previous();
			
			result[div] = itsy.remainingToCharArray();
		}
		
		return result;
	}

	@Override
	public final String toString()
	{
		return new String(this.toCharArray());
	}

	@Override
	public final CharListIterator listIterator()
	{
		return new CharListIterator(this);
	}

	private static final class CharNode
	{
		private static final void linkNodes(CharNode pre, CharNode post)
		{
			if (pre == null)
			{
				if (post == null)
				{
					throw new IllegalArgumentException("double-null call!");
				}

				post.prev = null;
			}
			else
			{
				if (post == null)
				{
					pre.next = null;
				}
				else
				{
					pre.next = post;
					post.prev = pre;
				}
			}
		}

		private final char[] core;
		private CharNode prev;

		private CharNode next;

		private CharNode(char[] core)
		{
			this(core, null, null);
		}

		private CharNode(char[] inCore, CharNode inPrev, CharNode inNext)
		{
			this.core = inCore;
			this.prev = inPrev;
			this.next = inNext;
		}

		private final boolean hasNext()
		{
			return this.next != null;
		}

		private final boolean hasPrev()
		{
			return this.prev != null;
		}

		/**
		 * copies all of this node's core to dest
		 * 
		 * @param dest
		 * @param destPos
		 * @return the new destPos
		 */
		private final int arrayCopy(char[] dest, int destPos)
		{
			EnhancedArrayIndexOutOfBoundsException.checkArrayCopy(this.core, 0, dest, destPos, this.core.length);
			System.arraycopy(this.core, 0, dest, destPos, this.core.length);
			return destPos + this.core.length;
		}

		private final char firstChar()
		{
			return this.core[0];
		}

		private final char lastChar()
		{
			return this.core[this.core.length - 1];
		}
	}

	public static final class CharListIterator implements AbstractCharListIterator
	{
		private final BaseCharList core;
		private long index; // represents currently consumed index, with the obvious exception of before the first consumption is called
		private GoStates goState;
		private IterationStates iterationState;
		private CharNode currNode;
		private char[] subArray;
		private int subIndex;

		private CharListIterator(BaseCharList core)
		{
			this.core = core;
			this.index = 0;
			this.goState = GoStates.Start;

			switch (this.core.activeStates)
			{
				case hasBeenPushed_hasNoNodes_hasBeenAdded:
				case hasBeenPushed_hasNoNodes_hasNotBeenAdded:
				{
					this.iterationState = IterationStates.Push;
					this.subIndex = this.core.pushPos + 1;
					this.subArray = this.core.pushBuffer;
					this.currNode = null;
					break;
				}
				case hasBeenPushed_hasNodes_hasBeenAdded:
				case hasBeenPushed_hasNodes_hasNotBeenAdded:
				{
					this.iterationState = IterationStates.Push;
					this.subIndex = this.core.pushPos + 1;
					this.subArray = this.core.pushBuffer;
					this.currNode = core.head;
					break;
				}
				case hasNotBeenPushed_hasNoNodes_hasBeenAdded:
				{
					this.iterationState = IterationStates.Add;
					this.subIndex = 0;
					this.currNode = null;
					this.subArray = this.core.addBuffer;
					break;
				}
				case hasNotBeenPushed_hasNoNodes_hasNotBeenAdded:
				{
					this.iterationState = IterationStates.Empty;
					this.subIndex = -1;
					this.currNode = null;
					this.subArray = EmptyPrimitiveArrays.emptyCharArr;
					break;
				}
				case hasNotBeenPushed_hasNodes_hasBeenAdded:
				case hasNotBeenPushed_hasNodes_hasNotBeenAdded:
				{
					this.iterationState = IterationStates.Nodes;
					this.currNode = this.core.head;
					this.subArray = this.currNode.core;
					this.subIndex = 0;
					break;
				}
				default:
				{
					throw new UnhandledEnumException(this.core.activeStates);
				}
			}
		}

		@Override
		public final boolean hasNext()
		{
			return this.index < (this.core.size - 1);
		}

		@Override
		public final char next()
		{
			final char result;

			switch (this.goState)
			{
				case Forward:
				{
					this.incrementIndeces();
					break;
				}
				case Start:
				case Backward:
				{
					this.goState = GoStates.Forward;
					break;
				}
				default:
				{
					throw new UnhandledEnumException(this.goState);
				}
			}

			switch (this.iterationState)
			{
				case Push:
				{
					result = this.next_Push();

					break;
				}
				case Nodes:
				{
					result = this.next_Nodes();
					break;
				}
				case Add:
				{
					result = this.next_Add();
					break;
				}
				default:
				{
					throw new UnhandledEnumException(this.iterationState);
				}
			}

			return result;
		}

		private final void incrementIndeces()
		{
			this.subIndex += 1;
			this.index += 1;
		}

		private final void decrementIndeces()
		{
			this.subIndex -= 1;
			this.index -= 1;
		}

		/**
		 * presumes that subIndex has been incremented already, if necessary
		 * 
		 * @return
		 */
		private final char next_Push()
		{
			final char result;

			if (this.subIndex < this.subArray.length) // this.subArray should be core's pushBuffer
			{
				result = this.subArray[this.subIndex];
			}
			else
			{
				if (this.core.hasNodes())
				{
					this.iterationState = IterationStates.Nodes;
					result = this.setCurrNodeAndGetFirstChar(this.core.head);
				}
				else
				{
					result = this.next_Add_first();
				}
			}

			return result;
		}

		/**
		 * presumes that subIndex has been incremented already, if necessary
		 * 
		 * @return
		 */
		private final char next_Nodes()
		{
			final char result;

			if (this.subIndex < this.currNode.core.length)
			{
				result = this.currNode.core[this.subIndex];
			}
			else
			{
				if (this.currNode.hasNext())
				{
					result = this.setCurrNodeAndGetFirstChar(this.currNode.next);
				}
				else
				{
					result = this.next_Add_first();
				}
			}

			return result;
		}

		/**
		 * presumes that subIndex has been incremented already, if necessary
		 * 
		 * @return
		 */
		private final char next_Add()
		{
			if (this.subIndex >= this.core.addPos)
			{
				throw new EnhancedArrayIndexOutOfBoundsException(this.core.size, this.index, this.core.addPos, this.subIndex);
			}

			return this.subArray[this.subIndex];
		}

		private final char next_Add_first()
		{
			if (!this.core.hasBeenAdded())
			{
				throw new EnhancedArrayIndexOutOfBoundsException(0, 0);
			}

			this.iterationState = IterationStates.Add;
			return this.setSubArrayAndGetFirstChar(this.core.addBuffer);
		}

		private final char setCurrNodeAndGetFirstChar(CharNode in)
		{
			this.currNode = in;
			return this.setSubArrayAndGetFirstChar(in.core);
		}

		private final char setSubArrayAndGetFirstChar(char[] in)
		{
			this.subArray = in;
			this.subIndex = 0;
			return in[0];
		}

		@Override
		public final char next_asChar()
		{
			return this.next();
		}

		@Override
		public final void add(char in)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public final boolean hasPrevious()
		{
			return this.index > 0;
		}

		@Override
		public final long nextIndex()
		{
			if (this.goState == GoStates.Start)
			{
				return 0;
			}
			else
			{
				return this.index + 1;
			}
		}

		@Override
		public final char previous()
		{
			final char result;

			switch (this.goState)
			{
				case Forward:
				case Start:
				{
					this.goState = GoStates.Backward;
					break;
				}
				case Backward:
				{
					this.decrementIndeces();
					break;
				}
				default:
				{
					throw new UnhandledEnumException(this.goState);
				}
			}

			switch (this.iterationState)
			{
				case Push:
				{
					result = this.previous_Push();

					break;
				}
				case Nodes:
				{
					result = this.previous_Nodes();
					break;
				}
				case Add:
				{
					result = this.previous_Add();
					break;
				}
				default:
				{
					throw new UnhandledEnumException(this.iterationState);
				}
			}

			return result;
		}

		private final char previous_Push()
		{
			if (this.subIndex <= this.core.pushPos)
			{
				throw new EnhancedArrayIndexOutOfBoundsException(0, 0);
			}

			return this.subArray[this.subIndex];
		}

		private final char previous_Push_last()
		{
			if (!this.core.hasBeenPushed())
			{
				throw new EnhancedArrayIndexOutOfBoundsException(0, 0);
			}

			this.iterationState = IterationStates.Push;
			return this.setSubArrayAndGetLastChar(this.core.pushBuffer);
		}

		private final char previous_Nodes()
		{
			final char result;

			if (this.subIndex >= 0)
			{
				return this.subArray[this.subIndex];
			}
			else
			{
				if (this.currNode.hasPrev())
				{
					result = this.setCurrNodeAndGetLastChar(this.currNode.prev);
				}
				else
				{
					result = this.previous_Push_last();
				}
			}

			return result;
		}

		private final char previous_Add()
		{
			final char result;

			if (this.subIndex >= 0)
			{
				result = this.subArray[this.subIndex];
			}
			else
			{
				if (this.core.hasNodes())
				{
					this.iterationState = IterationStates.Nodes;
					result = this.setCurrNodeAndGetLastChar(this.core.tail);
				}
				else
				{
					result = this.previous_Push_last();
				}
			}

			return result;
		}

		private final char setCurrNodeAndGetLastChar(CharNode in)
		{
			this.currNode = in;
			return this.setSubArrayAndGetLastChar(in.core);
		}

		private final char setSubArrayAndGetLastChar(char[] in)
		{
			this.subArray = in;
			this.subIndex = this.subArray.length - 1;
			return in[this.subIndex];
		}

		@Override
		public final long previousIndex()
		{
			return this.index - 1;
		}

		@Override
		public final void remove()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public final void set(char in)
		{
			this.subArray[this.subIndex] = in;
		}

		/**
		 * does not include current char
		 * 
		 * @return
		 */
		public final char[] precedingToCharArray()
		{
			if (this.index > Integer.MAX_VALUE)
			{
				throw new IllegalArgumentException("arrays need to have (signed int)-sized lengths");
			}

			final char[] result = new char[(int) this.index];
			int offset = 0;

			if (this.core.hasBeenPushed())
			{
				final int pushLength = this.core.pushBuffer.length - this.core.pushPos;

				if (pushLength > this.index)
				{
					System.arraycopy(this.core.pushBuffer, this.core.pushPos, result, offset, (int) this.index);
					return result;
				}
				else
				{
					System.arraycopy(this.core.pushBuffer, this.core.pushPos, result, offset, pushLength);
					offset += pushLength;
				}
			}

			if (this.core.hasNodes())
			{
				CharNode currNode = this.core.head;

				loop: while (true)
				{
					final char[] subArray = currNode.core;

					if (offset + subArray.length > this.index)
					{
						System.arraycopy(subArray, 0, result, offset, (int) (this.index - offset));
						return result;
					}

					System.arraycopy(subArray, 0, result, offset, subArray.length);
					offset += subArray.length;

					if (!currNode.hasNext())
					{
						break loop;
					}
				}
			}

			if (this.core.hasBeenAdded())
			{
				if (offset + this.core.addPos > this.index)
				{
					System.arraycopy(this.core.addBuffer, 0, result, offset, (int) (this.index - offset));
					return result;
				}
				else
				{
					System.arraycopy(this.core.addBuffer, 0, result, offset, this.core.addPos);
				}
			}

			return result;
		}

		/**
		 * includes current char
		 * 
		 * @return
		 */
		public final char[] remainingToCharArray()
		{
			final long remainingLength = this.core.size - this.index;

			if (remainingLength >= Integer.MAX_VALUE)
			{
				throw new IllegalArgumentException("arrays need to have (signed int)-sized lengths");
			}
			else if (remainingLength == 1)
			{
				return new char[] {
									this.subArray[this.subIndex] };
			}

			final char[] result = new char[(int) remainingLength];
			int offset = 0;
			int length = this.subArray.length - this.subIndex;

			System.arraycopy(this.subArray, this.subIndex, result, 0, length);
			offset += length;

			if (offset != remainingLength)
			{
				switch (this.iterationState)
				{
					case Push:
					{
						if (this.core.hasNodes())
						{
							offset = this.copyInNodes(this.core.head, result, offset);
						}

						if (this.core.hasBeenAdded())
						{
							System.arraycopy(this.core.addBuffer, 0, result, offset, this.core.addPos);
						}

						break;
					}
					case Nodes:
					{
						if (this.currNode.hasNext())
						{
							offset = this.copyInNodes(this.currNode.next, result, offset);
						}

						if (this.core.hasBeenAdded())
						{
							System.arraycopy(this.core.addBuffer, 0, result, offset, this.core.addPos);
						}

						break;
					}
					case Add:
					{
						// because, if it's here, it's somehow bypassed the length check immediately before this switch
						throw new IllegalArgumentException();
					}
					default:
					{
						throw new UnhandledEnumException(this.iterationState);
					}
				}
			}

			return result;
		}

		private final int copyInNodes(CharNode currNode, char[] result, int offset)
		{
			loop: while (true)
			{
				final char[] subArray = currNode.core;
				System.arraycopy(subArray, 0, result, offset, subArray.length);
				offset += subArray.length;

				if (!currNode.hasNext())
				{
					break loop;
				}
			}

			return offset;
		}

		public final void addToPosition(long posDelta)
		{
			if (posDelta < 0)
			{
				throw new FatalLazinessException("handle negative index changes");
			}

			if ((this.index + posDelta) >= this.core.size)
			{
				throw new EnhancedArrayIndexOutOfBoundsException(this.core.size, this.index + posDelta);
			}

			switcher: switch (this.iterationState)
			{
				case Push:
				{
					if ((this.subIndex + posDelta) < this.subArray.length)
					{
						this.subIndex += posDelta;
						break switcher;
					}
					else
					{
						posDelta -= (this.subArray.length - this.subIndex);
					}

					this.currNode = this.core.head;
					this.addToPosition_helper(posDelta);
					break switcher;
				}
				case Nodes:
				{
					if ((this.subIndex + posDelta) < this.subArray.length)
					{
						this.subIndex += posDelta;
						break switcher;
					}
					else
					{
						posDelta -= (this.subArray.length - this.subIndex);
					}

					this.currNode = this.currNode.next;
					this.addToPosition_helper(posDelta);
					break switcher;
				}
				case Add:
				{
					if ((this.subIndex + posDelta) < this.subArray.length)
					{
						this.subIndex += posDelta;
						break switcher;
					}
					else
					{
						throw new IndexOutOfBoundsException();
					}
				}
				default:
				{
					throw new UnhandledEnumException(this.iterationState);
				}
			}

			this.index += posDelta;
		}

		private final void addToPosition_helper(long posDelta)
		{
			System.out.println("going in with posDelta: " + posDelta);

			while (this.currNode != null)
			{
				this.subArray = this.currNode.core;

				if (posDelta < this.subArray.length)
				{
					this.subIndex = (int) posDelta;
					return;
				}
				else
				{
					posDelta -= this.subArray.length;
					this.currNode = this.currNode.next;
				}
			}

			if (this.core.hasBeenAdded())
			{
				this.subArray = this.core.addBuffer;
				this.subIndex = 0;

				if (posDelta < this.subArray.length)
				{
					this.subIndex = (int) posDelta;
					return;
				}
				else
				{
					throw new EnhancedArrayIndexOutOfBoundsException(this.subArray.length, posDelta);
				}
			}
			else
			{
				throw new EnhancedArrayIndexOutOfBoundsException(this.subArray.length, posDelta);
			}
		}
	}

	private static enum GoStates
	{
		/**
		 * index == 0, nothing consumed yet
		 */
		Start,

		/**
		 * (index-1) was the last consumed element
		 */
		Forward,

		/**
		 * (index+1) was the last consumed element
		 */
		Backward;
	}

	private static enum IterationStates
	{
		Push,
		Nodes,
		Add,
		Empty;
	}

	protected static enum ActiveStates
	{
		hasNotBeenPushed_hasNoNodes_hasNotBeenAdded(
				false,
				false,
				false),
		hasNotBeenPushed_hasNoNodes_hasBeenAdded(
				false,
				false,
				true),
		hasNotBeenPushed_hasNodes_hasNotBeenAdded(
				false,
				true,
				false),
		hasNotBeenPushed_hasNodes_hasBeenAdded(
				false,
				true,
				true),
		hasBeenPushed_hasNoNodes_hasNotBeenAdded(
				true,
				false,
				false),
		hasBeenPushed_hasNoNodes_hasBeenAdded(
				true,
				false,
				true),
		hasBeenPushed_hasNodes_hasNotBeenAdded(
				true,
				true,
				false),
		hasBeenPushed_hasNodes_hasBeenAdded(
				true,
				true,
				true);

		private final boolean hasBeenPushed;
		private final boolean hasBeenNoded;
		private final boolean hasBeenAdded;

		private ActiveStates(boolean inHasBeenPushed, boolean inHasBeenNoded, boolean inHasBeenAdded)
		{
			this.hasBeenPushed = inHasBeenPushed;
			this.hasBeenNoded = inHasBeenNoded;
			this.hasBeenAdded = inHasBeenAdded;
		}

		public final boolean hasBeenPushed()
		{
			return this.hasBeenPushed;
		}

		public final boolean hasBeenNoded()
		{
			return this.hasBeenNoded;
		}

		public final boolean hasBeenAdded()
		{
			return this.hasBeenAdded;
		}

		public final ActiveStates togglePushed()
		{
			final ActiveStates result;

			switch (this)
			{
				case hasBeenPushed_hasNoNodes_hasBeenAdded:
				{
					result = hasNotBeenPushed_hasNoNodes_hasBeenAdded;
					break;
				}
				case hasBeenPushed_hasNoNodes_hasNotBeenAdded:
				{
					result = hasNotBeenPushed_hasNoNodes_hasNotBeenAdded;
					break;
				}
				case hasBeenPushed_hasNodes_hasBeenAdded:
				{
					result = hasNotBeenPushed_hasNodes_hasBeenAdded;
					break;
				}
				case hasBeenPushed_hasNodes_hasNotBeenAdded:
				{
					result = hasNotBeenPushed_hasNodes_hasNotBeenAdded;
					break;
				}
				case hasNotBeenPushed_hasNoNodes_hasBeenAdded:
				{
					result = hasBeenPushed_hasNoNodes_hasBeenAdded;
					break;
				}
				case hasNotBeenPushed_hasNoNodes_hasNotBeenAdded:
				{
					result = hasBeenPushed_hasNoNodes_hasNotBeenAdded;
					break;
				}
				case hasNotBeenPushed_hasNodes_hasBeenAdded:
				{
					result = hasBeenPushed_hasNodes_hasBeenAdded;
					break;
				}
				case hasNotBeenPushed_hasNodes_hasNotBeenAdded:
				{
					result = hasBeenPushed_hasNodes_hasNotBeenAdded;
					break;
				}
				default:
				{
					throw new UnhandledEnumException(this);
				}
			}

			if (result.hasBeenPushed == false)
			{
				System.out.println(this.toString() + " turned off, into " + result.toString());
			}

			return result;
		}

		public final ActiveStates toggleNoded()
		{
			final ActiveStates result;

			switch (this)
			{
				case hasBeenPushed_hasNoNodes_hasBeenAdded:
				{
					result = hasBeenPushed_hasNodes_hasBeenAdded;
					break;
				}
				case hasBeenPushed_hasNoNodes_hasNotBeenAdded:
				{
					result = hasBeenPushed_hasNodes_hasNotBeenAdded;
					break;
				}
				case hasBeenPushed_hasNodes_hasBeenAdded:
				{
					result = hasBeenPushed_hasNoNodes_hasBeenAdded;
					break;
				}
				case hasBeenPushed_hasNodes_hasNotBeenAdded:
				{
					result = hasBeenPushed_hasNoNodes_hasNotBeenAdded;
					break;
				}
				case hasNotBeenPushed_hasNoNodes_hasBeenAdded:
				{
					result = hasNotBeenPushed_hasNodes_hasBeenAdded;
					break;
				}
				case hasNotBeenPushed_hasNoNodes_hasNotBeenAdded:
				{
					result = hasNotBeenPushed_hasNodes_hasNotBeenAdded;
					break;
				}
				case hasNotBeenPushed_hasNodes_hasBeenAdded:
				{
					result = hasNotBeenPushed_hasNoNodes_hasBeenAdded;
					break;
				}
				case hasNotBeenPushed_hasNodes_hasNotBeenAdded:
				{
					result = hasNotBeenPushed_hasNoNodes_hasNotBeenAdded;
					break;
				}
				default:
				{
					throw new UnhandledEnumException(this);
				}
			}

			if (result.hasBeenNoded == false)
			{
				System.out.println(this.toString() + " turned off, into " + result.toString());
			}

			return result;
		}

		public final ActiveStates toggleAdded()
		{
			final ActiveStates result;

			switch (this)
			{
				case hasBeenPushed_hasNoNodes_hasBeenAdded:
				{
					result = hasBeenPushed_hasNoNodes_hasNotBeenAdded;
					break;
				}
				case hasBeenPushed_hasNoNodes_hasNotBeenAdded:
				{
					result = hasBeenPushed_hasNoNodes_hasBeenAdded;
					break;
				}
				case hasBeenPushed_hasNodes_hasBeenAdded:
				{
					result = hasBeenPushed_hasNodes_hasNotBeenAdded;
					break;
				}
				case hasBeenPushed_hasNodes_hasNotBeenAdded:
				{
					result = hasBeenPushed_hasNodes_hasBeenAdded;
					break;
				}
				case hasNotBeenPushed_hasNoNodes_hasBeenAdded:
				{
					result = hasNotBeenPushed_hasNoNodes_hasNotBeenAdded;
					break;
				}
				case hasNotBeenPushed_hasNoNodes_hasNotBeenAdded:
				{
					result = hasNotBeenPushed_hasNoNodes_hasBeenAdded;
					break;
				}
				case hasNotBeenPushed_hasNodes_hasBeenAdded:
				{
					result = hasNotBeenPushed_hasNodes_hasNotBeenAdded;
					break;
				}
				case hasNotBeenPushed_hasNodes_hasNotBeenAdded:
				{
					result = hasNotBeenPushed_hasNodes_hasBeenAdded;
					break;
				}
				default:
				{
					throw new UnhandledEnumException(this);
				}
			}

			if (result.hasBeenAdded == false)
			{
				System.out.println(this.toString() + " turned off, into " + result.toString());
			}

			return result;
		}
	}
}
