package com.coolsql.pub.display;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 * Token.java - Generic token
 * Copyright (C) 1998, 1999 Slava Pestov
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

/**
 * A linked list of tokens. Each token has three fields - a token
 * identifier, which is a byte value that can be looked up in the
 * array returned by <code>SyntaxDocument.getColors()</code>
 * to get a color value, a length value which is the length of the
 * token in the text, and a pointer to the next token in the list.
 *
 * @author Slava Pestov
 * @version $Id: Token.java,v 1.1 2010/08/03 14:09:19 xiaolin Exp $
 */
public class Token
{
	/**
	 * Normal text token id. This should be used to mark
	 * normal text.
	 */
	public static final byte NULL = 0;

	/**
	 * Comment 1 token id. This can be used to mark a comment.
	 */
	public static final byte COMMENT1 = 1;

	/**
	 * Comment 2 token id. This can be used to mark a comment.
	 */
	public static final byte COMMENT2 = 2;

	
	/**
	 * Literal 1 token id. This can be used to mark a string
	 * literal (eg, C mode uses this to mark "..." literals)
	 */
	public static final byte LITERAL1 = 3;

	/**
	 * Literal 2 token id. This can be used to mark an object
	 * literal (eg, Java mode uses this to mark true, false, etc)
	 */
	public static final byte LITERAL2 = 4;

	/**
	 * Label token id. This can be used to mark labels
	 * (eg, C mode uses this to mark ...: sequences)
	 */
	public static final byte LABEL = 5;

	/**
	 * Keyword 1 token id. This can be used to mark a
	 * keyword. This should be used for general language
	 * constructs.
	 */
	public static final byte KEYWORD1 = 6;

	/**
	 * Keyword 2 token id. This can be used to mark a
	 * keyword. This should be used for preprocessor
	 * commands, or variables.
	 */
	public static final byte KEYWORD2 = 7;

	/**
	 * Keyword 3 token id. This can be used to mark a
	 * keyword. This should be used for data types.
	 */
	public static final byte KEYWORD3 = 8;

	/**
	 * Operator token id. This can be used to mark an
	 * operator. (eg, SQL mode marks +, -, etc with this
	 * token type)
	 */
	public static final byte OPERATOR = 9;

	/**
	 * Invalid token id. This can be used to mark invalid
	 * or incomplete tokens, so the user can easily spot
	 * syntax errors.
	 */
	public static final byte INVALID = 10;

	/**
	 * number token id. This can be used to mark a number.
	 */
	public static final byte NUMBER= 11;
	/**
	 * The total number of defined token ids.
	 */
	public static final byte ID_COUNT = 12;

	/**
	 * The first id that can be used for internal state
	 * in a token marker.
	 */
	public static final byte INTERNAL_FIRST = 100;

	/**
	 * The last id that can be used for internal state
	 * in a token marker.
	 */
	public static final byte INTERNAL_LAST = 126;

	/**
	 * The token type, that along with a length of 0
	 * marks the end of the token list.
	 */
	public static final byte END = 127;

	/**
	 * 自定义token的关键字描述
	 */
	public static final String CUSTOMTOKEN_DES="customtoken";
	/**
	 * 保存自定义token类型id信息
	 */
	private static Map ids=new HashMap();
	
	private static PropertyChangeSupport pcs=new PropertyChangeSupport(ids); //自定义token属性变化的监控管理
	/**
	 * The length of this token.
	 */
	public int length;

	/**
	 * The id of this token.
	 */
	public byte id;

	/**
	 * The next token in the linked list.
	 */
	public Token next;

	/**
	 * Creates a new token.
	 * @param length The length of the token
	 * @param id The id of the token
	 */
	public Token(int length, byte id)
	{
		this.length = length;
		this.id = id;
	}

	/**
	 * Returns a string representation of this token.
	 */
	public String toString()
	{
		return "[id=" + id + ",length=" + length + "]";
	}
	public static int getIDCount()
	{
		return ids.size()+ID_COUNT;
	}
	/**
	 * 自定义token id，该id 同键Key进行对应
	 * @param key  与id对应的键，在取得自定义的id时通过该key获取。
	 * @param color 该token对应的颜色
	 * @return
	 */
	public synchronized static int customTokenID(String key,Color color)
	{
		Object obj=ids.get(key);
		if(obj!=null)
			return ((CustomToken)obj).getId();
		
		int newId=getNextId();
		ids.put(key, new CustomToken(newId,color,key));
		pcs.firePropertyChange(CUSTOMTOKEN_DES, null, null);
		return newId;
	}
	/**
	 * 删除自定义的id
	 * @param key
	 * @return
	 */
	public synchronized static int removeTokenID(String key)
	{
		Object v=ids.remove(key);
		if(v!=null)
		{
			adjustAfterDelete(((CustomToken)v).getId());
			pcs.firePropertyChange(CUSTOMTOKEN_DES, null, null);
			return ((CustomToken)v).getId();
		}else
			return -1;
		
	}
	/**
	 * 在删除自定义的tokenid后，为了保证后续id的在数值上的连续性，该方法做了相应的调整。
	 * @param v --被移除的id值
	 */
	private static void adjustAfterDelete(int v)
	{
		Iterator it=ids.keySet().iterator();
		while(it.hasNext())
		{
			Object key=it.next();
			CustomToken value=(CustomToken)ids.get(key);
			int tmp=value.getId();
			if(tmp>v)
			{
				tmp--;
				value.setId(tmp);
			}
		}
	}
	/**
	 * 获取下一个可用的自定义id
	 * @return
	 */
	private synchronized static int getNextId()
	{
		return ids.size()+ID_COUNT;
	}
	/**
	 * 获取所有自定义的Token ID
	 * @return
	 */
	public static CustomToken[] getCustomTokens()
	{
		
		CustomToken[] values=(CustomToken[])ids.values().toArray(new CustomToken[ids.size()]);
		return values;
	}
	public static void addPropertyListener(PropertyChangeListener listener)
	{
		pcs.addPropertyChangeListener(listener);
	}
	public static void removePropertyListener(PropertyChangeListener listener)
	{
		pcs.removePropertyChangeListener(listener);
	}
	public static class CustomToken
	{
		private String key; //自定义token的名字
		private int id;  //token id
		private Color color;  //color of token
		public CustomToken(int id,Color color,String key)
		{
			this.id=id;
			this.color=color;
			this.key=key;
		}
		public Color getColor() {
			return color;
		}
		public void setColor(Color color) {
			this.color = color;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		
	}
}
