package HandyStuff.HandyEnums;

public enum FuzzyQuantifiers
{
	Equal,
	Unequal,
	Equivalent,
	Similar,
	Different,
	Smaller,
	SmallerThanOrEqualTo,
	LessThan,
	LessThanOrEqualTo,
	Bigger,
	BiggerThanOrEqualTo,
	GreaterThan,
	GreaterThanOrEqualTo;

	/**
	 * Equality > Equivalence > Similarity
	 * @return
	 */
	public final boolean impliesEquality()
	{
		final boolean result;

		switch (this)
		{
			case Equal:
			{
				result = true;
				break;
			}
			default:
			{
				result = false;
				break;
			}
		}

		return result;
	}
	
	public final boolean impliesInequality()
	{
		return !this.impliesEquality();
	}

	/**
	 * Equality > Equivalence > Similarity
	 * @return
	 */
	public final boolean impliesEquivalence()
	{
		final boolean result;

		switch (this)
		{
			case Equal:
			case Equivalent:
			{
				result = true;
				break;
			}
			default:
			{
				result = false;
				break;
			}
		}

		return result;
	}

	/**
	 * Equality > Equivalence > Similarity
	 * @return
	 */
	public final boolean impliesSimilarity()
	{
		final boolean result;

		switch (this)
		{
			case Equal:
			case Equivalent:
			case Similar:
			{
				result = true;
				break;
			}
			default:
			{
				result = false;
				break;
			}
		}

		return result;
	}
	
	public final boolean impliesRelationship_lessThan()
	{
		final boolean result;

		switch (this)
		{
			case Smaller:
			case LessThan:
			{
				result = true;
				break;
			}
			default:
			{
				result = false;
				break;
			}
		}

		return result;
	}
	
	public final boolean impliesRelationship_lessThanOrEqualTo()
	{
		final boolean result;

		switch (this)
		{
			case SmallerThanOrEqualTo:
			case LessThanOrEqualTo:
			{
				result = true;
				break;
			}
			default:
			{
				result = false;
				break;
			}
		}

		return result;
	}
	
	public final boolean impliesRelationship_greaterThanOrEqualTo()
	{
		final boolean result;

		switch (this)
		{
			case BiggerThanOrEqualTo:
			case GreaterThanOrEqualTo:
			{
				result = true;
				break;
			}
			default:
			{
				result = false;
				break;
			}
		}

		return result;
	}
}
