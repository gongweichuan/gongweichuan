package com.coolsql.sql;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.sql.model.Entity;
import com.coolsql.system.PropertyConstant;
import com.coolsql.system.Setting;

public class SQLStandardResultSetResults extends SQLResultSetResults {

    protected SQLStandardResultSetResults(Bookmark bookmark, String query,
            Entity entity[], int numberOfRowsPerPage) {
        super(query, bookmark, entity);
        hasMore = false;
        start = 1;
        totalNumberOfRows = -1;
        fullMode = false;
        this.numberOfRowsPerPage = numberOfRowsPerPage;
        
        Setting.getInstance().addPropertyChangeListener(new PropertyChangeListener()
        {

			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getNewValue()==null)
					return;
				setMaxColumnWidth(Integer.parseInt(evt.getNewValue().toString()));
			}
        	
        }
        , PropertyConstant.PROPERTY_VIEW_RESULTSET_MAXCOLUMNWIDTH);
        
    }

    static SQLResultSetResults create(ResultSet set, Bookmark bookmark,
            String query, Entity[] entity, int numberOfRows) throws SQLException {
        SQLStandardResultSetResults results = new SQLStandardResultSetResults(
                bookmark, query, entity, numberOfRows);
        results.parseResultSet(set);
        if(entity==null)
            results.setEntitys(getEntitiesInResult(set,bookmark));
        return results;
    }

    /**
     * 对于数据库中每个字段的值，只取前2048个字节
     */
    protected void parseResultSet(ResultSet set) throws SQLException {
        int rowCount = 1;
        ResultSetMetaData metaData = set.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        ResultSetReader reader=new ResultSetReader(set);
        reader.setMaxColumnWith(maxColumnWidth);
        /**
         * 获取结果集中的所有列信息
         */
        List<SQLResultSetResults.Column> columns = new ArrayList<SQLResultSetResults.Column>();
        for (int i = 1; i <= columnCount; i++)
            columns.add(new SQLResultSetResults.Column(metaData
                    .getColumnName(i), metaData.getColumnTypeName(i), metaData
                    .getColumnDisplaySize(i),metaData.getColumnType(i),metaData.isAutoIncrement(i)));

        setColumns((SQLResultSetResults.Column[]) columns
                .toArray(new SQLResultSetResults.Column[columns.size()]));
        
        
        boolean exitEarly = false;
        int firstRow = fullMode ? 0 : start;
        int lastRow = fullMode ? Integer.MAX_VALUE : (start + numberOfRowsPerPage) - 1;
        List<SQLResultSetResults.Row> rowList = new ArrayList<SQLResultSetResults.Row>();
        while (true) {
        	Object[] rowData=reader.readRow();
        	if (rowData == null)
				break;
        	
            boolean disable = start < 1 || lastRow < 1;
            if (disable || rowCount >= firstRow && rowCount <= lastRow) {
//                List row = new ArrayList();
//                int i = 1;
                
//                /**
//                 * Parse row data
//                 */
//                for (int length = columns.size(); i <= length; i++) {
//                    Object value = null;
//                    row.add(value != null && !set.wasNull() ? ((Object) (value))
//                                    : "<NULL>");
//                }

                //将解析的行数据对象(SQLResultSetResults.Row)添加入结果集合中
                rowList.add(new SQLResultSetResults.Row(rowData));
            }
            rowCount++;
            
            //如果超出最后数据位置，退出数据解析
            if (!disable && rowCount > lastRow) {
                exitEarly = true;
                break;
            }
        }
        if (exitEarly) {  //如果提早退出结果集的数据解析
            hasMore = set.next();
        } else {   //完成结果集的解析，那么计算结果集的总数量
            totalNumberOfRows = Math.max(0, rowCount - 1);
            hasMore = false;
        }
        
        //将解析后的结果数据保存
        setRows((SQLResultSetResults.Row[]) rowList
                .toArray(new SQLResultSetResults.Row[rowList.size()]));
    }


    public int getTotalNumberOfRows() {
        return totalNumberOfRows;
    }

    public void nextPage(Connection connection) throws SQLException {
        if (hasNextPage()) {
            start += numberOfRowsPerPage;
            maxSizeOfResultset=start+numberOfRowsPerPage;
            super.refresh(connection);
        }
    }

    public void previousPage(Connection connection) throws SQLException {
        if (hasPreviousPage()) {
            start = Math.max(1, start - numberOfRowsPerPage);
            maxSizeOfResultset=start+numberOfRowsPerPage;
            super.refresh(connection);
        }
    }
    public void refresh(Connection connection) throws SQLException{
    	maxSizeOfResultset=start+numberOfRowsPerPage;
    	super.refresh(connection);
	}
    /**
     * return max size of next query
     */
    protected int getMaxSizeOfResultset(){
    	return fullMode ? 0 : maxSizeOfResultset;
    }
    public boolean hasNextPage() {
        return hasMore;
    }

    public boolean hasPreviousPage() {
        return start > 1;
    }

    public void setFullMode(boolean fullMode) {
        this.fullMode = fullMode;
    }

    public boolean isFullMode() {
        return fullMode;
    }

    public int getStart() {
        return getRowCount() != 0 ? fullMode ? 1 : start : 0;
    }

    public int getEnd() {
        return fullMode ? getRowCount() : (start + getRowCount()) - 1;
    }

    public int getLast() {
        return totalNumberOfRows;
    }

    public void setFilterSort(FilterSort filterSort) {
        super.setFilterSort(filterSort);
        start = 1;
    }
    public int getMaxColumnWidth()
    {
    	return maxColumnWidth;
    }
    public void setMaxColumnWidth(int w)
    {
    	this.maxColumnWidth=w;
    }
    
    private int maxColumnWidth=Setting.getInstance().getIntProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_MAXCOLUMNWIDTH, 2048);
    /**
     * 结果集中是否仍然存在没有解析的数据
     */
    private boolean hasMore;


    /**
     * 每页的纪录数量
     */
    private int numberOfRowsPerPage;

    /**
     * 查询结果集的总纪录数
     */
    private int totalNumberOfRows;

    /**
     * 是否展示结果集的所有数据
     */
    private boolean fullMode;
    
    /**
     * max size that current querying may fetch from database.
     */
    protected int maxSizeOfResultset=0;
}
