/*
 * Copyright (c) 2019 Adam <Adam@sigterm.info>
 * Copyright (c) 2021 Nick Wolff <nick@wolff.tech>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

//Taken from https://github.com/WolffTech/coal-bag-plugin/blob/master/src/main/java/com/coalbagplugin/CoalBag.java

package com.github.calebwhiting.runelite.plugins.actionprogress;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoalBag
{
	private static final int UNKNOWN_AMOUNT = 0;
	private static final int EMPTY_AMOUNT = 0;

	private static final Pattern BAG_EMPTY_MESSAGE = Pattern.compile("^The coal bag is (?:now\\s)?empty\\.");
	private static final Pattern BAG_ONE_OR_MANY_MESSAGE = Pattern.compile("^The coal bag (?:still\\s)?contains ([\\d]+|one) pieces? of coal\\.");

	private static int storedAmount;

	private static void setAmount(int amount)
	{
		storedAmount = amount;
	}

	private static void setEmptyAmount()
	{
		storedAmount = EMPTY_AMOUNT;
	}

	public static void setUnknownAmount()
	{
		storedAmount = UNKNOWN_AMOUNT;
	}

	public static int getAmount()
	{
		return storedAmount;
	}

	public static void updateAmount(String message)
	{
		final Matcher emptyMatcher = BAG_EMPTY_MESSAGE.matcher(message);
		if (emptyMatcher.matches())
		{
			setEmptyAmount();
		}
		else
		{
			final Matcher oneOrManyMatcher = BAG_ONE_OR_MANY_MESSAGE.matcher(message);
			if (oneOrManyMatcher.matches())
			{
				final String match = oneOrManyMatcher.group(1);
				if (match.equals("one"))
				{
					setAmount(1);
				} else {
					setAmount(Integer.parseInt(match));
				}
			}
		}
	}

	public static boolean isUnknown()
	{
		return storedAmount == UNKNOWN_AMOUNT;
	}

	public static boolean isEmpty()
	{
		return storedAmount == EMPTY_AMOUNT;
	}
}