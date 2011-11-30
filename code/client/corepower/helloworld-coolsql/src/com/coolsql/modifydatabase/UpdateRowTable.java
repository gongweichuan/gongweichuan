/*
 * Created on 2007-1-15
 */
package com.coolsql.modifydatabase;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.coolsql.modifydatabase.model.BaseTableCell;
import com.coolsql.modifydatabase.model.CheckBean;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.sql.model.Column;

/**
 * @author liu_xlin 创建更新语句的表控件
 */
public class UpdateRowTable extends JTable {

    /**
     * 表格元素的背景颜色，默认设置为浅绿色
     */
    protected static Color cellBackground = new Color(155, 236, 193, 200);

    public UpdateRowTable() {
        super();
        /**
         * 重写缺省表模型的方法，使第一列为复选框组件
         */
        DefaultTableModel model = new DefaultTableModel() {
            public Class getColumnClass(int col) {
                if (col == 0)
                    return java.lang.Boolean.class;
                else
                    return Object.class;
            }
        };
        setModel(model);
        JTableHeader header = this.getTableHeader();
        header.setReorderingAllowed(false); //不允许更换列
        setAutoResizeMode(AUTO_RESIZE_OFF);
        setAutoCreateColumnsFromModel(false);

        setColumnModel(createColumns());

        //        setRowSelectionAllowed(false);
        //        setColumnSelectionAllowed(true);
        //        setCellSelectionEnabled(true);
        setRowHeight(34);

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//只允许选择一行
    }

    /**
     * 创建列模型: 0:选择状态 1：列名称 2：原值 3：新值
     * 
     * @return --表控件的列模型
     */
    protected DefaultTableColumnModel createColumns() {
        DefaultTableColumnModel columnModel = (DefaultTableColumnModel) this
                .getColumnModel();

        TableColumn col = new TableColumn(0, 55, new IBooleanRenderer(),
                new IBooleanEditor());
        col.setHeaderValue(UpdateConstant.COLUMN_STATE);
        columnModel.addColumn(col);

        UpdateTableCellRender cellRender = new UpdateTableCellRender();

        col = new TableColumn(1, 100, cellRender, null); //字段名称列
        col.setHeaderValue(UpdateConstant.COLUMN_NAME);
        columnModel.addColumn(col);

        col = new TableColumn(2, 120, cellRender, null); //原值列
        col.setHeaderValue(UpdateConstant.COLUMN_OLDVALUE);
        columnModel.addColumn(col);

        UpdateTableCellEditor cellEditor = new UpdateTableCellEditor();
        col = new TableColumn(3, 180, cellRender, cellEditor); //新值列
        col.setHeaderValue(UpdateConstant.COLUMN_NEWVALUE);
        columnModel.addColumn(col);

        return columnModel;
    }

    /**
     * 重新定义表控件的提示信息
     */
    public String getToolTipText(MouseEvent event) {
        Point p = event.getPoint();
        int column = columnAtPoint(p);
        if (column == 0)
            return null;
        int row = rowAtPoint(p);
        TableCell tableCell = (TableCell) getValueAt(row, 1); //获取当前列所对应的列
        Column colInfo = (Column) tableCell.getValue();

        return getToolText(colInfo);
    }

    /**
     * 根据列对象,获取该列对象的简要描述,该描述以静态html格式展示
     * 
     * @param col
     *            --需要被描述的列对象
     * @return --对列对象的简要描述.
     */
    private String getToolText(Column col) {
        return "<html><body><table><tr><th align=right>name:</th><td>"
                + col.getName() + "</td></tr>"
                + "<tr><th align=right>size:</th><td>" + col.getSize()
                + "</td></tr>" + "<tr><th align=right>type:</th><td>"
                + col.getTypeName() + "</td></tr>"
                + "<tr><th align=right>isPrimarykey:</th><td>"
                + (col.isPrimaryKey() ? "yes" : "no") + "</td></tr>"
                + "</table></body></htm>";
    }

