/*
 * Created on 2007-1-26
 */
package com.coolsql.modifydatabase.insert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.coolsql.pub.display.ClipboardUtil;

/**
 * @author liu_xlin 自定义可编辑表模型
 */
public class EditorTableModel extends AbstractTableModel {

    /**
     * 列名集合
     */
    private List columnList = null;

    //行数据集合对象
    private List dataList = null;

    /**
     * 更新单元格数据时，对数据格式的正确性进行校验
     * true:需要检验
     * false:不进行校验处理
     */
    private boolean isValueCheck=true;

    public EditorTableModel()
    {
        this(new Object[0],0);
    }
    public EditorTableModel(List columnNames, int rowCount) {
        setDataList(newList(rowCount), columnNames);
    }
    public EditorTableModel(Object[] columnNames, int rowCount) {
        setDataList(newList(rowCount), convertToList(columnNames));
    }
    public EditorTableModel(List dataList, List columnList) {
        setDataList(dataList, columnList);
    }

    public EditorTableModel(Object[][] dataList, Object[] columnList) {
        setDataList(convertToList(dataList), convertToList(columnList));
    }

    private static List nonNullList(List v) {
        return (v != null) ? v : new ArrayList();
    }

    private List newList(int rows)
    {
        return convertToList(new Object[rows]);
    }
    /**
     * 将数据对象转化成集合（List）对象
     * 
     * @param object
     *            --需要被转化的数组对象
     * @return --由数组转化来的List对象
     */
    private List convertToList(Object[] object) {
        if (object == null)
            return null;
        List list = Arrays.asList(object);
        return list;
    }

    /**
     * 将二维数组转化为List对象
     * 
     * @param object
     *            --需要被转化的二维数组
     * @return
     */
    private List convertToList(Object[][] object) {
        if (object == null)
            return null;

        List list = new ArrayList();
        for (int i = 0; i < object.length; i++) {
            list.add(convertToList(object[i]));
        }
        return list;
    }

    /**
     * 更新表结构的数据，包括行数据和列信息
     * 
     * @param dataList
     *            --行数据集合对象
     * @param columnList
     *            --列信息集合对象
     */
    public void setDataList(List dataList, List columnList) {
        this.dataList = nonNullList(dataList);
        this.columnList = nonNullList(columnList);
        justifyRows(0, getRowCount());
        fireTableStructureChanged();
    }

    //************************************************
    public void addRow(List rowList) {
        insertRow(getRowCount(), rowList);
    }

    public void addRow(Object[] rowList) {
        insertRow(getRowCount(), convertToList(rowList));
    }
    public void addRows(Object[][] rowList) {
        insertRows(getRowCount(), convertToList(rowList));
    }
    public void addRows(List rowList) {
        insertRows(getRowCount(), rowList);
    }
    /**
     * Removes the row at <code>row</code> from the model. Notification of the
     * row being removed will be sent to all the listeners.
     * 
     * @param row
     *            the row index of the row to be removed
     * @exception ArrayIndexOutOfBoundsException
     *                if the row was invalid
     */
    public void removeRow(int row) {
        dataList.remove(row);
        fireTableRowsDeleted(row, row);
    }
    /**
     * 设置列的数量
     * 
     * @param columnCount
     *            --新的列数
     */
    public void setColumnCount(int columnCount) {
        int old = getColumnCount();
        if (old == columnCount)
            return;

        if (old >= columnCount) {
            columnList = columnList.subList(0, columnCount);
        } else
            columnList.addAll(Arrays.asList(new Object[columnCount - old]));

        justifyRows(0, getRowCount());
        fireTableStructureChanged();
    }

    /**
     * 向表模型添加一列
     * 
     * @param columnName
     *            --新增的列对象
     * @param columnData
     *            --新增列所对应的数据
     */
    public void addColumn(Object columnName, List columnData) {
        columnList.add(columnName);
        if (columnData != null) {
            int columnSize = columnData.size();
            if (columnSize > getRowCount()) { //如果给定行数据的列数超出定义长度，将多余尾数据删除
                columnData = columnData.subList(0, getRowCount());
            }
            justifyRows(0, getRowCount());
            int newColumn = getColumnCount() - 1;
            for (int i = 0; i < columnData.size(); i++) {
                List row = (List) dataList.get(i);
                row.set(newColumn, columnData.get(i));
            }
        } else {
            justifyRows(0, getRowCount());
        }

        fireTableStructureChanged();
    }

