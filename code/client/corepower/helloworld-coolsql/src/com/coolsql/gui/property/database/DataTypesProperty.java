/*
 * 创建日期 2006-9-12
 */
package com.coolsql.gui.property.database;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.gui.property.PropertyInterface;
import com.coolsql.gui.property.PropertyPane;
import com.coolsql.pub.display.CommonDataTable;
import com.coolsql.pub.display.TableScrollPane;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.sql.model.DataType;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *数据库数据类型信息
 */
public class DataTypesProperty extends PropertyPane implements PropertyInterface {

	private static final long serialVersionUID = 1L;
	JPanel content;
    public DataTypesProperty()
    {
        super();

    }
    /* （非 Javadoc）
     * @see com.coolsql.gui.property.PropertyInterface#set()
     */
    public boolean set() {
        return true;
    }
    /* （非 Javadoc）
     * @see com.coolsql.gui.property.PropertyPane#initContent(java.lang.Object)
     */
    public JPanel initContent() {
        content=new JPanel();        
        return content;
    }
    /* （非 Javadoc）
     * @see com.coolsql.gui.property.PropertyInterface#quit()
     */
    public void cancel() {
        set();
    }

    /* （非 Javadoc）
     * @see com.coolsql.gui.property.PropertyInterface#apply()
     */
    public void apply() {
        
    }
    /* （非 Javadoc）
     * @see com.coolsql.gui.property.PropertyInterface#setData(java.lang.Object)
     */
    public void setData(Object ob) {
        if(ob==null)
            return ;
        
        Bookmark bookmark=(Bookmark)ob;
        if(bookmark.isConnected())  //连接状态，初始化数据类型面板。
        {
            content.removeAll();
            content.setLayout(new BorderLayout());
            content.add(new JLabel(PublicResource.getSQLString("sql.propertyset.datatypes")),BorderLayout.NORTH);
            try {
                DataType[] dt=bookmark.getDbInfoProvider().getTypes();  //获取数据类型
                
                //初始化表头信息
                String[] header=new String[6];
                for(int i=0;i<6;i++)
                {
                    header[i]=PublicResource.getSQLString("sql.propertyset.column"+i);
                }
                
                String[][] data=new String[dt.length][6];
                for(int i=0;i<dt.length;i++)
                {

                     data[i][0]=dt[i].getDatabaseTypeName();
                     data[i][1]=String.valueOf(dt[i].getJavaType());
                     data[i][2]=String.valueOf(dt[i].getPrecision());
                     data[i][3]=dt[i].getLiteralPrefix();
                     data[i][4]=dt[i].getLiteralSuffix();
                     data[i][5]=dt[i].getCreateParameters();
                }
                
                CommonDataTable table=new CommonDataTable(data,header)
                {
					private static final long serialVersionUID = 1L;

					public boolean isCellEditable(int row, int column)
                    {
                        return false;
                    }
                }
                ;
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                content.add(new TableScrollPane(table),BorderLayout.CENTER);
                for(int i=0;i<6;i++)
                {
                    table.sizeColumnsToFit(i);
                }
            } catch (UnifyException e) {
                LogProxy.errorReport(e);
            } catch (SQLException e) {
                LogProxy.SQLErrorReport(e);
            }
        }else  //如果未连接数据库，只显示提示信息
        {
            content.removeAll();
            content.setLayout(new FlowLayout());
            content.add(new JLabel(PublicResource.getSQLString("sql.propertyset.nodatatypeinfo")));
        }
            
        
    }
    /* （非 Javadoc）
     * @see com.coolsql.gui.property.PropertyInterface#isNeedApply()
     */
    public boolean isNeedApply() {

        return false;
    }

}
