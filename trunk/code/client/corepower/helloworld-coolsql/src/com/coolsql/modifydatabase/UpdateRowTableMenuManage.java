/*
 * Created on 2007-1-24
 */
package com.coolsql.modifydatabase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

import com.coolsql.gui.property.database.ColumnProperty;
import com.coolsql.modifydatabase.model.BaseTableCell;
import com.coolsql.pub.component.BaseMenuManage;
import com.coolsql.pub.component.BasePopupMenu;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.sql.commonoperator.EntityPropertyOperator;
import com.coolsql.sql.commonoperator.Operatable;
import com.coolsql.sql.commonoperator.OperatorFactory;
import com.coolsql.sql.model.Column;
import com.coolsql.sql.model.Entity;
import com.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.coolsql.view.bookmarkview.model.Identifier;
import com.coolsql.view.bookmarkview.model.TableNode;
import com.coolsql.view.bookmarkview.model.ViewNode;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 更新行数据窗口中表控件的右键菜单管理器
 */
public class UpdateRowTableMenuManage extends BaseMenuManage {

    private JCheckBoxMenuItem asTermItem = null; //设置字段是否为查询条件的菜单

    private JCheckBoxMenuItem setAsNull = null; //是否设置为null值

    private JMenuItem detailColumnItem = null; //查看列的详细信息

    private JMenuItem detailEntityItem = null; //查看实体详细信息

    private UpdateRowDialog dialog = null;

    public UpdateRowTableMenuManage(UpdateRowTable table) {
        super(table);
        dialog = (UpdateRowDialog) GUIUtil.getUpParent(table,
                UpdateRowDialog.class);
    }

    protected void createPopMenu() {
        if (popMenu == null) {
            popMenu = new BasePopupMenu();

            //作为更新条件
            asTermItem = new JCheckBoxMenuItem(PublicResource
                    .getSQLString("rowupdate.popmenu.asterm.label"));
            asTermItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    UpdateRowTable table = (UpdateRowTable) getComponent();
                    BaseTableCell tableCell = (BaseTableCell) table.getValueAt(
                            table.getSelectedRow(), 1);//获取列名列元素值
                    tableCell.setAsTerm(asTermItem.getState());
                    table.setValueAt(tableCell, table.getSelectedRow(), 1);
                }

            });
            popMenu.add(asTermItem);

            //是否设置为null值菜单项
            setAsNull = new JCheckBoxMenuItem(PublicResource
                    .getSQLString("rowupdate.popmenu.setasnull.label"));
            setAsNull.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    UpdateRowTable table = (UpdateRowTable) getComponent();
                    BaseTableCell tableCell = (BaseTableCell) table.getValueAt(
                            table.getSelectedRow(), 3);//获取新值列元素值
                    tableCell.setIsNullValue(setAsNull.getState());
                    table.setValueAt(tableCell, table.getSelectedRow(), 3);

                }

            });
            popMenu.add(setAsNull);

            //查看列信息菜单项
            ActionListener columnPropertyListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    UpdateRowTable table = (UpdateRowTable) getComponent();

                    Column data = (Column) ((TableCell) table.getValueAt(table
                            .getSelectedRow(), 1)).getValue();//获取选中行所对应的列对象

                    ColumnProperty cp = null;
                    cp = new ColumnProperty(dialog, data, data
                            .getParentEntity().getBookmark());

                    cp.initData(data);
                    cp.setVisible(true);

                }

            };
            detailColumnItem = this.createMenuItem(PublicResource
                    .getSQLString("rowupdate.popmenu.detailcolumn.label"),
                    BookMarkPubInfo.getIconList()[BookMarkPubInfo.NODE_COLUMN],
                    columnPropertyListener);
            popMenu.add(detailColumnItem);

            //查看实体信息菜单项
            ActionListener entityPropertyListener = new ActionListener() {
                public void actionPerformed(ActionEvent e1) {
                    Entity entity = dialog.getEntityObject();
                    String type = entity.getType();
                    Identifier id = null;
                    if (type == "VIEW") {
                        id = new ViewNode(entity.getName(), entity
                                .getBookmark(), entity);
                    } else {
                        id = new TableNode(entity.getName(), entity
                                .getBookmark(), entity);
                    }

                    try {
                        Operatable operator = OperatorFactory
                                .getOperator(EntityPropertyOperator.class);
                        List list = new ArrayList();
                        list.add(id);
                        list.add(dialog);
                        operator.operate(list);
                    } catch (ClassNotFoundException e) {
                        LogProxy.errorReport(dialog, e);
                    } catch (InstantiationException e) {
                        LogProxy.internalError(e);
                    } catch (IllegalAccessException e) {
                        LogProxy.internalError(e);
                    } catch (UnifyException e) {
                        LogProxy.errorReport(dialog, e);
                    } catch (SQLException e) {
                        LogProxy.SQLErrorReport(dialog, e);
                    }
                }
            };
            detailEntityItem = this.createMenuItem(PublicResource
                    .getSQLString("rowupdate.popmenu.detailentity.label"),
                    BookMarkPubInfo.getIconList()[BookMarkPubInfo.NODE_TABLE],
                    entityPropertyListener);
            popMenu.add(detailEntityItem);
        }
    }

    /*
     * 主要校验菜单：asTermItem和detailColumnItem
     * 
     * @see com.coolsql.pub.display.BaseMenuManage#itemSet()
     */
    public BasePopupMenu itemCheck() {
        if (popMenu == null)
            createPopMenu();

        UpdateRowTable table = (UpdateRowTable) getComponent();
        int selectRow = table.getSelectedRow();
        if (selectRow < 0) {
            GUIUtil.setComponentEnabled(false, asTermItem);
            GUIUtil.setComponentEnabled(false, detailColumnItem);//
            GUIUtil.setComponentEnabled(false, setAsNull);
        } else {
            //是否作为更新条件
            GUIUtil.setComponentEnabled(true, asTermItem);
            TableCell cellValue = (TableCell) table.getValueAt(selectRow, 1);//获取选择行中第一列对应的元素对象值
            asTermItem.setSelected(cellValue.isAsTerm()); //根据元素当前的状态值，来设定菜单项的选中状态

            //是否为null
            cellValue = (TableCell) table.getValueAt(selectRow, 3);//获取选择行中第三列对应的元素对象值
            if (cellValue.isEditable())
                GUIUtil.setComponentEnabled(true, setAsNull);
            else
                GUIUtil.setComponentEnabled(false, setAsNull);           
            setAsNull.setSelected(cellValue.isNullValue()); //根据元素当前的状态值，来设定菜单项的选中状态

            //列相信信息
            GUIUtil.setComponentEnabled(true, detailColumnItem);
        }

        return popMenu;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coolsql.pub.display.BaseMenuManage#getPopMenu()
     */
    public BasePopupMenu getPopMenu() {
        return itemCheck();
    }
}
