/*
 * Created on 2007-1-16
 */
package com.coolsql.modifydatabase;

import javax.swing.JComponent;

import com.coolsql.modifydatabase.model.SQLDateSelector;
import com.coolsql.modifydatabase.model.SQLNumberEditor;
import com.coolsql.modifydatabase.model.SQLStringEditor;
import com.coolsql.sql.util.TypesHelper;

/**
 * @author liu_xlin
 *编辑组件常量类，定义了针对数据库更新插入所需要的编辑组件类型
 */
public class EditorFactory {

    /**
     * 文本类型编辑组件
     */
    public static final String EDITORTYPE_TEXT="text";
    
    /**
     * 数字类型
     */
    public static final String EDITORTYPE_NUMBER="number";
    
    /**
     * 日期类型
     */
    public static final String EDITORTYPE_DATE="date";
    /**
     * 时间类型
     */
    public static final String EDITORTYPE_TIME="time";
    
    /**
     * 时间戳类型
     */
    public static final String EDITORTYPE_TIMESTAMP="timestamp";
    
    /**
     * 字节（二进制）类型
     */
    public static final String EDITORTYPE_BIT="bit";
    
    /**
     * 获取编辑组件对象
     * @param editorType  --编辑组件类型
     * @return --根据组件类型,获取相应的组件对象
     */
    public static JComponent getEditorComponent(String editorType,int size,int digits)
    {
        if(editorType==null)
            return new SQLStringEditor(size);
        if(editorType.equals(EDITORTYPE_TEXT))
        {
            return new SQLStringEditor(size);
        }else if(editorType.equals(EDITORTYPE_NUMBER))
        {
            return new SQLNumberEditor(size,digits);
        }else if(editorType.equals(EDITORTYPE_DATE))
//                ||editorType.equals(EDITORTYPE_TIME)
//                ||editorType.equals(EDITORTYPE_TIMESTAMP))
        {
            return new SQLDateSelector(editorType);
        }else
            return new SQLStringEditor(size);
    }
    /**
     * 通过sql类型来获取对应的编辑组件类型
     * @param sqlType  --sql类型
     * @return  --编辑组件类型
     */
    public static String getEditorTypeBySQLType(int sqlType)
    {
        if(TypesHelper.isNumberic(sqlType))
        {
            return EDITORTYPE_NUMBER;
        }else if(TypesHelper.isText(sqlType))
            return EDITORTYPE_TEXT;
        else if(sqlType==TypesHelper.DATE)
            return EDITORTYPE_DATE;
        else if(sqlType==TypesHelper.TIME)
            return EDITORTYPE_TIME;
        else if(sqlType==TypesHelper.TIMESTAMP)
            return EDITORTYPE_TIMESTAMP;
        else if(sqlType==TypesHelper.BLOB)
            return EDITORTYPE_BIT;
        else
            return EDITORTYPE_TEXT;
            
    }
}
