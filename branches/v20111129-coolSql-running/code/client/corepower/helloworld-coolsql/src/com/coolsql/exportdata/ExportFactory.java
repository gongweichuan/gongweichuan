/*
 * 创建日期 2006-10-19
 */
package com.coolsql.exportdata;

import javax.swing.JTable;
import javax.swing.text.JTextComponent;

import com.coolsql.bookmarkBean.Bookmark;

/**
 * @author liu_xlin
 *  
 */
public class ExportFactory {

    /**
     * 创建文本控件的导出器
     * 
     * @param source
     * @return
     */
    public static ExportData createExportForTextComponent(JTextComponent source) {
        return new ExportFromTextComponent(source);
    }
    /**
     * 导出table控件数据
     * @param table --JTable类型控件
     * @return
     */
    public static ExportData createExportForTable(JTable table)
    {
        return new ExportDataFromTable(table);
    }
    /**
     * 创建通过sql执行得到的结果集进行数据导出的实例类
     * @param bookmark 书签
     * @param sql  sql语句
     * @return
     */
    public static ExportData createExportForSql(Bookmark bookmark,String sql)
    {
        return new ExportDataFromSql(sql,bookmark);
    }
}
