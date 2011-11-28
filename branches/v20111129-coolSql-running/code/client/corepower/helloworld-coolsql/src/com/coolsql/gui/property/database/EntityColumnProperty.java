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
import com.coolsql.pub.util.SqlUtil;
import com.coolsql.sql.model.Column;
import com.coolsql.sql.model.Entity;
import com.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.coolsql.view.bookmarkview.model.Identifier;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *表属性面板
 */
public class EntityColumnProperty extends PropertyPane {
	private static final long serialVersionUID = 1L;
	JPanel content;
    public EntityColumnProperty()
    {
        super();
    }
    /* （非 Javadoc）
     * @see com.coolsql.gui.property.PropertyPane#initContent()
     */
    public JPanel initContent() {
        content=new JPanel();
        
        return content;
    }

    /* （非 Javadoc）
     * @see com.coolsql.gui.property.PropertyInterface#set()
     */
    public boolean set() {
        return true;
    }

    /* （非 Javadoc）
     * @see com.coolsql.gui.property.PropertyInterface#quit()
     */
    public void cancel() {

    }

    /* （非 Javadoc）
     * @see com.coolsql.gui.property.PropertyInterface#setData(java.lang.Object)
     */
    public void setData(Object ob) {
        if(ob==null)
            return ;
        
        Identifier node=(Identifier)ob;
        Entity entity=(Entity)node.getDataObject();
        Bookmark bookmark=node.getBookmark();
        
        if(bookmark.isConnected())  //连接状态，初始化数据类型面板。
        {
            content.removeAll();
            if(entity.getType().equals(SqlUtil.SEQUENCE))
                return;
            content.setLayout(new BorderLayout());
            try {
               Column[] cols=entity.getColumns(); //获取表的所有列信息
                
                //初始化表头信息
                String[] header=new String[8];
                for(int i=0;i<header.length;i++)
                {
                    header[i]=PublicResource.getSQLString("sql.propertyset.entity.column"+i);
                }
                
                TableCellObject[][] data=new TableCellObject[cols.length][header.length];
                for(int i=0;i<cols.length;i++)
                {
                     int type=cols[i].isPrimaryKey()?BookMarkPubInfo.NODE_KEYCOLUMN:BookMarkPubInfo.NODE_COLUMN;
                     
                     data[i][0]=new TableCellObject(cols[i].getName(),BookMarkPubInfo.getIconList()[type]);
                     data[i][1]=new TableCellObject(cols[i].getTypeName());
                     data[i][2]=new TableCellObject(String.valueOf(cols[i].getSize()));
                     data[i][3]=new TableCellObject(String.valueOf(cols[i].getNumberOfFractionalDigits()));
                     data[i][4]=new TableCellObject(cols[i].isPrimaryKey()?"yes":"no");
                     data[i][5]=new TableCellObject(cols[i].isNullable()?"yes":"no");
                     data[i][6]=new TableCellObject(cols[i].getDefaultValue());
                     data[i][7]=new TableCellObject(cols[i].getRemarks());
                }
                
                int[] render={0};
                CommonDataTable table=new CommonDataTable(data,header,render)
                {
					private static final long serialVersionUID = 1L;

					public boolean isCellEditable(int row, int column)
                    {
                        return false;
                    }
                }
                ;
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                table.adjustPerfectWidth();
                content.add(new TableScrollPane(table),BorderLayout.CENTER);
                
            } catch (UnifyException e) {
                LogProxy.errorReport(e);
            } catch (SQLException e) {
                LogProxy.SQLErrorReport(e);
            }
        }else  //如果未连接数据库，只显示提示信息
        {
            content.removeAll();
            content.setLayout(new FlowLayout());
            content.add(new JLabel(PublicResource.getSQLString("sql.propertyset.notconnect")));
        }
    }

    /* （非 Javadoc）
     * @see com.coolsql.gui.property.PropertyInterface#apply()
     */
    public void apply() {

    }
    /* （非 Javadoc）
     * @see com.coolsql.gui.property.PropertyInterface#isNeedApply()
     */
    public boolean isNeedApply() {
        return false;
    }

}
