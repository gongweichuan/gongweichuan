/*
 * Created on 2007-4-28
 */
package com.coolsql.pub.display;

import java.awt.Component;

import javax.swing.JTable;

import com.coolsql.pub.component.FindProcess;

/**
 * @author liu_xlin table控件中查找匹配项的处理过程
 */
public class TableFindProcess implements FindProcess {

    /**
     * 当前匹配表格的行与列属性
     */
    private int row;

    private int column;

//    private boolean isFind = false;//是否找到匹配的表格
    private int count=0;  //记录当前循环查找的次数

    /**
     * 将数据调整正确
     * @param config
     * @param table
     */
    private void checkLoactionValidation(FindProcessConfig config, JTable table) {
        if (config.getForward() == FindProcessConfig.FORWARD) {
            if(row>=table.getRowCount()&&!config.isCircle())
                return ;
            row++;
            column++;
        }else
        {
            if(row<0&&!config.isCircle())
                return;
            row--;
            column--;
        }
        
        if (row > table.getRowCount() || row < -1) {
            if (config.getForward() == FindProcessConfig.FORWARD) {

                row = 0;
                column=0;

            }else
            {
                row = table.getRowCount()-1;
                column=table.getColumnCount()-1;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coolsql.pub.display.FindProcess#find(com.coolsql.data.display.FindProcessConfig,
     *      java.awt.Component)
     */
    public boolean find(FindProcessConfig config, Component com)
            throws Exception {
        JTable table = (JTable) com;
        
        /**
         * 如果表控件被选中，那么从选中的表格开始查找
         */
        row=table.getSelectedRow();
        column=table.getSelectedColumn();
        if(row==-1) //如果没有被选中,从头开始查找
        {
            row=0;
            column=0;
        }
        
        count=1;
        return findTable(config,table);
    }
    private boolean findTable(FindProcessConfig config,JTable table)
    {
        checkLoactionValidation(config,table);
        
        if (config.getForward() == FindProcessConfig.FORWARD) {
            
            for (; row < table.getRowCount(); row++) {
                count++;
                column=0;
                for (; column < table.getColumnCount(); column++) {
                    Object ob = table.getValueAt(row, column);
                    if (ob == null)
                        continue;
                    String str = ob.toString();
                    if (match(config, str)) {
                        table.changeSelection(row, column, false, false);
                        
                        return true;
                    }
                }
            }
        } else {
            for (; row > -1; row--) {
                count++;
                column=table.getColumnCount()-1;
                for (; column > -1; column--) {
                    Object ob = table.getValueAt(row, column);
                    if (ob == null)
                        continue;
                    String str = ob.toString();
                    if (match(config, str)) {
                        table.changeSelection(row, column, false, false);
                        
                        return true;
                    }
                }
            }
        }
       
        /**
         * 如果到达表控件的头或者尾，将位置值进行调整
         */
//        if (row == -1) //向后查找
//        {
//            row = table.getRowCount() - 1;
//            column = table.getColumnCount() - 1;
//        } else //向前查找
//        {
//            row = 0;
//            column = 0;
//        }
        /**
         * 如果没有找到，并且存在匹配的项，继续查找。
         */
        int tmp = count / table.getRowCount();
        if (tmp < 1) {  //从中途进行遍历
            if (config.isCircle()) {

                return findTable(config, table);
            }else
                return false;
        }else
        
            //如果不循环，并且没有找到，返回false
            return false;
    }
    /**
     * 对给定的表格元素值进行匹配
     * 
     * @param config
     *            --匹配选项
     * @param value
     *            --表格元素值
     * @return --true：匹配成功 false：匹配不成功
     */
    private boolean match(FindProcessConfig config, String value) {
        if (config.getCaseMatch() == FindProcessConfig.IGNORECASE) //如果忽略大小写
        {
            value = value.toLowerCase();
            config.setKeyWord(config.getKeyWord().toLowerCase());
        }
        if (config.getMatchMode() == FindProcessConfig.MATCH_FULL) {
            return value.equals(config.getKeyWord());
        } else
            return value.indexOf(config.getKeyWord()) > -1;
    }

    /**
     * @return Returns the column.
     */
    public int getColumn() {
        return column;
    }

    /**
     * @param column
     *            The column to set.
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * @return Returns the row.
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row
     *            The row to set.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /* (non-Javadoc)
     * @see com.coolsql.pub.display.FindProcess#resultInfo()
     */
    public String resultInfo() {
        return (row+1)+","+(column+1);
    }
}
