/*
 * 创建日期 2006-9-1
 *
 * 
 */
package com.coolsql.exportdata.excel;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.coolsql.sql.util.TypesHelper;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 结果集处理公共类
 */
public class ResultUtil {

    /**
     * 检查结果集中是否存在Lob字段。
     * 
     * @param set
     * @return 0:非lob字段 1:大对象（包含了clob,blob）2:暂不用
     * @throws SQLException
     */
    private static int checkLobOfResult(ResultSet set) throws SQLException {
        ResultSetMetaData metaData = set.getMetaData();
        int colCount = metaData.getColumnCount();
        for (int i = 0; i < colCount; i++) {
            int type = metaData.getColumnType(i + 1);
            if (TypesHelper.isLob(type)) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 获取结果集的总记录数 
     * 
     * @param set
     * @return 如果出现错误，那么返回-1
     */
    public static int countResultRow(ResultSet set) {
        int count = -1;
        try {
            if (checkLobOfResult(set) == 1) //如果结果集中存在大对象字段，返回-1
                return -1;

            set.last();
            count = set.getRow();
            set.beforeFirst();
        } catch (Throwable e) {
            LogProxy.outputErrorLog(e);
            count = -1;
        }
        return count;
    }

    /**
     * 通过结果集，获取Excel相关设置
     * 
     * @param set
     * @return
     * @throws ExcelProcessException
     */
    public static ExcelComponentSet getExcelSet(ResultSet set)
            throws ExcelProcessException {
        CellDefined[] defined = CellDefined.createInstanceOfResult(set);
        ExcelComponentSet setting = new ExcelComponentSet();
        setting.setDisplayHead(true);
        setting.setHeadDefined(defined);
        return setting;
    }
}
