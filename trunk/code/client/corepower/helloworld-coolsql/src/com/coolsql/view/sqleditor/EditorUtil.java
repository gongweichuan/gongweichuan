/*
 * 创建日期 2006-6-11
 *
 */
package com.coolsql.view.sqleditor;

import java.awt.Color;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


/**
 * @author liu_xlin
 *对sql编辑器的显示规则进行封装，包括关键字颜色，关键字的校验，
 */
public class EditorUtil {
    public static String KEYWORDS[] = {
            "SELECT","DELETE","INSERT","UPDATE","FROM","AND","WHERE","NOT", "NULL","DESC","BY","LIKE","COUNT", "SET",
            "DISTINCT","ALTER","SUM","AS", "ASC","IS", "IN","GROUP", "MAX", "MIN", "JOIN", "OUTER", "RIGHT","LEFT", 
            "WITH","VALUES", "AVA","BINARY", "BIT", "BOOLEAN",  "CREATE", "BYTE", "CHAR", "CHARACTER","DROP", "EXISTS",
            "AUTOINCREMENT","COUNTER", "CURRENCY", "DATABASE", "DATE", "DATETIME",  "DISALLOW",  
            "DISTINCTROW", "DOUBLE", "FLOAT", "FLOAT4", "FLOAT8", "FOREIGN", "GENERAL", "BETWEEN", "ANY","ALL", "ADD",
            "GUID", "HAVING", "INNER", "IGNORE", "IMP", "INDEX", "INT", "CONSTRAINT",
            "INTEGER", "INTEGER1", "INTEGER2", "INTEGER4", "INTO", "KEY", "LEVEL", 
            "LOGICAL", "LONG", "LONGBINARY", "LONGTEXT","MEMO","MOD", "MONEY", "COLUMN",
            "NUMBER", "NUMERIC", "OLEOBJECT", "ON", "PIVOT", "OPTION", "PRIMARY", "ORDER", 
            "OWNERACCESS", "PARAMETERS", "PERCENT", "REAL", "REFERENCES",  "SHORT", 
            "SINGLE", "SMALLINT", "SOME", "STDEV", "STDEVP", "STRING","TABLE", "TABLEID", "TEXT", 
            "TIME", "TIMESTAMP", "TOP", "TRANSFORM", "UNION", "UNIQUE",  "VALUE",  "VAR", 
            "VARBINARY", "VARCHAR", "VARP", "YESNO"
        };
    /**
     * 关键字的颜色
     */
    public final static Color keywordColor=new Color(126, 0, 75);
    /**
     * 正常情况下编辑器中文字的颜色
     */
    public final static Color normalColor=Color.BLACK;
    /**
     * 单引号内的值得颜色
     */
    public final static Color valueColor=Color.BLUE;
    /**
     * 数字的颜色
     */
    public final static Color numberColor=Color.RED;
    /**
     * 注释颜色
     */
    public final static Color commentColor=Color.GREEN;
    /**
     * 待定义
     */
    public final static Color spaceColor=Color.BLACK;
    
    /*
     * sql编辑器的内容元素属性设置
     */
    public final static MutableAttributeSet KEYWORD_SET =new SimpleAttributeSet();

    public final static MutableAttributeSet NORMAL_SET =new SimpleAttributeSet();

    public final static MutableAttributeSet VALUE_SET = new SimpleAttributeSet();

    public final static MutableAttributeSet NUMBER_SET = new SimpleAttributeSet();
    
    public final static MutableAttributeSet COMMENT_SET = new SimpleAttributeSet();
       
    static
    {
        //****************
        StyleConstants.setForeground(KEYWORD_SET, EditorUtil.keywordColor);
        StyleConstants.setBold(KEYWORD_SET, true);

        StyleConstants.setForeground(NORMAL_SET, EditorUtil.normalColor);
        StyleConstants.setBold(NORMAL_SET, false);

        StyleConstants.setForeground(VALUE_SET, EditorUtil.valueColor);
        StyleConstants.setBold(VALUE_SET, false);

        StyleConstants.setForeground(NUMBER_SET, EditorUtil.numberColor);
        StyleConstants.setBold(NUMBER_SET, false);

        StyleConstants.setForeground(COMMENT_SET, EditorUtil.commentColor);
        StyleConstants.setBold(COMMENT_SET, false);     
    }
	/**
	 * 校验是否为sql关键字
	 * @param s
	 * @return
	 */
	public static boolean isKeyWord(String s)
	{
	
		for(int i=0;i<KEYWORDS.length;i++)
		{
			if(KEYWORDS[i].equals(s.toUpperCase()))
				return true;
		}
		return false;
	}
	public static Color getKeyWordColor(String s)
	{
		if(s.indexOf("'")>-1)
			return null;
		for(int i=0;i<10;i++)
		{
			if(s.indexOf(String.valueOf(i))>-1)
				return null;
		}
		return isKeyWord(s) ? keywordColor: normalColor;
	}
	/**
	 * 获取指定位置的文字前景色
	 * @param doc
	 * @param offset
	 * @return
	 */
    public static Color getDocForeground(Document doc,int offset)
	{
    	Element e=getDocElement(doc,offset);
    	AttributeSet set=e.getAttributes().copyAttributes();
        return StyleConstants.getForeground(set); 
	}
    public static Element getDocElement(Document doc,int offset)
    {
    	Element root = doc.getDefaultRootElement();
    	int line = root.getElementIndex(offset); //取offset当前行
    	Element lineElement=root.getElement(line); //取得行元素
    	int index=lineElement.getElementIndex(offset);   //取得行元素的子元素
    	return  lineElement.getElement(index);
    	
    }
    public static int getElementStart(Document doc,int offset,Color c)
	{
    	Element e=getDocElement(doc,offset-1);
    	String str="";
    	try {
			str=doc.getText(e.getStartOffset(),e.getEndOffset()-e.getStartOffset());
		} catch (BadLocationException e1) {
		    return -1;
		}
    	AttributeSet set=e.getAttributes().copyAttributes();
    	Color foreColor=StyleConstants.getForeground(set);
    	if(c==foreColor)
    	{
    		return e.getStartOffset();
    	}
        return offset; 
	}
}
