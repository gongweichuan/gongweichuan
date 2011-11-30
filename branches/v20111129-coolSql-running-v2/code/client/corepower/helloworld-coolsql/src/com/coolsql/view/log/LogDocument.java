/*
 * 创建日期 2006-7-13
 *
 */
package com.coolsql.view.log;

import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.util.FileUtil;
import com.coolsql.system.PropertyConstant;
import com.coolsql.system.Setting;
import com.coolsql.system.SystemConstant;

/**
 * @author liu_xlin 日志文档模型
 */
public class LogDocument extends DefaultStyledDocument {
	
	public static final long DEFAULT_MAXLENGTH=80000;
	
	private static final long serialVersionUID = 1L;
	private MutableAttributeSet myAttributeSet = null;
    private static LogDocument log=null;
    
    private long maxDocumentLength=DEFAULT_MAXLENGTH;
	public static LogDocument getInstance()
	{
	    if(log==null)
	        log=new LogDocument();
	    return log;
	}
	private LogDocument() {
		super();
		myAttributeSet = new SimpleAttributeSet();
		addDocumentListener(new DocumentListener()
		{

			public void changedUpdate(DocumentEvent e) {
				
			}

			public void insertUpdate(DocumentEvent e) {
				checkDocumentLength();
			}

			public void removeUpdate(DocumentEvent e) {		
				checkDocumentLength();
			}
			
		}
		);
		String txtValue=Setting.getInstance().getProperty(PropertyConstant.PROPERTY_VIEW_LOG_MAXLOGTEXT, ""+DEFAULT_MAXLENGTH);
		try
		{
			maxDocumentLength=Long.parseLong(txtValue);
		}catch(Exception e)
		{
			maxDocumentLength=DEFAULT_MAXLENGTH;
		}
		Setting.getInstance().addPropertyChangeListener(new PropertyChangeListener()
		{

			public void propertyChange(PropertyChangeEvent evt) {
				String txt=(String)evt.getNewValue();
				try
				{
					maxDocumentLength=Long.parseLong(txt);
				}catch(Exception e)
				{
					maxDocumentLength=DEFAULT_MAXLENGTH;
				}
			}
			
		}
		, PropertyConstant.PROPERTY_VIEW_LOG_MAXLOGTEXT);
	}
	public boolean isNeedClear()
	{
		if(getLength()>=maxDocumentLength)
			return true;
		else
			return false;
	}
	public void checkDocumentLength()
	{
		if(isNeedClear())
		{
			GUIUtil.processOnSwingEventThread(new Runnable()
			{
				public void run()
				{
					try {
						if(Setting.getInstance().getBoolProperty(PropertyConstant.PROPERTY_VIEW_LOG_ISSAVETOFILE,false))
						{
							FileUtil.writeSimpleTextToFile(getText(0, getLength()), 
									new File(Setting.getInstance().getProperty(PropertyConstant.PROPERTY_VIEW_LOG_FILEPATH, SystemConstant.LOGVIEW_LOGFILE)));
						}
						remove(0, getLength());
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
			}
			);
			
		}
	}
	/**
	 * 设置字体颜色
	 * 
	 * @param c
	 */
	public void setColor(Color c) {
		StyleConstants.setForeground(myAttributeSet, c);

	}
	/**
	 * 设置字体
	 * 
	 * @param family
	 */
	public void setFontFamily(String family) {
		StyleConstants.setFontFamily(myAttributeSet, family);
	}
    /**
     * 设置字体大小
     * @param size
     */
	public void setFontSize(int size) {
		StyleConstants.setFontSize(myAttributeSet, size);
	}
    /**
     * 设置字体风格,参数含义和Font类定义的一样
     * @param style
     */
	public void setFontStyle(int style) {
		if (style == 0 || style == 2)
			StyleConstants.setBold(myAttributeSet, false);
		else if (style == 1 || style == 3) {
			StyleConstants.setBold(myAttributeSet, true);
		}
		if (style == 0 || style == 1)
			StyleConstants.setItalic(myAttributeSet, false);
		else if (style == 2 || style == 3) {
			StyleConstants.setItalic(myAttributeSet, true);
		}
	}

	/**
	 * 设置字体整体风格
	 * 
	 * @param f
	 */
	public void setFont(Font f) {
		setFontFamily(f.getFamily());
		setFontSize(f.getSize());
		setFontStyle(f.getStyle());
	}
	/**
	 * 插入字符串信息
	 * @param ob
	 * @throws BadLocationException
	 */
	public void insertString(Object ob) throws BadLocationException
	{
		String con=ob+"\n";
		super.insertString(this.getLength(),con,myAttributeSet);
	}
	/**
	 * 渲染一段文字的风格
	 * @param start
	 * @param end
	 */
    protected void render(int start,int end)
    {
    	this.setCharacterAttributes(start,end-start,myAttributeSet,true);
    }
	/**
	 * 将风格属性应用到文档模型中
	 *  
	 */
	public void validateSet() {
		setParagraphAttributes(0, 0, myAttributeSet, true);
	}
}
