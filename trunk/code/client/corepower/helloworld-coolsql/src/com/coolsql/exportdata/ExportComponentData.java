/*
 * 创建日期 2006-10-19
 */
package com.coolsql.exportdata;

import javax.swing.JComponent;

import com.coolsql.pub.exception.UnifyException;

/**
 * @author liu_xlin 导出组件数据基类
 */
public class ExportComponentData extends ExportData {

    /**
     * @param source
     */
    public ExportComponentData(JComponent source) {
        super(source);
    }

    /**
     * 定义接口中的方法，将该类作为适配器使用
     */
    public void exportToTxt() throws UnifyException {
    }

    public void exportToExcel() throws UnifyException {
    }

    public void exportToHtml() throws UnifyException {
    };
}