    public void addColumn(Object columnName, Object[] columnData) {
        addColumn(columnName, convertToList(columnData));
    }
    public void addColumn(Object columnName) {
        addColumn(columnName, (List)null);
    }
    /**
     * 获取指定的列所对应的列名
     */
    public String getColumnName(int column) {
        Object id = null;
        // This test is to cover the case when
        // getColumnCount has been subclassed by mistake ...
        if (column < columnList.size()) {
            id = columnList.get(column);
        }
        return (id == null) ? super.getColumnName(column) : id.toString();
    }

    public boolean isCellEditable(int row, int column) {
        return true;
    }

    /**
     * 设置列集合
     * @param columnList  --新的列信息集合
     */
    public void setColumnIdentifiers(List columnList) {
        setDataList(dataList, columnList);
    }
    public void setColumnIdentifiers(Object[] newIdentifiers) {
        setColumnIdentifiers(convertToList(newIdentifiers));
    }
    //*************************************************

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return columnList.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return dataList.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        List rowList = (List) dataList.get(rowIndex);
        return rowList.get(columnIndex);
    }

    /**
     * 设置参数指定表格中的元素对象值 aValue --表格被更新的对象值 row --表格对应的行索引值 column --表格对应的列索引值
     */
    public void setValueAt(Object aValue, int row, int column) {
        List rowList = (List) dataList.get(row);
        rowList.set(column, aValue);
        fireTableCellUpdated(row, column);
    }

    /**
     * 调整给定行间的数据的完整性，如果行数据的列数与定义的列数不等，将会适当的调整
     * 
     * @param from
     *            --起始行索引，包含该值
     * @param to
     *            --结束行索引，不包含该值
     */
    private void justifyRows(int from, int to) {

        for (int i = from; i < to; i++) {
            List rowList = (List) dataList.get(i);
            if (rowList == null) {
                rowList = Arrays.asList(new Object[getColumnCount()]);
                dataList.set(i, rowList);
            }

            int differCount = rowList.size() - getColumnCount();
            if (differCount > 0) //如果行数据的元素个数超出了定义的列值
            {
                rowList = rowList.subList(0, getColumnCount());
                dataList.set(i, rowList);
            } else if (differCount < 0) {
                differCount = -differCount;
                rowList.addAll(Arrays.asList(new Object[differCount]));
            }

        }
    }

    /**
     * Inserts a row at <code>row</code> in the model. The new row will
     * contain <code>null</code> values unless <code>rowData</code> is
     * specified. Notification of the row being added will be generated.
     * 
     * @param row
     *            the row index of the row to be inserted
     * @param rowData
     *            optional data of the row being added
     * @exception ArrayIndexOutOfBoundsException
     *                if the row was invalid
     */
    public void insertRow(int row, List rowData) {
        dataList.add(row, rowData);
        justifyRows(row, row + 1);
        fireTableRowsInserted(row, row);
    }
    public void insertRow(int row, Object[] rowData) {
        insertRow(row,convertToList(rowData));
    }
    public void insertRows(int row, Object[][] rowData) {
        insertRows(row,convertToList(rowData));
    }
    public void insertRows(int row, List rowData) {
        dataList.addAll(row, rowData);
        justifyRows(row, row + rowData.size());
        fireTableRowsInserted(row, row+rowData.size()-1);
    }
    /**
     * 将表控件的行数据重新设置为给定行数（rowCount）。 如果当前行大于给定行，将删除尾部多余的行数据；
     * 如果当前行小于给定行，在尾部添加相应的行，被添加的行数据为null。
     * 
     * @param rowCount
     *            --表模型新的行数
     */
    public void setRowCount(int rowCount) {
        int old = getRowCount();
        if (old == rowCount) {
            return;
        }

        if (rowCount <= old) {
            dataList = dataList.subList(0, rowCount);
            fireTableRowsDeleted(rowCount, old - 1);
        } else {
            dataList.addAll(Arrays.asList(new Object[rowCount - old]));

            justifyRows(old, rowCount);
            fireTableRowsInserted(old, rowCount - 1);
        }
    }
    /**
     * 将剪贴板上指定的信息粘贴在表控件中。
     * @param startRow  --粘贴的起始行
     * @param startColumn  --粘贴的起始列
     */
    public void batchPaste(int startRow,int startColumn,JTable table)
    {        
        if(startRow<0||startRow>getRowCount()||startColumn<0||startColumn>getColumnCount())
            return;
        
        batchPaste(ClipboardUtil.getStringContent(),startRow,startColumn,table);
        
    }
    /**
     * 在修改单元格值时，是否进行新值的校验
     * @return true:校验  false:不校验
     */
    public boolean isCheckValue()
    {
        return isValueCheck;
    }
    public void setCheckValue(boolean isValueCheck)
    {
        this.isValueCheck=isValueCheck;
    }
    /**
     * 将指定的信息粘贴在表控件中。
     * 1、列不能扩充
     * 2、行可以根据粘贴内容来进行相应的扩充
     * 3、给定内容（content）以符号‘\t\n\r\f’进行分割，获取元素值，并将元素值更新到相应的表格元素中
     * @param content  --被粘贴的内容
     * @param startRow   --粘贴的起始行
     * @param startColumn  --粘贴的起始列
     */
    protected void batchPaste(String content,int startRow,int startColumn,JTable table)
    {
	    if(content!=null)
	    {
	        
	        StringTokenizer stn=new StringTokenizer(content,"\t\n\r\f",true);
	        boolean flag=false;  //是否刚刚换过行
	        int rowPointer=startRow;
	        int columnPointer=startColumn;
	        while(stn.hasMoreTokens())
	        {
	            String tmp=stn.nextToken();
	            char firstChar=tmp.charAt(0);
	            switch(firstChar)
	            {
	              case '\n': 
	                  rowPointer++;
	                  flag=true;
	                  columnPointer=startColumn;
	                  break;
	              case '\t':
	                  columnPointer++;
	                  break;
	              case '\r':
	                  break;
	              case '\f':
	                  break;
	              default:
	                  if(columnPointer>=getColumnCount())  //如果列指针超出了最大列定义
	                  {
	                      continue;
	                  }else
	                  {
	                      if(flag)
	                      {
	                          flag=false;
	    	                  if(rowPointer>getRowCount()-1)
	    	                      dataList.add(EditorTableModel.getEmptyCell(getColumnCount())); //扩展一行
	                      }
	                      setValueNoNotify(tmp,rowPointer,table.convertColumnIndexToView(columnPointer));
	                  }
	            }
	        }
	        fireTableStructureChanged();
	    }
    }
    /**
     * 设置指定表格的元素对象值，但不通知监听处理。
     * @param aValue  --新对象值
     * @param row  --指定行索引
     * @param column  --指定列索引
     */
    void setValueNoNotify(Object aValue, int row, int column) {
        List rowList = (List) dataList.get(row);
        int size=rowList.size();
        EditeTableCell cell=(EditeTableCell)rowList.get(column);
        cell.setValue(aValue);
        rowList.set(column, cell);
    }
	/**
	 * 获取空行数据
	 * @param columnSize  --列数
	 * @return  --空行数据
	 */
	protected static List getEmptyCell(int columnSize)
	{
	    List list=new ArrayList();
	    for(int i=0;i<columnSize;i++)
	        list.add(new InsertTableCell("",false));
	    
	    return list;
	}
	/**
	 * 获取多行空数据
	 * @param rowSize  --行数
	 * @param columnSize  --列数
	 * @return  --多行空数据对象
	 */
	protected static List getEmptyCell(int rowSize,int columnSize)
	{
	    List list=new ArrayList();
	    
	    for(int i=0;i<rowSize;i++)
	        list.add(EditorTableModel.getEmptyCell(columnSize));
	    
	    return list;
	}
}
