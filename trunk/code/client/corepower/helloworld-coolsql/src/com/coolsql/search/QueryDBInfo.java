/*
 * 创建日期 2006-9-19
 */
package com.coolsql.search;

import java.sql.SQLException;
import java.util.List;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.display.TableCellObject;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.sql.model.Column;
import com.coolsql.sql.model.Entity;
import com.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 该类用来数据库的实体和列信息
 */
public class QueryDBInfo {

    private SearchInfoDialog visual = null;

    /**
     * 查询结果展示窗口
     */
    private SearchResultFrame resultFrame = null;

    public QueryDBInfo(SearchInfoDialog v) {
        visual = v;
    }

    /**
     * 设置查询结果窗口
     * 
     * @param frame
     */
    public void setResultFrame(SearchResultFrame frame) {
        resultFrame = frame;
    }

    /**
     * 查询信息
     *  
     */
    public void query() {
        //首先校验书签的有效性
        Bookmark bookmark = visual.getSelectBookmark();
        if (bookmark == null)
            return;

        int type = visual.getQueryType();
        if (type == 0) // 实体查询
        {
            resultFrame.setProcessInfo(PublicResource
                    .getSQLString("searchinfo.result.processinfo.getfromdb"));//提示正在获取数据
            Entity[] result;
            try {
                result = queryEntityInfo();
            } catch (UnifyException e) {
                resultFrame.closeFrame();
                LogProxy.errorReport(resultFrame, e);
                return;
            } catch (SQLException e) {
                resultFrame.closeFrame();
                LogProxy.SQLErrorReport(resultFrame, e);
                return;
            }
            if (result == null || resultFrame == null) //如果没有结果，或者没有展示窗口，则不作处理
                return;
            resultFrame.setCount(result.length);
            Object[] rowData = null;

            resultFrame.setProcessInfo(PublicResource
                    .getSQLString("searchinfo.result.processinfo.adddata")); //提示正在加载
            for (int i = 0; i < result.length; i++) {
                rowData = new Object[4];
                String entityType = result[i].getType();
                
                rowData[0] = new TableCellObject(
                        result[i].getName(),
                        BookMarkPubInfo.getTableTypeIcon(entityType));
                rowData[1] = result[i].getCatalog();
                rowData[2] = result[i].getSchema();
                rowData[3] = result[i].getType();
                resultFrame.addRow(rowData); //添加一行数据
            }
            resultFrame.setProcessInfo("");
            resultFrame.adjustGUI();
        } else if (type == 1) //列查询
        {
            resultFrame.setProcessInfo(PublicResource
                    .getSQLString("searchinfo.result.processinfo.getfromdb"));
            Column[] result;
            try {
                result = queryColumnInfo();
            } catch (SQLException e) {
                resultFrame.closeFrame();
                LogProxy.SQLErrorReport(resultFrame, e);
                return;
            } catch (UnifyException e) {
                resultFrame.closeFrame();
                LogProxy.errorReport(resultFrame, e);
                return;
            }
            if (result == null || resultFrame == null)
                return;
            resultFrame.setCount(result.length);
            Object[] rowData = null;

            resultFrame.setProcessInfo(PublicResource
                    .getSQLString("searchinfo.result.processinfo.adddata"));

            for (int i = 0; i < result.length; i++) {
                rowData = new Object[5];
                rowData[0] = new TableCellObject(
                        result[i].getName(),
                        BookMarkPubInfo.getIconList()[BookMarkPubInfo.NODE_COLUMN]); //列名
                rowData[1] = result[i].getParentEntity().getName(); //所属实体名
                rowData[2] = result[i].getParentEntity().getType(); //实体类型
                rowData[3] = result[i].getParentEntity().getSchema(); //所属模式
                rowData[4] = result[i].getParentEntity().getCatalog(); //所属模式
                resultFrame.addRow(rowData); //添加一行数据
            }
            resultFrame.setProcessInfo("");
            resultFrame.adjustGUI();
        }
    }

