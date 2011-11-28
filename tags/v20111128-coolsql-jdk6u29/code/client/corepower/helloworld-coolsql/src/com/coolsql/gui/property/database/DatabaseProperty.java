/*
 * 创建日期 2006-9-12
 */
package com.coolsql.gui.property.database;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.gui.property.PropertyInterface;
import com.coolsql.gui.property.PropertyPane;
import com.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin 数据库信息属性面板
 */
public class DatabaseProperty extends PropertyPane implements PropertyInterface {

    private Bookmark bookmark;

    private JLabel l;
    public DatabaseProperty() {
        super();

    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.gui.property.PropertyInterface#set()
     */
    public boolean set() {
        return true;
    }

    /* （非 Javadoc）
     * @see com.coolsql.gui.property.PropertyInterface#apply()
     */
    public void apply() {
       set();
        
    }
    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.gui.property.PropertyPane#initContent(java.lang.Object)
     */
    public JPanel initContent() {
        JPanel tmp = new JPanel();
        l=new JLabel("");
        tmp.add(l);
        return tmp;
    }
    
    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.gui.property.PropertyInterface#quit()
     */
    public void cancel() {

    }

    /* （非 Javadoc）
     * @see com.coolsql.gui.property.PropertyInterface#setData(java.lang.Object)
     */
    public void setData(Object ob) {
        if (ob == null)
            return ;

        Bookmark bookmark = (Bookmark) ob;
        if (bookmark.isConnected()) {
            setInfo(PublicResource
                    .getSQLString("sql.propertyset.dbproduct")
                    + bookmark.getDriver().getDbProductName()
                    + " "
                    + bookmark.getDriver().getDbPruductVersion());
        } else
            setInfo(PublicResource
                    .getSQLString("sql.propertyset.noversioninfo"));
        
    }
   public void setInfo(String txt)
   {
       l.setText(txt);
   }

/* （非 Javadoc）
 * @see com.coolsql.gui.property.PropertyInterface#isNeedApply()
 */
public boolean isNeedApply() {
    return false;
}
}
