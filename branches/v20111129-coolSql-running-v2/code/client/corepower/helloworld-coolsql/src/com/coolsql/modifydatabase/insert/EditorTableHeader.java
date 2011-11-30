/*
 * Created on 2007-1-31
 */
package com.coolsql.modifydatabase.insert;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.coolsql.pub.component.BasicTableHeader;
import com.coolsql.sql.model.Column;

/**
 * @author liu_xlin 可编辑表控件的表头部分定义。重写了表头的渲染类。表头的列定义对象值为Column类型
 */
public class EditorTableHeader extends BasicTableHeader {

    private boolean isPressed; //鼠标按下标志

    private int columnIndex = -1; //鼠标按下的列索引位置

    private boolean isDragged; //鼠标是否拖动

    public EditorTableHeader() {
        super();
        setDefaultRenderer(new EditorTableHeaderRender());
        MouseProcess mouseProcess = new MouseProcess();
        addMouseListener(mouseProcess);
        addMouseMotionListener(mouseProcess);

        ToolTipManager.sharedInstance().registerComponent(this);
    }

    public String getToolTipText(MouseEvent e) {
        int col = columnAtPoint(e.getPoint());
        String retStr = null;
        if (col >= 0) {
            TableColumn tcol = getColumnModel().getColumn(col);
            Object value = tcol.getHeaderValue();
            if (value != null) //表头列对象不为空
            {
                if (value instanceof EditeTableHeaderCell) //如果对象不为结果集的列对象，不设置提示信息
                {
                    Object ho = ((EditeTableHeaderCell) value).getHeaderValue();
                    if (ho instanceof Column) {
                        Column columnInfo = (Column) ho;
                        retStr = getHtmlText(columnInfo);
                    }
                }
            }
        }
        return retStr;
    }

    /**
     * 生成提示信息，以html代码展示
     * 
     * @param type
     * @param size
     * @return
     */
    protected String getHtmlText(Column column) {
        String toolTip = "<html><body><table border=0>"
                + "<tr><td align=right>name:</td><td>" + column.getName() + "</td></tr>"
                + "<tr><td align=right>type:</td><td>" + column.getTypeName()
                + "</td></tr>" + "<tr><td align=right>size:</td><td>" + column.getSize()
                + "</td></tr>" + "<tr><td align=right>isPrimaryKey:</td><td>"
                + (column.isPrimaryKey() ? "Yes" : "No") + "</td></tr>"
                + "<tr><td align=right>isNullable:</td><td>"
                + (column.isNullable() ? "Yes" : "No") + "</td></tr>"
                + "</table></body></html>";
        return toolTip;
    }

    protected class EditorTableHeaderRender implements TableCellRenderer {

        private JPanel pane;

        private JCheckBox select = null; //当前列是否选种

        public EditorTableHeaderRender() {
            pane = new JPanel();
            pane.setLayout(new BorderLayout());
            select = new JCheckBox();
            pane.add(select, BorderLayout.CENTER);
            pane.setBorder(BorderFactory.createEtchedBorder());
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
         *      java.lang.Object, boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            if (value == null) {
                select.setSelected(false);
                select.setText("");
            } else {
                TableHeaderCell headerCell = (TableHeaderCell) value;
                select.setSelected(headerCell.getState());
                Object ob = headerCell.getHeaderValue();
                if (ob instanceof Column) {
                    Column col = (Column) ob;
                    if (col.isPrimaryKey())
                        select.setText(ob == null ? "" : "<html><body><u>"
                                + ob.toString() + "</u></body></html>");
                    else
                        select.setText(ob == null ? "" : ob.toString());
                } else
                    select.setText(ob == null ? "" : ob.toString());
            }
            return pane;
        }

    }

    protected class MouseProcess extends MouseAdapter implements
            MouseMotionListener {

        /**
         * 检查指定的坐标是否处在列（column）正在调整大小的范围内
         * 
         * @param p
         *            --坐标
         * @param column
         *            --列索引
         * @return --true：坐标处在可调整列宽的范围内 ，false：不再调整范围内
         */
        public boolean isResizingState(Point p, int column) {
            if (column == -1) {
                return true;
            }
            Rectangle r = getHeaderRect(column);
            r.grow(-3, 0);
            if (r.contains(p)) {
                return false;
            } else
                return true;
        }

        public void mousePressed(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                columnIndex = -1;
                return;
            }

            columnIndex = columnAtPoint(e.getPoint());
            if (!isResizingState(e.getPoint(), columnIndex)) { //在调整宽度范围内不允许修改列对象状态
                isPressed = true;
                repaint();
            } else {
                columnIndex = -1;
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (!isDragged && SwingUtilities.isLeftMouseButton(e)) {
                isDragged = false;
                int tmp = columnAtPoint(e.getPoint());
                if (columnIndex < 0 || tmp != columnIndex)
                    return;

                TableColumnModel model = getColumnModel();
                TableColumn column = model.getColumn(columnIndex);
                EditeTableHeaderCell headerCell = (EditeTableHeaderCell) column
                        .getHeaderValue();
                headerCell.setSelected(!headerCell.getState());
                repaint();
            }
            if (isDragged)
                isDragged = false;
            columnIndex = -1;
        }

        public void mouseDragged(MouseEvent e) {
            isDragged = true;
            if (isPressed) {
                isPressed = false;
                repaint();
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
         */
        public void mouseMoved(MouseEvent e) {

        }
    }
}
