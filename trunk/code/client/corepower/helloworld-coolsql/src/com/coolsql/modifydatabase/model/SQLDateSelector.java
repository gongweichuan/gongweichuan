/*
 * Created on 2007-1-17
 */
package com.coolsql.modifydatabase.model;

import java.text.ParseException;
import java.util.Date;

import com.coolsql.modifydatabase.DataHolder;
import com.coolsql.modifydatabase.EditorFactory;
import com.coolsql.pub.component.DateSelector;

/**
 * @author liu_xlin
 *日期选择组件的继承，便于作为编辑组件时，获取当前组件所选中的日期值
 */
public class SQLDateSelector extends DateSelector implements DataHolder {

    private String addtionalPart;
    public SQLDateSelector(String editorType)
    {
        super(STYLE_DATE1);
        setStyleByType(editorType);
    }
    /**
     * 设置日期值（1900-01-01）后面的格式
     * @param editorType --对于时间，日期，和时间戳三种类型返回不同的时间格式
     */
    private void setStyleByType(String editorType)
    {
        if(editorType==null||editorType.equals(EditorFactory.EDITORTYPE_DATE))
             addtionalPart="";
        else if(editorType.equals(EditorFactory.EDITORTYPE_TIME))
        {
            addtionalPart=" 00:00:00";
        }else if(editorType.equals(EditorFactory.EDITORTYPE_TIMESTAMP))  //暂时只适用于DB2数据库
        {
            addtionalPart=" 00:00:00 000000";
        }else 
            addtionalPart="";
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.DataHolder#getHolderValue()
     */
    public Object getHolderValue() {
        String currentDate=getSelectedItem().toString();
        
        return currentDate+addtionalPart;
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.DataHolder#setValue(java.lang.Object)
     */
    public void setValue(Object value) {
        if(value==null)
            try {
                setSelectedDate(new Date());
            } catch (ParseException e) {
                setSelectedItem("1900-01-01");
            }
        setSelectedItem(value==null?"":value.toString());
    }

}
