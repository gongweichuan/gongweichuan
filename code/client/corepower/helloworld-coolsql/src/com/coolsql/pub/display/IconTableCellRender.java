/*
 * 创建日期 2006-8-20
 */
package com.coolsql.pub.display;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

/**
 * @author liu_xlin
 *表元素中包含了图标的展示渲染类
 */
public class IconTableCellRender extends BaseTableCellRender  {
    /**
     * 重写表格元素渲染方法
     */
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table,
                value, isSelected, hasFocus, row, column);
        if(value instanceof TableCellObject)
        {
            TableCellObject tmp=(TableCellObject)value;
            if(tmp.hasIcon())  //如果有图标，则给标签加上图标
                label.setIcon(tmp.getIcon());
        }
        return label;
    }
}