    /**
     * To query entity information match sepecified condition 
     */
    public Entity[] queryEntityInfo() throws UnifyException, SQLException {
        Bookmark bookmark = visual.getSelectBookmark();
        if (bookmark == null)
            return null;

        String catalogName= visual.getQueryCatalog();
        String schemaName = visual.getQuerySchema();

        String entityName = visual.getQueryEntity(); //实体

        if(bookmark.getDbInfoProvider().getDatabaseMetaData().storesLowerCaseIdentifiers())
        {
        	if(catalogName!=null)
        		catalogName = catalogName.toLowerCase();
        	if(schemaName!=null)
        		schemaName = schemaName.toLowerCase();
        	if(entityName!=null)
        		entityName = entityName.toLowerCase();
        }else if(bookmark.getDbInfoProvider().getDatabaseMetaData().storesUpperCaseIdentifiers())
        {
        	if(catalogName!=null)
        		catalogName = catalogName.toUpperCase();
        	if(schemaName!=null)
        		schemaName = schemaName.toUpperCase();
        	if(entityName!=null)
        		entityName = entityName.toUpperCase();
        }
//        如果不是mysql数据库，将查询条件的内容调整为大写
//        if (catalogName != null&&!(bookmark.isMysql()))
//        	catalogName = catalogName.toUpperCase();
//        if (schemaName != null&&!(bookmark.isMysql()))
//            schemaName = schemaName.toUpperCase();
//        if (entityName != null&&!(bookmark.isMysql()))
//            entityName = entityName.toUpperCase();
        return queryEntityInfo(bookmark,catalogName, schemaName, entityName);
    }
    /**
     * 根据指定的实体路径进行实体信息的查询
     * 
     * @param bookmark
     * @param schemaName
     * @param entityName
     * @return
     * @throws SQLException
     * @throws UnifyException
     */
    public static Entity[] queryEntityInfo(Bookmark bookmark,String catalog,
            String schemaName, String entityName) throws UnifyException,
            SQLException {

        List list = bookmark.getDbInfoProvider().queryEntities(bookmark,
                bookmark.getConnection(),catalog, schemaName, entityName, null);
        return (Entity[]) list.toArray(new Entity[list.size()]);
    }

    /**
     * 查询列信息
     * 
     * @return
     * @throws UnifyException
     * @throws SQLException
     */
    public Column[] queryColumnInfo() throws SQLException, UnifyException {
        Bookmark bookmark = visual.getSelectBookmark();
        if (bookmark == null)
            return null;

        String catalogName= visual.getQueryCatalog();
        String schemaName = visual.getQuerySchema(); //模式名

        String entityName = visual.getQueryEntity(); //实体

        String columnName = visual.getQueryColumn(); //列名

        if (catalogName != null&&!(bookmark.isMysql()))
        	catalogName = catalogName.toUpperCase();
        if (schemaName != null&&!(bookmark.isMysql()))
            schemaName = schemaName.toUpperCase();
        if (entityName != null&&!(bookmark.isMysql()))
            entityName = entityName.toUpperCase();
        if (columnName != null&&!(bookmark.isMysql()))
            columnName = columnName.toUpperCase();
        return queryColumnInfo(bookmark,catalogName, schemaName, entityName, columnName);
    }

    /**
     * 查询列信息
     * 
     * @return
     * @throws UnifyException
     * @throws SQLException
     */
    public static Column[] queryColumnInfo(Bookmark bookmark,String catalogName,
            String schemaName, String entityName, String columnName)
            throws SQLException, UnifyException {
        List list = bookmark.getDbInfoProvider().queryColumns(bookmark,
                bookmark.getConnection(),catalogName, schemaName, entityName, columnName);
        return (Column[]) list.toArray(new Column[list.size()]); //转换为列数组类型

    }

    /**
     * 查询列的详细信息
     * 
     * @param bookmark
     * @param schemaName
     * @param entityName
     * @param columnName
     * @return
     * @throws SQLException
     * @throws UnifyException
     */
    public static Column queryColumnDetail(Bookmark bookmark,String catalogName,
            String schemaName, String entityName, String columnName)
            throws SQLException, UnifyException {
        Column column = bookmark.getDbInfoProvider().queryColumnDetail(bookmark,
                bookmark.getConnection(),catalogName, schemaName, entityName, columnName);
        return column;
    }
}
