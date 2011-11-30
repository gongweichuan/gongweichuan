/*
 * Created on 2007-1-12
 */
package com.coolsql.modifydatabase.model;

import java.awt.Color;

import com.coolsql.modifydatabase.EditorFactory;
import com.coolsql.modifydatabase.TableCell;

/**
 * @author liu_xlin
 *针对插入(insert)和更新（update）两类不同的sql生成，其中每列所对应的表格元素特点不一样，为此定义一个基础类型。
 */
public abstract class BaseTableCell implements TableCell {

    /**
     * 是否可编辑变量
     */
    private boolean isEditable;
    /**
     * 前景色颜色
     */
    private Color foregroundColor=null;
    
    /**
     * 被景色颜色
     */
    private Color backgroundColor=null;
    
    /**
     * 是否作为更新条件的标志字段
     */
    private boolean isAsTerm;
    
    private boolean isNull;
    public BaseTableCell()
    {
        this(false,null,null,false,false);
    }
    public BaseTableCell(boolean isEditable,boolean isAsTerm,boolean isNull)
    {
        this(isEditable,null,null,isAsTerm,isNull);
    }
    public BaseTableCell(boolean isEditable,Color foreColor,Color backColor,boolean isAsTerm,boolean isNull)
    {
        this.isEditable=isEditable;
        foregroundColor=foreColor;
        backgroundColor=backColor;
        this.isAsTerm=isAsTerm;
        this.isNull=isNull;
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#isEditable()
     */
    public boolean isEditable() {
        return isEditable;
    }

    /* 默认为文本类型
     * @see com.coolsql.modifydatabase.TableCell#getEditor()
     */
    public String getEditorType() {
        return EditorFactory.EDITORTYPE_TEXT;
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#isNeedModify()
     */
    public boolean isNeedModify() {
        return false;
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#getRenderOfForeground()
     */
    public Color getRenderOfForeground() {
        return foregroundColor;
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#getRenderOfBackground()
     */
    public Color getRenderOfBackground() {
        return backgroundColor;
    }

    /**
     * 实现风格文本，默认与方法：toString()的返回值一致
     */
    public String getStyleString()
    {
        return toString();
    }
    /**
     * 使列名字段能够灵活的定制字段是否作为更新条件
     * 默认不作为更新条件
     */
    public boolean isAsTerm()
    {
        return isAsTerm;
    }
    /**
     * 
     */
    public boolean isNullValue()
    {
        return isNull;
    }
    public void setIsNullValue(boolean isNull)
    {
        this.isNull=isNull;
    }
    /**
     * @param isAsTerm The isAsTerm to set.
     */
    public void setAsTerm(boolean isAsTerm) {
        this.isAsTerm = isAsTerm;
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#getToolTip()
     */
    public String getToolTip() {
        return null;
    }
    public String toString()
    {
        return getValue().toString();
    }
    /**
     * @param backgroundColor The backgroundColor to set.
     */
    public void setRenderOfBackground(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    /**
     * @param foregroundColor The foregroundColor to set.
     */
    public void setRenderOfForeground(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }
    /**
     * @param isEditable The isEditable to set.
     */
    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }
}