    /**
     * 获取表头标识向量对象数据
     * 
     * @return --表头标示
     */
    protected Vector getHeaderData() {
        Vector v = new Vector();
        v.add(UpdateConstant.COLUMN_STATE);
        v.add(UpdateConstant.COLUMN_NAME);
        v.add(UpdateConstant.COLUMN_OLDVALUE);
        v.add(UpdateConstant.COLUMN_NEWVALUE);
        return v;
    }

    /**
     * 将数据进行更新替换
     * 
     * @param data
     *            --新数据向量
     */
    public void replaceData(Vector data) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.setDataVector(data, getHeaderData());
    }

    /**
     * 重写编辑方法。只允许第一列（选择状态）和第四列（新值）进行编辑
     */
    public boolean isCellEditable(int row, int column) {
        if (column == 0) {
            CheckBean bean = (CheckBean) getValueAt(row, column);
            return bean.isEnable();
        } else {
            TableCell cell = (TableCell) getValueAt(row, column);

            if (!cell.isEditable() && cell.isNullValue() && column == 3) {
                int result = JOptionPane.showConfirmDialog(this, PublicResource
                        .getSQLString("rowupdate.table.ismodifynull"),
                        "confirm", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    ((BaseTableCell) cell).setEditable(true);
                }
            }
            return cell.isEditable();
        }
    }

    /**
     * 设置指定行中的相关表格值得背景颜色
     * 
     * @param row
     *            --指定行
     * @param color
     *            --需要设定的颜色
     */
    protected void setRowBackgroundColor(int row, Color color) {
        BaseTableCell cell = (BaseTableCell) UpdateRowTable.this.getValueAt(
                row, 1);
        cell.setRenderOfBackground(color);
        cell = (BaseTableCell) UpdateRowTable.this.getValueAt(row, 2);
        cell.setRenderOfBackground(color);
        Object ob = UpdateRowTable.this.getValueAt(row, 3);
        cell = (BaseTableCell) UpdateRowTable.this.getValueAt(row, 3);
        cell.setRenderOfBackground(color);
    }

    /**
     * 
     * @author liu_xlin 复选框的渲染类，应用更新表格的第一列
     */
    protected class IBooleanRenderer extends JCheckBox implements
            TableCellRenderer {
        public IBooleanRenderer() {
            super();
            setHorizontalAlignment(JLabel.CENTER);
        }

        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            setBackground(null); //使第一列无背景色
            if (value != null && value instanceof CheckBean) {
                CheckBean bean = (CheckBean) value;
                setSelected(bean.getSelectValue().booleanValue());
            } else
                setSelected(false);
            return this;
        }
    }

    /**
     * 
     * @author liu_xlin 复选框编辑元素类的重定义,增加了编辑组件可用性的判断
     */
    protected class IBooleanEditor extends DefaultCellEditor {
        private CheckBean currentBean = null;

        public IBooleanEditor() {
            super(new JCheckBox());

            JCheckBox checkBox = (JCheckBox) getComponent();
            checkBox.setHorizontalAlignment(JCheckBox.CENTER);
        }

        /** Implements the <code>TableCellEditor</code> interface. */
        public Component getTableCellEditorComponent(JTable table,
                Object value, boolean isSelected, int row, int column) {
            CheckBean bean = (CheckBean) value;
            currentBean = bean;

            Component com = super.getTableCellEditorComponent(table, bean
                    .getSelectValue(), isSelected, row, column);
            GUIUtil.setComponentEnabled(bean.isEnable(), (JComponent) com);
            return com;
        }

        public Object getCellEditorValue() {
            if (currentBean != null)
                currentBean
                        .setSelectValue((Boolean) super.getCellEditorValue());
            return currentBean;
        }
    }

    public interface UpdateConstant {
        public static final String COLUMN_STATE = PublicResource
                .getSQLString("rowupdate.table.column.state"); //选中状态列

        public static final String COLUMN_NAME = PublicResource
                .getSQLString("rowupdate.table.column.columname"); //列名

        public static final String COLUMN_OLDVALUE = PublicResource
                .getSQLString("rowupdate.table.column.oldvalue"); //原值

        public static final String COLUMN_NEWVALUE = PublicResource
                .getSQLString("rowupdate.table.column.newvalue"); //新值
    }

}
