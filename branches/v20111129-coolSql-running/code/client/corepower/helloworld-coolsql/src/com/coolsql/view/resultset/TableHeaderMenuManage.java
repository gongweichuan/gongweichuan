/*
 * 创建日期 2006-12-15
 */
package com.coolsql.view.resultset;

import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.coolsql.pub.component.BaseMenuManage;
import com.coolsql.pub.component.BasePopupMenu;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.sql.SQLResultSetResults;

/**
 * @author liu_xlin
 *表头组件的右键弹出菜单管理
 */
public class TableHeaderMenuManage extends BaseMenuManage {

    /**
     * 保存本次菜单弹出所在的位置
     */
    private Point point=null;
    /**
     * 复制列名菜单项
     */
    private JMenuItem copyColumnName=null;
    
    /**
     * @param com
     */
    public TableHeaderMenuManage(JTableHeader com) {
        super(com);

    }

    protected void createPopMenu()
    {
        if(popMenu==null)
        {
            popMenu=new BasePopupMenu();
            ActionListener copyNameAction=new ActionListener()
            {

                public void actionPerformed(ActionEvent e) {
                    JTableHeader tableHeader=(JTableHeader)getComponent();
                    int col = tableHeader.columnAtPoint(point);
                    String retStr = null;
                    if (col >= 0) {
                        TableColumn tcol = tableHeader.getColumnModel().getColumn(col);
                        int colWidth = tcol.getWidth();
                        TableCellRenderer h = tcol.getHeaderRenderer();
                        if (h == null)
                            h = tableHeader.getDefaultRenderer();
                        Component c = h.getTableCellRendererComponent(tableHeader.getTable(), tcol
                                .getHeaderValue(), false, false, -1, col);
                        Object value = tcol.getHeaderValue();
                        if (value != null) //表头列对象不为空
                        {

                            if (value instanceof SQLResultSetResults.Column) //如果对象不为结果集的列对象，不设置提示信息
                            {
                                SQLResultSetResults.Column columnInfo = (SQLResultSetResults.Column) value;
                                StringSelection ss = new StringSelection(StringUtil.trim(columnInfo.getName()));
                                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
                            }
                        }
                    }
                }
                
            };
            copyColumnName = createMenuItem(PublicResource
                    .getString("resultView.tableheader.popmenu.copyname"),null, copyNameAction);
            popMenu.add(copyColumnName);
        }
    }
    /* （非 Javadoc）
     * @see com.coolsql.pub.display.BaseMenuManage#itemSet()
     */
    public BasePopupMenu itemCheck() {
        if(popMenu==null)
            createPopMenu();
        return popMenu;
    }

    /* （非 Javadoc）
     * @see com.coolsql.pub.display.BaseMenuManage#getPopMenu()
     */
    public BasePopupMenu getPopMenu() {
        return itemCheck();
    }

    public Point getPoint() {
        return point;
    }
    public void setPoint(Point point) {
        this.point = point;
    }
}
