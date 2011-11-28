/*
 * 创建日期 2006-10-18
 */
package com.coolsql.exportdata;

import com.coolsql.pub.exception.UnifyException;

/**
 * @author liu_xlin 数据导出接口
 */
public interface Exportable {
    /**
     * 导出为文本
     */
    public abstract void exportToTxt() throws UnifyException;

    /**
     * 导出为excel文件
     *  
     */
    public abstract void exportToExcel() throws UnifyException;

    /**
     * 导出为html
     *  
     */
    public abstract void exportToHtml() throws UnifyException;
}
