/*
 * Created on 2007-1-17
 */
package com.coolsql.modifydatabase.model;

import com.coolsql.modifydatabase.DataHolder;
import com.coolsql.modifydatabase.EditorLimiter;
import com.coolsql.pub.component.NumberEditor;

/**
 * @author liu_xlin
 *继承数字编辑组件类，实现该组件的编辑值的返回
 */
public class SQLNumberEditor extends NumberEditor implements DataHolder,EditorLimiter {

    public SQLNumberEditor() {
        super();
    }

    public SQLNumberEditor(boolean addAction) {
        super(addAction);
    }

    public SQLNumberEditor(int intPartLen) {
        super(intPartLen);
    }

    public SQLNumberEditor(int intPartLen, boolean addAction) {
        super(intPartLen,addAction);
    }

    public SQLNumberEditor(int maxLen, int decLen) {
        super(maxLen, decLen);
    }

    public SQLNumberEditor(int maxLen, int decLen, boolean addAction) {
        super(maxLen,decLen,addAction);
    }

    public SQLNumberEditor(int maxLen, int decLen, double minRange,
            double maxRange, boolean addAction) {
        super(maxLen,decLen,minRange,maxRange,addAction);
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.DataHolder#getHolderValue()
     */
    public Object getHolderValue() {
        
        return getText();
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.DataHolder#setValue(java.lang.Object)
     */
    public void setValue(Object value) {
        setText(value==null?"0":value.toString());
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.EditorLimiter#setSize(int)
     */
    public void setSize(int size) {
        setMaxLength(size);
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.EditorLimiter#setDigits(int)
     */
    public void setDigits(int digits) {
        setDecLength(digits);
    }

}
