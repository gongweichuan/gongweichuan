/*
 * Created on 2007-2-6
 */
package com.coolsql.pub.display;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;

import com.coolsql.pub.util.StringUtil;
import com.coolsql.view.sqleditor.SyntaxHighlighter;

/**
 * @author liu_xlin
 *文档对象关键字高亮显示监听处理类
 */
public class HighlightDocumentListener implements DocumentListener {

    private boolean isHighlight=true;  //是否高亮文档
    private DefaultStyledDocument document=null;
    
    private SyntaxHighlighter keywordRender;
    public HighlightDocumentListener(DefaultStyledDocument document)
    {
        this.document=document;
        keywordRender=new SyntaxHighlighter(document);
        keywordRender.setName(StringUtil.getHashNameOfObject(document));
        SyntaxHighlighter.addThread(document,keywordRender);
    }
    /* (non-Javadoc)
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
     */
    public void changedUpdate(DocumentEvent e) {
    }

    /* (non-Javadoc)
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
     */
    public void insertUpdate(DocumentEvent e) {
        if(!isHighlight)
            return;
            
        int offset=e.getOffset();   //插入的起始位置
        int end=offset+e.getLength();  //插入内容的尾部偏移量
        Element element=getLineElement(offset);  //获取起始位置所在的行元素       
        int elementEnd=-1;
        int elementStart=-1;
        if(element!=null)
        {
            elementStart=element.getStartOffset(); //行元素的起始偏移
            elementEnd=element.getEndOffset();  //行元素的尾部偏移
        }
        
        int txtLength=0;
        if(end<elementEnd)  //插入在一行中进行
        {
            if(elementStart>=end)
                return;
            txtLength=elementEnd-elementStart;
        }else   //在多行中修改
        {
            txtLength=end-elementStart;
        }
        try {

            keywordRender.updateText(document.getText(elementStart, txtLength), elementStart);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
     */
    public void removeUpdate(DocumentEvent e) {
        if(!isHighlight)
            return;
        
        int offset=e.getOffset()-e.getLength();   //删除后的光标位置
        Element element=getLineElement(offset);  //获取起始位置所在的行元素       
        int elementEnd=-1;
        int elementStart=-1;
        if(element!=null)
        {
            elementStart=element.getStartOffset(); //行元素的起始偏移
            elementEnd=element.getEndOffset();  //行元素的尾部偏移
        }
        if(elementStart==elementEnd)
            return;
        int txtLength=elementEnd-elementStart;
        try {
            keywordRender.updateText(document.getText(elementStart,txtLength), elementStart);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }
    /**
     * 获取指定位置所在的行元素
     * 
     * @param offset
     * @return
     */
    private Element getLineElement(int offset) {
        if (offset > document.getLength() - 1)
            return null;
        Element root = document.getDefaultRootElement();
        int line = root.getElementIndex(offset);
        Element lineElement = root.getElement(line);
        return lineElement;
    }
    /**
     * @return Returns the isHighlight.
     */
    public boolean isHighlight() {
        return isHighlight;
    }
    /**
     * @param isHighlight The isHighlight to set.
     */
    public void setHighlight(boolean isHighlight) {
        this.isHighlight = isHighlight;
    }
}
