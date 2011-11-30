package com.coolsql.sql.commonoperator;
/*
 * 创建日期 2006-9-12
 */
import java.sql.SQLException;

import javax.swing.Icon;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.gui.property.NodeKey;
import com.coolsql.gui.property.PropertyFrame;
import com.coolsql.gui.property.database.BookMarkProperty;
import com.coolsql.gui.property.database.DataTypesProperty;
import com.coolsql.gui.property.database.DatabaseProperty;
import com.coolsql.gui.property.database.DriverProperty;
import com.coolsql.gui.property.database.NumericFunctionsProperty;
import com.coolsql.gui.property.database.StringFunctionProperty;
import com.coolsql.gui.property.database.SystemFunctionProperty;
import com.coolsql.gui.property.database.TableTypeProperty;
import com.coolsql.gui.property.database.TimeDateFunctionProperty;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.parse.StringManager;
import com.coolsql.pub.parse.StringManagerFactory;

/**
 * @author liu_xlin
 *获取书签属性信息
 */
public class BookMarkPropertyOperator implements Operatable {
	
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(BookMarkPropertyOperator.class); 
	
    public BookMarkPropertyOperator()
    {
        
    }
    /* 获取书签的属性信息
     * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object)
     */
    public void operate(Object arg) throws UnifyException, SQLException {
        if(arg==null)
            throw new UnifyException("no operate object!");
        if(!(arg instanceof Bookmark))
        {
            throw new UnifyException("operate object error! class:"+arg.getClass());
        }
        Bookmark bookmark=(Bookmark)arg;
        
        PropertyFrame pf=new PropertyFrame(GUIUtil.getMainFrame());
        pf.setTitle(PublicResource.getString("propertyframe.bookmark.title"));
        
        NodeKey key=new NodeKey(PublicResource.getSQLString("db.propertyset.bookmark.safety"),(Icon)null,PropertyFrame.getRootData());
        key.setDisplayName(PublicResource.getSQLString("db.propertyset.bookmark.safety"));
        pf.addCard(key,BookMarkProperty.class,bookmark);
        pf.setDefaultCard(key);
        
        key=new NodeKey(PublicResource.getSQLString("db.propertyset.bookmark.driver"),(Icon)null,PropertyFrame.getRootData());
        key.setDisplayName(PublicResource.getSQLString("db.propertyset.bookmark.driver"));   
        pf.addCard(key,DriverProperty.class,bookmark);
        
        key=new NodeKey(PublicResource.getSQLString("db.propertyset.bookmark.dbversion"),(Icon)null,PropertyFrame.getRootData());
        key.setDisplayName(PublicResource.getSQLString("db.propertyset.bookmark.dbversion"));   
        pf.addCard(key,DatabaseProperty.class,bookmark);
        
        key=new NodeKey(PublicResource.getSQLString("db.propertyset.bookmark.datatype"),(Icon)null,PropertyFrame.getRootData());
        key.setDisplayName(PublicResource.getSQLString("db.propertyset.bookmark.datatype"));   
        pf.addCard(key,DataTypesProperty.class,bookmark);
        
        key=new NodeKey(stringMgr.getString("property.database.numericfunction.nodename"),(Icon)null,PropertyFrame.getRootData());
        key.setDisplayName(stringMgr.getString("property.database.numericfunction.desc"));   
        pf.addCard(key,NumericFunctionsProperty.class,bookmark);
        
        key=new NodeKey(stringMgr.getString("property.database.stringfunction.nodename"),(Icon)null,PropertyFrame.getRootData());
        key.setDisplayName(stringMgr.getString("property.database.stringfunction.desc"));   
        pf.addCard(key,StringFunctionProperty.class,bookmark);
        
        key=new NodeKey(stringMgr.getString("property.database.systemfunction.nodename"),(Icon)null,PropertyFrame.getRootData());
        key.setDisplayName(stringMgr.getString("property.database.systemfunction.desc"));   
        pf.addCard(key,SystemFunctionProperty.class,bookmark);
        
        key=new NodeKey(stringMgr.getString("property.database.timedatefunction.nodename"),(Icon)null,PropertyFrame.getRootData());
        key.setDisplayName(stringMgr.getString("property.database.timedatefunction.desc"));   
        pf.addCard(key,TimeDateFunctionProperty.class,bookmark);
        
        key=new NodeKey(stringMgr.getString("property.database.tabletype.nodename"),(Icon)null,PropertyFrame.getRootData());
        key.setDisplayName(stringMgr.getString("property.database.tabletype.desc"));   
        pf.addCard(key,TableTypeProperty.class,bookmark);
        
        pf.setVisible(true);
    }

    /* （非 Javadoc）
     * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object, java.lang.Object)
     */
    public void operate(Object arg0, Object arg1) throws UnifyException,
            SQLException {

    }

}
