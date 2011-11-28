/*
 * Created on 2007-1-17
 */
package com.coolsql.modifydatabase.model;

import com.coolsql.modifydatabase.DataHolder;
import com.coolsql.modifydatabase.EditorLimiter;
import com.coolsql.pub.component.StringEditor;

/**
 * @author liu_xlin
 *继承文本编辑组件，实现了数据持有接口，便于获取编辑组件的值
 */
public class SQLStringEditor extends StringEditor implements DataHolder,EditorLimiter {

    public SQLStringEditor() {
        super();
    }

    public SQLStringEditor(int MaxLen) {
        super(MaxLen);
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.DataHolder#getHolderValue()
     */
    public Object getHolderValue() {
        return getText();
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.DataHolder#setValue()
     */
    public void setValue(Object value) {
        setText(value==null?"":value.toString());       
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.EditorLimiter#setSize(int)
     */
    public void setSize(int size) {
        setMaxLength(size);
    }

    /*不做任何处理
     * @see com.coolsql.modifydatabase.EditorLimiter#setDigits(int)
     */
    public void setDigits(int digits) {
        
    }

}
