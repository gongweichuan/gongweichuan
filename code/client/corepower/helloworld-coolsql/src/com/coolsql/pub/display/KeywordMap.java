package com.coolsql.pub.display;

/*
 * KeywordMap.java - Fast keyword->id map
 * Copyright (C) 1998, 1999 Slava Pestov
 * Copyright (C) 1999 Mike Dillon
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

import javax.swing.text.Segment;

import com.coolsql.pub.display.Token;
import com.coolsql.pub.util.SyntaxUtilities;

/**
 * A <code>KeywordMap</code> is similar to a hashtable in that it maps keys
 * to values. However, the `keys' are Swing segments. This allows lookups of
 * text substrings without the overhead of creating a new string object.
 * <p>
 * This class is used by <code>CTokenMarker</code> to map keywords to ids.
 *
 * @author Slava Pestov, Mike Dillon
 * @version $Id: KeywordMap.java,v 1.1 2010/08/03 14:09:19 xiaolin Exp $
 */
public class KeywordMap
{
	/**
	 * Creates a new <code>KeywordMap</code>.
	 * @param ignoreCase True if keys are case insensitive
	 */
	public KeywordMap(boolean ignoreCase)
	{
		this(ignoreCase, 52);
		this.ignoreCase = ignoreCase;
	}

	/**
	 * Creates a new <code>KeywordMap</code>.
	 * @param ignoreCase True if the keys are case insensitive
	 * @param mapLength The number of `buckets' to create.
	 * A value of 52 will give good performance for most maps.
	 */
	public KeywordMap(boolean ignoreCase, int mapLength)
	{
		this.mapLength = mapLength;
		this.ignoreCase = ignoreCase;
		map = new Keyword[mapLength];
	}

	/**
	 * Looks up a key.
	 * @param text The text segment
	 * @param offset The offset of the substring within the text segment
	 * @param length The length of the substring
	 */
	public byte lookup(Segment text, int offset, int length)
	{
		if(length == 0)	return Token.NULL;
		int i = getSegmentMapKey(text, offset, length);
		if (i < 0) return Token.NULL;
		Keyword k = map[i];
		while(k != null)
		{
			if(length != k.keyword.length)
			{
				k = k.next;
				continue;
			}
			if(SyntaxUtilities.regionMatches(ignoreCase,text,offset,k.keyword))
				return k.id;
			k = k.next;
		}
		return Token.NULL;
	}

	public boolean containsKey(String aKey)
	{
		if (aKey == null) return false;
		Segment s = new Segment(aKey.toCharArray(), 0, aKey.length());
		return (this.lookup(s, 0, aKey.length()) != Token.NULL);
	}
	
	/**
	 * Adds a key-value mapping.
	 * @param keyword The key
	 * @param id The value
	 */
	public void add(String keyword, byte id)
	{
		if (containsKey(keyword)) return;
		int key = getStringMapKey(keyword);
		map[key] = new Keyword(keyword.toCharArray(),id,map[key]);
	}

	/**
	 * Returns true if the keyword map is set to be case insensitive,
	 * false otherwise.
	 */
	public boolean getIgnoreCase()
	{
		return ignoreCase;
	}

	/**
	 * Sets if the keyword map should be case insensitive.
	 * @param ignoreCase True if the keyword map should be case
	 * insensitive, false otherwise
	 */
	public void setIgnoreCase(boolean ignoreCase)
	{
		this.ignoreCase = ignoreCase;
	}

	// protected members
	protected int mapLength;

	protected int getStringMapKey(String s)
	{
		return (Character.toUpperCase(s.charAt(0)) + Character.toUpperCase(s.charAt(s.length()-1)))	% mapLength;
	}

	protected int getSegmentMapKey(Segment s, int off, int len)
	{
		if (off < 0) return -1;
		if ((off + len - 1) > s.getEndIndex()) return -1;
		if ((off + len - 1) < 0) return -1;
		return (Character.toUpperCase(s.array[off]) + Character.toUpperCase(s.array[off + len - 1])) % mapLength;
	}

	
	static class Keyword
	{
		public Keyword(char[] keyword, byte id, Keyword next)
		{
			this.keyword = keyword;
			this.id = id;
			this.next = next;
		}

		public char[] keyword;
		public byte id;
		public Keyword next;
	}

	private Keyword[] map;
	private boolean ignoreCase;
}
