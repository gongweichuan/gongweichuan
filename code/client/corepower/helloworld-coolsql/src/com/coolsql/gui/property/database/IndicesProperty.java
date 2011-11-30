/*
 * 创建日期 2006-9-14
 */
package com.coolsql.gui.property.database;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.gui.property.PropertyPane;
import com.coolsql.pub.display.CommonDataTable;
import com.coolsql.pub.display.TableCellObject;
import com.coolsql.pub.display.TableScrollPane;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.sql.model.Entity;
import com.coolsql.sql.model.Index;
import com.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.coolsql.view.bookmarkview.model.Identifier;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 索引、主键信息窗口
 */
public class IndicesProperty extends PropertyPane {

	private static final long serialVersionUID = 1L;
	private JPanel content;

    public IndicesProperty() {
        super();
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.gui.property.PropertyPane#initContent()
     */
    public JPanel initContent() {
        content = new JPanel();
        return content;
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.gui.property.PropertyInterface#set()
     */
    public boolean set() {
        return true;
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.gui.property.PropertyInterface#quit()
     */
    public void cancel() {

    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.gui.property.PropertyInterface#setData(java.lang.Object)
     */
    public void setData(Object ob) {
        if (ob == null)
            return;

        Identifier node = (Identifier) ob;
        Entity entity = (Entity) node.getDataObject();
        Bookmark bookmark = node.getBookmark();

        if (bookmark.isConnected()) //连接状态，初始化数据类型面板。
        {
            content.removeAll();
            if (entity.getType().equals("SEQUENCE"))
                return;
            content.setLayout(new BorderLayout());

            try {
                Index indeces[] = entity.getIndexes();

                //初始化头信息
                String[] header = new String[3];
                for (int i = 0; i < 3; i++) {
                    header[i] = header[i] = PublicResource
                            .getSQLString("sql.propertyset.index.column" + i);
                }

                //初始化数据
                TableCellObject[][] data = new TableCellObject[getDataRows(indeces)][3];
                int count = 0;
                for (int i = 0; i < indeces.length; i++) //对每个index进行处理
                {
                    for (int j = 0; j < indeces[i].getNumberOfColumns(); j++) {
                        if (indeces[i].getColumnName(j) == null
                                || indeces[i].getColumnName(j).trim()
                                        .equals("")) {
                            continue;
                        }
                        if (j == 0) {
                            data[count][0] = new TableCellObject(indeces[i]
                                    .getName(), null);
                        } else
                            data[count][0] = new TableCellObject("", null);

                        data[count][1] = new TableCellObject(indeces[i]
                                .getColumnName(j), BookMarkPubInfo
                                .getIconList()[BookMarkPubInfo.NODE_COLUMN]);
                        data[count][2] = new TableCellObject(indeces[i]
                                .isAscending(j) ? "Ascending" : "Descending",
                                null);
                        count++;
                    }
                }

                int[] render = { 1 };
                CommonDataTable table = new CommonDataTable(data, header,
                        render) {
					private static final long serialVersionUID = 1L;

					public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                content.add(new TableScrollPane(table), BorderLayout.CENTER);
                for (int i = 0; i < 3; i++) {
                    table.sizeColumnsToFit(i);
                }
            } catch (UnifyException e) {
                LogProxy.errorReport(e);
            } catch (SQLException e) {
                LogProxy.SQLErrorReport(e);
            }

        } else {
            content.removeAll();
            content.setLayout(new FlowLayout());
            content.add(new JLabel(PublicResource
                    .getSQLString("sql.propertyset.notconnect")));
        }

    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.gui.property.PropertyInterface#apply()
     */
    public void apply() {

    }

    /**
     * 计算实体所有index的列之和
     * 
     * @param indeces
     * @return
     */
    public int getDataRows(Index indeces[]) {
        int count = 0;
        for (int i = 0; i < indeces.length; i++) //对每个index进行处理
        {
            for (int j = 0; j < indeces[i].getNumberOfColumns(); j++) {
                if (indeces[i].getColumnName(j) == null
                        || indeces[i].getColumnName(j).trim().equals("")) {
                    continue;
                }
                count++;
            }
        }
        return count;
    }

    /* （非 Javadoc）
     * @see com.coolsql.gui.property.PropertyInterface#isNeedApply()
     */
    public boolean isNeedApply() {
        return false;
    }
}
