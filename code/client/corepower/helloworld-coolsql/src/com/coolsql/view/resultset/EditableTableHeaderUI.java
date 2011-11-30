/*
 * 创建日期 2006-11-6
 */
package com.coolsql.view.resultset;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.TableColumn;

import com.coolsql.pub.display.BaseTable;

/**
 * @author liu_xlin 基本的表头UI，主要用来限制表列的移动
 */
public class EditableTableHeaderUI extends BasicTableHeaderUI {

    public EditableTableHeaderUI() {
        super();
    }

    protected MouseInputListener createMouseInputListener() {
        return new EditableMouseInputHandler();
    }

    public class EditableMouseInputHandler extends MouseInputHandler {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Point p = e.getPoint();

                if(!SwingUtilities.isLeftMouseButton(e))//如果不是点击左键
                    return;
                // Firstly find which header cell was hit
                int index = header.columnAtPoint(p);
                TableColumn resizingColumn = getResizingColumn(p, index);
                if (resizingColumn != null) { //如果鼠标处于可调整宽度的位置，允许双击进行列宽的调整
                    BaseTable table = (BaseTable) header.getTable();
                    try {
                        table.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                        table.columnsToFitWidth(resizingColumn.getModelIndex());
                    } finally {
                        table.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            }
        }

        /**
         * 根据给定的坐标点，在指定的列范围内，获取出左边的列对象
         * 
         * @param p
         *            --给定的坐标点
         * @param column
         *            --列索引
         * @return --如果坐标点p在可调整列宽度的位置，返回左边列的列对象，否则返回null
         */
        private TableColumn getResizingColumn(Point p, int column) {
            if (column == -1) {
                return null;
            }
            Rectangle r = header.getHeaderRect(column);
            r.grow(-3, 0);
            if (r.contains(p)) {
                return null;
            }
            int midPoint = r.x + r.width / 2;
            int columnIndex;
            if (header.getComponentOrientation().isLeftToRight()) {
                columnIndex = (p.x < midPoint) ? column - 1 : column;
            } else {
                columnIndex = (p.x < midPoint) ? column : column - 1;
            }
            if (columnIndex == -1) {
                return null;
            }
            return header.getColumnModel().getColumn(columnIndex);
        }
    }
}
