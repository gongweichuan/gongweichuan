/*
 * Created on 2007-3-7
 */
package com.coolsql.modifydatabase.insert;

/**
 * @author liu_xlin
 *表控件单元格元素值的校验接口
 */
public interface CellValidation {

    /**
     * 校验单元格对象值的有效性
     * @param value  --单元格对象值
     * @param row  --单元格对应的行索引
     * @param column  --单元格对应的列索引
     * @return  --如果校验成功，返回true，不成功（可能）返回false
     */
    public boolean checkValidation(Object value,int row,int column);
}
